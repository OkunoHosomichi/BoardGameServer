package gmi.boardgame.chat;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import mockit.Deencapsulation;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerTest {
  @Mocked
  ChannelGroup fGroup;
  @Mocked
  ChannelPipeline fPipeline;

  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServer server = new ChatServer();

    assertNotNull(Deencapsulation.getField(server, "PANEL_INSTANCE"));
    assertTrue(Deencapsulation.getField(server, "PANEL_INSTANCE") instanceof ChatView);
  }
}
