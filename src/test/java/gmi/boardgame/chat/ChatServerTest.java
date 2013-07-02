package gmi.boardgame.chat;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

import static org.testng.Assert.*;

public class ChatServerTest {
  @Mocked
  ChatModel fModel;

  @Test(groups = { "LocalOnly" })
  public void コンストラクタの引数が正しく指定されたらちゃんとインスタンスを作るよ() {
    final ChatServer server = new ChatServer();

    assertNotNull(Deencapsulation.getField(server, "fInjector"));
  }

  @Test(groups = { "LocalOnly" }, expectedExceptions = { NullPointerException.class })
  public void notifyServerInformationの引数にnullを指定されたらNullPointerExceptionを投げるよ() {
    final ChatServer server = new ChatServer();
    Deencapsulation.setField(server, "fInjector", Guice.createInjector(new TestModule()));

    server.notifyServerInformation(null);
  }

  @Test(groups = { "LocalOnly" })
  public void notifyServerInformationの引数に空文字列を指定されたら何もしないよ() {
    final ChatServer server = new ChatServer();
    Deencapsulation.setField(server, "fInjector", Guice.createInjector(new TestModule()));

    new Expectations() {
      {
        fModel.updateInformation("");
        times = 0;
      }
    };

    server.notifyServerInformation("");
  }

  @Test(groups = { "LocalOnly" })
  public void notifyServerInformationが呼び出されたらmodelに通知するよ() {
    final ChatServer server = new ChatServer();
    Deencapsulation.setField(server, "fInjector", Guice.createInjector(new TestModule()));

    new Expectations() {
      {
        fModel.updateInformation("test");
      }
    };

    server.notifyServerInformation("test");
  }

  private final class TestModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(ChatModel.class).toInstance(fModel);
    }
  }
}
