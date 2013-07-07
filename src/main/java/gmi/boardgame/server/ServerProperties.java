package gmi.boardgame.server;

import gmi.utils.exceptions.NullArgumentException;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * サーバの設定をまとめて保持するクラスです。enumを利用してSingletonパターンにしています。
 * 
 * @author おくのほそみち
 */
public enum ServerProperties {
  INSTANCE;
  /**
   * チャットで使う文字セット。
   */
  public static final Charset CHARSET = Charset.forName("UTF-16BE");
  /**
   * ボードゲームサーバのバージョンを示す文字列。いずれクライアントとのバージョンチェックに使う予定の情報。現在は何の意味もありません。
   */
  public static final String VERSION = "Garbage";
  /**
   * 設定ファイルを読み込めなかった場合に設定される初期ウインドウ位置。
   * 不本意なことに可変クラスですがフィールドへの代入や変更するメソッドを呼ぶことは避けてください。
   */
  private static final Point INITIAL_WINDOW_LOCATION = new Point(0, 0);
  /**
   * 設定ファイルを読み込めなかった場合に設定される初期ウインドウサイズ。
   * 不本意なことに可変クラスですがフィールドへの代入や変更メソッドを呼ぶことは避けてください。
   */
  private static final Dimension INITIAL_WINDOW_SIZE = new Dimension(250, 250);
  /**
   * ウインドウの高さを取得/設定するためのキー文字列。
   */
  private static final String KEY_WINDOW_H = "WINDOW_H";
  /**
   * ウインドウの幅を取得/設定するためのキー文字列。
   */
  private static final String KEY_WINDOW_W = "WINDOW_W";
  /**
   * ウインドウ左上隅のX座標を取得/設定するためのキー文字列。
   */
  private static final String KEY_WINDOW_X = "WINDOW_X";
  /**
   * ウインドウ左上隅のY座標を取得/設定するためのキー文字列。
   */
  private static final String KEY_WINDOW_Y = "WINDOW_Y";
  /**
   * 設定ファイルの文字セット。
   */
  private static final String PROPERTIES_FILE_CHARSET = "UTF-8";
  /**
   * 設定ファイルに付けるコメント。
   */
  private static final String PROPERTIES_FILE_COMMENT = "Properties File for Boardgame Server";
  /**
   * 設定ファイルの拡張子。
   */
  private static final String PROPERTIES_FILE_EXTENTION = ".xml";
  /**
   * 設定ファイル名。
   */
  private static final String PROPERTIES_FILE_NAME = "ServerSettings" + PROPERTIES_FILE_EXTENTION;
  /**
   * 設定が変更されたか示すフィールド。変更されたならtrue、されてなければfalse。
   */
  private boolean fChange;
  /**
   * プロパティを保持するフィールド。
   */
  private final Properties fProperties;

  /**
   * インスタンスを構築します。
   */
  private ServerProperties() {
    fProperties = new Properties();
    fChange = false;
  }

  /**
   * ウインドウの位置を返します。
   * 
   * @return ウインドウの位置。不本意なことに可変クラスですが変更しても意味はないので変更しないでください。
   */
  public Point getWindowLocation() {
    final int x = Integer.parseInt(fProperties.getProperty(KEY_WINDOW_X,
        Integer.toString((int) INITIAL_WINDOW_LOCATION.getX())));
    final int y = Integer.parseInt(fProperties.getProperty(KEY_WINDOW_Y,
        Integer.toString((int) INITIAL_WINDOW_LOCATION.getY())));
    return new Point(x, y);
  }

  /**
   * ウインドウのサイズを返します。
   * 
   * @return ウインドウのサイズ。不本意なことに可変クラスですが変更しても意味はないので変更しないでください。
   */
  public Dimension getWindowSize() {
    final int width = Integer.parseInt(fProperties.getProperty(KEY_WINDOW_W,
        Integer.toString((int) INITIAL_WINDOW_SIZE.getWidth())));
    final int height = Integer.parseInt(fProperties.getProperty(KEY_WINDOW_H,
        Integer.toString((int) INITIAL_WINDOW_SIZE.getHeight())));
    return new Dimension(width, height);
  }

  /**
   * デフォルトのファイルから設定を読み込みます。ファイルが存在しなかった場合やファイルが存在しても読み込める形式でなかった場合は設定を初期化します。
   * 
   * @throws RuntimeException
   *           設定ファイルの読み込み時にIOExceptionがスローされた場合にラップしてスローします。
   */
  public void load() {
    assert PROPERTIES_FILE_NAME != null;
    assert PROPERTIES_FILE_NAME.endsWith(PROPERTIES_FILE_EXTENTION);

    load(PROPERTIES_FILE_NAME);
  }

