package org.smartregister.anc.library.util;

/**
 * Created by ndegwamartin on 30/06/2018.
 */
public class DBConstantsUtils {
    public static final String CONTACT_ENTITY_TYPE = "contact";
    public static final String DEMOGRAPHIC_TABLE_NAME = "ec_client";
    public static final String WOMAN_DETAILS_TABLE_NAME = "ec_mother_details";

    public interface RegisterTable {
        String DEMOGRAPHIC = "ec_client";
        String DETAILS = "ec_mother_details";
    }

    public static final class KeyUtils {
        public static final String ID = "_ID";
        public static final String ID_LOWER_CASE = "_id";
        public static final String STEPNAME = "stepName";
        public static final String NUMBER_PICKER = "number_picker";
        public static final String FIRST_NAME = "first_name";
        public static final String MAIDEN_NAME = "maiden_name";
        public static final String LAST_NAME = "last_name";
        public static final String BASE_ENTITY_ID = "base_entity_id";
        public static final String DOB = "dob";//Date Of Birth
        public static final String DOB_UNKNOWN = "dob_unknown";
        public static final String EDD = "edd";
        public static final String GENDER = "gender";
        public static final String ANC_ID = "register_id";
        public static final String LAST_INTERACTED_WITH = "last_interacted_with";
        public static final String DATE_REMOVED = "date_removed";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String ALT_NAME = "alt_name";
        public static final String ALT_PHONE_NUMBER = "alt_phone_number";
        public static final String HOME_ADDRESS = "home_address";
        public static final String LANDMARK = "landmark";
        public static final String STUDY_ID = "study_id";
        public static final String NRC_NUMBER = "nrc";
        public static final String RELATION_NK = "relation_NK";
        public static final String OCCUPATION = "occupation";
        public static final String OCCUPATION_REDACTED = "occupation_redacted";
        public static final String MARITAL_STATUS = "marital_status";
        public static final String EDUC_LEVEL = "educ_level";
        public static final String COUPLE = "couple";
        public static final String AGE = "age";
        public static final String ORIGIN = "origin";
        public static final String REMINDERS = "reminders";
        public static final String RED_FLAG_COUNT = "red_flag_count";
        public static final String YELLOW_FLAG_COUNT = "yellow_flag_count";
        public static final String CONTACT_STATUS = "contact_status";
        public static final String PREVIOUS_CONTACT_STATUS = "previous_contact_status";
        public static final String NEXT_CONTACT = "next_contact";
        public static final String NEXT_CONTACT_DATE = "next_contact_date";
        public static final String LAST_CONTACT_RECORD_DATE = "last_contact_record_date";
        public static final String RELATIONAL_ID = "relationalid";
        public static final String VISIT_START_DATE = "visit_start_date";
        public static final String IS_FIRST_VISIT = "is_first_visit";
        public static final String COHABITANTS = "cohabitants";
        public static final String OTHER_RELATIONS = "other_relation_to_client";

        public static final String GRAVIDA = "gravida";
        public static final String PREG_ONE_COMPLICATIONS = "preg_one_complications";
        public static final String PREG_ONE_BIRTH_WEIGHT = "preg_one_birth_weight";
        public static final String PREG_ONE_SEX_INFANT = "preg_one_sex_infant";
        public static final String PREG_ONE_OUTCOME_EARLY = "preg_one_outcome_early";
        public static final String PREG_ONE_OUTCOME = "preg_one_outcome";
        public static final String C_TYPE = "c_type";
        public static final String LABOUR_TYPE = "labour_type";
        public static final String TYPE_OF_ASSISTED = "type_of_assisted";
        public static final String DELIVERY_METHOD = "delivery_method";
        public static final String GESTATIONAL_AGE = "gestational_age";
        public static final String PREG_ONE_ESTIMATED_DELIVERY_TERMINATION = "preg_one_estimated_delivery_termination";
        public static final String PREG_ONE_DT_UNKNOWN = "preg_one_dt_unknown";
        public static final String PREG_ONE_DELIVERY_TERMINATION = "preg_one_delivery_termination";

