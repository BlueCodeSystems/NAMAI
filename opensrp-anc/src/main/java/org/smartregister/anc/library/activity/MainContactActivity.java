package org.smartregister.anc.library.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.rules.RuleConstant;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.anc.library.AncLibrary;
import org.smartregister.anc.library.R;
import org.smartregister.anc.library.contract.ContactContract;
import org.smartregister.anc.library.domain.Contact;
import org.smartregister.anc.library.model.PartialContact;
import org.smartregister.anc.library.model.PreviousContact;
import org.smartregister.anc.library.presenter.ContactPresenter;
import org.smartregister.anc.library.repository.PatientRepository;
import org.smartregister.anc.library.util.ANCFormUtils;
import org.smartregister.anc.library.util.ANCJsonFormUtils;
import org.smartregister.anc.library.util.ConstantsUtils;
import org.smartregister.anc.library.util.DBConstantsUtils;
import org.smartregister.anc.library.util.FilePathUtils;
import org.smartregister.anc.library.util.Utils;
import org.smartregister.util.FormUtils;
import org.smartregister.view.contract.MeContract;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.UUID;


public class MainContactActivity extends BaseContactActivity implements ContactContract.View {
    private TextView patientNameView;
    private final Map<String, Integer> requiredFieldsMap = new HashMap<>();
    private final Map<String, String> eventToFileMap = new HashMap<>();
    private final Yaml yaml = new Yaml();
    private final Map<String, List<String>> formGlobalKeys = new HashMap<>();
    private final Map<String, String> formGlobalValues = new HashMap<>();
    private final Set<String> globalKeys = new HashSet<>();
    private final Set<String> defaultValueFields = new HashSet<>();
    private List<String> globalValueFields = new ArrayList<>();
    private List<String> editableFields = new ArrayList<>();
    private String baseEntityId;
    private String womanAge = "";
    private final List<String> invisibleRequiredFields = new ArrayList<>();
    private final String[] contactForms = new String[]{ConstantsUtils.JsonFormUtils.ANC_QUICK_CHECK, ConstantsUtils.JsonFormUtils.ANC_PROFILE,
            ConstantsUtils.JsonFormUtils.ANC_SYMPTOMS_FOLLOW_UP, ConstantsUtils.JsonFormUtils.ANC_PHYSICAL_EXAM,
            ConstantsUtils.JsonFormUtils.ANC_TEST, ConstantsUtils.JsonFormUtils.ANC_COUNSELLING_TREATMENT, ConstantsUtils.JsonFormUtils.ANC_TEST_TASKS};
    private String formInvalidFields = null;
    public List<Contact> contacts = new ArrayList<>();
    TextView requiredFields, requiredFields2, requiredFields3, requiredFields4, requiredFields5, requiredFields6, requiredFieldsx;
    LinearLayout completeLayout, completeLayout2, completeLayout3, completeLayout4,
            completeLayout5, completeLayout6, completeLayoutx, mainlayout, middleLayout;
    Button finalizeBtn;


    @Override
    protected void onResume() {
        super.onResume();

        baseEntityId = getIntent().getStringExtra(ConstantsUtils.IntentKeyUtils.BASE_ENTITY_ID);
        contactNo = getIntent().getIntExtra(ConstantsUtils.IntentKeyUtils.CONTACT_NO, 1);
        @SuppressWarnings("unchecked") Map<String, String> womanDetails =
                (Map<String, String>) getIntent().getSerializableExtra(ConstantsUtils.IntentKeyUtils.CLIENT_MAP);
        if (womanDetails != null && womanDetails.size() > 0) {
            womanAge = String.valueOf(Utils.getAgeFromDate(womanDetails.get(DBConstantsUtils.KeyUtils.DOB)));
        }
        if (!presenter.baseEntityIdExists()) {
            presenter.setBaseEntityId(baseEntityId);
        }

        initializeMainContactContainers();
        initializeContactLayout();
        setFields(contacts);
        int finished = getRequiredCountTotal();

        if (finished == 0) {
            finalizeBtn.setEnabled(true);
        }

    }


