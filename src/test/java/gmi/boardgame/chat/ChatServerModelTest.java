package gmi.boardgame.chat;

import gmi.utils.netty.channel.ChannelUtilities;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.FullVerificationsInOrder;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ChatServerModelTest {
  private static final String LINE_SEPARATOR = System.lineSeparator();
  @Mocked
  private Observer fObserver;
  @Mocked
  private ChannelGroup fGroup;
  @Mocked
  private Channel fChannel;

  @Test(groups = { "AllEnv" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServerModel model = new ChatServerModel();

    assertTrue(model.getInformation().isEmpty());
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void updateInformationの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.updateInformation(null);
  }

  @Test(groups = { "AllEnv" })
  public void updateInformationの引数に空文字列が指定されても何もしないよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fObserver);

    new Expectations() {
      {
        fObserver.update(model, "info");
        times = 0;
      }
    };

    model.updateInformation("");

    assertTrue(model.getInformation().isEmpty());
  }

  @Test(groups = { "AllEnv" })
  public void updateInformationを呼び出されたら通知文を更新してビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fObserver);

    new Expectations() {
      {
        fObserver.update(model, "info");
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
    model.addObserver(fObserver);
    final Method appendMessage = ChatServerModel.class.getDeclaredMethod("appendInformation", String.class);
    appendMessage.setAccessible(true);

    new Expectations() {
      {
        fObserver.update(model, "info");
        times = 0;
      }
    };

    appendMessage.invoke(model, "");
  }

  @Test(groups = { "AllEnv" })
  public void appendInformationを呼び出されたら通知文を更新してビューに通知するよ() throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fObserver);
    final Method appendMessage = ChatServerModel.class.getDeclaredMethod("appendInformation", String.class);
    appendMessage.setAccessible(true);

    new Expectations() {
      {
        fObserver.update(model, "info");
      }
    };

    appendMessage.invoke(model, "aaa");

    assertEquals(model.getInformation(), "aaa" + LINE_SEPARATOR);
  }

  @Test(groups = { "AllEnv" })
  public void getClientNamesを呼ばれたら変更不能のクライアント名一覧を返すよ() {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    Deencapsulation.setField(model, "fClients", group);

    final Channel channel1 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final Channel channel2 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final Channel channel3 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    ChannelUtilities.setNickName(channel1, "aaa");
    ChannelUtilities.setNickName(channel2, "ccc");
    ChannelUtilities.setNickName(channel3, "eee");
    group.add(channel1);
    group.add(channel2);
    group.add(channel3);

    final List<String> result = model.getClientNames();
    assertEquals(result.size(), 3);
    assertEquals(result.get(0), "aaa");
    assertEquals(result.get(2), "eee");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { UnsupportedOperationException.class })
  public void getClientNamesが返すクライアント名一覧は追加不能だよ() {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    Deencapsulation.setField(model, "fClients", group);

    final Channel channel1 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    ChannelUtilities.setNickName(channel1, "aaa");
    group.add(channel1);

    model.getClientNames().add("asdfg");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { UnsupportedOperationException.class })
  public void getClientNamesが返すクライアント名一覧は削除不能だよ() {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    Deencapsulation.setField(model, "fClients", group);

    final Channel channel1 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final Channel channel2 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    ChannelUtilities.setNickName(channel1, "aaa");
    ChannelUtilities.setNickName(channel2, "bbb");
    group.add(channel1);
    group.add(channel2);

    model.getClientNames().remove(0);
  }

  @Test(groups = { "AllEnv" })
  public void leaveClientを呼ばれたらクライアント一覧を更新してビューに通知するよ() throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Method leaveClient = ChatServerModel.class.getDeclaredMethod("leaveClient", new Class[] {});
    leaveClient.setAccessible(true);

    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fObserver);

    new Expectations() {
      {
        fObserver.update(model, "clients");
      }
    };

    leaveClient.invoke(model, new Object[] {});
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void sendServerMessageの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.sendServerMessage(null);
  }

  @Test(groups = { "AllEnv" })
  public void sendServerMessageの引数に空文字列が指定されたら何もしないよ() {
    final ChatServerModel model = new ChatServerModel();
    Deencapsulation.setField(model, "fClients", fGroup);

    new Expectations() {
      Channel fChannel1;
      Channel fChannel2;
      {
        fGroup.iterator();
        times = 0;
        result = Arrays.asList(fChannel1, fChannel2).iterator();
        fChannel1.write("<Server> \n");
        times = 0;
        fChannel2.write("<Server> \n");
        times = 0;
      }
    };

    model.sendServerMessage("");
  }

  @Test(groups = { "AllEnv" })
  public void sendServerMessageを呼び出されたら各クライアントにサーバからのメッセージを送信するよ() {
    final ChatServerModel model = new ChatServerModel();
    Deencapsulation.setField(model, "fClients", fGroup);

    new Expectations() {
      Channel fChannel1;
      Channel fChannel2;
      {
        fGroup.iterator();
        result = Arrays.asList(fChannel1, fChannel2).iterator();

        fChannel1.write("<Server> これはテストです\n");
        fChannel2.write("<Server> これはテストです\n");
      }
    };

    model.sendServerMessage("これはテストです");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void processMessageCommandの引数clientにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.processMessageCommand(null, "aaa");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void processMessageCommandの引数messageにnullが指定されたらIllegalArgumentExceptionを投げるよ(Channel client) {
    final ChatServerModel model = new ChatServerModel();
    model.processMessageCommand(client, null);
  }

  @Test(groups = { "AllEnv" })
  public void processMessageCommandの引数messageに空文字列が指定されたら何もしないよ(final Channel client) {
    final ChatServerModel model = new ChatServerModel();
    Deencapsulation.setField(model, "fClients", fGroup);

    new Expectations() {
      Channel fChannel1;
      Channel fChannel2;
      {
        fGroup.iterator();
        times = 0;
        result = Arrays.asList(client, fChannel1, fChannel2).iterator();
        client.write(anyString);
        times = 0;
        fChannel1.write(anyString);
        times = 0;
        fChannel2.write(anyString);
        times = 0;
      }
    };

    model.processMessageCommand(client, "");
  }

  @Test(groups = { "AllEnv" })
  public void processMessageCommandを呼び出されたらコマンドを送信してきたクライアント以外にメッセージを送るよ(final Channel client) {
    final ChatServerModel model = new ChatServerModel();
    Deencapsulation.setField(model, "fClients", fGroup);

    new Expectations() {
      Channel fChannel1;
      Channel fChannel2;
      SocketAddress fAddress;
      {
        fGroup.iterator();
        result = Arrays.asList(fChannel1, client, fChannel2).iterator();
        client.remoteAddress();
        result = fAddress;
        fChannel1.write("[" + fAddress.toString() + "] test Message\n");
        client.write(anyString);
        times = 0;
        client.remoteAddress();
        result = fAddress;
        fChannel2.write("[" + fAddress.toString() + "] test Message\n");
      }
    };

    model.processMessageCommand(client, "test Message");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void processByeCommandの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.processByeCommand(null);
  }

  @Test(groups = { "AllEnv" })
  public void processByeCommandを呼び出されたらクライアントの切断処理を実行するよ(final Channel client) {
    final ChatServerModel model = new ChatServerModel();

    new Expectations() {
      {
        client.close();
      }
    };

    model.processByeCommand(client);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void processNameCommandの引数clientにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.processNameCommand(null, "aaa");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void processNameCommandの引数nickNameにnullが指定されたらIllegalArgumentExceptionを投げるよ(Channel client) {
    final ChatServerModel model = new ChatServerModel();
    model.processNameCommand(client, null);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void processNameCommandの引数nickNameに空文字列が指定されたらIllegalArgumentExceptionを投げるよ(Channel client) {
    final ChatServerModel model = new ChatServerModel();
    model.processNameCommand(client, "");
  }

  @Test(groups = { "AllEnv" })
  public void processNameCommandを呼び出されたらチャットに参加しているクライアント一覧に登録して他のクライアントに通知するよ(
      @Mocked("write") final EmbeddedChannel channel) throws Exception {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup g = Deencapsulation.getField(model, "fClients");

    model.processNameCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), "test1");
    model.processNameCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), "test2");
    model.processNameCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), "test3");

    new NonStrictExpectations() {
    };

    assertEquals(g.size(), 3);
    model.processNameCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), "test");
    assertEquals(g.size(), 4);

    new FullVerifications() {
      {
        channel.write("ENTER test\n");
        times = 3;
        channel.write("Welcome to Chat!\n");
        channel.write(withPrefix("It is "));
        channel.write("WELCOME test1,test2,test3\n");
      }
    };
  }

  @Test(groups = { "AllEnv" })
  public void processNameCommandを呼び出されたけど他のクライアントと名前が被ってたらRENAMEコマンドを送信するよ(
      @Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test1");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test2");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test3");

    new NonStrictExpectations() {
    };

    assertEquals(group.size(), 3);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test2");
    assertEquals(group.size(), 3);

    new FullVerificationsInOrder() {
      {
        channel.write("RENAME\n");
        times = 1;
      }
    };
  }

  @Test(groups = { "AllEnv" })
  public void processNameCommandを呼び出されたけどニックネームにServerを指定してたらRENAMEコマンドを送信するよ(
      @Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test");

    new NonStrictExpectations() {
    };

    assertEquals(group.size(), 1);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "Server");
    assertEquals(group.size(), 1);

    new FullVerificationsInOrder() {
      {
        channel.write("RENAME\n");
        times = 1;
      }
    };
  }

  @Test(groups = { "AllEnv" })
  public void processNameCommandを呼び出されたけどニックネームに半角記号のカンマやスペースやアットマークなどが入っていたらRENAMEコマンドを送信するよ(
      @Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test2");

    new Expectations() {
    };

    assertEquals(group.size(), 2);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test Name1");
    assertEquals(group.size(), 2);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "testN@ame2");
    assertEquals(group.size(), 2);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test,3");
    assertEquals(group.size(), 2);

    new FullVerificationsInOrder() {
      {
        channel.write("RENAME\n");
        times = 3;
      }
    };
  }
}
