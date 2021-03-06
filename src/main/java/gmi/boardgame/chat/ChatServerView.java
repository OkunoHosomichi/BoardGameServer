package gmi.boardgame.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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

import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * チャットサーバに必要なデータを表示し、ユーザからの入力をモデルに処理させます。MVCパターンでいうところのビューとコントローラ部分のつもりです。
 * あくまでもチャットに必要な部分だけなので、ボードゲーム管理データを表示する部分は別のビューを作って使います。
 * 
 * @author おくのほそみち
 */
@SuppressWarnings("serial")
final class ChatServerView extends JPanel {
  /**
   * 接続したクライアント名を一覧表示するリスト。
   */
  private final JList<String> fClientNames = new JList<>();
  /**
   * モデルクラス。
   */
  private final ChatModel fModel;
  /**
   * サーバの情報を表示するためのテキストエリア。
   */
  private final JTextArea fServerInformation = new JTextArea();
  /**
   * 各クライアントに送るメッセージを入力するフィールド。
   */
  private final JTextField fServerMessageField = new JTextField();

  /**
   * 指定されたモデルからインスタンスを構築します。
   * 
   * @param model
   *          モデル。nullを指定できません。
   * @throws IllegalArgumentException
   *           modelがnullの場合。
   */
  @Inject
  public ChatServerView(ChatModel model) throws IllegalArgumentException {
    checkNotNullArgument(model, "model");

    fModel = model;
    fModel.addObserver(new Observer() {

      /**
       * モデルからの更新通知を元にビューを更新します。
       * 何が更新されたのかをargに指定された文字列で判断し、対象をイベントディスパッチスレッドから更新します。
       * これは良いやり方ではない気もしますがどうすれば良いのかわかりません。 現時点で更新通知で届く文字列は以下の通りです。<br>
       * clients - クライアント一覧が更新されたことを示します。<br>
       * info - サーバ情報が更新されたことを示します。<br>
       * このメソッドはイベントディスパッチスレッド以外のスレッドから呼び出されることを想定してassert文によるチェックを行っています。
       * 
       * @param o
       *          更新通知を出したオブジェクト。nullを指定できません。
       * @param arg
       *          更新情報を知らせる文字列。nullを指定できません。
       * @throws IllegalArgumentException
       *           o又はargがnullの場合。又はargがString型でなかった場合。
       *           又はargで渡された文字列が正しい更新情報でなかった場合。
       */
      @Override
      public void update(Observable o, Object arg) throws IllegalArgumentException {
        // INFO: コメント参照。他に良いやり方を思いついたら直す。
        assert !SwingUtilities.isEventDispatchThread();
        checkNotNullArgument(o, "o");
        checkNotNullArgument(arg, "arg");
        if (!(arg instanceof String)) throw new IllegalArgumentException("argに文字列を渡すようにしてください。");

        // INFO: モデルの通知情報が変更された場合にコメント等きちんと修正する。
        switch ((String) arg) {
        case "clients":
          SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
              setClientNames(fModel.getClientNames());
            }
          });
          break;
        case "info":
          SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
              setServerInformation(fModel.getInformation());
            }
          });
          break;

        default:
          throw new IllegalArgumentException("argに正しい更新情報を渡すようにしてください。");
        }
      }
    });

    initializeComponents();
    initializeEventListener();
    layoutComponents();
  }

  /**
   * コンポーネントを初期設定します。
   */
  private void initializeComponents() {
    fClientNames.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
  }

  /**
   * イベントリスナを設定します。
   */
  private void initializeEventListener() {
    fServerMessageField.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (fServerMessageField.getText().isEmpty()) return;

        fModel.sendServerMessage(fServerMessageField.getText());
        fServerMessageField.setText("");
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
                .addComponent(fClientNames, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
        .addComponent(fServerMessageField, GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE));

    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(fClientNames, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(scrollpane, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
            .addGap(1)
            .addComponent(fServerMessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE)));

    setLayout(groupLayout);
  }

  /**
   * チャットに参加しているクライアント名一覧をfClientNamesに設定して表示させます。
   * このメソッドはイベントディスパッチスレッド内で呼び出されることを想定してassert文によるチェックを行っています。
   * 
   * @param names
   *          クライアント名一覧。nullを指定できません。
   */
  private void setClientNames(List<String> names) {
    assert SwingUtilities.isEventDispatchThread();
    assert names != null;

    final DefaultListModel<String> model = new DefaultListModel<>();

    for (final String client : names) {
      model.addElement(client);
    }

    fClientNames.setModel(model);
  }

  /**
   * サーバ情報をfServerInformationに設定して表示させます。
   * このメソッドはイベントディスパッチスレッド内で呼び出されることを想定してassert文によるチェックを行っています。
   * 
   * @param information
   *          サーバの情報。nullを指定できません。
   */
  private void setServerInformation(String information) {
    assert SwingUtilities.isEventDispatchThread();
    assert information != null;

    fServerInformation.setText(information);
  }
}
