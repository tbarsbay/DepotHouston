package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import android.app.Activity;

import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity getActivity() {
        return this.activity;
    }

}
