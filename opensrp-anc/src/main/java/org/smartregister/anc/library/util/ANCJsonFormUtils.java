package org.smartregister.anc.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;

import com.google.common.reflect.TypeToken;
import com.vijay.jsonwizard.activities.FormConfigurationJsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.jeasy.rules.api.Facts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.anc.library.AncLibrary;
//import org.smartregister.anc.library.BuildConfig;
import org.smartregister.anc.library.R;
import org.smartregister.anc.library.activity.EditJsonFormActivity;
import org.smartregister.anc.library.domain.YamlConfigItem;
import org.smartregister.anc.library.domain.YamlConfigWrapper;
import org.smartregister.anc.library.model.ContactSummaryModel;
import org.smartregister.anc.library.model.Task;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.FormEntityConstants;
import org.smartregister.domain.Photo;
import org.smartregister.domain.ProfileImage;
import org.smartregister.domain.form.FormLocation;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.ImageRepository;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.FormUtils;
import org.smartregister.util.ImageUtils;
import org.smartregister.view.LocationPickerView;
import org.smartregister.view.activity.DrishtiApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import timber.log.Timber;

/**
 * Created by keyman on 27/06/2018.
 */
public class ANCJsonFormUtils extends org.smartregister.util.JsonFormUtils {
    public static final String METADATA = "metadata";
    public static final String ENCOUNTER_TYPE = "encounter_type";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    public static final SimpleDateFormat EDD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String READ_ONLY = "read_only";
    public static final int REQUEST_CODE_GET_JSON = 3432;
    private static final String TAG = ANCJsonFormUtils.class.getCanonicalName();

    public static boolean isFieldRequired(JSONObject fieldObject) throws JSONException {
        boolean isValueRequired = false;
        if (fieldObject.has(JsonFormConstants.V_REQUIRED)) {
            JSONObject valueRequired = fieldObject.getJSONObject(JsonFormConstants.V_REQUIRED);
            String value = valueRequired.getString(JsonFormConstants.VALUE);
            isValueRequired = Boolean.parseBoolean(value);
        }
        //Don't check required for hidden, toaster notes, spacer and label widgets
        return (!fieldObject.getString(JsonFormConstants.TYPE).equals(JsonFormConstants.LABEL) &&
                !fieldObject.getString(JsonFormConstants.TYPE).equals(JsonFormConstants.SPACER) &&
                !fieldObject.getString(JsonFormConstants.TYPE).equals(JsonFormConstants.TOASTER_NOTES) &&
                !fieldObject.getString(JsonFormConstants.TYPE).equals(JsonFormConstants.HIDDEN)) &&
                isValueRequired;
    }

    public static JSONObject getFormAsJson(JSONObject form, String formName, String id, String currentLocationId)
            throws Exception {
        if (form == null) {
            return null;
        }

        String entityId = id;
        form.getJSONObject(METADATA).put(ANCJsonFormUtils.ENCOUNTER_LOCATION, currentLocationId);

        if (ConstantsUtils.JsonFormUtils.ANC_REGISTER.equals(formName)) {
            if (StringUtils.isNotBlank(entityId)) {
                entityId = entityId.replace("-", "");
            }

            addRegistrationLocationHierarchyQuestions(form);
            // Inject opensrp id into the form
            JSONArray field = ANCJsonFormUtils.fields(form);
            JSONObject ancId = getFieldJSONObject(field, ConstantsUtils.JsonFormKeyUtils.ANC_ID);
            JSONObject studyId = getFieldJSONObject(field, ConstantsUtils.JsonFormKeyUtils.STUDY_ID);
            if (ancId != null) {
                ancId.remove(ANCJsonFormUtils.VALUE);
                ancId.put(ANCJsonFormUtils.VALUE, entityId);

                long RandomStudyID = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                //String randomGeneratedStudyID = RandomStudyID.toString();

                System.out.println("Random UUID String = " + randomUUIDString);
                System.out.println("UUID version       = " + uuid.version());
                System.out.println("UUID variant       = " + uuid.variant());


                studyId.put(ANCJsonFormUtils.VALUE, RandomStudyID);
            }

        } else if (ConstantsUtils.JsonFormUtils.ANC_CLOSE.equals(formName)) {
            if (StringUtils.isNotBlank(entityId)) {
                // Inject entity id into the remove form
                form.remove(ANCJsonFormUtils.ENTITY_ID);
                form.put(ENTITY_ID, entityId);
            }
        } else {
            Timber.tag(TAG).w("Unsupported form requested for launch %s", formName);
        }
        Timber.d("form is %s", form.toString());
        return form;
    }

    private static void addRegistrationLocationHierarchyQuestions(@NonNull JSONObject form) {
        try {
            JSONArray fields = com.vijay.jsonwizard.utils.FormUtils.getMultiStepFormFields(form);
            AncMetadata metadata = AncLibrary.getInstance().getAncMetadata();
            ArrayList<String> allLevels = metadata.getLocationLevels();
            ArrayList<String> healthFacilities = metadata.getHealthFacilityLevels();
            List<String> defaultLocation = LocationHelper.getInstance().generateDefaultLocationHierarchy(allLevels);
            List<String> defaultFacility = LocationHelper.getInstance().generateDefaultLocationHierarchy(healthFacilities);
            List<FormLocation> entireTree = LocationHelper.getInstance().generateLocationHierarchyTree(true, allLevels);
            String defaultLocationString = AssetHandler.javaToJsonString(defaultLocation, new TypeToken<List<String>>() {
            }.getType());

            String defaultFacilityString = AssetHandler.javaToJsonString(defaultFacility, new TypeToken<List<String>>() {
            }.getType());

            String entireTreeString = AssetHandler.javaToJsonString(entireTree, new TypeToken<List<FormLocation>>() {
            }.getType());

            updateLocationTree(fields, defaultLocationString, defaultFacilityString, entireTreeString);
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> addRegLocHierarchyQuestions");
        }
    }

    private static void updateLocationTree(@NonNull JSONArray fields,
                                           @NonNull String defaultLocationString,
                                           @NonNull String defaultFacilityString,
                                           @NonNull String entireTreeString) {
        AncMetadata ancMetadata = AncLibrary.getInstance().getAncMetadata();
        if (ancMetadata.getFieldsWithLocationHierarchy() != null && !ancMetadata.getFieldsWithLocationHierarchy().isEmpty()) {
            for (int i = 0; i < fields.length(); i++) {
                JSONObject widget = fields.optJSONObject(i);
                if (ancMetadata.getFieldsWithLocationHierarchy().contains(widget.optString(JsonFormConstants.KEY))) {
                    if (StringUtils.isNotBlank(entireTreeString)) {
                        addLocationTree(widget.optString(JsonFormConstants.KEY), widget, entireTreeString, JsonFormConstants.TREE);
                    }
                    if (StringUtils.isNotBlank(defaultFacilityString)) {
                        addLocationTreeDefault(widget.optString(JsonFormConstants.KEY), widget, defaultLocationString);
                    }
                }
            }
        }
    }

    private static void addLocationTree(@NonNull String widgetKey, @NonNull JSONObject widget, @NonNull String updateString, @NonNull String treeType) {
        try {
            if (widget.optString(JsonFormConstants.KEY).equals(widgetKey)) {
                widget.put(treeType, new JSONArray(updateString));
            }
        } catch (JSONException e) {
            Timber.e(e, "JsonFormUtils --> addLocationTree");
        }
    }

    private static void addLocationTreeDefault(@NonNull String widgetKey, @NonNull JSONObject widget, @NonNull String updateString) {
        addLocationTree(widgetKey, widget, updateString, JsonFormConstants.DEFAULT);
    }

