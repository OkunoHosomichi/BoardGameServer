package gmi.utils.chain;

/**
 * 実行するコマンドはこのインターフェースを実装します。CommandChainに登録して使います。
 * 
 * @author おくのほそみち
 * @param <T>
 *          コンテキストの型。
 */
public interface Command<T> {
  /**
   * コマンドを実行します。
   * 
   * @param context
   *          実行に必要なコンテキスト。nullを指定できません。
   * @return 実行したかどうか。実行したならtrue、実行していないならFalseを返します。
   * @throws IllegalArgumentException
   *           contextがnullの場合。
   */
  public boolean execute(T context) throws IllegalArgumentException, NoSuchCommandException;
}
