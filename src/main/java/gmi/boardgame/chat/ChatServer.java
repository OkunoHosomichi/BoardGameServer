package gmi.boardgame.chat;

import javax.swing.JPanel;

import com.google.inject.AbstractModule;

public final class ChatServer {
  private final JPanel fPanel;

  /**
   * インスタンスを構築します。
   */
  public ChatServer() {
  }

  /**
   * チャット画面を返します。
   * 
   * @return チャット画面のパネル。nullではありません。
   */
  public JPanel getPanel() {
    assert fPanel != null;

    return fPanel;
  }

  /**
   * ビュー、モデル、プレゼンテーションモデルを切り替えたいときはこのクラスのconfigure()を変更します。
   * モデルだけはtoInstance()を使っていますが、これはコンストラクタにChannelGroupとChannelPipelineを渡すためです。
   * 
   * @author おくのほそみち
   */
  private final class ChatServerModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(JPanel.class).to(ChatServerPanel.class).asEagerSingleton();
      bind(ChatModel.class).to(ChatServerModel.class).asEagerSingleton();
    }
  }
}
