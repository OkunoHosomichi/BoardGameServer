package gmi.boardgame.server;

import mockit.Deencapsulation;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ServerFrameTest {
  private static final int DEFAULT_PORT_NUMBER = 60935;

  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数portNumberに範囲外の値を指定したらデフォルトの番号を設定するよ() {
    ServerFrame frame = new ServerFrame(49512);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(DEFAULT_PORT_NUMBER));

    frame = new ServerFrame(65536);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(DEFAULT_PORT_NUMBER));

    frame = new ServerFrame(Integer.MIN_VALUE);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(DEFAULT_PORT_NUMBER));

    frame = new ServerFrame(Integer.MAX_VALUE);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(DEFAULT_PORT_NUMBER));
  }

  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数を正しく指定したらポート番号に設定するよ() {
    ServerFrame frame = new ServerFrame(49513);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(49513));

    frame = new ServerFrame(DEFAULT_PORT_NUMBER);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(DEFAULT_PORT_NUMBER));

    frame = new ServerFrame(65535);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(65535));
  }
}
