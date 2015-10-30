package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.domain.Stop;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.StopModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.MapSearchView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class MapSearchPresenter extends DefaultSubscriber<List<Stop>> implements Presenter {

    private double lat;
    private double lon;
    private String radiusInMiles;

    private MapSearchView mapSearchView;

    private final UseCase getStopsNearLocation;
    private final StopModelDataMapper stopModelDataMapper;

    @Inject
    MapSearchPresenter(@Named("stopsNearLocation") UseCase getStopsNearLocation,
                      StopModelDataMapper stopModelDataMapper) {
        this.getStopsNearLocation = getStopsNearLocation;
        this.stopModelDataMapper = stopModelDataMapper;
    }

    public void setView(@NonNull MapSearchView mapSearchView) {
        this.mapSearchView = mapSearchView;
    }

    @Override public void resume() {}

    @Override public void pause() {}

    @Override public void destroy() {
        getStopsNearLocation.unsubscribe();
    }

    public void initialize(double lat, double lon, String radiusInMiles) {
        this.lat = lat;
        this.lon = lon;
        this.radiusInMiles = radiusInMiles;
        loadStopList();
    }

    private void loadStopList() {
        hideViewRetry();
        showViewLoading();
        getStopsNearLocation.execute(this);
    }

    public void onStopClicked(StopModel stopModel) {
        mapSearchView.viewStop(stopModel);
    }

    private void showViewLoading() {
        mapSearchView.showLoading();
    }

    private void hideViewLoading() {
        mapSearchView.hideLoading();
    }

    private void showViewRetry() {
        mapSearchView.showRetry();
    }

    private void hideViewRetry() {
        mapSearchView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(mapSearchView.getContext(),
                errorBundle.getException());
        mapSearchView.showError(errorMessage);
    }

    private void plotStops(Collection<Stop> stops) {
        final Collection<StopModel> stopModels = stopModelDataMapper.transform(stops);
        mapSearchView.renderStopList(stopModels);
    }

    @Override
    public void onNext(List<Stop> stops) {
        plotStops(stops);
    }

    @Override
    public void onCompleted() {
        this.hideViewLoading();
    }

    @Override
    public void onError(Throwable e) {
        this.hideViewLoading();
        this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        this.showViewRetry();
    }

}
