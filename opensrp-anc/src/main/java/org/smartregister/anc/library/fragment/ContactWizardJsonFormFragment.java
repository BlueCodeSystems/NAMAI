package org.smartregister.anc.library.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.vijay.jsonwizard.activities.FormConfigurationJsonFormActivity;
import com.vijay.jsonwizard.fragments.JsonWizardFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import org.smartregister.anc.library.R;
import org.smartregister.anc.library.activity.ContactJsonFormActivity;
import org.smartregister.anc.library.activity.InputFilterMinMax;
import org.smartregister.anc.library.activity.MainContactActivity;
import org.smartregister.anc.library.activity.TestsActivity;
import org.smartregister.anc.library.domain.Contact;
import org.smartregister.anc.library.presenter.ContactWizardJsonFormFragmentPresenter;
import org.smartregister.anc.library.presenter.ProfilePresenter;
import org.smartregister.anc.library.util.ANCFormUtils;
import org.smartregister.anc.library.util.ConstantsUtils;
import org.smartregister.anc.library.util.DBConstantsUtils;
import org.smartregister.anc.library.util.Utils;
import org.smartregister.anc.library.viewstate.ContactJsonFormFragmentViewState;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by ndegwamartin on 30/06/2018.
 */
public class ContactWizardJsonFormFragment extends JsonWizardFormFragment {
    public static final String TAG = ContactWizardJsonFormFragment.class.getName();
    private static final int MENU_NAVIGATION = 100001;
    private boolean savePartial = false;
    private TextView contactTitle;
    private BottomNavigationListener navigationListener = new BottomNavigationListener();
    private ContactWizardJsonFormFragment formFragment;
    public static boolean contactFinished;

    public static JsonWizardFormFragment getFormFragment(String stepName) {
        ContactWizardJsonFormFragment jsonFormFragment = new ContactWizardJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DBConstantsUtils.KeyUtils.STEPNAME, stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contact_json_form_fragment_wizard, null);
        this.mMainView = rootView.findViewById(com.vijay.jsonwizard.R.id.main_layout);
        this.mScrollView = rootView.findViewById(com.vijay.jsonwizard.R.id.scroll_view);

        setupNavigation(rootView);
        setupCustomUI();
        showScrollBars();

