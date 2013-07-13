package gmi.boardgame.client;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ChannelUtilityTest {
  @Mocked
  private Channel fChannel;

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void setNickNameの引数channelにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    ChannelUtility.setNickName(null, "Test");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void setNickNameの引数nickNameにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    ChannelUtility.setNickName(fChannel, null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void setNickNameの引数nickNameに空文字列が指定されたらIllegalArgumentExceptionを投げるよ() {
    ChannelUtility.setNickName(fChannel, "");
  }

  @Test(groups = "AllEnv")
  public void setNickNameを呼び出されたらチャンネルにニックネームを設定するよ() {
    final NioSocketChannel channel = new NioSocketChannel();

    ChannelUtility.setNickName(channel, "test");
    assertEquals(channel.attr(ChannelUtility.KEY_NICKNAME).get(), "test");
  }

  @Test(groups = "AllEnv")
  public void setNickNameは何度もニックネームを設定できるよ() {
    final NioSocketChannel channel = new NioSocketChannel();

    ChannelUtility.setNickName(channel, "test1");
    ChannelUtility.setNickName(channel, "test2");
    assertEquals(channel.attr(ChannelUtility.KEY_NICKNAME).get(), "test2");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void getNickNameの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    ChannelUtility.getNickName(null);
  }

  @Test(groups = "AllEnv")
  public void setNickNameを呼び出されたけどチャンネルにニックネームが設定されていなかったら空文字列を返すよ() {
    final NioSocketChannel channel = new NioSocketChannel();

    assertEquals(ChannelUtility.getNickName(channel), "");
  }

  @Test(groups = "AllEnv")
  public void setNickNameを呼び出されたらチャンネルに設定されたニックネームを返すよ() {
    final NioSocketChannel channel = new NioSocketChannel();

    channel.attr(ChannelUtility.KEY_NICKNAME).set("testdesu");
    assertEquals(ChannelUtility.getNickName(channel), "testdesu");
  }
}
