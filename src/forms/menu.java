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

        personInsertForm =  new PersonInsert(this, frame, pcrSystem);
        PCRInsertForm = new PCRInsert(this, frame, pcrSystem);

        //pcrSystem = new PCRSystem();
        Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(PCRInsertForm.getPCRInsertPanel());
                frame.pack();
                frame.setVisible(true);
            }
        });
        Button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Button17.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(personInsertForm.getPersonInsertPanel());
                frame.pack();
                frame.setVisible(true);
                //PersonInsert personInsert = new PersonInsert();
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

