package gmi.utils;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

public class PreconditionsTest {
  /*
   * checkNotEmptyArgument
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotEmptyArgumentの引数argNameがnullならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument("test", null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotEmptyArgumentの引数stringがnullならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument(null, "test");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotEmptyArgumentの引数stringが空文字列ならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument("", "test");
  }

  @Test(groups = "AllEnv")
  public void checkNotEmptyArgumentの引数がどちらもnullでなく引数stringが空文字列でないなら引数自身を返すよ() {
    assertEquals(Preconditions.checkNotEmptyArgument("tes", "test"), "tes");
  }

  /*
   * checkNotNullArgument
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotNullArgumentの引数argNameがnullならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotNullArgument(new Object(), null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotNullArgumentの引数referenceがnullならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotNullArgument(null, "test");
  }

  @Test(groups = "AllEnv")
  public void checkNotNullArgumentの引数がどちらもnullでないなら引数自身を返すよ() {
    final Object object = new Object();
    assertSame(Preconditions.checkNotNullArgument(object, "object"), object);
  }

  /*
   * checkNotNullOrEmptyArgument
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotNullOrEmptyArgumentの引数argNameがnullならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument("test", null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotNullOrEmptyArgumentの引数stringがnullならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument(null, "test");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotNullOrEmptyArgumentの引数stringが空文字列ならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument("", "test");
  }

  @Test(groups = "AllEnv")
  public void checkNotNullOrEmptyArgumentの引数がどちらもnullでなく引数stringが空文字列でないなら引数自身を返すよ() {
    assertEquals(Preconditions.checkNotEmptyArgument("abc", "test"), "abc");
  }
}
