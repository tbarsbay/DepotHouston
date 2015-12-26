package com.tamerbarsbay.depothouston.presentation.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.domain.Arrival;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.GetArrivalsByStopAndRoute;
import com.tamerbarsbay.depothouston.presentation.mapper.ArrivalModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;
import com.tamerbarsbay.depothouston.presentation.view.activity.ArrivalListActivity;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * A service that allows a user to track arrivals for a specific route and stop
 * in their notification shade.
 */
public class ActiveTrackingService extends IntentService {

    @Inject
    GetArrivalsByStopAndRoute getArrivalsByStopAndRoute; //TODO will this work

    @Inject
    ArrivalModelDataMapper arrivalModelDataMapper; //TODO ?

    private AlarmManager alarmManager;
    private NotificationManager ntfManager;

    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_CANCEL = "cancel";

    // Keys for Intent extras relating to tracking data
    public static final String KEY_ROUTE_ID = "route_id"; // Id of the route being tracked
    public static final String KEY_ROUTE_NUM = "route"; // Number of the route being tracked
    public static final String KEY_STOP_ID = "stop_id"; // Id of the stop being tracked
    public static final String KEY_STOP_NAME = "stop_name"; // Name of the stop being tracked
    public static final String KEY_UNIQUE_ID = "uid"; // Unique id of the notification being issued
    public static final String KEY_VEHICLE_DISTANCE = "vehicle_distance"; // Optional: vehicle distance at which device will ring or vibrate
    public static final String KEY_ENABLE_RING = "enable_ring"; // Optional: whether the device will ring when a vehicle is XX minutes away
    public static final String KEY_ENABLE_VIBRATE = "enable_vibrate"; // Optional: whether the device will vibrate when a vehicle is XX minutes away
    public static final String KEY_END_TIME = "end_time"; // Time that the active tracking will end

    private String routeId;
    private String routeNum;
    private String stopId;
    private String stopName;
    private int uid;
    private int vehicleDistance;
    private long endUpdatesTimeMillis;
    private boolean enableRing;
    private boolean enableVibrate;
    private boolean keepUpdating = false;

    private static final int REQUEST_CODE_START_SERVICE = 1;
    private static final int REQUEST_CODE_STOP_SERVICE = 0;

    public ActiveTrackingService() {
        super("Active Tracking Service"); //TODO make into string constant
    }

