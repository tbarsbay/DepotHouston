package com.tamerbarsbay.depothouston.presentation.internal.di.components;

import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.ActivityModule;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.RouteModule;
import com.tamerbarsbay.depothouston.presentation.view.fragment.RouteListFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, RouteModule.class})
public interface RouteComponent extends ActivityComponent {

    void inject(RouteListFragment routeListFragment);

}
