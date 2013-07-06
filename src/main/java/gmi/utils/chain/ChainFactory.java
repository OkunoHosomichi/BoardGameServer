package gmi.utils.chain;

/**
 * コマンド連鎖を生成するためのインターフェースです。
 * 
 * @author おくのほそみち
 * @param <T>
 *          コンテキストの型。
 */
public interface ChainFactory<T> {
  /**
   * コマンド連鎖を生成するファクトリメソッドです。
   * 
   * @return コマンドの連鎖。nullではありません。
   */
  Command<T> getChain();
}
