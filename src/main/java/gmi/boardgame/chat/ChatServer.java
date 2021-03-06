package gmi.boardgame.chat;

import io.netty.channel.ChannelInboundHandler;

import javax.swing.JPanel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * ビューやモデルを生成するクラスです。他パッケージのクラスはChatServerを通じてチャットのパネルやハンドラを取得することが出来ます。
 * 実際に取得できるインスタンスはDIにより構築されます
 * 。チャットサーバはMVCパターンを利用していて、JPanelがビューとコントローラ、ChatModelがモデルに相当します。
 * しかし、例えば「クライアントをキックした時のサーバ情報を赤文字で表示させたい
 * 」などを考えたときにはプレゼンテーションクラスを作成してプレゼンテーションモデルパターンに変更するかもしれません。
 * ビューのモデルへの登録は自動で行われるため
 * 、ChatServer#getPanel()によりJPanelのインスタンスを取得した後に特別にしないといけないことはありません。
 * 
 * @author おくのほそみち
 */
public final class ChatServer {
  /**
   * DI用のインジェクタ。
   */
  private final Injector fInjector;

  /**
   * インスタンスを構築します。
   */
  public ChatServer() {
    fInjector = Guice.createInjector(new ChatServerModule());
  }

  /**
   * ネットワークのイベントを処理するハンドラを返します。
   * 
   * @return ハンドラ。
   */
  public ChannelInboundHandler createHandler() {
    return fInjector.getInstance(ChannelInboundHandler.class);
  }

  /**
   * チャット画面(ビュー)を返します。モデルにはビューを登録済みです。
   * 
   * @return チャット画面のパネル。
   */
  public JPanel getPanel() {
    return fInjector.getInstance(JPanel.class);
  }

  /**
   * 指定された文字列でサーバ情報を更新します。空文字の場合は何もしません。
   * 
   * @param info
   *          サーバ情報。nullを指定できません。
   * @throws IllegalArgumentException
   *           messageがnullの場合。
   */
  public void notifyServerInformation(String info) throws IllegalArgumentException {
    checkNotNullArgument(info, "info");
    if (info.isEmpty()) return;

    fInjector.getInstance(ChatModel.class).updateInformation(info);
  }

  /**
   * DIのためのモジュールです。ビューやモデルやハンドラを切り替えたいときはconfigureメソッドを変更します。
   * ビューとモデルはシングルトンにしますが、ハンドラのインスタンスはその都度生成します。
   * 
   * @author おくのほそみち
   */
  private final class ChatServerModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(JPanel.class).to(ChatServerView.class).asEagerSingleton();
      bind(ChatModel.class).to(ChatServerModel.class).asEagerSingleton();
      bind(ChannelInboundHandler.class).to(ChatServerHandler.class);
    }
  }
}
