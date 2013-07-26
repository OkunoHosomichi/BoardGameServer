package gmi.boardgame.chat.message.outbound;

import gmi.boardgame.chat.message.OutboundMessage;
import gmi.boardgame.chat.message.parameters.Parameters;
import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * 送信メッセージです。
 * 
 * @author おくのほそみち
 */
final class Message implements OutboundMessage {
  /**
   * メッセージの文字列表現。
   */
  private final String fMessage;

  /**
   * 指定されたコマンドとパラメータからインスタンスを構築します。
   * 
   * @param command
   *          コマンド。nullを指定できません。
   * @param parameters
   *          パラメータ。nullを指定できません。
   * @throws IllegalArgumentException
   *           command又はparametersがnullの場合。
   */
  Message(Commands command, Parameters parameters) {
    checkNotNullArgument(command, "command");
    checkNotNullArgument(parameters, "parameters");

    fMessage = command.toString() + parameters.toString() + MESSAGE_DELIMITER;
  }

  @Override
  public String toString() {
    return fMessage;
  }
}