package Models;

import Structure.BST23;

public class Region {
    //kraj
    private int regionId;
    private String name;
    private BST23<PCRKey, PCR> treeOfTests = new BST23<>();

    public Region(int regionId, String name) {
        this.regionId = regionId;
        this.name = name;
    }

    public boolean insertTest(PCRData insertedTest){
        return treeOfTests.insert(insertedTest);
    }

    public boolean deletePCRTest(PCRData deletedTest){
        return treeOfTests.delete(deletedTest);
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BST23<PCRKey, PCR> getTreeOfTests() {
        return treeOfTests;
    }

    public void setTreeOfTests(BST23<PCRKey, PCR> treeOfTests) {
        this.treeOfTests = treeOfTests;
    }
}
