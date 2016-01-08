package com.tamerbarsbay.depothouston.presentation.internal.di.components;

import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.WidgetProviderModule;
import com.tamerbarsbay.depothouston.presentation.receiver.WidgetProvider;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {WidgetProviderModule.class})
public interface WidgetProviderComponent {

    void inject(WidgetProvider widgetProvider);

}
