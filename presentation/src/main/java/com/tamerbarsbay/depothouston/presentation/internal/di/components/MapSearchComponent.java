package com.tamerbarsbay.depothouston.presentation.internal.di.components;

import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ActivityModule;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.MapSearchModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, MapSearchModule.class})
public interface MapSearchComponent extends ActivityComponent {

    //void inject(MapSearchFragment mapSearchFragment); //TODO

}