    public static Intent getCallingIntent(@NonNull Context context, String routeId,
                                   String routeNum, String stopId,
                                   String stopName, int uniqueId,
                                   int vehicleDistance, long endUpdatesTimeMillis,
                                   boolean enableRing, boolean enableVibrate) {
        Intent intent = new Intent(context, ActiveTrackingService.class);
        intent.putExtra(KEY_ROUTE_NUM, routeNum);
        intent.putExtra(KEY_ROUTE_ID, routeId);
        intent.putExtra(KEY_STOP_NAME, stopName);
        intent.putExtra(KEY_STOP_ID, stopId);
        intent.putExtra(KEY_UNIQUE_ID, uniqueId);
        intent.putExtra(KEY_VEHICLE_DISTANCE, vehicleDistance);
        intent.putExtra(KEY_END_TIME, endUpdatesTimeMillis);
        intent.putExtra(KEY_ENABLE_RING, enableRing);
        intent.putExtra(KEY_ENABLE_VIBRATE, enableVibrate);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        ntfManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        setValues(intent);

        if (keepUpdating) {
            updateData();
            scheduleNextUpdate();
        } else {
            intent.setAction(ACTION_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getService(
                    this, REQUEST_CODE_START_SERVICE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            ntfManager.cancel(uid); //TODO create a uid
        }
    }

    private void setValues(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            routeId = extras.getString(KEY_ROUTE_ID);
            routeNum = extras.getString(KEY_ROUTE_NUM);
            stopId = extras.getString(KEY_STOP_ID);
            stopName = extras.getString(KEY_STOP_NAME);
            uid = extras.getInt(KEY_UNIQUE_ID);
            vehicleDistance = extras.getInt(KEY_VEHICLE_DISTANCE);
            endUpdatesTimeMillis = extras.getLong(KEY_END_TIME);
            enableRing = extras.getBoolean(KEY_ENABLE_RING);
            enableVibrate = extras.getBoolean(KEY_ENABLE_VIBRATE);
            keepUpdating = shouldWeUpdate(intent);
        }
    }

    private boolean shouldWeUpdate(Intent intent) {
        long currentTimeMillis = System.currentTimeMillis();
        boolean timeLeft = currentTimeMillis < endUpdatesTimeMillis;
        boolean cancelled = ACTION_CANCEL.equals(intent.getAction());
        return (timeLeft && !cancelled);
    }

    private void updateData() {
        getArrivalsByStopAndRoute.execute(new ActiveTrackingSubscriber());
    }

    private void onDataReceived(Collection<ArrivalModel> arrivals) {
        String ntfTitle = buildNotificationTitle((List<ArrivalModel>) arrivals);
        String ntfBody = buildNotificationBody(routeNum, stopName);
        boolean alertUser = isThereVehicleAtThresholdDistance((List<ArrivalModel>) arrivals, vehicleDistance);
        //TODO issue a notification
        //TODO if matchingArrival, make ring or noise depending on which options enabled
    }

    private String buildNotificationTitle(List<ArrivalModel> arrivals) {
        String title = "--, --, --";
        int limit = Math.min(arrivals.size(), 3);
        for (int i = 0; i < limit ; i++) {
            ArrivalModel arrival = arrivals.get(i);
            String minsUntilArrivalString = String.valueOf(arrival.getMinsUntilArrival()) + "m";
            title = title.replaceFirst("--", minsUntilArrivalString);
        }
        return title;
    }

    private String buildNotificationBody(String routeName, String stopName) {
        return routeName + " - " + stopName; //TODO include direction?
    }

    /**
     * Active Tracking includes an additional optional feature where the user's device can
     * be set to ring or vibrate whenever a vehicle is a certain number of minutes away.
     * @param arrivals List of upcoming arrivals to be checked for one that is XX minutes away.
     * @return
     */
    private boolean isThereVehicleAtThresholdDistance(List<ArrivalModel> arrivals, int distanceToCheck) {
        for (ArrivalModel arrival : arrivals) {
            if (arrival.getMinsUntilArrival() == distanceToCheck) {
                return true;
            }
        }
        return false;
    }

    private void issueNotification(String title, String message, int uid, boolean alertUser) {
        NotificationCompat.Builder ntfBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_bus)
                .setColor(getResources().getColor(R.color.primary))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setContentIntent(getNotificationContentIntent())
                .setDeleteIntent(getNotificationDeleteIntent());

        if (alertUser && enableVibrate) {
            ntfBuilder = ntfBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            ntfBuilder = ntfBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ntfBuilder = ntfBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        ntfManager.cancelAll();
        ntfManager.notify(uid, ntfBuilder.build());
    }

    private PendingIntent getNotificationContentIntent() {
        Intent intent = ArrivalListActivity.getCallingIntent(this, stopId, stopName);
        intent = populateStopInfoExtras(intent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent getNotificationDeleteIntent() {
        Intent intent = new Intent(this, ActiveTrackingService.class);
        intent = populateStopInfoExtras(intent);
        intent = populateNotificationInfoExtras(intent);
        intent.setAction(ACTION_CANCEL);
        return PendingIntent.getService(this, REQUEST_CODE_STOP_SERVICE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent populateStopInfoExtras(Intent intent) {
        intent.putExtra(KEY_ROUTE_ID, routeId);
        intent.putExtra(KEY_ROUTE_NUM, routeNum);
        intent.putExtra(KEY_STOP_ID, stopId);
        intent.putExtra(KEY_STOP_NAME, stopName);
        intent.putExtra(KEY_UNIQUE_ID, uid);
        return intent;
    }

    private Intent populateNotificationInfoExtras(Intent intent) {
        intent.putExtra(KEY_VEHICLE_DISTANCE, vehicleDistance);
        intent.putExtra(KEY_END_TIME, endUpdatesTimeMillis);
        intent.putExtra(KEY_ENABLE_RING, enableRing);
        intent.putExtra(KEY_ENABLE_VIBRATE, enableVibrate);
        return intent;
    }

    /**
     * Schedule the service to update the arrival times one minute from now.
     */
    private void scheduleNextUpdate() {
        Intent intent = new Intent(this, ActiveTrackingService.class);
        //TODO populate intent extras
        intent.setAction(ACTION_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getService(this, REQUEST_CODE_START_SERVICE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long currentTimeMillis = SystemClock.elapsedRealtime();
        long nextUpdateTimeMillis = currentTimeMillis + DateUtils.MINUTE_IN_MILLIS;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextUpdateTimeMillis, pendingIntent);
    }

    private final class ActiveTrackingSubscriber extends DefaultSubscriber<List<Arrival>> {

        @Override
        public void onNext(List<Arrival> arrivals) {
            Collection<ArrivalModel> arrivalModels = arrivalModelDataMapper.transform(arrivals);
            onDataReceived(arrivalModels);
        }

        @Override
        public void onCompleted() {
            // Do nothing
            //TODO issue notification here?
        }

        @Override
        public void onError(Throwable e) {
            //TODO show error message? show previous times?
        }
    }
}