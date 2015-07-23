package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tamerbarsbay.depothouston.R;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

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
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
