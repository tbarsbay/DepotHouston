package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.StopModel;

import java.util.Collection;

/**
 * Created by Tamer on 7/23/2015.
 */
public interface StopListView extends LoadDataView {

    void renderStopList(Collection<StopModel> stopModels);
    void viewStop(StopModel stopModel);
}
