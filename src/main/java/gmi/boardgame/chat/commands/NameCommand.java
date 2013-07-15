package gmi.boardgame.chat.commands;

import gmi.utils.chain.Command;

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
    // TODO 自動生成されたメソッド・スタブ
    return false;
  }
}
