package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.RecentStopModel;
import com.tamerbarsbay.depothouston.presentation.view.fragment.RecentStopsFragment;

public class RecentStopsActivity
        extends NavigationDrawerActivity
        implements RecentStopsFragment.RecentStopsListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RecentStopsActivity.class);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_RECENT_STOPS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_stops);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_recent_stops_fragment, RecentStopsFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recent_stops, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStopClicked(RecentStopModel stop) {
        if (stop != null) {
            navigator.navigateToArrivalList(this, stop.getStopId(), stop.getName());
        }
    }
}
