package com.tamerbarsbay.depothouston.presentation.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.HasComponent;

public abstract class BaseFragment extends Fragment {

    protected static final int REQUEST_CODE_LOCATION_PERMISSION = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showSnackbar(View parentView, String message) {
        if (parentView != null) {
            Snackbar snackbar = Snackbar
                    .make(parentView,
                            message,
                            Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_default));
            snackbar.show();
        }
    }

    protected void showSnackbarError(View parentView, int stringResId) {
        showSnackbarError(parentView, getString(stringResId));
    }

    protected void showSnackbarError(View parentView, String message) {
        if (parentView != null && getResources() != null) {
            Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_error));
            snackbar.show();
        }
    }

    protected void showSnackbar(View parentView, int stringResId) {
        showSnackbar(parentView, getString(stringResId));
    }

    protected void showPermissionRationale(View view, int stringResId) {
        if (view != null) {
            Snackbar snackbar = Snackbar
                    .make(view, stringResId, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.OK, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_LOCATION_PERMISSION);
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.white));
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_default));
            snackbar.show();
        }
    }

    protected boolean hasUserGrantedLocationPermission() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

}
