package gmi.boardgame.client;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import static com.google.common.base.Preconditions.checkArgument;
import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * チャンネルへの操作を行いやすくするためのユーティリティクラスです。
 * 
 * @author おくのほそみち
 */
public final class ChannelUtility {
  /**
   * ニックネーム取得/設定用のキー。
   */
  static final AttributeKey<String> KEY_NICKNAME = new AttributeKey<>("NickName");

  /**
   * 指定されたチャンネルに指定されたニックネームを設定します。
   * 
   * @param channel
   *          ニックネームを設定するチャンネル。nullを指定できません。
   * @param nickName
   *          設定するニックネーム。nullや空文字を指定できません。
   * @throws IllegalArgumentException
   *           channel又はnickNameがnullの場合。又はnickNameが空文字列の場合。
   */
  public static final void setNickName(Channel channel, String nickName) {
    checkNotNullArgument(channel, "channel");
    checkNotNullArgument(nickName, "nickName");
    checkArgument(!nickName.isEmpty(), "nickNameに空文字列を指定できません。");

    channel.attr(KEY_NICKNAME).set(nickName);
  }
}
