package gmi.boardgame.chat;

import mockit.Deencapsulation;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerPresenterTest {
  @Mocked
  ChatModel fModel;
  @Mocked
  ChatView fView;

  @Test(expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerPresenter(null);
  }

  @Test
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatPresenter presenter = new ChatServerPresenter(fModel);

    assertTrue(Deencapsulation.getField(presenter, "fModel") == fModel);
  }
}
