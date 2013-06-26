package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
final class ChatServerPanel extends JPanel implements ChatView {
  /**
   * 接続したクライアント名を一覧表示するリスト。
   */
  private final JList<String> fClientNameList = new JList<>();
  /**
   * プレゼンタクラス。
   */
  private final ChatPresenter fPresenter;
  /**
   * サーバの情報を表示するためのテキストエリア。
   */
  private final JTextArea fServerInformation = new JTextArea();
  /**
   * 各クライアントに送るメッセージを入力するフィールド。
   */
  private final JTextField fServerMessageField = new JTextField();

  /**
   * 指定されたプレゼンタからインスタンスを構築します。
   * 
   * @param presenter
   *          プレゼンタ。nullを指定できません。
   * @throws NullPointerException
   *           presenterがnullの場合。
   */
  @Inject
  public ChatServerPanel(ChatPresenter presenter) throws NullPointerException {
    if (presenter == null) throw new NullArgumentException("model");

    fPresenter = presenter;
    fPresenter.addChatView(this);

    initializeComponents();
    initializeEventListener();
    layoutComponents();
  }

  @Override
  public void update(Observable o, Object arg) {
  }

  /**
   * コンポーネントを初期設定します。
   */
  private void initializeComponents() {
    fClientNameList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
  }

  /**
   * イベントリスナを設定します。
   */
  private void initializeEventListener() {
    fServerMessageField.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        fPresenter.sendServerMessage(fServerMessageField.getText());
      }
    });
  }

  /**
   * GroupLayoutを使ってコンポーネントを配置します。 コードはWindowBuilderで自動生成ししました。
   */
  private void layoutComponents() {
    final JScrollPane scrollpane = new JScrollPane(fServerInformation);
    scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

    final GroupLayout groupLayout = new GroupLayout(this);

    groupLayout.setHorizontalGroup(groupLayout
        .createParallelGroup(Alignment.LEADING)
        .addGroup(
            groupLayout.createSequentialGroup()
                .addComponent(scrollpane, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE).addGap(1)
                .addComponent(fClientNameList, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
        .addComponent(fServerMessageField, GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE));

    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(fClientNameList, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(scrollpane, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
            .addGap(1)
            .addComponent(fServerMessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE)));

    setLayout(groupLayout);
  }

  /**
   * 接続したクライアントの一覧をfClientNameListに設定して表示させます。
   * このメソッドはイベントディスパッチスレッド内で呼び出されるることを想定してassert文によるチェックを行っています。
   * 
   * @param clientList
   *          接続しているクライアントの一覧。nullを指定できません。
   */
  private void setClientList(List<String> clientList) {
    assert SwingUtilities.isEventDispatchThread();
    assert clientList != null;

    final DefaultListModel<String> model = new DefaultListModel<>();

    for (final String nickName : clientList) {
      model.addElement(nickName);
    }

    fClientNameList.setModel(model);
  }

  /**
   * サーバ情報をfServerInformationに設定して表示させます。
   * このメソッドはイベントディスパッチスレッド内で呼び出されるることを想定してassert文によるチェックを行っています。
   * 
   * @param information
   *          サーバの情報。nullを指定できません。
   */
  private void setServerInformation(String information) {
  }
}
