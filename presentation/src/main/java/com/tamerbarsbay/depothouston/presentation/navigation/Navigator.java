package com.tamerbarsbay.depothouston.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import com.tamerbarsbay.depothouston.presentation.view.activity.StopListActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/23/2015.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {};

    /**
     * Navigate to the stop list screen.
     * @param context Context.
     * @param routeId The id of the route for which we're loading stops.
     */
    public void navigateToStopList(Context context, String routeId) {
        if (context != null) {
            Intent intent = StopListActivity.getCallingIntent(context, routeId);
            context.startActivity(intent);
        }
    }

}
