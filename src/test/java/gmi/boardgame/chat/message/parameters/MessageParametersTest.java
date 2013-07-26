package gmi.boardgame.chat.message.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MessageParametersTest {
  /*
   * コンストラクタMessageParameters(Collection<String>)
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class, description = "コンストラクタMessageParameters(Collection<String>)の引数parametersにnullが指定されたらIllegalArgumentExceptionを投げるよ")
  public void コンストラクタの引数parametersにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new MessageParameters((Collection<String>) null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class, description = "コンストラクタMessageParameters(Collection<String>)の引数parametersに指定されたコレクションが空ならIllegalArgumentExceptionを投げるよ")
  public void コンストラクタの引数parametersに指定されたコレクションが空ならIllegalArgumentExceptionを投げるよ() {
    new MessageParameters(new ArrayList<String>());
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class, description = "コンストラクタMessageParameters(Collection<String>)の引数parametersに指定されたコレクションの要素にnullが含まれていたらIllegalArgumentExceptionを投げるよ")
  public void コンストラクタの引数parametersに指定されたコレクションの要素にnullが含まれていたらIllegalArgumentExceptionを投げるよ() {
    final List<String> parameters = new ArrayList<String>();
    parameters.add("aaa");
    parameters.add(null);
    parameters.add("ccc");

    new MessageParameters(parameters);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class, description = "コンストラクタMessageParameters(Collection<String>)の引数parametersに指定されたコレクションの要素に空文字列が含まれていたらIllegalArgumentExceptionを投げるよ")
  public void コンストラクタの引数parametersに指定されたコレクションの要素に空文字列が含まれていたらIllegalArgumentExceptionを投げるよ() {
    final List<String> parameters = new ArrayList<String>();
    parameters.add("aaa");
    parameters.add("bbb");
    parameters.add("");

    new MessageParameters(parameters);
  }

  @Test(groups = "AllEnv", description = "コンストラクタMessageParameters(Collection<String>)の引数parametersを正しく指定して呼び出したらインスタンスを作成するよ")
  public void コンストラクタの引数parametersを正しく指定して呼び出したらインスタンスを作成するよ() {
    final List<String> parameters = new ArrayList<String>();
    parameters.add("aaa");
    parameters.add("bbb");
    parameters.add("ccc");

    final Parameters result = new MessageParameters(parameters);
    assertEquals(result.toString(), " aaa,bbb,ccc");
  }

  /*
   * コンストラクタMessageParameters(String)
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class, description = "コンストラクタMessageParameters(String)の引数parameterにnullが指定されたらIllegalArgumentExceptionを投げるよ")
  public void コンストラクタの引数parameterにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new MessageParameters((String) null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class, description = "コンストラクタMessageParameters(String)の引数parameterに空文字列が指定されたらIllegalArgumentExceptionを投げるよ")
  public void コンストラクタの引数parameterに空文字列が指定されたらIllegalArgumentExceptionを投げるよ() {
    new MessageParameters("");
  }

  @Test(groups = "AllEnv", description = "コンストラクタMessageParameters(String)の引数parameterを正しく指定して呼び出したらインスタンスを作成するよ")
  public void コンストラクタの引数parameterを正しく指定して呼び出したらインスタンスを作成するよ() {
    final Parameters result = new MessageParameters("test");

    assertEquals(result.toString(), " test");
  }
}
