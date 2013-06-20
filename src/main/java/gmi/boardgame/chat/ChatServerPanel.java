package gmi.boardgame.chat;

import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class ChatServerPanel extends JPanel implements Observer {
  private final JTextField fTextField = new JTextField();
  private final JTextArea fTextArea = new JTextArea();
  private final JList<String> fList = new JList<>();

  public ChatServerPanel() {

    initializeComponents();

    JScrollPane scrollpane = new JScrollPane(fTextArea);
    scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

    GroupLayout groupLayout = new GroupLayout(this);

    groupLayout.setHorizontalGroup(groupLayout
        .createParallelGroup(Alignment.LEADING)
        .addGroup(
            groupLayout.createSequentialGroup()
                .addComponent(scrollpane, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE).addGap(1)
                .addComponent(fList, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
        .addComponent(fTextField, GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE));

    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(
            groupLayout
                .createSequentialGroup()
                .addGroup(
                    groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(fList, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addComponent(scrollpane, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                .addGap(1)
                .addComponent(fTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                    GroupLayout.PREFERRED_SIZE)));

    setLayout(groupLayout);
  }

  /**
   * コンポーネントを初期設定する。
   */
  private void initializeComponents() {
    fTextField.setColumns(10);
    fList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
  }

  @Override
  public void update(Observable o, Object arg) {
  }
}
