package gmi.utils.exceptions;

/**
 * 引数にnullが渡された時にthrowされる例外です。NullPointerExceptionとして扱います。
 * 例外のメッセージを毎回指定するのが煩わしかったので作りました。
 * 
 * @author おくのほそみち
 */
@SuppressWarnings("serial")
public class NullArgumentException extends NullPointerException {
  /**
   * 指定された引数名からインスタンスを構築します。 引数名はエラーメッセージに使われます。
   * 
   * @param ArgName
   *          nullだった引数の名前。
   */
  public NullArgumentException(String ArgName) {

    super("引数" + ArgName + "にnull値を指定できません。");
  }
}
