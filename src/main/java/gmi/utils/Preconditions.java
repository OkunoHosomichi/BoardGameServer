package gmi.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 引数のチェックをするクラスです。
 * 
 * @author おくのほそみち
 */
public final class Preconditions {
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("gmi.utils.PreconditionsMessages"); //$NON-NLS-1$

  /**
   * インスタンスを構築できないようにprivateにしています。
   */
  private Preconditions() {
  }

  /**
   * 指定された文字列が空文字列かどうかチェックします。空文字列ならIllegalArgumentExceptionをスローし、
   * 空文字列でなければ文字列自身を返します。
   * 
   * @param string
   *          チェックする文字列。nullを指定できません。
   * @param argName
   *          チェックする引数の名前。nullを指定できません。
   * @return string自身。
   * @throws IllegalArgumentException
   *           string又はargNameがnullの場合。又はstringが空文字列の場合。
   */
  public static final String checkNotEmptyArgument(@Nonnull String string, @Nonnull String argName) {
    checkArgument(string != null, getString("NULL_EX"), "string"); //$NON-NLS-1$
    checkArgument(argName != null, getString("NULL_EX"), "argName"); //$NON-NLS-1$

    if (string.isEmpty()) {
      throw new IllegalArgumentException(getFormattedString("EMPTY_EX", argName));
    } else {
      return string;
    }
  }

  /**
   * 指定された引数がnullかどうかチェックします。nullならIllegalArgumentExceptionをスローし、
   * nullでなければ引数自身を返します。
   * 
   * @param reference
   *          チェックする引数。
   * @param argName
   *          チェックする引数の名前。nullを指定できません。
   * @return reference自身。
   * @throws IllegalArgumentException
   *           referenceがnullの場合。又はargNameがnullの場合。
   */
  public static final <T> T checkNotNullArgument(@Nullable T reference, @Nonnull String argName) {
    checkArgument(argName != null, getString("NULL_EX"), "argName"); //$NON-NLS-1$

    if (reference == null) {
      throw new IllegalArgumentException(getFormattedString("NULL_EX", argName));
    } else {
      return reference;
    }
  }

  /**
   * 指定された文字列がnullでも空文字列でもないことをチェックします。null又は空文字列ならIllegalArgumentExceptionをスローし
   * 、nullでも空文字列でもなければ文字列自身を返します。
   * 
   * @param string
   *          チェックする文字列。
   * @param argName
   *          チェックする引数の名前。nullを指定できません。
   * @return string自身。
   * @throws IllegalArgumentException
   *           string又はargNameがnullの場合。又はstringが空文字列の場合。
   */
  public static final String checkNotNullOrEmptyArgument(@Nullable String string, @Nonnull String argName) {
    checkArgument(argName != null, getString("NULL_EX"), "argName"); //$NON-NLS-1$

    return checkNotEmptyArgument(checkNotNullArgument(string, argName), argName);
  }

  private static final String getFormattedString(String key, String argName) {
    return String.format(getString(key), argName);
  }

  private static final String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (final MissingResourceException ex) {
      throw new RuntimeException(ex);
    }
  }
}
