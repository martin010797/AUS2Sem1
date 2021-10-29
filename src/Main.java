import javax.swing.*;

import Tests.GeneratorOfData;
import Tests.TestKey;
import Tests.TestingData;
import forms.menu;

public class Main {

    public static void main(String[] args) {
        GeneratorOfData generator = new GeneratorOfData();
        //generator.testInsert(100000);
        //generator.testDelete(100000);
        generator.testRandomOperation(300000, 0.9);
        /*TestKey minKey = new TestKey(9950);
        TestKey maxKey = new TestKey(15000);
        TestingData minTest = new TestingData(minKey);
        TestingData maxTest = new TestingData(maxKey);
        generator.testIntervalSearch(minTest, maxTest, 100000);*/

        JFrame frame = new JFrame("Menu");
        frame.setContentPane(new menu().getMenuPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
