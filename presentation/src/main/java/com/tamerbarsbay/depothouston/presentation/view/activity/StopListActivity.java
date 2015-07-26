package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.HasComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerStopComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.StopComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.StopModule;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.fragment.StopListFragment;

public class StopListActivity extends BaseActivity implements HasComponent<StopComponent>,
        StopListFragment.StopListListener {

    private static final String INTENT_EXTRA_PARAM_ROUTE_ID = "com.tamerbarsbay.depothouston.INTENT_PARAM_ROUTE_ID";
    private static final String INSTANCE_STATE_PARAM_ROUTE_ID = "com.tamerbarsbay.depothouston.STATE_PARAM_ROUTE_ID";

    private String routeId;
    private StopComponent stopComponent;

    /**
     * Get an Intent that can be used to open up this activity.
     * @param routeId The id of the route for which we are loading stops.
     * @return An Intent to open this activity.
     */
    public static Intent getCallingIntent(Context context, String routeId) {
        Intent intent = new Intent(context, StopListActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_ROUTE_ID, routeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);
        this.initializeActivity(savedInstanceState);
        this.initializeInjector();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_ROUTE_ID, this.routeId);
        }
        super.onSaveInstanceState(outState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.routeId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_ROUTE_ID);
            addFragment(R.id.fl_stop_list_fragment, StopListFragment.newInstance(this.routeId));
        } else {
            this.routeId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_ROUTE_ID);
        }
    }

    private void initializeInjector() {
        this.stopComponent = DaggerStopComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .stopModule(new StopModule(this.routeId))
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stop_list, menu);
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

    @Override
    public StopComponent getComponent() {
        return stopComponent;
    }

    @Override
    public void onStopClicked(StopModel stopModel) {
        //TODO open arrivals screen
    }
}
