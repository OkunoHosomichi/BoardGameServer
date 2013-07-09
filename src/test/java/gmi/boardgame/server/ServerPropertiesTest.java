package gmi.boardgame.server;

import gmi.utils.exceptions.NullArgumentException;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import mockit.Deencapsulation;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ServerPropertiesTest {
  private static final Path Cテスト用ルートディレクトリ = Paths.get("target/test").normalize();
  private static final Path C読み込みテスト用ディレクトリ = Cテスト用ルートディレクトリ.resolve(Paths.get("test01")).normalize();
  private static final Path C書き込みテスト用ディレクトリ = Cテスト用ルートディレクトリ.resolve(Paths.get("test02")).normalize();
  private static final Path C存在しない設定ファイル = C読み込みテスト用ディレクトリ.resolve(Paths.get("Settings01.xml")).normalize();
  private static final Path C存在するけど形式がおかしい設定ファイル = C読み込みテスト用ディレクトリ.resolve(Paths.get("Settings02.xml")).normalize();
  private static final Path C存在して読み込み可能な設定ファイル = C読み込みテスト用ディレクトリ.resolve(Paths.get("Settings03.xml")).normalize();
  private static final Path C読み込める設定ファイルかと思ったらディレクトリだった = C読み込みテスト用ディレクトリ.resolve(Paths.get("Settings04.xml"))
      .normalize();
  private static final Path C設定ファイルが読み込み不能だった = C書き込みテスト用ディレクトリ.resolve(Paths.get("Settings05.xml")).normalize();
  private static final Path C保存可能な設定ファイル = C書き込みテスト用ディレクトリ.resolve(Paths.get("Settings01.xml")).normalize();
  private static final Path C書き込める設定ファイルかと思ったらディレクトリだった = C書き込みテスト用ディレクトリ.resolve(Paths.get("Settings02.xml"))
      .normalize();
  private static final Path C設定ファイルが書き込み不能だった = C書き込みテスト用ディレクトリ.resolve(Paths.get("Settings03.xml")).normalize();

  @BeforeClass(groups = "AllEnv")
  public void makeTestDirectories() throws IOException {
    if (Files.exists(Cテスト用ルートディレクトリ)) DirectoryDeleter.delete(Cテスト用ルートディレクトリ);

    Files.createDirectories(C読み込みテスト用ディレクトリ);
    Files.createDirectories(C書き込みテスト用ディレクトリ);
    Files.createDirectories(C読み込める設定ファイルかと思ったらディレクトリだった);
    Files.createDirectories(C書き込める設定ファイルかと思ったらディレクトリだった);

    Files.createFile(C存在するけど形式がおかしい設定ファイル);
    Files.write(C存在して読み込み可能な設定ファイル, Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>",
        "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">", "<properties>",
        "<comment>Properties File for Boardgame Server</comment>", "<entry key=\"WINDOW_H\">252</entry>",
        "<entry key=\"WINDOW_Y\">2</entry>", "<entry key=\"WINDOW_X\">1</entry>",
        "<entry key=\"WINDOW_W\">251</entry>", "</properties>"), Charset.forName("UTF-8"));
    Files.createFile(C設定ファイルが書き込み不能だった);
    C設定ファイルが書き込み不能だった.toFile().setWritable(false);
  }

  @AfterClass(groups = "AllEnv")
  public void cleanTestDirectories() throws IOException {
    DirectoryDeleter.delete(Cテスト用ルートディレクトリ);
  }

  @Test(groups = "AllEnv")
  public void getWindowLocationを呼ばれても設定にない場合は初期設定位置を返すよ() {
    Deencapsulation.setField(ServerProperties.INSTANCE, "fProperties", new Properties());

    final Point actual = ServerProperties.INSTANCE.getWindowLocation();

    assertEquals(actual.x, 0);
    assertEquals(actual.y, 0);
  }

  @Test(groups = "AllEnv")
  public void getWindowSizeを呼ばれても設定にない場合は初期設定サイズを返すよ() {
    Deencapsulation.setField(ServerProperties.INSTANCE, "fProperties", new Properties());

    final Dimension actual = ServerProperties.INSTANCE.getWindowSize();

    assertEquals(actual.width, 250);
    assertEquals(actual.height, 250);
  }

  @Test(groups = "AllEnv")
  public void setWindowLocationとgetWindowLocationの動作確認だよ() {
    ServerProperties.INSTANCE.setWindowLocation(new Point(845, 15));
    final Point actual = ServerProperties.INSTANCE.getWindowLocation();

    assertEquals(actual.x, 845);
    assertEquals(actual.y, 15);
  }

  @Test(groups = "AllEnv")
  public void setWindowSizeとgetWindowSizeの動作確認だよ() {
    ServerProperties.INSTANCE.setWindowSize(new Dimension(561, 3126));
    final Dimension actual = ServerProperties.INSTANCE.getWindowSize();

    assertEquals(actual.width, 561);
    assertEquals(actual.height, 3126);
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
    ServerProperties.INSTANCE.load(C存在しない設定ファイル.toString());

    final Point location = ServerProperties.INSTANCE.getWindowLocation();
    final Dimension size = ServerProperties.INSTANCE.getWindowSize();

    assertEquals(location.x, 0);
    assertEquals(location.y, 0);
    assertEquals(size.width, 250);
    assertEquals(size.height, 250);
  }

  @Test(groups = "AllEnv")
  public void loadを呼び出されても設定ファイルの形式がおかしい場合は初期設定するよ() throws IOException {
    ServerProperties.INSTANCE.load(C存在するけど形式がおかしい設定ファイル.toString());

    final Point location = ServerProperties.INSTANCE.getWindowLocation();
    final Dimension size = ServerProperties.INSTANCE.getWindowSize();

    assertEquals(location.x, 0);
    assertEquals(location.y, 0);
    assertEquals(size.width, 250);
    assertEquals(size.height, 250);
  }

  @Test(groups = "AllEnv")
  public void loadを呼び出されたら設定ファイルから設定を読み込むよ() throws IOException {
    ServerProperties.INSTANCE.load(C存在して読み込み可能な設定ファイル.toString());

    final Point location = ServerProperties.INSTANCE.getWindowLocation();
    final Dimension size = ServerProperties.INSTANCE.getWindowSize();

    assertEquals(location.x, 1);
    assertEquals(location.y, 2);
    assertEquals(size.width, 251);
    assertEquals(size.height, 252);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void loadが呼び出されたけど設定ファイルと同名のフォルダがあって読み込めない時はIllegalArgumentExceptionをスローするよ() throws IOException {
    ServerProperties.INSTANCE.load(C読み込める設定ファイルかと思ったらディレクトリだった.toString());
  }

  /*
   * Windowsでは読み込み可否設定はできないっぽい
   */
  @Test(groups = "LinuxOnly", expectedExceptions = IllegalArgumentException.class)
  public void loadが呼び出されたけど設定ファイルが読み込み不能な時はIllegalArgumentExceptionをスローするよ() throws IOException {
    Files.createFile(C設定ファイルが読み込み不能だった);
    C設定ファイルが読み込み不能だった.toFile().setReadable(false);

    ServerProperties.INSTANCE.load(C設定ファイルが読み込み不能だった.toString());
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
    ServerProperties.INSTANCE.setWindowLocation(new Point(3, 4));
    ServerProperties.INSTANCE.setWindowSize(new Dimension(253, 254));
    ServerProperties.INSTANCE.store(C保存可能な設定ファイル.toString());

    final List<String> actual = Files.readAllLines(C保存可能な設定ファイル, Charset.forName("UTF-8"));
    final List<String> expected = Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>",
        "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">", "<properties>",
        "<comment>Properties File for Boardgame Server</comment>", "<entry key=\"WINDOW_H\">254</entry>",
        "<entry key=\"WINDOW_Y\">4</entry>", "<entry key=\"WINDOW_X\">3</entry>",
        "<entry key=\"WINDOW_W\">253</entry>", "</properties>");

    assertEquals(actual, expected);
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void storeが呼び出されたけど設定ファイルと同名のフォルダがあって書き込めない時はIllegalArgumentExceptionをスローするよ() throws IOException {
    ServerProperties.INSTANCE.setWindowLocation(new Point(1, 1));
    ServerProperties.INSTANCE.store(C書き込める設定ファイルかと思ったらディレクトリだった.toString());
  }

  @Test(groups = "AllEnv", expectedExceptions = IllegalArgumentException.class)
  public void storeが呼び出されたけど設定ファイルが書き込み不能な時はIllegalArgumentExceptionをスローするよ() throws IOException {
    ServerProperties.INSTANCE.setWindowLocation(new Point(1, 1));
    ServerProperties.INSTANCE.store(C設定ファイルが書き込み不能だった.toString());
  }

  final static class DirectoryDeleter {
    static void delete(Path directory) {

      if (directory == null) throw new NullArgumentException("directory");
      if (Files.notExists(directory)) throw new IllegalArgumentException("引数directoryには実在するパスを渡してください。");
      if (!Files.isDirectory(directory)) throw new IllegalArgumentException("引数directoryにはディレクトリを渡してください。");

      try {
        Files.walkFileTree(directory, new DeleteVisitor());
      } catch (final IOException ex) {
        throw new RuntimeException(ex);
      }
    }

    private static class DeleteVisitor extends SimpleFileVisitor<Path> {
      @Override
      public FileVisitResult postVisitDirectory(Path directory, IOException ex) throws IOException {

        if (ex != null) throw ex;

        Files.delete(directory);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes atts) throws IOException {

        file.toFile().setWritable(true);
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }
    }
  }
}
