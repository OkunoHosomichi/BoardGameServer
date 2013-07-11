package gmi.boardgame.chat.commands;

import gmi.utils.chain.Command;
import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * チャットに参加している全クライアントにメッセージを送信するコマンドです。
 * 
 * @author おくのほそみち
 */
final class MessageCommand implements Command<ChatCommandContext> {
  /**
   * コンテキストのコマンド文字列がこの文字列と等しければMessageコマンドだと判断します。
   */
  private static final String COMMAND = "MSG";

  @Override
  public boolean execute(ChatCommandContext context) throws IllegalArgumentException {
    checkNotNullArgument(context, "context");
    if (!COMMAND.equals(context.getCommand())) return false;

    context.processMessageCommand(context.getClient(), context.getArguments());

    return true;
  }
}