        public static final String PREGNANCY_TWO_COMPLICATIONS = "pregnancy_two_complications";
        public static final String PREGNANCY_TWO_BIRTH_WEIGHT = "pregnancy_two_birth_weight";
        public static final String PREGNANCY_TWO_SEX_INFANT = "pregnancy_two_sex_infant";
        public static final String PREGNANCY_TWO_OUTCOME_EARLY = "pregnancy_two_outcome_early";
        public static final String PREGNANCY_TWO_OUTCOME = "pregnancy_two_outcome";
        public static final String C_TYPE_TWO = "c_type_two";
        public static final String PREGNANCY_TWO_LABOUR_TYPE = "pregnancy_two_labour_type";
        public static final String PREGNANCY_TWO_TYPE_OF_ASSISTED = "pregnancy_two_type_of_assisted";
        public static final String PREGNANCY_TWO_DELIVERY_METHOD = "pregnancy_two_delivery_method";
        public static final String PREGNANCY_TWO_GESTATIONAL_AGE = "pregnancy_two_gestational_age";
        public static final String PREGNANCY_TWO_ESTIMATED_DELIVERY_TERMINATION = "pregnancy_two_estimated_delivery_termination";
        public static final String PREGNANCY_TWO_DT_UNKNOWN = "pregnancy_two_dt_unknown";
        public static final String PREGNANCY_TWO_DELIVERY_TERMINATION = "pregnancy_two_delivery_termination";

        public static final String PREG_THREE_COMPLICATIONS = "preg_three_complications";
        public static final String PREG_THREE_BIRTH_WEIGHT = "preg_three_birth_weight";
        public static final String PREG_THREE_SEX_INFANT = "preg_three_sex_infant";
        public static final String PREG_THREE_OUTCOME_EARLY = "preg_three_outcome_early";
        public static final String PREG_THREE_OUTCOME = "preg_three_outcome";
        public static final String PREG_THREE_LABOUR_TYPE = "preg_three_labour_type";
        public static final String C_TYPE_THREE = "c_type_three";
        public static final String PREGNANCY_THREE_TYPE_OF_ASSISTED = "pregnancy_three_type_of_assisted";
        public static final String PREG_THREE_DELIVERY_METHOD = "preg_three_delivery_method";
        public static final String PREG_THREE_GESTATIONAL_AGE = "preg_three_gestational_age";
        public static final String PREG_THREE_ESTIMATED_DELIVERY_TERMINATION = "preg_three_estimated_delivery_termination";
        public static final String PREG_THREE_DT_UNKNOWN = "preg_three_dt_unknown";
        public static final String PREG_THREE_DELIVERY_TERMINATION = "preg_three_delivery_termination";


        public static final String PREG_FOUR_COMPLICATIONS = "preg_four_complications";
        public static final String PREG_FOUR_BIRTH_WEIGHT = "preg_four_birth_weight";
        public static final String PREG_FOUR_SEX_INFANT = "preg_four_sex_infant";
        public static final String PREG_FOUR_OUTCOME_EARLY = "preg_four_outcome_early";
        public static final String PREG_FOUR_OUTCOME = "preg_four_outcome";
        public static final String PREG_FOUR_LABOUR_TYPE = "preg_four_labour_type";
        public static final String C_TYPE_FOUR = "c_type_four";
        public static final String PREGNANCY_FOUR_TYPE_OF_ASSISTED = "pregnancy_four_type_of_assisted";
        public static final String PREG_FOUR_DELIVERY_METHOD = "preg_four_delivery_method";
        public static final String PREG_FOUR_GESTATIONAL_AGE = "preg_four_gestational_age";
        public static final String PREG_FOUR_ESTIMATED_DELIVERY_TERMINATION = "preg_four_estimated_delivery_termination";
        public static final String PREG_FOUR_DT_UNKNOWN = "preg_four_dt_unknown";
        public static final String PREG_FOUR_DELIVERY_TERMINATION = "preg_four_delivery_termination";

