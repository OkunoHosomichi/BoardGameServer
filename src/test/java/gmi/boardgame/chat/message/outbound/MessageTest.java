package gmi.boardgame.chat.message.outbound;

import gmi.boardgame.chat.message.parameters.Parameters;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MessageTest {
  /*
   * コンストラクタMessag(Commands,Parameters)
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void コンストラクタの引数commandにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new Message(null, Parameters.getParameters(""));
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void コンストラクタの引数parametersにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new Message(Commands.ENTER, null);
  }

  @Test(groups = "AllEnv")
  public void コンストラクタを呼び出されたらインスタンスを作成するよ() {
    assertEquals(new Message(Commands.MESSAGE, Parameters.getParameters("test")).toString(), "MSG test\n");
  }
}
