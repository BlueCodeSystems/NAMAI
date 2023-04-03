package org.smartregister.anc.library.activity;

import java.io.Serializable;

public class ClientreportModel implements Serializable {

    private String product_name;
    private String date_of_birth;
    private String gender;
    private String fpnumber;
    private String drug_type;
    private String last_fp_method;


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDrug_type() {
        return drug_type;
    }

    public void setDrug_type(String drug_type) {
        this.drug_type = drug_type;
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

    public String getLast_fp_method() {
        return last_fp_method;
    }

    public void setLast_fp_method(String last_fp_method) {
        this.last_fp_method = last_fp_method;
    }
}
