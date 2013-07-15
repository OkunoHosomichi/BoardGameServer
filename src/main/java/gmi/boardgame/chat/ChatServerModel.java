package gmi.boardgame.chat;

import gmi.boardgame.chat.commands.ChatCommandChainFactory;
import gmi.boardgame.chat.commands.ChatCommandContext;
import gmi.utils.chain.NoSuchCommandException;
import gmi.utils.netty.channel.ChannelUtilities;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkArgument;
import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * チャットサーバに必要な処理を行い、必要に応じてビューに更新を通知します。MVCパターンでいうところのモデル部分のつもりです。
 * あくまでもチャットに必要な部分だけなので、ボードゲーム部分の処理は別のモデルを作って使います。
 * 
 * @author おくのほそみち
 */
final class ChatServerModel extends Observable implements ChatModel {
  /**
   * 行の区切り文字。
   */
  private static final String LINE_SEPARATOR = System.lineSeparator();
  /**
   * 接続したクライアントのグループ。
   */
  private final ChannelGroup fClients;
  /**
   * fClientsを読み書きするときに使うロックオブジェクト。単純なアクセスなのでReentrantReadWriteLockは使うまでもなさそう。
   */
  private final Object fClientsLock;
  /**
   * サーバ情報を保持します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   */
  private String fMessage;

  /**
   * インスタンスを構築します。
   */
  @Inject
  public ChatServerModel() {
    fClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    fClientsLock = new Object();
    fMessage = "";
  }

  @Override
  public List<String> getClientNames() {
    final List<String> result = new ArrayList<>();

    synchronized (fClientsLock) {
      for (final Channel channel : fClients) {
        result.add(ChannelUtilities.getNickName(channel));
      }
    }

    Collections.sort(result);
    return Collections.unmodifiableList(result);
  }

  @Override
  public String getInformation() {
    assert fMessage != null;

    return fMessage;
  }

  @Override
  public void processByeCommand(Channel client) throws IllegalArgumentException {
    checkNotNullArgument(client, "client");

    client.close();
  }

  /**
   * チャットに参加しているクライアントから受信したコマンドを処理します。コマンドが空文字列の場合は何もしません。現時点でのコマンドは以下の通りです。<br>
   * MSG メッセージ - 他クライアントにメッセージを送信する。<br>
   * BYE - クライアントとの切断処理を行う。
   */
  @Override
  public void processClientCommand(Channel client, String command) throws IllegalArgumentException {
    // INFO: コマンドが変更された場合にコメント等きちんと修正する。
    // TODO: 書き方がわかったらテストを書く。
    checkNotNullArgument(client, "client");
    checkNotNullArgument(command, "command");
    if (command.isEmpty()) return;
    if (command.indexOf(' ') == 0) throw new IllegalArgumentException("commandが不正です。");

    try {
      executeCommandChain(client, command);
    } catch (final NoSuchCommandException ex) {
      updateInformation("未定義のコマンド: " + client.localAddress() + "から送信(" + command + ")");
    }
  }

  @Override
  public void processMessageCommand(Channel client, String message) throws IllegalArgumentException {
    checkNotNullArgument(client, "client");
    checkNotNullArgument(message, "message");
    if (message.isEmpty()) return;

    synchronized (fClientsLock) {
      for (final Channel channel : fClients) {
        if (channel == client) continue;

        channel.write("[" + ChannelUtilities.getNickName(client) + "] " + message + "\n");
      }
    }
  }

  @Override
  public void processNameCommand(Channel client, String nickName) throws IllegalArgumentException {
    checkNotNullArgument(client, "client");
    checkNotNullArgument(nickName, "nickName");
    checkArgument(!nickName.isEmpty(), "nickNameに空文字列を指定できません。");

    if (!checkValidName(nickName)) {
      client.write("RENAME " + nickName + "\n");
      return;
    }

    ChannelUtilities.setNickName(client, nickName);

    synchronized (fClientsLock) {
      final String result = getAllClientName();

      fClients.add(client);
      client.closeFuture().addListener(new ChannelFutureListener() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
          leaveClient();
        }
      });

