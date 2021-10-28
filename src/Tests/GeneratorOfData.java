package Tests;

import Structure.BST23;
import Structure.BST23Node;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GeneratorOfData {

    public void testInsert(int numberOfValues){
        BST23<TestKey, TestingObjectValue> tree = new BST23<>();
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
            TestKey d = new TestKey(values.get(randomIndex));
            TestingObjectValue v = new TestingObjectValue("Martin", "NEG");
            //TestData d = new TestData(numberOfValues-i);
            values.remove(randomIndex);
            TestingData data = new TestingData(d,v);
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
            TestKey d = new TestKey(i+1);
            TestingData data = new TestingData(d);
            if (tree.find(data) == null){
                System.out.println(data.get_data1().getKey() + " nenajdene");
            }
            if (i == numberOfValues-1){
                System.out.println(" Vsetky vlozene prvky najdene");
            }
        }

        //overovanie vzdialenosti vsetkych listov od korena
        if (testDepth(tree.get_root())) {
            System.out.println("Hlbka vsetkych listov je rovnaka.");
        }else {
            System.out.println("Hlbka vsetkych listov nie je rovnaka!");
        }

    }

    public void testDelete(int numberOfValues){
        BST23<TestKey, TestingObjectValue> tree = new BST23<>();
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
            TestKey d = new TestKey(values.get(randomIndex));
            TestingObjectValue v = new TestingObjectValue("Martin", "NEG");
            values.remove(randomIndex);
            TestingData data = new TestingData(d,v);
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
            TestKey d = new TestKey(i+1);
            TestingObjectValue v = new TestingObjectValue("Martin", "NEG");
            TestingData data = new TestingData(d,v);
            if (tree.delete(data) == false){
                System.out.println(data.get_data1().getKey() + " nevymazane");
            }
            //overovanie vzdialenosti vsetkych listov od korena
            /*if (testDepth(tree.get_root())) {
                System.out.println("Hlbka vsetkych listov je rovnaka.");
            }else {
                System.out.println("Hlbka vsetkych listov nie je rovnaka!");
            }*/
            if (i == numberOfValues-1){
                System.out.println(" Vsetko vymazane");
            }
        }
        /*
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
        Tests.TestingData data1 = new Tests.TestingData(d);
        Tests.TestingData data2 = new Tests.TestingData(d2);
        Tests.TestingData data3 = new Tests.TestingData(d3);
        Tests.TestingData data4 = new Tests.TestingData(d4);
        Tests.TestingData data5 = new Tests.TestingData(d5);
        Tests.TestingData data6 = new Tests.TestingData(d6);
        Tests.TestingData data7 = new Tests.TestingData(d7);
        Tests.TestingData data8 = new Tests.TestingData(d8);
        Tests.TestingData data9 = new Tests.TestingData(d9);
        Tests.TestingData data10 = new Tests.TestingData(d10);
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
        Tests.TestingData delData1 = new Tests.TestingData(delD);
        Tests.TestingData delData2 = new Tests.TestingData(delD2);
        Tests.TestingData delData3 = new Tests.TestingData(delD3);
        Tests.TestingData delData4 = new Tests.TestingData(delD4);
        Tests.TestingData delData5 = new Tests.TestingData(delD5);
        Tests.TestingData delData6 = new Tests.TestingData(delD6);
        Tests.TestingData delData7 = new Tests.TestingData(delD7);
        Tests.TestingData delData8 = new Tests.TestingData(delD8);
        Tests.TestingData delData9 = new Tests.TestingData(delD9);
        Tests.TestingData delData10 = new Tests.TestingData(delD10);

        tree.delete(delData1);
        tree.delete(delData2);
        tree.delete(delData3);
        tree.delete(delData4);
        tree.delete(delData5);
        tree.delete(delData6);
        tree.delete(delData7);
        tree.delete(delData8);
        tree.delete(delData9);
        tree.delete(delData10);*/
    }

    public boolean testDepth(BST23Node root){
        int depth = 1;
        int totalDepth = 0;
        BST23Node prev = null;
        BST23Node temp = root;
        while (temp != null){
            if (!temp.isThreeNode()){
                if (temp.get_left1() == null && temp.get_right1() == null){
                    //ak je listom
                    if (totalDepth == 0) {
                        totalDepth = depth;
                    }
                    if (totalDepth != depth){
                        //listy nemaju rovnaku hlbku
                        return false;
                    }
                    if (totalDepth == depth){
                        prev = temp;
                        temp = prev.get_parent();
                        depth--;
                    }
                }else {
                    //ak nie je listom
                    if (prev == temp.get_left1()){
                        prev = temp;
                        temp = prev.get_right1();
                        depth++;
                    }else if(prev == temp.get_parent()){
                        prev = temp;
                        temp = prev.get_left1();
                        depth++;
                    }else if(prev == temp.get_right1()){
                        prev = temp;
                        temp = prev.get_parent();
                        depth--;
                    }
                }
            }else {
                if (temp.get_left1() == null &&
                        temp.get_right1() == null &&
                        temp.get_left2() == null &&
                        temp.get_right2() == null){
                    //je listom
                    if (totalDepth == 0) {
                        totalDepth = depth;
                    }
                    if (totalDepth != depth){
                        //listy nemaju rovnaku hlbku
                        return false;
                    }
                    if (totalDepth == depth){
                        prev = temp;
                        temp = prev.get_parent();
                        depth--;
                    }
                }else {
                    //nie je listom
                    if (prev == temp.get_parent()){
                        prev = temp;
                        temp = prev.get_left1();
                        depth++;
                    }else if (prev == temp.get_left1()){
                        prev = temp;
                        temp = prev.get_right1();
                        depth++;
                    }else if (prev == temp.get_right1()){
                        prev = temp;
                        temp = prev.get_right2();
                        depth++;
                    }else if (prev == temp.get_right2()){
                        prev = temp;
                        temp = prev.get_parent();
                        depth--;
                    }
                }
            }
        }
        return true;
    }

    public void testRandomOperation(int numberOfValues, double probabilityOfInsert){
        BST23<TestKey, TestingObjectValue> tree = new BST23<>();
        ArrayList<Integer> valuesForInsert = new ArrayList<Integer>();
        for (int i = 0; i < numberOfValues; i++){
            valuesForInsert.add(i+1);
        }
        ArrayList<Integer> valuesInserted = new ArrayList<Integer>();
        for (int i = 0; i < numberOfValues; i++){
            /*if (i == numberOfValues -1){
                System.out.println("Posledna operacia");
            }*/
            double typeOfOperation = Math.random();
            System.out.println(i + ". operacia: " + typeOfOperation);
            if (typeOfOperation < probabilityOfInsert){
                //insert
                int randomIndex;
                if (valuesForInsert.size() == 1){
                    randomIndex = 0;
                }else {
                    randomIndex = ThreadLocalRandom.current().nextInt(0, valuesForInsert.size() - 1);
                }
                Integer value = valuesForInsert.get(randomIndex);
                //TestData d = new TestData(valuesForInsert.get(randomIndex));
                TestKey d = new TestKey(value);
                String name = "Martin"+value;
                TestingObjectValue v = new TestingObjectValue(name, "NEG");
                valuesForInsert.remove(randomIndex);
                TestingData data = new TestingData(d,v);
                if(!tree.insert(data)){
                    System.out.println("cislo " + value + " nevlozene");
                }else {
                    //System.out.println("Vlozene cislo " + data.get_data1().getKey());
                    System.out.println("Vlozene cislo " + value);
                    valuesInserted.add(value);
                }
                if (tree.find(data) == null){
                    System.out.println(value + " neulozene spravne");
                }
            }else {
                //delete
                if (tree.get_root() == null){
                    System.out.println("Nie je co mazat");
                }else {
                    int randomIndex;
                    if (valuesInserted.size() == 1){
                        randomIndex = 0;
                    }else {
                        randomIndex = ThreadLocalRandom.current().nextInt(0, valuesInserted.size() - 1);
                    }
                    Integer value = valuesInserted.get(randomIndex);
                    //TestData d = new TestData(valuesInserted.get(randomIndex));
                    TestKey d = new TestKey(value);
                    String name = "Martin"+value;
                    TestingObjectValue v = new TestingObjectValue(name, "NEG");
                    TestingData data = new TestingData(d,v);
                    System.out.println("pojde sa mazat " + value);
                    if (!tree.delete(data)){
                        System.out.println(value + " nevymazane");
                    }else {
                        System.out.println("Vymazane cislo " + value);
                        valuesInserted.remove(randomIndex);
                    }
                }
            }

        }
        if (testDepth(tree.get_root())) {
            System.out.println("Po testovani operacii je hlbka vsetkych listov rovnaka.");
        }else {
            System.out.println("Po testovani operacii nie je hlbka vsetkych listov rovnaka!");
        }
        int notFound = 0;
        for (Integer value: valuesInserted) {
            TestKey d = new TestKey(value);
            TestingData data = new TestingData(d);
            if (tree.find(data) == null){
                System.out.println(data.get_data1().getKey() + " nenajdene");
                notFound++;
            }
        }
        System.out.println("Pocet nenajdenych prvkov po operaciach: " + notFound);
    }

    public void testIntervalSearch(TestingData min, TestingData max, int numberOfValues){
        BST23<TestKey, TestingObjectValue> tree = new BST23<>();
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
            TestKey d = new TestKey(values.get(randomIndex));
            TestingObjectValue v = new TestingObjectValue("Martin", "NEG");
            //TestData d = new TestData(numberOfValues-i);
            values.remove(randomIndex);
            TestingData data = new TestingData(d,v);
            if(!tree.insert(data)){
                System.out.println("nevlozene");
            }else {
                System.out.println("Vlozene cislo " + data.get_data1().getKey());
            }
            if (tree.find(data) == null){
                System.out.println(data.get_data1().getKey() + " neulozene spravne");
            }
        }

        /*Structure.BST23<Tests.TestKey, Tests.TestingObjectValue> tree = new Structure.BST23<>();
        ArrayList<Integer> valuesForInsert = new ArrayList<Integer>();
        for (int i = 0; i < numberOfValues; i++){
            valuesForInsert.add(i+1);
        }
        ArrayList<Integer> valuesInserted = new ArrayList<Integer>();
        for (int i = 0; i < numberOfValues; i++) {
            double typeOfOperation = Math.random();
            System.out.println(i + ". operacia: " + typeOfOperation);
            if (typeOfOperation < 0.8) {
                //insert
                int randomIndex;
                if (valuesForInsert.size() == 1) {
                    randomIndex = 0;
                } else {
                    randomIndex = ThreadLocalRandom.current().nextInt(0, valuesForInsert.size() - 1);
                }
                Integer value = valuesForInsert.get(randomIndex);
                //TestData d = new TestData(valuesForInsert.get(randomIndex));
                Tests.TestKey d = new Tests.TestKey(value);
                String name = "Martin" + value;
                Tests.TestingObjectValue v = new Tests.TestingObjectValue(name, "NEG");
                valuesForInsert.remove(randomIndex);
                Tests.TestingData data = new Tests.TestingData(d, v);
                if (!tree.insert(data)) {
                    System.out.println("cislo " + value + " nevlozene");
                } else {
                    //System.out.println("Vlozene cislo " + data.get_data1().getKey());
                    System.out.println("Vlozene cislo " + value);
                    valuesInserted.add(value);
                }
                if (tree.find(data) == null) {
                    System.out.println(value + " neulozene spravne");
                }
            } else {
                //delete
                if (tree.get_root() == null) {
                    System.out.println("Nie je co mazat");
                } else {
                    int randomIndex;
                    if (valuesInserted.size() == 1) {
                        randomIndex = 0;
                    } else {
                        randomIndex = ThreadLocalRandom.current().nextInt(0, valuesInserted.size() - 1);
                    }
                    Integer value = valuesInserted.get(randomIndex);
                    //TestData d = new TestData(valuesInserted.get(randomIndex));
                    Tests.TestKey d = new Tests.TestKey(value);
                    String name = "Martin" + value;
                    Tests.TestingObjectValue v = new Tests.TestingObjectValue(name, "NEG");
                    Tests.TestingData data = new Tests.TestingData(d, v);
                    System.out.println("pojde sa mazat " + value);
                    if (!tree.delete(data)) {
                        System.out.println(value + " nevymazane");
                    } else {
                        System.out.println("Vymazane cislo " + value);
                        valuesInserted.remove(randomIndex);
                    }
                }
            }
        }*/

        ArrayList<BST23Node> listOfFoundNodes = tree.intervalSearch(min,max);
        System.out.println("Interval od " + min.get_data1().getKey() + " do "+ max.get_data1().getKey());
        for (int i = 0; i < listOfFoundNodes.size(); i++){
            System.out.println(((TestKey)listOfFoundNodes.get(i).get_data1()).getKey());
        }
    }

    /*public void test(){
        Structure.BST23<Tests.TestKey, Tests.TestingObjectValue> tree = new Structure.BST23<>();

        Tests.TestKey di = new Tests.TestKey(4);
        Tests.TestKey di2 = new Tests.TestKey(24);
        Tests.TestKey di3 = new Tests.TestKey(26);
        Tests.TestKey di4 = new Tests.TestKey(42);
        Tests.TestKey di5 = new Tests.TestKey(94);
        Tests.TestKey di6 = new Tests.TestKey(74);
        Tests.TestKey di7 = new Tests.TestKey(97);
        Tests.TestKey di8 = new Tests.TestKey(28);
        Tests.TestKey di9 = new Tests.TestKey(25);
        Tests.TestKey di10 = new Tests.TestKey(86);
        Tests.TestKey di11 = new Tests.TestKey(19);
        Tests.TestKey di12 = new Tests.TestKey(77);
        Tests.TestKey di13 = new Tests.TestKey(44);
        Tests.TestKey di14 = new Tests.TestKey(14);
        Tests.TestingObjectValue v1 = new Tests.TestingObjectValue("Martin4", "NEG");
        Tests.TestingObjectValue v2 = new Tests.TestingObjectValue("Martin24", "NEG");
        Tests.TestingObjectValue v3 = new Tests.TestingObjectValue("Martin26", "NEG");
        Tests.TestingObjectValue v4 = new Tests.TestingObjectValue("Martin42", "NEG");
        Tests.TestingObjectValue v5 = new Tests.TestingObjectValue("Martin94", "NEG");
        Tests.TestingObjectValue v6 = new Tests.TestingObjectValue("Martin74", "NEG");
        Tests.TestingObjectValue v7 = new Tests.TestingObjectValue("Martin97", "NEG");
        Tests.TestingObjectValue v8 = new Tests.TestingObjectValue("Martin28", "NEG");
        Tests.TestingObjectValue v9 = new Tests.TestingObjectValue("Martin25", "NEG");
        Tests.TestingObjectValue v10 = new Tests.TestingObjectValue("Martin86", "NEG");
        Tests.TestingObjectValue v11 = new Tests.TestingObjectValue("Martin19", "NEG");
        Tests.TestingObjectValue v12 = new Tests.TestingObjectValue("Martin77", "NEG");
        Tests.TestingObjectValue v13 = new Tests.TestingObjectValue("Martin44", "NEG");
        Tests.TestingObjectValue v14 = new Tests.TestingObjectValue("Martin14", "NEG");
        Tests.TestingData data1 = new Tests.TestingData(di,v1);
        Tests.TestingData data2 = new Tests.TestingData(di2,v2);
        Tests.TestingData data3 = new Tests.TestingData(di3,v3);
        Tests.TestingData data4 = new Tests.TestingData(di4,v4);
        Tests.TestingData data5 = new Tests.TestingData(di5,v5);
        Tests.TestingData data6 = new Tests.TestingData(di6,v6);
        Tests.TestingData data7 = new Tests.TestingData(di7,v7);
        Tests.TestingData data8 = new Tests.TestingData(di8,v8);
        Tests.TestingData data9 = new Tests.TestingData(di9,v9);
        Tests.TestingData data10 = new Tests.TestingData(di10,v10);
        Tests.TestingData data11 = new Tests.TestingData(di11,v11);
        Tests.TestingData data12 = new Tests.TestingData(di12,v12);
        Tests.TestingData data13 = new Tests.TestingData(di13,v13);
        Tests.TestingData data14 = new Tests.TestingData(di14,v14);

        //Tests.TestKey delD = new Tests.TestKey(24);
        //Tests.TestKey delD2 = new Tests.TestKey(4);
        //Tests.TestKey delD3 = new Tests.TestKey(42);
        //Tests.TestKey delD4 = new Tests.TestKey(74);
        //Tests.TestingData delData1 = new Tests.TestingData(delD);
        //Tests.TestingData delData2 = new Tests.TestingData(delD2);
        //Tests.TestingData delData3 = new Tests.TestingData(delD3);
        //Tests.TestingData delData4 = new Tests.TestingData(delD4);

        tree.insert(data1);
        tree.insert(data2);
        tree.insert(data3);

        //tree.delete(delData1);

        tree.insert(data4);
        tree.insert(data5);
        tree.insert(data6);
        tree.insert(data7);
        tree.insert(data8);
        tree.insert(data9);
        tree.insert(data10);
        tree.insert(data11);

        //tree.delete(delData2);

        tree.insert(data12);
        tree.insert(data13);

        //tree.delete(delData3);

        tree.insert(data14);

        Tests.TestKey minKey = new Tests.TestKey(0);
        Tests.TestKey maxKey = new Tests.TestKey(100);
        Tests.TestingData minTest = new Tests.TestingData(minKey);
        Tests.TestingData maxTest = new Tests.TestingData(maxKey);
        ArrayList<Structure.BST23Node> listOfFoundNodes = tree.intervalSearch(minTest,maxTest);
        for (int i = 0; i < listOfFoundNodes.size(); i++){
            System.out.println(((Tests.TestKey)listOfFoundNodes.get(i).get_data1()).getKey());
        }
        //tree.delete(delData4);
    }*/
}
