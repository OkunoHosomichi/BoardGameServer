package gmi.boardgame.server;

import gmi.boardgame.chat.ChatServer;
import gmi.utils.IntRange;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public final class ServerFrame extends JFrame {
  /**
   * convertNumberStringIntoPortNumber()で引数チェックに利用する正規表現。
   */
  private static final String CHECK_NUMBER_STRING_PATTERN = "^\\+?\\d+";
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

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

  public static void main(String[] args) {
    final int port = (args.length > 0) ? convertNumberStringIntoPortNumber(args[0]) : DEFAULT_PORT_NUMBER;

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          new ServerFrame(port).setVisible(true);
        } catch (final Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    });
  }

  /**
   * 指定された文字列をポート番号に変更します。文字列がnullだった場合や10進整数でなかった場合や負の整数だった場合はデフォルトのポート番号を返します。
   * 
   * @param str
   *          ポート番号の文字列表現。
   * @return ポート番号。
   */
  private static int convertNumberStringIntoPortNumber(String str) {
    if (str == null) return DEFAULT_PORT_NUMBER;
    if (str.isEmpty()) return DEFAULT_PORT_NUMBER;
    if (!Pattern.matches(CHECK_NUMBER_STRING_PATTERN, str)) return DEFAULT_PORT_NUMBER;

    return Integer.parseInt(str);
  }
}