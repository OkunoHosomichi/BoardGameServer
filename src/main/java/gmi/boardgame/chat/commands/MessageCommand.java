package gmi.boardgame.chat.commands;

import gmi.utils.chain.Command;

/**
 * チャットに参加している全クライアントにメッセージを送信するコマンドです。
 * 
 * @author おくのほそみち
 */
public final class MessageCommand implements Command<ChatCommandContext> {
  /**
   * コンテキストのコマンド文字列がこの文字列と等しければMessageコマンドだと判断します。
   */
  private static final String COMMAND = "MSG";

  @Override
  public boolean execute(ChatCommandContext context) throws IllegalArgumentException {
    return false;
  }
}
