package gmi.boardgame.chat;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerModelTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  @Mocked
  private ChatServerPanel fChatPanel;
  @Mocked
  private ChannelGroup group;
  @Mocked
  private Channel testChannel01;
  @Mocked
  private Channel testChannel02;

  @Test(expectedExceptions = { NullPointerException.class })
  public void joinClientの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.joinClient(null);
  }

  @Test
  public void joinClientを呼び出されたらクライアント一覧を更新してビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);

    new Expectations() {
      {
        fChatPanel.update(model, "clientList");
        times = 3;
      }
    };

    model.joinClient(new Client(new Integer(0), "test01"));
    model.joinClient(new Client(new Integer(1), "test03"));
    model.joinClient(new Client(new Integer(2), "test02"));

    final List<String> expected = new ArrayList<>();
    expected.add("test01");
    expected.add("test03");
    expected.add("test02");

    assertEquals(model.getClientList(), expected);
  }

  @Test(expectedExceptions = { NullPointerException.class })
  public void leaveClientの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.leaveClient(null);
  }

  @Test(expectedExceptions = { RuntimeException.class })
  public void leaveClientを呼び出されたけど指定されたクライアントが一覧になかったらRuntimeExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);
    model.joinClient(new Client(new Integer(0), "test01"));
    model.joinClient(new Client(new Integer(1), "test02"));
    model.joinClient(new Client(new Integer(2), "test03"));

    model.leaveClient(new Client(new Integer(1), "test"));
  }

  @Test
  public void leaveClientを呼び出されたらクライアント一覧から削除してビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);
    model.joinClient(new Client(new Integer(0), "test01"));
    model.joinClient(new Client(new Integer(1), "test02"));
    model.joinClient(new Client(new Integer(2), "test03"));

    new Expectations() {
      {
        fChatPanel.update(model, "clientList");
      }
    };

    model.leaveClient(new Client(new Integer(1), "test02"));

    final List<String> expected = new ArrayList<>();
    expected.add("test01");
    expected.add("test03");

    assertEquals(model.getClientList(), expected);
  }

  @Test(expectedExceptions = { NullPointerException.class })
  public void messageの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.message(null);
  }

  @Test
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

  @Test
  public void messageを呼び出されたらビューに通知するよ() {
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

  @Test(expectedExceptions = { NullPointerException.class })
  public void sendServerMessageの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.sendServerMessage(null);
  }

  @Test
  public void sendServerMessageの引数に空文字が指定されても何もしないよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);
    model.joinClient(new Client(Integer.valueOf(0), "aaa"));
    model.joinClient(new Client(Integer.valueOf(1), "bbb"));

    new Expectations() {
      {
        group.find(Integer.valueOf(0));
        times = 0;

        testChannel01.write(anyString);
        times = 0;

        testChannel02.write(anyString);
        times = 0;

        fChatPanel.update(model, "message");
        times = 0;
      }
    };

    model.sendServerMessage("");

    assertTrue(model.getMessage().isEmpty());
  }

  @Test
  public void sendServerMessageを呼び出されたら全クライアントにメッセージを送ってビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);
    model.joinClient(new Client(Integer.valueOf(0), "aaa"));
    model.joinClient(new Client(Integer.valueOf(1), "bbb"));

    new Expectations() {
      {
        group.find(Integer.valueOf(0));
        result = testChannel01;

        testChannel01.write("test\n");

        group.find(Integer.valueOf(1));
        result = testChannel02;

        testChannel02.write("test\n");

        fChatPanel.update(model, "message");
      }
    };

    model.sendServerMessage("test");

    assertEquals(model.getMessage(), "全クライアントに\"test\"と送信しました。" + LINE_SEPARATOR);
  }
}
