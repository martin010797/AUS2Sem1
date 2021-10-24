import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GeneratorOfData {

    public void testInsert(int numberOfValues){
        //dorobit test na kombiovanie operácii insert aj delete
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

    public void testRandomOperation(int numberOfValues){
        BST23<TestData> tree = new BST23<>();
        ArrayList<Integer> valuesForInsert = new ArrayList<Integer>();
        for (int i = 0; i < numberOfValues; i++){
            valuesForInsert.add(i+1);
        }
        ArrayList<Integer> valuesInserted = new ArrayList<Integer>();
        for (int i = 0; i < numberOfValues; i++){
            if (i == numberOfValues -1){
                System.out.println("Posledna operacia");
            }
            if (i < 100){
                //prvych 100 sa bude len vkladat
                int randomIndex;
                if (valuesForInsert.size() == 1){
                    randomIndex = 0;
                }else {
                    randomIndex = ThreadLocalRandom.current().nextInt(0, valuesForInsert.size() - 1);
                }
                Integer value = valuesForInsert.get(randomIndex);
                //TestData d = new TestData(valuesForInsert.get(randomIndex));
                TestData d = new TestData(value);
                valuesForInsert.remove(randomIndex);
                TestingData data = new TestingData(d);
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
                double typeOfOperation = Math.random();
                System.out.println(i + ". operacia: " + typeOfOperation);
                if (typeOfOperation < 0.5){
                    //insert
                    int randomIndex;
                    if (valuesForInsert.size() == 1){
                        randomIndex = 0;
                    }else {
                        randomIndex = ThreadLocalRandom.current().nextInt(0, valuesForInsert.size() - 1);
                    }
                    Integer value = valuesForInsert.get(randomIndex);
                    //TestData d = new TestData(valuesForInsert.get(randomIndex));
                    TestData d = new TestData(value);
                    valuesForInsert.remove(randomIndex);
                    TestingData data = new TestingData(d);
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
                    //overovanie vzdialenosti vsetkych listov od korena
                /*if (testDepth(tree.get_root())) {
                    System.out.println("Po vlozeni je hlbka vsetkych listov rovnaka.");
                }else {
                    System.out.println("Po vlozeni nie je hlbka vsetkych listov rovnaka!");
                }*/
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
                        TestData d = new TestData(value);
                        TestingData data = new TestingData(d);
                        System.out.println("pojde sa mazat " + value);
                        if (!tree.delete(data)){
                            System.out.println(value + " nevymazane");
                        }else {
                            System.out.println("Vymazane cislo " + value);
                            valuesInserted.remove(randomIndex);
                        }
                        //overovanie vzdialenosti vsetkych listov od korena
                    /*if (testDepth(tree.get_root())) {
                        System.out.println("Po mazani prvku je hlbka vsetkych listov rovnaka.");
                    }else {
                        System.out.println("Po mazani prvku nie je hlbka vsetkych listov rovnaka!");
                    }*/
                    }
                }
            }

        }

    }

    public void test(){
        BST23<TestData> tree = new BST23<>();

        TestData di = new TestData(4);
        TestData di2 = new TestData(24);
        TestData di3 = new TestData(26);
        TestData di4 = new TestData(42);
        TestData di5 = new TestData(94);
        TestData di6 = new TestData(74);
        TestData di7 = new TestData(97);
        TestData di8 = new TestData(28);
        TestData di9 = new TestData(25);
        TestData di10 = new TestData(86);
        TestData di11 = new TestData(19);
        TestData di12 = new TestData(77);
        TestData di13 = new TestData(44);
        TestData di14 = new TestData(14);
        TestingData data1 = new TestingData(di);
        TestingData data2 = new TestingData(di2);
        TestingData data3 = new TestingData(di3);
        TestingData data4 = new TestingData(di4);
        TestingData data5 = new TestingData(di5);
        TestingData data6 = new TestingData(di6);
        TestingData data7 = new TestingData(di7);
        TestingData data8 = new TestingData(di8);
        TestingData data9 = new TestingData(di9);
        TestingData data10 = new TestingData(di10);
        TestingData data11 = new TestingData(di11);
        TestingData data12 = new TestingData(di12);
        TestingData data13 = new TestingData(di13);
        TestingData data14 = new TestingData(di14);

        TestData delD = new TestData(24);
        TestData delD2 = new TestData(4);
        TestData delD3 = new TestData(42);
        TestData delD4 = new TestData(74);
        TestingData delData1 = new TestingData(delD);
        TestingData delData2 = new TestingData(delD2);
        TestingData delData3 = new TestingData(delD3);
        TestingData delData4 = new TestingData(delD4);

        tree.insert(data1);
        tree.insert(data2);
        tree.insert(data3);

        tree.delete(delData1);

        tree.insert(data4);
        tree.insert(data5);
        tree.insert(data6);
        tree.insert(data7);
        tree.insert(data8);
        tree.insert(data9);
        tree.insert(data10);
        tree.insert(data11);

        tree.delete(delData2);

        tree.insert(data12);
        tree.insert(data13);

        tree.delete(delData3);

        tree.insert(data14);

        tree.delete(delData4);
    }
}
