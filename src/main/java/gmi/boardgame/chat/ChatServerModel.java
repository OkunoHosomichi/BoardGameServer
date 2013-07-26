package gmi.boardgame.chat;

import gmi.boardgame.chat.commands.ChatCommandChainFactory;
import gmi.boardgame.chat.commands.ChatCommandContext;
import gmi.utils.netty.channel.ChannelUtilities;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static gmi.utils.Preconditions.checkNotEmptyArgument;
import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * チャットサーバに必要な処理を行い、必要に応じてビューに更新を通知します。MVCパターンでいうところのモデル部分のつもりです。
 * あくまでもチャットに必要な部分だけなので、ボードゲーム部分の処理は別のモデルを作って使います。
 * 
 * @author おくのほそみち
 */
// FIXME:クラスが大きいので分割するべきだと思う。
final class ChatServerModel extends Observable implements ChatModel {
  /**
   * 行の区切り文字。
   */
  private static final String LINE_SEPARATOR = System.lineSeparator();
  /**
   * 接続したクライアントのグループ。
   */
  private final ChannelGroup fClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  /**
   * fClientsを読み書きするときに使うロックオブジェクト。単純なアクセスなのでReentrantReadWriteLockは使うまでもなさそう。
   */
  private final Object fClientsLock = new Object();
  /**
   * サーバ情報を保持します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   */
  private volatile String fMessage = "";

  /**
   * チャットに参加しているクライアントの名前一覧を返します。一覧はソートされていて変更不能のビューになっています。
   * 
   * @return クライアントの名前一覧。nullではありません。
   */
  @Override
  public List<String> getClientNames() {
    synchronized (fClientsLock) {
      assert fClients != null;

      final List<String> result = new ArrayList<>(fClients.size());

      for (final Channel channel : fClients) {
        result.add(ChannelUtilities.getNickName(channel));
      }

      Collections.sort(result);
      return Collections.unmodifiableList(result);
    }
  }

  /**
   * サーバ情報を返します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   * 
   * @return サーバ情報。nullではありません。
   */
  @Override
  public String getInformation() {
    assert fMessage != null;

    return fMessage;
  }

  /**
   * Byeコマンドの処理を行います。指定されたクライアントの切断処理を実行します。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。nullを指定できません。
   * @throws IllegalArgumentException
   *           clientがnullの場合。
   */
  @Override
  public void processByeCommand(@Nonnull Channel client) {
    checkNotNullArgument(client, "client");

    client.close();
  }

  /**
   * チャットに参加しているクライアントから受信したコマンドを処理します。現時点でのコマンドは以下の通りです。<br>
   * MSG メッセージ - 他クライアントにメッセージを送信する。<br>
   * BYE - クライアントとの切断処理を行う。<br>
   * NAME ニックネーム - ニックネームを決める。このコマンドを処理するとチャットに参加したことになる。
   * 
   * @param client
   *          コマンドを送信したクライアント。nullを指定できません。
   * @param command
   *          コマンド文字列。null又は空文字列を指定できません。
   * @throws IllegalArgumentException
   *           client又はcommandがnullの場合。又はcommandが空文字列の場合。
   * @return コマンドを処理したならtrue、処理しなかったならfalse。
   */
  @Override
  public boolean processClientCommand(@Nonnull Channel client, @Nonnull String command) {
    // INFO: コマンドが変更された場合にコメント等きちんと修正する。
    checkNotNullArgument(client, "client");
    checkNotNullArgument(command, "command");
    checkNotEmptyArgument(command, "command");
    checkArgument(command.indexOf(' ') != 0, "引数commandが不正です。");

    return executeCommandChain(client, command);
  }

  /**
   * Messageコマンドを処理します。コマンドを送信してきたクライアント以外の全クライアントにメッセージを送信します。
   * メッセージが空文字列の場合は送信しません。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。nullを指定できません。
   * @param message
   *          送信するメッセージ。nullを指定できません。
   * @throws IllegalArgumentException
   *           client又はmessageがnullの場合。
   */
  @Override
  public void processMessageCommand(@Nonnull Channel client, @Nonnull String message) {
    checkNotNullArgument(client, "client");
    checkNotNullArgument(message, "message");
    if (message.isEmpty()) return;

    synchronized (fClientsLock) {
      for (final Channel channel : fClients) {
        if (channel == client) continue;

        channel.write(createMessageCommandMessage(ChannelUtilities.getNickName(client), message));
      }
    }
  }

  /**
   * Nameコマンドを処理します。チャットに参加しているクライアントの一覧に登録し、
   * コマンドを送信してきたクライアント以外の全クライアントに参加したことを通知します。その後、ビューにクライアントの一覧が変更されたことを通知します。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。nullを指定できません。
   * @param nickName
   *          ニックネーム。nullや空文字列を指定できません。
   * @throws IllegalArgumentException
   *           client又はnickNameがnullの場合。又はnickNameが空文字列の場合。
   */
  @Override
  public void processNameCommand(@Nonnull Channel client, @Nonnull String nickName) {
    checkNotNullArgument(client, "client");
    checkNotNullArgument(nickName, "nickName");
    checkNotEmptyArgument(nickName, "nickName");

    if (!checkValidName(nickName)) {
      client.write(createRenameCommandMessage(nickName));
      return;
    }

    synchronized (fClientsLock) {
      registerClient(client, nickName);

      for (final Channel channel : fClients) {
        if (client == channel) {
          channel.write(createWelcomeCommandMessage(getClientNames()));
        } else {
          channel.write(createEnterCommandMessage(nickName));
        }
      }
      setChanged();
      notifyObservers("clients");
    }
  }

