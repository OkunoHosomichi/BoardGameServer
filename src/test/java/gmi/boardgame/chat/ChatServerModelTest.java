package gmi.boardgame.chat;

import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChatServerModelTest {
  @Mocked
  private ChatServerPanel fChatPanel;

  @Test(expectedExceptions = { NullPointerException.class })
  public void joinClientの引数にnullが指定されたらNullPointerExceptionを投げるよ() {
    ChatServerModel model = new ChatServerModel();
    model.joinClient(null);
  }

  @Test
  public void joinClientを呼び出されたらクライアント一覧を更新してビューに通知するよ() {
    final ChatServerModel model = new ChatServerModel();
    model.addObserver(fChatPanel);

    new Expectations() {
      {
        fChatPanel.update(model, "clientList");
        fChatPanel.update(model, "clientList");
        fChatPanel.update(model, "clientList");
      }
    };

    model.joinClient(new Client(new Integer(0), "test01"));
    model.joinClient(new Client(new Integer(1), "test03"));
    model.joinClient(new Client(new Integer(2), "test02"));

    List<String> expected = new ArrayList<>();
    expected.add("test01");
    expected.add("test03");
    expected.add("test02");

    assertEquals(model.getClientList(), expected);
  }
}
