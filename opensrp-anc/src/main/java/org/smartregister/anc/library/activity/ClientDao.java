package org.smartregister.anc.library.activity;

import org.smartregister.dao.AbstractDao;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientDao extends AbstractDao {


    public static String countChildren(String householdID){

        String sql = "SELECT COUNT(*) AS childrenCount FROM ec_client_index WHERE household_id = '" + householdID + "'";

        DataMap<String> dataMap = c -> getCursorValue(c, "childrenCount");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return "1";

        return values.get(0);

    }



    public static String countTestedChildren(String householdID){

        String sql = "SELECT COUNT(*) AS childrenCount FROM ec_hiv_assessment_below_15 WHERE household_id = '" + householdID + "' AND hiv_test IS NOT NULL AND hiv_test != 'never_tested'";

        DataMap<String> dataMap = c -> getCursorValue(c, "childrenCount");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return "0";

        return values.get(0);

    }

    public static List<VCAServiceModel> getServicesByVCAID(String vcaid) {

        String sql = "SELECT * FROM ec_vca_service_report WHERE unique_id = '" + vcaid + "'";

        List<VCAServiceModel> values = AbstractDao.readData(sql, getServiceModelMap());
        if (values == null || values.size() == 0)
            return new ArrayList<>();

        return values;

    }

    public static DataMap<VCAServiceModel> getServiceModelMap() {
        return c -> {

            VCAServiceModel record = new VCAServiceModel();
            record.setBase_entity_id(getCursorValue(c, "base_entity_id"));
            record.setUnique_id(getCursorValue(c, "unique_id"));
            record.setDate(getCursorValue(c, "date"));
            record.setArt_clinic(getCursorValue(c, "art_clinic"));
            record.setDate_last_vl(getCursorValue(c, "date_last_vl"));
            record.setVl_last_result(getCursorValue(c, "vl_last_result"));
            record.setDate_next_vl(getCursorValue(c, "date_next_vl"));
            record.setChild_mmd(getCursorValue(c, "child_mmd"));
            record.setLevel_mmd(getCursorValue(c, "level_mmd"));
            record.setServices(getCursorValue(c, "services"));
            record.setOther_service(getCursorValue(c, "other_service"));

            return record;
        };
    }

    public static String countPositiveChildren(String householdID){

        String sql = "SELECT COUNT(*) AS childrenCount FROM ec_client_index WHERE household_id = '" + householdID + "' AND is_hiv_positive IS NOT NULL AND is_hiv_positive = 'yes'";

        DataMap<String> dataMap = c -> getCursorValue(c, "childrenCount");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return "0";

        return values.get(0);

    }
    public static String countSuppressedChildren(String householdID){

        String sql = "SELECT COUNT(*) AS childrenCount FROM ec_client_index WHERE household_id = '" + householdID + "' AND CAST(vl_last_result as integer) < 1000";

        DataMap<String> dataMap = c -> getCursorValue(c, "childrenCount");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return "0";

        return values.get(0);

    }


    public static String countTestedAbove15Children(String householdID){

        String sql = "SELECT COUNT(*) AS childrenCount FROM ec_hiv_assessment_above_15 WHERE household_id = '" + householdID + "' AND hiv_test IS NOT NULL AND hiv_test != 'never_tested'";

        DataMap<String> dataMap = c -> getCursorValue(c, "childrenCount");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return "0";

        return values.get(0);

    }

    public static String countFemales (String householdID){


        String sql = "SELECT COUNT(*) AS females FROM ec_client_index WHERE gender = 'female' AND household_id = '" + householdID + "'";

        DataMap<String> dataMap = c -> getCursorValue(c, "females");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return "0";

        return values.get(0);

    }

    public static String countMales (String householdID){


        String sql = "SELECT COUNT(*) AS males FROM ec_client_index WHERE gender = 'male' AND household_id = '" + householdID + "'";

        DataMap<String> dataMap = c -> getCursorValue(c, "males");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return "0";

        return values.get(0);

    }
    public static List<String> getMalesBirthdates (String householdID){


        String sql = "SELECT adolescent_birthdate FROM ec_client_index WHERE gender = 'male' AND household_id = '" + householdID + "'";

        DataMap<String> dataMap = c -> getCursorValue(c, "adolescent_birthdate");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return null;
        else{
            return values;
        }


    }




    public static List<String> getGenders(String household_id){

        String sql = "SELECT gender FROM ec_client_index WHERE household_id = '" + household_id + "' AND gender IS NOT NULL";

        DataMap<String> dataMap = c -> getCursorValue(c, "gender");

        List<String> values = AbstractDao.readData(sql, dataMap);

        return values;
    }

    public static List<String> getAges(String household_id){

        String sql = "SELECT adolescent_birthdate FROM ec_client_index WHERE household_id = '" + household_id + "' AND adolescent_birthdate IS NOT NULL";

        DataMap<String> dataMap = c -> getCursorValue(c, "adolescent_birthdate");

        List<String> values = AbstractDao.readData(sql, dataMap);

        return values;
    }

    public static List<ClientModel> getFamilyChildren(String householdID) {

        String sql = "SELECT * FROM ec_client_index WHERE household_id = '"+ householdID +"' AND is_closed = '0'";

        List<ClientModel> values = AbstractDao.readData(sql, getClientDataMap());// Remember to edit getChildDataMap METHOD Below
        if (values == null || values.size() == 0)
            return new ArrayList<>();

        return values;

    }

    public static List<ClientModel> getAllClientsWithSchedules(int page) {

        int limit = 20;
        int offset = (page - 1) * limit;

        String sql = "SELECT ec_client.*, ec_visits.fpnumber AS fpn FROM ec_client JOIN ec_visits ON ec_client.fpnumber = ec_visits.fpnumber WHERE  ec_visits.product_name != 'Condoms Latex Male' AND ec_visits.product_name != 'Condoms Latex Female' \n" +
                "AND ec_visits.number_of_months IS NOT NULL GROUP BY ec_visits.fpnumber ORDER BY ec_visits.date DESC LIMIT " + limit + " OFFSET " + offset;

        List<ClientModel> values = AbstractDao.readData(sql, getClientDataMap());// Remember to edit getChildDataMap METHOD Below

        return values;

    }
    public static List<ClientModel> getAllClients(int page) {

        int limit = 20;
        int offset = (page - 1) * limit;

        String sql = "SELECT * FROM ec_client WHERE fpnumber != '' OR fpnumber IS NOT NULL ORDER BY date_of_visit DESC LIMIT " + limit + " OFFSET " + offset;

        List<ClientModel> values = AbstractDao.readData(sql, getClientDataMap());// Remember to edit getChildDataMap METHOD Below

        return values;

    }

    public static List<ReportModel1> getReport1() {



       /* String sql = "SELECT ec_visits.product_name, ec_visits.id AS product_id, ec_client.date_of_birth, ec_client.gender, ec_visits.fpnumber AS fpnumber \n" +
                "FROM ec_visits JOIN ec_client ON ec_visits.fpnumber = ec_client.fpnumber \n" +
                "WHERE ec_visits.fpnumber IS NOT NULL AND ec_visits.product_name IS NOT NULL" ;*/
        String sql = "SELECT ec_visits.product_name FROM ec_visits\n" +
                "WHERE ec_visits.fpnumber IS NOT NULL AND ec_visits.product_name IS NOT NULL GROUP BY ec_visits.product_name";

        List<ReportModel1> values = AbstractDao.readData(sql, getReportDataMap());

        return values;

    }

    public static List<ReportModel1> getReport3() {



       /* String sql = "SELECT ec_visits.product_name, ec_visits.id AS product_id, ec_client.date_of_birth, ec_client.gender, ec_visits.fpnumber AS fpnumber \n" +
                "FROM ec_visits JOIN ec_client ON ec_visits.fpnumber = ec_client.fpnumber \n" +
                "WHERE ec_visits.fpnumber IS NOT NULL AND ec_visits.product_name IS NOT NULL" ;*/
        String sql = "SELECT ec_client.base_entity_id, ec_client.dob, ec_client.last_interacted_with FROM ec_client \n"+
                "WHERE ec_client.base_entity_id IS NOT NULL AND ec_client.dob IS NOT NULL GROUP BY ec_client.dob "; //+
                //"WHERE ec_mother_details.base_entity_id IS NOT NULL AND ec_mother_details.dob IS NOT NULL GROUP BY ec_mother_details.dob";

        List<ReportModel1> values = AbstractDao.readData(sql, getReferralDataMap());

        return values;

    }

    public static List<ReportModel1> getFeedbackCount() {

        String sql = "SELECT ec_referral.feedback FROM ec_referral WHERE ec_referral.feedback IS NOT NULL";

        List<ReportModel1> values = AbstractDao.readData(sql, getFeedbackDataMap());

        return values;
    }

/*
    SELECT (SELECT COUNT(ec_referral.last_fp_method) AS 'Number referred for injectables' FROM ec_referral
    WHERE ec_referral.last_fp_method = 'Medroxyprogesterone Acetate 104Mg/0.65Ml' OR ec_referral.last_fp_method = 'Medroxyprogesterone Acetate 150mg'
    OR ec_referral.last_fp_method = 'Norethisterone Enanthate 200mg/ml') AS 'Number referred for injectables',  (SELECT COUNT(*) AS 'Number of feedbacks' FROM ec_referral WHERE feedback IS NOT NULL) AS 'Number of feedbacks'
    FROM ec_referral  WHERE ec_referral.last_fp_method IS NOT NULL GROUP BY 'Number referred for injectables'
*/


    public static List<ClientreportModel> getVisitedClientsMale(String product_name) {

        String sql = "SELECT ec_visits.product_name, ec_visits.id AS product_id, ec_client.date_of_birth, ec_client.gender, ec_visits.fpnumber AS fpnumber \n" +
                "FROM ec_visits JOIN ec_client ON ec_visits.fpnumber = ec_client.fpnumber \n" +
                "WHERE ec_visits.fpnumber IS NOT NULL AND ec_visits.product_name IS NOT NULL AND ec_client.gender = 'male' AND product_name =  " + "'" + product_name + "'" + " GROUP BY ec_visits.fpnumber" ;

        List<ClientreportModel> values = AbstractDao.readData(sql, getclientReportDataMap());

        return values;

    }

    public static List<ClientreportModel> getRefVisitedClientsMale(String drug_type) {

        String sql = "SELECT ec_referral.drug_type, ec_referral.date_of_birth, ec_referral.gender, ec_referral.client_id AS client_id, ec_referral.last_fp_method \n" +
                "FROM ec_referral \n" +
                "WHERE ec_referral.drug_type IS NOT NULL AND ec_referral.gender = 'male' AND drug_type =  " + "'" + drug_type + "'" + " GROUP BY ec_referral.drug_type" ;

        List<ClientreportModel> values = AbstractDao.readData(sql, getRefclientReportDataMap());

        return values;

    }

    public static List<ClientreportModel> getVisitedClientsFemale(String product_name) {

        String sql = "SELECT ec_visits.product_name, ec_client.date_of_birth, ec_client.gender, ec_visits.fpnumber AS fpnumber \n" +
                "FROM ec_visits JOIN ec_client ON ec_visits.fpnumber = ec_client.fpnumber \n" +
                "WHERE ec_visits.fpnumber IS NOT NULL AND ec_visits.product_name IS NOT NULL AND ec_client.gender = 'female' AND product_name =  " + "'" + product_name + "'" + " GROUP BY ec_visits.fpnumber" ;

        List<ClientreportModel> values = AbstractDao.readData(sql, getclientReportDataMap());

        return values;

    }

    public static List<ClientreportModel> getRefVisitedClientsFemale(String drug_type) {

        String sql = "SELECT ec_referral.drug_type, ec_referral.id AS product_id, ec_referral.date_of_birth, ec_referral.gender, ec_referral.fpnumber AS fpnumber, ec_referral.last_fp_method \n" +
                "FROM ec_referral \n" +
                "WHERE ec_referral.drug_type IS NOT NULL AND ec_referral.gender = 'female' AND drug_type =  " + "'" + drug_type + "'" + " GROUP BY ec_referral.drug_type" ;

        List<ClientreportModel> values = AbstractDao.readData(sql, getRefclientReportDataMap());

        return values;

    }


    public static String getVisitedClientsTotal(String product_name, String gender, int minAge, int maxAge) {

        String sql = "SELECT ec_client.date_of_birth \n" +
                "FROM ec_visits JOIN ec_client ON ec_visits.fpnumber = ec_client.fpnumber \n" +
                "WHERE ec_visits.fpnumber IS NOT NULL AND ec_visits.product_name IS NOT NULL AND ec_client.gender = " + "'" + gender + "'" + " AND product_name =  " + "'" + product_name + "'" + " GROUP BY ec_visits.fpnumber" ;

        AbstractDao.DataMap<String> dataMap = c -> getCursorValue(c, "date_of_birth");
        List<String> values = AbstractDao.readData(sql, dataMap);

        int total = countAgesBetween(values, minAge, maxAge);

        if(total > 0){
            return String.valueOf(total);
        } else {
            return "0";
        }

    }

    public static String getRefVisitedClientsTotal(String drug_type, String gender, int minAge, int maxAge) {

        String sql = "SELECT ec_referral.date_of_birth FROM ec_referral\n" +
                "WHERE ec_referral.gender = " + "'" + gender + "'" + " AND ec_referral.drug_type =  " + "'" + drug_type + "'" + " GROUP BY ec_referral.drug_type";

        AbstractDao.DataMap<String> dataMap = c -> getCursorValue(c, "date_of_birth");
        List<String> values = AbstractDao.readData(sql, dataMap);

        int total = countAgesBetween(values, minAge, maxAge);

        if(total > 0){
            return String.valueOf(total);
        } else {
            return "0";
        }

    }

    public static int countAgesBetween(List<String> values, int minAge, int maxAge) {

        int x = 0;

        /*for (int i = x; i < values.size(); i++) {


            *//*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-u");
            LocalDate dateOfBirth = LocalDate.parse(values.get(i), formatter);
            LocalDate currentDate = LocalDate.now();
            Period age = Period.between(dateOfBirth, currentDate);*//*

            if(age.getYears() >= minAge && age.getYears() < maxAge){
                x += 1;
            }

        }*/

        return x;
    }



    public static DataMap<ReportModel1> getReportDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setProduct_name(getCursorValue(c, "product_name"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getReferralDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setDrug_type(getCursorValue(c, "dob"));
            record.setFeedback(getCursorValue(c, "base_entity_id"));
            record.setQuerry_drug(getCursorValue(c, "dob"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getFeedbackDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setCountFeedback(getCursorValue(c, "feedback"));

            return record;
        };
    }

    public static DataMap<ClientreportModel> getclientReportDataMap() {
        return c -> {
            ClientreportModel record = new ClientreportModel();
            record.setProduct_name(getCursorValue(c, "product_name"));
            record.setDate_of_birth(getCursorValue(c, "date_of_birth"));
            record.setGender(getCursorValue(c, "gender"));
            record.setFpnumber(getCursorValue(c, "fpnumber"));

            return record;
        };
    }

    public static DataMap<ClientreportModel> getRefclientReportDataMap() {
        return c -> {
            ClientreportModel record = new ClientreportModel();
            record.setDrug_type(getCursorValue(c, "drug_type"));
            record.setDate_of_birth(getCursorValue(c, "date_of_birth"));
            record.setGender(getCursorValue(c, "gender"));
            record.setFpnumber(getCursorValue(c, "client_id"));
            record.setLast_fp_method(getCursorValue(c, "last_fp_method"));

            return record;
        };
    }

    public static List<ClientModel> searchClients(String searchItem) {

        String sql = "SELECT * FROM ec_client WHERE fpnumber LIKE " + "'%" + searchItem + "%'" + " OR first_name LIKE " + "'%" + searchItem + "%' " +
                "OR last_name LIKE " + "'%" + searchItem + "%'" + " ORDER BY date_of_visit DESC";

        List<ClientModel> values = AbstractDao.readData(sql, getClientDataMap());// Remember to edit getChildDataMap METHOD Below

        if(values != null && values.size() > 0 ){
            return values;
        } else {
            List<ClientModel> emptyList = new ArrayList<ClientModel>();
            return emptyList;

        }

    }

    public static String countClients () {

        String sql = "SELECT COUNT(*) AS clients FROM ec_client WHERE fpnumber != '' || fpnumber IS NOT NULL";

        AbstractDao.DataMap<String> dataMap = c -> getCursorValue(c, "clients");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if(values != null && values.size() > 0 ){
            return values.get(0);
        } else {
            return "0";
        }

    }


    public static ClientModel getClientByID(String clientID) {

        String sql = "SELECT * FROM ec_client WHERE id =  '" + clientID + "'";

        List<ClientModel> values = AbstractDao.readData(sql, getClientDataMap());
        if (values == null || values.size() == 0)
        {
            return null;
        };

        return values.get(0);

    }

    public static ClientModel getClientByFpNumber(String clientID) {

        String sql = "SELECT * FROM ec_client WHERE fpnumber =  '" + clientID + "'";

        List<ClientModel> values = AbstractDao.readData(sql, getClientDataMap());
        if (values == null || values.size() == 0)
        {
            return null;
        };

        return values.get(0);

    }


    public static DataMap<ClientModel> getClientDataMap() {
        return c -> {
            ClientModel record = new ClientModel();
            record.setFpnumber(getCursorValue(c, "fpnumber"));
            record.setUnique_id(getCursorValue(c, "unique_id"));
            record.setEntity_id(getCursorValue(c, "base_entity_id"));
            record.setFirst_name(getCursorValue(c, "first_name"));
            record.setMiddle_name(getCursorValue(c, "middle_name"));
            record.setClient_type(getCursorValue(c, "client_type"));
            record.setLast_name(getCursorValue(c, "last_name"));
            record.setDate_of_birth(getCursorValue(c, "date_of_birth"));
            record.setEstimated_birth_date(getCursorValue(c, "estimated_birth_date"));
            record.setAddress(getCursorValue(c, "address"));
            record.setGender(getCursorValue(c, "gender"));
            record.setParity(getCursorValue(c, "parity"));
            record.setLive(getCursorValue(c, "live"));
            record.setInitial_fp_methods(getCursorValue(c, "initial_fp_methods"));
            record.setFacility(getCursorValue(c, "facility"));
            record.setLast_delivery(getCursorValue(c, "last_delivery"));
            record.setProvider_self(getCursorValue(c, "provider_self"));
            record.setOther_clientType(getCursorValue(c,"other_clientType"));
            record.setDate_of_visit(getCursorValue(c,"date_of_visit"));
            record.setRegistration_date(getCursorValue(c,"registration_date"));
            record.setNrc(getCursorValue(c,"nrc"));
            record.setDrug_condom(getCursorValue(c,"drug_condom"));
            record.setDrug_tablet(getCursorValue(c,"drug_tablet"));
            record.setDrug_type(getCursorValue(c,"drug_type"));
            record.setDrug_injection(getCursorValue(c,"drug_injection"));
            record.setOther_provider(getCursorValue(c,"other_provider"));
            record.setDrug_condom(getCursorValue(c,"drug_condom"));
            record.setDrug_implant(getCursorValue(c,"drug_implant"));
            record.setOther_product(getCursorValue(c,"other_product"));
            record.setClient_phone(getCursorValue(c,"client_phone"));
            record.setLast_fp_method(getCursorValue(c,"last_fp_method"));
            record.setCounselling_fp_method(getCursorValue(c,"counselling_fp_method"));
            record.setZone(getCursorValue(c,"zone"));
            record.setFacility(getCursorValue(c,"facility"));

            return record;
        };
    }



    public static List<String> getAllChildrenBirthdate(String householdID) {

        String sql = "SELECT adolescent_birthdate FROM ec_client_index WHERE household_id = '" + householdID + "' AND case_status = '1'";

        DataMap<String> dataMap = c -> getCursorValue(c, "adolescent_birthdate");

        List<String> values = AbstractDao.readData(sql, dataMap);

        if (values == null || values.size() == 0)
            return null;
        else{
            return values;
        }

    }
}
