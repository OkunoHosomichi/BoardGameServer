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
   *          チャンネルID値。nullを指定できません。
   * @param nickName
   *          クライアントのニックネーム。nullや空文字列を指定できません。
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

  /*
   * (非 Javadoc) eclipseにより自動生成したメソッドです。
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Client other = (Client) obj;
    if (fChannelID == null) {
      if (other.fChannelID != null) return false;
    } else if (!fChannelID.equals(other.fChannelID)) return false;
    if (fNickName == null) {
      if (other.fNickName != null) return false;
    } else if (!fNickName.equals(other.fNickName)) return false;
    return true;
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

  /*
   * (非 Javadoc) eclipseにより自動生成したメソッドです。
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fChannelID == null) ? 0 : fChannelID.hashCode());
    result = prime * result + ((fNickName == null) ? 0 : fNickName.hashCode());
    return result;
  }
}
