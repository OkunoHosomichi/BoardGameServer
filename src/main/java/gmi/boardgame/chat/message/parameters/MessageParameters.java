package gmi.boardgame.chat.message.parameters;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static gmi.utils.Preconditions.checkNotNullArgument;
import static gmi.utils.Preconditions.checkNotNullOrEmptyArgument;

/**
 * メッセージのパラメータ部分です。不変クラスです。
 * 
 * @author おくのほそみち
 */
final class MessageParameters extends Parameters {
  public static final String COMMAND_DELIMITER = " ";
  /**
   * パラメータの文字列表現。
   */
  private final String fParameters;

  /**
   * 指定されたパラメータのコレクションからインスタンスを構築します。
   * 
   * @param parameters
   *          パラメータの一覧。nullを指定できません。空ではいけません。要素内にnullや空文字列を含んでいてはいけません。
   * @throws IllegalArgumentException
   *           parametersがnullの場合。又は要素が一つもなかった場合。又は要素内にnullや空文字列を含んでいる場合。
   */
  MessageParameters(@Nonnull Collection<String> parameters) {
    checkNotNullArgument(parameters, "parameters");
    checkArgument(!parameters.isEmpty(), "引数parametersに要素が一つもありません。");
    if (parameters.contains(null) || parameters.contains(""))
      throw new IllegalArgumentException("引数parametersの要素にnullや空文字列を指定できません。");

    fParameters = COMMAND_DELIMITER + StringUtils.join(parameters, DELIMITER);
  }

  /**
   * 指定されたパラメータからインスタンスを構築します。
   * 
   * @param parameter
   *          パラメータ。null又は空文字列を指定できません。
   * @throws IllegalArgumentException
   *           parameterがnull又は空文字列の場合。
   */
  MessageParameters(@Nonnull String parameter) {
    checkNotNullOrEmptyArgument(parameter, "parameter");

    fParameters = COMMAND_DELIMITER + parameter;
  }

  @Override
  String convertIntoString() {
    return fParameters;
  }
}
