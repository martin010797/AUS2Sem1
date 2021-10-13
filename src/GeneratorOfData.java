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
            values.remove(randomIndex);
            TestingData data = new TestingData(d);
            if(!tree.insert(data)){
                System.out.println("nevlozene");
            }else {
                System.out.println("Vlozene cislo " + data.get_data1().getKey());
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

    public void testDelete(){

    }
}
