package gmi.boardgame.server;

import gmi.boardgame.chat.ChatServer;
import gmi.utils.netty.MyDelimiters;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * チャットサーバのウインドウです。サーバの接続待ち設定もこのクラスで行います。
 * 
 * @author おくのほそみち
 */
@SuppressWarnings("serial")
public final class ServerFrame extends JFrame {
  /**
   * convertNumberStringIntoPortNumber()で引数チェックに利用する正規表現。
   */
  private static final String CHECK_NUMBER_STRING_PATTERN = "^\\+?\\d+";
  /**
   * チャットサーバ。
   */
  private final ChatServer fChatServer;
  /**
   * 接続待ちするポート番号。
   */
  private final int fPortNumber;
  /**
   * サーバの設定。
   */
  private final ServerProperties fProperties = ServerProperties.INSTANCE;
  /**
   * サーバのチャンネル。切断処理に使う。
   */
  private Channel fServerChannel;

  /**
   * デフォルトのポート番号を指定してインスタンスを構築します。デフォルト値は60935番です。
   */
  public ServerFrame() {
    this(ServerProperties.DEFAULT_PORT_NUMBER);
  }

  /**
   * ポート番号を指定してインスタンスを構築します。ポート番号は49513～65535までの値を指定します。
   * 指定された番号が利用可能な範囲外の場合はデフォルトの番号を指定します。
   * 
   * @param portNumber
   *          クライアントの接続を待つポート番号。
   */
  public ServerFrame(int portNumber) {
    assert ServerProperties.PORT_RANGE.Contains(ServerProperties.DEFAULT_PORT_NUMBER);

    fPortNumber = ServerProperties.PORT_RANGE.Contains(portNumber) ? portNumber : ServerProperties.DEFAULT_PORT_NUMBER;

    fProperties.load();

    fChatServer = new ChatServer();
    layoutComponents();

    setTitle(Messages.getString("KEY_TITLE")); //$NON-NLS-1$
    setLocation(fProperties.getWindowLocation());
    setSize(fProperties.getWindowSize());

    addWindowListener(new WindowAdapter() {

      /*
       * (非 Javadoc)
       * 
       * @see
       * java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosing(WindowEvent e) {
        fProperties.setWindowLocation(getLocation());
        fProperties.setWindowSize(getSize());
        fProperties.store();

        // TODO:全クライアントにシャットダウンを伝えて切断を待つ処理を書くべき。
        // fServerChannel.close();
      }
    });

    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  /**
   * コンポーネントを配置します。
   */
  private void layoutComponents() {
    final Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(fChatServer.getPanel(), BorderLayout.CENTER);
  }

  /**
   * ネットワークを設定して接続待ち処理を開始します。 バックログ値を10に設定しています。
   */
  private void start() {
    final EventLoopGroup bossGroup = new NioEventLoopGroup();
    final EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      final ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, Integer.valueOf(10)).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              final ChannelPipeline pipeline = ch.pipeline();

              pipeline.addLast("framer",
                  new DelimiterBasedFrameDecoder(2048, MyDelimiters.lineDelimiter(ServerProperties.CHARSET)));
              pipeline.addLast("decoder", new StringDecoder(ServerProperties.CHARSET));
              pipeline.addLast("encoder", new StringEncoder(ServerProperties.CHARSET));

              pipeline.addLast("chathandler", fChatServer.createHandler());

              pipeline.addLast("handler", new ChannelInboundHandlerAdapter() {

                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                  System.err.println(Messages.getString("KEY_EXCEPTION_CAUGHT")); //$NON-NLS-1$
                  ctx.channel().close();
                }
              });

            }
          });
      fServerChannel = bootstrap.bind(fPortNumber).sync().channel();
      fServerChannel.closeFuture().sync();
    } catch (final InterruptedException ex) {
      // ChannelFutureのsync()で処理の終了を待ってるときに割り込みをかけられた場合。特になにもせず終了させていいと思う。
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  /**
   * メインメソッド。最初の引数にポート番号を渡すことができます。
   * 引数が無い又は最初の引数がポート番号ではないと判断した場合にはデフォルトのポート番号を使用します。
   * サーバのウインドウを作成して表示し、ネットワークを設定してクライアントからの接続待ち状態に入ります。
   * 
   * @param args
   *          最初の引数にポート番号を渡すことができます。
   */
  public static void main(String[] args) {
    final int port = (args.length > 0) ? convertNumberStringIntoPortNumber(args[0])
        : ServerProperties.DEFAULT_PORT_NUMBER;

    final ServerFrame frame = new ServerFrame(port);

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          frame.setVisible(true);
        } catch (final Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    });

    frame.fChatServer.notifyServerInformation(String.format(
        Messages.getString("KEY_LISTENSTART"), Integer.valueOf(port))); //$NON-NLS-1$
    frame.start();
  }

  /**
   * 指定された文字列をポート番号に変更します。文字列がnullだった場合や10進整数でなかった場合や負の整数だった場合はデフォルトのポート番号を返します。
   * 
   * @param str
   *          ポート番号の文字列表現。
   * @return ポート番号。
   */
  private static int convertNumberStringIntoPortNumber(String str) {
    if (str == null) return ServerProperties.DEFAULT_PORT_NUMBER;
    if (str.isEmpty()) return ServerProperties.DEFAULT_PORT_NUMBER;
    if (!Pattern.matches(CHECK_NUMBER_STRING_PATTERN, str)) return ServerProperties.DEFAULT_PORT_NUMBER;

    return Integer.parseInt(str);
  }
}