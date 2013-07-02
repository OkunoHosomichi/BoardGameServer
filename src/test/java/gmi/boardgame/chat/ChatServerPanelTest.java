package gmi.boardgame.chat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.Verifications;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerPanelTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Mocked
  ChatServerModel fModel;

  @Test(groups = { "LocalOnly" }, expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerView(null);
  }

  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServerView panel = new ChatServerView(fModel);

    new Verifications() {
      {
        fModel.addObserver(panel);
      }
    };

    assertSame(Deencapsulation.getField(panel, "fModel"), fModel);
  }

  @Test(groups = { "LocalOnly" })
  public void setServerInformationを呼び出されたらテキストエリアに設定するよ() throws InvocationTargetException, InterruptedException {
    final ChatServerView panel = new ChatServerView(fModel);
    final JTextArea area = Deencapsulation.getField(panel, "fServerInformation");

    SwingUtilities.invokeAndWait(new Runnable() {

      @Override
      public void run() {
        try {
          invokeSetServerInformation(panel, "aaa" + LINE_SEPARATOR + "bbb" + LINE_SEPARATOR);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
            | SecurityException ex) {
          throw new RuntimeException(ex);
        }
      }

      private void invokeSetServerInformation(ChatServerView obj, String arg) throws NoSuchMethodException,
          SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Method setClientList = ChatServerView.class.getDeclaredMethod("setServerInformation", String.class);
        setClientList.setAccessible(true);

        setClientList.invoke(obj, arg);
      }
    });

    assertEquals(area.getText(), "aaa" + LINE_SEPARATOR + "bbb" + LINE_SEPARATOR);
  }
}
