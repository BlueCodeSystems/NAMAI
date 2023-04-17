package org.smartregister.anc.library.activity;

import static org.smartregister.anc.library.activity.ReportActivity3.firstTrimesterCounter;
import static org.smartregister.anc.library.activity.ReportActivity3.secondTrimesterCounter;
import static org.smartregister.anc.library.activity.ReportActivity3.thirdTrimesterCounter;

import java.io.Serializable;

public class ReportModel1 implements Serializable {

    private String product_name;
    private String date_of_birth;
    private String gender;
    private String fpnumber;
    private String drug_type;
    private String drug_implant;
    private String drug_injection;
    private String last_fp_method;
    private String feedback;
    private String querry_drug;
    private String countFeedback;
    private String trimester;
    private String age;
    private String origin;
    private String originCount;
    private String firstCount;
    private String secondCount;
    private String thirdCount;
    private String FourthToSeventhCount;
    private String EighthAboveCount;
    private String firstID;
    private String secondID;
    private String thirdID;
    private String FourthToSeventhID;
    private String EighthAboveID;
    private String firstC;
    private String secondC;
    private String thirdC;
    private String FourthToSeventhC;
    private String EighthAboveC;
    private String firstCID;
    private String secondCID;
    private String thirdCID;
    private String FourthToSeventhCID;
    private String EighthAboveCID;
    private String generalBaseEntityID;
    private String generalValue;
    private String generalKey;
    private String KeygeneralBaseEntityID;
    private String KeygeneralValue;
    private String KeygeneralKey;
    private String syphScreenedC;
    private String syphPositiveC;
    private String hepbScreenedC;
    private String ProvidedITNC;
    private String alreadyOnARTC;
    private String startedARTC;
    private String alreadyOnPrepC;
    private String startedOnPrepC;
    private String dewormedC;
    private String providedIronC;
    private String anaemiaScreenedC;
    private String hepBPositiveC;
    private String anaemiaPositiveC;
    private String IPTP1C;
    private String IPTP2C;
    private String IPTP3C;
    private String IPTP4C;


    public String getGeneralKey() {
        return generalKey;
    }

    public void setGeneralKey(String generalKey) {
        this.generalKey = generalKey;
    }

    public String getGeneralBaseEntityID() {
        return generalBaseEntityID;
    }

    public void setGeneralBaseEntityID(String generalBaseEntityID) {
        this.generalBaseEntityID = generalBaseEntityID;
    }

    public String getGeneralValue() {
        return generalValue;
    }

    public void setGeneralValue(String generalValue) {
        this.generalValue = generalValue;
    }

    public String getKeyGeneralKey() {
        return KeygeneralKey;
    }

    public void setKeyGeneralKey(String KeygeneralKey) {
        this.KeygeneralKey = KeygeneralKey;
    }

    public String getKeyGeneralBaseEntityID() {
        return KeygeneralBaseEntityID;
    }

    public void setKeyGeneralBaseEntityID(String KeygeneralBaseEntityID) {
        this.KeygeneralBaseEntityID = KeygeneralBaseEntityID;
    }

    public String getKeyGeneralValue() {
        return KeygeneralValue;
    }

    public void setKeyGeneralValue(String KeygeneralValue) {
        this.KeygeneralValue = KeygeneralValue;
    }

    public String getCountFeedback() {
        return countFeedback;
    }

