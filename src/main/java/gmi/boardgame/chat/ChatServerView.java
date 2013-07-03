package gmi.boardgame.chat;

import gmi.utils.exceptions.NullArgumentException;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 * チャットサーバに必要なデータを表示し、ユーザからの入力をモデルに処理させます。MVCパターンでいうところのビューとコントローラ部分のつもりです。
 * あくまでもチャットに必要な部分だけなので、ボードゲーム管理データを表示する部分は別のビューを作って使います。
 * 
 * @author おくのほそみち
 */
@SuppressWarnings("serial")
final class ChatServerView extends JPanel implements Observer {
  /**
   * 接続したクライアント名を一覧表示するリスト。
   */
  private final JList<String> fClientNameList = new JList<>();
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
   * @throws NullPointerException
   *           modelがnullの場合。
   */
  @Inject
  public ChatServerView(ChatModel model) throws NullPointerException {
    if (model == null) throw new NullArgumentException("model");

    fModel = model;
    fModel.addObserver(this);

    initializeComponents();
    initializeEventListener();
    layoutComponents();
  }

  /**
   * モデルからの更新通知を元にビューを更新します。
   * 何が更新されたのかをargに指定された文字列で判断し、対象をイベントディスパッチスレッドから更新します。
   * これは良いやり方ではない気もしますがどうすれば良いのかわかりません。 現時点で更新通知で届く文字列は以下の通りです。<br>
   * info - サーバ情報が更新されたことを示します。<br>
   * このメソッドはイベントディスパッチスレッド以外のスレッドから呼び出されることを想定してassert文によるチェックを行っています。
   * 
   * @param o
   *          更新通知を出したオブジェクト。nullを指定できません。
   * @param arg
   *          更新情報を知らせる文字列。nullを指定できません。
   * @throws NullPointerException
   *           o又はargがnullの場合。
   * @throws IllegalArgumentException
   *           argがString型でなかった場合。又はargで渡された文字列が正しい更新情報でなかった場合。
   */
  @Override
  public void update(Observable o, Object arg) throws NullPointerException, IllegalArgumentException {
    // INFO: コメント参照。他に良いやり方を思いついたら直す。
    assert !SwingUtilities.isEventDispatchThread();
    if (o == null) throw new NullArgumentException("o");
    if (arg == null) throw new NullArgumentException("arg");
    if (!(arg instanceof String)) throw new IllegalArgumentException("argに文字列を渡すようにしてください。");

    // INFO: モデルの通知情報が変更された場合にきちんと修正する。
    switch ((String) arg) {
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