  /**
   * サーバからのメッセージをチャットに参加している全クライアントに送信します。メッセージが空文字列の場合は何もしません。
   * 
   * @param message
   *          送信するメッセージ。nullを指定できません。
   * @throws IllegalArgumentException
   *           messageがnullの場合。
   */
  @Override
  public void sendServerMessage(@Nonnull String message) {
    checkNotNullArgument(message, "message");
    if (message.isEmpty()) return;

    synchronized (fClientsLock) {
      for (final Channel c : fClients) {
        c.write(createServerMessageCommandMessage(message));
      }
    }
  }

  /**
   * サーバ情報を更新してビューに通知します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   * 空文字列が指定された場合は何もせず、ビューへの更新通知も行いません。
   * 
   * @param message
   *          通知するメッセージ。nullを指定できません。
   * @throws IllegalArgumentException
   *           messageがnullの場合。
   */
  @Override
  public void updateInformation(@Nonnull String info) {
    checkNotNullArgument(info, "info");
    if (info.isEmpty()) return;

    appendInformation(info);
  }

  /**
   * 指定されたサーバ情報を追加し、変更をビューに通知します。サーバ情報の末尾には自動で改行コードが追加されます。
   * 
   * @param info
   *          追加するサーバ情報。null又は空文字列を指定できません。
   */
  private void appendInformation(@Nonnull String info) {
    assert info != null;
    assert !info.isEmpty();
    assert fMessage != null;

    fMessage = fMessage + info + LINE_SEPARATOR;

    setChanged();
    notifyObservers("info");
  }

  /**
   * 指定されたニックネームが不正なものでないか調べます。不正な名前の条件は以下の通りです。<br>
   * ･すでに参加しているクライアントと同じニックネームが指定された。<br>
   * ･名前にServerという文字列が含まれている。<br>
   * ･半角記号のカンマ、スペース、アットマークなどが含まれている。
   * 
   * @param nickName
   *          調べるニックネーム。null又は空文字列を指定できません。
   * @return 不正な名前でなければtrue、不正ならばfalse。
   */
  private boolean checkValidName(String nickName) {
    assert nickName != null;
    assert !nickName.isEmpty();

    synchronized (fClientsLock) {
      assert fClientsLock != null;

      for (final Channel client : fClients) {
        if (ChannelUtilities.getNickName(client).equals(nickName)) return false;
      }

      return !(nickName.indexOf("Server") != -1 || nickName.indexOf(",") != -1 || nickName.indexOf(" ") != -1 || nickName
          .indexOf("@") != -1);
    }
  }

  /**
   * 指定されたニックネーム一覧に空文字列が含まれているかどうかを返します。
   * 
   * @param nickNames
   *          ニックネームの一覧。nullを指定できません。
   * @return 要素に空文字列が含まれていればtrue、含まれていなければfalse。
   */
  private boolean containsEmptyName(@Nonnull List<String> nickNames) {
    assert nickNames != null;

    for (final String nickName : nickNames) {
      if (nickName.isEmpty()) return true;
    }

    return false;
  }

  /**
   * 指定されたニックネーム一覧にnullが含まれているかどうかを返します。
   * 
   * @param nickNames
   *          ニックネームの一覧。nullを指定できません。
   * @return 要素にnullが含まれていればtrue、含まれていなければfalse。
   */
  private boolean containsNullName(@Nonnull List<String> nickNames) {
    assert nickNames != null;

    for (final String nickName : nickNames) {
      if (nickName == null || nickName.isEmpty()) return true;
    }

    return false;
  }

  /**
   * 指定されたコマンド文字列と引数文字列のリストからコマンド送信用の文字列を作って返します。
   * コマンド文字列に"コマンド"、引数文字列のリストに["引数1",null,"引数2"]を指定した場合、「コマンド 引数1,,引数2\n」が返ります。
   * 引数文字列リスト内にnullがある場合、空文字列として扱われます。
   * 
   * @param command
   *          コマンド文字列。null又は空文字列を指定できません。
   * @param arguments
   *          引数文字列のリスト。nullを指定できません。
   * @return コマンド送信用の文字列。nullではありません。
   */
  private String createCommandMessage(@Nonnull String command, @Nonnull List<String> arguments) {
    assert command != null;
    assert arguments != null;
    assert !command.isEmpty();

    return command + " " + StringUtils.join(arguments, ",") + '\n';
  }

