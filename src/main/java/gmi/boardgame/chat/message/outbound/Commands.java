package gmi.boardgame.chat.message.outbound;

import javax.annotation.Nonnull;

/**
 * 送信メッセージのコマンド部分です。
 * 
 * @author おくのほそみち
 */
enum Commands {
  ENTER("ENTER"), MESSAGE("MSG"), RENAME("RENAME"), SERVERMESSAGE("MSG"), WELCOME("WELCOME");

  /**
   * コマンドを表す文字列。
   */
  private final String fCommand;

  /**
   * 指定されたコマンド文字列からインスタンスを構築します。
   * 
   * @param command
   *          コマンド文字列。null又は空文字列を指定できません。
   */
  Commands(@Nonnull String command) {
    assert command != null;
    assert !command.isEmpty();

    fCommand = command;
  }

  @Override
  public String toString() {
    return fCommand;
  }
}