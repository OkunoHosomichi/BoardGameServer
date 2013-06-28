package gmi.utils;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class IntRangeTest {
  @Test(groups = { "AllEnv" })
  public void コンストラクタを呼ばれた時に引数の最大値より最小値が大きかったら入れ替えてインスタンスを作るよ() {
    final IntRange range = new IntRange(Integer.MAX_VALUE, Integer.MIN_VALUE);

    assertEquals(range.max(), Integer.MAX_VALUE);
    assertEquals(range.min(), Integer.MIN_VALUE);
  }

  @Test(groups = { "AllEnv" })
  public void Containsを呼び出されたら引数の値が範囲の最小値以上最大値以下にあるか返すよ() {
    IntRange range = new IntRange(-10, 51);

    assertFalse(range.Contains(-11));
    assertTrue(range.Contains(-10));
    assertTrue(range.Contains(51));
    assertFalse(range.Contains(52));

    range = new IntRange(-121, -121);

    assertFalse(range.Contains(-122));
    assertTrue(range.Contains(-121));
    assertFalse(range.Contains(-120));
  }

  @Test(groups = { "AllEnv" })
  public void toStringを呼び出されたら範囲を文字列表現にして返すよ() {
    final IntRange range = new IntRange(Integer.MAX_VALUE, Integer.MIN_VALUE);
    assertEquals(range.toString(), Integer.MIN_VALUE + "～" + Integer.MAX_VALUE);
  }

}