  /**
   * 指定されたコマンド文字列と引数文字列の配列からコマンド送信用の文字列を作って返します。
   * コマンド文字列に"コマンド"、引数文字列の配列に["引数1",null,"引数2"]を指定した場合、「コマンド 引数1,,引数2\n」が返ります。
   * 引数文字列配列内にnullがある場合、空文字列として扱われます。
   * 
   * @param command
   *          コマンド文字列。null又は空文字列を指定できません。
   * @param arguments
   *          引数文字列の配列。可変長引数として使えます。nullを指定できません。
   * @return コマンド送信用の文字列。nullではありません。
   */
  private String createCommandMessage(@Nonnull String command, @Nonnull String... arguments) {
    assert command != null;
    assert arguments != null;
    assert !command.isEmpty();

    return createCommandMessage(command, Arrays.asList(arguments));
  }

  /**
   * 指定されたニックネームからEnterコマンド送信用の文字列を作って返します。
   * 
   * @param nickName
   *          ニックネーム。null又は空文字列を指定できません。
   * @return Enterコマンド送信用の文字列。nullではありません。
   */
  private String createEnterCommandMessage(@Nonnull String nickName) {
    assert nickName != null;
    assert !nickName.isEmpty();

    return createCommandMessage("ENTER", nickName);
  }

  /**
   * 指定されたニックネームと文字列からMessageコマンド送信用の文字列を作って返します。
   * 
   * @param nickName
   *          ニックネーム。null又は空文字列を指定できません。
   * @param message
   *          送信するメッセージ。null又は空文字列を指定できません。
   * @return Messageコマンド送信用の文字列。nullではありません。
   */
  private String createMessageCommandMessage(String nickName, String message) {
    assert nickName != null;
    assert message != null;
    assert !nickName.isEmpty();
    assert !message.isEmpty();

    return createCommandMessage("MSG", "[" + nickName + "] " + message);
  }

  /**
   * 指定されたニックネームからRenameコマンド送信用の文字列を作って返します。
   * 
   * @param nickName
   *          ニックネーム。null又は空文字列を指定できません。
   * @return Renameコマンド送信用の文字列。nullではありません。
   */
  private String createRenameCommandMessage(@Nonnull String nickName) {
    assert nickName != null;
    assert !nickName.isEmpty();

    return createCommandMessage("RENAME", nickName);
  }

  /**
   * 指定された文字列からサーバからのメッセージ送信用の文字列を作って返します。
   * 実際にはServerというニックネームでMessageコマンドの文字列を作成します。
   * 
   * @param message
   *          送信するメッセージ。null又は空文字列を指定できません。
   * @return サーバからのメッセージ送信用の文字列。nullではありません。
   */
  private String createServerMessageCommandMessage(@Nonnull String message) {
    assert message != null;
    assert !message.isEmpty();

    return createMessageCommandMessage("Server", message);
  }

  /**
   * 指定されたニックネームのリストからWelcomeコマンド送信用の文字列を作って返します。
   * ニックネームのリスト内にnullや空文字列が含まれていてはいけません。
   * 
   * @param nickNames
   *          チャットに参加しているクライアントのニックネーム一覧。NAMEコマンドを送信してきたクライアント自身のニックネームも含みます。
   *          null又を指定できません。
   * @return Welcomeコマンド送信用の文字列。nullではありません。
   */
  private String createWelcomeCommandMessage(@Nonnull List<String> nickNames) {
    assert nickNames != null;
    assert !containsNullName(nickNames);
    assert !containsEmptyName(nickNames);

    return createCommandMessage("WELCOME", nickNames) + createServerMessageCommandMessage("Welcome to Chat!")
        + createServerMessageCommandMessage("It is " + new Date().toString() + " now.");
  }

  /**
   * コマンド連鎖を実行します。
   * 
   * @param client
   *          コマンドを送信したクライアント。nullを指定できません。
   * @param command
   *          コマンド文字列。null又は空文字列を指定できません。
   * @return コマンドを処理したならtrue、処理しなかったならfalse。
   */
  private boolean executeCommandChain(Channel client, String command) {
    assert client != null;
    assert command != null;
    assert !command.isEmpty();
    assert command.indexOf(' ') != 0;

    final String[] parseCommand = command.split(" ", 2);

    return ChatCommandChainFactory.INSTANCE.getChain().execute(
        new ChatCommandContext(client, parseCommand[0], parseCommand.length == 2 ? parseCommand[1] : "", this));
  }

  /**
   * クライアントが切断した場合の処理を行い、ビューに変更を通知します。
   */
  private void leaveClient() {
    setChanged();
    notifyObservers("clients");
  }

  /**
   * 参加しているクライアント一覧に指定されたチャンネルを追加し、チャンネルが閉じられたときの処理を追加します。
   * 
   * @param client
   *          登録するクライアント。nullを指定できません。
   * @param nickName
   *          ニックネーム。null又は空文字列を指定できません。
   */
  private void registerClient(@Nonnull Channel client, @Nonnull String nickName) {
    assert client != null;
    assert nickName != null;
    assert !nickName.isEmpty();

    ChannelUtilities.setNickName(client, nickName);

    synchronized (fClientsLock) {
      assert fClients != null;

      fClients.add(client);
      client.closeFuture().addListener(new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
          leaveClient();
        }
      });
    }
  }
}
