package org.smartregister.anc.library.activity;

import static org.smartregister.anc.library.activity.BaseHomeRegisterActivity.context;
import static org.smartregister.utils.PropertiesConverter.gson;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import org.jeasy.rules.api.Facts;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.anc.library.AncLibrary;
import org.smartregister.anc.library.BuildConfig;
import org.smartregister.anc.library.R;
import org.smartregister.anc.library.contract.ProfileContract;
import org.smartregister.anc.library.domain.YamlConfig;
import org.smartregister.anc.library.model.PartialContact;
import org.smartregister.anc.library.presenter.ProfilePresenter;
import org.smartregister.anc.library.repository.PartialContactRepository;
import org.smartregister.anc.library.repository.PatientRepository;
import org.smartregister.anc.library.task.FinalizeContactTask;
import org.smartregister.anc.library.task.LoadContactSummaryDataTask;
import org.smartregister.anc.library.util.ANCFormUtils;
import org.smartregister.anc.library.util.ConstantsUtils;
import org.smartregister.anc.library.util.DBConstantsUtils;
import org.smartregister.anc.library.util.FilePathUtils;
import org.smartregister.anc.library.util.Utils;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.util.PermissionUtils;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;


/**
 * Created by ndegwamartin on 10/07/2018.
 */


public class ContactSummaryFinishActivity extends BaseProfileActivity implements ProfileContract.View {
    public MenuItem saveFinishMenuItem;
    private TextView nameView;
    private TextView ageView;
    private TextView gestationAgeView;
    private TextView ancIdView;
    private ImageView imageView;
    private ImageRenderHelper imageRenderHelper;
    private final Facts facts = new Facts();
    private List<YamlConfig> yamlConfigList = new ArrayList<>();
    private String baseEntityId;
    private int contactNo;

    private String phoneNumber;
    private static String jsonReadResponse;
    private HashMap<String, String> detailMap;
    private HashMap<String, String> contactMap;
    private String firstName;
    private String lastName;
    private String nextContactNo;

    private String nextContactDate;
    private String nextContactDateEdited;
    private String contactDoneDate;
    private String lastContactDate;
    private String diastolicBloodPressure;
    private String systolicBloodPressure;
    private String lmpedd;
    private String gest;
    private String contactDate;
    private String medications;
    private String danger_signs;
    private String expectedDueDate;
    private String jsonString;
    private static String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseEntityId = getIntent().getStringExtra(ConstantsUtils.IntentKeyUtils.BASE_ENTITY_ID);
        contactNo = getIntent().getExtras().getInt(ConstantsUtils.IntentKeyUtils.CONTACT_NO);

        setUpViews();

