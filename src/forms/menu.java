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
    private FindTestsForPatient_3 findTestsForPatient_3forml;
    private SearchForTestsInDistrict_5 searchForTestsInDistrict_5form;
    private SearchPositiveTestsForRegion_6 searchPositiveTestsForRegion_6form;
    private SearchForTestsInRegion_7 searchForTestsInRegion_7form;
    private SearchPositiveTests_8 searchPositiveTests_8form;
    private SearchTests_9 searchTests_9form;

    private OutputForTests outputForTestsForm;

    private JFrame frame;
    private JButton Button1;
    private JPanel MenuPanel;
    private JButton Button2;
    private JButton Button4;
    private JButton Button15;
    private JButton Button17;
    private JButton Button3;
    private JButton Button5;
    private JButton Button6;
    private JButton Button7;
    private JButton Button8;
    private JButton Button9;
    private JButton Button10;
    private JButton Button11;
    private JButton Button12;
    private JButton Button13;
    private JButton Button14;
    private JButton Button16;
    private JButton Button18;
    private JButton Button19;

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
        findTestsForPatient_3forml = new FindTestsForPatient_3(this,frame,pcrSystem,outputForTestsForm);
        searchForTestsInDistrict_5form = new SearchForTestsInDistrict_5(this,frame,pcrSystem,outputForTestsForm);
        searchPositiveTestsForRegion_6form = new SearchPositiveTestsForRegion_6(
                this,
                frame,
                pcrSystem,
                outputForTestsForm);
        searchForTestsInRegion_7form = new SearchForTestsInRegion_7(this,frame,pcrSystem,outputForTestsForm);
        searchPositiveTests_8form = new SearchPositiveTests_8(this,frame,pcrSystem,outputForTestsForm);
        searchTests_9form = new SearchTests_9(this,frame,pcrSystem,outputForTestsForm);

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
        Button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(findTestsForPatient_3forml.getFindTestForPatientPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(searchForTestsInDistrict_5form.getSearchForTestsInDistrictPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(searchPositiveTestsForRegion_6form.getSearchPositiveTestsForRegionPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(searchForTestsInRegion_7form.getSearchForTestsInRegionPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(searchPositiveTests_8form.getSearchPositiveTestsPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(searchTests_9form.getSearchTestsPanel());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
        Button10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button16.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button18.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button19.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

