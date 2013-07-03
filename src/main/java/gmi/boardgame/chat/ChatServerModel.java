package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

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
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
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
    // TODO: 書き方がわかったらテストを書く。
    final List<String> result = new LinkedList<>();

    synchronized (fClientsLock) {
      for (final Channel channel : fClients) {
        result.add(channel.remoteAddress().toString());
      }
    }

    return Collections.unmodifiableList(result);
  }

  @Override
  public String getInformation() {
    assert fMessage != null;

    return fMessage;
  }

  @Override
  public void joinClient(Channel client) throws NullPointerException {
    // TODO: 書き方がわかったらテストを書く。
    if (client == null) throw new NullArgumentException("client");
    client.write("Welcome to Chat!\n");
    client.write("It is " + new Date() + " now.\n");

    synchronized (fClientsLock) {
      fClients.add(client);
    }

    setChanged();
    notifyObservers("clients");
  }

  @Override
  public void leaveClient(Channel client) throws NullPointerException {
    // TODO: 書き方がわかったらテストを書く。
    client.close();

    setChanged();
    notifyObservers("clients");
  }

  @Override
  public void processClientCommand(Channel client, String command) throws NullPointerException {
    if (client == null) throw new NullArgumentException("client");
    if (command == null) throw new NullArgumentException("command");
    if (command.isEmpty()) return;

    synchronized (fClientsLock) {
      for (final Channel c : fClients) {
        if (c != client) {
          c.write("[" + client.remoteAddress() + "] " + command + '\n');
        } else {
          c.write("[you] " + command + '\n');
        }
      }
    }

    // Close the connection if the client has sent 'bye'.
    if ("bye".equals(command.toLowerCase())) {
      leaveClient(client);
    }
  }

  @Override
  public void sendServerMessage(String message) throws NullPointerException {
    // TODO: 書き方がわかったらテストを書く。
    if (message == null) throw new NullArgumentException("message");
    if (message.isEmpty()) return;

    final String sendMsg = "<Server> " + message + "\n";
    synchronized (fClientsLock) {
      for (final Channel c : fClients) {
        c.write(sendMsg);
      }
    }
  }

  @Override
  public void updateInformation(String info) throws NullPointerException {
    if (info == null) throw new NullArgumentException("message");

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
}
