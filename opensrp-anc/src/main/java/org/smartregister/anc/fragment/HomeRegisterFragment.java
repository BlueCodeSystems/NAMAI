package org.smartregister.anc.fragment;


import android.view.View;

import org.smartregister.anc.R;
import org.smartregister.anc.activity.BaseRegisterActivity;
import org.smartregister.anc.activity.HomeRegisterActivity;
import org.smartregister.anc.helper.DBQueryHelper;
import org.smartregister.anc.presenter.RegisterFragmentPresenter;
import org.smartregister.anc.view.LocationPickerView;

/**
 * Created by keyman on 26/06/2018.
 */

public class HomeRegisterFragment extends BaseRegisterFragment {

    @Override
    protected void initializePresenter() {
        if (getActivity() == null) {
            return;
        }

        String viewConfigurationIdentifier = ((BaseRegisterActivity) getActivity()).getViewIdentifiers().get(0);
        presenter = new RegisterFragmentPresenter(this, viewConfigurationIdentifier);
    }

    @Override
    protected String getMainCondition() {
        return DBQueryHelper.getHomePatientRegisterCondition();
    }

    public LocationPickerView getLocationPickerView() {
        return getFacilitySelection();
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        View searchButton = view.findViewById(R.id.search_button);
        if (searchButton != null) {
            searchButton.setOnClickListener(registerActionHandler);
        }

        if (filterStatus != null) {
            filterStatus.setOnClickListener(registerActionHandler);
        }

        // QR Code
        View qrCode = view.findViewById(R.id.scan_qr_code);
        if (qrCode != null) {
            qrCode.setOnClickListener(registerActionHandler);
        }

    }

    @Override
    protected void onViewClicked(View view) {
        if (view.getId() == R.id.scan_qr_code) {
            if (getActivity() != null) {
                ((HomeRegisterActivity) getActivity()).startQrCodeScanner();
            }
        } else if (view.getId() == R.id.search_button) {
            if (getActivity() != null) {
                ((HomeRegisterActivity) getActivity()).switchToFragment(1);
            }
        } else if (view.getId() == R.id.filter_status) {
            if (getActivity() != null) {
                ((HomeRegisterActivity) getActivity()).switchToFragment(2);
            }
        }
    }
}
