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

  @Test(groups = { "LocalOnly" }, expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数groupにnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServer(null, fPipeline);
  }

  @Test(groups = { "LocalOnly" }, expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数pipelineにnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServer(fGroup, null);
  }

  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServer server = new ChatServer(fGroup, fPipeline);

    assertTrue(Deencapsulation.getField(server, "fGroup") == fGroup);
    assertNotNull(Deencapsulation.getField(server, "fPanel"));
    assertTrue(Deencapsulation.getField(server, "fPanel") instanceof ChatView);
    assertTrue(Deencapsulation.getField(server, "fPipeline") == fPipeline);
  }
}
