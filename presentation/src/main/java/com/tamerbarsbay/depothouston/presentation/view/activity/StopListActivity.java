package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerStopComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.StopComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.StopModule;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.fragment.StopListFragment;

public class StopListActivity extends BaseActivity implements StopListFragment.StopListListener {

    private static final String INTENT_EXTRA_PARAM_ROUTE_ID = "com.tamerbarsbay.depothouston.INTENT_PARAM_ROUTE_ID";
    private static final String INTENT_EXTRA_PARAM_DIRECTION = "com.tamerbarsbay.depothouson.INTENT_PARAM_DIRECTION";
    private static final String INSTANCE_STATE_PARAM_ROUTE_ID = "com.tamerbarsbay.depothouston.STATE_PARAM_ROUTE_ID";
    private static final String INSTANCE_STATE_PARAM_DIRECTION = "com.tamerbarsbay.depothouston.STATE_PARAM_DIRECTION";

    private String routeId;
    private String direction;
    private StopComponent stopComponent;

    /**
     * Get an Intent that can be used to open up this activity.
     * @param routeId The id of the route for which we are loading stops.
     * @return An Intent to open this activity.
     */
    public static Intent getCallingIntent(Context context, String routeId, String direction) {
        Intent intent = new Intent(context, StopListActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_ROUTE_ID, routeId);
        intent.putExtra(INTENT_EXTRA_PARAM_DIRECTION, direction);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);

        initializeActivity(savedInstanceState);
        initializeInjector();

        setToolbarBackArrow();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_ROUTE_ID, routeId);
            outState.putString(INSTANCE_STATE_PARAM_DIRECTION, direction);
        }
        super.onSaveInstanceState(outState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            routeId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_ROUTE_ID);
            direction = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DIRECTION);
            addFragment(R.id.fl_stop_list_fragment, StopListFragment.newInstance(routeId, direction));
        } else {
            routeId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_ROUTE_ID);
            direction = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DIRECTION);
        }
    }

    private void initializeInjector() {
        stopComponent = DaggerStopComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .stopModule(new StopModule(routeId, direction))
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
    public StopComponent getStopComponent() {
        return stopComponent;
    }

    @Override
    public void onStopClicked(StopModel stopModel) {
        navigator.navigateToArrivalList(this, stopModel.getStopId(), stopModel.getName());
    }
}
