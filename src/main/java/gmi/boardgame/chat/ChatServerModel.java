package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class ChatServerModel extends Observable implements ChatModel {
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
  public void joinClient(Client client) {
    if (client == null) throw new NullArgumentException("client");

    fClientList.add(client);

    setChanged();
    notifyObservers("clientList");
  }
}
