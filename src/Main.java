public class Main {

    public static void main(String[] args) {
        GeneratorOfData generator = new GeneratorOfData();
        generator.testInsert(100000);
        /*BST23<TestData> tree = new BST23<>();
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
        //TestData d11 = new TestData(25);
        //TestData d12 = new TestData(12);
        //TestData d13 = new TestData(14);
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
        //TestingData data11 = new TestingData(d11);
        //TestingData data12 = new TestingData(d12);
        //TestingData data13 = new TestingData(d13);
        //TestingData data1 = new TestingData(10);
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
        for (int i = 0; i < 10; i++){
            TestData dd = new TestData(i+1);
            TestingData data = new TestingData(dd);
            if (tree.find(data) == null){
                System.out.println(data.get_data1().getKey() + " not found");
            }
        }
        //tree.insert(data11);
        //tree.insert(data12);
        //tree.insert(data13);*/
    }
}
