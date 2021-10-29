package Models;

import Structure.BST23;

public class District {
    //okres
    private int districtId;
    private String name;
    private BST23<PCRKey, PCR> treeOfTests = new BST23<>();

    public District(int districtId, String name) {
        this.districtId = districtId;
        this.name = name;
    }

    public boolean insertTest(PCRData insertedTest){
        return treeOfTests.insert(insertedTest);
    }

    public boolean deletePCRTest(PCRData deletedTest){
        return treeOfTests.delete(deletedTest);
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BST23<PCRKey, PCR> getTreeOfTestedPeople() {
        return treeOfTests;
    }

    public void setTreeOfTestedPeople(BST23<PCRKey, PCR> treeOfTests) {
        this.treeOfTests = treeOfTests;
    }
}