        public static final String PREG_FIVE_COMPLICATIONS = "preg_five_complications";
        public static final String PREG_FIVE_BIRTH_WEIGHT = "preg_five_birth_weight";
        public static final String PREG_FIVE_SEX_INFANT = "preg_five_sex_infant";
        public static final String PREG_FIVE_OUTCOME_EARLY = "preg_five_outcome_early";
        public static final String PREG_FIVE_OUTCOME = "preg_five_outcome";
        public static final String PREG_FIVE_LABOUR_TYPE = "preg_five_labour_type";
        public static final String C_TYPE_FIVE = "c_type_five";
        public static final String PREGNANCY_FIVE_TYPE_OF_ASSISTED = "pregnancy_five_type_of_assisted";
        public static final String PREG_FIVE_DELIVERY_METHOD = "preg_five_delivery_method";
        public static final String PREG_FIVE_GESTATIONAL_AGE = "preg_five_gestational_age";
        public static final String PREG_FIVE_ESTIMATED_DELIVERY_TERMINATION = "preg_five_estimated_delivery_termination";
        public static final String PREG_FIVE_DT_UNKNOWN = "preg_five_dt_unknown";
        public static final String PREG_FIVE_DELIVERY_TERMINATION = "preg_five_delivery_termination";


        public static final String PREG_SIX_COMPLICATIONS = "preg_six_complications";
        public static final String PREG_SIX_BIRTH_WEIGHT = "preg_six_birth_weight";
        public static final String PREG_SIX_SEX_INFANT = "preg_six_sex_infant";
        public static final String PREG_SIX_OUTCOME_EARLY = "preg_six_outcome_early";
        public static final String PREG_SIX_OUTCOME = "preg_six_outcome";
        public static final String PREG_SIX_LABOUR_TYPE = "preg_six_labour_type";
        public static final String C_TYPE_SIX = "c_type_six";
        public static final String PREGNANCY_SIX_TYPE_OF_ASSISTED = "pregnancy_six_type_of_assisted";
        public static final String PREG_SIX_DELIVERY_METHOD = "preg_six_delivery_method";
        public static final String PREG_SIX_GESTATIONAL_AGE = "preg_six_gestational_age";
        public static final String PREG_SIX_ESTIMATED_DELIVERY_TERMINATION = "preg_six_estimated_delivery_termination";
        public static final String PREG_SIX_DT_UNKNOWN = "preg_six_dt_unknown";
        public static final String PREG_SIX_DELIVERY_TERMINATION = "preg_six_delivery_termination";

        public static final String PREG_SEVEN_COMPLICATIONS = "preg_seven_complications";
        public static final String PREG_SEVEN_BIRTH_WEIGHT = "preg_seven_birth_weight";
        public static final String PREG_SEVEN_SEX_INFANT = "preg_seven_sex_infant";
        public static final String PREG_SEVEN_OUTCOME_EARLY = "preg_seven_outcome_early";
        public static final String PREG_SEVEN_OUTCOME = "preg_seven_outcome";
        public static final String PREG_SEVEN_LABOUR_TYPE = "preg_seven_labour_type";
        public static final String C_TYPE_SEVEN = "c_type_seven";
        public static final String PREGNANCY_SEVEN_TYPE_OF_ASSISTED = "pregnancy_seven_type_of_assisted";
        public static final String PREG_SEVEN_DELIVERY_METHOD = "preg_seven_delivery_method";
        public static final String PREG_SEVEN_GESTATIONAL_AGE = "preg_seven_gestational_age";
        public static final String PREG_SEVEN_ESTIMATED_DELIVERY_TERMINATION = "preg_seven_estimated_delivery_termination";
        public static final String PREG_SEVEN_DT_UNKNOWN = "preg_seven_dt_unknown";
        public static final String PREG_SEVEN_DELIVERY_TERMINATION = "preg_seven_delivery_termination";

