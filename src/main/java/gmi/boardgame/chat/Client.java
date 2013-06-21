package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

/**
 * チャットに接続したクライアントの情報を保持するクラスです。 不変クラスです。
 * 
 * @author おくのほそみち
 */
public final class Client {
  private final Integer fChannelID;
  private final String fNickName;

  /**
   * 指定されたチャンネルIDとニックネームからインスタンスを構築します。
   * 
   * @param channelID
   *          チャンネルID値。null値を指定できません。
   * @param nickName
   *          クライアントのニックネーム。null値や空文字列を指定できません。
   * @throws NullPointerException
   *           channelIDもしくはnickNameがnullの場合。
   * @throws IllegalArgumentException
   *           nickNameが空文字列の場合。
   */
  public Client(Integer channelID, String nickName) throws NullPointerException, IllegalArgumentException {
    if (channelID == null) throw new NullArgumentException("channelID");
    if (nickName == null) throw new NullArgumentException("nickName");
    if (nickName.isEmpty()) throw new IllegalArgumentException("nickNameに空文字列を指定できません。");

    fChannelID = channelID;
    fNickName = nickName;
  }

  /**
   * チャンネルIDを返します。
   * 
   * @return チャンネルID値。nullではありません。
   */
  public Integer getChannelID() {
    assert fChannelID != null;

    return fChannelID;
  }

  /**
   * ニックネームを返します。
   * 
   * @return クライアントのニックネーム。nullでも空文字でもありません。
   */
  public String getNickName() {
    assert fNickName != null;
    assert !fNickName.isEmpty();

    return fNickName;
  }
}
