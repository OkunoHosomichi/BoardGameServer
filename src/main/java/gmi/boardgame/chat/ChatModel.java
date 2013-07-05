package gmi.boardgame.chat;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Observer;

/**
 * チャットのロジック部分を担当するインターフェースです。MVCパターンのモデル部分のつもりです。
 * コントローラから呼ばれる処理のほかにチャット部分の通信処理も担当します。
 * 
 * @author おくのほそみち
 */
interface ChatModel {
  /**
   * モデルの更新を通知するビューを登録します。 モデルはビューに更新通知以外の操作をしてはならないので引数の型をObserverにしています。
   * 
   * @see java.util.Observable#addObserver(java.util.Observer)
   * @param o
   *          追加するObserver
   * @throws IllegalArgumentException
   *           パラメータ o が null の場合。
   */
  void addObserver(Observer o) throws IllegalArgumentException;

  /**
   * チャットに参加しているクライアントの名前一覧を返します。一覧は変更不能になっています。テストはどう書けばいいのかわかりません。
   * 
   * @return クライアントの名前一覧。
   */
  List<String> getClientNames();

  /**
   * サーバ情報を返します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   * 
   * @return サーバ情報。nullではありません。
   */
  String getInformation();

  /**
   * チャットに参加してきたクライアントを処理し、ビューに変更を通知します。クライアントが切断したときの処理も登録しておきます。
   * 
   * @param client
   *          接続してきたクライアント。nullを指定できません。
   * @throws IllegalArgumentException
   *           clientがnullの場合。
   */
  void joinClient(Channel client) throws IllegalArgumentException;

  /**
   * チャットに参加しているクライアントから受信したコマンドを処理します。コマンドが空文字列の場合は何もしません。
   * 
   * @param client
   *          コマンドを送信したクライアント。nullを指定できません。
   * @param command
   *          コマンド文字列。nullを指定できません。
   * @throws IllegalArgumentException
   *           client又はcommandがnullの場合。
   */
  void processClientCommand(Channel client, String command) throws IllegalArgumentException;

  /**
   * サーバからのメッセージをチャットに参加している全クライアントに送信します。メッセージが空文字列の場合は何もしません。
   * 
   * @param message
   *          送信するメッセージ。nullを指定できません。
   * @throws IllegalArgumentException
   *           messageがnullの場合。
   */
  void sendServerMessage(String message) throws IllegalArgumentException;

  /**
   * サーバ情報を更新してビューに通知します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   * 空文字列が指定された場合は何もせず、ビューへの更新通知も行いません。
   * 
   * @param message
   *          通知するメッセージ。nullを指定できません。
   * @throws IllegalArgumentException
   *           messageがnullの場合。
   */
  void updateInformation(String message) throws IllegalArgumentException;
}
