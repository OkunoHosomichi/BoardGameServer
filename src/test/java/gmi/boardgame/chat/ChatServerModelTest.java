package gmi.boardgame.chat;

import gmi.utils.netty.channel.ChannelUtilities;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ChatServerModelTest {
  private static final String LINE_SEPARATOR = System.lineSeparator();

  /*
   * appendInformation
   */
  @Test(groups = "AllEnv")
  public void appendInformationを呼び出されたら通知文を更新してビューに通知するよ(final Observer observer) {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(observer);

    new Expectations() {
      {
        observer.update(model, "info");
        times = 2;
      }
    };

    Deencapsulation.invoke(model, "appendInformation", "Test");
    Deencapsulation.invoke(model, "appendInformation", "Information");

    assertEquals(model.getInformation(), "Test" + LINE_SEPARATOR + "Information" + LINE_SEPARATOR);
  }

  /*
   * getClientNames
   */
  @Test(groups = "AllEnv", expectedExceptions = UnsupportedOperationException.class)
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

  @Test(groups = "AllEnv", expectedExceptions = UnsupportedOperationException.class)
  public void getClientNamesが返すクライアント名一覧は追加不能だよ() {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    Deencapsulation.setField(model, "fClients", group);

    final Channel channel1 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    ChannelUtilities.setNickName(channel1, "aaa");
    group.add(channel1);

    model.getClientNames().add("asdfg");
  }

  @Test(groups = "AllEnv")
  public void getClientNamesを呼ばれたら変更不能のクライアント名一覧を返すよ() {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    Deencapsulation.setField(model, "fClients", group);

    final Channel channel1 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final Channel channel2 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final Channel channel3 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    ChannelUtilities.setNickName(channel1, "aaa");
    ChannelUtilities.setNickName(channel2, "eee");
    ChannelUtilities.setNickName(channel3, "ccc");
    group.add(channel1);
    group.add(channel2);
    group.add(channel3);

    final List<String> result = model.getClientNames();
    assertEquals(result, Arrays.asList("aaa", "ccc", "eee"));
  }

  /*
   * leaveClient
   */
  @Test(groups = "AllEnv")
  public void leaveClientを呼ばれたらクライアント一覧を更新してビューに通知するよ(final Observer observer) {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(observer);

    new Expectations() {
      {
        observer.update(model, "clients");
        times = 1;
      }
    };

    Deencapsulation.invoke(model, "leaveClient");
  }

  /*
   * processByeCommand
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processByeCommandの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processByeCommand(null);
  }

  @Test(groups = "AllEnv")
  public void processByeCommandを呼び出されたらクライアントの切断処理を実行するよ(final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();

    new Expectations() {
      {
        channel.close();
        times = 1;
      }
    };

    model.processByeCommand(channel);
  }

  /*
   * processClientCommand
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processClientCommandの引数clientにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processClientCommand(null, "BYE");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processClientCommandの引数commandにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processClientCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processClientCommandの引数commandに空文字列が指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processClientCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), "");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processClientCommandの引数commandの最初がスペースだったらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processClientCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), " Test");
  }

  @Test(groups = "AllEnv")
  public void processClientCommandが存在しないコマンドを指定して呼び出されたら何も実行せずfalseを返すよ() {
    // INFO:多分無いけどNotExistCommandを定義した場合には存在しないコマンドに変更する。
    assertFalse(new ChatServerModel().processClientCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()),
        "NotExistCommand NotExistArgument"), "存在しないコマンドを指定してるのでfalseを返すはずのパターン");
  }

  @Test(groups = "AllEnv")
  public void processClientCommandが存在するコマンドを指定して呼び出されたらそのコマンドを実行するよ(@Mocked final EmbeddedChannel channel) {
    // INFO:各コマンドはそれぞれのコマンドクラスでテストする。
    new Expectations() {
      {
        channel.close();
        times = 1;
      }
    };

    assertTrue(
        new ChatServerModel().processClientCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), "BYE"),
        "BYEコマンドを指定してるので実行するはずのパターン");
  }

  /*
   * processMessageCommand
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processMessageCommandの引数clientにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processMessageCommand(null, "aaa");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processMessageCommandの引数messageにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    final ChatServerModel model = new ChatServerModel();
    model.processMessageCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), null);
  }

  @Test(groups = "AllEnv")
  public void processMessageCommandの引数messageに空文字列が指定されたら何もしないよ(@Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    group.add(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()));
    group.add(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()));
    group.add(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()));
    final Channel client = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    group.add(client);

    new Expectations() {
      {
        channel.write(any);
        times = 0;
      }
    };

    model.processMessageCommand(client, "");
  }

  @Test(groups = "AllEnv")
  public void processMessageCommandを呼び出されたらコマンドを送信してきたクライアント以外にメッセージを送るよ(@Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");

    final EmbeddedChannel client1 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final EmbeddedChannel client2 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final EmbeddedChannel client3 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());

    ChannelUtilities.setNickName(client1, "client1");
    ChannelUtilities.setNickName(client2, "client2");
    ChannelUtilities.setNickName(client3, "client3");

    group.add(client1);
    group.add(client2);
    group.add(client3);

    new NonStrictExpectations() {
    };

    model.processMessageCommand(client2, "test Message");

    new FullVerifications() {
      {
        onInstance(client1).write("[client2] test Message\n");
        times = 1;
        onInstance(client3).write("[client2] test Message\n");
        times = 1;
      }
    };
  }

  /*
   * processNameCommand
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processNameCommandの引数clientにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processNameCommand(null, "aaa");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processNameCommandの引数nickNameにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processNameCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void processNameCommandの引数nickNameに空文字列が指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().processNameCommand(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()), "");
  }

  @Test(groups = "AllEnv")
  public void processNameCommandを呼び出されたけどニックネームにServerを指定してたらRENAMEコマンドを送信するよ(
      @Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test");

    new Expectations() {
      {
        channel.write("RENAME Server\n");
        times = 1;
      }
    };

    assertEquals(group.size(), 1);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "Server");
    assertEquals(group.size(), 1);
  }

  @Test(groups = "AllEnv")
  public void processNameCommandを呼び出されたけどニックネームに半角記号のカンマやスペースやアットマークなどが入っていたらRENAMEコマンドを送信するよ(
      @Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test2");

    new Expectations() {
      {
        channel.write("RENAME test Name1\n");
        times = 1;
        channel.write("RENAME testN@ame2\n");
        times = 1;
        channel.write("RENAME test,3\n");
        times = 1;
      }
    };

    assertEquals(group.size(), 2);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test Name1");
    assertEquals(group.size(), 2);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "testN@ame2");
    assertEquals(group.size(), 2);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test,3");
    assertEquals(group.size(), 2);
  }

  @Test(groups = "AllEnv")
  public void processNameCommandを呼び出されたけど他のクライアントと名前が被ってたらRENAMEコマンドを送信するよ(
      @Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test1");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test2");
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test3");

    new Expectations() {
      {
        channel.write("RENAME test2\n");
        times = 1;
      }
    };

    assertEquals(group.size(), 3);
    model.processNameCommand(new EmbeddedChannel(new ChannelInboundHandlerAdapter()), "test2");
    assertEquals(group.size(), 3);
  }

  @Test(groups = "AllEnv")
  public void processNameCommandを呼び出されたらチャットに参加しているクライアント一覧に登録してから他のクライアントに通知してビューにも更新を通知するよ(
      @Mocked final Observer observer, @Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(observer);
    final DefaultChannelGroup group = Deencapsulation.getField(model, "fClients");

    final EmbeddedChannel client1 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final EmbeddedChannel client2 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final EmbeddedChannel client3 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());
    final EmbeddedChannel client4 = new EmbeddedChannel(new ChannelOutboundHandlerAdapter());

    ChannelUtilities.setNickName(client1, "test1");
    ChannelUtilities.setNickName(client2, "test2");
    ChannelUtilities.setNickName(client3, "test3");

    group.add(client1);
    group.add(client2);
    group.add(client3);

    new NonStrictExpectations() {
    };

    assertEquals(group.size(), 3);
    model.processNameCommand(client4, "test4");
    assertEquals(group.size(), 4);

    new FullVerifications() {
      {
        onInstance(client1).write("ENTER test4\n");
        times = 1;
        onInstance(client2).write("ENTER test4\n");
        times = 1;
        onInstance(client3).write("ENTER test4\n");
        times = 1;
        onInstance(client4).write(
            withMatch("^WELCOME test1,test2,test3,test4\\n" + "MSG <Server> Welcome to Chat!\\n"
                + "MSG <Server> It is .* now\\.\\n$"));
        times = 1;
        observer.update(model, "clients");
        times = 1;
      }
    };
  }

  /*
   * sendServerMessage
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void sendServerMessageの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().sendServerMessage(null);
  }

  @Test(groups = "AllEnv")
  public void sendServerMessageの引数に空文字列が指定されたら何もしないよ(@Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    group.add(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()));
    group.add(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()));

    new Expectations() {
      {
        channel.write(any);
        times = 0;
      }
    };

    model.sendServerMessage("");
  }

  @Test(groups = "AllEnv")
  public void sendServerMessageを呼び出されたら各クライアントにサーバからのメッセージを送信するよ(@Mocked("write") final EmbeddedChannel channel) {
    final ChatServerModel model = new ChatServerModel();
    final ChannelGroup group = Deencapsulation.getField(model, "fClients");
    group.add(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()));
    group.add(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()));
    group.add(new EmbeddedChannel(new ChannelOutboundHandlerAdapter()));

    new Expectations() {
      {
        channel.write("<Server> これはテストです。\n");
        times = 3;
      }
    };

    model.sendServerMessage("これはテストです。");
  }

  /*
   * updateInformation
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void updateInformationの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    new ChatServerModel().updateInformation(null);
  }

  @Test(groups = "AllEnv")
  public void updateInformationの引数に空文字列が指定されても何もしないよ(final Observer observer) {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(observer);

    new Expectations() {
      {
        observer.update(model, any);
        times = 0;
      }
    };

    model.updateInformation("");

    assertTrue(model.getInformation().isEmpty());
  }

  @Test(groups = "AllEnv")
  public void updateInformationを呼び出されたら通知文を更新してビューに通知するよ(final Observer observer) {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(observer);

    new Expectations() {
      {
        observer.update(model, "info");
        times = 3;
      }
    };

    model.updateInformation("test01");
    model.updateInformation("test02");
    model.updateInformation("test03");

    assertEquals(model.getInformation(), "test01" + LINE_SEPARATOR + "test02" + LINE_SEPARATOR + "test03"
        + LINE_SEPARATOR);
  }
}
