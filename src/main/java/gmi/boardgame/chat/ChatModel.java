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
public interface ChatModel {
  /**
   * モデルの更新を通知するビューを登録します。 モデルはビューに更新通知以外の操作をしてはならないので引数の型をObserverにしています。
   * 
   * @see java.util.Observable#addObserver(java.util.Observer)
   * @param o
   *          追加するObserver。
   */
  void addObserver(Observer o);

  /**
   * チャットに参加しているクライアントの名前一覧を返します。
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
   * Byeコマンドの処理を行います。指定されたクライアントの切断処理を実行します。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。
   */
  void processByeCommand(Channel client);

  /**
   * チャットに参加しているクライアントから受信したコマンドを処理します。
   * 
   * @param client
   *          コマンドを送信したクライアント。
   * @param command
   *          コマンド文字列。
   * @return コマンドを処理したならtrue、処理しなかったならfalse。
   */
  boolean processClientCommand(Channel client, String command);

  /**
   * Messageコマンドを処理します。コマンドを送信してきたクライアント以外の全クライアントにメッセージを送信します。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。
   * @param message
   *          送信するメッセージ。
   */
  void processMessageCommand(Channel client, String message);

  /**
   * Nameコマンドを処理します。チャットに参加しているクライアントの一覧に登録し、
   * コマンドを送信してきたクライアント以外の全クライアントに参加したことを通知します。その後、ビューにクライアントの一覧が変更されたことを通知します。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。
   * @param nickName
   *          ニックネーム。
   */
  void processNameCommand(Channel client, String nickName);

  /**
   * サーバからのメッセージをチャットに参加している全クライアントに送信します。
   * 
   * @param message
   *          送信するメッセージ。
   */
  void sendServerMessage(String message);

  /**
   * サーバ情報を更新してビューに通知します。サーバ情報とは接続待ち開始の通知、クライアント接続の通知、クライアントをキックしたことの通知、
   * エラーが発生した事を示す警告など様々なものを想定しています。 想定しているだけで実際にそれらを通知するかはまだわかりません。
   * 
   * @param message
   *          通知するメッセージ。
   */
  void updateInformation(String message);
}
