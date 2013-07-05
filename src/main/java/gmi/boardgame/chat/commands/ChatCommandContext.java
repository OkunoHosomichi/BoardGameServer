package gmi.boardgame.chat.commands;

import gmi.boardgame.chat.ChatModel;
import gmi.utils.exceptions.NullArgumentException;

/**
 * コマンド実行に必要なコンテキストです。各コマンドはこのクラスを通じてモデルに処理させることになります。
 * 
 * @author おくのほそみち
 */
public final class ChatCommandContext {
  /**
   * 引数。引数が存在しない場合は空文字列です。複数の引数が存在する場合はカンマ区切りで繋がっています。
   */
  private final String fArguments;
  /**
   * コマンド文字列。
   */
  private final String fCommand;

  /**
   * モデル。各コマンドはモデルに処理を任せてしまうことが殆どになる予定です。
   */
  private final ChatModel fModel;

  /**
   * 指定されたコマンド文字列を引数からインスタンスを構築します。
   * 
   * @param command
   *          コマンド文字列。nullを指定できません。空文字列を指定できません。
   * @param arguments
   *          引数。nullを指定できません。コマンドによっては引数が存在しませんが、その場合は空文字列を指定します。
   *          コマンドによっては複数の引数が存在しますが、 その場合はカンマ区切りで繋げてください。
   * @param model
   *          モデル。各コマンドはモデルに処理を任せてしまうことが殆どになる予定です。
   * @throws IllegalArgumentException
   *           command又はarguments又はmodelにnullが指定された場合。又はcommandに空文字列が指定された場合。
   */
  public ChatCommandContext(String command, String arguments, ChatModel model) throws IllegalArgumentException {
    if (command == null) throw new NullArgumentException("command");
    if (arguments == null) throw new NullArgumentException("arguments");
    if (model == null) throw new NullArgumentException("model");
    if (command.isEmpty()) throw new IllegalArgumentException("commandに空文字列を指定できません。");

    fCommand = command;
    fArguments = arguments;
    fModel = model;
  }

  /**
   * 引数を返します。コマンドによっては引数が存在しませんが、その場合は空文字列が返ります。コマンドによっては複数の引数が存在しますが、
   * その場合はカンマ区切りで繋げています。
   * 
   * @return arguments 引数。nullではありません。
   */
  public String getArguments() {
    assert fArguments != null;

    return fArguments;
  }

  /**
   * コマンド文字列を返します。
   * 
   * @return command コマンド文字列。nullでも空文字列でもありません。
   */
  public String getCommand() {
    assert fCommand != null;
    assert !fCommand.isEmpty();

    return fCommand;
  }
}
