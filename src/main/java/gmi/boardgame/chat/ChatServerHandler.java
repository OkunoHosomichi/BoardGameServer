package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import javax.inject.Inject;

/**
 * クライアントからの通信を受け取るハンドラです。あくまでチャット部分用のハンドラなのでボードゲームに必要な通信は他のハンドラを作って使うことになります。
 * 現状はこのクラスで色々処理していますが、あくまでもハンドラなので実際の処理はモデルに殆どを任せることになります。
 * 本当はこのクラスはChatServerの内部クラスにしたかったのですが、 内部クラスにするとGoogle
 * Guiceのインジェクタを作る時に例外が発生するので分けました。 わざわざDIしなくてもいい気はするのですが、やりたかったのでこうなりました。
 * 
 * @author おくのほそみち
 */
final class ChatServerHandler extends ChannelInboundHandlerAdapter {
  /**
   * 接続したクライアントのグループ。モデルに移すことになると思います。
   */
  private static final ChannelGroup fChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  /**
   * 処理を任せていく予定のモデル。現時点では全く使っていません。
   */
  private final ChatModel fModel;

  /**
   * モデルを指定してインスタンスを作成します。
   * 
   * @param model
   *          処理を任せるモデル。nullを指定できません。
   * @throws NullPointerException
   *           modelがnullの場合。
   */
  @Inject
  ChatServerHandler(ChatModel model) throws NullPointerException {
    if (model == null) throw new NullArgumentException("model");

    fModel = model;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    fModel.joinClient(ctx.channel());
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> requests) throws Exception {
    final MessageList<String> msgs = requests.cast();
    for (int i = 0; i < msgs.size(); i++) {
      final String msg = msgs.get(i);
      // Send the received message to all channels but the current one.
      for (final Channel c : fChannels) {
        if (c != ctx.channel()) {
          c.write("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
        } else {
          c.write("[you] " + msg + '\n');
        }
      }

      // Close the connection if the client has sent 'bye'.
      if ("bye".equals(msg.toLowerCase())) {
        ctx.close();
      }
    }
    msgs.releaseAllAndRecycle();
  }
}