    private void initializeMainContactContainers() {

        try {

            requiredFieldsMap.clear();
            loadContactGlobalsConfig();

            process(contactForms);
            requiredFieldsMap.put(ConstantsUtils.JsonFormUtils.ANC_TEST_TASKS_ENCOUNTER_TYPE, 0);

            if (StringUtils.isNotBlank(formInvalidFields) && contactNo > 1 && !PatientRepository.isFirstVisit(baseEntityId)) {
                String[] pair = formInvalidFields.split(":");
                if (ConstantsUtils.JsonFormUtils.ANC_PROFILE_ENCOUNTER_TYPE.equals(pair[0]))
                    requiredFieldsMap.put(pair[0], Integer.parseInt(pair[1]));
            }

            /*-- 1: RAM Quick Check Container --*/

            Contact quickCheck = new Contact();

            quickCheck.setName(getString(R.string.quick_check));
            quickCheck.setContactNumber(contactNo);
            quickCheck.setActionBarBackground(R.color.quick_check_red);
            quickCheck.setBackground(R.drawable.quick_check_bg);
            quickCheck.setWizard(false);
            quickCheck.setHideSaveLabel(true);
            setRequiredFields(quickCheck);
            quickCheck.setFormName(ConstantsUtils.JsonFormUtils.ANC_QUICK_CHECK);
            contacts.add(quickCheck);

            /*-- 2: Client Profile Container --*/

            Contact profile = new Contact();
            profile.setName(getString(R.string.profile));
            profile.setContactNumber(contactNo);
            profile.setActionBarBackground(R.color.contact_profile_actionbar);
            profile.setNavigationBackground(R.color.contact_profile_navigation);
            setRequiredFields(profile);
            profile.setFormName(ConstantsUtils.JsonFormUtils.ANC_PROFILE);
            contacts.add(profile);


            /*-- 3: Symptoms and Follow Up --*/

            Contact symptomsAndFollowUp = new Contact();
            symptomsAndFollowUp.setName(getString(R.string.symptoms_follow_up));
            symptomsAndFollowUp.setContactNumber(contactNo);
            symptomsAndFollowUp.setActionBarBackground(R.color.contact_symptoms_actionbar);
            symptomsAndFollowUp.setNavigationBackground(R.color.contact_symptoms_navigation);
            setRequiredFields(symptomsAndFollowUp);
            symptomsAndFollowUp.setFormName(ConstantsUtils.JsonFormUtils.ANC_SYMPTOMS_FOLLOW_UP);
            contacts.add(symptomsAndFollowUp);

            /*-- 4: Physical Exam --*/

            Contact physicalExam = new Contact();
            physicalExam.setName(getString(R.string.physical_exam));
            physicalExam.setContactNumber(contactNo);
            physicalExam.setActionBarBackground(R.color.contact_exam_actionbar);
            physicalExam.setNavigationBackground(R.color.contact_exam_navigation);
            setRequiredFields(physicalExam);
            physicalExam.setFormName(ConstantsUtils.JsonFormUtils.ANC_PHYSICAL_EXAM);
            contacts.add(physicalExam);

            /*-- 5: Tests Container --*/

            Contact tests = new Contact();
            tests.setName(getString(R.string.tests));
            tests.setContactNumber(contactNo);
            tests.setBackground(R.drawable.tests_bg);
            tests.setActionBarBackground(R.color.contact_tests_actionbar);
            tests.setNavigationBackground(R.color.contact_tests_navigation);
            setRequiredFields(tests);
            tests.setFormName(ConstantsUtils.JsonFormUtils.ANC_TEST);
            contacts.add(tests);


            /*-- Counselling Container --*/

            Contact counsellingAndTreatment = new Contact();
            counsellingAndTreatment.setName(getString(R.string.counselling_treatment));
            counsellingAndTreatment.setContactNumber(contactNo);
            counsellingAndTreatment.setBackground(R.drawable.counselling_bg);
            counsellingAndTreatment.setActionBarBackground(R.color.contact_counselling_actionbar);
            counsellingAndTreatment.setNavigationBackground(R.color.contact_counselling_navigation);
            setRequiredFields(counsellingAndTreatment);
            counsellingAndTreatment.setFormName(ConstantsUtils.JsonFormUtils.ANC_COUNSELLING_TREATMENT);
            contacts.add(counsellingAndTreatment);


        } catch (Exception e) {
            Timber.e(e, " --> initializeMainContactContainers");
        }

    }

    public void initializeContactLayout(){
        requiredFields = findViewById(R.id.required_fields);
        completeLayout = findViewById(R.id.complete_layout);
        requiredFields2 = findViewById(R.id.required_fields2);
        completeLayout2 = findViewById(R.id.complete_layout2);
        requiredFields3 = findViewById(R.id.required_fields3);
        completeLayout3 = findViewById(R.id.complete_layout3);
        requiredFields4 = findViewById(R.id.required_fields4);
        completeLayout4 = findViewById(R.id.complete_layout4);
        requiredFields5 = findViewById(R.id.required_fields5);
        completeLayout5 = findViewById(R.id.complete_layout5);
        requiredFields6 = findViewById(R.id.required_fields6);
        completeLayout6 = findViewById(R.id.complete_layout6);
        requiredFieldsx = findViewById(R.id.required_fieldsx);
        completeLayoutx = findViewById(R.id.complete_layoutx);
        mainlayout = findViewById(R.id.mainlayout);
        middleLayout = findViewById(R.id.middle_containers);
        finalizeBtn = findViewById(R.id.finalize_contact);
    }




