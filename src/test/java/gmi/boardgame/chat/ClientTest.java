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
    new Client(new Integer(0), null);
  }

  @Test(expectedExceptions = { IllegalArgumentException.class })
  public void nickNameが空文字列だとIllegalArgumentExceptionを投げるよ() {

    new Client(new Integer(0), "");
  }

  @Test
  public void それ以外だとちゃんとクラスを作るよ() {
    Client instance = new Client(new Integer(0), "test");
    assertEquals(instance.getChannelID(), new Integer(0));
    assertEquals(instance.getNickName(), "test");
  }
}
