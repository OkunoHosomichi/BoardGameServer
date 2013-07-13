package gmi.utils.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ChannelUtilitiesTest {
  @Mocked
  private Channel fChannel;

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void setNickNameの引数channelにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    ChannelUtilities.setNickName(null, "Test");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void setNickNameの引数nickNameにnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    ChannelUtilities.setNickName(fChannel, null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void setNickNameの引数nickNameに空文字列が指定されたらIllegalArgumentExceptionを投げるよ() {
    ChannelUtilities.setNickName(fChannel, "");
  }

  @Test(groups = "AllEnv")
  public void setNickNameを呼び出されたらチャンネルにニックネームを設定するよ() {
    final NioSocketChannel channel = new NioSocketChannel();

    ChannelUtilities.setNickName(channel, "test");
    assertEquals(channel.attr(ChannelUtilities.KEY_NICKNAME).get(), "test");
  }

  @Test(groups = "AllEnv")
  public void setNickNameは何度もニックネームを設定できるよ() {
    final NioSocketChannel channel = new NioSocketChannel();

    ChannelUtilities.setNickName(channel, "test1");
    ChannelUtilities.setNickName(channel, "test2");
    assertEquals(channel.attr(ChannelUtilities.KEY_NICKNAME).get(), "test2");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void getNickNameの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    ChannelUtilities.getNickName(null);
  }

  @Test(groups = "AllEnv")
  public void setNickNameを呼び出されたけどチャンネルにニックネームが設定されていなかったら空文字列を返すよ() {
    final NioSocketChannel channel = new NioSocketChannel();

    assertEquals(ChannelUtilities.getNickName(channel), "");
  }

  @Test(groups = "AllEnv")
  public void setNickNameを呼び出されたらチャンネルに設定されたニックネームを返すよ() {
    final NioSocketChannel channel = new NioSocketChannel();

    channel.attr(ChannelUtilities.KEY_NICKNAME).set("testdesu");
    assertEquals(ChannelUtilities.getNickName(channel), "testdesu");
  }
}