        public void setFields(List<Contact> contacts){

        Contact contact1 = contacts.get(0);
        Contact contact2 = contacts.get(1);
        Contact contact3 = contacts.get(2);
        Contact contact4 = contacts.get(3);
        Contact contact5 = contacts.get(4);
        Contact contact6 = contacts.get(5);

            if (contact1.getRequiredFields() == null) {
                requiredFields.setVisibility(View.GONE);
                completeLayout.setVisibility(View.GONE);
            } else if (contact1.getRequiredFields() == 0) {
                completeLayout.setVisibility(View.VISIBLE);
                requiredFields.setVisibility(View.GONE);
            } else {
                requiredFields.setText(String.format(this.getString(R.string.required_fields), contact1.getRequiredFields()));
                requiredFields.setVisibility(View.VISIBLE);
                completeLayout.setVisibility(View.GONE);
            }

            if (contact2.getRequiredFields() == null) {
                requiredFields2.setVisibility(View.GONE);
                completeLayout2.setVisibility(View.GONE);
            } else if (contact2.getRequiredFields() == 0) {
                completeLayout2.setVisibility(View.VISIBLE);
                requiredFields2.setVisibility(View.GONE);
            } else {
                requiredFields2.setText(String.format(this.getString(R.string.required_fields), contact2.getRequiredFields()));
                requiredFields2.setVisibility(View.VISIBLE);
                completeLayout2.setVisibility(View.GONE);
            }

            if (contact3.getRequiredFields() == null) {
                requiredFields3.setVisibility(View.GONE);
                completeLayout3.setVisibility(View.GONE);
            } else if (contact3.getRequiredFields() == 0) {
                completeLayout3.setVisibility(View.VISIBLE);
                requiredFields3.setVisibility(View.GONE);
            } else {
                requiredFields3.setText(String.format(this.getString(R.string.required_fields), contact3.getRequiredFields()));
                requiredFields3.setVisibility(View.VISIBLE);
                completeLayout3.setVisibility(View.GONE);
            }

            if (contact4.getRequiredFields() == null) {
                requiredFields4.setVisibility(View.GONE);
                completeLayout4.setVisibility(View.GONE);
            } else if (contact4.getRequiredFields() == 0) {
                completeLayout4.setVisibility(View.VISIBLE);
                requiredFields4.setVisibility(View.GONE);
            } else {
                requiredFields4.setText(String.format(this.getString(R.string.required_fields), contact4.getRequiredFields()));
                requiredFields4.setVisibility(View.VISIBLE);
                completeLayout4.setVisibility(View.GONE);
            }

            if (contact5.getRequiredFields() == null) {
                requiredFields5.setVisibility(View.GONE);
                completeLayout5.setVisibility(View.GONE);
            } else if (contact5.getRequiredFields() == 0) {
                completeLayout5.setVisibility(View.VISIBLE);
                requiredFields5.setVisibility(View.GONE);
            } else {
                requiredFields5.setText(String.format(this.getString(R.string.required_fields), contact5.getRequiredFields()));
                requiredFields5.setVisibility(View.VISIBLE);
                completeLayout5.setVisibility(View.GONE);
            }

            if (contact6.getRequiredFields() == null) {
                requiredFields6.setVisibility(View.GONE);
                completeLayout6.setVisibility(View.GONE);
            } else if (contact6.getRequiredFields() == 0) {
                completeLayout6.setVisibility(View.VISIBLE);
                requiredFields6.setVisibility(View.GONE);
            } else {
                requiredFields6.setText(String.format(this.getString(R.string.required_fields), contact6.getRequiredFields()));
                requiredFields6.setVisibility(View.VISIBLE);
                completeLayout6.setVisibility(View.GONE);
            }

            if ((contact3.getRequiredFields() == null || contact3.getRequiredFields() == 0) && (contact4.getRequiredFields() == null || contact4.getRequiredFields() == 0) ) {
                requiredFieldsx.setVisibility(View.GONE);
                completeLayoutx.setVisibility(View.GONE);
            } else {

                int c3 = 0;
                int c4 = 0;

                if (contact3.getRequiredFields() != null) {
                    c3 = contact3.getRequiredFields();
                }

                if (contact4.getRequiredFields() != null) {
                    c4 = contact4.getRequiredFields();
                }

                int fc = c3 + c4;

                requiredFieldsx.setText(String.format(this.getString(R.string.required_fields), fc));
                requiredFieldsx.setVisibility(View.VISIBLE);

                if(fc == 0){
                    completeLayoutx.setVisibility(View.VISIBLE);
                } else {
                    completeLayoutx.setVisibility(View.GONE);
                }

            }


        }

    public void clickme(View v) {

        int i = v.getId();
        if (i == R.id.ram) {
            presenter.startForm(contacts.get(0));
        } else if (i == R.id.profile){
            presenter.startForm(contacts.get(1));
        } else if (i == R.id.symptoms){
            presenter.startForm(contacts.get(2));
        } else if (i == R.id.exam){
            presenter.startForm(contacts.get(3));
        }else if (i == R.id.imaging){
            presenter.startForm(contacts.get(4));
        }else if (i == R.id.counselling){
            presenter.startForm(contacts.get(5));
        } else if (i == R.id.routine){
            mainlayout.setVisibility(View.GONE);
            middleLayout.setVisibility(View.VISIBLE);
        } else if (i == R.id.containerBack){
            mainlayout.setVisibility(View.VISIBLE);
            middleLayout.setVisibility(View.GONE);
        }
    }

    private int getRequiredCountTotal() {
        int count = -1;
        Set<Map.Entry<String, Integer>> entries = requiredFieldsMap.entrySet();
        //We count required fields for all the contact forms
        if (entries.size() == contactForms.length) {
            count++;
            for (Map.Entry<String, Integer> entry : entries) {
                count += entry.getValue();
            }
        }
        return count;
    }


    private void loadContactGlobalsConfig() throws IOException {
        Iterable<Object> contactGlobals = readYaml(FilePathUtils.FileUtils.CONTACT_GLOBALS);

        for (Object ruleObject : contactGlobals) {
            Map<String, Object> map = ((Map<String, Object>) ruleObject);
            formGlobalKeys.put(map.get(ConstantsUtils.FORM).toString(), (List<String>) map.get(JsonFormConstants.FIELDS));
            globalKeys.addAll((List<String>) map.get(JsonFormConstants.FIELDS));
        }
    }

