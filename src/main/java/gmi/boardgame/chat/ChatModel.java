package gmi.boardgame.chat;

import java.util.Observer;

interface ChatModel {
  /**
   * このオブジェクトの Observer のセットに Observer を追加します (セット内にすでにあるいくつかの Observer
   * と同じでない場合)。複数の Observer に通知が配信される順序は未指定です。クラスのコメントを参照してください。
   * モデルはビューに更新通知以外の操作をしてはならないので引数の型をObserverにしています。
   * 
   * @see java.util.Observable#addObserver(java.util.Observer)
   * @param o
   *          追加するObserver
   * @throws NullPointerException
   *           パラメータ o が null の場合。
   */
  void addObserver(Observer o) throws NullPointerException;

  /**
   * ユーザーに通知するメッセージを返します。
   * 
   * @return 通知するメッセージ。nullではありません。
   */
  String getMessage();

  /**
   * ユーザに指定されたメッセージを通知します。空文字列が指定された場合は何もしません。
   * 
   * @param message
   *          通知するメッセージ。nullを指定できません。
   * @throws NullPointerException
   *           messageがnullの場合。
   */
  void message(String message) throws NullPointerException;
}
