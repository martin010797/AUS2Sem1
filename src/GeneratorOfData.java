import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GeneratorOfData {

    public void testInsert(int numberOfValues){
        //dorobit test na kombiovanie operácii insert aj delete
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

    public void test(){
        BST23<TestKey, TestingObjectValue> tree = new BST23<>();

        TestKey di = new TestKey(4);
        TestKey di2 = new TestKey(24);
        TestKey di3 = new TestKey(26);
        TestKey di4 = new TestKey(42);
        TestKey di5 = new TestKey(94);
        TestKey di6 = new TestKey(74);
        TestKey di7 = new TestKey(97);
        TestKey di8 = new TestKey(28);
        TestKey di9 = new TestKey(25);
        TestKey di10 = new TestKey(86);
        TestKey di11 = new TestKey(19);
        TestKey di12 = new TestKey(77);
        TestKey di13 = new TestKey(44);
        TestKey di14 = new TestKey(14);
        TestingObjectValue v1 = new TestingObjectValue("Martin4", "NEG");
        TestingObjectValue v2 = new TestingObjectValue("Martin24", "NEG");
        TestingObjectValue v3 = new TestingObjectValue("Martin26", "NEG");
        TestingObjectValue v4 = new TestingObjectValue("Martin42", "NEG");
        TestingObjectValue v5 = new TestingObjectValue("Martin94", "NEG");
        TestingObjectValue v6 = new TestingObjectValue("Martin74", "NEG");
        TestingObjectValue v7 = new TestingObjectValue("Martin97", "NEG");
        TestingObjectValue v8 = new TestingObjectValue("Martin28", "NEG");
        TestingObjectValue v9 = new TestingObjectValue("Martin25", "NEG");
        TestingObjectValue v10 = new TestingObjectValue("Martin86", "NEG");
        TestingObjectValue v11 = new TestingObjectValue("Martin19", "NEG");
        TestingObjectValue v12 = new TestingObjectValue("Martin77", "NEG");
        TestingObjectValue v13 = new TestingObjectValue("Martin44", "NEG");
        TestingObjectValue v14 = new TestingObjectValue("Martin14", "NEG");
        TestingData data1 = new TestingData(di,v1);
        TestingData data2 = new TestingData(di2,v2);
        TestingData data3 = new TestingData(di3,v3);
        TestingData data4 = new TestingData(di4,v4);
        TestingData data5 = new TestingData(di5,v5);
        TestingData data6 = new TestingData(di6,v6);
        TestingData data7 = new TestingData(di7,v7);
        TestingData data8 = new TestingData(di8,v8);
        TestingData data9 = new TestingData(di9,v9);
        TestingData data10 = new TestingData(di10,v10);
        TestingData data11 = new TestingData(di11,v11);
        TestingData data12 = new TestingData(di12,v12);
        TestingData data13 = new TestingData(di13,v13);
        TestingData data14 = new TestingData(di14,v14);

        /*TestKey delD = new TestKey(24);
        TestKey delD2 = new TestKey(4);
        TestKey delD3 = new TestKey(42);
        TestKey delD4 = new TestKey(74);
        TestingData delData1 = new TestingData(delD);
        TestingData delData2 = new TestingData(delD2);
        TestingData delData3 = new TestingData(delD3);
        TestingData delData4 = new TestingData(delD4);*/

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

        TestKey minKey = new TestKey(20);
        TestKey maxKey = new TestKey(22);
        TestingObjectValue minValue = new TestingObjectValue("Martin24", "NEG");
        TestingObjectValue maxValue = new TestingObjectValue("Martin28", "NEG");
        TestingData minTest = new TestingData(minKey,minValue);
        TestingData maxTest = new TestingData(maxKey,maxValue);
        ArrayList<BST23Node> listOfFoundNodes = tree.intervalSearch(minTest,maxTest);
        for (int i = 0; i < listOfFoundNodes.size(); i++){
            System.out.println(listOfFoundNodes.get(i).get_data1());
        }
        //tree.delete(delData4);
    }
}
