package gmi.boardgame.chat.message.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ParametersTest {
  /*
   * getParameters(Collection<String>)
   */
  @Test(groups = "AllEnv")
  public void getParametersの引数parametersにnullが指定されたら空パラメータのインスタンスを返すよ() {
    assertEquals(Parameters.getParameters((Collection<String>) null).toString(), "");
  }

  @Test(groups = "AllEnv")
  public void getParametersの引数parametersに指定されたコレクションが空だったら空パラメータのインスタンスを返すよ() {
    assertEquals(Parameters.getParameters(new ArrayList<String>()).toString(), "");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void getParametersの引数parametersに指定されたコレクションの要素にnullが含まれていたらIllegalArgumentExceptionを投げるよ() {
    final List<String> parameters = new ArrayList<String>();
    parameters.add("aaa");
    parameters.add(null);
    parameters.add("ccc");

    Parameters.getParameters(parameters);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void getParametersの引数parametersに指定されたコレクションの要素に空文字列が含まれていたらIllegalArgumentExceptionを投げるよ() {
    final List<String> parameters = new ArrayList<String>();
    parameters.add("aaa");
    parameters.add("bbb");
    parameters.add("");

    Parameters.getParameters(parameters);
  }

  @Test(groups = "AllEnv")
  public void getParametersの引数parametersを正しく指定して呼び出したらインスタンスを返すよ() {
    final List<String> parameters = new ArrayList<String>();
    parameters.add("aaa");
    parameters.add("sss");
    parameters.add("ddd");

    assertEquals(Parameters.getParameters(parameters).toString(), " aaa,sss,ddd");
  }

  /*
   * getParameters(String)
   */
  @Test(groups = "AllEnv")
  public void getParametersの引数parameterにnullが指定されたら空パラメータのインスタンスを返すよ() {
    assertEquals(Parameters.getParameters((String) null).toString(), "");
  }

  @Test(groups = "AllEnv")
  public void getParametersの引数parameterに空文字列が指定されたら空パラメータのインスタンスを返すよ() {
    assertEquals(Parameters.getParameters("").toString(), "");
  }

  @Test(groups = "AllEnv")
  public void getParametersの引数parameterを正しく指定して呼び出したらインスタンスを返すよ() {
    assertEquals(Parameters.getParameters("asdfg").toString(), " asdfg");
  }
}
