package gmi.utils.exceptions;

import java.util.Objects;

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
   * @param argName
   *          nullだった引数の名前。nullを指定できません。
   * @throws NullPointerException
   *           argNameがnullの場合。
   */
  public NullArgumentException(String argName) throws NullPointerException {

    super("引数" + Objects.requireNonNull(argName, "引数argNameにnullを指定できません。") + "にnullを指定できません。");
  }
}
