package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.RouteModel;

import java.util.Collection;

/**
 * Represents the menu where a user can enable active tracking of a route's arrivals
 * at a given stop.
 */
public interface ActiveTrackingMenuView {

    // Methods relating to Active Tracking feature (where the user can
    // track a route's arrivals in their notification shade)

    /**
     * Show all the routes that are serviced at this stop.
     * @param routes
     */
    void renderActiveTrackingRouteOptions(Collection<RouteModel> routes);

    /**
     * Show the options for how long arrivals can be tracked in the notification shade.
     * @param durationOptions
     */
    void renderActiveTrackingDurationOptions(Collection<String> durationOptions);

    /**
     * Called when the user chooses to enable active tracking for a given route and stop.
     * @param routeId The route to track.
     * @param stopId The stop to track.
     * @param duration The duration for which to track arrivals.
     * @param minutesAway (Optional) Ring or vibrate the device every time a vehicle is this many minutes away.
     * @param isVibrateEnabled (Optional) Does the user want device to vibrate whenever a vehicle is minutesAway minutes away?
     * @param isRingEnabled (Optional) Does the user want device to ring whenever a vehicle is minutesAway minutes away?
     */
    void onActiveTrackingEnabled(String routeId, String stopId,
                                 String duration, String minutesAway,
                                 boolean isVibrateEnabled, boolean isRingEnabled);

    /**
     * Show a loading view while the route options for this stop are loading.
     */
    void showActiveTrackingRoutesLoadingView();

    /**
     * Hide the loading view for the route options at this stop.
     */
    void hideActiveTrackingRoutesLoadingView();

    /**
     * Show a view signaling an error in loading the route options for this stop.
     */
    void showActiveTrackingRoutesErrorView();

    /**
     * Hide the view signaling an error in loading the route options for this stop.
     */
    void hideActiveTrackingRoutesErrorView();

}