      for (final Channel channel : fClients) {
        if (client == channel) {
          channel.write("Welcome to Chat!\n");
          channel.write("It is " + new Date() + " now.\n");
          channel.write("WELCOME " + result + "\n");
        } else {
          channel.write("ENTER " + nickName + "\n");
        }
      }

      setChanged();
      notifyObservers("clients");
    }
  }

  /**
   * 接続しているクライアント名全てをカンマで連結した文字列を返します。接続しているクライアントがなければ空文字列が返ります。
   * 
   * @return クライアント名をカンマで連結した文字列。
   */
  private String getAllClientName() {
    synchronized (fClientsLock) {
      if (fClients.size() == 0) return "";

      final List<String> list = getClientNames();
      final StringBuilder result = new StringBuilder();
      for (final String nickName : list) {
        result.append(",").append(nickName);
      }
      return result.substring(1);
    }
  }

  /**
   * 指定されたニックネームが不正なものでないか調べます。不正な名前の条件は以下の通りです。<br>
   * ･名前にServerという文字列が含まれている<br>
   * ･半角記号のカンマ、スペース、アットマークなどが含まれている<br>
   * 
   * @param nickName
   *          調べるニックネーム。nullや空文字を指定できません。
   * @return 不正な名前でなければtrue、不正ならばfalse。
   */
  private boolean checkValidName(String nickName) {
    assert nickName != null;
    assert !nickName.isEmpty();

    synchronized (fClientsLock) {
      for (final Channel client : fClients) {
        if (ChannelUtilities.getNickName(client).equals(nickName)) return false;
      }
    }

    return !(nickName.indexOf("Server") != -1 || nickName.indexOf(",") != -1 || nickName.indexOf(" ") != -1 || nickName
        .indexOf("@") != -1);
  }

  @Override
  public void sendServerMessage(String message) throws IllegalArgumentException {
    checkNotNullArgument(message, "message");
    if (message.isEmpty()) return;

    final String sendMsg = "<Server> " + message + "\n";
    synchronized (fClientsLock) {
      for (final Channel c : fClients) {
        c.write(sendMsg);
      }
    }
  }

  @Override
  public void updateInformation(String info) throws IllegalArgumentException {
    checkNotNullArgument(info, "info");

    appendInformation(info);
  }

  /**
   * 指定されたサーバ情報を追加し、変更をビューに通知します。サーバ情報の末尾には自動で改行コードが追加されます。
   * messageにnullを指定した場合はassertによりエラーが発生します。 空文字列が指定された場合は何もせず、ビューへの通知も行いません。
   * 
   * @param info
   *          追加するサーバ情報。nullを指定できません。
   */
  private void appendInformation(String info) {
    assert info != null;
    if (info.isEmpty()) return;

    fMessage = fMessage + info + LINE_SEPARATOR;

    setChanged();
    notifyObservers("info");
  }

  /**
   * コマンド連鎖を実行します。
   * 
   * @param client
   *          コマンドを送信したクライアント。nullを指定できません。
   * @param command
   *          コマンド文字列。nullを指定できません。
   * @throws NoSuchCommandException
   *           コマンドが見つからず実行できなかった場合。
   */
  private void executeCommandChain(Channel client, String command) throws NoSuchCommandException {
    assert client != null;
    assert command != null;
    assert !command.isEmpty();
    assert command.indexOf(' ') != 0;

    final String[] parseCommand = command.split(" ", 2);
    ChatCommandChainFactory.INSTANCE.getChain().execute(
        new ChatCommandContext(client, parseCommand[0], parseCommand.length == 2 ? parseCommand[1] : "", this));
  }

  /**
   * クライアントが切断した場合の処理を行い、ビューに変更を通知します。
   */
  private void leaveClient() {
    setChanged();
    notifyObservers("clients");
  }
}
