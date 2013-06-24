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
    // TODO 自動生成されたメソッド・スタブ

  }

  @Override
  public List<String> getClientList() {
    // TODO 自動生成されたメソッド・スタブ
    return null;
  }

  @Override
  public String getMessage() {
    // TODO 自動生成されたメソッド・スタブ
    return null;
  }

  @Override
  public void joinClient(Client client) throws NullPointerException {
    // TODO 自動生成されたメソッド・スタブ

  }

  @Override
  public void leaveClient(Client client) throws NullPointerException, RuntimeException {
    // TODO 自動生成されたメソッド・スタブ

  }

  @Override
  public void message(String message) throws NullPointerException {
    // TODO 自動生成されたメソッド・スタブ

  }

  @Override
  public void sendServerMessage(String message) throws NullPointerException {
    // TODO 自動生成されたメソッド・スタブ

  }
}