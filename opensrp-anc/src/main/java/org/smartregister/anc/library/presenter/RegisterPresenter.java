package org.smartregister.anc.library.presenter;

import static android.view.View.inflate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.smartregister.anc.library.R;
import org.smartregister.anc.library.activity.BaseHomeRegisterActivity;
import org.smartregister.anc.library.contract.RegisterContract;
import org.smartregister.anc.library.interactor.RegisterInteractor;
import org.smartregister.anc.library.model.RegisterModel;
import org.smartregister.anc.library.repository.PatientRepository;
import org.smartregister.anc.library.util.ConstantsUtils;
import org.smartregister.anc.library.util.Utils;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.FetchStatus;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.LocationPickerView;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

/**
 * Created by keyamn on 27/06/2018.
 */
public class RegisterPresenter implements RegisterContract.Presenter, RegisterContract.InteractorCallBack {
    private WeakReference<RegisterContract.View> viewReference;
    private RegisterContract.Interactor interactor;
    private RegisterContract.Model model;
    private String baseEntityId;

    private RegisterPresenter context;

    public RegisterPresenter(RegisterContract.View view) {
        viewReference = new WeakReference<>(view);
        interactor = new RegisterInteractor();
        model = new RegisterModel();
        context = this;
    }

    public void setModel(RegisterContract.Model model) {
        this.model = model;
    }

    public void setInteractor(RegisterContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void registerViewConfigurations(List<String> viewIdentifiers) {
        model.registerViewConfigurations(viewIdentifiers);
    }

    @Override
    public void unregisterViewConfiguration(List<String> viewIdentifiers) {
        model.unregisterViewConfiguration(viewIdentifiers);
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        viewReference = null;//set to null on destroy
        // Inform interactor
        interactor.onDestroy(isChangingConfiguration);
        // Activity destroyed set interactor to null
        if (!isChangingConfiguration) {
            interactor = null;
            model = null;
        }
    }

    @Override
    public void updateInitials() {
        String initials = model.getInitials();
        if (initials != null) {
            getView().updateInitialsText(initials);
        }
    }

    private RegisterContract.View getView() {
        if (viewReference != null) return viewReference.get();
        else return null;
    }

    @Override
    public void saveLanguage(String language) {
        model.saveLanguage(language);
        getView().displayToast(language + " selected");
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, LocationPickerView locationPickerView)
            throws Exception {
        if (locationPickerView == null || StringUtils.isBlank(locationPickerView.getSelectedItem())) {
            getView().displayToast(R.string.no_location_picker);
        } else {
            String semiCurrentLocationId = model.getLocationId(locationPickerView.getSelectedItem());
            String currentLocationId = semiCurrentLocationId.replaceAll("_", "");
            startForm(formName, entityId, metadata, currentLocationId);
        }
    }


    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception {
        if ("anc_register".equals(formName)) {
            Context context = getView().getContext();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            View view = LayoutInflater.from(context).inflate(R.layout.give_consent_dialog, null);
            alertDialogBuilder.setView(view);

            Button yes = view.findViewById(R.id.consent_yes);
            final Button no = view.findViewById(R.id.consent_no);

            AlertDialog alertDialog = alertDialogBuilder.create();

            yes.setOnClickListener(v -> {
                alertDialog.dismiss();
                if (StringUtils.isBlank(entityId)) {
                    Triple<String, String, String> triple = Triple.of(formName, metadata, currentLocationId);
                    interactor.getNextUniqueId(triple, this);
                    return;
                }

                JSONObject form = null;
                try {
                    form = model.getFormAsJson(formName, entityId, currentLocationId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                getView().startFormActivity(form);
            });

            no.setOnClickListener(v -> {
                alertDialog.dismiss();
                getView().displayToast("Consent was not given, unable to proceed");
            });

            alertDialog.show();
        } else {
            if (StringUtils.isBlank(entityId)) {
                Triple<String, String, String> triple = Triple.of(formName, metadata, currentLocationId);
                interactor.getNextUniqueId(triple, this);
                return;
            }

            JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId);
            getView().startFormActivity(form);
        }
    }

    @Override
    public void saveRegistrationForm(String jsonString, boolean isEditMode) {
        try {
            getView().showProgressDialog(R.string.saving_dialog_title);
            Pair<Client, Event> pair = model.processRegistration(jsonString);
            if (pair == null) {
                return;
            }

            interactor.saveRegistration(pair, jsonString, isEditMode, this);
        } catch (Exception e) {
            Timber.e(e, " --> saveRegistrationForm");
        }
    }

    @Override
    public void closeAncRecord(String jsonString) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(preferences);

            Timber.d(jsonString);
            getView().showProgressDialog(jsonString.contains(ConstantsUtils.EventTypeUtils.CLOSE) ? R.string.removing_dialog_title :
                    R.string.saving_dialog_title);

            interactor.removeWomanFromANCRegister(jsonString, allSharedPreferences.fetchRegisteredANM());
        } catch (Exception e) {
            Timber.e(e, " --> closeAncRecord");

        }
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
//30010-007-uuid/22
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int thisYear = year % 100;
        String facilityID = BaseHomeRegisterActivity.getFacilityID();

        String smNumber = facilityID + "/" +entityId+"/"+thisYear;
        try {
            startForm(triple.getLeft(), smNumber, triple.getMiddle(), triple.getRight());
        } catch (Exception e) {
            Timber.e(e, " --> onUniqueIdFetched");
            getView().displayToast(R.string.error_unable_to_start_form);
        }
    }

    @Override
    public void onNoUniqueId() {
        getView().displayShortToast(R.string.no_openmrs_id);
    }

    @Override
    public void setBaseEntityRegister(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {
        getView().refreshList(FetchStatus.fetched);
        goToClientProfile(baseEntityId);
        getView().hideProgressDialog();
    }

    private void goToClientProfile(String baseEntityId) {
        if (StringUtils.isNotBlank(baseEntityId)) {
            HashMap<String, String> womanProfileDetails = (HashMap<String, String>) PatientRepository.getWomanProfileDetails(baseEntityId);
            if (womanProfileDetails != null) {
                Utils.navigateToProfile(getView().getContext(), womanProfileDetails);
            }
        }
    }
}