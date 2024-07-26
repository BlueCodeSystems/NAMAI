package org.smartregister.anc.library.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClientModel implements Serializable {

    public static final String ENTITY_ID = "entity_id";

    public ClientModel() {

    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }


    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }


    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    @SerializedName("client_phone")
    @Expose
    private String client_phone;

    @SerializedName("last_interacted_with")
    @Expose
    private String last_interacted_with;

    @SerializedName("caseworker_name")
    @Expose
    private String caseworker_name;

    @SerializedName("dated_edited")
    @Expose
    private String dated_edited;

    @SerializedName("landmark")
    @Expose
    private String landmark;

    @SerializedName("age")
    @Expose
    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getClient_screened() {
        return client_screened;
    }

    public void setClient_screened(String client_screened) {
        this.client_screened = client_screened;
    }

    public String getClient_result() {
        return client_result;
    }

    public void setClient_result(String client_result) {
        this.client_result = client_result;
    }

    public String getPhysical_address() {
        return physical_address;
    }

    public void setPhysical_address(String physical_address) {
        this.physical_address = physical_address;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    @SerializedName("client_email")
    @Expose
    private String client_email;

    @SerializedName("client_screened")
    @Expose
    private String client_screened;

    @SerializedName("client_result")
    @Expose
    private String client_result;


    @SerializedName("case_status")
    @Expose
    private String case_status;


    @SerializedName("base_entity_id")
    @Expose
    private String base_entity_id;

    @SerializedName("household_id")
    @Expose
    private String fpnumber;

    @SerializedName("unique_id")
    @Expose
    private String unique_id;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("date_of_birth")
    @Expose
    private String date_of_birth;

    @SerializedName("registration_date")
    @Expose
    private String registration_date;

    @SerializedName("client_type")
    @Expose
    private String client_type;

    @SerializedName("middle_name")
    @Expose
    private String middle_name;

    @SerializedName("estimated_birth_date")
    @Expose
    private String estimated_birth_date;

    @SerializedName("parity")
    @Expose
    private String parity;
    @SerializedName("zone")
    @Expose
    private String zone;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    @SerializedName("live")
    @Expose
    private String live;

    @SerializedName("last_delivery")
    @Expose
    private String last_delivery;

    @SerializedName("initial_fp_methods")
    @Expose
    private String initial_fp_methods;

    @SerializedName("provider_self")
    @Expose
    private String provider_self;





    @SerializedName("facility")
    @Expose
    private String facility;

    @SerializedName("gender")
    @Expose
    private String gender;




    @SerializedName("date_of_visit")
    @Expose
    private String date_of_visit;

    @SerializedName("live")
    @Expose
    private String date_hiv_known;

    @SerializedName("other_clientType")
    @Expose
    private String other_clientType;

    @SerializedName("drug_type")
    @Expose
    private String drug_type;


    @SerializedName("drug_condom")
    @Expose
    private String drug_condom;


    @SerializedName("drug_tablet")
    @Expose
    private String drug_tablet;

    @SerializedName("drug_injection")
    @Expose
    private String drug_injection;

    @SerializedName("other_product")
    @Expose
    private String other_product;

    @SerializedName("other_provider")
    @Expose
    private String other_provider;


    @SerializedName("counselling_fp_method")
    @Expose
    private String counselling_fp_method;


    @SerializedName("drug_implant")
    @Expose
    private String drug_implant;


    public String getDrug_implant() {
        return drug_implant;
    }

    public void setDrug_implant(String drug_implant) {
        this.drug_implant = drug_implant;
    }

    public String getDrug_type() {
        return drug_type;
    }

    public void setDrug_type(String drug_type) {
        this.drug_type = drug_type;
    }

    public String getDrug_condom() {
        return drug_condom;
    }

    public void setDrug_condom(String drug_condom) {
        this.drug_condom = drug_condom;
    }

    public String getDrug_tablet() {
        return drug_tablet;
    }

    public void setDrug_tablet(String drug_tablet) {
        this.drug_tablet = drug_tablet;
    }

    public String getDrug_injection() {
        return drug_injection;
    }

    public void setDrug_injection(String drug_injection) {
        this.drug_injection = drug_injection;
    }

    public String getOther_product() {
        return other_product;
    }

    public void setOther_product(String other_product) {
        this.other_product = other_product;
    }

    public String getOther_provider() {
        return other_provider;
    }

    public void setOther_provider(String other_provider) {
        this.other_provider = other_provider;
    }

    public String getCounselling_fp_method() {
        return counselling_fp_method;
    }

    public void setCounselling_fp_method(String counselling_fp_method) {
        this.counselling_fp_method = counselling_fp_method;
    }

    @SerializedName("province")
    @Expose
    private String province;

    @SerializedName("district")
    @Expose
    private String district;

    @SerializedName("ward")
    @Expose
    private String ward;

    @SerializedName("adolescent_village")
    @Expose
    private String adolescent_village;




    @SerializedName("nrc")
    @Expose
    private String nrc;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @SerializedName("schoolName")
    @Expose
    private String schoolName;

    @SerializedName("date_offered_enrollment")
    @Expose
    private String date_offered_enrollment;

    @SerializedName("child_enrolled_in_early_childhood_development_program")
    @Expose
    private String child_enrolled_in_early_childhood_development_program;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @SerializedName("school")
    @Expose
    private String school;

    @SerializedName("other_school")
    @Expose
    private String other_school;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("address")
    @Expose
    private String address;


    public String getLast_interacted_with() {
        return last_interacted_with;
    }

    public void setLast_interacted_with(String last_interacted_with) {
        this.last_interacted_with = last_interacted_with;
    }

    @SerializedName("nrc")
    @Expose
    private String physical_address;


    public static String getEntityId() {
        return ENTITY_ID;
    }


    public String getOther_school() {
        return other_school;
    }

    public void setOther_school(String other_school) {
        this.other_school = other_school;
    }




    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getEstimated_birth_date() {
        return estimated_birth_date;
    }

    public void setEstimated_birth_date(String estimated_birth_date) {
        this.estimated_birth_date = estimated_birth_date;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getLast_delivery() {
        return last_delivery;
    }

    public void setLast_delivery(String last_delivery) {
        this.last_delivery = last_delivery;
    }

    public String getInitial_fp_methods() {
        return initial_fp_methods;
    }

    public void setInitial_fp_methods(String initial_fp_methods) {
        this.initial_fp_methods = initial_fp_methods;
    }

    public String getProvider_self() {
        return provider_self;
    }

    public void setProvider_self(String provider_self) {
        this.provider_self = provider_self;
    }




    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }


    public String getCase_status() {
        return case_status;
    }

    public void setCase_status(String case_status) {
        this.case_status = case_status;
    }



    public String getDate_offered_enrollment() {
        return date_offered_enrollment;
    }

    public void setDate_offered_enrollment(String date_offered_enrollment) {
        this.date_offered_enrollment = date_offered_enrollment;
    }



    public String getDate_of_visit() {
        return date_of_visit;
    }

    public void setDate_of_visit(String date_of_visit) {
        this.date_of_visit = date_of_visit;
    }

    public String getDate_hiv_known() {
        return date_hiv_known;
    }

    public void setDate_hiv_known(String date_hiv_known) {
        this.date_hiv_known = date_hiv_known;
    }

    public String getOther_clientType() {
        return other_clientType;
    }

    public void setOther_clientType(String other_clientType) {
        this.other_clientType = other_clientType;
    }

    public String getBase_entity_id() {
        return base_entity_id;
    }

    public void setBase_entity_id(String base_entity_id) {
        this.base_entity_id = base_entity_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAdolescent_village() {
        return adolescent_village;
    }

    public void setAdolescent_village(String adolescent_village) {
        this.adolescent_village = adolescent_village;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getChild_enrolled_in_early_childhood_development_program() {
        return child_enrolled_in_early_childhood_development_program;
    }

    public void setChild_enrolled_in_early_childhood_development_program(String child_enrolled_in_early_childhood_development_program) {
        this.child_enrolled_in_early_childhood_development_program = child_enrolled_in_early_childhood_development_program;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBaseEntity_id() {
        return base_entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.base_entity_id = entity_id;
    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getFpnumber() {
        return fpnumber;
    }

    public void setFpnumber(String fpnumber) {
        this.fpnumber = fpnumber;
    }

    public String getCaseworker_name() {
        return caseworker_name;
    }

    public void setCaseworker_name(String caseworker_name) {
        this.caseworker_name = caseworker_name;
    }

    public String getDated_edited() {
        return dated_edited;
    }

    public void setDated_edited(String dated_edited) {
        this.dated_edited = dated_edited;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    private String last_fp_method;


    public String getLast_fp_method() {
        return last_fp_method;
    }

    public void setLast_fp_method(String last_fp_method) {
        this.last_fp_method = last_fp_method;
    }
}
