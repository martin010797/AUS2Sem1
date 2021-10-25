public class Main {

    public static void main(String[] args) {
        GeneratorOfData generator = new GeneratorOfData();
        //generator.testInsert(100000);
        //generator.testDelete(100000);
        generator.testRandomOperation(300000, 0.8);
    }
}
