package gmi.boardgame.chat;

import java.util.List;
import java.util.Observer;

public interface ChatModel {
  void addObserver(Observer o);

  List<String> getClientList();

  void joinClient(Client client);
}