    public static JSONObject getFieldJSONObject(JSONArray jsonArray, String key) {
        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = ANCJsonFormUtils.getJSONObject(jsonArray, i);
            String keyVal = ANCJsonFormUtils.getString(jsonObject, ANCJsonFormUtils.KEY);
            if (keyVal != null && keyVal.equals(key)) {
                return jsonObject;
            }
        }
        return null;
    }

    public static Pair<Client, Event> processRegistrationForm(AllSharedPreferences allSharedPreferences, String jsonString) {
        try {
            Triple<Boolean, JSONObject, JSONArray> registrationFormParams = validateParameters(jsonString);

            if (!registrationFormParams.getLeft()) {
                return null;
            }

            JSONObject jsonForm = registrationFormParams.getMiddle();
            JSONArray fields = registrationFormParams.getRight();

            String entityId = getEntityId(jsonForm);
            String encounterType = ANCJsonFormUtils.getString(jsonForm, ENCOUNTER_TYPE);
            JSONObject metadata = ANCJsonFormUtils.getJSONObject(jsonForm, METADATA);
            addLastInteractedWith(fields);
            getDobStrings(fields);
            String previousVisitsMap = initializeFirstContactValues(fields);
            processLocationFields(fields);

            FormTag formTag = getFormTag(allSharedPreferences);

            Client baseClient = org.smartregister.util.JsonFormUtils.createBaseClient(fields, formTag, entityId);
            Event baseEvent = org.smartregister.util.JsonFormUtils
                    .createEvent(fields, metadata, formTag, entityId, encounterType, DBConstantsUtils.DEMOGRAPHIC_TABLE_NAME);

            if (previousVisitsMap != null) {
                baseEvent.addDetails(ConstantsUtils.JsonFormKeyUtils.PREVIOUS_VISITS_MAP, previousVisitsMap);
            }
            tagSyncMetadata(allSharedPreferences, baseEvent);// tag docs

            return Pair.create(baseClient, baseEvent);
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> processRegistrationForm");
            return null;
        }
    }

    protected static void processLocationFields(@NonNull JSONArray fields) throws JSONException {
        for (int i = 0; i < fields.length(); i++) {
            if (fields.optJSONObject(i).has(JsonFormConstants.TYPE) &&
                    fields.optJSONObject(i).optString(JsonFormConstants.TYPE).equals(JsonFormConstants.TREE))
                try {
                    String rawValue = fields.optJSONObject(i).optString(JsonFormConstants.VALUE);
                    if (StringUtils.isNotBlank(rawValue)) {
                        JSONArray valueArray = new JSONArray(rawValue);
                        if (valueArray.length() > 0) {
                            String lastLocationName = valueArray.optString(valueArray.length() - 1);
                            String lastLocationId = LocationHelper.getInstance().getOpenMrsLocationId(lastLocationName);
                            fields.optJSONObject(i).put(JsonFormConstants.VALUE, lastLocationId);
                        }
                    }
                } catch (NullPointerException e) {
                    Timber.e(e);
                } catch (IllegalArgumentException e) {
                    Timber.e(e);
                }
        }
    }

    public static Triple<Boolean, JSONObject, JSONArray> validateParameters(String jsonString) {

        JSONObject jsonForm = ANCJsonFormUtils.toJSONObject(jsonString);
        JSONArray fields = ANCJsonFormUtils.fields(jsonForm);

        return Triple.of(jsonForm != null && fields != null, jsonForm, fields);
    }

    @NotNull
    private static String getEntityId(JSONObject jsonForm) {
        String entityId = ANCJsonFormUtils.getString(jsonForm, ANCJsonFormUtils.ENTITY_ID);
        if (StringUtils.isBlank(entityId)) {
            entityId = ANCJsonFormUtils.generateRandomUUIDString();
        }
        return entityId;
    }

    private static void addLastInteractedWith(JSONArray fields) throws JSONException {
        JSONObject lastInteractedWith = new JSONObject();
        lastInteractedWith.put(ConstantsUtils.KeyUtils.KEY, DBConstantsUtils.KeyUtils.LAST_INTERACTED_WITH);
        lastInteractedWith.put(ConstantsUtils.KeyUtils.VALUE, Calendar.getInstance().getTimeInMillis());
        fields.put(lastInteractedWith);
    }

    private static void getDobStrings(JSONArray fields) throws JSONException {
        JSONObject dobUnknownObject = getFieldJSONObject(fields, DBConstantsUtils.KeyUtils.DOB_UNKNOWN);
        JSONArray options = ANCJsonFormUtils.getJSONArray(dobUnknownObject, ConstantsUtils.JsonFormKeyUtils.OPTIONS);
        JSONObject option = ANCJsonFormUtils.getJSONObject(options, 0);
        String dobUnKnownString = option != null ? option.getString(ANCJsonFormUtils.VALUE) : null;

        if (StringUtils.isNotBlank(dobUnKnownString)) {
            dobUnknownObject.put(ANCJsonFormUtils.VALUE, Boolean.valueOf(dobUnKnownString) ? 1 : 0);
        }
    }

    /***
     * Initializes the values in the mother details table used by contact containers
     * @param fields {@link JSONArray}
     * @return
     * @throws JSONException
     */
    private static String initializeFirstContactValues(@NonNull JSONArray fields) throws JSONException {
        String strGroup = null;

        int nextContact = 1;

        String nextContactDate = Utils.convertDateFormat(Calendar.getInstance().getTime(), Utils.DB_DF);

        if (ConstantsUtils.DueCheckStrategy.CHECK_FOR_FIRST_CONTACT.equals(Utils.getDueCheckStrategy())) {
            HashMap<String, HashMap<String, String>> previousVisitsMap = Utils.buildRepeatingGroupValues(fields, ConstantsUtils.JsonFormKeyUtils.PREVIOUS_VISITS);
            if (!previousVisitsMap.isEmpty()) {

                nextContact = previousVisitsMap.size() + 1;

                strGroup = ANCJsonFormUtils.gson.toJson(previousVisitsMap);

                Set<Map.Entry<String, HashMap<String, String>>> previousVisitsMapSet = previousVisitsMap.entrySet();

                HashMap<String, String> previousVisitsMapItem = new LinkedHashMap<>();

                for (Map.Entry<String, HashMap<String, String>> entry : previousVisitsMapSet) {
                    previousVisitsMapItem = entry.getValue();
                }

                JSONObject lastContactDateJSONObject = getFieldJSONObject(fields, DBConstantsUtils.KeyUtils.LAST_CONTACT_RECORD_DATE);
                lastContactDateJSONObject.put(ANCJsonFormUtils.VALUE, previousVisitsMapItem.get(ConstantsUtils.JsonFormKeyUtils.VISIT_DATE));
            }
        }
        JSONObject nextContactJSONObject = getFieldJSONObject(fields, DBConstantsUtils.KeyUtils.NEXT_CONTACT);
        if (nextContactJSONObject.has(JsonFormConstants.VALUE) &&
                "".equals(nextContactJSONObject.getString(JsonFormConstants.VALUE))) {
            nextContactJSONObject.put(ANCJsonFormUtils.VALUE, nextContact);
        }

        JSONObject nextContactDateJSONObject = getFieldJSONObject(fields, DBConstantsUtils.KeyUtils.NEXT_CONTACT_DATE);
        if (nextContactDateJSONObject.has(JsonFormConstants.VALUE) &&
                "".equals(nextContactDateJSONObject.getString(JsonFormConstants.VALUE))) {
            nextContactDateJSONObject.put(ANCJsonFormUtils.VALUE, nextContactDate);
        }

        return strGroup;
    }


    @NotNull
    public static FormTag getFormTag(AllSharedPreferences allSharedPreferences) {
        FormTag formTag = new FormTag();
        formTag.providerId = allSharedPreferences.fetchRegisteredANM();
        formTag.appVersion = 10614;//BuildConfig.VERSION_CODE
        formTag.databaseVersion = AncLibrary.getInstance().getDatabaseVersion();
        return formTag;
    }

    public static void tagSyncMetadata(AllSharedPreferences allSharedPreferences, Event event) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        event.setProviderId(providerId);
        event.setLocationId(allSharedPreferences.fetchDefaultLocalityId(providerId));
        event.setChildLocationId(getChildLocationId(event.getLocationId(), allSharedPreferences));
        event.setTeam(allSharedPreferences.fetchDefaultTeam(providerId));
        event.setTeamId(allSharedPreferences.fetchDefaultTeamId(providerId));
        //event.setVersion(BuildConfig.EVENT_VERSION);
        event.setClientApplicationVersion(Integer.valueOf("10614"));//BuildConfig.VERSION_CODE
        event.setClientDatabaseVersion(AncLibrary.getInstance().getDatabaseVersion());
    }

    @Nullable
    public static String getChildLocationId(@NonNull String defaultLocationId, @NonNull AllSharedPreferences allSharedPreferences) {
        String currentLocality = allSharedPreferences.fetchCurrentLocality();

        if (StringUtils.isNotBlank(currentLocality)) {
            String currentLocalityId = LocationHelper.getInstance().getOpenMrsLocationId(currentLocality);
            if (StringUtils.isNotBlank(currentLocalityId) && !defaultLocationId.equals(currentLocalityId)) {
                return currentLocalityId;
            }
        }

        return null;
    }

    public static void mergeAndSaveClient(Client baseClient) throws Exception {
        JSONObject updatedClientJson = new JSONObject(org.smartregister.util.JsonFormUtils.gson.toJson(baseClient));

        JSONObject originalClientJsonObject =
                AncLibrary.getInstance().getEcSyncHelper().getClient(baseClient.getBaseEntityId());

        JSONObject mergedJson = org.smartregister.util.JsonFormUtils.merge(originalClientJsonObject, updatedClientJson);

        //TODO Save edit log ?

        AncLibrary.getInstance().getEcSyncHelper().addClient(baseClient.getBaseEntityId(), mergedJson);
    }

    public static void saveImage(String providerId, String entityId, String imageLocation) {
        OutputStream outputStream = null;
        try {
            if (StringUtils.isBlank(imageLocation)) {
                return;
            }

            File file = FileUtil.createFileFromPath(imageLocation);
            if (!file.exists()) {
                return;
            }

            Bitmap compressedBitmap = AncLibrary.getInstance().getCompressor().compressToBitmap(file);
            if (compressedBitmap == null || StringUtils.isBlank(providerId) || StringUtils.isBlank(entityId)) {
                return;
            }

            if (!entityId.isEmpty()) {
                final String absoluteFileName = DrishtiApplication.getAppDir() + File.separator + entityId + ".JPEG";

                File outputFile = FileUtil.createFileFromPath(absoluteFileName);
                outputStream = FileUtil.createFileOutputStream(outputFile);
                Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
                compressedBitmap.compress(compressFormat, 100, outputStream);
                // insert into the db
                ProfileImage profileImage = getProfileImage(providerId, entityId, absoluteFileName);
                ImageRepository imageRepository = AncLibrary.getInstance().getContext().imageRepository();
                imageRepository.add(profileImage);
            }

        } catch (FileNotFoundException e) {
            Timber.e("Failed to save static image to disk");
        } catch (IOException e) {
            Timber.e(e, " --> saveImage");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Timber.e("Failed to close static images output stream after attempting to write image");
                }
            }
        }
    }

    @NotNull
    private static ProfileImage getProfileImage(String providerId, String entityId, String absoluteFileName) {
        ProfileImage profileImage = new ProfileImage();
        profileImage.setImageid(UUID.randomUUID().toString());
        profileImage.setAnmId(providerId);
        profileImage.setEntityID(entityId);
        profileImage.setFilepath(absoluteFileName);
        profileImage.setFilecategory(ConstantsUtils.FileCategoryUtils.PROFILE_PIC);
        profileImage.setSyncStatus(ImageRepository.TYPE_Unsynced);
        return profileImage;
    }

    public static String getString(String jsonString, String field) {
        return ANCJsonFormUtils.getString(ANCJsonFormUtils.toJSONObject(jsonString), field);
    }

    public static String getFieldValue(String jsonString, String key) {
        JSONObject jsonForm = ANCJsonFormUtils.toJSONObject(jsonString);
        if (jsonForm == null) {
            return null;
        }

        JSONArray fields = ANCJsonFormUtils.fields(jsonForm);
        if (fields == null) {
            return null;
        }

        return ANCJsonFormUtils.getFieldValue(fields, key);

    }

    public static String getAutoPopulatedJsonEditRegisterFormString(Context context, Map<String, String> womanClient) {
        try {
            JSONObject form = FormUtils.getInstance(context).getFormJson(ConstantsUtils.JsonFormUtils.ANC_REGISTER);
            LocationPickerView lpv = createLocationPickerView(context);
            if (lpv != null) {
                lpv.init();
            }

            Timber.d("Form is %s", form.toString());
            if (form != null) {
                form.put(ANCJsonFormUtils.ENTITY_ID, womanClient.get(DBConstantsUtils.KeyUtils.BASE_ENTITY_ID));
                form.put(ANCJsonFormUtils.ENCOUNTER_TYPE, ConstantsUtils.EventTypeUtils.UPDATE_REGISTRATION);

                JSONObject metadata = form.getJSONObject(ANCJsonFormUtils.METADATA);
                String lastLocationId =
                        lpv != null ? LocationHelper.getInstance().getOpenMrsLocationId(lpv.getSelectedItem()) : "";

                metadata.put(ANCJsonFormUtils.ENCOUNTER_LOCATION, lastLocationId);

                form.put(ConstantsUtils.CURRENT_OPENSRP_ID, womanClient.get(DBConstantsUtils.KeyUtils.ANC_ID).replace("-", ""));

                //inject opensrp id into the form
                JSONObject stepOne = form.getJSONObject(ANCJsonFormUtils.STEP1);
                JSONArray jsonArray = stepOne.getJSONArray(ANCJsonFormUtils.FIELDS);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    processPopulatableFields(womanClient, jsonObject);

                }

                return form.toString();
            }
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> getAutoPopulatedJsonEditRegisterFormString");
        }

        return "";
    }

    private static LocationPickerView createLocationPickerView(Context context) {
        try {
            return new LocationPickerView(context);
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> createLocationPickerView");
            return null;
        }
    }

    protected static void processPopulatableFields(Map<String, String> womanClient, JSONObject jsonObject)
            throws JSONException {

        AncMetadata ancMetadata = AncLibrary.getInstance().getAncMetadata();

        if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(ConstantsUtils.JsonFormKeyUtils.DOB_ENTERED)) {
            getDobUsingEdd(womanClient, jsonObject, DBConstantsUtils.KeyUtils.DOB);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.HOME_ADDRESS)) {
            String homeAddress = womanClient.get(DBConstantsUtils.KeyUtils.HOME_ADDRESS);
            jsonObject.put(ANCJsonFormUtils.VALUE, homeAddress);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.ORIGIN)) {
            String origin = womanClient.get(DBConstantsUtils.KeyUtils.ORIGIN);
            jsonObject.put(ANCJsonFormUtils.VALUE, origin);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.OTHER_RELATIONS)) {
            String otherRelationsToClient = womanClient.get(DBConstantsUtils.KeyUtils.OTHER_RELATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, otherRelationsToClient);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.NRC_NUMBER)) {
            String nrcNumber = womanClient.get(DBConstantsUtils.KeyUtils.NRC_NUMBER);
            jsonObject.put(ANCJsonFormUtils.VALUE, nrcNumber);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.STUDY_ID)) {
            String studyID = womanClient.get(DBConstantsUtils.KeyUtils.STUDY_ID);
            jsonObject.put(ANCJsonFormUtils.VALUE, studyID);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.RELATION_NK)) {
            String relationNK = womanClient.get(DBConstantsUtils.KeyUtils.RELATION_NK);
            jsonObject.put(ANCJsonFormUtils.VALUE, relationNK);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.ALT_PHONE_NUMBER)) {
            String altnumber = womanClient.get(DBConstantsUtils.KeyUtils.ALT_PHONE_NUMBER);
            jsonObject.put(ANCJsonFormUtils.VALUE, altnumber);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PHONE_NUMBER)) {
            String phoneNumber = womanClient.get(DBConstantsUtils.KeyUtils.PHONE_NUMBER);
            jsonObject.put(ANCJsonFormUtils.VALUE, phoneNumber);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREV_PREG_PROBS)) {
            String prevPregProbs = womanClient.get(DBConstantsUtils.KeyUtils.PREV_PREG_PROBS);
            jsonObject.put(ANCJsonFormUtils.VALUE, prevPregProbs);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREV_PREG_COMPS)) {
            String prevPregComps = womanClient.get(DBConstantsUtils.KeyUtils.PREV_PREG_COMPS);
            jsonObject.put(ANCJsonFormUtils.VALUE, prevPregComps);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.OCCUPATION)) {
            String occupation = womanClient.get(DBConstantsUtils.KeyUtils.OCCUPATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, occupation);

        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.OCCUPATION_REDACTED)) {
            String occupationRedacted = womanClient.get(DBConstantsUtils.KeyUtils.OCCUPATION_REDACTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, occupationRedacted);

        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.EDUC_LEVEL)) {
            String educLevel = womanClient.get(DBConstantsUtils.KeyUtils.EDUC_LEVEL);
            jsonObject.put(ANCJsonFormUtils.VALUE, educLevel);

        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.COUPLE)) {
            String couple = womanClient.get(DBConstantsUtils.KeyUtils.COUPLE);
            jsonObject.put(ANCJsonFormUtils.VALUE, couple);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.MARITAL_STATUS)) {
            String maritalStatus = womanClient.get(DBConstantsUtils.KeyUtils.MARITAL_STATUS);
            jsonObject.put(ANCJsonFormUtils.VALUE, maritalStatus);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(ConstantsUtils.WOM_IMAGE)) {
            getPhotoFieldValue(womanClient, jsonObject);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.GRAVIDA)) {
            String gravida = womanClient.get(DBConstantsUtils.KeyUtils.GRAVIDA);
            jsonObject.put(ANCJsonFormUtils.VALUE, gravida);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ONE_COMPLICATIONS)) {
            String pregOneComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ONE_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ONE_BIRTH_WEIGHT)) {
            String pregOneBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ONE_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ONE_SEX_INFANT)) {
            String pregOneSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ONE_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ONE_OUTCOME_EARLY)) {
            String pregOneOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ONE_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ONE_OUTCOME)) {
            String pregOneOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ONE_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.LABOUR_TYPE)) {
            String pregOneLabourType = womanClient.get(DBConstantsUtils.KeyUtils.LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE)) {
            String cTypeOne = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeOne);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.TYPE_OF_ASSISTED)) {
            String pregnancyOneTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyOneTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.DELIVERY_METHOD)) {
            String pregOneDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.GESTATIONAL_AGE)) {
            String pregOneGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ONE_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregOneEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ONE_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ONE_DT_UNKNOWN)) {
            String pregOneDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ONE_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ONE_DELIVERY_TERMINATION)) {
            String pregOneDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ONE_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregOneDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_COMPLICATIONS)) {
            String pregTwoComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_BIRTH_WEIGHT)) {
            String pregTwoBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_SEX_INFANT)) {
            String pregTwoSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_OUTCOME_EARLY)) {
            String pregTwoOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_OUTCOME)) {
            String pregTwoOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_LABOUR_TYPE)) {
            String pregTwoLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_TWO)) {
            String cTypeTwo = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_TWO);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeTwo);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_TYPE_OF_ASSISTED)) {
            String pregnancyTwoTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyTwoTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DELIVERY_METHOD)) {
            String pregTwoDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_GESTATIONAL_AGE)) {
            String pregTwoGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregTwoEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DT_UNKNOWN)) {
            String pregTwoDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DELIVERY_TERMINATION)) {
            String pregTwoDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWO_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwoDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_COMPLICATIONS)) {
            String pregThreeComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_BIRTH_WEIGHT)) {
            String pregThreeBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_SEX_INFANT)) {
            String pregThreeSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_OUTCOME_EARLY)) {
            String pregThreeOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_OUTCOME)) {
            String pregThreeOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_LABOUR_TYPE)) {
            String pregThreeLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_THREE)) {
            String cTypeThree = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_THREE);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeThree);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_THREE_TYPE_OF_ASSISTED)) {
            String pregnancyThreeTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_THREE_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyThreeTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_DELIVERY_METHOD)) {
            String pregThreeDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_GESTATIONAL_AGE)) {
            String pregThreeGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregThreeEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_DT_UNKNOWN)) {
            String pregThreeDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THREE_DELIVERY_TERMINATION)) {
            String pregThreeDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THREE_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThreeDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_COMPLICATIONS)) {
            String pregFourComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_BIRTH_WEIGHT)) {
            String pregFourBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_SEX_INFANT)) {
            String pregFourSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_OUTCOME_EARLY)) {
            String pregFourOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_OUTCOME)) {
            String pregFourOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourOutcome);
        }// Continue this pattern for Pregnancy Four
        else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_LABOUR_TYPE)) {
            String pregFourLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_FOUR)) {
            String cTypeFour = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_FOUR);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeFour);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_FOUR_TYPE_OF_ASSISTED)) {
            String pregnancyFourTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_FOUR_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyFourTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_DELIVERY_METHOD)) {
            String pregFourDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_GESTATIONAL_AGE)) {
            String pregFourGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregFourEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_DT_UNKNOWN)) {
            String pregFourDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOUR_DELIVERY_TERMINATION)) {
            String pregFourDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOUR_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_COMPLICATIONS)) {
            String pregFiveComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_BIRTH_WEIGHT)) {
            String pregFiveBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_SEX_INFANT)) {
            String pregFiveSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_OUTCOME_EARLY)) {
            String pregFiveOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_OUTCOME)) {
            String pregFiveOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_LABOUR_TYPE)) {
            String pregFiveLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_FIVE)) {
            String cTypeFive = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_FIVE);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeFive);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_FIVE_TYPE_OF_ASSISTED)) {
            String pregnancyFiveTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_FIVE_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyFiveTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_DELIVERY_METHOD)) {
            String pregFiveDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_GESTATIONAL_AGE)) {
            String pregFiveGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregFiveEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_DT_UNKNOWN)) {
            String pregFiveDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIVE_DELIVERY_TERMINATION)) {
            String pregFiveDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIVE_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFiveDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_COMPLICATIONS)) {
            String pregSixComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_BIRTH_WEIGHT)) {
            String pregSixBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_SEX_INFANT)) {
            String pregSixSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_OUTCOME_EARLY)) {
            String pregSixOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_OUTCOME)) {
            String pregSixOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_LABOUR_TYPE)) {
            String pregSixLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_SIX)) {
            String cTypeSix = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_SIX);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeSix);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_SIX_TYPE_OF_ASSISTED)) {
            String pregnancySixTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_SIX_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancySixTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_DELIVERY_METHOD)) {
            String pregSixDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_GESTATIONAL_AGE)) {
            String pregSixGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregSixEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_DT_UNKNOWN)) {
            String pregSixDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SIX_DELIVERY_TERMINATION)) {
            String pregSixDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SIX_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSixDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_COMPLICATIONS)) {
            String pregSevenComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_BIRTH_WEIGHT)) {
            String pregSevenBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_SEX_INFANT)) {
            String pregSevenSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_OUTCOME_EARLY)) {
            String pregSevenOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_OUTCOME)) {
            String pregSevenOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_LABOUR_TYPE)) {
            String pregSevenLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_SEVEN)) {
            String cTypeSeven = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_SEVEN);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeSeven);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_SEVEN_TYPE_OF_ASSISTED)) {
            String pregnancySevenTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_SEVEN_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancySevenTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_DELIVERY_METHOD)) {
            String pregSevenDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_GESTATIONAL_AGE)) {
            String pregSevenGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregSevenEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_DT_UNKNOWN)) {
            String pregSevenDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_SEVEN_DELIVERY_TERMINATION)) {
            String pregSevenDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_SEVEN_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregSevenDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_COMPLICATIONS)) {
            String pregEightComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_BIRTH_WEIGHT)) {
            String pregEightBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_SEX_INFANT)) {
            String pregEightSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_OUTCOME_EARLY)) {
            String pregEightOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_OUTCOME)) {
            String pregEightOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_LABOUR_TYPE)) {
            String pregEightLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_EIGHT)) {
            String cTypeEight = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_EIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeEight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_EIGHT_TYPE_OF_ASSISTED)) {
            String pregnancyEightTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_EIGHT_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyEightTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_DELIVERY_METHOD)) {
            String pregEightDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_GESTATIONAL_AGE)) {
            String pregEightGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregEightEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_DT_UNKNOWN)) {
            String pregEightDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_EIGHT_DELIVERY_TERMINATION)) {
            String pregEightDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_EIGHT_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregEightDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_COMPLICATIONS)) {
            String pregNineComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_BIRTH_WEIGHT)) {
            String pregNineBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_SEX_INFANT)) {
            String pregNineSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_OUTCOME_EARLY)) {
            String pregNineOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_OUTCOME)) {
            String pregNineOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_LABOUR_TYPE)) {
            String pregNineLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_NINE)) {
            String cTypeNine = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_NINE);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeNine);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_NINE_TYPE_OF_ASSISTED)) {
            String pregnancyNineTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_NINE_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyNineTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_DELIVERY_METHOD)) {
            String pregNineDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_GESTATIONAL_AGE)) {
            String pregNineGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregNineEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_DT_UNKNOWN)) {
            String pregNineDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_NINE_DELIVERY_TERMINATION)) {
            String pregNineDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_NINE_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregNineDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_COMPLICATIONS)) {
            String pregTenComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_BIRTH_WEIGHT)) {
            String pregTenBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_SEX_INFANT)) {
            String pregTenSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_OUTCOME_EARLY)) {
            String pregTenOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_OUTCOME)) {
            String pregTenOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_LABOUR_TYPE)) {
            String pregTenLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_TEN)) {
            String cTypeTen = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_TEN);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeTen);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TEN_TYPE_OF_ASSISTED)) {
            String pregnancyTenTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TEN_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyTenTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_DELIVERY_METHOD)) {
            String pregTenDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_GESTATIONAL_AGE)) {
            String pregTenGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregTenEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_DT_UNKNOWN)) {
            String pregTenDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TEN_DELIVERY_TERMINATION)) {
            String pregTenDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TEN_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTenDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_COMPLICATIONS)) {
            String pregElevenComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_BIRTH_WEIGHT)) {
            String pregElevenBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_SEX_INFANT)) {
            String pregElevenSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_OUTCOME_EARLY)) {
            String pregElevenOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_OUTCOME)) {
            String pregElevenOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_LABOUR_TYPE)) {
            String pregElevenLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_ELEVEN)) {
            String cTypeEleven = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_ELEVEN);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeEleven);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_ELEVEN_TYPE_OF_ASSISTED)) {
            String pregnancyElevenTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_ELEVEN_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyElevenTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_DELIVERY_METHOD)) {
            String pregElevenDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_GESTATIONAL_AGE)) {
            String pregElevenGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregElevenEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_DT_UNKNOWN)) {
            String pregElevenDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_ELEVEN_DELIVERY_TERMINATION)) {
            String pregElevenDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_ELEVEN_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregElevenDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_COMPLICATIONS)) {
            String pregTwelveComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_BIRTH_WEIGHT)) {
            String pregTwelveBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_SEX_INFANT)) {
            String pregTwelveSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_OUTCOME_EARLY)) {
            String pregTwelveOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_OUTCOME)) {
            String pregTwelveOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_LABOUR_TYPE)) {
            String pregTwelveLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_TWELVE)) {
            String cTypeTwelve = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_TWELVE);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeTwelve);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_TWELVE_TYPE_OF_ASSISTED)) {
            String pregnancyTwelveTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_TWELVE_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyTwelveTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_DELIVERY_METHOD)) {
            String pregTwelveDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_GESTATIONAL_AGE)) {
            String pregTwelveGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregTwelveEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_DT_UNKNOWN)) {
            String pregTwelveDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_TWELVE_DELIVERY_TERMINATION)) {
            String pregTwelveDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_TWELVE_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregTwelveDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_COMPLICATIONS)) {
            String pregThirteenComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_BIRTH_WEIGHT)) {
            String pregThirteenBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_SEX_INFANT)) {
            String pregThirteenSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_OUTCOME_EARLY)) {
            String pregThirteenOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_OUTCOME)) {
            String pregThirteenOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_LABOUR_TYPE)) {
            String pregThirteenLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_THIRTEEN)) {
            String cTypeThirteen = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_THIRTEEN);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeThirteen);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_THIRTEEN_TYPE_OF_ASSISTED)) {
            String pregnancyThirteenTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_THIRTEEN_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyThirteenTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DELIVERY_METHOD)) {
            String pregThirteenDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_GESTATIONAL_AGE)) {
            String pregThirteenGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregThirteenEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DT_UNKNOWN)) {
            String pregThirteenDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DELIVERY_TERMINATION)) {
            String pregThirteenDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_THIRTEEN_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregThirteenDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_COMPLICATIONS)) {
            String pregFourteenComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_BIRTH_WEIGHT)) {
            String pregFourteenBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_SEX_INFANT)) {
            String pregFourteenSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_OUTCOME_EARLY)) {
            String pregFourteenOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_OUTCOME)) {
            String pregFourteenOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_LABOUR_TYPE)) {
            String pregFourteenLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_FOURTEEN)) {
            String cTypeFourteen = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_FOURTEEN);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeFourteen);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_FOURTEEN_TYPE_OF_ASSISTED)) {
            String pregnancyFourteenTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_FOURTEEN_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyFourteenTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DELIVERY_METHOD)) {
            String pregFourteenDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_GESTATIONAL_AGE)) {
            String pregFourteenGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregFourteenEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DT_UNKNOWN)) {
            String pregFourteenDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DELIVERY_TERMINATION)) {
            String pregFourteenDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FOURTEEN_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFourteenDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_COMPLICATIONS)) {
            String pregFifteenComplications = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_COMPLICATIONS);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenComplications);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_BIRTH_WEIGHT)) {
            String pregFifteenBirthWeight = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_BIRTH_WEIGHT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenBirthWeight);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_SEX_INFANT)) {
            String pregFifteenSexInfant = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_SEX_INFANT);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenSexInfant);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_OUTCOME_EARLY)) {
            String pregFifteenOutcomeEarly = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_OUTCOME_EARLY);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenOutcomeEarly);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_OUTCOME)) {
            String pregFifteenOutcome = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_OUTCOME);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenOutcome);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_LABOUR_TYPE)) {
            String pregFifteenLabourType = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_LABOUR_TYPE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenLabourType);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.C_TYPE_FIFTEEN)) {
            String cTypeFifteen = womanClient.get(DBConstantsUtils.KeyUtils.C_TYPE_FIFTEEN);
            jsonObject.put(ANCJsonFormUtils.VALUE, cTypeFifteen);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREGNANCY_FIFTEEN_TYPE_OF_ASSISTED)) {
            String pregnancyFifteenTypeOfAssisted = womanClient.get(DBConstantsUtils.KeyUtils.PREGNANCY_FIFTEEN_TYPE_OF_ASSISTED);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregnancyFifteenTypeOfAssisted);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DELIVERY_METHOD)) {
            String pregFifteenDeliveryMethod = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DELIVERY_METHOD);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenDeliveryMethod);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_GESTATIONAL_AGE)) {
            String pregFifteenGestationalAge = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_GESTATIONAL_AGE);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenGestationalAge);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_ESTIMATED_DELIVERY_TERMINATION)) {
            String pregFifteenEstimatedDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_ESTIMATED_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenEstimatedDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DT_UNKNOWN)) {
            String pregFifteenDtUnknown = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DT_UNKNOWN);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenDtUnknown);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DELIVERY_TERMINATION)) {
            String pregFifteenDeliveryTermination = womanClient.get(DBConstantsUtils.KeyUtils.PREG_FIFTEEN_DELIVERY_TERMINATION);
            jsonObject.put(ANCJsonFormUtils.VALUE, pregFifteenDeliveryTermination);
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.DOB_UNKNOWN)) {
            jsonObject.put(ANCJsonFormUtils.READ_ONLY, false);
            JSONObject optionsObject = jsonObject.getJSONArray(ConstantsUtils.JsonFormKeyUtils.OPTIONS).getJSONObject(0);
            optionsObject.put(ANCJsonFormUtils.VALUE, womanClient.get(DBConstantsUtils.KeyUtils.DOB_UNKNOWN));

        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(ConstantsUtils.KeyUtils.AGE_ENTERED)) {
            jsonObject.put(ANCJsonFormUtils.READ_ONLY, false);
            if (StringUtils.isNotBlank(womanClient.get(DBConstantsUtils.KeyUtils.DOB))) {
                jsonObject.put(ANCJsonFormUtils.VALUE, Utils.getAgeFromDate(womanClient.get(DBConstantsUtils.KeyUtils.DOB)));
            }
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.COHABITANTS)) {
            String cohabitants = womanClient.get(DBConstantsUtils.KeyUtils.COHABITANTS);
            Integer numberOfCohabitants = jsonObject.getJSONArray("options").length();
            for(int i = 0; i < numberOfCohabitants; i++ )
            {
                String key = jsonObject.getJSONArray("options").getJSONObject(i).getString("key");
                if(cohabitants != null && cohabitants.contains(key))
                {
                    jsonObject.getJSONArray("options").getJSONObject(i).put(ANCJsonFormUtils.VALUE,true);
                }
            }

        } else if (ancMetadata != null && ancMetadata.getFieldsWithLocationHierarchy() != null &&
                ancMetadata.getFieldsWithLocationHierarchy().contains(jsonObject.optString(ANCJsonFormUtils.KEY))) {
            reverseLocationTree(jsonObject, womanClient.get(jsonObject.optString(ANCJsonFormUtils.KEY)));
        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(DBConstantsUtils.KeyUtils.EDD)) {
            formatEdd(womanClient, jsonObject, DBConstantsUtils.KeyUtils.EDD);

        } else if (jsonObject.getString(ANCJsonFormUtils.KEY).equalsIgnoreCase(ConstantsUtils.JsonFormKeyUtils.ANC_ID)) {
            jsonObject.put(ANCJsonFormUtils.VALUE, womanClient.get(DBConstantsUtils.KeyUtils.ANC_ID).replace("-", ""));
        }  else if (womanClient.containsKey(jsonObject.getString(ANCJsonFormUtils.KEY))) {
            jsonObject.put(ANCJsonFormUtils.READ_ONLY, false);
            jsonObject.put(ANCJsonFormUtils.VALUE, womanClient.get(jsonObject.getString(ANCJsonFormUtils.KEY)));
        } else {
            Timber.e("ERROR:: Unprocessed Form Object Key %s", jsonObject.getString(ANCJsonFormUtils.KEY));
        }
    }

    private static void reverseLocationTree(@NonNull JSONObject jsonObject, @Nullable String entity) throws JSONException {
        List<String> entityHierarchy = null;
        if (entity != null) {
            if (ConstantsUtils.OTHER.equalsIgnoreCase(entity)) {
                entityHierarchy = new ArrayList<>();
                entityHierarchy.add(entity);
            } else {
                String locationId = LocationHelper.getInstance().getOpenMrsLocationId(entity);
                entityHierarchy = LocationHelper.getInstance().getOpenMrsLocationHierarchy(locationId, false);
            }
        }
        ArrayList<String> allLevels = AncLibrary.getInstance().getAncMetadata().getHealthFacilityLevels();
        List<FormLocation> entireTree = LocationHelper.getInstance().generateLocationHierarchyTree(true, allLevels);
        String entireTreeString = AssetHandler.javaToJsonString(entireTree, new TypeToken<List<FormLocation>>() {
        }.getType());
        String facilityHierarchyString = AssetHandler.javaToJsonString(entityHierarchy, new TypeToken<List<String>>() {
        }.getType());
        if (StringUtils.isNotBlank(facilityHierarchyString)) {
            jsonObject.put(JsonFormConstants.VALUE, facilityHierarchyString);
            jsonObject.put(JsonFormConstants.TREE, new JSONArray(entireTreeString));
        }
    }

    private static void getDobUsingEdd(Map<String, String> womanClient, JSONObject jsonObject, String birthDate)
            throws JSONException {
        String dobString = womanClient.get(birthDate);
        if (StringUtils.isNotBlank(dobString)) {
            Date dob = Utils.dobStringToDate(dobString);
            if (dob != null) {
                jsonObject.put(ANCJsonFormUtils.VALUE, DATE_FORMAT.format(dob));
            }
        }
    }

    private static void getPhotoFieldValue(Map<String, String> womanClient, JSONObject jsonObject) throws JSONException {
        Photo photo = ImageUtils.profilePhotoByClientID(womanClient.get(DBConstantsUtils.KeyUtils.BASE_ENTITY_ID),
                Utils.getProfileImageResourceIdentifier());

        if (photo != null && StringUtils.isNotBlank(photo.getFilePath())) {
            jsonObject.put(ANCJsonFormUtils.VALUE, photo.getFilePath());

        }
    }

    private static void formatEdd(Map<String, String> womanClient, JSONObject jsonObject, String eddDate)
            throws JSONException {
        String eddString = womanClient.get(eddDate);
        if (StringUtils.isNotBlank(eddString)) {
            Date edd = Utils.dobStringToDate(eddString);
            if (edd != null) {
                jsonObject.put(ANCJsonFormUtils.VALUE, EDD_DATE_FORMAT.format(edd));
            }
        }
    }

    public static void startFormForEdit(Activity context, int jsonFormActivityRequestCode, String metaData) {
        Intent intent = new Intent(context, EditJsonFormActivity.class);
        intent.putExtra(ConstantsUtils.IntentKeyUtils.JSON, metaData);
        intent.putExtra(JsonFormConstants.PERFORM_FORM_TRANSLATION, true);
        Timber.d("form is %s", metaData);
        context.startActivityForResult(intent, jsonFormActivityRequestCode);
    }

    public static Triple<Boolean, Event, Event> saveRemovedFromANCRegister(AllSharedPreferences allSharedPreferences, String jsonString, String providerId) {
        try {
            boolean isDeath = false;
            Triple<Boolean, JSONObject, JSONArray> registrationFormParams = validateParameters(jsonString);

            if (!registrationFormParams.getLeft()) {
                return null;
            }

            JSONObject jsonForm = registrationFormParams.getMiddle();
            JSONArray fields = registrationFormParams.getRight();

            String encounterType = ANCJsonFormUtils.getString(jsonForm, ENCOUNTER_TYPE);
            JSONObject metadata = ANCJsonFormUtils.getJSONObject(jsonForm, METADATA);

            String encounterLocation = null;

            try {
                encounterLocation = metadata.getString(ConstantsUtils.JsonFormKeyUtils.ENCOUNTER_LOCATION);
            } catch (JSONException e) {
                Timber.e(e, "JsonFormUtils --> saveRemovedFromANCRegister --> getEncounterLocation");
            }

            Date encounterDate = new Date();
            String entityId = ANCJsonFormUtils.getString(jsonForm, ANCJsonFormUtils.ENTITY_ID);

            Event event = (Event) new Event().withBaseEntityId(entityId) //should be different for main and subform
                    .withEventDate(encounterDate).withEventType(encounterType).withLocationId(encounterLocation)
                    .withProviderId(providerId).withEntityType(DBConstantsUtils.DEMOGRAPHIC_TABLE_NAME)
                    .withFormSubmissionId(ANCJsonFormUtils.generateRandomUUIDString()).withDateCreated(new Date());
            tagSyncMetadata(allSharedPreferences, event);

            for (int i = 0; i < fields.length(); i++) {
                JSONObject jsonObject = ANCJsonFormUtils.getJSONObject(fields, i);

                String value = ANCJsonFormUtils.getString(jsonObject, ANCJsonFormUtils.VALUE);
                if (StringUtils.isNotBlank(value)) {
                    ANCJsonFormUtils.addObservation(event, jsonObject);
                    if (jsonObject.get(ANCJsonFormUtils.KEY).equals(ConstantsUtils.JsonFormKeyUtils.ANC_CLOSE_REASON)) {
                        isDeath = "Woman Died".equalsIgnoreCase(value);
                    }
                }
            }

            Iterator<?> keys = metadata.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                JSONObject jsonObject = ANCJsonFormUtils.getJSONObject(metadata, key);
                String value = ANCJsonFormUtils.getString(jsonObject, ANCJsonFormUtils.VALUE);
                if (StringUtils.isNotBlank(value)) {
                    String entityVal = ANCJsonFormUtils.getString(jsonObject, ANCJsonFormUtils.OPENMRS_ENTITY);
                    if (entityVal != null) {
                        if (entityVal.equals(ANCJsonFormUtils.CONCEPT)) {
                            ANCJsonFormUtils.addToJSONObject(jsonObject, ANCJsonFormUtils.KEY, key);
                            ANCJsonFormUtils.addObservation(event, jsonObject);

                        } else if (entityVal.equals(ANCJsonFormUtils.ENCOUNTER)) {
                            String entityIdVal = ANCJsonFormUtils.getString(jsonObject, ANCJsonFormUtils.OPENMRS_ENTITY_ID);
                            if (entityIdVal.equals(FormEntityConstants.Encounter.encounter_date.name())) {
                                Date eDate = ANCJsonFormUtils.formatDate(value, false);
                                if (eDate != null) {
                                    event.setEventDate(eDate);
                                }
                            }
                        }
                    }
                }
            }

            //Update Child Entity to include death date
            Event updateChildDetailsEvent =
                    (Event) new Event().withBaseEntityId(entityId) //should be different for main and subform
                            .withEventDate(encounterDate).withEventType(ConstantsUtils.EventTypeUtils.UPDATE_REGISTRATION)
                            .withLocationId(encounterLocation).withProviderId(providerId)
                            .withEntityType(DBConstantsUtils.DEMOGRAPHIC_TABLE_NAME).withFormSubmissionId(ANCJsonFormUtils.generateRandomUUIDString())
                            .withDateCreated(new Date());
            tagSyncMetadata(allSharedPreferences, updateChildDetailsEvent);

            return Triple.of(isDeath, event, updateChildDetailsEvent);
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> saveRemovedFromANCRegister");
        }
        return null;
    }

    public static void launchANCCloseForm(Activity activity) {
        try {
            Intent intent = new Intent(activity, FormConfigurationJsonFormActivity.class);
            JSONObject form = new com.vijay.jsonwizard.utils.FormUtils().getFormJsonFromRepositoryOrAssets(activity.getApplicationContext(), ConstantsUtils.JsonFormUtils.ANC_CLOSE);
            if (form != null) {
                form.put(ConstantsUtils.JsonFormKeyUtils.ENTITY_ID, activity.getIntent().getStringExtra(ConstantsUtils.IntentKeyUtils.BASE_ENTITY_ID));
                intent.putExtra(ConstantsUtils.IntentKeyUtils.JSON, form.toString());
                intent.putExtra(JsonFormConstants.PERFORM_FORM_TRANSLATION, true);
                activity.startActivityForResult(intent, ANCJsonFormUtils.REQUEST_CODE_GET_JSON);
            }
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> launchANCCloseForm");
        }
    }

    public static void launchSiteCharacteristicsForm(Activity activity) {
        try {
            Intent intent = new Intent(activity, FormConfigurationJsonFormActivity.class);
            JSONObject form = new com.vijay.jsonwizard.utils.FormUtils().getFormJsonFromRepositoryOrAssets(activity.getApplicationContext(), ConstantsUtils.JsonFormUtils.ANC_SITE_CHARACTERISTICS);
            if (form != null) {
                form.put(ConstantsUtils.JsonFormKeyUtils.ENTITY_ID,
                        activity.getIntent().getStringExtra(ConstantsUtils.IntentKeyUtils.BASE_ENTITY_ID));
                intent.putExtra(ConstantsUtils.IntentKeyUtils.JSON, form.toString());
                intent.putExtra(JsonFormConstants.PERFORM_FORM_TRANSLATION, true);
                activity.startActivityForResult(intent, ANCJsonFormUtils.REQUEST_CODE_GET_JSON);
            }
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> launchSiteCharacteristicsForm");
        }
    }

    public static Map<String, String> processSiteCharacteristics(String jsonString) {
        try {
            Triple<Boolean, JSONObject, JSONArray> registrationFormParams = validateParameters(jsonString);
            if (!registrationFormParams.getLeft()) {
                return null;
            }

            Map<String, String> settings = new HashMap<>();
            JSONArray fields =
                    registrationFormParams.getMiddle().getJSONObject(ANCJsonFormUtils.STEP1).getJSONArray(ANCJsonFormUtils.FIELDS);

            for (int i = 0; i < fields.length(); i++) {
                if (!"label".equals(fields.getJSONObject(i).getString(ConstantsUtils.KeyUtils.TYPE))) {
                    settings.put(fields.getJSONObject(i).getString(ConstantsUtils.KeyUtils.KEY),
                            StringUtils.isBlank(fields.getJSONObject(i).getString(ConstantsUtils.KeyUtils.VALUE)) ? "0" :
                                    fields.getJSONObject(i).getString(ConstantsUtils.KeyUtils.VALUE));
                }
            }

            return settings;
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> processSiteCharacteristics");
            return null;
        }
    }

    public static String getAutoPopulatedSiteCharacteristicsEditFormString(Context context,
                                                                           Map<String, String> characteristics) {
        try {
            JSONObject form = FormUtils.getInstance(context).getFormJson(ConstantsUtils.JsonFormUtils.ANC_SITE_CHARACTERISTICS);
            Timber.d("Form is %s", form.toString());
            if (form != null) {
                form.put(ANCJsonFormUtils.ENCOUNTER_TYPE, ConstantsUtils.EventTypeUtils.SITE_CHARACTERISTICS);

                JSONObject stepOne = form.getJSONObject(ANCJsonFormUtils.STEP1);
                JSONArray jsonArray = stepOne.getJSONArray(ANCJsonFormUtils.FIELDS);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (characteristics.containsKey(jsonObject.getString(ANCJsonFormUtils.KEY))) {
                        jsonObject.put(ANCJsonFormUtils.READ_ONLY, false);
                        jsonObject.put(ANCJsonFormUtils.VALUE,
                                "true".equals(characteristics.get(jsonObject.getString(ANCJsonFormUtils.KEY))) ? "1" : "0");
                    }

                }

                return form.toString();
            }
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> getAutoPopulatedSiteCharacteristicsEditFormString");
        }

        return "";
    }

    public static Pair<Event, Event> createVisitAndUpdateEvent(List<String> formSubmissionIDs,
                                                               Map<String, String> womanDetails) {
        if (formSubmissionIDs.size() < 1 && womanDetails.get(ConstantsUtils.REFERRAL) == null) {
            return null;
        }

        try {
            String baseEntityId = womanDetails.get(DBConstantsUtils.KeyUtils.BASE_ENTITY_ID);

            Event contactVisitEvent = Utils.createContactVisitEvent(formSubmissionIDs, womanDetails, String.valueOf(getOpenTasks(baseEntityId)));

            //Update client
            EventClientRepository db = AncLibrary.getInstance().getEventClientRepository();

            JSONObject clientForm = db.getClientByBaseEntityId(baseEntityId);
            JSONObject attributes = clientForm.getJSONObject(ConstantsUtils.JsonFormKeyUtils.ATTRIBUTES);
            attributes.put(DBConstantsUtils.KeyUtils.NEXT_CONTACT, womanDetails.get(DBConstantsUtils.KeyUtils.NEXT_CONTACT));
            attributes.put(DBConstantsUtils.KeyUtils.NEXT_CONTACT_DATE, womanDetails.get(DBConstantsUtils.KeyUtils.NEXT_CONTACT_DATE));
            attributes.put(DBConstantsUtils.KeyUtils.LAST_CONTACT_RECORD_DATE,
                    womanDetails.get(DBConstantsUtils.KeyUtils.LAST_CONTACT_RECORD_DATE));
            attributes.put(DBConstantsUtils.KeyUtils.CONTACT_STATUS, womanDetails.get(DBConstantsUtils.KeyUtils.CONTACT_STATUS));
            attributes.put(DBConstantsUtils.KeyUtils.YELLOW_FLAG_COUNT, womanDetails.get(DBConstantsUtils.KeyUtils.YELLOW_FLAG_COUNT));
            attributes.put(DBConstantsUtils.KeyUtils.RED_FLAG_COUNT, womanDetails.get(DBConstantsUtils.KeyUtils.RED_FLAG_COUNT));
            attributes.put(DBConstantsUtils.KeyUtils.EDD, womanDetails.get(DBConstantsUtils.KeyUtils.EDD));
            attributes.put(DBConstantsUtils.KeyUtils.ALT_NAME, womanDetails.get(DBConstantsUtils.KeyUtils.ALT_NAME));
            attributes.put(DBConstantsUtils.KeyUtils.PHONE_NUMBER, womanDetails.get(DBConstantsUtils.KeyUtils.PHONE_NUMBER));
            attributes.put(DBConstantsUtils.KeyUtils.ALT_PHONE_NUMBER, womanDetails.get(DBConstantsUtils.KeyUtils.ALT_PHONE_NUMBER));
            clientForm.put(ConstantsUtils.JsonFormKeyUtils.ATTRIBUTES, attributes);

            db.addorUpdateClient(baseEntityId, clientForm);

            Event updateClientEvent = createUpdateClientDetailsEvent(baseEntityId);

            return Pair.create(contactVisitEvent, updateClientEvent);

        } catch (Exception e) {
            Timber.e(e, " --> createContactVisitEvent");
            return null;
        }

    }

    public static Date getContactStartDate(String contactStartDate) {
        try {
            return new LocalDate(contactStartDate).toDate();
        } catch (Exception e) {
            return new LocalDate().toDate();
        }
    }

    private static JSONArray getOpenTasks(String baseEntityId) {
        List<Task> openTasks = AncLibrary.getInstance().getContactTasksRepository().getOpenTasks(baseEntityId);
        JSONArray openTaskArray = new JSONArray();
        if (openTasks != null && openTasks.size() > 0) {
            for (Task task : openTasks) {
                openTaskArray.put(task.getValue());
            }
        }
        return openTaskArray;
    }

    protected static Event createUpdateClientDetailsEvent(String baseEntityId) {

        Event updateChildDetailsEvent = (Event) new Event().withBaseEntityId(baseEntityId).withEventDate(new Date())
                .withEventType(ConstantsUtils.EventTypeUtils.UPDATE_REGISTRATION).withEntityType(DBConstantsUtils.DEMOGRAPHIC_TABLE_NAME)
                .withFormSubmissionId(ANCJsonFormUtils.generateRandomUUIDString()).withDateCreated(new Date());

        ANCJsonFormUtils
                .tagSyncMetadata(AncLibrary.getInstance().getContext().allSharedPreferences(), updateChildDetailsEvent);

        return updateChildDetailsEvent;
    }

    public static Event processContactFormEvent(JSONObject jsonForm, String baseEntityId) {
        AllSharedPreferences allSharedPreferences = AncLibrary.getInstance().getContext().allSharedPreferences();
        JSONArray fields = getMultiStepFormFields(jsonForm);

        String entityId = getString(jsonForm, ANCJsonFormUtils.ENTITY_ID);
        if (StringUtils.isBlank(entityId)) {
            entityId = baseEntityId;
        }

        String encounterType = getString(jsonForm, ENCOUNTER_TYPE);
        JSONObject metadata = getJSONObject(jsonForm, METADATA);

        FormTag formTag = getFormTag(allSharedPreferences);
        Event baseEvent = org.smartregister.util.JsonFormUtils
                .createEvent(fields, metadata, formTag, entityId, encounterType, DBConstantsUtils.DEMOGRAPHIC_TABLE_NAME);

        tagSyncMetadata(allSharedPreferences, baseEvent);// tag docs

        return baseEvent;
    }

    public static JSONObject readJsonFromAsset(Context context, String filePath) throws Exception {
        InputStream inputStream = context.getAssets().open(filePath + ".json");
        BufferedReader reader = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        }
        String jsonString;
        StringBuilder stringBuilder = new StringBuilder();
        while ((jsonString = reader.readLine()) != null) {
            stringBuilder.append(jsonString);
        }
        inputStream.close();
        return new JSONObject(stringBuilder.toString());
    }

    /**
     * Gets an expansion panel {@link JSONObject} value then check whether the test/tasks was `ordered` or `not done`.
     * If any either of this is selected then we mark it as not complete.
     *
     * @param field {@link JSONObject}
     * @return isComplete {@link Boolean}
     */
    public static boolean checkIfTaskIsComplete(JSONObject field) {
        boolean isComplete = true;
        try {
            if (field != null && field.has(JsonFormConstants.VALUE)) {
                JSONArray value = field.getJSONArray(JsonFormConstants.VALUE);
                if (value.length() > 1) {
                    JSONObject valueField = value.getJSONObject(0);
                    if (valueField != null && valueField.has(JsonFormConstants.VALUES)) {
                        JSONArray values = valueField.getJSONArray(JsonFormConstants.VALUES);
                        if (values.length() > 0) {
                            String selectedValue = values.getString(0);
                            if (selectedValue.contains(JsonFormConstants.AncRadioButtonOptionTypesUtils.ORDERED) || selectedValue.contains(JsonFormConstants.AncRadioButtonOptionTypesUtils.NOT_DONE)) {
                                isComplete = false;
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Timber.e(e, " --> checkIfTaskIsComplete");
        }
        return isComplete;
    }

    public List<ContactSummaryModel> generateNextContactSchedule(String edd, List<String> contactSchedule,
                                                                 Integer lastContactSequence) {
        List<ContactSummaryModel> contactDates = new ArrayList<>();
        Integer contactSequence = lastContactSequence;
        if (StringUtils.isNotBlank(edd)) {
            LocalDate localDate = new LocalDate(edd);
            LocalDate lmpDate = localDate.minusWeeks(ConstantsUtils.DELIVERY_DATE_WEEKS);

            for (String contactWeeks : contactSchedule) {
                contactDates.add(new ContactSummaryModel(String.format(
                        AncLibrary.getInstance().getContext().getStringResource(R.string.contact_number),
                        contactSequence++),
                        Utils.convertDateFormat(lmpDate.plusWeeks(Integer.valueOf(contactWeeks)).toDate(),
                                Utils.CONTACT_SUMMARY_DF), lmpDate.plusWeeks(Integer.valueOf(contactWeeks)).toDate(),
                        contactWeeks));
            }
        }
        return contactDates;
    }

    /**
     * Creates and populates a constraint view to add the contacts tab view instead of using recycler views which introduce
     * lots of scroll complexities
     *
     * @param data
     * @param facts
     * @param position
     * @param context
     * @return constraintLayout
     */
    @NonNull
    public ConstraintLayout createListViewItems(List<YamlConfigWrapper> data, Facts facts, int position, Context context) {
        YamlConfigItem yamlConfigItem = data.get(position).getYamlConfigItem();

        ANCJsonFormUtils.Template template = getTemplate(yamlConfigItem.getTemplate());
        String output = "";
        if (!TextUtils.isEmpty(template.detail)) {
            output = Utils.fillTemplate(template.detail, facts);
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout constraintLayout =
                (ConstraintLayout) inflater.inflate(R.layout.previous_contacts_preview_row, null);
        TextView sectionDetailTitle = constraintLayout.findViewById(R.id.overview_section_details_left);
        TextView sectionDetails = constraintLayout.findViewById(R.id.overview_section_details_right);


        sectionDetailTitle.setText(template.title);
        sectionDetails.setText(output);//Perhaps refactor to use Json Form Parser Implementation

        if (AncLibrary.getInstance().getAncRulesEngineHelper().getRelevance(facts, yamlConfigItem.getIsRedFont())) {
            sectionDetailTitle.setTextColor(context.getResources().getColor(R.color.overview_font_red));
            sectionDetails.setTextColor(context.getResources().getColor(R.color.overview_font_red));
        } else {
            sectionDetailTitle.setTextColor(context.getResources().getColor(R.color.overview_font_left));
            sectionDetails.setTextColor(context.getResources().getColor(R.color.overview_font_right));
        }

        sectionDetailTitle.setVisibility(View.VISIBLE);
        sectionDetails.setVisibility(View.VISIBLE);
        return constraintLayout;
    }

    public Template getTemplate(String rawTemplate) {
        Template template = new Template();

        if (rawTemplate.contains(":")) {
            String[] templateArray = rawTemplate.split(":");
            if (templateArray.length == 1) {
                template.title = templateArray[0].trim();
            } else if (templateArray.length > 1) {
                template.title = templateArray[0].trim();
                template.detail = templateArray[1].trim();
            }
        } else {
            template.title = rawTemplate;
            template.detail = "Yes";
        }

        return template;
    }

    public class Template {
        public String title = "";
        public String detail = "";
    }
}