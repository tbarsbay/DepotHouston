package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.RouteModel;

import java.util.Collection;

/**
 * Created by Tamer on 7/23/2015.
 */
public interface RouteListView extends LoadDataView {

    void renderRouteList(Collection<RouteModel> routeModelCollection);
    void viewRoute(RouteModel routeModel);

}
