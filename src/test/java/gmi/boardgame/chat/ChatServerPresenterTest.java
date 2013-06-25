package gmi.boardgame.chat;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerPresenterTest {
  @Mocked
  ChatModel fModel;
  @Mocked
  ChatView fView;

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void コンストラクタの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerPresenter(null);
  }

  @Test(groups = { "AllEnv" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatPresenter presenter = new ChatServerPresenter(fModel);

    assertTrue(Deencapsulation.getField(presenter, "fModel") == fModel);
  }

  @Test(groups = { "AllEnv" }, expectedExceptions = { NullPointerException.class })
  public void addChatViewの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    new ChatServerPresenter(fModel).addChatView(null);
  }

  @Test(groups = { "AllEnv" })
  public void addChatViewを呼び出されたらビューを登録するよ() {
    final ChatServerPresenter presenter = new ChatServerPresenter(fModel);

    assertEquals(presenter.countObservers(), 0);

    new Expectations() {
      {
        fModel.addObserver(fView);
      }
    };

    presenter.addChatView(fView);

    assertEquals(presenter.countObservers(), 1);
  }
}
