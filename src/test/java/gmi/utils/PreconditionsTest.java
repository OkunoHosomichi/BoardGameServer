package gmi.utils;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class PreconditionsTest {
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
  public void checkNotNullArgumentの引数がどちらもnullでないなら何も起きないよ() {
    Preconditions.checkNotNullArgument(new Object(), "test");
    assertTrue(true, "何も起きず普通にここまで来るはず。");
  }

  /*
   * checkNotEmptyArgument
   */
  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotEmptyArgumentの引数stringがnullならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument(null, "test");
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotEmptyArgumentの引数argNameがnullならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument("test", null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void checkNotEmptyArgumentの引数stringが空文字列ならIllegalArgumentExceptionをスローするよ() {
    Preconditions.checkNotEmptyArgument("", "test");
  }

  @Test(groups = "AllEnv")
  public void checkNotEmptyArgumentの引数がどちらもnullでなく引数stringが空文字列でないなら何も起きないよ() {
    Preconditions.checkNotEmptyArgument("tes", "test");
    assertTrue(true, "何も起きず普通にここまで来るはず。");
  }
}
