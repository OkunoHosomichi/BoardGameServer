package gmi.utils.netty;

import gmi.utils.exceptions.NullArgumentException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * Nettyのio.netty.handler.codec.DelimiterBasedFrameDecoderで利用する区切り文字を扱うクラスです。
 * NettyのlineDelimiterが文字コードによって正しい動作をしなくなるので、文字コードを指定できるようにしました。
 * 
 * @author おくのほそみち
 */
public final class MyDelimiters {
  /**
   * 指定された文字コードの区切り文字を返します。
   * 
   * @param charset
   *          対応する文字コード。nullを指定できません。
   * @return io.netty.handler.codec.DelimiterBasedFrameDecoderで利用できる区切り文字。
   *         改行コードでフレームを区切る。
   * @throws NullPointerException
   *           charsetがnullの場合。
   * @see io.netty.handler.codec.Delimiters#lineDelimiter()
   */

  public static ByteBuf[] lineDelimiter(Charset charset) throws NullPointerException {
    if (charset == null) throw new NullArgumentException("charset");

    return new ByteBuf[] { Unpooled.wrappedBuffer("\r\n".getBytes(charset)),
        Unpooled.wrappedBuffer("\n".getBytes(charset)), };
  }
}
