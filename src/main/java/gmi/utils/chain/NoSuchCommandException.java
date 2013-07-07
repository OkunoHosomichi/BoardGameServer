package gmi.utils.chain;

/**
 * コマンド連鎖を実行してコマンドが見つからない時にスローされる例外です。CommndChainクラス用の例外です。
 * コマンドを実行できたかどうかはチェックすべきだと思うので検査例外にしています。
 * 
 * @author おくのほそみち
 */
public final class NoSuchCommandException extends Exception {
  /**
   * INFO:無いとは思うけどクラスを変更したら変更する。
   */
  private static final long serialVersionUID = -572768686549798611L;

  /**
   * 例外のインスタンスを構築します。他パッケージからは呼び出せません。
   */
  NoSuchCommandException() {
  }
}
