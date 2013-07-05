package gmi.boardgame.chat.commands;

import gmi.utils.chain.Command;
import gmi.utils.exceptions.NullArgumentException;

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
    if (context == null) throw new NullArgumentException("context");
    if (!COMMAND.equals(context.getCommand())) return false;

    context.processMessageCommand(context.getClient(), context.getArguments());

    return true;
  }
}
