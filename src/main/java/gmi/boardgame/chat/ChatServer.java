package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;

import javax.swing.JPanel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

public final class ChatServer {
  private final ChannelGroup fGroup;
  private final JPanel fPanel;
  private final ChannelPipeline fPipeline;

  /**
   * 指定されたチャンネルグループとパイプラインからインスタンスを作成します。
   * 
   * @param group
   *          チャンネルグループ。nullを指定できません。
   * @param pipeline
   *          パイプライン。nullを指定できません。
   * @throws NullPointerException
   *           group又はpipelineがnullの場合。
   */
  public ChatServer(ChannelGroup group, ChannelPipeline pipeline) throws NullPointerException {
    if (group == null) throw new NullArgumentException("group");
    if (pipeline == null) throw new NullArgumentException("pipeline");

    fGroup = group;
    fPipeline = pipeline;
    fPanel = Guice.createInjector(new ChatServerModule()).getInstance(JPanel.class);
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
   * モデルだけはtoInstance()を使っていますが、これはコンストラクタにChannelGroupを渡すためです。
   * 
   * @author おくのほそみち
   */
  private final class ChatServerModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(JPanel.class).to(ChatServerPanel.class).asEagerSingleton();
      bind(ChatPresenter.class).to(ChatServerPresenter.class).asEagerSingleton();
      bind(ChatModel.class).toInstance(new ChatServerModel(fGroup));
    }
  }
}
