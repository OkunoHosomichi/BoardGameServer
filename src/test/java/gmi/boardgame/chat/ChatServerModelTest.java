package gmi.boardgame.chat;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerModelTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  @Mocked
  private ChatServerView fChatView;
  @Mocked
  private ChannelGroup fGroup;
  @Mocked
  private Channel fChannel;

  @Test(groups = { "AllEnv" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServerModel model = new ChatServerModel();

    assertTrue(model.getInformation().isEmpty());
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void updateInformationの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.updateInformation(null);
  }

  @Test(groups = { "AllEnv" })
  public void updateInformationの引数に空文字列が指定されても何もしないよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatView);

    new Expectations() {
      {
        fChatView.update(model, "info");
        times = 0;
      }
    };

    model.updateInformation("");

    assertTrue(model.getInformation().isEmpty());
  }

  @Test(groups = { "AllEnv" })
  public void updateInformationを呼び出されたら通知文を更新してビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatView);

    new Expectations() {
      {
        fChatView.update(model, "info");
        times = 3;
      }
    };

    model.updateInformation("test01");
    model.updateInformation("test02");
    model.updateInformation("test03");

    assertEquals(model.getInformation(), "test01" + LINE_SEPARATOR + "test02" + LINE_SEPARATOR + "test03"
        + LINE_SEPARATOR);
  }

  @Test(groups = { "AllEnv" })
  public void appendInformationの引数に空文字列が指定されたら何もしないよ() throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatView);
    final Method appendMessage = ChatServerModel.class.getDeclaredMethod("appendInformation", String.class);
    appendMessage.setAccessible(true);

    new Expectations() {
      {
        fChatView.update(model, "info");
        times = 0;
      }
    };

    appendMessage.invoke(model, "");
  }

  @Test(groups = { "AllEnv" })
  public void appendInformationを呼び出されたら通知文を更新してビューに通知するよ() throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatView);
    final Method appendMessage = ChatServerModel.class.getDeclaredMethod("appendInformation", String.class);
    appendMessage.setAccessible(true);

    new Expectations() {
      {
        fChatView.update(model, "info");
      }
    };

    appendMessage.invoke(model, "aaa");

    assertEquals(model.getInformation(), "aaa" + LINE_SEPARATOR);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void joinClientの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.joinClient(null);
  }

  @Test(groups = { "AllEnv" })
  public void joinClientを呼ばれたらクライアント一覧を更新するよ() {
    final ChatServerModel model = new ChatServerModel();
    Deencapsulation.setField(model, "fClients", fGroup);
    model.addObserver(fChatView);

    new Expectations() {
      {
        fChannel.write(anyString);
        minTimes = 1;
        fGroup.add(fChannel);
        fChatView.update(model, "clients");
      }
    };

    model.joinClient(fChannel);
  }

  @Test(groups = { "AllEnv" })
  public void getClientNamesを呼ばれたら変更不能のクライアント名一覧を返すよ(final SocketAddress address1, final SocketAddress address5) {
    final ChatServerModel model = new ChatServerModel();
    Deencapsulation.setField(model, "fClients", fGroup);

    new Expectations() {
      SocketAddress fAddress2;
      SocketAddress fAddress3;
      SocketAddress fAddress4;
      {
        fGroup.iterator();
        times = 1;
        result = Arrays.asList(fChannel, fChannel, fChannel, fChannel, fChannel).iterator();
        fChannel.remoteAddress();
        returns(address1, fAddress2, fAddress3, fAddress4, address5);
      }
    };

    final List<String> result = model.getClientNames();
    assertEquals(result.size(), 5);
    assertEquals(result.get(0), address1.toString());
    assertEquals(result.get(4), address5.toString());
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { UnsupportedOperationException.class })
  public void getClientNamesが返すクライアント名一覧は追加不能だよ() {
    final ChatServerModel model = new ChatServerModel();
    Deencapsulation.setField(model, "fClients", fGroup);

    new Expectations() {
      SocketAddress fAddress;
      {
        fGroup.iterator();
        result = Arrays.asList(fChannel).iterator();
        fChannel.remoteAddress();
        result = fAddress;
      }
    };

    model.getClientNames().add("asdfg");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { UnsupportedOperationException.class })
  public void getClientNamesが返すクライアント名一覧は削除不能だよ() {
    final ChatServerModel model = new ChatServerModel();
    Deencapsulation.setField(model, "fClients", fGroup);

    new Expectations() {
      SocketAddress fAddress;
      {
        fGroup.iterator();
        result = Arrays.asList(fChannel).iterator();
        fChannel.remoteAddress();
        result = fAddress;
      }
    };

    model.getClientNames().remove(0);
  }
}
