package gmi.boardgame.server;

import gmi.boardgame.chat.ChatServer;
import gmi.utils.IntRange;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public final class ServerFrame extends JFrame {
  /**
   * デフォルトのポート番号。ポート番号は49513～65535までの値を指定します。
   */
  private static final int DEFAULT_PORT_NUMBER = 60935;
  /**
   * ポート番号に使える値の範囲。49513～65535までの値です。
   */
  private static final IntRange PORT_RANGE = new IntRange(49513, 65535);
  private final JPanel fChatPanel;
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
    assert PORT_RANGE.Contains(DEFAULT_PORT_NUMBER);

    fPortNumber = PORT_RANGE.Contains(portNumber) ? portNumber : DEFAULT_PORT_NUMBER;

    fChatPanel = ChatServer.getPanel();
    layoutComponents();
  }

  /**
   * コンポーネントを配置します。
   */
  private void layoutComponents() {
    final Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(fChatPanel, BorderLayout.CENTER);
  }
}