package gmi.boardgame.chat.commands;

import gmi.utils.chain.ChainFactory;
import gmi.utils.chain.Command;
import gmi.utils.chain.CommandChain;

/**
 * チャット用のコマンド連鎖を作成するクラス。Singletonパターンです。
 * 
 * @author おくのほそみち
 */
public enum ChatCommandChainFactory implements ChainFactory<ChatCommandContext> {
  /**
   * 唯一のインスタンスを保持します。
   */
  INSTANCE;
  /**
   * コマンドの連鎖を保持するフィールド。Command型にしているのはaddメソッドを呼び出せないようにするため。
   */
  private final Command<ChatCommandContext> fChain;

  /**
   * コマンドの連鎖を作成してインスタンスを構築します。
   */
  ChatCommandChainFactory() {
    fChain = new CommandChain<ChatCommandContext>().addCommand(new MessageCommand()).addCommand(new ByeCommand());
  }

  @Override
  public Command<ChatCommandContext> getChain() {

    return fChain;
  }
}
