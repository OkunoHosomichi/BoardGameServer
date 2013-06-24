package gmi.boardgame.chat;

interface ChatPresenter extends ChatModel {
  /**
   * ビューを追加します。
   * 
   * @param view
   *          追加するビュー。nullを指定できません。
   * @throws NullPointerException
   *           viewがnullの場合。
   */
  void addChatView(ChatView view) throws NullPointerException;
}
