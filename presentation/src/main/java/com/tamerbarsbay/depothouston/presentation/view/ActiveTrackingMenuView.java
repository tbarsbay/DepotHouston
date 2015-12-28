package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.RouteModel;

import java.util.Collection;

/**
 * Represents the menu where a user can enable active tracking of a route's arrivals
 * at a given stop.
 */
public interface ActiveTrackingMenuView {

    String[] DURATION_OPTIONS = new String[]{"10 minutes", "20 minutes", "30 minutes"};
    Integer[] VEHICLE_DISTANCE_OPTIONS = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};

    int DEFAULT_VEHICLE_DISTANCE_INDEX = 4; // Represents a vehicle that's 5 minutes away

    // Methods relating to Active Tracking feature (where the user can
    // track a route's arrivals in their notification shade)

    /**
     * Show all the routes that are serviced at this stop.
     * @param routes
     */
    void populateActiveTrackingRouteOptions(Collection<RouteModel> routes);

    /**
     * Show the options for how long arrivals can be tracked in the notification shade.
     * @param durationOptions
     */
    void populateActiveTrackingDurationOptions(Collection<String> durationOptions);

    /**
     * Show the options for the distance of a vehicle which will trigger the device
     * to ring or vibrate.
     * @param vehicleDistanceOptions
     */
    void populateActiveTrackingVehicleDistanceOptions(Collection<Integer> vehicleDistanceOptions);

    /**
     * Called when the user chooses to enable active tracking for a given route and stop.
     * @param routeId The route to track.
     * @param stopId The stop to track.
     * @param duration The duration for which to track arrivals.
     * @param vehicleDistanceMins (Optional) Ring or vibrate the device every time a vehicle is this many minutes away.
     * @param isVibrateEnabled (Optional) Does the user want device to vibrate whenever a vehicle is minutesAway minutes away?
     * @param isRingEnabled (Optional) Does the user want device to ring whenever a vehicle is minutesAway minutes away?
     */
    void enableActiveTrackingService(String routeId, String routeNum,
                                     String stopId, String stopName,
                                     String duration, int vehicleDistanceMins,
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
