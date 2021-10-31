package Main_system;

import Models.*;
import Structure.BST23;
import Structure.BST23Node;

public class PCRSystem {
    //stromy
    private BST23<PersonKey, Person> treeOfPeople = new BST23<>();
    private BST23<DistrictKey, District> treeOfDistricts = new BST23<>();
    private BST23<RegionKey, Region> treeOfRegions = new BST23<>();
    private BST23<WorkplaceKey, Workplace> treeOfWorkplace = new BST23<>();

    public PCRSystem(){
        generateDistrictsRegionsAndWorkplaces();
    }

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
        BST23Node testedPersonNode = treeOfPeople.find(pData);
        if (testedPersonNode == null){
            //osoba sa v systeme nenachadza
            return new ResponseAndPCRTestId(ResponseType.PERSON_DOESNT_EXIST,null);
        }else {
            //vytvorenie testu
            //TODO asi vytvorit novy person pretoze to je referencia a meni aj hodnotu v inych stromoch
            Person person;
            if (((PersonKey) testedPersonNode.get_data1()).getIdNumber().equals(personIdNumber)){
                person = ((Person) testedPersonNode.get_value1());
            }else{
                person = ((Person) testedPersonNode.get_value2());
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
            //TODO testdata musia byt asi vzdy pred vkladanim do roznych stromov vytvori nanovo lebo vsade sa tym padom vklada ta ista referencia
            //TODO opatrne s vkladanim tych istych premennych do inych stromov(mozu sa navzajom ovplyvnovat)
            //TODO ak by sa objavovali prvky na inych miestach ako by mali byt tak moze byt problem tu
            PCRData personTestData = new PCRData(testKey, testValue);
            //vlozenie testu do stromu testov v osobe
            if(!person.insertPCRForPerson(personTestData)){
                return new ResponseAndPCRTestId(ResponseType.PCR_WITH_ID_EXISTS,testValue.getPCRId().toString());
            }
            //pre dany okres vlozi test
            DistrictKey dKey = new DistrictKey(districtId);
            DistrictData dData = new DistrictData(dKey,null);
            //DistrictData testedDistrictNode;
            //DistrictData testedDistrictNode = (DistrictData) treeOfDistricts.find(dData);
            BST23Node testedDistrictNode = treeOfDistricts.find(dData);
            if(testedDistrictNode == null){
                //vymaze sa test z osoby lebo sa nemoze vkladat do systemu pokial neexistuje okes
                PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                person.deletePCRTest(deletedPersonTestData);
                return new ResponseAndPCRTestId(ResponseType.DISTRICT_DOESNT_EXIST,testValue.getPCRId().toString());
            }else {
                /*testedDistrictNode = new DistrictData(
                        ((DistrictKey) districtParentNode.get_data1()),
                        ((District) districtParentNode.get_value1()));
                testedDistrictNode.set_data2((DistrictKey) districtParentNode.get_data2());
                testedDistrictNode.set_value2((District) districtParentNode.get_value2());*/
                PCRData districtTestData = new PCRData(testKey, testValue);
                if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId){
                    if(!((District) testedDistrictNode.get_value1()).insertTest(districtTestData)){
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(ResponseType.PCR_WITH_ID_EXISTS,testValue.getPCRId().toString());
                    }
                }else {
                    if (!((District) testedDistrictNode.get_value2()).insertTest(districtTestData)){
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(ResponseType.PCR_WITH_ID_EXISTS,testValue.getPCRId().toString());
                    }
                }
            }
            //pre dany kraj vlozi test
            RegionKey rKey = new RegionKey(regionId);
            RegionData rData = new RegionData(rKey,null);
            BST23Node testedRegionNode = treeOfRegions.find(rData);
            if(testedRegionNode == null){
                //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                    PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                    ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                }else {
                    PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                    ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                }
                PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                person.deletePCRTest(deletedPersonTestData);
                return new ResponseAndPCRTestId(ResponseType.REGION_DOESNT_EXIST, testValue.getPCRId().toString());
            }else {
                PCRData regionTestData = new PCRData(testKey, testValue);
                if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                    if (!((Region) testedRegionNode.get_value1()).insertTest(regionTestData)){
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_WITH_ID_EXISTS,
                                testValue.getPCRId().toString());
                    }
                }else {
                    if (!((Region) testedRegionNode.get_value2()).insertTest(regionTestData)){
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_WITH_ID_EXISTS,
                                testValue.getPCRId().toString());
                    }
                }
            }
            //pre dane pracovisko vlozi test
            WorkplaceKey wKey = new WorkplaceKey(workplaceId);
            WorkplaceData wData = new WorkplaceData(wKey,null);
            BST23Node workplaceNode = treeOfWorkplace.find(wData);
            if(workplaceNode == null){
                //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                    PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                    ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                }else {
                    PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                    ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                }
                if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                    PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                    ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                }else {
                    PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                    ((Region) testedRegionNode.get_value2()).deletePCRTest(deletedRegionTestData);
                }
                PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                person.deletePCRTest(deletedPersonTestData);
                return new ResponseAndPCRTestId(
                        ResponseType.WORKPLACE_DOESNT_EXIST,
                        testValue.getPCRId().toString());
            }else {
                PCRData workplaceTestData = new PCRData(testKey, testValue);
                if (((WorkplaceKey) workplaceNode.get_data1()).getWorkplaceId() == workplaceId){
                    if (!((Workplace) workplaceNode.get_value1()).insertTest(workplaceTestData)){
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                            PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                            ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                        }else {
                            PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                            ((Region) testedRegionNode.get_value2()).deletePCRTest(deletedRegionTestData);
                        }
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_WITH_ID_EXISTS,
                                testValue.getPCRId().toString());
                    }
                }else {
                    if (!((Workplace) workplaceNode.get_value2()).insertTest(workplaceTestData)){
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRData deletedDistrictTestData = new PCRData(testKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                            PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                            ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                        }else {
                            PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                            ((Region) testedRegionNode.get_value2()).deletePCRTest(deletedRegionTestData);
                        }
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
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

    private void generateDistrictsRegionsAndWorkplaces(){
        for (int i = 0; i < 10; i++){
            RegionKey rKey = new RegionKey(i);
            Region rValue = new Region(i,"Kraj "+i);
            RegionData rData = new RegionData(rKey,rValue);
            treeOfRegions.insert(rData);
        }
        for (int i = 0; i < 100; i++){
            DistrictKey dKey = new DistrictKey(i);
            District dValue = new District(i, "Okres "+i);
            DistrictData dData = new DistrictData(dKey,dValue);
            treeOfDistricts.insert(dData);
        }
        for (int i = 0; i < 300; i++){
            WorkplaceKey wKey = new WorkplaceKey(i);
            Workplace wValue = new Workplace(i);
            WorkplaceData wData = new WorkplaceData(wKey,wValue);
            treeOfWorkplace.insert(wData);
        }
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
