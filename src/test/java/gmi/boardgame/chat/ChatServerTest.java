package gmi.boardgame.chat;

import mockit.Deencapsulation;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerTest {
  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServer server = new ChatServer();

    assertNotNull(Deencapsulation.getField(server, "fInjector"));
  }
}
