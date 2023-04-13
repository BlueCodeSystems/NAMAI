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
}
