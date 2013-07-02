package gmi.boardgame.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;

import javax.swing.JPanel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public final class ChatServer extends ChannelInboundHandlerAdapter {
  private final Injector fInjector;
  private final ChatModel fModel;

  public ChatServer() {
    fInjector = Guice.createInjector(new ChatServerModule());
    fModel = fInjector.getInstance(ChatModel.class);
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> msgs) throws Exception {
    super.messageReceived(ctx, msgs);
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
    }
  }
}
