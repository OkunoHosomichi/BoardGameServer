package gmi.utils;

import java.util.Locale;
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
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(
      "gmi.utils.PreconditionsMessages", Locale.ROOT); //$NON-NLS-1$

  /**
   * 指定された引数がnullかどうかチェックし、nullならIllegalArgumentExceptionをスローします。
   * 
   * @param reference
   *          チェックする引数。
   *          nullを指定することを考慮してはいますがnullだとIllegalArgumentExceptionがスローされます。
   * @param argName
   *          チェックする引数の名前。nullを指定できません。
   * @throws IllegalArgumentException
   *           referenceがnullの場合。又はargNameがnullの場合。
   */
  public static final void checkNotNullArgument(@Nullable Object reference, @Nonnull String argName)
      throws IllegalArgumentException {
    checkArgument(argName != null, getString("NULL_EX"), "argName"); //$NON-NLS-1$
    checkArgument(reference != null, getString("NULL_EX"), argName); //$NON-NLS-1$
  }

  /**
   * 指定された引数が空文字列かどうかチェックし、空文字列ならIllegalArgumentExceptionをスローします。
   * 
   * @param string
   *          チェックする文字列。nullを指定できません。空文字列が指定されることを考慮してはいますが、
   *          空文字列だとIllegalArgumentExceptionがスローされます。
   * @param argName
   *          チェックする引数の名前。nullを指定できません。
   * @throws IllegalArgumentException
   *           string又はargNameがnullの場合。又はstringが空文字列の場合。
   */
  public static final void checkNotEmptyArgument(@Nullable String string, @Nonnull String argName)
      throws IllegalArgumentException {
    checkArgument(string != null, getString("NULL_EX"), "string"); //$NON-NLS-1$
    checkArgument(argName != null, getString("NULL_EX"), "argName"); //$NON-NLS-1$
    checkArgument(!string.isEmpty(), getString("EMPTY_EX"), argName); //$NON-NLS-1$
  }

  private static final String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (final MissingResourceException ex) {
      throw new RuntimeException(ex);
    }
  }
}
