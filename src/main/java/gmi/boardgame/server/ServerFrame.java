package gmi.boardgame.server;

import gmi.boardgame.chat.ChatServer;
import gmi.utils.IntRange;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
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
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public final class ServerFrame extends JFrame {
  private static final Charset CHARSET = Charset.forName("UTF-16BE");
  /**
   * convertNumberStringIntoPortNumber()で引数チェックに利用する正規表現。
   */
  private static final String CHECK_NUMBER_STRING_PATTERN = "^\\+?\\d+";
  /**
   * デフォルトのポート番号。ポート番号は49513～65535までの値を指定します。
   */
  private static final int DEFAULT_PORT_NUMBER = 60935;
  /**
   * ポート番号に使える値の範囲。49513～65535までの値です。
   */
  private static final IntRange PORT_RANGE = new IntRange(49513, 65535);
  private final EventLoopGroup fBossGroup;
  private final ChatServer fChatServer;
  private final int fPortNumber;
  private final EventLoopGroup fWorkerGroup;

  /**
   * デフォルトのポート番号を指定してインスタンスを構築します。デフォルト値は60935番です。
   */
  public ServerFrame() {
    this(DEFAULT_PORT_NUMBER);
  }

  /**
   * ポート番号を指定してインスタンスを構築します。ポート番号は49513～65535までの値を指定します。
   * 指定された番号が利用可能な範囲外の場合はデフォルトの番号を指定します。
   * 
   * @param portNumber
   *          クライアントの接続を待つポート番号。
   */
  public ServerFrame(int portNumber) {
    assert PORT_RANGE.Contains(DEFAULT_PORT_NUMBER);

    fPortNumber = PORT_RANGE.Contains(portNumber) ? portNumber : DEFAULT_PORT_NUMBER;
    fBossGroup = new NioEventLoopGroup();
    fWorkerGroup = new NioEventLoopGroup();

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    fChatServer = new ChatServer();
    layoutComponents();
  }

  /**
   * コンポーネントを配置します。
   */
  private void layoutComponents() {
    final Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(fChatServer.getPanel(), BorderLayout.CENTER);
  }

  private ChannelFuture setup() {
    final ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(fBossGroup, fWorkerGroup).channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, Integer.valueOf(10)).childHandler(new ChannelInitializer<SocketChannel>() {

          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            final ChannelPipeline pipeline = ch.pipeline();

            pipeline.addLast("framer",
                new DelimiterBasedFrameDecoder(2048, new ByteBuf[] { Unpooled.wrappedBuffer("\r\n".getBytes(CHARSET)),
                    Unpooled.wrappedBuffer("\n".getBytes(CHARSET)) }));
            pipeline.addLast("decoder", new StringDecoder(CHARSET));
            pipeline.addLast("encoder", new StringEncoder(CHARSET));

            pipeline.addLast("handler", new ChannelInboundHandlerAdapter() {

              @Override
              public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                System.err.println("Unexpected exception from downstream.");
                ctx.close();
              }
            });

            pipeline.addLast("chathandler", fChatServer.createHandler());
          }
        });
    return bootstrap.bind(fPortNumber);
  }

  private void start(ChannelFuture future) throws InterruptedException {
    try {

      future.sync().channel().closeFuture().sync();
    } finally {
      fBossGroup.shutdownGracefully();
      fWorkerGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws InterruptedException, UnknownHostException {
    final int port = (args.length > 0) ? convertNumberStringIntoPortNumber(args[0]) : DEFAULT_PORT_NUMBER;

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

    final ChannelFuture f = frame.setup();
    frame.fChatServer.notifyServerInformation("ポート" + port + "で接続待ちを開始します。");
    frame.start(f);
  }

  /**
   * 指定された文字列をポート番号に変更します。文字列がnullだった場合や10進整数でなかった場合や負の整数だった場合はデフォルトのポート番号を返します。
   * 
   * @param str
   *          ポート番号の文字列表現。
   * @return ポート番号。
   */
  private static int convertNumberStringIntoPortNumber(String str) {
    if (str == null) return DEFAULT_PORT_NUMBER;
    if (str.isEmpty()) return DEFAULT_PORT_NUMBER;
    if (!Pattern.matches(CHECK_NUMBER_STRING_PATTERN, str)) return DEFAULT_PORT_NUMBER;

    return Integer.parseInt(str);
  }
}