package gmi.boardgame.chat;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.Verifications;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerPanelTest {
  @Mocked
  ChatServerPresenter fPresenter;

  @Test(expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerPanel(null);
  }

  @Test
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServerPanel panel = new ChatServerPanel(fPresenter);

    new Verifications() {
      {
        fPresenter.addChatView(panel);
      }
    };

    assertTrue(Deencapsulation.getField(panel, "fPresenter") == fPresenter);
  }
}
