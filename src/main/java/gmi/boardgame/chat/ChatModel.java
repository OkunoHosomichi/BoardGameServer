package gmi.boardgame.chat;

import java.util.Observer;

public interface ChatModel {
  void addObserver(Observer o);

  void joinClient(Client client);
}
