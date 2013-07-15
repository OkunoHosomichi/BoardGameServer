package gmi.utils.chain;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * Chain of Responsibilityパターンでコマンドの連鎖を処理します。
 * 
 * @author おくのほそみち
 * @param <T>
 *          コンテキストの型
 */
public final class CommandChain<T> implements Command<T> {
  /**
   * コマンドの連鎖。
   */
  private final List<Command<T>> fCommands = new LinkedList<>();
  /**
   * fCommandsのロックオブジェクト。
   */
  private final Object fCommandsLock = new Object();

  /**
   * 連鎖に指定されたコマンドを追加します。
   * 
   * @param command
   *          追加するコマンド。nullを指定できません。
   * @throws IllegalArgumentException
   *           commandがnullの場合。
   */
  public CommandChain<T> addCommand(@Nonnull Command<T> command) {
    checkNotNullArgument(command, "command");

    synchronized (fCommandsLock) {
      fCommands.add(command);
      return this;
    }
  }

  /**
   * コマンドの連鎖を実行します。
   * 
   * @param context
   *          実行に必要なコンテキスト。
   * @return 実行したかどうか。実行したならtrue、実行していないならFalseを返します。
   * @throws IllegalArgumentException
   *           contextがnullの場合。
   */
  @Override
  public boolean execute(@Nonnull T context) {
    checkNotNullArgument(context, "context");

    synchronized (fCommandsLock) {
      for (final Command<T> command : fCommands) {
        if (command.execute(context)) return true;
      }
      return false;
    }
  }
}
