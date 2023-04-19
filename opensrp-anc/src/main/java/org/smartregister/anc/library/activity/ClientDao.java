package org.smartregister.anc.library.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.anc.library.model.AttentionFlagModel;
import org.smartregister.dao.AbstractDao;

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
    public static String getFirstContact(String key,String lowerAge,String upperAge) {

        //String sql = "SELECT * FROM ec_client_index WHERE household_id = '"+ householdID +"' AND is_closed = '0'";
        String query ="SELECT B.value ,C.value As age FROM (SELECT * FROM ec_details WHERE key IN ('next_contact')) AS A JOIN (SELECT * FROM ec_details WHERE key IN ('attention_flag_facts')) AS B ON A.base_entity_id = B.base_entity_id JOIN (SELECT * FROM ec_details WHERE Key IN('age_calculated')) AS C ON B.base_entity_id = C.base_entity_id WHERE A.value = '2'";
        int count = 0;
        List<AttentionFlagModel> values = AbstractDao.readData(query, getAttentionFlagDataMap());// Remember to edit getChildDataMap METHOD Below
        if (values == null || values.size() == 0) {
            return String.valueOf(count);
        } else{

            for (int i = 0; i < values.size(); i++) {
                String jsonString = values.get(i).getValues();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if( values.get(i).getAge() != null && Double.parseDouble(values.get(i).getAge()) < 15)
                if (jsonObject.has(key) ) {
                    int gestationAge = 0;
                    try {
                        gestationAge = jsonObject.getInt(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (gestationAge > Integer.parseInt(lowerAge) && gestationAge < Integer.parseInt(upperAge)) {
                        count++;
                    }
                }
            }

            System.out.println("Total number of people between the age of 0 and 15: " + count);
        }


        return String.valueOf(count);

    }

    public static String getFirstContactAbove15(String key,String lowerAge,String upperAge) {

        //String sql = "SELECT * FROM ec_client_index WHERE household_id = '"+ householdID +"' AND is_closed = '0'";
        String query ="SELECT B.value ,C.value As age FROM (SELECT * FROM ec_details WHERE key IN ('next_contact')) AS A JOIN (SELECT * FROM ec_details WHERE key IN ('attention_flag_facts')) AS B ON A.base_entity_id = B.base_entity_id JOIN (SELECT * FROM ec_details WHERE Key IN('age_calculated')) AS C ON B.base_entity_id = C.base_entity_id WHERE A.value = '2'";
        int count = 0;
        List<AttentionFlagModel> values = AbstractDao.readData(query, getAttentionFlagDataMap());// Remember to edit getChildDataMap METHOD Below
        if (values == null || values.size() == 0) {
            return String.valueOf(count);
        } else{

            for (int i = 0; i < values.size(); i++) {
                String jsonString = values.get(i).getValues();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if( values.get(i).getAge() != null && Double.parseDouble(values.get(i).getAge()) > 15 && Double.parseDouble(values.get(i).getAge()) < 20 )
                    if (jsonObject.has(key) ) {
                        int gestationAge = 0;
                        try {
                            gestationAge = jsonObject.getInt(key);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (gestationAge > Integer.parseInt(lowerAge) && gestationAge < Integer.parseInt(upperAge)) {
                            count++;
                        }
                    }
            }

            System.out.println("Total number of people between the age of 15 and 20: " + count);
        }


        return String.valueOf(count);

    }

    public static String getFirstContactAbove20(String key,String lowerAge,String upperAge) {

        //String sql = "SELECT * FROM ec_client_index WHERE household_id = '"+ householdID +"' AND is_closed = '0'";
        String query ="SELECT B.value ,C.value As age FROM (SELECT * FROM ec_details WHERE key IN ('next_contact')) AS A JOIN (SELECT * FROM ec_details WHERE key IN ('attention_flag_facts')) AS B ON A.base_entity_id = B.base_entity_id JOIN (SELECT * FROM ec_details WHERE Key IN('age_calculated')) AS C ON B.base_entity_id = C.base_entity_id WHERE A.value = '2'";
        int count = 0;
        List<AttentionFlagModel> values = AbstractDao.readData(query, getAttentionFlagDataMap());// Remember to edit getChildDataMap METHOD Below
        if (values == null || values.size() == 0) {
            return String.valueOf(count);
        } else{

            for (int i = 0; i < values.size(); i++) {
                String jsonString = values.get(i).getValues();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if( values.get(i).getAge() != null && Double.parseDouble(values.get(i).getAge()) > 20 && Double.parseDouble(values.get(i).getAge()) < 25 )
                    if (jsonObject.has(key) ) {
                        int gestationAge = 0;
                        try {
                            gestationAge = jsonObject.getInt(key);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (gestationAge > Integer.parseInt(lowerAge) && gestationAge < Integer.parseInt(upperAge)) {
                            count++;
                        }
                    }
            }

            System.out.println("Total number of people between the age of 20 and 25: " + count);
        }


        return String.valueOf(count);

    }

    public static String getFirstContactAbove25(String key,String lowerAge,String upperAge) {

        //String sql = "SELECT * FROM ec_client_index WHERE household_id = '"+ householdID +"' AND is_closed = '0'";
        String query ="SELECT B.value ,C.value As age FROM (SELECT * FROM ec_details WHERE key IN ('next_contact')) AS A JOIN (SELECT * FROM ec_details WHERE key IN ('attention_flag_facts')) AS B ON A.base_entity_id = B.base_entity_id JOIN (SELECT * FROM ec_details WHERE Key IN('age_calculated')) AS C ON B.base_entity_id = C.base_entity_id WHERE A.value = '2' GROUP BY 'number of women above the age of 25'";
        int count = 0;
        List<AttentionFlagModel> values = AbstractDao.readData(query, getAttentionFlagDataMap());// Remember to edit getChildDataMap METHOD Below
        if (values == null || values.size() == 0) {
            return String.valueOf(count);
        } else{

            for (int i = 0; i < values.size(); i++) {
                String jsonString = values.get(i).getValues();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if( values.get(i).getAge() != null && Double.parseDouble(values.get(i).getAge()) > 25)
                    if (jsonObject.has(key) ) {
                        int gestationAge = 0;
                        try {
                            gestationAge = jsonObject.getInt(key);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (gestationAge > Integer.parseInt(lowerAge) && gestationAge < Integer.parseInt(upperAge)) {
                            count++;
                        }
                    }
            }

            System.out.println("Total number of people above the age of 25: " + count);
        }


        return String.valueOf(count);

    }

    public static int getAllOutside(){
        String sql = "SELECT ec_mother_details.origin FROM ec_mother_details WHERE ec_mother_details.origin IS NOT 'catchment_area' AND ec_mother_details.origin IS NOT NULL";

        List<ReportModel1> values = AbstractDao.readData(sql, getOriginCountDataMap());

        return values.size();
    }

    public static int getAllHighRiskContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.key IS 'red_flag_count' and ec_details.value IS NOT '0' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllFirstContact(){
        String sql = "SELECT previous_contact.contact_no, previous_contact.base_entity_id FROM previous_contact where previous_contact.contact_no IS '1' GROUP BY previous_contact.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getFirstContactCountDataMap());

        return values.size();
    }

    public static int getAllSecondContact(){
        String sql = "SELECT previous_contact.contact_no, previous_contact.base_entity_id FROM previous_contact where previous_contact.contact_no IS '2' GROUP BY previous_contact.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getSecondContactCountDataMap());

        return values.size();
    }

    public static int getAllThirdContact(){
        String sql = "SELECT previous_contact.contact_no, previous_contact.base_entity_id FROM previous_contact where previous_contact.contact_no IS '3' GROUP BY previous_contact.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getThirdContactCountDataMap());

        return values.size();
    }

    public static int getAllFourthToSeventhContact(){
        String sql = "SELECT previous_contact.contact_no, previous_contact.base_entity_id FROM previous_contact where previous_contact.contact_no IS '4' OR previous_contact.contact_no IS '5' OR previous_contact.contact_no IS '6' OR previous_contact.contact_no IS '7' GROUP BY previous_contact.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getFourthToSeventhContactCountDataMap());

        return values.size();
    }

    public static int getAllEighthAboveContact(){
        String sql = "SELECT previous_contact.contact_no, previous_contact.base_entity_id FROM previous_contact where previous_contact.contact_no IS '8' OR previous_contact.contact_no IS '9' OR previous_contact.contact_no IS '10' OR previous_contact.contact_no IS '11' GROUP BY previous_contact.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getEighthAboveContactCountDataMap());

        return values.size();
    }

    public static int getAllSyphScreenedContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%syph_test_status\":\"done_%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllSyphPositiveContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%syphilis_positive\":\"1%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getSyphScreenedContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%syph_test_status\":\"done_%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getSyphScreenedCountDataMap());

        return values;
    }

    public static List<ReportModel1> getSyphPositiveContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%syphilis_positive\":\"1%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getSyphPositiveCountDataMap());

        return values;
    }

    public static int getAllHepBScreenedContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%hepb_test_status\":\"done%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getHepBScreenedContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%hepb_test_status\":\"done%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getHepbScreenedCountDataMap());

        return values;
    }

    public static List<ReportModel1> getAnaemiaPositiveContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%anaemic\":\"1%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getAnaemiaPositiveCountDataMap());

        return values;
    }

    public static int getAllAnaemiaPositiveContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%anaemic\":\"1%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getAnaemiaScreenedContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%ifa_anaemia\":\"done%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getAnaemiaScreenedCountDataMap());

        return values;
    }

    public static int getAllAnaemiaScreenedContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%ifa_anaemia\":\"done%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllHepBPositiveContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%hepb_positive\":\"1%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getHepBPositiveContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%hepb_positive\":\"1%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getHepBPositiveCountDataMap());

        return values;
    }

    public static int getAllIPTP1Contact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%iptp_sp1_dose_number\":\"1%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getIPTP1Contact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%iptp_sp1_dose_number\":\"1%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getIPTP1CountDataMap());

        return values;
    }

    public static int getAllIPTP2Contact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%iptp_sp2_dose_number\":\"1%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getIPTP2Contact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%iptp_sp2_dose_number\":\"1%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getIPTP2CountDataMap());

        return values;
    }

    public static int getAllIPTP3Contact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%iptp_sp3_dose_number\":\"1%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getIPTP3Contact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%iptp_sp3_dose_number\":\"1%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getIPTP3CountDataMap());

        return values;
    }

    public static int getAllIPTP4Contact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%iptp_sp4_dose_number\":\"1%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getIPTP4Contact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%iptp_sp4_dose_number\":\"0%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getIPTP4CountDataMap());

        return values;
    }

    public static int getAllProvidedITNContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%itn_issued\":\"yes%' GROUP BY ec_client.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getProvidedITNContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%itn_issued\":\"yes%' GROUP BY ec_client.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getProvidedITNCountDataMap());

        return values;
    }

    public static List<ReportModel1> getProvidedIronContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%ifa_low_prev_value\":\"Given%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getProvidedIronCountDataMap());

        return values;
    }

    public static int getAllProvidedIronContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%ifa_low_prev_value\":\"Given%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllDewormedContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%mebendazole\":\"yes%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getDewormedContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%mebendazole\":\"yes%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getDewormedCountDataMap());

        return values;
    }

    public static List<ReportModel1> getStartedOnPrepContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%started_on_prep\":\"yes%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getStartedOnPrepCountDataMap());

        return values;
    }

    public static int getAllStartedOnPrepContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%started_on_prep\":\"yes%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllAlreadyOnPrepContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%started_on_prep\":\"already%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getAlreadyOnPrepContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%started_on_prep\":\"already%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getAlreadyOnPrepCountDataMap());

        return values;
    }

    public static List<ReportModel1> getAlreadyARTinANCContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%started_art\":\"already%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getAlreadyOnARTCountDataMap());

        return values;
    }

    public static int getAllAlreadyARTinANCContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%started_art\":\"already%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllStartedARTinANCContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%started_art\":\"yes%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static List<ReportModel1> getStartedARTinANCContact(){
        String sql = "SELECT \"_rowid_\",* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%started_art\":\"yes%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getStartedOnARTCountDataMap());

        return values;
    }



    public static int getAllFollowUpContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.key IS 'next_contact' AND ec_details.value LIKE '%3%' OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%4%' OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%5%'OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%6%' OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%7%' OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%8%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllContactCount(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%contact_date%' GROUP BY ec_details.base_entity_id\n";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllReferredTBContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%reason\":\"refer%' GROUP BY ec_details.base_entity_id\n";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllTTCVPlusTwoContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%tt1_dose_no_value\":\"2%' OR ec_details.value LIKE '%tt1_dose_no_value\":\"3%' OR ec_details.value LIKE '%tt1_dose_no_value\":\"4%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllScreenedForTBContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%tb_screening_history\":\"[cough_weeks%' OR ec_details.value LIKE '%tb_screening_history\":\"[fever%' OR ec_details.value LIKE '%tb_screening_history\":\"[weight%' OR ec_details.value LIKE '%tb_screening_history\":\"[night%'  GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllTestedHIVFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_test_status\":\"done_today%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' GROUP BY ec_details.value";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllAlreadyPositiveFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_positive\":\"1%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' AND ec_details.value LIKE '%art_number\":\"%' GROUP BY ec_details.value";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllTestedPositiveFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_test_status\":\"done_today%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' AND ec_details.value LIKE '%hiv_positive\":\"1%' GROUP BY ec_details.value";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllOnARTContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%on_art\":\"yes%' AND ec_details.value LIKE '%hiv_positive\":\"1%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllViralLoadResultsContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%viral_load\":\"%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllMaleTestFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_test_partner_status\":\"done_today%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' GROUP BY ec_details.value";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllMalePositiveFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_test_partner_status\":\"done_today%' AND ec_details.value LIKE '%partner_hiv_status\":\"positive%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' GROUP BY ec_details.value";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllMaleAlreadyPositiveContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%partner_hiv_status\":\"positive%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' AND ec_details.value LIKE '%partner_on_art\":\"%' GROUP BY ec_details.value";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllMaleStartedARTinANCContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%partner_on_art\":\"no\"%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getAllDiscordantContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%discordant\":\"yes%' GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }





    public static List<ReportModel1> getFollowUpContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.key IS 'next_contact' AND ec_details.value LIKE '%3%' OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%4%' OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%5%'OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%6%' OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%7%' OR ec_details.key IS 'next_contact' AND ec_details.value LIKE '%8%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getFollowUpCountDataMap());

        return values;
    }

    public static List<ReportModel1> getCountContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%contact_date%' GROUP BY ec_details.key\n";

        List<ReportModel1> values = AbstractDao.readData(sql, getContactCountDataMap());

        return values;
    }

    public static List<ReportModel1> getReferredTBContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%reason\":\"refer%' GROUP BY ec_details.key\n";

        List<ReportModel1> values = AbstractDao.readData(sql, getRefferedTBCountDataMap());

        return values;
    }

    public static List<ReportModel1> getTTCVPlusTwoContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%tt1_dose_no_value\":\"2%' OR ec_details.value LIKE '%tt1_dose_no_value\":\"3%' OR ec_details.value LIKE '%tt1_dose_no_value\":\"4%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getTTCVPlusTwoDataMap());

        return values;
    }

    public static List<ReportModel1> getScreenedForTBContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%tb_screening_history\":\"[cough_weeks%' OR ec_details.value LIKE '%tb_screening_history\":\"[fever%' OR ec_details.value LIKE '%tb_screening_history\":\"[weight%' OR ec_details.value LIKE '%tb_screening_history\":\"[night%'  GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getScreenedForTBDataMap());

        return values;
    }

    public static List<ReportModel1> getTestedHIVFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_test_status\":\"done_today%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getTestedHIVFirstDataMap());

        return values;
    }

    public static List<ReportModel1> getAlreadyPositiveFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_positive\":\"1%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' AND ec_details.value LIKE '%art_number\":\"%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getAlreadyPositiveDataMap());

        return values;
    }

    public static List<ReportModel1> getTestedPositiveFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_test_status\":\"done_today%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' AND ec_details.value LIKE '%hiv_positive\":\"1%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getTestedPositiveDataMap());

        return values;
    }

    public static List<ReportModel1> getOnARTContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%on_art\":\"yes%' AND ec_details.value LIKE '%hiv_positive\":\"1%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getOnARTDataMap());

        return values;
    }

    public static List<ReportModel1> getViralLoadResultsContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%viral_load\":\"%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getViralLoadDataMap());

        return values;
    }

    public static List<ReportModel1> getMaleTestFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_test_partner_status\":\"done_today%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getMaleTestedDataMap());

        return values;
    }

    public static List<ReportModel1> getMalePositiveFirstContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%hiv_test_partner_status\":\"done_today%' AND ec_details.value LIKE '%partner_hiv_status\":\"positive%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getMalePositiveDataMap());

        return values;
    }

    public static List<ReportModel1> getMaleAlreadyPositiveContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%partner_hiv_status\":\"positive%' AND ec_details.value LIKE '%contact_reason\":\"first_contact%' AND ec_details.value LIKE '%partner_on_art\":\"%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getMaleAlreadyPositiveDataMap());

        return values;
    }

    public static List<ReportModel1> getMaleStartedARTinANCContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%partner_on_art\":\"no\"%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getMaleStartedARTDataMap());

        return values;
    }

    public static List<ReportModel1> getDiscordantContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.value LIKE '%discordant\":\"yes%' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getDiscordantDataMap());

        return values;
    }




    public static int getAllFirstTrimesterContact(){
        String sql = "SELECT \"_rowid_\" AS 'First Trimester',* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%gest_age_openmrs\":\"8%' OR \"value\" LIKE '%gest_age_openmrs\":\"9%' OR \"value\" LIKE '%gest_age_openmrs\":\"10%' OR \"value\" LIKE '%gest_age_openmrs\":\"11%' OR \"value\" LIKE '%gest_age_openmrs\":\"12%'GROUP BY ec_details.base_entity_id";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
    }

    public static int getFirstTrimesterContact(){
        String sql = "SELECT \"_rowid_\" AS 'First Trimester',* FROM \"main\".\"ec_details\" WHERE \"value\" LIKE '%gest_age_openmrs\":\"8%' OR \"value\" LIKE '%gest_age_openmrs\":\"9%' OR \"value\" LIKE '%gest_age_openmrs\":\"10%' OR \"value\" LIKE '%gest_age_openmrs\":\"11%' OR \"value\" LIKE '%gest_age_openmrs\":\"12%'GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getGeneralCountDataMap());

        return values.size();
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
        String sql ="SELECT B.value ,C.value As age\n" +
                "FROM (SELECT * FROM ec_details WHERE key IN ('next_contact')) AS A \n" +
                "JOIN (SELECT * FROM ec_details WHERE key IN ('attention_flag_facts')) AS B ON A.base_entity_id = B.base_entity_id \n" +
                "JOIN (SELECT * FROM ec_details WHERE Key IN('age_calculated')) AS C ON B.base_entity_id = C.base_entity_id WHERE A.value = '2' GROUP BY B.value";

//SELECT B.value ,C.value As age FROM (SELECT * FROM ec_details WHERE key IN ('next_contact')) AS A JOIN (SELECT * FROM ec_details WHERE key IN ('attention_flag_facts')) AS B ON A.base_entity_id = B.base_entity_id JOIN (SELECT * FROM ec_details WHERE Key IN('age_calculated')) AS C ON B.base_entity_id = C.base_entity_id WHERE A.value = '2' GROUP BY B.value
//SELECT B.value As gestation ,C.value As age FROM (SELECT * FROM ec_details WHERE key IN ('next_contact')) AS A JOIN (SELECT * FROM ec_details WHERE value LIKE '%gest_age_openmrs":"39%') AS B ON A.base_entity_id = B.base_entity_id JOIN (SELECT * FROM ec_details WHERE Key IN('age_calculated')) AS C ON B.base_entity_id = C.base_entity_id WHERE A.value = '2' GROUP BY B.value
                /*"SELECT B.value ,C.value As age " +
                "FROM (SELECT * FROM ec_details WHERE key IN ('next_contact')) AS A " +
                "JOIN (SELECT * FROM ec_details WHERE key IN ('attention_flag_facts')) AS B ON A.base_entity_id = B.base_entity_id " +
                "JOIN (SELECT * FROM ec_details WHERE Key IN('age_calculated')) AS C ON B.base_entity_id = C.base_entity_id WHERE A.value = '2' GROUP BY B.value";*/
                //"WHERE ec_mother_details.base_entity_id IS NOT NULL AND ec_mother_details.dob IS NOT NULL GROUP BY ec_mother_details.dob";

        //SELECT B.value AS gestation, C.value AS age
        //FROM (
        //    SELECT *
        //    FROM ec_details
        //    WHERE key = 'next_contact'
        //) AS A
        //JOIN (
        //    SELECT *
        //    FROM ec_details
        //    WHERE value REGEXP 'gest_age_openmrs":"(2[7-9]|[3-3][0-9]|40)'
        //) AS B ON A.base_entity_id = B.base_entity_id
        //JOIN (
        //    SELECT *
        //    FROM ec_details
        //    WHERE key = 'age_calculated'
        //) AS C ON B.base_entity_id = C.base_entity_id
        //WHERE A.value = '2'
        //GROUP BY B.value

        List<ReportModel1> values = AbstractDao.readData(sql, getReferralDataMap());

        return values;

    }

    public static List<ReportModel1> getOrigin() {
        String sql = "SELECT ec_mother_details.origin FROM ec_mother_details WHERE ec_mother_details.origin IS NOT 'catchment_area' AND ec_mother_details.origin IS NOT NULL GROUP BY ec_mother_details.origin";

        List<ReportModel1> values = AbstractDao.readData(sql, getOriginDataMap());

        return values;
    }

    public static List<ReportModel1> getFirstC() {
        String sql = "SELECT previous_contact.contact_no FROM previous_contact where previous_contact.contact_no IS '1' GROUP BY previous_contact.contact_no";

        List<ReportModel1> values = AbstractDao.readData(sql, getFirstCDataMap());

        return values;
    }

    public static List<ReportModel1> getSecondC() {
        String sql = "SELECT previous_contact.contact_no FROM previous_contact where previous_contact.contact_no IS '2' GROUP BY previous_contact.contact_no";

        List<ReportModel1> values = AbstractDao.readData(sql, getSecondCDataMap());

        return values;
    }

    public static List<ReportModel1> getThirdC() {
        String sql = "SELECT previous_contact.contact_no FROM previous_contact where previous_contact.contact_no IS '3' GROUP BY previous_contact.contact_no";

        List<ReportModel1> values = AbstractDao.readData(sql, getThirdCDataMap());

        return values;
    }

    public static List<ReportModel1> getFourthToSeventhC() {
        String sql = "SELECT previous_contact.contact_no FROM previous_contact where previous_contact.contact_no IS '4' OR previous_contact.contact_no IS '5' OR previous_contact.contact_no IS '6' OR previous_contact.contact_no IS '7' GROUP BY previous_contact.contact_no";

        List<ReportModel1> values = AbstractDao.readData(sql, getFourthToSeventhCDataMap());

        return values;
    }

    public static List<ReportModel1> getEighthAboveC() {
        String sql = "SELECT previous_contact.contact_no FROM previous_contact where previous_contact.contact_no IS '8' OR previous_contact.contact_no IS '9' OR previous_contact.contact_no IS '10' OR previous_contact.contact_no IS '11' GROUP BY previous_contact.contact_no";

        List<ReportModel1> values = AbstractDao.readData(sql, getEighthAboveCDataMap());

        return values;
    }

    public static List<ReportModel1> getHighRiskContact(){
        String sql = "SELECT * FROM ec_details WHERE ec_details.key IS 'red_flag_count' and ec_details.value IS NOT '0' GROUP BY ec_details.key";

        List<ReportModel1> values = AbstractDao.readData(sql, getFirstContactCountDataMap());

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
            record.setTrimester(getCursorValue(c, "value"));
            record.setAge(getCursorValue(c, "age"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getOriginDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setOrigin(getCursorValue(c, "origin"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getFirstCDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setFirstC(getCursorValue(c, "contact_no"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getSecondCDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setSecondC(getCursorValue(c, "contact_no"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getThirdCDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setThirdC(getCursorValue(c, "contact_no"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getFourthToSeventhCDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setFourthToSeventhC(getCursorValue(c, "contact_no"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getEighthAboveCDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setEighthAboveC(getCursorValue(c, "contact_no"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getAlreadyOnARTCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setAlreadyOnARTC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getStartedOnARTCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setStartedARTC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getAlreadyOnPrepCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setAlreadyOnPrepC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getStartedOnPrepCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setStartedOnPrepC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getDewormedCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setDewormedC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getProvidedIronCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setProvidedIronC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getProvidedITNCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setProvidedITNC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getIPTP4CountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setIPTP4C(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getIPTP3CountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setIPTP3C(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getIPTP2CountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setIPTP2C(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getIPTP1CountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setIPTP1C(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getAnaemiaPositiveCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setAnaemiaPositiveC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getAnaemiaScreenedCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setAnaemiaScreenedC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getHepBPositiveCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setHepBPositiveC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getHepbScreenedCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setHepbScreenedC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getSyphPositiveCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setSyphPositiveC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getSyphScreenedCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setSyphScreenedC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getFollowUpCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setFollowUpC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getContactCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setContactCountC(getCursorValue(c, "key"));

            return record;
        };
    }
    public static DataMap<ReportModel1> getRefferedTBCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setReferredTBC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getTTCVPlusTwoDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setTTCVPlusTwoC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getScreenedForTBDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setScreenedTBC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getTestedHIVFirstDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setTestedHIVC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getAlreadyPositiveDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setAlreadyPositiveC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getTestedPositiveDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setTestedPositiveC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getOnARTDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setOnARTC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getViralLoadDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setViralLoadC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getMaleTestedDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setMaleTestedC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getMalePositiveDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setMalePositiveC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getMaleAlreadyPositiveDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setMaleAlreadyPositiveC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getMaleStartedARTDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setMaleStartedARTC(getCursorValue(c, "key"));

            return record;
        };
    }

    public static DataMap<ReportModel1> getDiscordantDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setDiscordantC(getCursorValue(c, "key"));

            return record;
        };
    }


    public static DataMap<ReportModel1> getOriginCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setOriginCount(getCursorValue(c, "origin"));

            return record;
        };
    }


    public static DataMap<ReportModel1> getGeneralCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setGeneralKey(getCursorValue(c, "key"));
            record.setGeneralValue(getCursorValue(c, "value"));
            record.setGeneralBaseEntityID(getCursorValue(c, "base_entity_id"));
            return record;
        };
    }

    public static DataMap<ReportModel1> getFirstContactCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setFirstCount(getCursorValue(c, "contact_no"));
            record.setFirstIDCount(getCursorValue(c, "base_entity_id"));
            return record;
        };
    }

    public static DataMap<ReportModel1> getSecondContactCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setSecondCount(getCursorValue(c, "contact_no"));
            record.setSecondIDCount(getCursorValue(c, "base_entity_id"));
            return record;
        };
    }

    public static DataMap<ReportModel1> getThirdContactCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setThirdCount(getCursorValue(c, "contact_no"));
            record.setThirdIDCount(getCursorValue(c, "base_entity_id"));
            return record;
        };
    }

    public static DataMap<ReportModel1> getFourthToSeventhContactCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setFourthToSeventhCount(getCursorValue(c, "contact_no"));
            record.setFourthToSeventhIDCount(getCursorValue(c, "base_entity_id"));
            return record;
        };
    }

    public static DataMap<ReportModel1> getEighthAboveContactCountDataMap() {
        return c -> {
            ReportModel1 record = new ReportModel1();
            record.setEighthAboveCount(getCursorValue(c, "contact_no"));
            record.setEighthAboveIDCount(getCursorValue(c, "base_entity_id"));
            return record;
        };
    }

    public static DataMap<AttentionFlagModel> getAttentionFlagDataMap() {
        return c -> {
            AttentionFlagModel record = new AttentionFlagModel();
            record.setValues(getCursorValue(c, "value"));
            record.setAge(getCursorValue(c, "age"));


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
