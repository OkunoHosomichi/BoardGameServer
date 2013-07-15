package gmi.utils.chain;

import java.util.List;

import mockit.Deencapsulation;
import mockit.Expectations;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CommandChainTest {

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void addCommandの引数にnullが指定されたらIllegalArgumentExceptionが投げられるよ() {
    new CommandChain<String>().addCommand(null);
  }

  @Test(groups = { "AllEnv" })
  public void addCommandを呼び出されたらコマンドの連鎖に追加するよ(Command<String> command1, Command<String> command2,
      Command<String> command3) {
    final CommandChain<String> chain = new CommandChain<String>();
    final List<String> list = Deencapsulation.getField(chain, "fCommands");

    assertEquals(list.size(), 0);
    chain.addCommand(command1).addCommand(command2).addCommand(command3);
    assertEquals(list.size(), 3);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void executeの引数にnullが指定されたらIllegalArgumentExceptionが投げられるよ(Command<String> command1, Command<String> command2) {
    final CommandChain<String> chain = new CommandChain<String>().addCommand(command1).addCommand(command2);

    chain.execute(null);
  }

  @Test(groups = { "AllEnv" })
  public void executeを呼び出されたらコマンドの連鎖を実行するよ(final Command<String> command1, final Command<String> command2,
      final Command<String> command3, final Command<String> command4) {
    final CommandChain<String> chain = new CommandChain<String>().addCommand(command1).addCommand(command2)
        .addCommand(command3).addCommand(command4);

    new Expectations() {
      {
        command1.execute("test");
        result = Boolean.FALSE;
        command2.execute("test");
        result = Boolean.FALSE;
        command3.execute("test");
        result = Boolean.TRUE;
        command4.execute("test");
        times = 0;
      }
    };

    assertTrue(chain.execute("test"));
  }

  @Test(groups = { "AllEnv" })
  public void executeを呼び出されたけどコマンドが見つからず実行できなかったらfalseを返すよ(final Command<String> command1,
      final Command<String> command2, final Command<String> command3, final Command<String> command4) {
    final CommandChain<String> chain = new CommandChain<String>();
    chain.addCommand(command1);
    chain.addCommand(command2);
    chain.addCommand(command3);
    chain.addCommand(command4);

    new Expectations() {
      {
        command1.execute("test");
        result = Boolean.FALSE;
        command2.execute("test");
        result = Boolean.FALSE;
        command3.execute("test");
        result = Boolean.FALSE;
        command4.execute("test");
        result = Boolean.FALSE;
      }
    };

    assertFalse(chain.execute("test"));
  }
}
