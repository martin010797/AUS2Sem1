package Main_system;

import Models.*;
import Structure.BST23;
import Tests.TestKey;
import Tests.TestingData;

public class PCRSystem {
    //stromy
    private BST23<PersonKey, Person> treeOfPeople = new BST23<>();
    private BST23<DistrictKey, District> treeOfDistricts = new BST23<>();
    private BST23<RegionKey, Region> treeOfRegions = new BST23<>();
    private BST23<WorkplaceKey, Workplace> treeOfWorkplace = new BST23<>();


    //ulohy
    public void insertPCRTest(String personIdNumber,
                              int yearOfTest,
                              int monthOfTest,
                              int dayOfTest,
                              int hourOfTest,
                              int minuteOfTest,
                              int secondOfTest,
                              int workplaceId,
                              int districtId,
                              int regionId,
                              boolean result,
                              String description){
        //overuje sa ci osoba pre dany system existuje. ak nie tak hod chybu
        PersonKey pKey = new PersonKey(personIdNumber);
        PersonData pData = new PersonData(pKey,null);
        PersonData testedPersonNode = (PersonData) treeOfPeople.find(pData);
        if (testedPersonNode == null){
            //osoba sa v systeme nenachadza
            //TODO vyhodit nejaku chybu
        }else {
            //vytvorenie testu
            Person person;
            if (testedPersonNode.get_data1().getIdNumber().equals(personIdNumber)){
                person = testedPersonNode.get_value1();
            }else{
                //TODO overit ci spravne rozpozna
                person = testedPersonNode.get_value2();
            }
            PCR testValue = new PCR(
                    yearOfTest,
                    monthOfTest,
                    dayOfTest,
                    hourOfTest,
                    minuteOfTest,
                    secondOfTest,
                    personIdNumber,
                    workplaceId,
                    districtId,
                    regionId,
                    result,
                    description,
                    person);
            PCRKey testKey = new PCRKey(testValue.getPCRId());
            PCRData testData = new PCRData(testKey, testValue);
            //vlozenie testu do stromu testov v osobe
            person.insertPCRForPerson(testData);
            //pre dany okres vlozi test
            DistrictKey dKey = new DistrictKey(districtId);
            DistrictData dData = new DistrictData(dKey,null);
            DistrictData testedDistrictNode = (DistrictData) treeOfDistricts.find(dData);
            if(testedDistrictNode == null){
                //okres neexistuje
                //TODO vyhodit nejaku chybu
            }else {
                if (testedDistrictNode.get_data1().getDistrictId() == districtId){
                    testedDistrictNode.get_value1().insertTest(testData);
                }else {
                    testedDistrictNode.get_value2().insertTest(testData);
                }
            }
            //pre dany kraj vlozi test
            RegionKey rKey = new RegionKey(regionId);
            RegionData rData = new RegionData(rKey,null);
            RegionData testedRegionNode = (RegionData) treeOfRegions.find(rData);
            if(testedRegionNode == null){
                //kraj neexistuje
                //TODO vyhodit nejaku chybu
            }else {
                if (testedRegionNode.get_data1().getRegionId() == regionId){
                    testedRegionNode.get_value1().insertTest(testData);
                }else {
                    testedRegionNode.get_value2().insertTest(testData);
                }
            }
            //pre dane pracovisko vlozi test
            WorkplaceKey wKey = new WorkplaceKey(workplaceId);
            WorkplaceData wData = new WorkplaceData(wKey,null);
            WorkplaceData workplaceNode = (WorkplaceData) treeOfWorkplace.find(wData);
            if(workplaceNode == null){
                //pracovisko neexistuje
                //TODO vyhodit nejaku chybu
            }else {
                if (workplaceNode.get_data1().getWorkplaceId() == workplaceId){
                    workplaceNode.get_value1().insertTest(testData);
                }else {
                    workplaceNode.get_value2().insertTest(testData);
                }
            }
        }
    }
}
