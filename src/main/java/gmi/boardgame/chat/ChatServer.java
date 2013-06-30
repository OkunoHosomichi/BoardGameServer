package gmi.boardgame.chat;

import javax.swing.JPanel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

public final class ChatServer {
  private static final JPanel PANEL_INSTANCE = Guice.createInjector(new ChatServerModule()).getInstance(JPanel.class);

  /**
   * チャット画面を返します。
   * 
   * @return チャット画面のパネル。
   */
  public static JPanel getPanel() {
    assert PANEL_INSTANCE != null;

    return PANEL_INSTANCE;
  }

  /**
   * ビューやモデルを切り替えたいときはこのクラスのconfigure()を変更します。
   * モデルだけはtoInstance()を使っていますが、これはコンストラクタにChannelGroupとChannelPipelineを渡すためです。
   * 
   * @author おくのほそみち
   */
  private static final class ChatServerModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(JPanel.class).to(ChatServerPanel.class).asEagerSingleton();
      bind(ChatModel.class).to(ChatServerModel.class).asEagerSingleton();
    }
  }
}
