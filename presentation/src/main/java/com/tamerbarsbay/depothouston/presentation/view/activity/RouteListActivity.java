package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerRouteComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.RouteComponent;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.view.fragment.RouteListFragment;

public class RouteListActivity extends NavigationDrawerActivity implements RouteListFragment.RouteListListener {

    private RouteComponent routeComponent;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, RouteListActivity.class);
        return intent;
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_ROUTE_LIST;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        initializeInjector();

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_route_list_fragment, RouteListFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Reset the toolbar in case the user is returning from the Stops screen
        // where they may have turned off the NEARBY ONLY option.
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.d("RouteActivity", "Settings"); //TODO temp
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeInjector() {
        routeComponent = DaggerRouteComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public RouteComponent getRouteComponent() {
        return routeComponent;
    }

    @Override
    public void onRouteClicked(RouteModel routeModel) {
        this.navigator.navigateToStopList(this, routeModel.getRouteId());
    }
}
