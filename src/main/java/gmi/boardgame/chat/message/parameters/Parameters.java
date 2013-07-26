package gmi.boardgame.chat.message.parameters;

import java.util.Collection;

import javax.annotation.Nullable;

/**
 * メッセージのパラメータ部分です。
 * 
 * @author おくのほそみち
 */
public abstract class Parameters {
  /**
   * パラメータの区切り文字。
   */
  public static final String DELIMITER = ",";

  @Override
  public final String toString() {
    return convertIntoString();
  }

  /**
   * パラメータを文字列形式に変換します。
   * 
   * @return 文字列形式のパラメータ。nullではありません。
   */
  abstract String convertIntoString();

  /**
   * 指定されたパラメータのコレクションからインスタンスを取得します。nullを指定した場合やコレクションが空だった場合は「パラメータ無し」
   * のインスタンスを返します。
   * 
   * @param parameters
   *          パラメータのコレクション。要素にnullや空文字列を含んでいてはいけません。
   * @return パラメータのインスタンス。nullではありません。
   * @throws IllegalArgumentException
   *           parametersの要素にnull又は空文字を含んでいる場合。
   */
  public static final Parameters getParameters(@Nullable Collection<String> parameters) {
    if (parameters == null || parameters.isEmpty()) return NullParameter.INSTANCE;
    if (parameters.contains(null) || parameters.contains(""))
      throw new IllegalArgumentException("引数parametersの要素にnullや空文字列を指定できません。");

    return new MessageParameters(parameters);
  }

  /**
   * 指定されたパラメータからインスタンスを取得します。nullや空文字列を指定した場合は「パラメータ無し」のインスタンスを返します。
   * 
   * @param parameter
   *          パラメータのコレクション。
   * @return パラメータのインスタンス。nullではありません。
   */
  public static final Parameters getParameters(@Nullable String parameter) {
    if (parameter == null || parameter.isEmpty()) return NullParameter.INSTANCE;

    return new MessageParameters(parameter);
  }
}