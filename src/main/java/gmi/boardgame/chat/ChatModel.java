package gmi.boardgame.chat;

import java.util.List;
import java.util.Observer;

public interface ChatModel {
  void addObserver(Observer o);

  /**
   * チャットに参加しているクライアントのニックネーム一覧を返します。
   * 
   * @return チャットに参加しているクライアントのニックネーム一覧。
   */
  List<String> getClientList();

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
}
