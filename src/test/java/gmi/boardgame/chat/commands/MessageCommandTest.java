package gmi.boardgame.chat.commands;

import mockit.Expectations;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MessageCommandTest {

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void executeの引数がnullだったらIllegalArgumentExceptionを投げるよ() {
    new MessageCommand().execute(null);
  }

  @Test(groups = { "AllEnv" })
  public void executeが呼ばれたけどコンテキストがMessageコマンドじゃなかったらコマンドを実行しないよ(final ChatCommandContext context) {
    final MessageCommand command = new MessageCommand();

    new Expectations() {
      {
        context.getCommand();
        result = "test";
      }
    };

    assertFalse(command.execute(context), "MessageコマンドじゃないからFalseを返すはずだよ");
  }

  @Test(groups = { "AllEnv" })
  public void executeが呼ばれてコンテキストがMessageコマンドだったらコマンドを実行するよ(final ChatCommandContext context) {
    final MessageCommand command = new MessageCommand();

    new Expectations() {
      {
        context.getCommand();
        result = "MSG";
        context.getArguments();
        result = "Test Arguments";
      }
    };

    assertTrue(command.execute(context), "MessageコマンドだからTrueを返すはずだよ");
  }
}
