package gmi.boardgame.chat;

interface ChatPresenter extends ChatModel {
  /**
   * ビューを追加します。プレゼンタはビューに更新通知以外の操作を行えるので引数の型をChatViewにしています。
   * 
   * @param view
   *          追加するビュー。nullを指定できません。
   * @throws NullPointerException
   *           viewがnullの場合。
   */
  void addChatView(ChatView view) throws NullPointerException;
}
