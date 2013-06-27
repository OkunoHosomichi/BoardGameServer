package gmi.boardgame.chat;

import io.netty.channel.ChannelHandler;

import java.util.List;
import java.util.Observer;

interface ChatModel extends ChannelHandler {
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
   * チャットに参加しているクライアントのニックネーム一覧を返します。 一覧は変更できません。
   * 
   * @return チャットに参加しているクライアントのニックネーム一覧。
   */
  List<String> getClientList();

  /**
   * ユーザーに通知するメッセージを返します。
   * 
   * @return 通知するメッセージ。nullではありません。
   */
  String getMessage();

  /**
   * 指定されたクライアントをチャット参加者一覧に登録します。
   * 
   * @param client
   *          一覧に登録するクライアント。nullを指定できません。
   * @throws NullPointerException
   *           clientがnullの場合。
   */
  void joinClient(Client client) throws NullPointerException;

  /**
   * 指定されたクライアントをチャット参加者一覧から削除します。
   * 
   * @param client
   *          一覧から削除するクライアント。nullを指定できません。
   * @throws NullPointerException
   *           clientがnullの場合。
   * @throws RuntimeException
   *           一覧に指定されたクライアントが存在しない場合。
   */
  void leaveClient(Client client) throws NullPointerException, RuntimeException;

  /**
   * ユーザに指定されたメッセージを通知します。空文字列が指定された場合は何もしません。
   * 
   * @param message
   *          通知するメッセージ。nullを指定できません。
   * @throws NullPointerException
   *           messageがnullの場合。
   */
  void message(String message) throws NullPointerException;

  /**
   * 全クライアントにサーバからのメッセージを送信します。 指定されたメッセージが空文字の場合は送信しません。
   * 
   * @param message
   *          送信するメッセージ。nullを指定できません。
   * @throws NullPointerException
   *           messageがnullの場合。
   */
  void sendServerMessage(String message) throws NullPointerException;
}
