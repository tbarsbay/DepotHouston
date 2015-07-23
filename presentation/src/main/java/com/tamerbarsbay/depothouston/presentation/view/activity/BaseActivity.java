package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tamerbarsbay.depothouston.presentation.AndroidApplication;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.ApplicationComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ActivityModule;
import com.tamerbarsbay.depothouston.presentation.navigation.Navigator;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
public class BaseActivity extends ActionBarActivity {

    @Inject
    Navigator navigator;

//    @Inject //TODO temporarily disabled
//    SharedPreferences sharedPreferences;

    private Toolbar mActionBarToolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //getActionBarToolbar(); //todo TEMP DISABLED
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication)getApplication()).getApplicationComponent();
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            //mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar); //TODO temp disabled
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    protected void clearToolbarTitle() {
        getSupportActionBar().setTitle("");
    }

    protected void setToolbarBackArrow() {
        final Toolbar toolbar = getActionBarToolbar();
        //toolbar.setNavigationIcon(R.drawable.icon_arrow_back); //todo temp disabled
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
