package gmi.boardgame.chat;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ClientTest {

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void IDがnullだとNullPointerExceptionを投げるよ() {
    new Client(null, "test");
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void nickNameがnullだとNullPointerWxceptionを投げるよ() {
    new Client(Integer.valueOf(0), null);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { IllegalArgumentException.class })
  public void nickNameが空文字列だとIllegalArgumentExceptionを投げるよ() {

    new Client(Integer.valueOf(0), "");
  }

  @Test(groups = { "AllEnv" })
  public void それ以外だとちゃんとクラスを作るよ() {
    final Client instance = new Client(Integer.valueOf(0), "test");
    assertEquals(instance.getChannelID(), Integer.valueOf(0));
    assertEquals(instance.getNickName(), "test");
  }
}
