package gmi.utils;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class PreconditionsTest {
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
}
