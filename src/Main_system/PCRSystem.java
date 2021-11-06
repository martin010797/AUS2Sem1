package Main_system;

import Models.*;
import Structure.BST23;
import Structure.BST23Node;

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
                PCRData regionTestData = new PCRData(testKey, testValue);
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
                //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                    PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                    ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                }else {
                    PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                    ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                }
                if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                    PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                    ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                }else {
                    PCRData deletedRegionTestData = new PCRData(testKey, testValue);
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
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                            PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                            ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                        }else {
                            PCRData deletedRegionTestData = new PCRData(testKey, testValue);
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
                        //mazania kvoli tomu aby neostali data ked sa nemoze vkladat
                        if (((DistrictKey) testedDistrictNode.get_data1()).getDistrictId() == districtId) {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value1()).deletePCRTest(deletedDistrictTestData);
                        }else {
                            PCRDistrictPositiveData deletedDistrictTestData = new PCRDistrictPositiveData(districtPCRKey, testValue);
                            ((District) testedDistrictNode.get_value2()).deletePCRTest(deletedDistrictTestData);
                        }
                        if (((RegionKey) testedRegionNode.get_data1()).getRegionId() == regionId){
                            PCRData deletedRegionTestData = new PCRData(testKey, testValue);
                            ((Region) testedRegionNode.get_value1()).deletePCRTest(deletedRegionTestData);
                        }else {
                            PCRData deletedRegionTestData = new PCRData(testKey, testValue);
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
            int randMonth = ThreadLocalRandom.current().nextInt(1, 12 - 1);
            int randDay = ThreadLocalRandom.current().nextInt(1, 28 - 1);
            int randHour = ThreadLocalRandom.current().nextInt(1, 24 - 1);
            int randMinute = ThreadLocalRandom.current().nextInt(1, 59 - 1);
            int randSecond = ThreadLocalRandom.current().nextInt(1, 59 - 1);
            int randWorkplace = ThreadLocalRandom.current().nextInt(0, 1000 - 1);
            int randDistrict = ThreadLocalRandom.current().nextInt(0, 200 - 1);
            int randRegion = ThreadLocalRandom.current().nextInt(0, 10 - 1);
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
                if (((PCR) testNode.get_value1()).isResult()){
                    res = "POZITIVNY";
                }else {
                    res = "NEGATIVNY";
                }
                if (((PCRKey) testNode.get_data1()).getPCRId().toString().equals(pcrId)) {
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
