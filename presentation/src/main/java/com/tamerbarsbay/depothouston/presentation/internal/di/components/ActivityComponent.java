package com.tamerbarsbay.depothouston.presentation.internal.di.components;

import android.app.Activity;

import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    // Exposed to subgraphs
    Activity activity();

}
