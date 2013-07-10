package gmi.boardgame.server;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Eclipseの「ストリングの外部化」による自動生成。
 */
public class Messages {
  private static final String BUNDLE_NAME = "messages.ServerMessages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  private Messages() {
  }

  public static String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (final MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}