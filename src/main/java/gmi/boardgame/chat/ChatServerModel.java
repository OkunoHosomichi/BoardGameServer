package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

import java.util.Observable;

import javax.inject.Inject;

final class ChatServerModel extends Observable implements ChatModel {
  /**
   * 行の区切り文字。
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  /**
   * ユーザに通知するメッセージ。
   */
  private final StringBuilder fMessage = new StringBuilder();

  /**
   * インスタンスを構築します。
   */
  @Inject
  public ChatServerModel() {
  }

  @Override
  public String getMessage() {
    assert fMessage.toString() != null;

    return fMessage.toString();
  }

  @Override
  public void updateInformation(String info) throws NullPointerException {
    if (info == null) throw new NullArgumentException("message");

    appendInformation(info);
  }

  /**
   * ユーザに通知するメッセージに指定された文字列を追加し、変更をビューに通知します。messageにnullを指定した場合、
   * assertによりエラーが発生します。空文字列が指定された場合は何もしません。
   * 
   * @param info
   *          追加する文字列。nullを指定できません。
   */
  private void appendInformation(String info) {
    assert info != null;
    if (info.isEmpty()) return;

    fMessage.append(info).append(LINE_SEPARATOR);

    setChanged();
    notifyObservers("info");
  }
}
