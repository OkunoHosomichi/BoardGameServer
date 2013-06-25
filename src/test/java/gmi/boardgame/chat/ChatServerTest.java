package gmi.boardgame.chat;

import io.netty.channel.group.ChannelGroup;
import mockit.Deencapsulation;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerTest {
  @Mocked
  ChannelGroup fGroup;

  @Test(groups = { "LocalOnly" }, expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServer(null);
  }

  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServer server = new ChatServer(fGroup);

    assertTrue(Deencapsulation.getField(server, "fGroup") == fGroup);
    assertNotNull(Deencapsulation.getField(server, "fPanel"));
    assertTrue(Deencapsulation.getField(server, "fPanel") instanceof ChatView);
  }
}
