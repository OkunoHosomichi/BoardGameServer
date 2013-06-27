package gmi.boardgame.chat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.swing.JList;
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
  ChatServerPresenter fPresenter;

  @Test(groups = { "LocalOnly" }, expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerPanel(null);
  }

  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServerPanel panel = new ChatServerPanel(fPresenter);

    new Verifications() {
      {
        fPresenter.addChatView(panel);
      }
    };

    assertSame(Deencapsulation.getField(panel, "fPresenter"), fPresenter);
  }

  @Test(groups = { "LocalOnly" })
  public void setClientListを呼び出されたらクライアント一覧に設定するよ() throws InvocationTargetException, InterruptedException {
    final ChatServerPanel panel = new ChatServerPanel(fPresenter);
    final JList<String> jList = Deencapsulation.getField(panel, "fClientNameList");
    final List<String> clientList = Arrays.asList("aaaaa", "bbb", "cccccccc", "d");

    SwingUtilities.invokeAndWait(new Runnable() {

      @Override
      public void run() {
        try {
          invokeSetClientList(panel, clientList);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
            | SecurityException ex) {
          throw new RuntimeException(ex);
        }
      }

      private void invokeSetClientList(Object obj, List<String> arg) throws IllegalAccessException,
          IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        final Method setClientList = ChatServerPanel.class.getDeclaredMethod("setClientList", List.class);
        setClientList.setAccessible(true);

        setClientList.invoke(obj, arg);
      }
    });

    assertEquals(jList.getModel().getSize(), 4);
    assertEquals(jList.getModel().getElementAt(0), "aaaaa");
    assertEquals(jList.getModel().getElementAt(1), "bbb");
    assertEquals(jList.getModel().getElementAt(2), "cccccccc");
    assertEquals(jList.getModel().getElementAt(3), "d");
  }

  @Test(groups = { "LocalOnly" })
  public void setServerInformationを呼び出されたらテキストエリアに設定するよ() throws InvocationTargetException, InterruptedException {
    final ChatServerPanel panel = new ChatServerPanel(fPresenter);
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

      private void invokeSetServerInformation(ChatServerPanel obj, String arg) throws NoSuchMethodException,
          SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Method setClientList = ChatServerPanel.class.getDeclaredMethod("setServerInformation", String.class);
        setClientList.setAccessible(true);

        setClientList.invoke(obj, arg);
      }
    });

    assertEquals(area.getText(), "aaa" + LINE_SEPARATOR + "bbb" + LINE_SEPARATOR);
  }
}
