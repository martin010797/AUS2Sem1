package forms;

import Main_system.PCRSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menu {
    //private PCRSystem pcrSystem;
    private PCRSystem pcrSystem = new PCRSystem();
    private PersonInsert personInsertForm;
    private PCRInsert PCRInsertForm;
    private SearchResultForPerson searchResultForPersonForm;
    private SearchTestForWorkplace_15 searchTestForWorkplace_15form;
    private SearchPositiveTestsForDistrict_4 searchPositiveTestsForDistrict_4form;

    private OutputForTests outputForTestsForm;

    private JFrame frame;
    private JButton Button1;
    private JPanel MenuPanel;
    private JButton Button2;
    private JButton Button4;
    private JButton Button15;
    private JButton Button17;

    public menu() {
        frame = new JFrame("Menu");
        frame.setContentPane(MenuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        outputForTestsForm = new OutputForTests(this, frame, pcrSystem);

        personInsertForm =  new PersonInsert(this, frame, pcrSystem);
        PCRInsertForm = new PCRInsert(this, frame, pcrSystem);
        searchResultForPersonForm = new SearchResultForPerson(this, frame, pcrSystem);
        searchTestForWorkplace_15form = new SearchTestForWorkplace_15(this, frame, pcrSystem, outputForTestsForm);
        searchPositiveTestsForDistrict_4form = new SearchPositiveTestsForDistrict_4(
                this,
                frame,
                pcrSystem,
                outputForTestsForm);



        //pcrSystem = new PCRSystem();
        Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(PCRInsertForm.getPCRInsertPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(searchResultForPersonForm.getSearchResultForPersonPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(searchPositiveTestsForDistrict_4form.getSearchPositiveTestsForDistrictPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(searchTestForWorkplace_15form.getSearchTestForWorkplacePanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button17.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(personInsertForm.getPersonInsertPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }

    public JPanel getMenuPanel() {
        return MenuPanel;
    }

    public void setMenuPanel(JPanel menuPanel) {
        MenuPanel = menuPanel;
    }
}

