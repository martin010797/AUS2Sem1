import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GeneratorOfData {

    public void testInsert(int numberOfValues){
        BST23<TestData> tree = new BST23<>();
        ArrayList<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < numberOfValues; i++){
            values.add(i+1);
        }
        for (int i = 0; i < numberOfValues; i++){
            int randomIndex;
            if (values.size() == 1){
                randomIndex = 0;
            }else {
                randomIndex = ThreadLocalRandom.current().nextInt(0, values.size() - 1);
            }
            TestData d = new TestData(values.get(randomIndex));
            //TestData d = new TestData(numberOfValues-i);
            values.remove(randomIndex);
            TestingData data = new TestingData(d);
            if(!tree.insert(data)){
                System.out.println("nevlozene");
            }else {
                System.out.println("Vlozene cislo " + data.get_data1().getKey());
            }
            if (tree.find(data) == null){
                System.out.println(data.get_data1().getKey() + " neulozene spravne");
            }
        }
        for (int i = 0; i < numberOfValues; i++){
            TestData d = new TestData(i+1);
            TestingData data = new TestingData(d);
            if (tree.find(data) == null){
                System.out.println(data.get_data1().getKey() + " not found");
            }
        }

    }

    public void testDelete(int numberOfValues){
        BST23<TestData> tree = new BST23<>();
        ArrayList<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < numberOfValues; i++){
            values.add(i+1);
        }
        TestData d = new TestData(2);
        TestData d2 = new TestData(6);
        TestData d3 = new TestData(1);
        TestData d4 = new TestData(9);
        TestData d5 = new TestData(7);
        TestData d6 = new TestData(8);
        TestData d7 = new TestData(3);
        TestData d8 = new TestData(4);
        TestData d9 = new TestData(5);
        TestData d10 = new TestData(10);
        TestingData data1 = new TestingData(d);
        TestingData data2 = new TestingData(d2);
        TestingData data3 = new TestingData(d3);
        TestingData data4 = new TestingData(d4);
        TestingData data5 = new TestingData(d5);
        TestingData data6 = new TestingData(d6);
        TestingData data7 = new TestingData(d7);
        TestingData data8 = new TestingData(d8);
        TestingData data9 = new TestingData(d9);
        TestingData data10 = new TestingData(d10);
        tree.insert(data1);
        tree.insert(data2);
        tree.insert(data3);
        tree.insert(data4);
        tree.insert(data5);
        tree.insert(data6);
        tree.insert(data7);
        tree.insert(data8);
        tree.insert(data9);
        tree.insert(data10);

        TestData delD = new TestData(2);
        TestData delD2 = new TestData(6);
        TestData delD3 = new TestData(1);
        TestData delD4 = new TestData(9);
        TestData delD5 = new TestData(7);
        TestData delD6 = new TestData(8);
        TestData delD7 = new TestData(3);
        TestData delD8 = new TestData(4);
        TestData delD9 = new TestData(5);
        TestData delD10 = new TestData(10);
        TestingData delData1 = new TestingData(delD);
        TestingData delData2 = new TestingData(delD2);
        TestingData delData3 = new TestingData(delD3);
        TestingData delData4 = new TestingData(delD4);
        TestingData delData5 = new TestingData(delD5);
        TestingData delData6 = new TestingData(delD6);
        TestingData delData7 = new TestingData(delD7);
        TestingData delData8 = new TestingData(delD8);
        TestingData delData9 = new TestingData(delD9);
        TestingData delData10 = new TestingData(delD10);

        tree.delete(delData1);
        tree.delete(delData2);
        tree.delete(delData3);
        tree.delete(delData4);
        tree.delete(delData5);
        tree.delete(delData6);
        tree.delete(delData7);
        tree.delete(delData8);
        tree.delete(delData9);
        tree.delete(delData10);
    }
}
