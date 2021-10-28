package Models;

import Structure.BST23;
import Structure.BST23Node;

import java.util.Date;
import java.util.GregorianCalendar;

public class Person {
    private String name;
    private String surname;
    private Date dateOfBirth;
    private String idNumber;
    private BST23<PCRKey, PCR> treeOfTests = new BST23<>();

    public Person(String pName, String pSurname, int pYear, int pMonth, int pDay, String pIdNumber){
        name = pName;
        surname = pSurname;
        //dateOfBirth = new Date(2005, Calendar.DECEMBER,13,12,35,44);
        dateOfBirth = new GregorianCalendar(2001, 2, 24).getTime();
        idNumber = pIdNumber;
    }

    public boolean insertPCRForPerson(BST23Node PCRTest){
        return treeOfTests.insert(PCRTest);
    }

    public boolean deletePCRTest(BST23Node deletedTest){
        return treeOfTests.delete(deletedTest);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public BST23<PCRKey, PCR> getTreeOfTests() {
        return treeOfTests;
    }

    public void setTreeOfTests(BST23<PCRKey, PCR> treeOfTests) {
        this.treeOfTests = treeOfTests;
    }
}