  /**
   * 指定されたファイルから設定を読み込みます。ファイルが存在しなかった場合やファイルが存在しても読み込める形式でなかった場合は設定を初期化します。
   * 
   * @param fileName
   *          XMLファイルの名前。nullを指定できません。
   * @throws IllegalArgumentException
   *           fileNameがnullの場合。又はfileNameの拡張子が.xmlでなかった場合。
   * @throws RuntimeException
   *           設定ファイルの読み込み時にIOExceptionがスローされた場合にラップしてスローします。
   */
  public void load(String fileName) {
    if (fileName == null) throw new NullArgumentException("fileName");
    if (!fileName.endsWith(PROPERTIES_FILE_EXTENTION)) throw new IllegalArgumentException("拡張子が.xmlのファイル名を指定してください。");

    if (Files.notExists(Paths.get(fileName))) {
      initializeProperties();
      fChange = true;
      return;
    }

    try (InputStream in = new FileInputStream(fileName); BufferedInputStream input = new BufferedInputStream(in)) {
      assert input != null;

      fProperties.loadFromXML(input);
      fChange = false;
    } catch (final InvalidPropertiesFormatException ex) {
      initializeProperties();
      fChange = true;
      return;
    } catch (final IOException ex) {
      new RuntimeException(ex);
    }
  }

  /**
   * ウインドウの左上隅の座標を設定して変更フラグを立てます。
   * 
   * @param location
   *          ウインドウの左上隅の座標。nullを指定できません。
   * @throws IllegalArgumentException
   *           locationがnullの場合。
   */
  public void setWindowLocation(Point location) throws IllegalArgumentException {
    if (location == null) throw new NullArgumentException("location");

    final Point copy = new Point(location.x, location.y);

    fProperties.setProperty(KEY_WINDOW_X, Integer.toString(copy.x));
    fProperties.setProperty(KEY_WINDOW_Y, Integer.toString(copy.y));
    fChange = true;
  }

  /**
   * ウインドウのサイズを設定して変更フラグを立てます。
   * 
   * @param size
   *          ウインドウのサイズ。nullを指定できません。
   * @throws IllegalArgumentException
   *           sizeがnullの場合。
   */
  public void setWindowSize(Dimension size) throws IllegalArgumentException {
    if (size == null) throw new NullArgumentException("location");

    final Dimension copy = new Dimension(size.width, size.height);

    fProperties.setProperty(KEY_WINDOW_W, Integer.toString(copy.width));
    fProperties.setProperty(KEY_WINDOW_H, Integer.toString(copy.height));
    fChange = true;
  }

  /**
   * 変更されていればデフォルトのファイルに設定を保存します。
   * 
   * @throws RuntimeException
   *           設定ファイルへの保存時にIOExceptionがスローされた場合にラップしてスローします。
   */
  public void store() {
    assert PROPERTIES_FILE_NAME != null;
    assert PROPERTIES_FILE_NAME.endsWith(PROPERTIES_FILE_EXTENTION);

    store(PROPERTIES_FILE_NAME);
  }

  /**
   * 変更されていれば指定されたのファイルに設定を保存します。
   * 
   * @param fileName
   *          XMLファイルの名前。nullを指定できません。
   * @throws IllegalArgumentException
   *           fileNameがnullの場合。又はfileNameの拡張子が.xmlでなかった場合。
   * @throws RuntimeException
   *           設定ファイルへの保存時にIOExceptionがスローされた場合にラップしてスローします。
   */
  public void store(String fileName) {
    if (fileName == null) throw new NullArgumentException("fileName");
    if (!fileName.endsWith(PROPERTIES_FILE_EXTENTION)) throw new IllegalArgumentException();

    if (!fChange) return;

    try (OutputStream out = new FileOutputStream(fileName); BufferedOutputStream output = new BufferedOutputStream(out)) {
      assert output != null;

      fProperties.storeToXML(output, PROPERTIES_FILE_COMMENT, PROPERTIES_FILE_CHARSET);
      fChange = false;
    } catch (final IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * 設定を初期化します。
   */
  private void initializeProperties() {
    setWindowLocation(INITIAL_WINDOW_LOCATION);
    setWindowSize(INITIAL_WINDOW_SIZE);
    fChange = true;
  }
}