        public static final String PREG_EIGHT_COMPLICATIONS = "preg_eight_complications";
        public static final String PREG_EIGHT_BIRTH_WEIGHT = "preg_eight_birth_weight";
        public static final String PREG_EIGHT_SEX_INFANT = "preg_eight_sex_infant";
        public static final String PREG_EIGHT_OUTCOME_EARLY = "preg_eight_outcome_early";
        public static final String PREG_EIGHT_OUTCOME = "preg_eight_outcome";
        public static final String PREG_EIGHT_LABOUR_TYPE = "preg_eight_labour_type";
        public static final String C_TYPE_EIGHT = "c_type_eight";
        public static final String PREGNANCY_EIGHT_TYPE_OF_ASSISTED = "pregnancy_eight_type_of_assisted";
        public static final String PREG_EIGHT_DELIVERY_METHOD = "preg_eight_delivery_method";
        public static final String PREG_EIGHT_GESTATIONAL_AGE = "preg_eight_gestational_age";
        public static final String PREG_EIGHT_ESTIMATED_DELIVERY_TERMINATION = "preg_eight_estimated_delivery_termination";
        public static final String PREG_EIGHT_DT_UNKNOWN = "preg_eight_dt_unknown";
        public static final String PREG_EIGHT_DELIVERY_TERMINATION = "preg_eight_delivery_termination";

        public static final String PREG_NINE_COMPLICATIONS = "preg_nine_complications";
        public static final String PREG_NINE_BIRTH_WEIGHT = "preg_nine_birth_weight";
        public static final String PREG_NINE_SEX_INFANT = "preg_nine_sex_infant";
        public static final String PREG_NINE_OUTCOME_EARLY = "preg_nine_outcome_early";
        public static final String PREG_NINE_OUTCOME = "preg_nine_outcome";
        public static final String PREG_NINE_LABOUR_TYPE = "preg_nine_labour_type";
        public static final String C_TYPE_NINE = "c_type_nine";
        public static final String PREGNANCY_NINE_TYPE_OF_ASSISTED = "pregnancy_nine_type_of_assisted";
        public static final String PREG_NINE_DELIVERY_METHOD = "preg_nine_delivery_method";
        public static final String PREG_NINE_GESTATIONAL_AGE = "preg_nine_gestational_age";
        public static final String PREG_NINE_ESTIMATED_DELIVERY_TERMINATION = "preg_nine_estimated_delivery_termination";
        public static final String PREG_NINE_DT_UNKNOWN = "preg_nine_dt_unknown";
        public static final String PREG_NINE_DELIVERY_TERMINATION = "preg_nine_delivery_termination";


        public static final String PREG_TEN_COMPLICATIONS = "preg_ten_complications";
        public static final String PREG_TEN_BIRTH_WEIGHT = "preg_ten_birth_weight";
        public static final String PREG_TEN_SEX_INFANT = "preg_ten_sex_infant";
        public static final String PREG_TEN_OUTCOME_EARLY = "preg_ten_outcome_early";
        public static final String PREG_TEN_OUTCOME = "preg_ten_outcome";
        public static final String PREG_TEN_LABOUR_TYPE = "preg_ten_labour_type";
        public static final String C_TYPE_TEN = "c_type_ten";
        public static final String PREGNANCY_TEN_TYPE_OF_ASSISTED = "pregnancy_ten_type_of_assisted";
        public static final String PREG_TEN_DELIVERY_METHOD = "preg_ten_delivery_method";
        public static final String PREG_TEN_GESTATIONAL_AGE = "preg_ten_gestational_age";
        public static final String PREG_TEN_ESTIMATED_DELIVERY_TERMINATION = "preg_ten_estimated_delivery_termination";
        public static final String PREG_TEN_DT_UNKNOWN = "preg_ten_dt_unknown";
        public static final String PREG_TEN_DELIVERY_TERMINATION = "preg_ten_delivery_termination";

