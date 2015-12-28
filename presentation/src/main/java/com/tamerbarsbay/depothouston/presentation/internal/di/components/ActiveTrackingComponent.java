package com.tamerbarsbay.depothouston.presentation.internal.di.components;

import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ActiveTrackingModule;
import com.tamerbarsbay.depothouston.presentation.service.ActiveTrackingService;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActiveTrackingModule.class})
public interface ActiveTrackingComponent {

    void inject(ActiveTrackingService service);

}
