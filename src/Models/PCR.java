package Models;

import java.util.Date;
import java.util.UUID;

public class PCR {
    private Date dateAndTimeOfTest;
    private String patientId;
    private UUID PCRId;
    private int workplaceId;
    private int districtId;
    private int regionId;
    private boolean result;
    private String description;

    public PCR(int year,
               int month,
               int day,
               int hour,
               int minute,
               int second,
               String pPatienId,
               int pWorkplaceId,
               int pDistrictId,
               int pRegionId,
               boolean pResult,
               String pDescription){
        dateAndTimeOfTest = new Date(year,month-1,day,hour,minute,second);
        patientId = pPatienId;
        PCRId = UUID.randomUUID();
        workplaceId = pWorkplaceId;
        districtId = pDistrictId;
        regionId = pRegionId;
        result = pResult;
        description = pDescription;
    }

    public Date getDateAndTimeOfTest() {
        return dateAndTimeOfTest;
    }

    public void setDateAndTimeOfTest(Date dateAndTimeOfTest) {
        this.dateAndTimeOfTest = dateAndTimeOfTest;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public UUID getPCRId() {
        return PCRId;
    }

    public void setPCRId(UUID PCRId) {
        this.PCRId = PCRId;
    }

    public int getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(int workplaceId) {
        this.workplaceId = workplaceId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
