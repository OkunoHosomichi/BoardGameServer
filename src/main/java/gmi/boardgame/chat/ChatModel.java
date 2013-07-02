package gmi.boardgame.chat;

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
   * @throws NullPointerException
   *           パラメータ o が null の場合。
   */
  void addObserver(Observer o) throws NullPointerException;

  /**
   * サーバ情報を返します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   * 
   * @return サーバ情報。nullではありません。
   */
  String getMessage();

  /**
   * サーバ情報を更新してビューに通知します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   * 空文字列が指定された場合は何もせず、ビューへの更新通知も行いません。
   * 
   * @param message
   *          通知するメッセージ。nullを指定できません。
   * @throws NullPointerException
   *           messageがnullの場合。
   */
  void updateInformation(String message) throws NullPointerException;
}