        return rootView;
    }

    private Contact getContact() {
        if (getActivity() != null && getActivity() instanceof ContactJsonFormActivity) {
            return ((ContactJsonFormActivity) getActivity()).getContact();
        }else if (getActivity() != null && getActivity() instanceof TestsActivity) {
                return ((TestsActivity) getActivity()).getContact();
        }
        return null;
    }

    /**
     * Shows the form exit dialog message if yes is clicked a partial save of the quick check selection is saved and it
     * redirects back to the main register
     *
     * @author dubdabasoduba
     */
    private void quickCheckClose() {
        AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.AppThemeAlertDialog)
                .setTitle(getJsonApi().getConfirmCloseTitle()).setMessage(getJsonApi().getConfirmCloseMessage())
                .setNegativeButton(R.string.yes, (dialog1, which) -> ((ContactJsonFormActivity) getActivity()).finishInitialQuickCheck()).setPositiveButton(R.string.no, (dialog12, which) -> Timber.d("No button on dialog in %s", FormConfigurationJsonFormActivity.class.getCanonicalName())).create();

        dialog.show();
    }

    @Override
    protected void setupNavigation(View rootView) {
        super.setupNavigation(rootView);
        LinearLayout proceedLayout = rootView.findViewById(R.id.navigation_layout);

        Button previousButton = rootView.findViewById(com.vijay.jsonwizard.R.id.previous);
        ImageView previousIcon = rootView.findViewById(com.vijay.jsonwizard.R.id.previous_icon);

        previousButton.setVisibility(View.INVISIBLE);
        previousIcon.setVisibility(View.INVISIBLE);

        previousButton.setOnClickListener(navigationListener);
        previousIcon.setOnClickListener(navigationListener);

        Button nextButton = rootView.findViewById(com.vijay.jsonwizard.R.id.next);
        ImageView nextIcon = rootView.findViewById(com.vijay.jsonwizard.R.id.next_icon);

        nextButton.setOnClickListener(navigationListener);
        nextIcon.setOnClickListener(navigationListener);

        Button referClose = proceedLayout.findViewById(R.id.refer);
        referClose.setOnClickListener(navigationListener);

        Button proceed = proceedLayout.findViewById(R.id.proceed);
        proceed.setOnClickListener(navigationListener);
    }

    @Override
    protected void setupCustomUI() {
        super.setupCustomUI();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.contact_form_toolbar);
        View view = getSupportActionBar().getCustomView();
        if (view != null) {
            ImageButton goBackButton = view.findViewById(R.id.contact_menu);
            contactTitle = view.findViewById(R.id.contact_title);

            if (getContact() != null && getContact().getBackIcon() > 0 &&
                    getContact().getFormName().equals(ConstantsUtils.JsonFormUtils.ANC_QUICK_CHECK)) {
                goBackButton.setImageResource(R.drawable.ic_clear);
                goBackButton.setOnClickListener(view1 -> quickCheckClose());
            } else {
                goBackButton.setOnClickListener(view12 -> backClick());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setJsonFormFragment(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        Contact form = getContact();
        if (form != null && form.isHideSaveLabel()) {
            updateVisibilityOfNextAndSave(false, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Activity currentActivity = getActivity();
        if (item.getItemId() == R.id.action_save && currentActivity != null) {
            if (currentActivity instanceof ContactJsonFormActivity) {
                ((ContactJsonFormActivity) currentActivity).proceedToMainContactPage();
            } else if (currentActivity instanceof TestsActivity) {
                ((TestsActivity) currentActivity).proceedToMainContactPage();
            } else {
                Timber.e("Unhandled Activity type: %s", currentActivity.getClass().getCanonicalName());
            }
            return true;
        }

        if (item.getItemId() == MENU_NAVIGATION) {
            Toast.makeText(currentActivity, "Right navigation item clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /*public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save && getActivity() != null) {
            ((ContactJsonFormActivity) getActivity()).proceedToMainContactPage();
        }

        if (item.getItemId() == MENU_NAVIGATION) {
            Toast.makeText(getActivity(), "Right navigation item clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }*/

    @Override
    protected ContactJsonFormFragmentViewState createViewState() {
        return new ContactJsonFormFragmentViewState();
    }

    @Override
    protected ContactWizardJsonFormFragmentPresenter createPresenter() {
        return new ContactWizardJsonFormFragmentPresenter(this, JsonFormInteractor.getInstance());
    }

    @Override
    public void setActionBarTitle(String title) {
        Contact contact = getContact();
        if (contact != null) {
            contactTitle.setText(contact.getName());
            if (getStepName() != null) {
                getStepName().setText(title);
            }
        } else {
            contactTitle.setText(title);
        }
    }

    /*@Override
    protected void save() {
        try {
            if (savePartial) {
                if (getActivity() != null) {
                    ((ContactJsonFormActivity) getActivity()).proceedToMainContactPage();
                }
            } else {
                super.save();
            }
        } catch (Exception e) {
            Timber.e(e, "%s --> save", this.getClass().getCanonicalName());
            this.save(false);
        }

    }*/

    @Override
    protected void save() {
        try {
            if (savePartial) {
                Activity currentActivity = getActivity(); // Get the current attached Activity instance
                if (currentActivity instanceof ContactJsonFormActivity) {
                    // If the currentActivity is an instance of ContactJsonFormActivity, cast and call method
                    ((ContactJsonFormActivity) currentActivity).proceedToMainContactPage();
                } else if (currentActivity instanceof TestsActivity) {
                    ((TestsActivity) currentActivity).proceedToMainContactPage();
                }
            } else {
                super.save();
            }
        } catch (Exception e) {
            Timber.e(e, "%s --> save", this.getClass().getCanonicalName());
            this.save(false);
        }
    }

    @Override
    public ContactWizardJsonFormFragment getJsonFormFragment() {
        return formFragment;
    }

    public void setJsonFormFragment(ContactWizardJsonFormFragment formFragment) {
        this.formFragment = formFragment;
    }

    private void displayReferralDialog() {
        if (getActivity() != null) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.alert_referral_dialog, null);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);

            Button yes = view.findViewById(R.id.refer_yes);
            EditText GAValidation = view.findViewById(R.id.add_gest_age);

            final Button no = view.findViewById(R.id.refer_no);

            final AlertDialog dialog = builder.create();

            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams param = window.getAttributes();
                param.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                window.setAttributes(param);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }

            yes.setOnClickListener(v -> {
                EditText gestAgeEditText = view.findViewById(R.id.add_gest_age);
                String ga_ref_value = gestAgeEditText.getText().toString();
                if(gestAgeEditText != null) {
                    /*try {
                        int num = Integer.parseInt(gestAgeEditText.getText().toString());
                        if(num>39){
                            gestAgeEditText.setText("39");
                            ga_ref_value = "39";
                        }else if(num<12){
                            gestAgeEditText.setText("12");
                            ga_ref_value = "12";
                        }
                    } catch (NumberFormatException e) {
                        // Handle the case where the input is not a valid integer
                    }*/
                    //gestAgeEditText.setFilters(new InputFilter[]{new InputFilterMinMax("12", "39")});
                    ProfilePresenter.gest_age_profile = ga_ref_value;
                    Map<String, String> map = new HashMap<>();
                    if (TextUtils.isEmpty(MainContactActivity.formGlobalValues.get(ConstantsUtils.GEST_AGE_OPENMRS))) {
                        map.put(ConstantsUtils.GEST_AGE_OPENMRS, ga_ref_value);
                    }
                }

                saveReferral(dialog);

            });
            no.setOnClickListener(v -> quickCheckClose());

            dialog.show();
        }
    }

    /**
     * Re-directs to the contact finalize page which when refer and close in the quick check container is clicked
     *
     * @param dialog {@link Dialog}
     * @author dubdabasoduba
     */
    private void goToContactFinalize(Dialog dialog) {
        String baseEntityId = getActivity().getIntent().getStringExtra(ConstantsUtils.IntentKeyUtils.BASE_ENTITY_ID);
        Contact contact = getContact();
        if (contact != null) {
            int contactNo = contact.getContactNumber();
            if (contactNo < 0) {
                contact.setContactNumber(contactNo);
            } else {
                contact.setContactNumber(Integer.parseInt("-" + contact.getContactNumber()));
            }
            contact.setJsonForm(((ContactJsonFormActivity) getActivity()).currentJsonState());
            ANCFormUtils.persistPartial(baseEntityId, contact);
        }

        Utils.finalizeForm(getActivity(),
                ((HashMap<String, String>) getActivity().getIntent().getSerializableExtra(ConstantsUtils.IntentKeyUtils.CLIENT_MAP)),
                true);

        dialog.dismiss();
    }

    /**
     * Gets the root layout for the currently visible and finds the bottom refer & proceed layout then displays according to
     * the status of the function parameters
     *
     * @param none  {@link Boolean}
     * @param other {@link Boolean}
     * @author dubdabasoduba
     */
    public void displayQuickCheckBottomReferralButtons(boolean none, boolean other) {
        LinearLayout buttonLayout = getQuickCheckButtonsLayout();

        Button referButton = null;
        Button proceedButton = null;
        if (buttonLayout != null) {
            referButton = buttonLayout.findViewById(R.id.refer);
            proceedButton = buttonLayout.findViewById(R.id.proceed);
        }


        Button finalReferButton = referButton;
        Button finalProceedButton = proceedButton;
        getJsonApi().getAppExecutors().mainThread().execute(() -> {
            setQuickCheckButtonsVisible(none, other, buttonLayout, finalReferButton, finalProceedButton);
            setQuickCheckButtonsInvisible(none, other, buttonLayout, finalReferButton, finalProceedButton);

            if ((none && !other) && buttonLayout != null) {
                finalReferButton.setVisibility(View.GONE);
            }
        });
    }

    //go to main contact  to auto populate gestation age then display summary
    public void saveReferral(Dialog dialog)
    {
        ((ContactJsonFormActivity) getActivity()).proceedToMainContactPage();
        goToContactFinalize(dialog);

    }

    @Nullable
    private LinearLayout getQuickCheckButtonsLayout() {
        LinearLayout linearLayout = (LinearLayout) this.getView();
        LinearLayout buttonLayout = null;
        if (linearLayout != null) {
            buttonLayout = linearLayout.findViewById(R.id.navigation_layout);
        }
        return buttonLayout;
    }

    private void setQuickCheckButtonsVisible(boolean none, boolean other, LinearLayout buttonLayout, Button referButton, Button proceedButton) {
        if ((none || other) && buttonLayout != null) {
            buttonLayout.setVisibility(View.VISIBLE);
            proceedButton.setVisibility(View.VISIBLE);
            if (other) {
                if(contactFinished == false)
                {
                    referButton.setVisibility(View.VISIBLE);
                }else{
                    referButton.setVisibility(View.GONE);
                }
            }
        }
    }
/*
    ramTimed = false;
    public static Boolean phyTimed = false;
    public static Boolean proTimed = false;
    public static Boolean tesTimed = false;
    public static Boolean couTimed = false;
    public static Boolean symTimed*/

    private void setQuickCheckButtonsInvisible(boolean none, boolean other, LinearLayout buttonLayout, Button referButton, Button proceedButton) {
        if ((!none && !other) && buttonLayout != null) {
            buttonLayout.setVisibility(View.GONE);
            proceedButton.setVisibility(View.GONE);
            referButton.setVisibility(View.GONE);
        }
    }

    private class BottomNavigationListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getJsonApi().setPreviousPressed(false);
            if (view.getId() == com.vijay.jsonwizard.R.id.next || view.getId() == com.vijay.jsonwizard.R.id.next_icon) {
                Object tag = view.getTag(com.vijay.jsonwizard.R.id.NEXT_STATE);
                if (tag == null) {
                    getJsonApi().getAppExecutors().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            next();
                        }
                    });
//                    new ANCNextProgressDialogTask(getJsonFormFragment()).execute();
                } else {
                    boolean next = (boolean) tag;
                    if (next) {
                        getJsonApi().getAppExecutors().diskIO().execute(() -> next());
                    } else {
                        savePartial = true;
                        save();
                    }
                }
            } else if (view.getId() == com.vijay.jsonwizard.R.id.previous ||
                    view.getId() == com.vijay.jsonwizard.R.id.previous_icon) {
                assert getFragmentManager() != null;
                getJsonApi().setPreviousPressed(true);
                getFragmentManager().popBackStack();
            } else if (view.getId() == R.id.refer) {
                    displayReferralDialog();
            } else if (view.getId() == R.id.proceed && getActivity() != null) {
                ((ContactJsonFormActivity) getActivity()).proceedToMainContactPage();
                //contactStarted = true;
            }
        }
    }
}

