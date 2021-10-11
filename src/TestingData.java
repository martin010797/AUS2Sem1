public class TestingData extends BST23Node<TestingData> implements Comparable<TestingData>{
    private int key;

    public TestingData(TestingData pData1) {
        super(pData1);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public int compareTo(TestingData o) {
        if (key < o.key){
            return 1;
        }else if (key > o.key){
            return -1;
        }else {
            return 0;
        }
    }
}