    private void process(String[] mainContactForms) {
        //Fetch and load previously saved values
        try {
            if (contactNo > 1 && !PatientRepository.isFirstVisit(baseEntityId)) {
                for (String formEventType : new ArrayList<>(Arrays.asList(mainContactForms))) {
                    if (eventToFileMap.containsValue(formEventType)) {
                        updateGlobalValuesWithDefaults(formEventType);
                    }
                }
                //Get invisible required fields saved with the last contact visit
                for (String key : eventToFileMap.keySet()) {
                    String prevKey = JsonFormConstants.INVISIBLE_REQUIRED_FIELDS + "_" + key.toLowerCase().replace(" ", "_");
                    String invisibleFields = getMapValue(prevKey);
                    if (invisibleFields != null && invisibleFields.length() > 2) {
                        String toSplit = invisibleFields.substring(1, invisibleFields.length() - 1);
                        invisibleRequiredFields.addAll(Arrays.asList(toSplit.split(",")));
                    }
                }
                //Make profile always complete on second contact onwards
                requiredFieldsMap.put(ConstantsUtils.JsonFormUtils.ANC_PROFILE_ENCOUNTER_TYPE, 0);
                requiredFieldsMap.put(ConstantsUtils.JsonFormUtils.ANC_TEST_TASKS_ENCOUNTER_TYPE, 0);

            }

            JSONObject object;
            List<String> partialForms = new ArrayList<>(Arrays.asList(mainContactForms));
            List<PartialContact> partialContacts = getPartialContacts();

            if (partialContacts != null && partialContacts.size() > 0) {
                for (PartialContact partialContact : partialContacts) {
                    if (partialContact.getFormJsonDraft() != null || partialContact.getFormJson() != null) {
                        object = new JSONObject(partialContact.getFormJsonDraft() != null ? partialContact.getFormJsonDraft() : partialContact.getFormJson());
                        processRequiredStepsField(object);
                        if (object.has(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE)) {
                            partialForms.remove(eventToFileMap.get(object.getString(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, " --> process");
        }
    }

    private void setRequiredFields(Contact contact) {
        if (requiredFieldsMap != null && contact != null && requiredFieldsMap.containsKey(contact.getName())) {
            contact.setRequiredFields(requiredFieldsMap.get(contact.getName()));
        }
    }

    public Iterable<Object> readYaml(String filename) throws IOException {
        InputStreamReader inputStreamReader =
                new InputStreamReader(this.getAssets().open((FilePathUtils.FolderUtils.CONFIG_FOLDER_PATH + filename)));
        return yaml.loadAll(inputStreamReader);
    }

    /**
     * Get default values and set global values based on these keys
     *
     * @param formEventType list of forms
     */
    @SuppressLint("StaticFieldLeak")
    private void updateGlobalValuesWithDefaults(final String formEventType) {
        JSONObject mainJson;
        try {
            mainJson = ANCJsonFormUtils.readJsonFromAsset(getApplicationContext(), "json.form/" + formEventType);
            if (mainJson.has(ConstantsUtils.DEFAULT_VALUES)) {
                JSONArray defaultValuesArray = mainJson.getJSONArray(ConstantsUtils.DEFAULT_VALUES);
                defaultValueFields.addAll(getListValues(defaultValuesArray));
            }
        } catch (Exception e) {
            Timber.e(e, "Error reading json from asset file ");
        }

    }

    private String getMapValue(String key) {
        PreviousContact request = new PreviousContact();
        request.setBaseEntityId(baseEntityId);
        request.setKey(key);
        if (contactNo > 1) {
            request.setContactNo(String.valueOf(contactNo - 1));
        }

        PreviousContact previousContact = AncLibrary.getInstance().getPreviousContactRepository().getPreviousContact(request);
        return previousContact != null ? previousContact.getValue() : null;
    }

    public List<PartialContact> getPartialContacts() {
        return AncLibrary.getInstance().getPartialContactRepository().getPartialContacts(getIntent().getStringExtra(ConstantsUtils.IntentKeyUtils.BASE_ENTITY_ID), contactNo);
    }

    private void processRequiredStepsField(JSONObject object) throws Exception {
        if (object != null && object.has(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE)) {
            //initialize required fields map
            String encounterType = object.getString(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE);

            //Do not add defaults for test and CT contact containers unless they are opened
            if (!ConstantsUtils.JsonFormUtils.ANC_TEST_ENCOUNTER_TYPE.equals(encounterType) && (requiredFieldsMap.size() == 0 || !requiredFieldsMap.containsKey(encounterType))) {
                requiredFieldsMap.put(object.getString(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE), 0);
            }
            if (contactNo > 1 && ConstantsUtils.JsonFormUtils.ANC_PROFILE_ENCOUNTER_TYPE.equals(encounterType)
            && !PatientRepository.isFirstVisit(baseEntityId)) {
                requiredFieldsMap.put(ConstantsUtils.JsonFormUtils.ANC_PROFILE_ENCOUNTER_TYPE, 0);
            }

            if (ConstantsUtils.JsonFormUtils.ANC_TEST_TASKS_ENCOUNTER_TYPE.equals(encounterType)) {
                requiredFieldsMap.put(ConstantsUtils.JsonFormUtils.ANC_TEST_TASKS_ENCOUNTER_TYPE, 0);
            }

            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (key.startsWith(RuleConstant.STEP) && !object.getJSONObject(key).has("skipped")) {
                    JSONArray stepArray = object.getJSONObject(key).getJSONArray(JsonFormConstants.FIELDS);
                    for (int i = 0; i < stepArray.length(); i++) {
                        JSONObject fieldObject = stepArray.getJSONObject(i);
                        ANCFormUtils.processSpecialWidgets(fieldObject);
                        boolean isFieldVisible = getFieldVisibility(fieldObject);
                        boolean isRequiredField = isFieldVisible && ANCJsonFormUtils.isFieldRequired(fieldObject);
                        updateFieldRequiredCount(object, fieldObject, isRequiredField);
                        updateFormGlobalValues(fieldObject);
                        checkRequiredForSubForms(fieldObject, object);
                    }
                }
            }
        }
    }

    private List<String> getListValues(JSONArray jsonArray) {
        if (jsonArray != null) {
            return AncLibrary.getInstance().getGsonInstance()
                    .fromJson(jsonArray.toString(), new TypeToken<List<String>>() {
                    }.getType());
        } else {
            return new ArrayList<>();
        }
    }

    private boolean getFieldVisibility(JSONObject field) {
        boolean isVisible = true;
        try {
            if (field != null && field.has(JsonFormConstants.TYPE) && !JsonFormConstants.HIDDEN.equals(field.getString(JsonFormConstants.TYPE))) {
                if (field.has(JsonFormConstants.IS_VISIBLE) && !field.getBoolean(JsonFormConstants.IS_VISIBLE)) {
                    isVisible = false;
                }
            } else {
                isVisible = false;
            }
        } catch (JSONException e) {
            Timber.e(e, " --> getFieldVisibility");
        }
        return isVisible;
    }

    private void updateFieldRequiredCount(JSONObject object, JSONObject fieldObject, boolean isRequiredField) throws JSONException {
        if (isRequiredField && (!fieldObject.has(JsonFormConstants.VALUE) ||
                TextUtils.isEmpty(fieldObject.getString(JsonFormConstants.VALUE)))) {
            Integer requiredFieldCount = requiredFieldsMap.get(object.getString(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE));
            requiredFieldCount = requiredFieldCount == null ? 1 : ++requiredFieldCount;
            requiredFieldsMap.put(object.getString(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE), requiredFieldCount);
        }
    }

    private void updateFormGlobalValues(JSONObject fieldObject) throws Exception {
        if (globalKeys.contains(fieldObject.getString(JsonFormConstants.KEY)) &&
                fieldObject.has(JsonFormConstants.VALUE)) {

            formGlobalValues.put(fieldObject.getString(JsonFormConstants.KEY),
                    fieldObject.getString(JsonFormConstants.VALUE));//Normal value
            processAbnormalValues(formGlobalValues, fieldObject);

            String secKey = ANCFormUtils.getSecondaryKey(fieldObject);
            if (fieldObject.has(secKey)) {
                formGlobalValues.put(secKey, fieldObject.getString(secKey));//Normal value secondary key
            }

            if (fieldObject.has(ConstantsUtils.KeyUtils.SECONDARY_VALUES)) {
                fieldObject.put(ConstantsUtils.KeyUtils.SECONDARY_VALUES,
                        ANCFormUtils.sortSecondaryValues(fieldObject));//sort and reset
                JSONArray secondaryValues = fieldObject.getJSONArray(ConstantsUtils.KeyUtils.SECONDARY_VALUES);
                for (int j = 0; j < secondaryValues.length(); j++) {
                    JSONObject jsonObject = secondaryValues.getJSONObject(j);
                    processAbnormalValues(formGlobalValues, jsonObject);
                }
            }
            checkRequiredForCheckBoxOther(fieldObject);
        }
    }

    private void checkRequiredForSubForms(JSONObject fieldObject, JSONObject encounterObject) throws JSONException {
        if (fieldObject.has(JsonFormConstants.CONTENT_FORM)) {
            if ((fieldObject.has(JsonFormConstants.IS_VISIBLE) && !fieldObject.getBoolean(JsonFormConstants.IS_VISIBLE))) {
                return;
            }
            JSONArray requiredFieldsArray = fieldObject.has(ConstantsUtils.REQUIRED_FIELDS) ?
                    fieldObject.getJSONArray(ConstantsUtils.REQUIRED_FIELDS) : new JSONArray();
            updateSubFormRequiredCount(requiredFieldsArray, createAccordionValuesMap(fieldObject, encounterObject), encounterObject);
            updateFormGlobalValuesFromExpansionPanel(fieldObject);
        }
    }

    public static void processAbnormalValues(Map<String, String> facts, JSONObject jsonObject) throws Exception {
        String fieldKey = ANCFormUtils.getObjectKey(jsonObject);
        Object fieldValue = ANCFormUtils.getObjectValue(jsonObject);
        String fieldKeySecondary = fieldKey.contains(ConstantsUtils.SuffixUtils.OTHER) ?
                fieldKey.substring(0, fieldKey.indexOf(ConstantsUtils.SuffixUtils.OTHER)) + ConstantsUtils.SuffixUtils.VALUE : "";
        String fieldKeyOtherValue = fieldKey + ConstantsUtils.SuffixUtils.VALUE;

        if (fieldKey.endsWith(ConstantsUtils.SuffixUtils.OTHER) && !fieldKeySecondary.isEmpty() &&
                facts.get(fieldKeySecondary) != null && facts.get(fieldKeyOtherValue) != null) {

            List<String> tempList = new ArrayList<>(Arrays.asList(facts.get(fieldKeySecondary).split("\\s*,\\s*")));
            tempList.remove(tempList.size() - 1);
            tempList.add(StringUtils.capitalize(facts.get(fieldKeyOtherValue)));
            facts.put(fieldKeySecondary, ANCFormUtils.getListValuesAsString(tempList));

        } else {
            facts.put(fieldKey, fieldValue.toString());
        }

    }

    private void checkRequiredForCheckBoxOther(JSONObject fieldObject) throws Exception {
        //Other field for check boxes
        if (fieldObject.has(JsonFormConstants.VALUE) && !TextUtils.isEmpty(fieldObject.getString(JsonFormConstants.VALUE)) &&
                fieldObject.getString(ConstantsUtils.KeyUtils.KEY).endsWith(ConstantsUtils.SuffixUtils.OTHER) && formGlobalValues
                .get(fieldObject.getString(ConstantsUtils.KeyUtils.KEY).replace(ConstantsUtils.SuffixUtils.OTHER, ConstantsUtils.SuffixUtils.VALUE)) !=
                null) {

            formGlobalValues
                    .put(ANCFormUtils.getSecondaryKey(fieldObject), fieldObject.getString(JsonFormConstants.VALUE));
            processAbnormalValues(formGlobalValues, fieldObject);
        }
    }

    private void updateSubFormRequiredCount(JSONArray requiredAccordionFields, HashMap<String, JSONArray> accordionValuesMap,
                                            JSONObject encounterObject) throws JSONException {

        if (requiredAccordionFields.length() == 0) {
            return;
        }

        String encounterType = encounterObject.getString(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE);
        Integer requiredFieldCount = requiredFieldsMap.get(encounterType);

        for (int count = 0; count < requiredAccordionFields.length(); count++) {
            String item = requiredAccordionFields.getString(count);
            if (!accordionValuesMap.containsKey(item)) {
                requiredFieldCount = requiredFieldCount == null ? 1 : ++requiredFieldCount;
            }
        }

        if (requiredFieldCount != null) {
            requiredFieldsMap.put(encounterType, requiredFieldCount);
        }
    }

    private HashMap<String, JSONArray> createAccordionValuesMap(JSONObject accordionJsonObject, JSONObject encounterObject)
            throws JSONException {
        HashMap<String, JSONArray> accordionValuesMap = new HashMap<>();
        if (accordionJsonObject.has(JsonFormConstants.VALUE)) {
            String encounterType = encounterObject.getString(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_TYPE);
            //At this point we can now initialize required value for Test/CT since at least on of the expansion widget has a value
            if (requiredFieldsMap.size() == 0 || !requiredFieldsMap.containsKey(encounterType)) {
                requiredFieldsMap.put(encounterType, 0);
            }

            JSONArray accordionValues = accordionJsonObject.getJSONArray(JsonFormConstants.VALUE);
            for (int index = 0; index < accordionValues.length(); index++) {
                JSONObject item = accordionValues.getJSONObject(index);
                accordionValuesMap.put(item.getString(JsonFormConstants.KEY),
                        item.getJSONArray(JsonFormConstants.VALUES));
            }
            return accordionValuesMap;
        }
        return accordionValuesMap;
    }

    private void updateFormGlobalValuesFromExpansionPanel(JSONObject fieldObject) throws JSONException {
        if (fieldObject.has(JsonFormConstants.VALUE) && fieldObject.has(JsonFormConstants.TYPE)
                && TextUtils.equals(JsonFormConstants.EXPANSION_PANEL, fieldObject.getString(JsonFormConstants.TYPE))) {

            JSONArray accordionValue = fieldObject.getJSONArray(JsonFormConstants.VALUE);
            for (int count = 0; count < accordionValue.length(); count++) {
                JSONObject item = accordionValue.getJSONObject(count);
                JSONArray itemValueJSONArray = item.optJSONArray(JsonFormConstants.VALUES);
                if (itemValueJSONArray != null) {
                    String type = item.optString(JsonFormConstants.TYPE);
                    String itemValue = ANCFormUtils.extractItemValue(type, itemValueJSONArray);
                    if (globalKeys.contains(item.getString(JsonFormConstants.KEY))) {
                        formGlobalValues.put(item.getString(JsonFormConstants.KEY), itemValue);
                    }
                }
            }
        }
    }

    @Override
    protected void createContacts() {
        try {
            eventToFileMap.put(getString(R.string.quick_check), ConstantsUtils.JsonFormUtils.ANC_QUICK_CHECK);
            eventToFileMap.put(getString(R.string.profile), ConstantsUtils.JsonFormUtils.ANC_PROFILE);
            eventToFileMap.put(getString(R.string.physical_exam), ConstantsUtils.JsonFormUtils.ANC_PHYSICAL_EXAM);
            eventToFileMap.put(getString(R.string.tests), ConstantsUtils.JsonFormUtils.ANC_TEST);
            eventToFileMap.put(getString(R.string.counselling_treatment), ConstantsUtils.JsonFormUtils.ANC_COUNSELLING_TREATMENT);
            eventToFileMap.put(getString(R.string.symptoms_follow_up), ConstantsUtils.JsonFormUtils.ANC_SYMPTOMS_FOLLOW_UP);
        } catch (Exception e) {
            Timber.e(e, " --> createContacts");
        }
    }

    @Override
    protected void initializePresenter() {
        presenter = new ContactPresenter(this);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        patientNameView = findViewById(R.id.top_patient_name);
        AncLibrary.getInstance().populateGlobalSettings();
    }

    @Override
    public void startFormActivity(JSONObject form, Contact contact) {
        Set<String> hiddenFields = null;
        Set<String> disabledFields = null;
        try {
            JSONObject formConfig = com.vijay.jsonwizard.utils.Utils.getFormConfig(contact.getFormName(), JsonFormConstants.FORM_CONFIG_LOCATION, getApplicationContext());
            if (formConfig != null) {
                hiddenFields = com.vijay.jsonwizard.utils.Utils.convertJsonArrayToSet(formConfig.optJSONArray(JsonFormConstants.JSON_FORM_KEY.HIDDEN_FIELDS));
                disabledFields = com.vijay.jsonwizard.utils.Utils.convertJsonArrayToSet(formConfig.optJSONArray(JsonFormConstants.JSON_FORM_KEY.DISABLED_FIELDS));
            }
        } catch (JSONException | IOException e) {
            Timber.e(e);
        }
        contact.setHiddenFields(hiddenFields);
        contact.setDisabledFields(disabledFields);

        super.startFormActivity(form, contact);
    }

    @Override
    protected String getFormJson(PartialContact partialContactRequest, JSONObject form) {
        try {
            JSONObject object = ANCFormUtils.getFormJsonCore(partialContactRequest, form);
            preProcessDefaultValues(object);
            return object.toString();
        } catch (Exception e) {
            Timber.e(e, " --> getFormJson");
        }
        return "";
    }

    private void preProcessDefaultValues(JSONObject object) {
        try {
            if (object != null) {
                Iterator<String> keys = object.keys();

                while (keys.hasNext()) {
                    String key = keys.next();

                    if (ConstantsUtils.GLOBAL_PREVIOUS.equals(key)) {
                        JSONArray globalPreviousValues = object.getJSONArray(key);
                        globalValueFields = getListValues(globalPreviousValues);
                    }

                    if (ConstantsUtils.EDITABLE_FIELDS.equals(key)) {
                        JSONArray editableFieldValues = object.getJSONArray(key);
                        editableFields = getListValues(editableFieldValues);
                    }

                    if (key.startsWith(RuleConstant.STEP)) {
                        JSONArray stepArray = object.getJSONObject(key).getJSONArray(JsonFormConstants.FIELDS);

                        for (int i = 0; i < stepArray.length(); i++) {
                            JSONObject fieldObject = stepArray.getJSONObject(i);
                            updateDefaultValues(stepArray, i, fieldObject);
                        }
                    }
                }
                getValueMap(object);
            }
        } catch (JSONException e) {
            Timber.e(e, " --> preProcessDefaultValues");
        }
    }

    private void updateDefaultValues(JSONArray stepArray, int i, JSONObject fieldObject) throws JSONException {
        if (defaultValueFields.contains(fieldObject.getString(JsonFormConstants.KEY))) {

            if (!fieldObject.has(JsonFormConstants.VALUE) ||
                    TextUtils.isEmpty(fieldObject.getString(JsonFormConstants.VALUE))) {

                String defaultKey = fieldObject.getString(JsonFormConstants.KEY);
                String mapValue = getMapValue(defaultKey);

                if (mapValue != null) {
                    fieldObject.put(JsonFormConstants.VALUE, mapValue);
                    fieldObject.put(JsonFormConstants.EDITABLE, editableFields.contains(defaultKey));
                    fieldObject.put(JsonFormConstants.READ_ONLY, editableFields.contains(defaultKey));
                }

            }

            if (fieldObject.has(JsonFormConstants.OPTIONS_FIELD_NAME)) {
                boolean addDefaults = true;

                for (int m = 0; m < fieldObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME).length(); m++) {
                    String optionValue;
                    if (fieldObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME).getJSONObject(m)
                            .has(JsonFormConstants.VALUE)) {
                        optionValue = fieldObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME).getJSONObject(m)
                                .getString(JsonFormConstants.VALUE);
                        if (ConstantsUtils.BooleanUtils.TRUE.equals(optionValue)) {
                            addDefaults = false;
                            break;
                        }
                    }
                }

                if (addDefaults && fieldObject.getString(JsonFormConstants.TYPE).equals(JsonFormConstants.CHECK_BOX) &&
                        fieldObject.has(JsonFormConstants.VALUE)) {
                    List<String> values = Arrays.asList(fieldObject.getString(JsonFormConstants.VALUE)
                            .substring(1, fieldObject.getString(JsonFormConstants.VALUE).length() - 1).split(", "));

                    for (int m = 0; m < fieldObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME).length(); m++) {

                        if (values.contains(fieldObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME).getJSONObject(m)
                                .getString(JsonFormConstants.KEY))) {
                            stepArray.getJSONObject(i).getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME).getJSONObject(m)
                                    .put(JsonFormConstants.VALUE, true);
                            fieldObject.put(JsonFormConstants.EDITABLE,
                                    editableFields.contains(fieldObject.getString(JsonFormConstants.KEY)));
                            fieldObject.put(JsonFormConstants.READ_ONLY,
                                    editableFields.contains(fieldObject.getString(JsonFormConstants.KEY)));
                        }

                    }
                }
            }
        }
    }

    private void getValueMap(JSONObject object) throws JSONException {
        initializeGlobalPreviousValues(object);
        for (int i = 0; i < globalValueFields.size(); i++) {
            String mapValue = getMapValue(globalValueFields.get(i));
            if (mapValue != null) {
                if (object.has(JsonFormConstants.JSON_FORM_KEY.GLOBAL)) {
                    object.getJSONObject(JsonFormConstants.JSON_FORM_KEY.GLOBAL)
                            .put(ConstantsUtils.PrefixUtils.PREVIOUS + globalValueFields.get(i), mapValue);
                } else {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(ConstantsUtils.PrefixUtils.PREVIOUS + globalValueFields.get(i), mapValue);
                    object.put(JsonFormConstants.JSON_FORM_KEY.GLOBAL, jsonObject);
                }
            }

        }
    }

    /***
     * This method initializes all global_previous values with empty strings
     * @param object Form Json object
     * @throws JSONException
     */
    private void initializeGlobalPreviousValues(JSONObject object) throws JSONException {
        if (object.has(ConstantsUtils.GLOBAL_PREVIOUS)) {
            JSONArray globalPreviousArray = object.getJSONArray(ConstantsUtils.GLOBAL_PREVIOUS);
            for (int i = 0; i < globalPreviousArray.length(); i++) {
                if (object.has(JsonFormConstants.JSON_FORM_KEY.GLOBAL)) {
                    object.getJSONObject(JsonFormConstants.JSON_FORM_KEY.GLOBAL)
                            .put(ConstantsUtils.PrefixUtils.PREVIOUS + globalPreviousArray.getString(i), "");
                }
            }
        }
    }

    @Override
    public void displayPatientName(String patientName) {
        if (patientNameView != null && StringUtils.isNotBlank(patientName)) {
            patientNameView.setText(patientName);
        }
    }

    @Override
    public void displayToast(int resourceId) {
        Utils.showToast(getApplicationContext(), getString(resourceId));
    }

    @Override
    public void loadGlobals(Contact contact) {
        //Update global for fields that are default for contact greater than 1
        updateGlobalFieldsForContactAbove1();

        List<String> contactGlobals = formGlobalKeys.get(contact.getFormName());

        if (contactGlobals != null) {
            Map<String, String> map = new HashMap<>();
            addGlobalsToAMap(contactGlobals, map);

            //Inject some form defaults from client details
            map.put(ConstantsUtils.KeyUtils.CONTACT_NO, contactNo.toString());
            map.put(ConstantsUtils.PREVIOUS_CONTACT_NO, contactNo > 1 ? String.valueOf(contactNo - 1) : "0");
            map.put(ConstantsUtils.AGE, womanAge);

            updateFirstContactFlag(map);
            addGAWhenNotCalculated(map);
            addLastContactDate(map);

            contact.setGlobals(map);
        }

    }

    private void addLastContactDate(Map<String, String> map) {
        String lastContactDate =
                ((HashMap<String, String>) getIntent().getSerializableExtra(ConstantsUtils.IntentKeyUtils.CLIENT_MAP))
                        .get(DBConstantsUtils.KeyUtils.LAST_CONTACT_RECORD_DATE);
        map.put(ConstantsUtils.KeyUtils.LAST_CONTACT_DATE,
                !TextUtils.isEmpty(lastContactDate) ? Utils.reverseHyphenSeperatedValues(lastContactDate, "-") : "");
    }

    private void addGAWhenNotCalculated(Map<String, String> map) {
        //Inject gestational age when it has not been calculated from profile form
        if (TextUtils.isEmpty(formGlobalValues.get(ConstantsUtils.GEST_AGE_OPENMRS))) {
            map.put(ConstantsUtils.GEST_AGE_OPENMRS, String.valueOf(presenter.getGestationAge()));
        }
    }

    private void updateFirstContactFlag(Map<String, String> map) {
        if (ConstantsUtils.DueCheckStrategy.CHECK_FOR_FIRST_CONTACT.equals(Utils.getDueCheckStrategy())) {
            map.put(ConstantsUtils.IS_FIRST_CONTACT, String.valueOf(PatientRepository.isFirstVisit(baseEntityId)));
        }
    }

    private void addGlobalsToAMap(List<String> contactGlobals, Map<String, String> map) {
        for (String contactGlobal : contactGlobals) {
            if (formGlobalValues.containsKey(contactGlobal)) {
                String some = map.get(contactGlobal);

                if (some == null || !some.equals(formGlobalValues.get(contactGlobal))) {
                    map.put(contactGlobal, formGlobalValues.get(contactGlobal));
                }

            } else {
                map.put(contactGlobal, "");
            }
        }
    }

    private void updateGlobalFieldsForContactAbove1() {
        if (contactNo > 1) {
            for (String item : defaultValueFields) {
                if (globalKeys.contains(item)) {
                    formGlobalValues.put(item, getMapValue(item));
                }
            }
        }
    }

    @Override
    protected void onCreation() {//Overridden
    }

    @Override
    protected void onResumption() {//Overridden from Secured Activity

    }

    @Override
    protected void onPause() {
        super.onPause();
        formGlobalValues.clear();
        invisibleRequiredFields.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            formInvalidFields = data.getStringExtra("formInvalidFields");
        }

        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }
}
