package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

final class ChatServerModel extends Observable implements ChatModel, ChannelHandler {
  /**
   * 行の区切り文字。
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  /**
   * チャットに参加しているクライアントの一覧。
   */
  private final List<Client> fClientList = new LinkedList<>();
  /**
   * チャンネルのグループ。
   */
  private final ChannelGroup fGroup;
  /**
   * ユーザに通知するメッセージ。
   */
  private final StringBuilder fMessage = new StringBuilder();

  /**
   * 指定されたチャンネルグループとパイプラインからインスタンスを構築します。
   * 
   * @param group
   *          チャンネルグループ。nullを指定できません。
   * @param pipeline
   *          パイプライン。nullを指定できません。
   * @throws NullPointerException
   *           group又はpipelineがnullの場合。
   */
  @Inject
  public ChatServerModel(ChannelGroup group, ChannelPipeline pipeline) {
    if (group == null) throw new NullArgumentException("group");
    if (pipeline == null) throw new NullArgumentException("pipeline");

    fGroup = group;
    pipeline.addLast(this);
  }

  @Override
  public List<String> getClientList() {
    final List<String> result = new LinkedList<>();

    for (final Client i : fClientList) {
      result.add(i.getNickName());
    }

    return Collections.unmodifiableList(result);
  }

  @Override
  public String getMessage() {
    assert fMessage.toString() != null;

    return fMessage.toString();
  }

  @Override
  public void joinClient(Client client) throws NullPointerException {
    if (client == null) throw new NullArgumentException("client");

    fClientList.add(client);

    setChanged();
    notifyObservers("clientList");
  }

  @Override
  public void leaveClient(Client client) throws NullPointerException, RuntimeException {
    if (client == null) throw new NullArgumentException("client");

    if (!fClientList.remove(client)) throw new RuntimeException();

    setChanged();
    notifyObservers("clientList");
  }

  @Override
  public void message(String message) throws NullPointerException {
    if (message == null) throw new NullArgumentException("message");

    appendMessage(message);
  }

  @Override
  public void sendServerMessage(String message) throws NullPointerException {
    if (message == null) throw new NullArgumentException("message");
    if (message.isEmpty()) return;

    for (final Client i : fClientList) {
      fGroup.find(i.getChannelID()).write("<Server>:" + message + "\n");
    }

    appendMessage("全クライアントに\"" + message + "\"と送信しました。");
  }

  /**
   * ユーザに通知するメッセージに指定された文字列を追加し、変更をビューに通知します。messageにnullを指定した場合、
   * assertによりエラーが発生します。空文字列が指定された場合は何もしません。
   * 
   * @param message
   *          追加する文字列。nullを指定できません。
   */
  private void appendMessage(String message) {
    assert message != null;
    if (message.isEmpty()) return;

    fMessage.append(message).append(LINE_SEPARATOR);

    setChanged();
    notifyObservers("message");
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    // TODO 自動生成されたメソッド・スタブ

  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    // TODO 自動生成されたメソッド・スタブ

  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // TODO 自動生成されたメソッド・スタブ

  }
}
