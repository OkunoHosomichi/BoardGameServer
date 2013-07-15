package gmi.boardgame.chat.commands;

import gmi.utils.chain.Command;
import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * Nameコマンドを処理します。
 * 
 * @author おくのほそみち
 */
public class NameCommand implements Command<ChatCommandContext> {
  /**
   * コンテキストのコマンド文字列がこの文字列と等しければNameコマンドだと判断します。
   */
  private static final String COMMAND = "NAME";

  @Override
  public boolean execute(ChatCommandContext context) throws IllegalArgumentException {
    checkNotNullArgument(context, "context");
    if (!context.getCommand().equals(COMMAND)) return false;

    context.processNameCommand(context.getClient(), context.getArguments());

    return true;
  }
}
