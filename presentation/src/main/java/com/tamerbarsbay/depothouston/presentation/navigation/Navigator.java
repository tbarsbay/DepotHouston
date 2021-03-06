package com.tamerbarsbay.depothouston.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.presentation.view.activity.ArrivalListActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.MapSearchActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.RecentStopsActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.RouteListActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.SavedStopsActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.SettingsActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.StopListActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {

    @Inject
    public Navigator() {};

    public void navigateToRouteList(Context context, int flags) {
        if (context != null) {
            Intent intent = RouteListActivity.getCallingIntent(context);
            if (flags != -1) {
                intent.setFlags(flags);
            }
            context.startActivity(intent);
        }
    }

    public void navigateToMapSearch(Context context, int flags) {
        if (context != null) {
            Intent intent = MapSearchActivity.getCallingIntent(context);
            if (flags != -1) {
                intent.setFlags(flags);
            }
            context.startActivity(intent);
        }
    }

    public void navigateToSavedStops(Context context, int flags) {
        if (context != null) {
            Intent intent = SavedStopsActivity.getCallingIntent(context);
            if (flags != -1) {
                intent.setFlags(flags);
            }
            context.startActivity(intent);
        }
    }

    public void navigateToRecentStops(Context context, int flags) {
        if (context != null) {
            Intent intent = RecentStopsActivity.getCallingIntent(context);
            if (flags != -1) {
                intent.setFlags(flags);
            }
            context.startActivity(intent);
        }
    }

    public void navigateToSettings(Context context) {
        if (context != null) {
            Intent intent = SettingsActivity.getCallingIntent(context);
            context.startActivity(intent);
        }
    }

    /**
     * Navigate to the stop list screen.
     * @param context Context.
     * @param routeId The id of the route for which we're loading stops.
     */
    public void navigateToStopList(Context context, String routeId, String direction) {
        if (context != null) {
            Intent intent = StopListActivity.getCallingIntent(context, routeId, direction);
            context.startActivity(intent);
        }
    }

    /**
     * Navigate to the arrival list screen.
     * @param context Context.
     * @param stopId The id of the stop for which we're loading stops.
     */
    public void navigateToArrivalList(@NonNull Context context, @NonNull String stopId, @NonNull String stopName) {
        Intent intent = ArrivalListActivity.getCallingIntent(context, stopId, stopName);
        context.startActivity(intent);
    }

}