        mProfilePresenter = new ProfilePresenter(this);
        imageRenderHelper = new ImageRenderHelper(this);


    }

    private void setUpViews() {
        ageView = findViewById(R.id.textview_age);
        gestationAgeView = findViewById(R.id.textview_gestation_age);
        ancIdView = findViewById(R.id.textview_anc_id);
        nameView = findViewById(R.id.textview_name);
        imageView = findViewById(R.id.imageview_profile);

        findViewById(R.id.btn_profile_registration_info).setVisibility(View.GONE);

        collapsingToolbarLayout.setTitleEnabled(false);
        if (contactNo > 0) {
            actionBar.setTitle(String.format(this.getString(R.string.contact_number),
                    getIntent().getExtras().getInt(ConstantsUtils.IntentKeyUtils.CONTACT_NO)));
        } else {
            actionBar.setTitle(R.string.back);
        }
    }


    protected void loadContactSummaryData() {
        try {
            new LoadContactSummaryDataTask(this, getIntent(), mProfilePresenter, facts, baseEntityId).execute();
        } catch (Exception e) {
            Timber.e(e, "%s loadContactSummaryData()", this.getClass().getCanonicalName());
        }
    }

    public static String findUuidByPhoneNumber(String jsonResponse, String phoneNumber,  String tag_string_req, JSONObject jsonBody) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject result = resultsArray.getJSONObject(i);
                JSONArray urnsArray = result.getJSONArray("urns");
                for (int j = 0; j < urnsArray.length(); j++) {
                    String urn = urnsArray.getString(j);
                    if (urn.contains(phoneNumber)) {
                        uuid = result.getString("uuid");
                        String urlPost;
                        if(uuid != null) {
                            urlPost = "https://textit.com/api/v2/contacts.json?" + "uuid=" + uuid;
                        }else{
                            urlPost = "https://textit.com/api/v2/contacts.json";
                        }
                        System.out.println("UUID for phone number " + phoneNumber + ": " + uuid);
                        //System.out.println("Response: " + response.toString());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                urlPost,
                                jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.e("TextItAPIResponse", "Response Received: " + response.toString());
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error.networkResponse != null) {
                                            Log.e("TextItAPIError", "Error response code: " + error.networkResponse.statusCode);
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public byte[] getBody() {
                                return jsonBody.toString().getBytes();
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json";
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                String apiKey = BuildConfig.TEXTIT_API_KEY;
                                HashMap<String, String> headers = new HashMap<String, String>();
                                if(apiKey != null) {
                                    headers.put("Authorization", apiKey);
                                }
                                return headers;
                            }
                        };

                        addToJSONRequestQueue(jsonObjectRequest, tag_string_req);
                        return result.getString("uuid");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getTextitJson(String phoneNumber, String tag_string_req, JSONObject jsonBody){
        String url = "https://textit.com/api/v2/contacts.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        jsonReadResponse = String.valueOf(response);
                        uuid = findUuidByPhoneNumber(jsonReadResponse, phoneNumber, tag_string_req, jsonBody);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        if (error.networkResponse != null) {
                            System.out.println("Error response code: " + error.networkResponse.statusCode);
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                String apiKey = BuildConfig.TEXTIT_API_KEY;
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", apiKey);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    public static void addToJSONRequestQueue(Request<JSONObject> request, String tag) {
        Context context = AncLibrary.getInstance().getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        if (tag != null) {
            request.setTag(tag);
        }

        requestQueue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();

        Context context = this;

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.set_next_contact, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        CustomFontTextView textView = view.findViewById(R.id.txt_title_label);

        String text = "Please enter the Next Appointment Date";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.append(" *");

        spannableStringBuilder.setSpan(
                new ForegroundColorSpan(Color.RED),
                spannableStringBuilder.length() - 1,
                spannableStringBuilder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView.setText(spannableStringBuilder);

        Button yes = view.findViewById(R.id.save_date);
        TextView nextDate = view.findViewById(R.id.add_next_contact);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams param = window.getAttributes();
            param.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            window.setAttributes(param);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        nextDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    nextDate.setText(sdf.format(calendar.getTime()));
                }
            };
            new DatePickerDialog(context, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        yes.setOnClickListener(v -> {
            String next_value = nextDate.getText().toString();
            if (next_value == null || next_value.isEmpty()) {
                Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show();
            } else {
                nextContactDateEdited = next_value;
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("name", firstName + " " + lastName);
                    jsonBody.put("language", "eng");
                    JSONArray urnsArray = new JSONArray();
                    urnsArray.put("tel:" + "+26" + phoneNumber);
                    jsonBody.put("urns", urnsArray);

                    JSONArray groupsArray = new JSONArray();
                    groupsArray.put("3ad12a51-bf7f-4cac-ab2e-79871d8527f8");
                    jsonBody.put("groups", groupsArray);

                    JSONObject fields = new JSONObject();
                    fields.put("day_of_next_appointment", nextContactDate);
                    fields.put("next_contact_text", nextContactDateEdited);
                    if (nextContactNo.contains("0") || nextContactNo.contains("1") || nextContactNo.contains("2") || nextContactNo.contains("3")) {
                        fields.put("first_antenatal_appointment", contactDate);
                    }
                    fields.put("next_antenatal_appointment", (nextContactNo + 1));

                    if (!nextContactNo.contains("0") && !nextContactNo.contains("1")) {
                        fields.put("after_antenatal_appointment", (contactDate + 1));
                    }

                    fields.put("blood_pressure", (systolicBloodPressure + "/" + diastolicBloodPressure));
                    fields.put("danger_signs", danger_signs);
                    fields.put("medicines", medications);

                    String edd = "12-12-2024";

                    if (lmpedd != null) {
                        edd = lmpedd;
                    }
                    fields.put("post_due_date", (edd));
                    fields.put("post_due_date_text", (edd));

                    jsonBody.put("fields", fields);

                    String tag_string_req = "req_login";

                    getTextitJson(phoneNumber, tag_string_req, jsonBody);
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    PatientRepository.updateEDDDate(baseEntityId, null); //Reset EDD
                    back();
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });

        dialog.show();
        loadContactSummaryData();
    }

    public void back(){
        super.onBackPressed();
    }


    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_contact_summary_finish;
    }

    public void process() throws Exception {
        //Get actual Data
        JSONObject object;

        List<PartialContact> partialContacts = getPartialContactRepository()
                .getPartialContacts(getIntent().getStringExtra(ConstantsUtils.IntentKeyUtils.BASE_ENTITY_ID),
                        getIntent().getIntExtra(ConstantsUtils.IntentKeyUtils.CONTACT_NO, 1));

        if (partialContacts != null && !partialContacts.isEmpty()) {
            for (PartialContact partialContact : partialContacts) {
                if (partialContact.getFormJsonDraft() != null || partialContact.getFormJson() != null) {
                    object = new JSONObject(partialContact.getFormJsonDraft() != null ? partialContact.getFormJsonDraft() :
                            partialContact.getFormJson());
                    ANCFormUtils.processRequiredStepsField(facts, object);
                }
            }


            jsonString = String.valueOf(facts);

            int bpSystolicIndex = jsonString.indexOf("bp_systolic");
            if (bpSystolicIndex != -1) {
                int start = jsonString.indexOf(":", bpSystolicIndex) + 1;
                int end = jsonString.indexOf("}", start);
                String bpSystolicValue = jsonString.substring(start, end).trim();
                systolicBloodPressure = bpSystolicValue;
                System.out.println("BP Systolic Value: " + bpSystolicValue);
            }

            int lmpeddindex = jsonString.indexOf("{ edd");
            if (lmpeddindex != -1) {
                int start = jsonString.indexOf(":", lmpeddindex) + 1;
                int end = jsonString.indexOf("}", start);
                String lmpeddValue = jsonString.substring(start, end).trim();
                lmpedd = lmpeddValue;
                System.out.println("lmp edd: " + lmpeddValue);
            }

            int gestindex = jsonString.indexOf("{ gest_age");
            if (gestindex != -1) {
                int start = jsonString.indexOf(":", gestindex) + 1;
                int end = jsonString.indexOf("}", start);
                String gestValue = jsonString.substring(start, end).trim();
                gest = gestValue;
                System.out.println("{ gest_age" + gestValue);
            }

            int contactdateindex = jsonString.indexOf("contact_date");
            if (contactdateindex != -1) {
                int start = jsonString.indexOf(":", contactdateindex) + 1;
                int end = jsonString.indexOf("}", start);
                String contactDateValue = jsonString.substring(start, end).trim();
                contactDate = contactDateValue;
                System.out.println("contact date: " + contactDateValue);
            }


            int bpDiastolicIndex = jsonString.indexOf("bp_diastolic");
            if (bpDiastolicIndex != -1) {
                int start = jsonString.indexOf(":", bpDiastolicIndex) + 1;
                int end = jsonString.indexOf("}", start);
                String bpDiastolicValue = jsonString.substring(start, end).trim();
                diastolicBloodPressure = bpDiastolicValue;
                System.out.println("BP Diastolic Value: " + bpDiastolicValue);
            }

            int medicationsIndex = jsonString.indexOf("medications");
            if (medicationsIndex != -1) {
                int start = jsonString.indexOf(":", medicationsIndex) + 1;
                int end = jsonString.indexOf("]", start);
                String medicationsValue = jsonString.substring(start, end).trim();

                String[] medicationObjects = medicationsValue.split("\\},\\s*\\{");

                List<String> medicationTexts = new ArrayList<>();
                for (String medicationObj : medicationObjects) {

                    medicationObj = medicationObj.replaceAll("\\{\\s*|\\s*\\}", "");

                    String[] keyValuePairs = medicationObj.split(",");
                    for (String pair : keyValuePairs) {

                        if (pair.contains("text")) {
                            String[] keyValue = pair.split(":");
                            if (keyValue.length == 2) {
                                String medicationText = keyValue[1].replaceAll("\"", "").trim();
                                medicationTexts.add(medicationText);
                            }
                        }
                    }
                }

                StringBuilder finalMedicationsString = new StringBuilder();
                for (String medicationText : medicationTexts) {
                    finalMedicationsString.append(medicationText).append(", ");
                }
                if (finalMedicationsString.length() > 0) {
                    finalMedicationsString.setLength(finalMedicationsString.length() - 2);
                }

                medications = finalMedicationsString.toString();
                System.out.println("medications Value: " + medications);
            }



            int danger_signsIndex = jsonString.indexOf("danger_signs");
            if (danger_signsIndex != -1) {
                int start = jsonString.indexOf("[", danger_signsIndex);
                int end = jsonString.indexOf("]", start) + 1; // Include the closing bracket
                String danger_signsValue = jsonString.substring(start, end).trim();

                Pattern pattern = Pattern.compile("\"text\":\"(.*?)\"");
                Matcher matcher = pattern.matcher(danger_signsValue);
                StringBuilder extractedTexts = new StringBuilder();

                while (matcher.find()) {
                    if (extractedTexts.length() > 0) {
                        extractedTexts.append(", ");
                    }
                    extractedTexts.append(matcher.group(1));
                }

                danger_signs = extractedTexts.toString();
                System.out.println("Danger Signs: " + danger_signs);
            }

            detailMap = (HashMap<String, String>) getIntent().getSerializableExtra(ConstantsUtils.IntentKeyUtils.CLIENT_MAP);
            contactMap = (HashMap<String, String>) getIntent().getSerializableExtra(ConstantsUtils.JsonFormKeyUtils.PREVIOUS_VISITS);
            firstName = String.valueOf(detailMap.get(DBConstantsUtils.KeyUtils.FIRST_NAME));
            lastName = String.valueOf(detailMap.get(DBConstantsUtils.KeyUtils.LAST_NAME));
            phoneNumber = String.valueOf(detailMap.get(DBConstantsUtils.KeyUtils.PHONE_NUMBER));
            //nextContactDate = String.valueOf(detailMap.get(DBConstantsUtils.KeyUtils.NEXT_CONTACT_DATE));
            contactDoneDate = String.valueOf(detailMap.get(DBConstantsUtils.KeyUtils.VISIT_START_DATE));
            lastContactDate = String.valueOf(detailMap.get(DBConstantsUtils.KeyUtils.LAST_CONTACT_RECORD_DATE));
            expectedDueDate = String.valueOf(detailMap.get(DBConstantsUtils.KeyUtils.EDD));


            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf.parse(contactDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            nextContactNo = String.valueOf(Utils.getTodayContact(detailMap.get(DBConstantsUtils.KeyUtils.NEXT_CONTACT)) + 2);

            int ncn = Integer.parseInt(nextContactNo);

            float gestationalAgeFloat = Float.parseFloat(gest);
            int ga = (int) gestationalAgeFloat;


            if (ncn < 2 && ga < 22) {
                if (ga < 17) {
                    int weeksToAdd = 20 - ga;
                    calendar.add(Calendar.WEEK_OF_YEAR, weeksToAdd);
                } else if (ga >= 17 && ga < 18) {
                    int weeksToAdd = 22 - ga;
                    calendar.add(Calendar.WEEK_OF_YEAR, weeksToAdd);
                } else if (ga >= 18 && ga < 24) {
                    int weeksToAdd = 26 - ga;
                    calendar.add(Calendar.WEEK_OF_YEAR, weeksToAdd);
                } else if (ga >= 24 && ga < 34) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 4);
                } else if (ga >= 34 && ga < 40) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 2);
                }
            } else {
                if (ga % 2 != 0) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }
                if (ga < 17) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 20 - ga);
                } else if (ga >= 17 && ga < 18) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 22 - ga);
                } else if (ga >= 18 && ga < 24) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 26 - ga);
                } else if (ga >= 24 && ga < 34) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 4);
                } else if (ga >= 34 && ga < 40) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 2);
                }
            }

            nextContactDate = sdf.format(calendar.getTime());
            System.out.println("Next Contact Date: " + nextContactDate);


        }


        Iterable<Object> ruleObjects = AncLibrary.getInstance().readYaml(FilePathUtils.FileUtils.CONTACT_SUMMARY);

        yamlConfigList = new ArrayList<>();
        if(ruleObjects!=null){
            for (Object ruleObject : ruleObjects) {
                YamlConfig yamlConfig = (YamlConfig) ruleObject;
                yamlConfigList.add(yamlConfig);
            }
        }

    }

    public PartialContactRepository getPartialContactRepository() {
        return AncLibrary.getInstance().getPartialContactRepository();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // When user click home menu item then quit this activity.
        if (itemId == android.R.id.home) {
            PatientRepository.updateEDDDate(baseEntityId, null); //Reset EDD
            super.onBackPressed();
        } else if (itemId == R.id.save_finish_menu_item) {
            saveFinishForm();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact_summary_finish_activity, menu);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProfilePresenter.onDestroy(isChangingConfigurations());
    }

    @Override
    protected void onCreation() { //Overriden from Secured Activity
    }

    @Override
    protected void onResumption() {//Overriden from Secured Activity

    }

    private void saveFinishForm() {
        new FinalizeContactTask(new WeakReference<Context>(this), mProfilePresenter, getIntent()).execute();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        saveFinishMenuItem = menu.findItem(R.id.save_finish_menu_item);
        saveFinishMenuItem.setEnabled(false);//initially disable

        return true;
    }

    @Override
    public void setProfileName(String fullName) {
        this.womanName = fullName;
        nameView.setText(fullName);
    }

    @Override
    public void setProfileID(String ancId) {
        ancIdView.setText("ID: " + ancId);
    }

    @Override
    public void setProfileAge(String age) {
        ageView.setText("AGE " + age);

    }

    @Override
    public void setProfileGestationAge(String gestationAge, String gest_age_days_profile) {
        gestationAgeView.setText(gestationAge != null ? "GA: " + gestationAge + " WEEKS" : "GA");
    }

    @Override
    public void setProfileImage(String baseEntityId) {
        imageRenderHelper.refreshProfileImage(baseEntityId, imageView, R.drawable.ic_woman_with_baby);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        //Overridden
    }


    @Override
    public void setTaskCount(String taskCount) {
        // Implement here
    }

    @Override
    public void createContactSummaryPdf(String womanName) {

        /*if (isPermissionGranted() && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {*/
            generateFileinStorage(womanName);
        //}
    }

    public void generateFileinStorage(String womanName)
    {
        try {
            new Utils().createSavePdf(this, yamlConfigList, facts,womanName);
        } catch (FileNotFoundException e) {
            Timber.e(e, "%s createContactSummaryPdf()", this.getClass().getCanonicalName());
        }
    }

    @Override
    public void displayToast(int stringID) {
        Utils.showShortToast(this, this.getString(stringID));
    }

    @Override
    public String getIntentString(String intentKey) {

        return getIntent().getStringExtra(intentKey);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (requestCode == PermissionUtils.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {*/
               loadContactSummaryData();
            /*} else {
                displayToast(R.string.allow_phone_call_management);
            }
        }*/
    }

    protected boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 10 and higher (scoped storage), check for read and write permissions.
            return PermissionUtils.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionUtils.READ_EXTERNAL_STORAGE_REQUEST_CODE)
                    && PermissionUtils.isPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionUtils.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            // For Android versions below R, check for write external storage permission only.
            return PermissionUtils.isPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionUtils.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }


    public List<YamlConfig> getYamlConfigList() {
        return yamlConfigList;
    }
}

