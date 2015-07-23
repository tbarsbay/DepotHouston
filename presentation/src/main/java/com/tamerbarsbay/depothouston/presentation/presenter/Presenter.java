package com.tamerbarsbay.depothouston.presentation.presenter;

/**
 * Created by Tamer on 7/22/2015.
 */
public interface Presenter {
    /**
     * This should be called in the Activity's or Fragment's onResume() method.
     */
    void resume();

    /**
     * This should be called in the Activity's or Fragment's onPause() method.
     */
    void pause();

    /**
     * This should be called in the Activity's or Fragment's onDestroy() method.
     */
    void destroy();
}
