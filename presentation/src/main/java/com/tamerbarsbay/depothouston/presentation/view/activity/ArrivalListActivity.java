package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.HasComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.ArrivalComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerArrivalComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ArrivalModule;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;
import com.tamerbarsbay.depothouston.presentation.view.fragment.ArrivalListFragment;

public class ArrivalListActivity extends BaseActivity implements HasComponent<ArrivalComponent>,
        ArrivalListFragment.ArrivalListListener {

    private static final String INTENT_EXTRA_PARAM_STOP_ID = "com.tamerbarsbay.depothouston.INTENT_PARAM_STOP_ID";
    private static final String INSTANCE_STATE_PARAM_STOP_ID = "com.tamerbarsbay.depothouston.STATE_PARAM_STOP_ID";

    private String stopId;
    private ArrivalComponent arrivalComponent;

    /**
     * Get an Intent that can be used to open up this activity.
     * @return An Intent to open this activity.
     */
    public static Intent getCallingIntent(Context context, String stopId) {
        Intent intent = new Intent(context, ArrivalListActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_STOP_ID, stopId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_list);

        initializeActivity(savedInstanceState);
        initializeInjector();

        setToolbarBackArrow();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_STOP_ID, stopId);
        }
        super.onSaveInstanceState(outState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            stopId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_STOP_ID);
            addFragment(R.id.fl_arrival_list_fragment, ArrivalListFragment.newInstance(stopId));
        } else {
            stopId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_STOP_ID);
        }
    }

    private void initializeInjector() {
        arrivalComponent = DaggerArrivalComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .arrivalModule(new ArrivalModule(stopId))
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_arrival_list, menu);
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
    public ArrivalComponent getComponent() {
        return arrivalComponent;
    }

    @Override
    public void onArrivalClicked(ArrivalModel arrivalModel) {
        //TODO follow vehicle??
    }
}
