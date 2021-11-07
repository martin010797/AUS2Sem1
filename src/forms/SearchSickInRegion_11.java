package forms;

import Main_system.PCRSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchSickInRegion_11 {
    private PCRSystem pcrSystem;
    private menu m;
    private JFrame frame;
    private OutputForTests outputForTestsForm;

    private JPanel searchSickInRegionPanel;
    private JTextField regionIdTextField;
    private JTextField numberOfDaysTextField;
    private JButton searchPeopleButton;
    private JButton goBackToMenuButton;

    public SearchSickInRegion_11(menu pMenu, JFrame pFrame, PCRSystem pPcrSystem, OutputForTests pOutput) {
        pcrSystem = pPcrSystem;
        m = pMenu;
        frame = pFrame;
        outputForTestsForm = pOutput;

        searchPeopleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        goBackToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFieldsEmpty();
                frame.setContentPane(m.getMenuPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }

    public JPanel getSearchSickInRegionPanel() {
        return searchSickInRegionPanel;
    }

    private void setFieldsEmpty(){
        regionIdTextField.setText("");
        numberOfDaysTextField.setText("");
    }

    private boolean emptyFields(){
        if (regionIdTextField.getText().equals("")){
            return true;
        }
        if (numberOfDaysTextField.getText().equals("")){
            return true;
        }
        return false;
    }
}