        // Continuing with the same format...
        public static final String PREG_ELEVEN_COMPLICATIONS = "preg_eleven_complications";
        public static final String PREG_ELEVEN_BIRTH_WEIGHT = "preg_eleven_birth_weight";
        public static final String PREG_ELEVEN_SEX_INFANT = "preg_eleven_sex_infant";
        public static final String PREG_ELEVEN_OUTCOME_EARLY = "preg_eleven_outcome_early";
        public static final String PREG_ELEVEN_OUTCOME = "preg_eleven_outcome";
        public static final String PREG_ELEVEN_LABOUR_TYPE = "preg_eleven_labour_type";
        public static final String C_TYPE_ELEVEN = "c_type_eleven";
        public static final String PREGNANCY_ELEVEN_TYPE_OF_ASSISTED = "pregnancy_eleven_type_of_assisted";
        public static final String PREG_ELEVEN_DELIVERY_METHOD = "preg_eleven_delivery_method";
        public static final String PREG_ELEVEN_GESTATIONAL_AGE = "preg_eleven_gestational_age";
        public static final String PREG_ELEVEN_ESTIMATED_DELIVERY_TERMINATION = "preg_eleven_estimated_delivery_termination";
        public static final String PREG_ELEVEN_DT_UNKNOWN = "preg_eleven_dt_unknown";
        public static final String PREG_ELEVEN_DELIVERY_TERMINATION = "preg_eleven_delivery_termination";

        public static final String PREG_TWELVE_COMPLICATIONS = "preg_twelve_complications";
        public static final String PREG_TWELVE_BIRTH_WEIGHT = "preg_twelve_birth_weight";
        public static final String PREG_TWELVE_SEX_INFANT = "preg_twelve_sex_infant";
        public static final String PREG_TWELVE_OUTCOME_EARLY = "preg_twelve_outcome_early";
        public static final String PREG_TWELVE_OUTCOME = "preg_twelve_outcome";
        public static final String PREG_TWELVE_LABOUR_TYPE = "preg_twelve_labour_type";
        public static final String C_TYPE_TWELVE = "c_type_twelve";
        public static final String PREGNANCY_TWELVE_TYPE_OF_ASSISTED = "pregnancy_twelve_type_of_assisted";
        public static final String PREG_TWELVE_DELIVERY_METHOD = "preg_twelve_delivery_method";
        public static final String PREG_TWELVE_GESTATIONAL_AGE = "preg_twelve_gestational_age";
        public static final String PREG_TWELVE_ESTIMATED_DELIVERY_TERMINATION = "preg_twelve_estimated_delivery_termination";
        public static final String PREG_TWELVE_DT_UNKNOWN = "preg_twelve_dt_unknown";
        public static final String PREG_TWELVE_DELIVERY_TERMINATION = "preg_twelve_delivery_termination";

