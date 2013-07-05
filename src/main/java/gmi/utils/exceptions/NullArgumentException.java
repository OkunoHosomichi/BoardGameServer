package gmi.utils.exceptions;

/**
 * 引数にnullが渡された時にthrowされる例外です。IllegalArgumentExceptionとして扱います。
 * 例外のメッセージを毎回指定するのが煩わしかったので作りました。
 * 
 * @author おくのほそみち
 */
@SuppressWarnings("serial")
public class NullArgumentException extends IllegalArgumentException {
  /**
   * 指定された引数名からインスタンスを構築します。 引数名はエラーメッセージに使われます。
   * 
   * @param argName
   *          nullだった引数の名前。nullを指定できません。
   * @throws IllegalArgumentException
   *           argNameがnullの場合。
   */
  public NullArgumentException(String argName) throws IllegalArgumentException {

    super("引数" + checkArguments(argName) + "にnullを指定できません。");
  }

  /**
   * 引数に渡されたオブジェクトがnullかどうかチェックします。nullだった場合はIllegalArgumentExceptionがスローされます。
   * コンストラクタでsuper() を呼び出す都合でnullチェックをメソッドに分ける必要が出ました。
   * 
   * @param arg
   *          nullチェックを行いたいオブジェクト。
   * @return 引数のargがそのまま返ります。
   * @throws IllegalArgumentException
   *           argがnullの場合。
   */
  private static String checkArguments(String arg) throws IllegalArgumentException {
    if (arg == null) throw new IllegalArgumentException("引数argにnullを指定できません。");

    return arg;
  }
}
