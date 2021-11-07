package forms;

import Main_system.PCRSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchSickInDistrict_10 {
    private PCRSystem pcrSystem;
    private menu m;
    private JFrame frame;
    private OutputForTests outputForTestsForm;

    private JPanel searchSickInDistrictPanel;
    private JTextField districtIdTextField;
    private JTextField numberOfDaysTextField;
    private JButton searchPeopleButton;
    private JButton goBackToMenuButton;

    public SearchSickInDistrict_10(menu pMenu, JFrame pFrame, PCRSystem pPcrSystem, OutputForTests pOutput) {
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

    public JPanel getSearchSickInDistrictPanel() {
        return searchSickInDistrictPanel;
    }

    private void setFieldsEmpty(){
        districtIdTextField.setText("");
        numberOfDaysTextField.setText("");
    }

    private boolean emptyFields(){
        if (districtIdTextField.getText().equals("")){
            return true;
        }
        if (numberOfDaysTextField.getText().equals("")){
            return true;
        }
        return false;
    }
}
