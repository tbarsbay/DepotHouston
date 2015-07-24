package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.HasComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerRouteComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.RouteComponent;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.view.fragment.RouteListFragment;

public class MainActivity extends BaseActivity implements HasComponent<RouteComponent>,
        RouteListFragment.RouteListListener {

    private RouteComponent routeComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeInjector();

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_main_fragment, RouteListFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeInjector() {
        this.routeComponent = DaggerRouteComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public RouteComponent getComponent() {
        return this.routeComponent;
    }

    @Override
    public void onRouteClicked(RouteModel routeModel) {
        this.navigator.navigateToStopList(this, routeModel.getRouteId());
    }
}
