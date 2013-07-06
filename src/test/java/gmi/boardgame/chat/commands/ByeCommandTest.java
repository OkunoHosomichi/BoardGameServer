package gmi.boardgame.chat.commands;

import io.netty.channel.Channel;
import mockit.Expectations;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ByeCommandTest {
  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void executeの引数がnullだったらIllegalArgumentExceptionを投げるよ() {
    new ByeCommand().execute(null);
  }

  @Test(groups = { "AllEnv" })
  public void executeが呼ばれたけどコンテキストがMessageコマンドじゃなかったらコマンドを実行しないよ(final ChatCommandContext context) {
    final ByeCommand command = new ByeCommand();

    new Expectations() {
      {
        context.getCommand();
        result = "test";
      }
    };

    assertFalse(command.execute(context), "ByeコマンドじゃないからFalseを返すはずだよ");
  }

  @Test(groups = { "AllEnv" })
  public void executeが呼ばれてコンテキストがMessageコマンドだったらコマンドを実行するよ(final ChatCommandContext context) {
    final ByeCommand command = new ByeCommand();

    new Expectations() {
      Channel fClient;
      {
        context.getCommand();
        result = "BYE";
        context.getClient();
        result = fClient;
        context.processByeCommand(fClient);
      }
    };

    assertTrue(command.execute(context), "ByeコマンドだからTrueを返すはずだよ");
  }
}