        public static final String PREG_THIRTEEN_COMPLICATIONS = "preg_thirteen_complications";
        public static final String PREG_THIRTEEN_BIRTH_WEIGHT = "preg_thirteen_birth_weight";
        public static final String PREG_THIRTEEN_SEX_INFANT = "preg_thirteen_sex_infant";
        public static final String PREG_THIRTEEN_OUTCOME_EARLY = "preg_thirteen_outcome_early";
        public static final String PREG_THIRTEEN_OUTCOME = "preg_thirteen_outcome";
        public static final String PREG_THIRTEEN_LABOUR_TYPE = "preg_thirteen_labour_type";
        public static final String C_TYPE_THIRTEEN = "c_type_thirteen";
        public static final String PREGNANCY_THIRTEEN_TYPE_OF_ASSISTED = "pregnancy_thirteen_type_of_assisted";
        public static final String PREG_THIRTEEN_DELIVERY_METHOD = "preg_thirteen_delivery_method";
        public static final String PREG_THIRTEEN_GESTATIONAL_AGE = "preg_thirteen_gestational_age";
        public static final String PREG_THIRTEEN_ESTIMATED_DELIVERY_TERMINATION = "preg_thirteen_estimated_delivery_termination";
        public static final String PREG_THIRTEEN_DT_UNKNOWN = "preg_thirteen_dt_unknown";
        public static final String PREG_THIRTEEN_DELIVERY_TERMINATION = "preg_thirteen_delivery_termination";

        public static final String PREG_FOURTEEN_COMPLICATIONS = "preg_fourteen_complications";
        public static final String PREG_FOURTEEN_BIRTH_WEIGHT = "preg_fourteen_birth_weight";
        public static final String PREG_FOURTEEN_SEX_INFANT = "preg_fourteen_sex_infant";
        public static final String PREG_FOURTEEN_OUTCOME_EARLY = "preg_fourteen_outcome_early";
        public static final String PREG_FOURTEEN_OUTCOME = "preg_fourteen_outcome";
        public static final String PREG_FOURTEEN_LABOUR_TYPE = "preg_fourteen_labour_type";
        public static final String C_TYPE_FOURTEEN = "c_type_fourteen";
        public static final String PREGNANCY_FOURTEEN_TYPE_OF_ASSISTED = "pregnancy_fourteen_type_of_assisted";
        public static final String PREG_FOURTEEN_DELIVERY_METHOD = "preg_fourteen_delivery_method";
        public static final String PREG_FOURTEEN_GESTATIONAL_AGE = "preg_fourteen_gestational_age";
        public static final String PREG_FOURTEEN_ESTIMATED_DELIVERY_TERMINATION = "preg_fourteen_estimated_delivery_termination";
        public static final String PREG_FOURTEEN_DT_UNKNOWN = "preg_fourteen_dt_unknown";
        public static final String PREG_FOURTEEN_DELIVERY_TERMINATION = "preg_fourteen_delivery_termination";


        public static final String PREG_FIFTEEN_COMPLICATIONS = "preg_fifteen_complications";
        public static final String PREG_FIFTEEN_BIRTH_WEIGHT = "preg_fifteen_birth_weight";
        public static final String PREG_FIFTEEN_SEX_INFANT = "preg_fifteen_sex_infant";
        public static final String PREG_FIFTEEN_OUTCOME_EARLY = "preg_fifteen_outcome_early";
        public static final String PREG_FIFTEEN_OUTCOME = "preg_fifteen_outcome";
        public static final String PREG_FIFTEEN_LABOUR_TYPE = "preg_fifteen_labour_type";
        public static final String C_TYPE_FIFTEEN = "c_type_fifteen";
        public static final String PREGNANCY_FIFTEEN_TYPE_OF_ASSISTED = "pregnancy_fifteen_type_of_assisted";
        public static final String PREG_FIFTEEN_DELIVERY_METHOD = "preg_fifteen_delivery_method";
        public static final String PREG_FIFTEEN_GESTATIONAL_AGE = "preg_fifteen_gestational_age";
        public static final String PREG_FIFTEEN_ESTIMATED_DELIVERY_TERMINATION = "preg_fifteen_estimated_delivery_termination";
        public static final String PREG_FIFTEEN_DT_UNKNOWN = "preg_fifteen_dt_unknown";
        public static final String PREG_FIFTEEN_DELIVERY_TERMINATION = "preg_fifteen_delivery_termination";

        public static final String PREV_PREG_PROBS = "prev_preg_probs";
        public static final String PREV_PREG_COMPS = "prev_preg_comps";
    }
}