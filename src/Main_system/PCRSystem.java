package Main_system;

import Models.*;
import Structure.BST23;
import Structure.BST23Node;
import Structure.NodeWithKey;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
            PCRData personTestData = new PCRData(testKey, testValue);
            //vlozenie testu do stromu testov v osobe
            if(!person.insertPCRForPerson(personTestData)){
                return new ResponseAndPCRTestId(ResponseType.PCR_WITH_ID_EXISTS,testValue.getPCRId().toString());
            }
            //vlozenie testu do stromov v osobe podla datumu
            PCRKeyDate pKeyDate = new PCRKeyDate(testValue.getDateAndTimeOfTest());
            PCRWorkplaceData pDateData = new PCRWorkplaceData(pKeyDate,testValue);
            if (!person.insertPCRByDateForPerson(pDateData)){
                //nepodarilo sa vlozit tak vymaz z testov pre osobu podla id testu
                PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                person.deletePCRTest(deletedPersonTestData);
                return new ResponseAndPCRTestId(ResponseType.PCR_EXISTS_FOR_THAT_TIME, testValue.getPCRId().toString());
            }
            //pre dany okres vlozi test
            DistrictKey dKey = new DistrictKey(districtId);
            DistrictData dData = new DistrictData(dKey,null);
            BST23Node testedDistrictNode = treeOfDistricts.find(dData);
            if(testedDistrictNode == null){
                //vymaze sa test z osoby lebo sa nemoze vkladat do systemu pokial neexistuje okres
                PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                person.deletePCRTest(deletedPersonTestData);
                PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                person.deletePCRTestByDate(deletedPersonDateTestData);
                return new ResponseAndPCRTestId(ResponseType.DISTRICT_DOESNT_EXIST,testValue.getPCRId().toString());
            }else {
                PCRKeyDistrict districtPCRKey = new PCRKeyDistrict(testValue.isResult(),testValue.getDateAndTimeOfTest());
                PCRDistrictPositiveData districtTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId){
                    if(!((District) testedDistrictNode.get_value1()).insertTest(districtTestData)){
                        PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                        PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                        person.deletePCRTestByDate(deletedPersonDateTestData);
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(ResponseType.PCR_WITH_ID_EXISTS,testValue.getPCRId().toString());
                    }
                }else {
                    if (!((District) testedDistrictNode.get_value2()).insertTest(districtTestData)){
                        PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                        PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                        person.deletePCRTestByDate(deletedPersonDateTestData);
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
                PCRKeyDistrict districtPCRKey = new PCRKeyDistrict(testValue.isResult(),testValue.getDateAndTimeOfTest());
                //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                    PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                    ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                }else {
                    PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                    ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                }
                PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                person.deletePCRTestByDate(deletedPersonDateTestData);
                PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                person.deletePCRTest(deletedPersonTestData);
                return new ResponseAndPCRTestId(ResponseType.REGION_DOESNT_EXIST, testValue.getPCRId().toString());
            }else {
                PCRKeyRegion pcrKeyRegion = new PCRKeyRegion(testValue.isResult(),testValue.getDateAndTimeOfTest());
                PCRRegionData regionTestData = new PCRRegionData(pcrKeyRegion,testValue);
                if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                    if (!((Region) testedRegionNode.get_value1()).insertTest(regionTestData)){
                        PCRKeyDistrict districtPCRKey = new PCRKeyDistrict(testValue.isResult(),testValue.getDateAndTimeOfTest());
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                        PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                        person.deletePCRTestByDate(deletedPersonDateTestData);
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_WITH_ID_EXISTS,
                                testValue.getPCRId().toString());
                    }
                }else {
                    if (!((Region) testedRegionNode.get_value2()).insertTest(regionTestData)){
                        PCRKeyDistrict districtPCRKey = new PCRKeyDistrict(testValue.isResult(),testValue.getDateAndTimeOfTest());
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                        PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                        person.deletePCRTestByDate(deletedPersonDateTestData);
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
                PCRKeyDistrict districtPCRKey = new PCRKeyDistrict(testValue.isResult(),testValue.getDateAndTimeOfTest());
                PCRKeyRegion regionPCRKey = new PCRKeyRegion(testValue.isResult(),testValue.getDateAndTimeOfTest());
                //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                    PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                    ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                }else {
                    PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                    ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                }
                if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                    PCRRegionData deletedRegionTestData = new PCRRegionData(regionPCRKey, testValue);
                    ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                }else {
                    PCRRegionData deletedRegionTestData = new PCRRegionData(regionPCRKey, testValue);
                    ((Region) testedRegionNode.get_value2()).deletePCRTest(deletedRegionTestData);
                }
                PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                person.deletePCRTestByDate(deletedPersonDateTestData);
                PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                person.deletePCRTest(deletedPersonTestData);
                return new ResponseAndPCRTestId(
                        ResponseType.WORKPLACE_DOESNT_EXIST,
                        testValue.getPCRId().toString());
            }else {
                PCRKeyDate testWorkplaceKey = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                PCRWorkplaceData workplaceTestData = new PCRWorkplaceData(testWorkplaceKey, testValue);
                if (((WorkplaceKey) workplaceNode.get_data1()).getWorkplaceId() == workplaceId){
                    if (!((Workplace) workplaceNode.get_value1()).insertTest(workplaceTestData)){
                        PCRKeyDistrict districtPCRKey = new PCRKeyDistrict(testValue.isResult(),testValue.getDateAndTimeOfTest());
                        PCRKeyRegion regionPCRKey = new PCRKeyRegion(testValue.isResult(),testValue.getDateAndTimeOfTest());
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                            PCRRegionData deletedRegionTestData = new PCRRegionData(regionPCRKey, testValue);
                            ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                        }else {
                            PCRRegionData deletedRegionTestData = new PCRRegionData(regionPCRKey, testValue);
                            ((Region) testedRegionNode.get_value2()).deletePCRTest(deletedRegionTestData);
                        }
                        PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                        PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                        person.deletePCRTestByDate(deletedPersonDateTestData);
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_EXISTS_FOR_THAT_TIME,
                                testValue.getPCRId().toString());
                    }
                }else {
                    if (!((Workplace) workplaceNode.get_value2()).insertTest(workplaceTestData)){
                        PCRKeyDistrict districtPCRKey = new PCRKeyDistrict(testValue.isResult(),testValue.getDateAndTimeOfTest());
                        PCRKeyRegion regionPCRKey = new PCRKeyRegion(testValue.isResult(),testValue.getDateAndTimeOfTest());
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                            PCRRegionData deletedRegionTestData = new PCRRegionData(regionPCRKey, testValue);
                            ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                        }else {
                            PCRRegionData deletedRegionTestData = new PCRRegionData(regionPCRKey, testValue);
                            ((Region) testedRegionNode.get_value2()).deletePCRTest(deletedRegionTestData);
                        }
                        PCRKeyDate pKeyDateDeleted = new PCRKeyDate(testValue.getDateAndTimeOfTest());
                        PCRWorkplaceData deletedPersonDateTestData = new PCRWorkplaceData(pKeyDateDeleted,testValue);
                        person.deletePCRTestByDate(deletedPersonDateTestData);
                        PCRData deletedPersonTestData = new PCRData(testKey, testValue);
                        person.deletePCRTest(deletedPersonTestData);
                        return new ResponseAndPCRTestId(
                                ResponseType.PCR_EXISTS_FOR_THAT_TIME,
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
        return treeOfPeople.insert(pData);
    }

    private void generateDistrictsRegionsAndWorkplaces(){
        for (int i = 0; i < 10; i++){
            RegionKey rKey = new RegionKey(i);
            Region rValue = new Region(i,"Kraj "+i);
            RegionData rData = new RegionData(rKey,rValue);
            treeOfRegions.insert(rData);
        }
        for (int i = 0; i < 200; i++){
            DistrictKey dKey = new DistrictKey(i);
            District dValue = new District(i, "Okres "+i);
            DistrictData dData = new DistrictData(dKey,dValue);
            treeOfDistricts.insert(dData);
        }
        for (int i = 0; i < 1000; i++){
            WorkplaceKey wKey = new WorkplaceKey(i);
            Workplace wValue = new Workplace(i);
            WorkplaceData wData = new WorkplaceData(wKey,wValue);
            treeOfWorkplace.insert(wData);
        }
        for (int i = 0; i < 5000; i++){
            PersonKey pKey = new PersonKey(Integer.toString(i+1));
            Person pValue = new Person(
                    "Meno"+(i+1),
                    "Priezvisko"+(i+1),
                    1998,
                    2,
                    3,
                    Integer.toString(i+1));
            PersonData pData = new PersonData(pKey,pValue);
            treeOfPeople.insert(pData);
        }

        try {
            File myObj = new File("rod_cislo_test.txt");
            myObj.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10000; i++){
            double positivity = Math.random();
            boolean boolPos;
            if (positivity < 0.5){
                boolPos = true;
            }else {
                boolPos = false;
            }
            int randIdPerson = ThreadLocalRandom.current().nextInt(1, 5000 - 1);
            int randYear = ThreadLocalRandom.current().nextInt(2019, 2023 - 1);
            int randMonth = ThreadLocalRandom.current().nextInt(1, 13);
            int randDay = ThreadLocalRandom.current().nextInt(1, 29);
            int randHour = ThreadLocalRandom.current().nextInt(1, 25);
            int randMinute = ThreadLocalRandom.current().nextInt(1, 59 - 1);
            int randSecond = ThreadLocalRandom.current().nextInt(1, 59 - 1);
            int randWorkplace = ThreadLocalRandom.current().nextInt(0, 1000 - 1);
            int randDistrict = ThreadLocalRandom.current().nextInt(0, 200);
            int randRegion = ThreadLocalRandom.current().nextInt(0, 10);
            ResponseAndPCRTestId response = insertPCRTest(
                    Integer.toString(randIdPerson),
                    randYear,
                    randMonth,
                    randDay,
                    randHour,
                    randMinute,
                    randSecond,
                    randWorkplace,
                    randDistrict,
                    randRegion,
                    boolPos,
                    ""
                    );
            try {
                FileWriter myWriter;
                if (i == 0){
                    myWriter = new FileWriter("rod_cislo_test.txt");
                    myWriter.write("");
                }else{
                    myWriter = new FileWriter("rod_cislo_test.txt", true);
                }
                myWriter.write(randIdPerson + " " + response.getPCRTestId() + "\n");
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public PersonPCRResult findTestResultForPerson(String personId, String pcrId){
        //najdi osobu s danym rodnym cislom
        PersonKey pKey = new PersonKey(personId);
        PersonData pData = new PersonData(pKey,null);
        BST23Node testedPersonNode = treeOfPeople.find(pData);
        if (testedPersonNode == null){
            //osoba sa v systeme nenachadza
            return new PersonPCRResult(ResponseType.PERSON_DOESNT_EXIST,null);
        }else {
            Person person;
            if (((PersonKey) testedPersonNode.get_data1()).getIdNumber().equals(personId)) {
                person = ((Person) testedPersonNode.get_value1());
            } else {
                person = ((Person) testedPersonNode.get_value2());
            }
            PCRKey tKey;
            try{
                tKey = new PCRKey(UUID.fromString(pcrId));
            }catch (Exception exception){
                return new PersonPCRResult(
                        ResponseType.PCR_DOESNT_EXIST,
                        person.getName() + " " + person.getSurname());
            }
            PCRData tData = new PCRData(tKey, null);
            BST23Node testNode = person.getTreeOfTests().find(tData);
            if (testNode == null){
                return new PersonPCRResult(
                        ResponseType.PCR_DOESNT_EXIST,
                        person.getName() + " " + person.getSurname());
            }else {
                String resultString = person.getName() + " " + person.getSurname() + "\n" + person.getIdNumber() +
                        "\nNarodeny: " + person.getDateOfBirth().getDate() + "." + (person.getDateOfBirth().getMonth()+1)
                        + "." + person.getDateOfBirth().getYear();
                String res;
                if (((PCRKey) testNode.get_data1()).getPCRId().toString().equals(pcrId)) {
                    if (((PCR) testNode.get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    resultString += "\nKod testu: " + ((PCR) testNode.get_value1()).getPCRId() + "\nDatum a cas testu: "
                            + ((PCR) testNode.get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) testNode.get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) testNode.get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) testNode.get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) testNode.get_value1()).getDateAndTimeOfTest().getMinutes() + "\nKod pracoviska: "
                            + ((PCR) testNode.get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) testNode.get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) testNode.get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: " + ((PCR) testNode.get_value1()).getDescription();
                    return new PersonPCRResult(
                            ResponseType.SUCCESS, resultString);
                } else {
                    if (((PCR) testNode.get_value2()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    resultString += "\nKod testu: " + ((PCR) testNode.get_value2()).getPCRId() + "\n Datum a cas testu: "
                            + ((PCR) testNode.get_value2()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) testNode.get_value2()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) testNode.get_value2()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) testNode.get_value2()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) testNode.get_value2()).getDateAndTimeOfTest().getMinutes() + "\nKod pracoviska: "
                            + ((PCR) testNode.get_value2()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) testNode.get_value2()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) testNode.get_value2()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: " + ((PCR) testNode.get_value2()).getDescription();
                    return new PersonPCRResult(
                            ResponseType.SUCCESS, resultString);
                }
            }
        }
    }

    public PersonPCRResult findTestResultForPerson(Person person, String pcrId){
        PCRKey tKey;
        try{
            tKey = new PCRKey(UUID.fromString(pcrId));
        }catch (Exception exception){
            return new PersonPCRResult(
                    ResponseType.INCORRECT_PCR_FORMAT,
                    person.getName() + " " + person.getSurname());
        }
        PCRData tData = new PCRData(tKey, null);
        BST23Node testNode = person.getTreeOfTests().find(tData);
        if (testNode == null){
            return new PersonPCRResult(
                    ResponseType.PCR_DOESNT_EXIST,
                    person.getName() + " " + person.getSurname());
        }else {
            String resultString = person.getName() + " " + person.getSurname() + "\n" + person.getIdNumber() +
                    "\nNarodeny: " + person.getDateOfBirth().getDate() + "." + (person.getDateOfBirth().getMonth()+1)
                    + "." + person.getDateOfBirth().getYear();
            String res;
            if (((PCRKey) testNode.get_data1()).getPCRId().toString().equals(pcrId)) {
                if (((PCR) testNode.get_value1()).isResult()){
                    res = "POZITIVNY";
                }else {
                    res = "NEGATIVNY";
                }
                resultString += "\nKod testu: " + ((PCR) testNode.get_value1()).getPCRId() + "\nDatum a cas testu: "
                        + ((PCR) testNode.get_value1()).getDateAndTimeOfTest().getDate() + "."
                        + (((PCR) testNode.get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                        + ((PCR) testNode.get_value1()).getDateAndTimeOfTest().getYear() + " "
                        + ((PCR) testNode.get_value1()).getDateAndTimeOfTest().getHours() + ":"
                        + ((PCR) testNode.get_value1()).getDateAndTimeOfTest().getMinutes() + "\nKod pracoviska: "
                        + ((PCR) testNode.get_value1()).getWorkplaceId() + "\nKod okresu: "
                        + ((PCR) testNode.get_value1()).getDistrictId() + "\nKod kraja: "
                        + ((PCR) testNode.get_value1()).getRegionId() + "\nVysledok testu: "
                        + res + "\nPoznamka k testu: " + ((PCR) testNode.get_value1()).getDescription();
                return new PersonPCRResult(
                        ResponseType.SUCCESS, resultString);
            } else {
                if (((PCR) testNode.get_value2()).isResult()){
                    res = "POZITIVNY";
                }else {
                    res = "NEGATIVNY";
                }
                resultString += "\nKod testu: " + ((PCR) testNode.get_value2()).getPCRId() + "\n Datum a cas testu: "
                        + ((PCR) testNode.get_value2()).getDateAndTimeOfTest().getDate() + "."
                        + (((PCR) testNode.get_value2()).getDateAndTimeOfTest().getMonth()+1) + "."
                        + ((PCR) testNode.get_value2()).getDateAndTimeOfTest().getYear() + " "
                        + ((PCR) testNode.get_value2()).getDateAndTimeOfTest().getHours() + ":"
                        + ((PCR) testNode.get_value2()).getDateAndTimeOfTest().getMinutes() + "\nKod pracoviska: "
                        + ((PCR) testNode.get_value2()).getWorkplaceId() + "\nKod okresu: "
                        + ((PCR) testNode.get_value2()).getDistrictId() + "\nKod kraja: "
                        + ((PCR) testNode.get_value2()).getRegionId() + "\nVysledok testu: "
                        + res + "\nPoznamka k testu: " + ((PCR) testNode.get_value2()).getDescription();
                return new PersonPCRResult(
                        ResponseType.SUCCESS, resultString);
            }
        }
    }

    public PersonPCRResult searchForTestsInWorkplace(int workplaceId, Date dateFrom, Date dateTo){
        String resultString = "";
        WorkplaceKey wKey = new WorkplaceKey(workplaceId);
        WorkplaceData wData = new WorkplaceData(wKey,null);
        BST23Node workplaceNode = treeOfWorkplace.find(wData);
        if (workplaceNode == null){
            return new PersonPCRResult(ResponseType.WORKPLACE_DOESNT_EXIST,null);
        }else {
            if (dateFrom.compareTo(dateTo) > 0){
                return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
            }
            PCRKeyDate pKeyFrom = new PCRKeyDate(dateFrom);
            PCRWorkplaceData pDataFrom = new PCRWorkplaceData(pKeyFrom,null);
            PCRKeyDate pKeyTo = new PCRKeyDate(dateTo);
            PCRWorkplaceData pDataTo = new PCRWorkplaceData(pKeyTo,null);
            if (((WorkplaceKey) workplaceNode.get_data1()).getWorkplaceId() == workplaceId){
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((Workplace) workplaceNode.get_value1()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                if (listOfFoundNodes.size() == 0){
                    resultString = "Ziadne najdene testy pre pracovisko v zadanych datumoch.";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }else {
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((Workplace) workplaceNode.get_value2()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu:"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                if (listOfFoundNodes.size() == 0){
                    resultString = "Ziadne najdene testy pre pracovisko v zadanych datumoch.";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }
        }
    }

    public PersonPCRResult searchSickPeopleInRegion(int regionId, Date dateFrom, Date dateTo, boolean positivity){
        String resultString = "";
        RegionKey rKey = new RegionKey(regionId);
        RegionData rData = new RegionData(rKey,null);
        BST23Node regionNode = treeOfRegions.find(rData);
        if (regionNode == null){
            return new PersonPCRResult(ResponseType.REGION_DOESNT_EXIST,null);
        }else {
            if (dateFrom.compareTo(dateTo) > 0){
                return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
            }
            PCRKeyRegion pKeyFrom = new PCRKeyRegion(positivity,dateFrom);
            PCRRegionData pDataFrom = new PCRRegionData(pKeyFrom,null);
            PCRKeyRegion pKeyTo = new PCRKeyRegion(positivity,dateTo);
            PCRRegionData pDataTo = new PCRRegionData(pKeyTo,null);
            if (((RegionKey) regionNode.get_data1()).getRegionId() == regionId){
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((Region) regionNode.get_value1()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Chory na zaklade testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }else {
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((Region) regionNode.get_value2()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Chory na zaklade testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu:"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }
        }
    }

    public PersonPCRResult getSortedRegionsBySickPeople(Date dateFrom, Date dateTo){
        if (dateFrom.compareTo(dateTo) > 0){
            return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
        }
        String resultString = "";
        int numberOfSickPeople = 0;
        BST23<RegionSickCountKey, Region> regionsSortedByNumberOfSickPeople = new BST23<>();
        NodeWithKey firstNode = treeOfRegions.getFirst();
        if (firstNode == null){
            //ziadne kraje tak vrati prazdne
            return new PersonPCRResult(ResponseType.SUCCESS, resultString);
        }else {
            numberOfSickPeople = getNumberOfSickInRegion(
                    ((Region) firstNode.getNode().get_value1()),
                    dateFrom,
                    dateTo);
            //vlozenie kraju do stromu kde sa usporiadava podla poctu chorych
            RegionSickCountKey key = new RegionSickCountKey(
                    numberOfSickPeople, ((RegionKey) firstNode.getKey()).getRegionId());
            Region value = ((Region) firstNode.getNode().get_value1());
            RegionSickCountData data = new RegionSickCountData(key,value);
            regionsSortedByNumberOfSickPeople.insert(data);
        }
        NodeWithKey nextNode = treeOfRegions.getNext(firstNode.getNode(), (RegionKey) firstNode.getKey());
        while (nextNode != null){
            if (((RegionKey) nextNode.getKey()).getRegionId() == ((RegionKey) nextNode.getNode().get_data1()).getRegionId()){
                numberOfSickPeople = getNumberOfSickInRegion(
                        ((Region) nextNode.getNode().get_value1()),
                        dateFrom,
                        dateTo);
                //vlozenie kraju do stromu kde sa usporiadava podla poctu chorych
                RegionSickCountKey key = new RegionSickCountKey(
                        numberOfSickPeople, ((RegionKey) nextNode.getKey()).getRegionId());
                Region value = ((Region) nextNode.getNode().get_value1());
                RegionSickCountData data = new RegionSickCountData(key,value);
                regionsSortedByNumberOfSickPeople.insert(data);
            }else {
                numberOfSickPeople = getNumberOfSickInRegion(
                        ((Region) nextNode.getNode().get_value2()),
                        dateFrom,
                        dateTo);
                //vlozenie kraju do stromu kde sa usporiadava podla poctu chorych
                RegionSickCountKey key = new RegionSickCountKey(
                        numberOfSickPeople, ((RegionKey) nextNode.getKey()).getRegionId());
                Region value = ((Region) nextNode.getNode().get_value2());
                RegionSickCountData data = new RegionSickCountData(key,value);
                regionsSortedByNumberOfSickPeople.insert(data);
            }
            nextNode = treeOfRegions.getNext(nextNode.getNode(), ((RegionKey) nextNode.getKey()));
        }
        //prejdenie stromu s krajmi zoradenymi podla poctu chorych
        NodeWithKey firstRegion = regionsSortedByNumberOfSickPeople.getFirst();
        int order = 0;
        if (firstRegion == null){
            return new PersonPCRResult(ResponseType.SUCCESS, resultString);
        }else {
            order++;
            resultString += getStringOfRegionsBySickCount(firstRegion, order);
        }
        NodeWithKey nextRegion = regionsSortedByNumberOfSickPeople.getNext(
                firstRegion.getNode(), ((RegionSickCountKey) firstRegion.getKey()));
        while (nextRegion != null){
            order++;
            resultString += getStringOfRegionsBySickCount(nextRegion, order);
            nextRegion = regionsSortedByNumberOfSickPeople.getNext(
                    nextRegion.getNode(), ((RegionSickCountKey) nextRegion.getKey()));
        }
        return new PersonPCRResult(ResponseType.SUCCESS, resultString);
    }

    private String getStringOfRegionsBySickCount(NodeWithKey pNodeWithKey, int nextValue){
        String resultString = "";
        if (((RegionSickCountKey) pNodeWithKey.getNode().get_data1()).equals(((RegionSickCountKey) pNodeWithKey.getKey()))){
            resultString += nextValue + ". " + ((Region) pNodeWithKey.getNode().get_value1()).getName() + "\n" +
                    "Pocet chorych = " +
                    ((RegionSickCountKey) pNodeWithKey.getNode().get_data1()).getNumberOfSickPeople() + "\n" +
                    "---------------------------------\n";
        }else {
            resultString += nextValue + ". " + ((Region) pNodeWithKey.getNode().get_value2()).getName() + "\n" +
                    "Pocet chorych = " +
                    ((RegionSickCountKey) pNodeWithKey.getNode().get_data2()).getNumberOfSickPeople() + "\n" +
                    "---------------------------------\n";
        }
        return resultString;
    }

    private int getNumberOfSickInRegion(Region region, Date dateFrom, Date dateTo){
        PCRKeyRegion pKeyFrom = new PCRKeyRegion(true,dateFrom);
        PCRRegionData pDataFrom = new PCRRegionData(pKeyFrom,null);
        PCRKeyRegion pKeyTo = new PCRKeyRegion(true,dateTo);
        PCRRegionData pDataTo = new PCRRegionData(pKeyTo,null);
        ArrayList<BST23Node> listOfFoundNodes = region.getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
        return listOfFoundNodes.size();
    }

    public PersonPCRResult getSortedDistrictsBySickPeople(Date dateFrom, Date dateTo){
        if (dateFrom.compareTo(dateTo) > 0){
            return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
        }
        String resultString = "";
        int numberOfSickPeople = 0;
        BST23<DistrictSickCountKey, District> districtSortedByNumberOfSickPeople = new BST23<>();
        NodeWithKey firstNode = treeOfDistricts.getFirst();
        if (firstNode == null){
            //ziadne okresy tak vrati prazdne
            return new PersonPCRResult(ResponseType.SUCCESS, resultString);
        }else {
            numberOfSickPeople = getNumberOfSickInDistrict(
                    ((District) firstNode.getNode().get_value1()),
                    dateFrom,
                    dateTo);
            //vlozenie okresu do stromu kde sa usporiadava podla poctu chorych
            DistrictSickCountKey key = new DistrictSickCountKey(
                    numberOfSickPeople, ((DistrictKey) firstNode.getKey()).getDistrictId());
            District value = ((District) firstNode.getNode().get_value1());
            DistrictSickCountData data = new DistrictSickCountData(key,value);
            districtSortedByNumberOfSickPeople.insert(data);
        }
        NodeWithKey nextNode = treeOfDistricts.getNext(firstNode.getNode(), (DistrictKey) firstNode.getKey());
        while (nextNode != null){
            if (((DistrictKey) nextNode.getKey()).getDistrictId() == ((DistrictKey) nextNode.getNode().get_data1()).getDistrictId()){
                numberOfSickPeople = getNumberOfSickInDistrict(
                        ((District) nextNode.getNode().get_value1()),
                        dateFrom,
                        dateTo);
                //vlozenie okresu do stromu kde sa usporiadava podla poctu chorych
                DistrictSickCountKey key = new DistrictSickCountKey(
                        numberOfSickPeople, ((DistrictKey) nextNode.getKey()).getDistrictId());
                District value = ((District) nextNode.getNode().get_value1());
                DistrictSickCountData data = new DistrictSickCountData(key,value);
                districtSortedByNumberOfSickPeople.insert(data);
            }else {
                numberOfSickPeople = getNumberOfSickInDistrict(
                        ((District) nextNode.getNode().get_value2()),
                        dateFrom,
                        dateTo);
                //vlozenie okresu do stromu kde sa usporiadava podla poctu chorych
                DistrictSickCountKey key = new DistrictSickCountKey(
                        numberOfSickPeople, ((DistrictKey) nextNode.getKey()).getDistrictId());
                District value = ((District) nextNode.getNode().get_value2());
                DistrictSickCountData data = new DistrictSickCountData(key,value);
                districtSortedByNumberOfSickPeople.insert(data);
            }
            nextNode = treeOfDistricts.getNext(nextNode.getNode(), ((DistrictKey) nextNode.getKey()));
        }
        //prejdenie stromu s okresmi zoradenymi podla poctu chorych
        NodeWithKey firstDistrict = districtSortedByNumberOfSickPeople.getFirst();
        int order = 0;
        if (firstDistrict == null){
            return new PersonPCRResult(ResponseType.SUCCESS, resultString);
        }else {
            order++;
            resultString += getStringOfDistrictsBySickCount(firstDistrict, order);
        }
        NodeWithKey nextDistrict = districtSortedByNumberOfSickPeople.getNext(
                firstDistrict.getNode(), ((DistrictSickCountKey) firstDistrict.getKey()));
        while (nextDistrict != null){
            order++;
            resultString += getStringOfDistrictsBySickCount(nextDistrict, order);
            nextDistrict = districtSortedByNumberOfSickPeople.getNext(
                    nextDistrict.getNode(), ((DistrictSickCountKey) nextDistrict.getKey()));
        }
        return new PersonPCRResult(ResponseType.SUCCESS, resultString);
    }

    private String getStringOfDistrictsBySickCount(NodeWithKey pNodeWithKey, int nextValue){
        String resultString = "";
        if (((DistrictSickCountKey) pNodeWithKey.getNode().get_data1()).equals(((DistrictSickCountKey) pNodeWithKey.getKey()))){
            resultString += nextValue + ". " + ((District) pNodeWithKey.getNode().get_value1()).getName() + "\n" +
                    "Pocet chorych = " +
                    ((DistrictSickCountKey) pNodeWithKey.getNode().get_data1()).getNumberOfSickPeople() + "\n" +
                    "---------------------------------\n";
        }else {
            resultString += nextValue + ". " + ((District) pNodeWithKey.getNode().get_value2()).getName() + "\n" +
                    "Pocet chorych = " +
                    ((DistrictSickCountKey) pNodeWithKey.getNode().get_data2()).getNumberOfSickPeople() + "\n" +
                    "---------------------------------\n";
        }
        return resultString;
    }

    private int getNumberOfSickInDistrict(District district, Date dateFrom, Date dateTo){
        PCRKeyDistrict pKeyFrom = new PCRKeyDistrict(true,dateFrom);
        PCRDistrictPositiveData pDataFrom = new PCRDistrictPositiveData(pKeyFrom,null);
        PCRKeyDistrict pKeyTo = new PCRKeyDistrict(true,dateTo);
        PCRDistrictPositiveData pDataTo = new PCRDistrictPositiveData(pKeyTo,null);
        ArrayList<BST23Node> listOfFoundNodes = district.getTreeOfTestedPeople().intervalSearch(pDataFrom,pDataTo);
        return listOfFoundNodes.size();
    }

    public PersonPCRResult searchSickPeopleInDistrict(int districtId, Date dateFrom, Date dateTo, boolean positivity){
        String resultString = "";
        DistrictKey dKey = new DistrictKey(districtId);
        DistrictData dData = new DistrictData(dKey,null);
        BST23Node districtNode = treeOfDistricts.find(dData);
        if (districtNode == null){
            return new PersonPCRResult(ResponseType.DISTRICT_DOESNT_EXIST,null);
        }else {
            if (dateFrom.compareTo(dateTo) > 0){
                return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
            }
            PCRKeyDistrict pKeyFrom = new PCRKeyDistrict(positivity,dateFrom);
            PCRDistrictPositiveData pDataFrom = new PCRDistrictPositiveData(pKeyFrom,null);
            PCRKeyDistrict pKeyTo = new PCRKeyDistrict(positivity,dateTo);
            PCRDistrictPositiveData pDataTo = new PCRDistrictPositiveData(pKeyTo,null);
            if (((DistrictKey) districtNode.get_data1()).getDistrictId() == districtId){
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((District) districtNode.get_value1()).getTreeOfTestedPeople().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Chory na zaklade testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }else {
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((District) districtNode.get_value2()).getTreeOfTestedPeople().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Chory na zaklade testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu:"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }
        }
    }

    public PersonPCRResult searchTestsInDistrict(int districtId, Date dateFrom, Date dateTo, boolean positivity){
        String resultString = "";
        DistrictKey dKey = new DistrictKey(districtId);
        DistrictData dData = new DistrictData(dKey,null);
        BST23Node districtNode = treeOfDistricts.find(dData);
        if (districtNode == null){
            return new PersonPCRResult(ResponseType.DISTRICT_DOESNT_EXIST,null);
        }else {
            if (dateFrom.compareTo(dateTo) > 0){
                return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
            }
            PCRKeyDistrict pKeyFrom = new PCRKeyDistrict(positivity,dateFrom);
            PCRDistrictPositiveData pDataFrom = new PCRDistrictPositiveData(pKeyFrom,null);
            PCRKeyDistrict pKeyTo = new PCRKeyDistrict(positivity,dateTo);
            PCRDistrictPositiveData pDataTo = new PCRDistrictPositiveData(pKeyTo,null);
            if (((DistrictKey) districtNode.get_data1()).getDistrictId() == districtId){
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((District) districtNode.get_value1()).getTreeOfTestedPeople().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                if (listOfFoundNodes.size() == 0){
                    resultString = "Ziadne najdene testy v zadanych datumoch.";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }else {
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((District) districtNode.get_value2()).getTreeOfTestedPeople().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu:"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                if (listOfFoundNodes.size() == 0){
                    resultString = "Ziadne najdene testy v zadanych datumoch.";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }
        }
    }

    public PersonPCRResult searchTestsInRegion(int regionId, Date dateFrom, Date dateTo, boolean positivity){
        String resultString = "";
        RegionKey rKey = new RegionKey(regionId);
        RegionData rData = new RegionData(rKey,null);
        BST23Node regionNode = treeOfRegions.find(rData);
        if (regionNode == null){
            return new PersonPCRResult(ResponseType.REGION_DOESNT_EXIST,null);
        }else {
            if (dateFrom.compareTo(dateTo) > 0){
                return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
            }
            PCRKeyRegion pKeyFrom = new PCRKeyRegion(positivity,dateFrom);
            PCRRegionData pDataFrom = new PCRRegionData(pKeyFrom,null);
            PCRKeyRegion pKeyTo = new PCRKeyRegion(positivity,dateTo);
            PCRRegionData pDataTo = new PCRRegionData(pKeyTo,null);
            if (((RegionKey) regionNode.get_data1()).getRegionId() == regionId){
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((Region) regionNode.get_value1()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                if (listOfFoundNodes.size() == 0){
                    resultString = "Ziadne najdene testy v zadanych datumoch.";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }else {
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((Region) regionNode.get_value2()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu:"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                if (listOfFoundNodes.size() == 0){
                    resultString = "Ziadne najdene testy v zadanych datumoch.";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }
        }
    }

    public PersonPCRResult searchTestsInAllRegions(Date dateFrom, Date dateTo, boolean positivity){
        if (dateFrom.compareTo(dateTo) > 0){
            return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
        }
        String resultString = "";
        NodeWithKey firstNode = treeOfRegions.getFirst();
        if (firstNode == null){
            return new PersonPCRResult(ResponseType.SUCCESS, resultString);
        }else {
            resultString += getTestsStringForRegion(firstNode, dateFrom, dateTo, positivity);
        }
        NodeWithKey nextNode = treeOfRegions.getNext(firstNode.getNode(), (RegionKey) firstNode.getKey());
        while (nextNode != null){
            resultString += getTestsStringForRegion(nextNode, dateFrom, dateTo, positivity);
            nextNode = treeOfRegions.getNext(nextNode.getNode(), (RegionKey) nextNode.getKey());
        }
        return new PersonPCRResult(ResponseType.SUCCESS, resultString);
    }

    public PersonPCRResult findPCRTestById(String PCRId){
        NodeWithKey firstNode = treeOfPeople.getFirst();
        PersonPCRResult result;
        if (firstNode == null){
            return new PersonPCRResult(ResponseType.SUCCESS, null);
        }else {
            result = findTestResultForPerson(((Person) firstNode.getNode().get_value1()), PCRId);
            if (result.getResponseType() == ResponseType.SUCCESS){
                return result;
            }
            if (result.getResponseType() == ResponseType.INCORRECT_PCR_FORMAT){
                return new PersonPCRResult(ResponseType.INCORRECT_PCR_FORMAT, null);
            }
        }
        NodeWithKey nextNode = treeOfPeople.getNext(firstNode.getNode(), ((PersonKey) firstNode.getKey()));
        while (nextNode != null){
            if (((PersonKey) nextNode.getKey()).getIdNumber().equals(((PersonKey) nextNode.getNode().get_data1()).getIdNumber())){
                result = findTestResultForPerson(((Person) nextNode.getNode().get_value1()), PCRId);
            }else {
                result = findTestResultForPerson(((Person) nextNode.getNode().get_value2()), PCRId);
            }
            if (result.getResponseType() == ResponseType.SUCCESS){
                return result;
            }
            if (result.getResponseType() == ResponseType.INCORRECT_PCR_FORMAT){
                return new PersonPCRResult(ResponseType.INCORRECT_PCR_FORMAT, null);
            }
            nextNode = treeOfPeople.getNext(nextNode.getNode(), ((PersonKey) nextNode.getKey()));
        }
        return new PersonPCRResult(ResponseType.PCR_DOESNT_EXIST, null);
    }

    public PersonPCRResult searchSickPeopleInAllRegions(Date dateFrom, Date dateTo){
        if (dateFrom.compareTo(dateTo) > 0){
            return new PersonPCRResult(ResponseType.LOWER_FROM_DATE,null);
        }
        String resultString = "";
        NodeWithKey firstNode = treeOfRegions.getFirst();
        if (firstNode == null){
            return new PersonPCRResult(ResponseType.SUCCESS, resultString);
        }else {
            resultString += getSickPeopleStringForRegion(firstNode, dateFrom, dateTo);
        }
        NodeWithKey nextNode = treeOfRegions.getNext(firstNode.getNode(), (RegionKey) firstNode.getKey());
        while (nextNode != null){
            resultString += getSickPeopleStringForRegion(nextNode, dateFrom, dateTo);
            nextNode = treeOfRegions.getNext(nextNode.getNode(), (RegionKey) nextNode.getKey());
        }
        return new PersonPCRResult(ResponseType.SUCCESS, resultString);
    }

    private String getSickPeopleStringForRegion(NodeWithKey pNodeWithKey, Date dateFrom, Date dateTo){
        String resultString = "";
        PCRKeyRegion pKeyFrom = new PCRKeyRegion(true,dateFrom);
        PCRRegionData pDataFrom = new PCRRegionData(pKeyFrom,null);
        PCRKeyRegion pKeyTo = new PCRKeyRegion(true,dateTo);
        PCRRegionData pDataTo = new PCRRegionData(pKeyTo,null);
        if (((RegionKey) pNodeWithKey.getNode().get_data1()).getRegionId() == ((RegionKey) pNodeWithKey.getKey()).getRegionId()){
            ArrayList<BST23Node> listOfFoundNodes;
            listOfFoundNodes = ((Region) pNodeWithKey.getNode().get_value1()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
            for (int i = 0; i < listOfFoundNodes.size(); i++){
                String res;
                if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                    res = "POZITIVNY";
                }else {
                    res = "NEGATIVNY";
                }
                Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                resultString += person.getName() + " " + person.getSurname()
                        + "\n" + person.getIdNumber() +
                        "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                        + (person.getDateOfBirth().getMonth()+1)
                        + "." + person.getDateOfBirth().getYear() + "\n"
                        + "Chory na zaklade testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                        + "\nDatum a cas testu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                        + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                        + "\nKod pracoviska: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                        + res + "\nPoznamka k testu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                        + "\n-----------------------------------------\n";
            }
            return resultString;
        }else {
            ArrayList<BST23Node> listOfFoundNodes;
            listOfFoundNodes = ((Region) pNodeWithKey.getNode().get_value2()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
            for (int i = 0; i < listOfFoundNodes.size(); i++){
                String res;
                if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                    res = "POZITIVNY";
                }else {
                    res = "NEGATIVNY";
                }
                Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                resultString += person.getName() + " " + person.getSurname()
                        + "\n" + person.getIdNumber() +
                        "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                        + (person.getDateOfBirth().getMonth()+1)
                        + "." + person.getDateOfBirth().getYear() + "\n"
                        + "Chory na zaklade testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                        + "\nDatum a cas testu:"
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                        + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                        + "\nKod pracoviska: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                        + res + "\nPoznamka k testu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                        + "\n-----------------------------------------\n";
            }
            return resultString;
        }
    }

    private String getTestsStringForRegion(NodeWithKey pNodeWithKey, Date dateFrom, Date dateTo, boolean positivity){
        String resultString = "";
        PCRKeyRegion pKeyFrom = new PCRKeyRegion(positivity,dateFrom);
        PCRRegionData pDataFrom = new PCRRegionData(pKeyFrom,null);
        PCRKeyRegion pKeyTo = new PCRKeyRegion(positivity,dateTo);
        PCRRegionData pDataTo = new PCRRegionData(pKeyTo,null);
        if (((RegionKey) pNodeWithKey.getNode().get_data1()).getRegionId() == ((RegionKey) pNodeWithKey.getKey()).getRegionId()){
            ArrayList<BST23Node> listOfFoundNodes;
            listOfFoundNodes = ((Region) pNodeWithKey.getNode().get_value1()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
            for (int i = 0; i < listOfFoundNodes.size(); i++){
                String res;
                if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                    res = "POZITIVNY";
                }else {
                    res = "NEGATIVNY";
                }
                Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                        + "\n" + person.getIdNumber() +
                        "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                        + (person.getDateOfBirth().getMonth()+1)
                        + "." + person.getDateOfBirth().getYear() + "\n"
                        + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                        + "\nDatum a cas testu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                        + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                        + "\nKod pracoviska: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                        + res + "\nPoznamka k testu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                        + "\n-----------------------------------------\n";
            }
            return resultString;
        }else {
            ArrayList<BST23Node> listOfFoundNodes;
            listOfFoundNodes = ((Region) pNodeWithKey.getNode().get_value2()).getTreeOfTests().intervalSearch(pDataFrom,pDataTo);
            for (int i = 0; i < listOfFoundNodes.size(); i++){
                String res;
                if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                    res = "POZITIVNY";
                }else {
                    res = "NEGATIVNY";
                }
                Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                        + "\n" + person.getIdNumber() +
                        "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                        + (person.getDateOfBirth().getMonth()+1)
                        + "." + person.getDateOfBirth().getYear() + "\n"
                        + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                        + "\nDatum a cas testu:"
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                        + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                        + "\nKod pracoviska: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                        + res + "\nPoznamka k testu: "
                        + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                        + "\n-----------------------------------------\n";
            }
            return resultString;
        }
    }

    public PersonPCRResult searchTestsForPerson(String personId){
        String resultString = "";
        PersonKey pKey = new PersonKey(personId);
        PersonData pData = new PersonData(pKey,null);
        BST23Node personNode = treeOfPeople.find(pData);
        if (personNode == null){
            return new PersonPCRResult(ResponseType.PERSON_DOESNT_EXIST,null);
        }else {
            if (((PersonKey) personNode.get_data1()).getIdNumber().equals(personId)){
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((Person) personNode.get_value1()).getTreeOfTestsByDate().inOrder();
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                if (listOfFoundNodes.size() == 0){
                    resultString = "Ziadne najdene testy pre osobu " +
                            ((Person) personNode.get_value1()).getName() + " " +
                            ((Person) personNode.get_value1()).getSurname() + ".";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }else {
                ArrayList<BST23Node> listOfFoundNodes;
                listOfFoundNodes = ((Person) personNode.get_value2()).getTreeOfTests().inOrder();
                for (int i = 0; i < listOfFoundNodes.size(); i++){
                    String res;
                    if (((PCR) listOfFoundNodes.get(i).get_value1()).isResult()){
                        res = "POZITIVNY";
                    }else {
                        res = "NEGATIVNY";
                    }
                    Person person = ((PCR) listOfFoundNodes.get(i).get_value1()).getPerson();
                    resultString += (i+1) + ". \n" + person.getName() + " " + person.getSurname()
                            + "\n" + person.getIdNumber() +
                            "\nNarodeny: " + person.getDateOfBirth().getDate() + "."
                            + (person.getDateOfBirth().getMonth()+1)
                            + "." + person.getDateOfBirth().getYear() + "\n"
                            + "Kod testu: " + ((PCR) listOfFoundNodes.get(i).get_value1()).getPCRId()
                            + "\nDatum a cas testu:"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getDate() + "."
                            + (((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMonth()+1) + "."
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getYear() + " "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getHours() + ":"
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDateAndTimeOfTest().getMinutes()
                            + "\nKod pracoviska: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getWorkplaceId() + "\nKod okresu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDistrictId() + "\nKod kraja: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getRegionId() + "\nVysledok testu: "
                            + res + "\nPoznamka k testu: "
                            + ((PCR) listOfFoundNodes.get(i).get_value1()).getDescription()
                            + "\n-----------------------------------------\n";
                }
                if (listOfFoundNodes.size() == 0){
                    resultString = "Ziadne najdene testy pre osobu " +
                            ((Person) personNode.get_value2()).getName() + " " +
                            ((Person) personNode.get_value2()).getSurname() + ".";
                }
                return new PersonPCRResult(ResponseType.SUCCESS,resultString);
            }
        }
    }
}
