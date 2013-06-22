package gmi.boardgame.chat;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ClientTest {

  @Test(expectedExceptions = { NullPointerException.class })
  public void IDがnullだとNullPointerExceptionを投げるよ() {
    new Client(null, "test");
  }

  @Test(expectedExceptions = { NullPointerException.class })
  public void nickNameがnullだとNullPointerWxceptionを投げるよ() {
    new Client(Integer.valueOf(0), null);
  }

  @Test(expectedExceptions = { IllegalArgumentException.class })
  public void nickNameが空文字列だとIllegalArgumentExceptionを投げるよ() {

    new Client(Integer.valueOf(0), "");
  }

  @Test
  public void それ以外だとちゃんとクラスを作るよ() {
    final Client instance = new Client(Integer.valueOf(0), "test");
    assertEquals(instance.getChannelID(), Integer.valueOf(0));
    assertEquals(instance.getNickName(), "test");
  }
}
