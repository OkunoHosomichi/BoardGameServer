package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public final class ChatServerModel extends Observable implements ChatModel {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  /**
   * チャットに参加しているクライアントの一覧。
   */
  private final List<Client> fClientList = new LinkedList<>();
  /**
   * ユーザに通知するメッセージ。
   */
  private final StringBuilder fMessage = new StringBuilder();

  @Override
  public List<String> getClientList() {
    final List<String> result = new LinkedList<>();
    for (final Client i : fClientList) {
      result.add(i.getNickName());
    }

    return result;
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
    if (message.isEmpty()) return;

    fMessage.append(message + LINE_SEPARATOR);

    setChanged();
    notifyObservers("message");
  }
}
