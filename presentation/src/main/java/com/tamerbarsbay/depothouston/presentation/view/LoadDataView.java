package com.tamerbarsbay.depothouston.presentation.view;

import android.content.Context;

public interface LoadDataView {

    void showLoadingView();
    void hideLoadingView();
    void showRetryView();
    void hideRetryView();
    void showError(String message);
    Context getContext();

}
