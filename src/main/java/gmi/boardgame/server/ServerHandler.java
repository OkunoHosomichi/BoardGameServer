package gmi.boardgame.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {
  static final ChannelGroup fChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
    ctx.write("It is " + new Date() + " now.\r\n");
    fChannels.add(ctx.channel());
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> requests) throws Exception {
    MessageList<String> msgs = requests.cast();
    for (int i = 0; i < msgs.size(); i++) {
      String msg = msgs.get(i);
      // Send the received message to all channels but the current one.
      for (Channel c : fChannels) {
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

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.err.println("Unexpected exception from downstream.");
    ctx.close();
  }
}
