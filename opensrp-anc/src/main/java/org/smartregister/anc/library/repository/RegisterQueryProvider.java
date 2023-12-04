package org.smartregister.anc.library.repository;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.anc.library.util.ConstantsUtils;
import org.smartregister.anc.library.util.DBConstantsUtils;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

public class RegisterQueryProvider {

    public String getObjectIdsQuery(String mainCondition, String filters) {

        String strMainCondition = getMainCondition(mainCondition);

        String strFilters = getFilter(filters);

        if (StringUtils.isNotBlank(strFilters) && StringUtils.isBlank(strMainCondition)) {
            strFilters = String.format(" where " + getDemographicTable() + "." + CommonFtsObject.phraseColumn + " MATCH '*%s*'", filters);
        }

        return "select " + getDemographicTable() + "." + CommonFtsObject.idColumn + " from " + CommonFtsObject.searchTableName(getDemographicTable()) + " " + getDemographicTable() + "  " +
                "join " + getDetailsTable() + " on " + getDemographicTable() + "." + CommonFtsObject.idColumn + " =  " + getDetailsTable() + "." + "id " + strMainCondition + strFilters;
    }

    private String getMainCondition(String mainCondition) {
        if (StringUtils.isNotBlank(mainCondition)) {
            return " where " + mainCondition;
        }
        return "";
    }

    private String getFilter(String filters) {

        if (StringUtils.isNotBlank(filters)) {
            return String.format(" AND " + getDemographicTable() + "." + CommonFtsObject.phraseColumn + " MATCH '*%s*'", filters);
        }
        return "";
    }

    public String getDemographicTable() {
        return DBConstantsUtils.RegisterTable.DEMOGRAPHIC;
    }

    public String getDetailsTable() {
        return DBConstantsUtils.RegisterTable.DETAILS;
    }


    public String getCountExecuteQuery(String mainCondition, String filters) {

        String strFilters = getFilter(filters);

        if (StringUtils.isNotBlank(filters) && StringUtils.isBlank(mainCondition)) {
            strFilters = String.format(" where " + CommonFtsObject.searchTableName(getDemographicTable()) + "." + CommonFtsObject.phraseColumn + " MATCH '*%s*'", filters);
        }

        String strMainCondition = getMainCondition(mainCondition);

        return "select count(" + getDemographicTable() + "." + CommonFtsObject.idColumn + ") from " + CommonFtsObject.searchTableName(getDemographicTable()) + " " + getDemographicTable() + "  " +
                "join " + getDetailsTable() + " on " + getDemographicTable() + "." + CommonFtsObject.idColumn + " =  " + getDetailsTable() + "." + "id " + strMainCondition + strFilters;
    }

    public String mainRegisterQuery() {
        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.selectInitiateMainTable(getDemographicTable(), mainColumns());
        queryBuilder.customJoin(" join " + getDetailsTable()
                + " on " + getDemographicTable() + "." + DBConstantsUtils.KeyUtils.BASE_ENTITY_ID + "= " + getDetailsTable() + "." + DBConstantsUtils.KeyUtils.BASE_ENTITY_ID + " ");
        return queryBuilder.getSelectquery();
    }

    public String[] mainColumns() {
        return new String[]{DBConstantsUtils.KeyUtils.FIRST_NAME, DBConstantsUtils.KeyUtils.MAIDEN_NAME, DBConstantsUtils.KeyUtils.LAST_NAME, DBConstantsUtils.KeyUtils.DOB, DBConstantsUtils.KeyUtils.OTHER_RELATIONS, DBConstantsUtils.KeyUtils.HOME_ADDRESS, DBConstantsUtils.KeyUtils.LANDMARK, DBConstantsUtils.KeyUtils.OCCUPATION, DBConstantsUtils.KeyUtils.OCCUPATION_REDACTED, DBConstantsUtils.KeyUtils.EDUC_LEVEL, DBConstantsUtils.KeyUtils.COUPLE, DBConstantsUtils.KeyUtils.RELATION_NK, DBConstantsUtils.KeyUtils.STUDY_ID, DBConstantsUtils.KeyUtils.NRC_NUMBER, DBConstantsUtils.KeyUtils.MARITAL_STATUS,
                DBConstantsUtils.KeyUtils.DOB_UNKNOWN, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.ALT_NAME,
                getDemographicTable() + "." + DBConstantsUtils.KeyUtils.BASE_ENTITY_ID,
                getDemographicTable() + "." + DBConstantsUtils.KeyUtils.BASE_ENTITY_ID + " as " + DBConstantsUtils.KeyUtils.ID_LOWER_CASE, DBConstantsUtils.KeyUtils.ANC_ID,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.REMINDERS, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.EDD, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PHONE_NUMBER, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.ALT_PHONE_NUMBER,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.CONTACT_STATUS, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREVIOUS_CONTACT_STATUS, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.ORIGIN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.NEXT_CONTACT, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.NEXT_CONTACT_DATE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.VISIT_START_DATE, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.RED_FLAG_COUNT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.YELLOW_FLAG_COUNT, getDetailsTable() + "." + DBConstantsUtils.KeyUtils.LAST_CONTACT_RECORD_DATE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.COHABITANTS, getDemographicTable() + "." + DBConstantsUtils.KeyUtils.RELATIONAL_ID,
                getDetailsTable() + "." + ConstantsUtils.SpinnerKeyConstants.PROVINCE, getDetailsTable() + "." + ConstantsUtils.SpinnerKeyConstants.DISTRICT,
                getDetailsTable() + "." + ConstantsUtils.SpinnerKeyConstants.SUB_DISTRICT, getDetailsTable() + "." + ConstantsUtils.SpinnerKeyConstants.FACILITY,
                getDetailsTable() + "." + ConstantsUtils.SpinnerKeyConstants.VILLAGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.GRAVIDA,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREV_PREG_PROBS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREV_PREG_COMPS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ONE_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ONE_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ONE_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ONE_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ONE_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ONE_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ONE_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ONE_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_THREE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_THREE_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THREE_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_FOUR,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_FOUR_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOUR_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_FIVE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_FIVE_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIVE_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_SIX,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_SIX_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SIX_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_EIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_EIGHT_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_EIGHT_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_SEVEN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_SEVEN_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_SEVEN_DELIVERY_TERMINATION,


                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_NINE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_NINE_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_NINE_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_TEN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TEN_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TEN_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_ELEVEN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_ELEVEN_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_ELEVEN_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_TWELVE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWELVE_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_TWELVE_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_THIRTEEN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_THIRTEEN_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_FOURTEEN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_FOURTEEN_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_FIFTEEN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_FIFTEEN_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DELIVERY_TERMINATION,

                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_COMPLICATIONS,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_BIRTH_WEIGHT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_SEX_INFANT,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_OUTCOME_EARLY,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_OUTCOME,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_LABOUR_TYPE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.C_TYPE_TWO,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_TYPE_OF_ASSISTED,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DELIVERY_METHOD,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_GESTATIONAL_AGE,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_ESTIMATED_DELIVERY_TERMINATION,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DT_UNKNOWN,
                getDetailsTable() + "." + DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DELIVERY_TERMINATION,


        };

    }
}