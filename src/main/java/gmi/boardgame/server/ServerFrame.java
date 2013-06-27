package gmi.boardgame.server;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public final class ServerFrame extends JFrame {
  /**
   * デフォルトのポート番号。ポート番号は49513～65535までの値を指定します。
   */
  private static final int DEFAULT_PORT_NUMBER = 60935;
  private final int fPortNumber;

  /**
   * デフォルトのポート番号を指定してインスタンスを構築します。デフォルト値は60935番です。
   */
  public ServerFrame() {
    this(DEFAULT_PORT_NUMBER);
  }

  /**
   * ポート番号を指定してインスタンスを構築します。ポート番号は49513～65535までの値を指定します。
   * 指定された番号が利用可能な範囲外の場合はデフォルトの番号を指定します。
   * 
   * @param portNumber
   *          クライアントの接続を待つポート番号。
   */
  public ServerFrame(int portNumber) {
    assert (DEFAULT_PORT_NUMBER > 49512) && (DEFAULT_PORT_NUMBER < 65536);

    fPortNumber = ((portNumber > 49512) && (portNumber < 65536)) ? portNumber : DEFAULT_PORT_NUMBER;
  }
}