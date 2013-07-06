package gmi.boardgame.chat.commands;

import gmi.utils.chain.Command;
import gmi.utils.exceptions.NullArgumentException;

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
    if (context == null) throw new NullArgumentException("context");
    if (!COMMAND.equals(context.getCommand())) return false;

    context.processByeCommand(context.getClient());

    return true;
  }
}
