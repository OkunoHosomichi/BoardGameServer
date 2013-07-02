package gmi.boardgame.chat;

import io.netty.channel.ChannelInboundHandler;

import javax.swing.JPanel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public final class ChatServer {
  private final Injector fInjector;

  public ChatServer() {
    fInjector = Guice.createInjector(new ChatServerModule());
  }

  public ChannelInboundHandler createHandler() {
    return fInjector.getInstance(ChannelInboundHandler.class);
  }

  /**
   * チャット画面を返します。
   * 
   * @return チャット画面のパネル。
   */
  public JPanel getPanel() {
    return fInjector.getInstance(JPanel.class);
  }

  /**
   * ビューやモデルを切り替えたいときはこのクラスのconfigure()を変更します。
   * 
   * @author おくのほそみち
   */
  private final class ChatServerModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(JPanel.class).to(ChatServerPanel.class).asEagerSingleton();
      bind(ChatModel.class).to(ChatServerModel.class).asEagerSingleton();
      bind(ChannelInboundHandler.class).to(ChatServerHandler.class);
    }
  }
}
