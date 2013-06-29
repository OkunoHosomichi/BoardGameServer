package gmi.boardgame.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerModelTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  @Mocked
  private ChatServerPanel fChatPanel;
  @Mocked
  private ChannelGroup fGroup;
  @Mocked
  private ChannelPipeline fPipeline;
  @Mocked
  private Channel fTestChannel01;
  @Mocked
  private Channel fTestChannel02;

  @Test(groups = { "AllEnv" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServerModel model = new ChatServerModel();

    assertTrue(model.getMessage().isEmpty());
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void messageの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.message(null);
  }

  @Test(groups = { "AllEnv" })
  public void messageの引数に空文字列が指定されても何もしないよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);

    new Expectations() {
      {
        fChatPanel.update(model, "message");
        times = 0;
      }
    };

    model.message("");

    assertTrue(model.getMessage().isEmpty());
  }

  @Test(groups = { "AllEnv" })
  public void messageを呼び出されたら通知文を更新してビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);

    new Expectations() {
      {
        fChatPanel.update(model, "message");
        times = 3;
      }
    };

    model.message("test01");
    model.message("test02");
    model.message("test03");

    assertEquals(model.getMessage(), "test01" + LINE_SEPARATOR + "test02" + LINE_SEPARATOR + "test03" + LINE_SEPARATOR);
  }

  @Test(groups = { "AllEnv" })
  public void appendMessageの引数に空文字列が指定されたら何もしないよ() throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);
    final Method appendMessage = ChatServerModel.class.getDeclaredMethod("appendMessage", String.class);
    appendMessage.setAccessible(true);

    new Expectations() {
      {
        fChatPanel.update(model, "message");
        times = 0;
      }
    };

    appendMessage.invoke(model, "");
  }

  @Test(groups = { "AllEnv" })
  public void appendMessageを呼び出されたら通知文を更新してビューに通知するよ() throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);
    final Method appendMessage = ChatServerModel.class.getDeclaredMethod("appendMessage", String.class);
    appendMessage.setAccessible(true);

    new Expectations() {
      {
        fChatPanel.update(model, "message");
      }
    };

    appendMessage.invoke(model, "aaa");

    assertEquals(model.getMessage(), "aaa" + LINE_SEPARATOR);
  }
}
