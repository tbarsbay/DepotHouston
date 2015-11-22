package com.tamerbarsbay.depothouston.presentation.internal.di.components;

import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ActivityModule;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ArrivalModule;
import com.tamerbarsbay.depothouston.presentation.view.fragment.ArrivalListFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, ArrivalModule.class})
public interface ArrivalComponent extends ActivityComponent {

    void inject(ArrivalListFragment arrivalListFragment);

}
