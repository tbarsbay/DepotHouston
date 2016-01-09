package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.util.PrefUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkStartScreen();
    }

    private void checkStartScreen() {
        switch (PrefUtils.getStartScreen(this)) {
            case PrefUtils.START_SCREEN_ROUTE_LIST:
                navigator.navigateToRouteList(this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            case PrefUtils.START_SCREEN_MAP_SEARCH:
                navigator.navigateToMapSearch(this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            case PrefUtils.START_SCREEN_SAVED_STOPS:
                navigator.navigateToSavedStops(this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            case PrefUtils.START_SCREEN_RECENT_STOPS:
                navigator.navigateToRecentStops(this, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
        }
    }

}
