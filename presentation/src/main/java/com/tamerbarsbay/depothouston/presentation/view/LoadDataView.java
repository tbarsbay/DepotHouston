package com.tamerbarsbay.depothouston.presentation.view;

import android.content.Context;

/**
 * Created by Tamer on 7/23/2015.
 */
public interface LoadDataView {

    void showLoading();
    void hideLoading();
    void showRetry();
    void hideRetry();
    void showError(String message);
    Context getContext();

}