    public void setCountFeedback(String countFeedback) {
        this.countFeedback = countFeedback;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOriginCount(String originCount) {
        this.originCount = originCount;
    }

    public String getOriginCount() {
        return originCount;
    }

    public void setFirstCount(String firstCount) {
        this.firstCount = firstCount;
    }

    public String getFirstCount() {
        return firstCount;
    }

    public void setSecondCount(String secondCount) {
        this.secondCount = secondCount;
    }

    public String getsecondCount() {
        return secondCount;
    }

    public void setThirdCount(String thirdCount) {
        this.thirdCount = thirdCount;
    }

    public String getThirdCount() {
        return thirdCount;
    }

    public void setFourthToSeventhCount(String FourthToSeventhCount) {
        this.FourthToSeventhCount = FourthToSeventhCount;
    }

    public String getFourthToSeventhCount() {
        return FourthToSeventhCount;
    }

    public void setEighthAboveCount(String EighthAboveCount) {
        this.EighthAboveCount = EighthAboveCount;
    }

    public String getEighthAboveCount() {
        return EighthAboveCount;
    }

    public void setFirstC(String firstC) {
        this.firstC = firstC;
    }

    public String getFirstC() {
        return firstC;
    }

    public void setSecondC(String secondC) {
        this.secondC = secondC;
    }

    public String getSecondC() {
        return secondC;
    }

    public void setThirdC(String thirdC) {
        this.thirdC = thirdC;
    }

    public String getThirdC() {
        return thirdC;
    }

    public void setFourthToSeventhC(String FourthToSeventhC) {
        this.FourthToSeventhC = FourthToSeventhC;
    }

    public String getFourthToSeventhC() {
        return FourthToSeventhC;
    }

    public void setEighthAboveC(String EighthAboveC) {
        this.EighthAboveC = EighthAboveC;
    }

    public String getEighthAboveC() {
        return EighthAboveC;
    }

    public void setFirstCID(String firstCID) {
        this.firstCID = firstCID;
    }

    public String getFirstCID() {
        return firstCID;
    }

    public void setFirstIDCount(String firstID) {
        this.firstID = firstID;
    }

    public void setThirdIDCount(String thirdID) {
        this.thirdID = thirdID;
    }

    public void setSecondIDCount(String secondID) {
        this.secondID = secondID;
    }

    public void setFourthToSeventhIDCount(String FourthToSeventhID) {
        this.FourthToSeventhID = FourthToSeventhID;
    }

    public void setEighthAboveIDCount(String EighthAboveID) {
        this.EighthAboveID = EighthAboveID;
    }

    public String getFirstIDCount() {
        return firstID;
    }

    public String getSecondIDCount() {
        return secondID;
    }

    public String getThirdIDCount() {
        return thirdID;
    }

    public String getFourthToSeventhIDCount() {
        return FourthToSeventhID;
    }

    public String getEighthAboveIDCount() {
        return EighthAboveID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getDrug_type() {
        return drug_type;
    }

    public String getQuerry_drug() {
        return querry_drug;
    }

    public void setQuerry_drug(String querry_drug) {
        this.querry_drug = querry_drug;
    }

    public String getTrimester() {
        return trimester;
    }

    public void setTrimester(String trimester) {
        if(trimester.contains("\"gest_age_openmrs\":\"8\"") && firstTrimesterCounter < 1)
        {
            this.trimester = "First Trimester";
            firstTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"9\"") && firstTrimesterCounter < 1)
        {
            this.trimester = "First Trimester";
            firstTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"10\"") && firstTrimesterCounter < 1)
        {
            this.trimester = "First Trimester";
            firstTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"11\"") && firstTrimesterCounter < 1)
        {
            this.trimester = "First Trimester";
            firstTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"12\"") && firstTrimesterCounter < 1)
        {
            this.trimester = "First Trimester";
            firstTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"13\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"14\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"15\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"16\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"17\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"18\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"19\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"20\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"21\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"22\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"23\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"24\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"25\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"26\"") && secondTrimesterCounter < 1)
        {
            this.trimester = "Second Trimester";
            secondTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"27\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"28\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"29\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"30\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"31\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"32\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"33\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"34\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"35\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"36\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"37\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"38\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"39\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        else if(trimester.contains("\"gest_age_openmrs\":\"40\"") && thirdTrimesterCounter < 1)
        {
            this.trimester = "Third Trimester";
            thirdTrimesterCounter = 1;
        }
        //this.trimester = trimester;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDrug_type(String drug_type) {
        this.drug_type = drug_type;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getLast_fp_method() {
        return last_fp_method;
    }

    public void setLast_fp_method(String last_fp_method) {
        this.last_fp_method = last_fp_method;
    }

    public String getDrug_implant() {
        return drug_implant;
    }

    public void setDrug_implant(String drug_implant) {
        this.drug_implant = drug_implant;
    }

    public String getDrug_injection() {
        return drug_injection;
    }

    public void setDrug_injection(String drug_injection) {
        this.drug_injection = drug_injection;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFpnumber() {
        return fpnumber;
    }

    public void setFpnumber(String fpnumber) {
        this.fpnumber = fpnumber;
    }

    public void setHepBPositiveC(String hepBPositiveC) {
        this.hepBPositiveC = hepBPositiveC;
    }

    public void setAnaemiaScreenedC(String anaemiaScreenedC) {
        this.anaemiaScreenedC = anaemiaScreenedC;
    }

    public void setAnaemiaPositiveC(String anaemiaPositiveC) {
        this.anaemiaPositiveC = anaemiaPositiveC;
    }

    public void setIPTP1C(String IPTP1C) {
        this.IPTP1C = IPTP1C;
    }

    public void setIPTP2C(String IPTP2C) {
        this.IPTP2C = IPTP2C;
    }

    public void setIPTP4C(String IPTP4C) {
        this.IPTP4C = IPTP4C;
    }

    public void setIPTP3C(String IPTP3C) {
        this.IPTP3C = IPTP3C;
    }

    public void setProvidedITNC(String ProvidedITNC) {
        this.ProvidedITNC = ProvidedITNC;
    }

    public void setProvidedIronC(String providedIronC) {
        this.providedIronC = providedIronC;
    }

    public void setDewormedC(String dewormedC) {
        this.dewormedC = dewormedC;
    }

    public void setStartedOnPrepC(String startedOnPrepC) {
        this.startedOnPrepC = startedOnPrepC;
    }

    public void setAlreadyOnPrepC(String alreadyOnPrepC) {
        this.alreadyOnPrepC = alreadyOnPrepC;
    }

    public void setStartedARTC(String startedARTC) {
        this.startedARTC = startedARTC;
    }

    public void setAlreadyOnARTC(String alreadyOnARTC) {
        this.alreadyOnARTC = alreadyOnARTC;
    }

    public void setHepbScreenedC(String hepbScreenedC) {
        this.hepbScreenedC = hepbScreenedC;
    }

    public void setSyphPositiveC(String syphPositiveC) {
        this.syphPositiveC = syphPositiveC;
    }

    public void setSyphScreenedC(String syphScreenedC) {
        this.syphScreenedC = syphScreenedC;
    }
    public String getHepBPositiveC() {
        return hepBPositiveC;
    }

    public String getAnaemiaScreenedC() {
        return anaemiaScreenedC;
    }

    public String getAnaemiaPositiveC() {
        return anaemiaPositiveC;
    }

    public String getIPTP1C() {
        return IPTP1C;
    }

    public String getIPTP2C() {
        return IPTP2C;
    }

    public String getIPTP3C() {
        return IPTP3C;
    }

    public String getIPTP4C() {
        return IPTP4C;
    }

    public String getProvidedITNC() {
        return ProvidedITNC;
    }

    public String getProvidedIronC() {
        return providedIronC;
    }

    public String getDewormedC() {
        return dewormedC;
    }

    public String getStartedOnPrepC() {
        return startedOnPrepC;
    }

    public String getAlreadyOnPrepC() {
        return alreadyOnPrepC;
    }

    public String getStartedARTC() {
        return startedARTC;
    }

    public String getAlreadyOnARTC() {
        return alreadyOnARTC;
    }

    public String getHepbScreenedC() {
        return hepbScreenedC;
    }

    public String getSyphPositiveC() {
        return syphPositiveC;
    }

    public String getSyphScreenedC() {
        return syphScreenedC;
    }

}
