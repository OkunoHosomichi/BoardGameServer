package gmi.boardgame.server;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ServerPropertiesTest {
  private final Path fSettingFilePath = Paths.get(".", "target", "ServerSettings.xml").toAbsolutePath().normalize();

  @BeforeMethod(groups = "AllEnv")
  public void beforeMethod() throws IOException {
    Files.deleteIfExists(fSettingFilePath);
  }

  @AfterMethod(groups = "AllEnv")
  public void afterMethod() throws IOException {
    Files.deleteIfExists(fSettingFilePath);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void loadの引数にnullを指定されたらIllegalArgumentExceptionをスローするよ() throws IOException {
    ServerProperties.INSTANCE.load(null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void loadの引数に指定されたファイル名の拡張子がxmlじゃなかったらIllegalArgumentExceptionをスローするよ() throws IOException {
    ServerProperties.INSTANCE.load("test.txt");
  }

  @Test(groups = "AllEnv")
  public void loadを呼び出されても設定ファイルが無い場合は初期設定するよ() {
    ServerProperties.INSTANCE.load(fSettingFilePath.toString());

    final Point location = ServerProperties.INSTANCE.getWindowLocation();
    final Dimension size = ServerProperties.INSTANCE.getWindowSize();

    assertEquals(location.x, 0);
    assertEquals(location.y, 0);
    assertEquals(size.width, 250);
    assertEquals(size.height, 250);
  }

  @Test(groups = "AllEnv")
  public void loadを呼び出されても設定ファイルの形式がおかしい場合は初期設定するよ() throws IOException {
    Files.createFile(fSettingFilePath);

    ServerProperties.INSTANCE.load(fSettingFilePath.toString());

    final Point location = ServerProperties.INSTANCE.getWindowLocation();
    final Dimension size = ServerProperties.INSTANCE.getWindowSize();

    assertEquals(location.x, 0);
    assertEquals(location.y, 0);
    assertEquals(size.width, 250);
    assertEquals(size.height, 250);
  }

  @Test(groups = "AllEnv")
  public void loadを呼び出されたら設定ファイルから設定を読み込むよ() throws IOException {
    final List<String> list = Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>",
        "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">", "<properties>",
        "<comment>Properties File for Boardgame Server</comment>", "<entry key=\"WINDOW_H\">252</entry>",
        "<entry key=\"WINDOW_Y\">2</entry>", "<entry key=\"WINDOW_X\">1</entry>",
        "<entry key=\"WINDOW_W\">251</entry>", "</properties>");
    Files.write(fSettingFilePath, list, Charset.forName("UTF-8"));

    ServerProperties.INSTANCE.load(fSettingFilePath.toString());

    final Point location = ServerProperties.INSTANCE.getWindowLocation();
    final Dimension size = ServerProperties.INSTANCE.getWindowSize();

    assertEquals(location.x, 1);
    assertEquals(location.y, 2);
    assertEquals(size.width, 251);
    assertEquals(size.height, 252);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void storeの引数にnullを指定されたらIllegalArgumentExceptionをスローするよ() throws IOException {
    ServerProperties.INSTANCE.store(null);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void storeの引数に指定されたファイル名の拡張子がxmlじゃなかったらIllegalArgumentExceptionをスローするよ() throws IOException {
    ServerProperties.INSTANCE.store("test.txt");
  }

  @Test(groups = "AllEnv")
  public void storeを呼び出されたら設定ファイルに保存するよ() throws IOException {
    final List<String> list = Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>",
        "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">", "<properties>",
        "<comment>Properties File for Boardgame Server</comment>", "<entry key=\"WINDOW_H\">254</entry>",
        "<entry key=\"WINDOW_Y\">4</entry>", "<entry key=\"WINDOW_X\">3</entry>",
        "<entry key=\"WINDOW_W\">253</entry>", "</properties>");
    ServerProperties.INSTANCE.setWindowLocation(new Point(3, 4));
    ServerProperties.INSTANCE.setWindowSize(new Dimension(253, 254));
    ServerProperties.INSTANCE.store(fSettingFilePath.toString());

    assertEquals(Files.readAllLines(fSettingFilePath, Charset.forName("UTF-8")), list);
  }
}
