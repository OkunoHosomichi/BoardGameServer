package gmi.boardgame.chat;

import java.util.Observable;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

@SuppressWarnings("serial")
final class ChatServerPanel extends JPanel implements ChatView {
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

  public ChatServerPanel() {
    final Injector injector = Guice.createInjector(new ChatServerModule());
    fModel = injector.getInstance(ChatModel.class);
    fModel.addChatView(this);

    initializeComponents();
    layoutComponents();
  }

  @Override
  public void update(Observable o, Object arg) {
  }

  /**
   * コンポーネントを初期設定します。
   */
  private void initializeComponents() {
    fServerMessageField.setColumns(10);
    fClientNameList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
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

  private class ChatServerModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(ChatModel.class).to(ChatServerModel.class).asEagerSingleton();
    }
  }
}
