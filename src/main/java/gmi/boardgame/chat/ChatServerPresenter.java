package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

import javax.inject.Inject;

final class ChatServerPresenter implements ChatPresenter {
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
}