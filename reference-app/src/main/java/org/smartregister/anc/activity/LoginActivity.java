package org.smartregister.anc.activity;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.anc.BuildConfig;
import org.smartregister.anc.library.R;
import org.smartregister.anc.library.activity.BaseHomeRegisterActivity;
import org.smartregister.anc.library.activity.SiteCharacteristicsEnterActivity;
import org.smartregister.anc.library.contract.RegisterContract;
import org.smartregister.anc.library.event.ViewConfigurationSyncCompleteEvent;
import org.smartregister.anc.library.util.ConstantsUtils;
import org.smartregister.anc.library.util.Utils;
import org.smartregister.anc.presenter.LoginPresenter;
import org.smartregister.task.SaveTeamLocationsTask;
import org.smartregister.util.PermissionUtils;
import org.smartregister.view.activity.BaseLoginActivity;
import org.smartregister.view.contract.BaseLoginContract;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by ndegwamartin on 21/06/2018.
 */
public class LoginActivity extends BaseLoginActivity implements BaseLoginContract.View {

    private static final String BASE_URL = "http://102.23.123.28:5069";
    static boolean allfiles = false;
    @Override
    protected void onResume() {
        super.onResume();
        mLoginPresenter.processViewCustomizations();

        isPermissionGranted();

        if (!mLoginPresenter.isUserLoggedOut()) {
            goToHome(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViews();
    }

    private void setUpViews() {
        TextView formReleaseTextView = findViewById(R.id.manifest_text_view);
        if (StringUtils.isNotBlank(new Utils().getManifestVersion(this))) {
            formReleaseTextView.setText(new Utils().getManifestVersion(this));
        } else {
            formReleaseTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initializePresenter() {
        mLoginPresenter = new LoginPresenter(this);
    }

    @Override
    public boolean isAppVersionAllowed() {
        return true;
    }

    @Override
    public void goToHome(boolean remote) {
        //setContentView(R.layout.activity_login);
        getContentView();

        EditText usernameEditText = findViewById(R.id.login_user_name_edit_text);
        BaseHomeRegisterActivity.username = usernameEditText.getText().toString();

        EditText passwordEditText = findViewById(R.id.login_password_edit_text);
        BaseHomeRegisterActivity.password = passwordEditText.getText().toString();

        if (remote) {
            Utils.startAsyncTask(new SaveTeamLocationsTask(), null);
        }

        if (mLoginPresenter.isServerSettingsSet()) {
            gotToHomeRegister(remote);
        } else {
            goToSiteCharacteristics(remote);
        }

        finish();
    }



    private void gotToHomeRegister(boolean remote) {
        Intent intent = new Intent(this, BaseHomeRegisterActivity.class);
        intent.putExtra(ConstantsUtils.IntentKeyUtils.IS_REMOTE_LOGIN, remote);
        startActivity(intent);
    }

    private void goToSiteCharacteristics(boolean remote) {
        Intent intent = new Intent(this, SiteCharacteristicsEnterActivity.class);
        intent.putExtra(ConstantsUtils.IntentKeyUtils.IS_REMOTE_LOGIN, remote);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void refreshViews(ViewConfigurationSyncCompleteEvent syncCompleteEvent) {
        if (syncCompleteEvent != null) {
            Timber.d("Refreshing Login View...");
            mLoginPresenter.processViewCustomizations();

        }
    }

    protected boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (!Environment.isExternalStorageManager()) {
                // Permission is not yet granted, show the permission request
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, REQUEST_CODE);
                allfiles = true;
            }
            return true; // If permission is already granted, we continue with true
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 10 and higher (scoped storage), check for read and write permissions.
            if (!Environment.isExternalStorageManager()) {
                // Permission is not yet granted, show the permission request
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, REQUEST_CODE);
                allfiles = true;
            }
            return true;
            //return PermissionUtils.isPermissionGranted(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE, 15140);
        } else {
            // For Android versions below R, check for write external storage permission only.
            return PermissionUtils.isPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionUtils.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    /*private void requestFilePermission() {
        String targetDirectory =  // Replace with your assigned path
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        }

        // Set the initial directory for the user to choose
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.fromFile(new File(targetDirectory)));

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null && result.getData().getData() != null) {
                                Uri directoryUri = result.getData().getData();
                                // Make the permission persistent using takePersistableUriPermission()
                                int modeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    getContentResolver().takePersistableUriPermission(directoryUri, modeFlags);
                                }
                                // Now you have persistent access to the specified directory
                                // You can perform file operations within this directory as needed
                            }
                        }
                    }
                });

        activityResultLauncher.launch(intent);
    }*/

}