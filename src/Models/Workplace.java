package Models;

import Structure.BST23;

public class Workplace {
    private int workplaceId;
    private BST23<PCRKey, PCR> treeOfTests = new BST23<>();

    public Workplace(int workplaceId) {
        this.workplaceId = workplaceId;
    }

    public boolean insertTest(PCRData insertedTest){
        return treeOfTests.insert(insertedTest);
    }

    public boolean deletePCRTest(PCRData deletedTest){
        return treeOfTests.delete(deletedTest);
    }

    public int getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(int workplaceId) {
        this.workplaceId = workplaceId;
    }

    public BST23<PCRKey, PCR> getTreeOfTests() {
        return treeOfTests;
    }

    public void setTreeOfTests(BST23<PCRKey, PCR> treeOfTests) {
        this.treeOfTests = treeOfTests;
    }
}
