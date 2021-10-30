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

    public ResponseAndPCRTestId insertPCRTest(String personIdNumber,
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
            return new ResponseAndPCRTestId(ResponseType.PERSON_DOESNT_EXIST,null);
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
            if(!person.insertPCRForPerson(testData)){
                return new ResponseAndPCRTestId(ResponseType.PCR_WITH_ID_EXISTS,testValue.getPCRId().toString());
            }
            //pre dany okres vlozi test
            DistrictKey dKey = new DistrictKey(districtId);
            DistrictData dData = new DistrictData(dKey,null);
            DistrictData testedDistrictNode = (DistrictData) treeOfDistricts.find(dData);
            if(testedDistrictNode == null){
                //vymaze sa test z osoby lebo sa nemoze vkladat do systemu pokial neexistuje okes
                person.deletePCRTest(testData);
                return new ResponseAndPCRTestId(ResponseType.DISTRICT_DOESNT_EXIST,testValue.getPCRId().toString());
            }else {
                if (testedDistrictNode.get_data1().getDistrictId() == districtId){
                    if(!testedDistrictNode.get_value1().insertTest(testData)){
                        person.deletePCRTest(testData);
                        return new ResponseAndPCRTestId(ResponseType.PCR_WITH_ID_EXISTS,testValue.getPCRId().toString());
                    }
                }else {
                    if (!testedDistrictNode.get_value2().insertTest(testData)){
                        person.deletePCRTest(testData);
                        return new ResponseAndPCRTestId(ResponseType.PCR_WITH_ID_EXISTS,testValue.getPCRId().toString());
                    }
                }
            }
            //pre dany kraj vlozi test
            RegionKey rKey = new RegionKey(regionId);
            RegionData rData = new RegionData(rKey,null);
            RegionData testedRegionNode = (RegionData) treeOfRegions.find(rData);
            if(testedRegionNode == null){
                //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                if (testedDistrictNode.get_data1().getDistrictId() == districtId) {
                    testedDistrictNode.get_value1().deletePCRTest(testData);
                }else {
                    testedDistrictNode.get_value2().deletePCRTest(testData);
                }
                person.deletePCRTest(testData);
                return new ResponseAndPCRTestId(ResponseType.REGION_DOESNT_EXIST, testValue.getPCRId().toString());
            }else {
                if (testedRegionNode.get_data1().getRegionId() == regionId){
                    if (!testedRegionNode.get_value1().insertTest(testData)){
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (testedDistrictNode.get_data1().getDistrictId() == districtId) {
                            testedDistrictNode.get_value1().deletePCRTest(testData);
                        }else {
                            testedDistrictNode.get_value2().deletePCRTest(testData);
                        }
                        person.deletePCRTest(testData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_WITH_ID_EXISTS,
                                testValue.getPCRId().toString());
                    }
                }else {
                    if (!testedRegionNode.get_value2().insertTest(testData)){
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (testedDistrictNode.get_data1().getDistrictId() == districtId) {
                            testedDistrictNode.get_value1().deletePCRTest(testData);
                        }else {
                            testedDistrictNode.get_value2().deletePCRTest(testData);
                        }
                        person.deletePCRTest(testData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_WITH_ID_EXISTS,
                                testValue.getPCRId().toString());
                    }
                }
            }
            //pre dane pracovisko vlozi test
            WorkplaceKey wKey = new WorkplaceKey(workplaceId);
            WorkplaceData wData = new WorkplaceData(wKey,null);
            WorkplaceData workplaceNode = (WorkplaceData) treeOfWorkplace.find(wData);
            if(workplaceNode == null){
                //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                if (testedDistrictNode.get_data1().getDistrictId() == districtId) {
                    testedDistrictNode.get_value1().deletePCRTest(testData);
                }else {
                    testedDistrictNode.get_value2().deletePCRTest(testData);
                }
                if (testedRegionNode.get_data1().getRegionId() == regionId){
                    testedRegionNode.get_value1().deletePCRTest(testData);
                }else {
                    testedRegionNode.get_value2().deletePCRTest(testData);
                }
                person.deletePCRTest(testData);
                return new ResponseAndPCRTestId(
                        ResponseType.WORKPLACE_DOESNT_EXIST,
                        testValue.getPCRId().toString());
            }else {
                if (workplaceNode.get_data1().getWorkplaceId() == workplaceId){
                    if (!workplaceNode.get_value1().insertTest(testData)){
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (testedDistrictNode.get_data1().getDistrictId() == districtId) {
                            testedDistrictNode.get_value1().deletePCRTest(testData);
                        }else {
                            testedDistrictNode.get_value2().deletePCRTest(testData);
                        }
                        if (testedRegionNode.get_data1().getRegionId() == regionId){
                            testedRegionNode.get_value1().deletePCRTest(testData);
                        }else {
                            testedRegionNode.get_value2().deletePCRTest(testData);
                        }
                        person.deletePCRTest(testData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_WITH_ID_EXISTS,
                                testValue.getPCRId().toString());
                    }
                }else {
                    if (!workplaceNode.get_value2().insertTest(testData)){
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (testedDistrictNode.get_data1().getDistrictId() == districtId) {
                            testedDistrictNode.get_value1().deletePCRTest(testData);
                        }else {
                            testedDistrictNode.get_value2().deletePCRTest(testData);
                        }
                        if (testedRegionNode.get_data1().getRegionId() == regionId){
                            testedRegionNode.get_value1().deletePCRTest(testData);
                        }else {
                            testedRegionNode.get_value2().deletePCRTest(testData);
                        }
                        person.deletePCRTest(testData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_WITH_ID_EXISTS,
                                testValue.getPCRId().toString());
                    }
                }
            }
            return new ResponseAndPCRTestId(ResponseType.SUCCESS,testValue.getPCRId().toString());
        }
    }

    public boolean insertPerson(String name, String surname, int year, int month, int day, String idNumber){
        PersonKey pKey = new PersonKey(idNumber);
        Person pValue = new Person(name,surname,year,month,day,idNumber);
        PersonData pData = new PersonData(pKey,pValue);
        //PersonData testedPersonNode = (PersonData) treeOfPeople.find(pData);
        return treeOfPeople.insert(pData);
    }

    public class ResponseAndPCRTestId{
        private ResponseType response;
        private String PCRTestId;

        public ResponseAndPCRTestId(ResponseType response, String PCRTestId) {
            this.response = response;
            this.PCRTestId = PCRTestId;
        }

        public ResponseType getResponse() {
            return response;
        }

        public String getPCRTestId() {
            return PCRTestId;
        }
    }
}
