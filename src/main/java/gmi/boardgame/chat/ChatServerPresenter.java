package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

final class ChatServerPresenter extends Observable implements ChatPresenter {
  private final ChatModel fModel;

  /**
   * 指定されたモデルからインスタンスを構築します。
   * 
   * @param model
   *          モデル。nullを指定できません。
   * @throws NullPointerException
   *           view又はmodelがnullの場合。
   */
  @Inject
  public ChatServerPresenter(ChatModel model) throws NullPointerException {
    if (model == null) throw new NullArgumentException("model");

    fModel = model;
  }

  @Override
  public void addChatView(ChatView view) throws NullPointerException {
    addObserver(view);
    fModel.addObserver(view);
  }

  @Override
  public List<String> getClientList() {
    return fModel.getClientList();
  }

  @Override
  public String getMessage() {
    return fModel.getMessage();
  }

  @Override
  public void joinClient(Client client) throws NullPointerException {
    fModel.joinClient(client);
  }

  @Override
  public void leaveClient(Client client) throws NullPointerException, RuntimeException {
    fModel.leaveClient(client);
  }

  @Override
  public void message(String message) throws NullPointerException {
    fModel.message(message);
  }

  @Override
  public void sendServerMessage(String message) throws NullPointerException {
    fModel.sendServerMessage(message);
  }
}