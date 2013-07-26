package gmi.boardgame.chat.message.parameters;

/**
 * パラメータが無いことを表現するクラスです。不変クラスで、Singletonパターンにしています。
 * 
 * @author おくのほそみち
 */
final class NullParameter extends Parameters {
  /**
   * NullParameterの唯一のインスタンス。
   */
  static final Parameters INSTANCE = new NullParameter();

  /**
   * 他からインスタンスを生成されないようアクセス指定をprivateにしています。アクセス指定を緩めないでください。
   */
  private NullParameter() {
  }

  /**
   * パラメータを文字列形式に変換します。パラメータが存在しないので空文字列を返します。
   */
  @Override
  String convertIntoString() {
    return "";
  }
}