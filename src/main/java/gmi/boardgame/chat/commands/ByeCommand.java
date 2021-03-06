package gmi.boardgame.chat.commands;

import gmi.utils.chain.Command;
import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * クライアントの切断処理を行うコマンドです。
 * 
 * @author おくのほそみち
 */
final class ByeCommand implements Command<ChatCommandContext> {
  /**
   * コンテキストのコマンド文字列がこの文字列と等しければMessageコマンドだと判断します。
   */
  private static final String COMMAND = "BYE";

  @Override
  public boolean execute(ChatCommandContext context) throws IllegalArgumentException {
    checkNotNullArgument(context, "context");
    if (!context.getCommand().equals(COMMAND)) return false;

    context.processByeCommand(context.getClient());

    return true;
  }
}
