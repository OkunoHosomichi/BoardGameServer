package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;

import javax.inject.Inject;

/**
 * クライアントからの通信を受け取るハンドラです。あくまでチャット部分用のハンドラなのでボードゲームに必要な通信は他のハンドラを作って使うことになります。
 * あくまでもハンドラなので実際の処理はモデルに殆どを任せることになります。 本当はこのクラスはChatServerの内部クラスにしたかったのですが、
 * 内部クラスにするとGoogle Guiceのインジェクタを作る時に例外が発生するので分けました。
 * わざわざDIしなくてもいい気はするのですが、やりたかったのでこうなりました。
 * 
 * @author おくのほそみち
 */
final class ChatServerHandler extends ChannelInboundHandlerAdapter {
  /**
   * 処理を任せるモデル。
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
      fModel.processClientCommand(ctx.channel(), msgs.get(i));
    }
    msgs.releaseAllAndRecycle();
  }
}