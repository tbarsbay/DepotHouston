package com.tamerbarsbay.depothouston.presentation.presenter;

/**
 * Created by Tamer on 7/22/2015.
 */
public interface Presenter {
    /**
     * Called in the Activity/Fragment onResume() method.
     */
    void resume();

    /**
     * Called in the Activity/Fragment onPause() method.
     */
    void pause();

    /**
     * Called in the Activity/Fragment onDestroy() method.
     */
    void destroy();
}
