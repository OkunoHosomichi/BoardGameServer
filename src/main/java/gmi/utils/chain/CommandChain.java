package gmi.utils.chain;

import gmi.utils.exceptions.NullArgumentException;

import java.util.LinkedList;
import java.util.List;

/**
 * Chain of Responsibilityパターンでコマンドの連鎖を処理します。
 * 
 * @author おくのほそみち
 * @param <T>
 *          コンテキストの型
 */
public final class CommandChain<T> implements Command<T> {
  private final List<Command<T>> fCommands;
  private final Object fCommandsLock;

  /**
   * インスタンスを構築します。
   */
  public CommandChain() {
    fCommands = new LinkedList<>();
    fCommandsLock = new Object();
  }

  /**
   * 連鎖に指定されたコマンドを追加します。
   * 
   * @param command
   *          追加するコマンド。nullを指定できません。
   * @throws IllegalArgumentException
   *           commandがnullの場合。
   */
  public CommandChain<T> addCommand(Command<T> command) throws IllegalArgumentException {
    if (command == null) throw new NullArgumentException("command");

    synchronized (fCommandsLock) {
      fCommands.add(command);
      return this;
    }
  }

  /**
   * コマンドの連鎖を実行します。
   * 
   * @throws NoSuchCommandException
   *           コンテキストに指定されたコマンドが実行できない場合。
   */
  @Override
  public boolean execute(T context) throws IllegalArgumentException, NoSuchCommandException {
    if (context == null) throw new NullArgumentException("context");

    synchronized (fCommandsLock) {
      for (final Command<T> command : fCommands) {
        if (command.execute(context)) return true;
      }
      throw new NoSuchCommandException();
    }
  }
}
