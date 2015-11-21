package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.StopModel;

import java.util.Collection;

public interface StopListView extends LoadDataView {

    void renderStopList(Collection<StopModel> stopModels);
    void viewStop(StopModel stopModel);
    void showEmpty();
    void hideEmpty();
}
