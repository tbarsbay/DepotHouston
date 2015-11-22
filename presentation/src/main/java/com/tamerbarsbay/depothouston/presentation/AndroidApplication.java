package com.tamerbarsbay.depothouston.presentation;

import android.app.Application;

import com.tamerbarsbay.depothouston.presentation.internal.di.components.ApplicationComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerApplicationComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ApplicationModule;

public class AndroidApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

}
