package gmi.boardgame.validator;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class NicknameValidatorTest {
  /*
   * isValid
   */
  @Test(groups = "AllEnv")
  public void isValidが呼ばれたけどニックネームが予約されていたらfalseを返すよ() {
    assertFalse(NicknameValidator.isValid("server"), "小文字のみのパターン");
    assertFalse(NicknameValidator.isValid("SERVER"), "大文字のみのパターン");
    assertFalse(NicknameValidator.isValid("Server"), "混在してるパターン");
    assertFalse(NicknameValidator.isValid("System"), "Systemのパターン");
    assertFalse(NicknameValidator.isValid("Admin"), "Adminのパターン");
  }

  @Test(groups = "AllEnv")
  public void isValidが呼ばれたけどニックネームに不正な文字が含まれていたらfalseを返すよ() {
    assertFalse(NicknameValidator.isValid("te st"), "空白が含まれているパターン");
    assertFalse(NicknameValidator.isValid("t,est"), "カンマが含まれているパターン");
    assertFalse(NicknameValidator.isValid("test@"), "アットマークが含まれているパターン");
  }

  @Test(groups = "AllEnv")
  public void isValidが呼ばれて指定されたニックネームが妥当だったらtrueを返すよ() {
    assertTrue(NicknameValidator.isValid("Ichirou"));
    assertTrue(NicknameValidator.isValid("山田太郎"));
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void isValidの引数にnullが指定されたらIllegalArgumentExceptionを投げるよ() {
    NicknameValidator.isValid(null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void isValidの引数に空文字列が指定されたらIllegalArgumentExceptionを投げるよ() {
    NicknameValidator.isValid(null);
  }
}
