package gmi.boardgame.chat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.Verifications;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerViewTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Mocked
  ChatServerModel fModel;

  @Test(groups = { "GUIOnly" }, expectedExceptions = { IllegalArgumentException.class })
  public void コンストラクタの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerView(null);
  }

  @Test(groups = { "GUIOnly" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServerView panel = new ChatServerView(fModel);

    new Verifications() {
      {
        fModel.addObserver((Observer) any);
      }
    };

    assertSame(Deencapsulation.getField(panel, "fModel"), fModel);
  }

  @Test(groups = { "GUIOnly" })
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

  @Test(groups = { "GUIOnly" })
  public void setClientNamesを呼び出されたらリストに設定するよ() throws InvocationTargetException, InterruptedException {
    final ChatServerView panel = new ChatServerView(fModel);
    final JList<String> list = Deencapsulation.getField(panel, "fClientNames");

    SwingUtilities.invokeAndWait(new Runnable() {

      @Override
      public void run() {
        try {
          invokeSetClientNames(panel, Arrays.asList("aaa", "bb", "vvv", "ssss"));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
            | SecurityException ex) {
          throw new RuntimeException(ex);
        }
      }

      private void invokeSetClientNames(ChatServerView obj, List<String> arg) throws NoSuchMethodException,
          SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Method setClientList = ChatServerView.class.getDeclaredMethod("setClientNames", List.class);
        setClientList.setAccessible(true);

        setClientList.invoke(obj, arg);
      }
    });

    assertEquals(list.getModel().getSize(), 4);
    assertEquals(list.getModel().getElementAt(0), "aaa");
    assertEquals(list.getModel().getElementAt(3), "ssss");
  }
}
