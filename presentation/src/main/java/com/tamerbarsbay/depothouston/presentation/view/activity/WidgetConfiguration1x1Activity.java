package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import com.tamerbarsbay.depothouston.R;

import butterknife.ButterKnife;

public class WidgetConfiguration1x1Activity extends WidgetConfigurationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration1x1); //TODO change to other one
        setResult(RESULT_CANCELED);
        //TODO set focus on scrollview??

        ButterKnife.bind(this);
        loadStepOne();
    }

    @Override
    protected void buildWidget() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        //TODO apply selections to preferences
        //TODO turn provider into an interface, then move buildWidget to base config act
        //TODO provider.updateAppWidget(this, widgetManager, widgetId);
        showRefreshHint();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    protected String getWidgetSizeAsString() {
        return "1x1";
    }
}
