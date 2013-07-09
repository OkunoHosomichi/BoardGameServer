package gmi.boardgame.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mockit.Deencapsulation;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ServerFrameTest {
  private static final Integer DEFAULT_PORT_NUMBER = Integer.valueOf(60935);

  @Test(groups = { "GUIOnly" })
  public void コンストラクタの引数portNumberに範囲外の値を指定したらデフォルトの番号を設定するよ() {
    ServerFrame frame = new ServerFrame(49512);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), DEFAULT_PORT_NUMBER);

    frame = new ServerFrame(65536);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), DEFAULT_PORT_NUMBER);

    frame = new ServerFrame(Integer.MIN_VALUE);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), DEFAULT_PORT_NUMBER);

    frame = new ServerFrame(Integer.MAX_VALUE);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), DEFAULT_PORT_NUMBER);
  }

  @Test(groups = { "GUIOnly" })
  public void コンストラクタの引数を正しく指定したらポート番号に設定するよ() {
    ServerFrame frame = new ServerFrame(49513);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(49513));

    frame = new ServerFrame(DEFAULT_PORT_NUMBER.intValue());
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), DEFAULT_PORT_NUMBER);

    frame = new ServerFrame(65535);
    assertEquals(Deencapsulation.getField(frame, "fPortNumber"), Integer.valueOf(65535));

    assertNotNull(Deencapsulation.getField(frame, "fChatServer"));
  }

  @Test(groups = { "AllEnv" })
  public void convertNumberStringIntoPortNumberを呼ばれたら引数strをポート番号に変換するよ() throws NoSuchMethodException,
      SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Method convertNumverString = ServerFrame.class.getDeclaredMethod("convertNumberStringIntoPortNumber",
        String.class);
    convertNumverString.setAccessible(true);

    assertEquals(Integer.valueOf(45678), convertNumverString.invoke(ServerFrame.class, "45678"), "普通に想定している正の10進整数パターン");
    assertEquals(Integer.valueOf(56789), convertNumverString.invoke(ServerFrame.class, "+56789"), "先頭に+が付いていても認めるパターン");
  }

  @Test(groups = { "AllEnv" })
  public void convertNumberStringIntoPortNumberの引数strがnullだったらデフォルトの番号を返すよ() throws NoSuchMethodException,
      SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Method convertNumberString = ServerFrame.class.getDeclaredMethod("convertNumberStringIntoPortNumber",
        String.class);
    convertNumberString.setAccessible(true);

    assertEquals(convertNumberString.invoke(ServerFrame.class, (String) null), DEFAULT_PORT_NUMBER);
  }

  @Test(groups = { "AllEnv" })
  public void convertNumberStringIntoPortNumberの引数strが正の10進整数でなかったらだったらデフォルトの番号を返すよ() throws NoSuchMethodException,
      SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Method convertNumberString = ServerFrame.class.getDeclaredMethod("convertNumberStringIntoPortNumber",
        String.class);
    convertNumberString.setAccessible(true);

    assertEquals(convertNumberString.invoke(ServerFrame.class, ""), DEFAULT_PORT_NUMBER, "引数が空文字のパターン");
    assertEquals(convertNumberString.invoke(ServerFrame.class, "asdfg"), DEFAULT_PORT_NUMBER, "引数が数じゃない場合のパターン");
    assertEquals(convertNumberString.invoke(ServerFrame.class, "0x1f"), DEFAULT_PORT_NUMBER, "引数が10進数じゃない場合のパターン");
    assertEquals(convertNumberString.invoke(ServerFrame.class, "45678.9"), DEFAULT_PORT_NUMBER, "引数が小数のパターン");
    assertEquals(convertNumberString.invoke(ServerFrame.class, "-45678"), DEFAULT_PORT_NUMBER, "引数が負の10進整数のパターン");
    assertEquals(convertNumberString.invoke(ServerFrame.class, "++45678"), DEFAULT_PORT_NUMBER,
        "間違って先頭に+を2個つけたりしてるパターン");
  }
}
