package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

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
    if (view == null) throw new NullArgumentException("view");

    addObserver(view);
    fModel.addObserver(view);
  }

  @Override
  public String getMessage() {
    return fModel.getMessage();
  }

  @Override
  public void message(String message) throws NullPointerException {
    fModel.message(message);
  }
}