package gmi.boardgame.chat.commands;

import io.netty.channel.Channel;
import mockit.Expectations;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class NameCommandTest {
  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void executeの引数がnullだったらIllegalArgumentExceptionを投げるよ() {
    new NameCommand().execute(null);
  }

  @Test(groups = { "AllEnv" })
  public void executeが呼ばれたけどコンテキストがNameコマンドじゃなかったらコマンドを実行しないよ(final ChatCommandContext context) {
    final NameCommand command = new NameCommand();

    new Expectations() {
      {
        context.getCommand();
        result = "test";
      }
    };

    assertFalse(command.execute(context), "NameコマンドじゃないからFalseを返すはずだよ");
  }

  @Test(groups = { "AllEnv" })
  public void executeが呼ばれてコンテキストがNameコマンドだったらコマンドを実行するよ(final ChatCommandContext context) {
    final NameCommand command = new NameCommand();

    new Expectations() {
      Channel fClient;
      {
        context.getCommand();
        result = "NAME";
        context.getClient();
        result = fClient;
        context.getArguments();
        result = "Test";
        context.processNameCommand(fClient, "Test");
      }
    };

    assertTrue(command.execute(context), "NameコマンドだからTrueを返すはずだよ");
  }
}
