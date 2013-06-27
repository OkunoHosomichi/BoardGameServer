package gmi.boardgame.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

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

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数groupにnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerModel(null, fPipeline);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数pipelineにnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerModel(fGroup, null);
  }

  @Test(groups = { "AllEnv" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatModel model = new ChatServerModel(fGroup, fPipeline);

    new Verifications() {
      {
        fPipeline.addLast(model);
      }
    };

    assertSame(Deencapsulation.getField(model, "fGroup"), fGroup);
    assertTrue(model.getMessage().isEmpty());
    assertEquals(model.getClientList().size(), 0);
  }

  @Test(groups = { "AllEnv" })
  public void getClientListを呼ばれたらクライアントのリストを返すよ() {
    final ChatModel model = new ChatServerModel(fGroup, fPipeline);
    model.joinClient(new Client(Integer.valueOf(0), "aaa"));
    model.joinClient(new Client(Integer.valueOf(1), "bbb"));
    model.joinClient(new Client(Integer.valueOf(2), "ccc"));
    model.joinClient(new Client(Integer.valueOf(3), "ddd"));

    final List<String> expected = Arrays.asList("aaa", "bbb", "ccc", "ddd");

    assertEquals(model.getClientList().size(), 4);
    assertEquals(model.getClientList(), expected);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { UnsupportedOperationException.class })
  public void getClientListが返すリストは変更不能だよ() {
    final ChatModel model = new ChatServerModel(fGroup, fPipeline);
    model.joinClient(new Client(Integer.valueOf(0), "aaa"));
    model.joinClient(new Client(Integer.valueOf(1), "bbb"));
    model.joinClient(new Client(Integer.valueOf(2), "ccc"));
    model.joinClient(new Client(Integer.valueOf(3), "ddd"));

    model.getClientList().add("eee");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void joinClientの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.joinClient(null);
  }

  @Test(groups = { "AllEnv" })
  public void joinClientを呼び出されたらクライアント一覧を更新してビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.addObserver(fChatPanel);

    new Expectations() {
      {
        fChatPanel.update(model, "clientList");
        times = 3;
      }
    };

    model.joinClient(new Client(Integer.valueOf(0), "test01"));
    model.joinClient(new Client(Integer.valueOf(1), "test03"));
    model.joinClient(new Client(Integer.valueOf(2), "test02"));

    final List<String> expected = new ArrayList<>();
    expected.add("test01");
    expected.add("test03");
    expected.add("test02");

    assertEquals(model.getClientList(), expected);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void leaveClientの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.leaveClient(null);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { RuntimeException.class })
  public void leaveClientを呼び出されたけど指定されたクライアントが一覧になかったらRuntimeExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.addObserver(fChatPanel);
    model.joinClient(new Client(Integer.valueOf(0), "test01"));
    model.joinClient(new Client(Integer.valueOf(1), "test02"));
    model.joinClient(new Client(Integer.valueOf(2), "test03"));

    model.leaveClient(new Client(Integer.valueOf(1), "test"));
  }

  @Test(groups = { "AllEnv" })
  public void leaveClientを呼び出されたらクライアント一覧から削除してビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.addObserver(fChatPanel);
    model.joinClient(new Client(Integer.valueOf(0), "test01"));
    model.joinClient(new Client(Integer.valueOf(1), "test02"));
    model.joinClient(new Client(Integer.valueOf(2), "test03"));

    new Expectations() {
      {
        fChatPanel.update(model, "clientList");
      }
    };

    model.leaveClient(new Client(Integer.valueOf(1), "test02"));

    final List<String> expected = new ArrayList<>();
    expected.add("test01");
    expected.add("test03");

    assertEquals(model.getClientList(), expected);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void messageの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.message(null);
  }

  @Test(groups = { "AllEnv" })
  public void messageの引数に空文字列が指定されても何もしないよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
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
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
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

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void sendServerMessageの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.sendServerMessage(null);
  }

  @Test(groups = { "AllEnv" })
  public void sendServerMessageの引数に空文字が指定されても何もしないよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.addObserver(fChatPanel);
    model.joinClient(new Client(Integer.valueOf(0), "aaa"));
    model.joinClient(new Client(Integer.valueOf(1), "bbb"));

    new Expectations() {
      {
        fGroup.find(anyInt);
        times = 0;

        fTestChannel01.write(anyString);
        times = 0;

        fTestChannel02.write(anyString);
        times = 0;

        fChatPanel.update(model, "message");
        times = 0;
      }
    };

    model.sendServerMessage("");

    assertTrue(model.getMessage().isEmpty());
  }

  @Test(groups = { "AllEnv" })
  public void sendServerMessageを呼び出されたら全クライアントにメッセージを送ってビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
    model.addObserver(fChatPanel);
    model.joinClient(new Client(Integer.valueOf(0), "aaa"));
    model.joinClient(new Client(Integer.valueOf(1), "bbb"));

    new Expectations() {
      {
        fGroup.find(Integer.valueOf(0));
        result = fTestChannel01;

        fTestChannel01.write("<Server>:test\n");

        fGroup.find(Integer.valueOf(1));
        result = fTestChannel02;

        fTestChannel02.write("<Server>:test\n");

        fChatPanel.update(model, "message");
      }
    };

    model.sendServerMessage("test");

    assertEquals(model.getMessage(), "全クライアントに\"test\"と送信しました。" + LINE_SEPARATOR);
  }

  @Test(groups = { "AllEnv" })
  public void appendMessageの引数に空文字列が指定されたら何もしないよ() throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
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
    final ChatServerModel model = new ChatServerModel(fGroup, fPipeline);
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
