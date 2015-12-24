package com.tamerbarsbay.depothouston.presentation.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.RouteComponent;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.presenter.RouteListPresenter;
import com.tamerbarsbay.depothouston.presentation.util.UserLocationManager;
import com.tamerbarsbay.depothouston.presentation.view.RouteListView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.RouteAdapter;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouteListFragment extends BaseFragment
        implements RouteListView, UserLocationManager.UserLocationListener {

    public interface RouteListListener {
        void onRouteClicked(final RouteModel routeModel);
    }

    @Inject
    RouteListPresenter routeListPresenter;

    @Bind(R.id.rv_route_list)
    RecyclerView rvRoutes;

    @Bind(R.id.layout_route_list_parent)
    RelativeLayout layoutParent;

    @Bind(R.id.layout_progress)
    RelativeLayout rlProgress;

    @Bind(R.id.rl_retry)
    RelativeLayout rlRetry;

    @Bind(R.id.btn_retry)
    Button btnRetry;

    @Bind(R.id.layout_route_list_empty)
    LinearLayout layoutEmpty;

    private RouteAdapter routesAdapter;

    private RouteListListener routeListListener;

    private UserLocationManager userLocationManager;

    private SwitchCompat nearbyToggle;

    public RouteListFragment() {}

    public static RouteListFragment newInstance() {
        return new RouteListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RouteListListener) {
            routeListListener = (RouteListListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_route_list, container, false);
        ButterKnife.bind(this, fragmentView);
        rvRoutes.setLayoutManager(new LinearLayoutManager(getActivity()));
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        loadRouteList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION:
                // If the request was cancelled, the results array is empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Granted, get the user's location and show nearby routes
                    getUserLocationAndLoadNearbyRoutes();
                } else {
                    // Denied, reset the nearby toggle
                    resetNearbyToggle();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.action_filter_nearby);
        if (item != null) {
            nearbyToggle = (SwitchCompat) MenuItemCompat.getActionView(item);
            styleNearbyToggle();
            //TODO set checked based on prefs, if checked last time
            nearbyToggle.setOnCheckedChangeListener(onNearbyRoutesToggledListener);
        }
    }

    private void styleNearbyToggle() {
        nearbyToggle.setText(getString(R.string.nearby_only));
        nearbyToggle.setTypeface(null, Typeface.BOLD);
        nearbyToggle.setTextColor(getResources().getColor(R.color.white));

        int sizeInDp = 16;
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        nearbyToggle.setSwitchPadding(dpAsPixels);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter_nearby) {
            // User wants to filter for only nearby stops
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onNearbyRoutesToggled(boolean isChecked) {
        if (isChecked) {
            getUserLocationManager().connect();
        } else {
            getUserLocationManager().disconnect();
            loadRouteList();
        }
    }

    private void getUserLocationAndLoadNearbyRoutes() {
        Location userLocation = getUserLocationManager().getUserLocation();

//        //TODO temp for testing purposes
//        userLocation.setLatitude(29.783133);
//        userLocation.setLongitude(-95.409385);

        if (userLocation != null) {
            loadNearbyRouteList(
                    userLocation.getLatitude(),
                    userLocation.getLongitude(),
                    ".25"); //TODO temp, use prefs
        } else {
            showError(getString(R.string.error_invalid_user_location_tap_feature_remains));
            resetNearbyToggle();
        }
    }

    private UserLocationManager getUserLocationManager() {
        if (userLocationManager == null) {
            userLocationManager = new UserLocationManager(getContext(), this);
        }
        return userLocationManager;
    }

    @Override
    public void onConnected() {
        if (Build.VERSION.SDK_INT < 23) {
            // User has Lollipop or below, no need to request permissions
            // Just get user location and load nearby stops
            getUserLocationAndLoadNearbyRoutes();
        } else {
            if (!hasUserGrantedLocationPermission()) {
                // User has not granted location permission
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showPermissionRationale(layoutParent, R.string.location_permission_rationale_nearby_routes);
                } else {
                    showSnackbar(layoutParent, R.string.enable_location_permission_to_see_nearby_routes);
                    resetNearbyToggle();
                }
            } else {
                // User has granted location permission
                getUserLocationAndLoadNearbyRoutes();
            }
        }
    }

    @Override
    public void onConnectionFailed() {
        showError(getString(R.string.error_invalid_user_location_tap_feature_remains));
        resetNearbyToggle();
    }

    private void resetNearbyToggle() {
        if (nearbyToggle != null) {
            nearbyToggle.setChecked(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        routeListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        routeListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        routeListPresenter.destroy();
    }

    private void initialize() {
        getComponent(RouteComponent.class).inject(this);
        routeListPresenter.setView(this);
    }

    @Override
    public void showLoadingView() {
        rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {
        rlRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetryView() {
        rlRetry.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
        layoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        layoutEmpty.setVisibility(View.GONE);
    }

    @Override
    public void renderRouteList(Collection<RouteModel> routeModels) {
        if (routeModels != null) {
            if (routesAdapter == null) {
                routesAdapter = new RouteAdapter(getActivity(), routeModels);
            } else {
                routesAdapter.setRoutesCollection(routeModels);
            }
            routesAdapter.setOnItemClickListener(onItemClickListener);
            rvRoutes.setAdapter(routesAdapter);
        }
    }

    @Override
    public void viewRoute(RouteModel routeModel) {
        if (routeListListener != null) {
            routeListListener.onRouteClicked(routeModel);
        }
    }

    @Override public void showError(String message) {
        showToastMessage(message);
    }

    private void loadRouteList() {
        routeListPresenter.loadRouteList();
    }

    private void loadNearbyRouteList(double lat, double lon, String radiusInMiles) {
        routeListPresenter.loadNearbyRouteList(lat, lon, radiusInMiles);
    }

    @OnClick(R.id.btn_retry)
    void onButtonRetryClick() {
        routeListPresenter.retryLastRequest();
    }

    private RouteAdapter.OnItemClickListener onItemClickListener =
            new RouteAdapter.OnItemClickListener() {
                @Override
                public void onRouteItemClicked(RouteModel routeModel) {
                    if (RouteListFragment.this.routeListPresenter != null && routeModel != null) {
                        RouteListFragment.this.routeListPresenter.onRouteClicked(routeModel);
                    }
                }
            };

    private CompoundButton.OnCheckedChangeListener onNearbyRoutesToggledListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onNearbyRoutesToggled(isChecked);
                }
            };
}
