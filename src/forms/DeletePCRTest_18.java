package forms;

import Main_system.PCRSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeletePCRTest_18 {
    private PCRSystem pcrSystem;
    private menu m;
    private JFrame frame;

    private JTextField PCRIdTextField;
    private JButton deleteTestButton;
    private JButton goBackToMenuButton;
    private JPanel deletePCRTestPanel;

    public DeletePCRTest_18(menu pMenu, JFrame pFrame, PCRSystem pPcrSystem) {
        pcrSystem = pPcrSystem;
        m = pMenu;
        frame = pFrame;

        deleteTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        goBackToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PCRIdTextField.setText("");
                frame.setContentPane(m.getMenuPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }

    public JPanel getDeletePCRTestPanel() {
        return deletePCRTestPanel;
    }
}
