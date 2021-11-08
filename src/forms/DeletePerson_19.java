package forms;

import Main_system.PCRSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeletePerson_19 {
    private PCRSystem pcrSystem;
    private menu m;
    private JFrame frame;

    private JTextField personIdTextField;
    private JButton deletePersonButton;
    private JButton goBackToMenuButton;
    private JPanel deletePersonPanel;

    public DeletePerson_19(menu pMenu, JFrame pFrame, PCRSystem pPcrSystem) {
        pcrSystem = pPcrSystem;
        m = pMenu;
        frame = pFrame;

        deletePersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        goBackToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                personIdTextField.setText("");
                frame.setContentPane(m.getMenuPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }

    public JPanel getDeletePersonPanel() {
        return deletePersonPanel;
    }
}
