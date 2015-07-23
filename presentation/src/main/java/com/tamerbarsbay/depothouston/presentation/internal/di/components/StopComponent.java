package com.tamerbarsbay.depothouston.presentation.internal.di.components;

import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ActivityModule;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.StopModule;
import com.tamerbarsbay.depothouston.presentation.view.fragment.StopListFragment;

import dagger.Component;

/**
 * Created by Tamer on 7/23/2015.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, StopModule.class})
public interface StopComponent extends ActivityComponent {

    void inject(StopListFragment stopListFragment);
}
