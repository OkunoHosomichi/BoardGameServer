package gmi.boardgame.chat;

import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings("serial")
public class ChatServerPanel extends JPanel implements Observer {
  private final JTextField fTextField = new JTextField();
  private final JTextArea fTextArea = new JTextArea();
  private final JList<String> fList = new JList<>();
  private final JSplitPane fSplitPane = new JSplitPane();

  public ChatServerPanel() {

    fTextField.setColumns(10);
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        .addComponent(fSplitPane, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        .addComponent(fTextField, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
        Alignment.TRAILING,
        groupLayout.createSequentialGroup().addComponent(fSplitPane, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(fTextField, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)));

    fSplitPane.setLeftComponent(fTextArea);
    fSplitPane.setRightComponent(fList);
    setLayout(groupLayout);
  }

  @Override
  public void update(Observable o, Object arg) {
  }
}
