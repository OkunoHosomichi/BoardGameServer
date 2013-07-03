package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

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
   * 接続したクライアントのグループ。
   */
  private final ChannelGroup fClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  /**
   * 行の区切り文字。
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  /**
   * サーバ情報を保持します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   */
  private final StringBuilder fMessage;

  /**
   * インスタンスを構築します。
   */
  @Inject
  public ChatServerModel() {
    fMessage = new StringBuilder();
  }

  @Override
  public String getInformation() {
    assert fMessage.toString() != null;

    return fMessage.toString();
  }

  @Override
  public void joinClient(Channel client) throws NullPointerException {
    if (client == null) throw new NullArgumentException("client");

    fClients.add(client);

    setChanged();
    notifyObservers("clients");
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

    fMessage.append(info).append(LINE_SEPARATOR);

    setChanged();
    notifyObservers("info");
  }
}
