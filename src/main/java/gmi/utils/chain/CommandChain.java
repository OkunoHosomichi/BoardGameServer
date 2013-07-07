package gmi.utils.chain;

import gmi.utils.exceptions.NullArgumentException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Chain of Responsibilityパターンでコマンドの連鎖を処理します。
 * 
 * @author おくのほそみち
 * @param <T>
 *          コンテキストの型
 */
public final class CommandChain<T> implements Command<T> {
  private final List<Command<T>> fCommands;
  private final ReadWriteLock fCommandsLock;
  private final Lock fCommandsReadLock;
  private final Lock fCommandsWriteLock;

  /**
   * インスタンスを構築します。
   */
  public CommandChain() {
    fCommands = new LinkedList<>();
    fCommandsLock = new ReentrantReadWriteLock();
    fCommandsReadLock = fCommandsLock.readLock();
    fCommandsWriteLock = fCommandsLock.writeLock();
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

    fCommandsWriteLock.lock();
    try {
      fCommands.add(command);
    } finally {
      fCommandsWriteLock.unlock();
    }

    return this;
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

    fCommandsReadLock.lock();
    try {
      for (final Command<T> command : fCommands) {
        if (command.execute(context)) return true;
      }
      throw new NoSuchCommandException();
    } finally {
      fCommandsReadLock.unlock();
    }
  }
}
