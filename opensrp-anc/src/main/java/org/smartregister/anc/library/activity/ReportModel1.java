package org.smartregister.anc.library.activity;

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

    public String getCountFeedback() {
        return countFeedback;
    }

    public void setCountFeedback(String countFeedback) {
        this.countFeedback = countFeedback;
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

    public void setDrug_type(String drug_type) {
        this.drug_type = drug_type;
        /*if(drug_type.contains("Condoms"))
        {
            this.drug_type = "Number referred for Condoms";
        }
        else if(drug_type.contains("Implant"))
        {
            this.drug_type = "Number referred for Implants";
        }
        else if(drug_type.contains("Injection"))
        {
            this.drug_type = "Number referred for Injections";
        }
        else if(drug_type.contains("IUCD"))
        {
            this.drug_type = "Number referred for IUCD";
        }
        else if(drug_type.contains("Tablet"))
        {
            this.drug_type = "Number referred for Tablets";
        }
        else if(drug_type.contains("Sterilization"))
        {
            this.drug_type = "Number referred for Sterilization";
        }
        else if(drug_type.contains("Other"))
        {
            this.drug_type = "Number referred for Other";
        }*/
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
