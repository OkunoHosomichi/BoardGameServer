package gmi.boardgame.chat;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.Verifications;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerPanelTest {
  @Mocked
  ChatModel fModel;

  @Test(expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerPanel(null);
  }

  @Test
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatView panel = new ChatServerPanel(fModel);

    new Verifications() {
      {
        fModel.addChatView(panel);
      }
    };

    assertTrue(Deencapsulation.getField(panel, "fModel") == fModel);
  }
}
