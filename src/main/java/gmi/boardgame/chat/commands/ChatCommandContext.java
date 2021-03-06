package gmi.boardgame.chat.commands;

import gmi.boardgame.chat.ChatModel;
import io.netty.channel.Channel;
import static gmi.utils.Preconditions.checkNotNullArgument;

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
   * コマンドを送信してきたクライアント。
   */
  private final Channel fClient;
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
   * @param client
   *          コマンドを送信してきたクライアント。nullを指定できません。
   * @param command
   *          コマンド文字列。nullを指定できません。空文字列を指定できません。
   * @param arguments
   *          引数。nullを指定できません。コマンドによっては引数が存在しませんが、その場合は空文字列を指定します。
   *          コマンドによっては複数の引数が存在しますが、 その場合はカンマ区切りで繋げてください。
   * @param model
   *          モデル。各コマンドはモデルに処理を任せてしまうことが殆どになる予定です。
   * @throws IllegalArgumentException
   *           client又はcommand又はarguments又はmodelにnullが指定された場合。
   *           又はcommandに空文字列が指定された場合。
   */
  public ChatCommandContext(Channel client, String command, String arguments, ChatModel model)
      throws IllegalArgumentException {
    checkNotNullArgument(client, "client");
    checkNotNullArgument(command, "command");
    checkNotNullArgument(arguments, "arguments");
    checkNotNullArgument(model, "model");
    if (command.isEmpty()) throw new IllegalArgumentException("commandに空文字列を指定できません。");

    fClient = client;
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
   * コマンドを送信してきたクライアントを返します。
   * 
   * @return client クライアント。nullではありません。
   */
  public Channel getClient() {
    assert fClient != null;

    return fClient;
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

  /**
   * Byeコマンドを処理します。実際にはモデルに委譲するだけです。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。nullを指定できません。
   * @throws IllegalArgumentException
   *           clientがnullの場合。
   */
  public void processByeCommand(Channel client) throws IllegalArgumentException {
    fModel.processByeCommand(client);
  }

  /**
   * Messageコマンドを処理します。実際にはモデルに委譲するだけです。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。nullを指定できません。
   * @param message
   *          送信するメッセージ。nullを指定できません。
   * @throws IllegalArgumentException
   *           client又はmessageがnullの場合。
   */
  void processMessageCommand(Channel client, String message) throws IllegalArgumentException {
    fModel.processMessageCommand(client, message);
  }

  /**
   * Nameコマンドを処理します。実際にはモデルに委譲するだけです。
   * 
   * @param client
   *          コマンドを送信してきたクライアント。nullを指定できません。
   * @param nickName
   *          クライアントのニックネーム。nullや空文字列を指定できません。
   * @throws IllegalArgumentException
   *           client又はnickNameにnullが指定された場合。又はnickNameに空文字列が指定された場合。
   */
  void processNameCommand(Channel client, String nickName) throws IllegalArgumentException {
    fModel.processNameCommand(client, nickName);
  }
}
