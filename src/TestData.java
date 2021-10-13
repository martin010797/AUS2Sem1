public class TestData implements Comparable<TestData> {
    private int key;

    public TestData(int pKey){
        key = pKey;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public int compareTo(TestData o) {
        if (key < o.key){
            return 1;
        }else if (key > o.key){
            return -1;
        }else {
            return 0;
        }
    }
}
