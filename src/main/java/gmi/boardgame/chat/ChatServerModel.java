package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class ChatServerModel extends Observable implements ChatModel {
  /**
   * チャットに参加しているクライアントの一覧。
   */
  private final List<Client> fClientList = new LinkedList<>();

  @Override
  public List<String> getClientList() {
    List<String> result = new LinkedList<>();
    for (Client i : fClientList) {
      result.add(i.getNickName());
    }

    return result;
  }

  @Override
  public void joinClient(Client client) throws NullPointerException {
    if (client == null) throw new NullArgumentException("client");

    fClientList.add(client);

    setChanged();
    notifyObservers("clientList");
  }

  @Override
  public void leaveClient(Client client) throws NullPointerException {
    // TODO 自動生成されたメソッド・スタブ

  }
}
