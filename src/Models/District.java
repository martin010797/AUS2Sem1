package Models;

import Structure.BST23;

public class District {
    //okres
    private int districtId;
    private String name;

    //TODO popremyslat mozno radsej strom ludi kti budu mat referenciu aj na testy
    private BST23<PCRKey, PCR> treeOfTests = new BST23<>();
}
