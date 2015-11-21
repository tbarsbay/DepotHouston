package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.domain.Stop;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsNearLocation;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.StopModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.MapSearchView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

@PerActivity
public class MapSearchPresenter implements Presenter {

    private MapSearchView mapSearchView;

    private final GetStopsNearLocation getStopsNearLocation;
    private final StopModelDataMapper stopModelDataMapper;

    private double lat;
    private double lon;
    private String radiusInMiles;

    @Inject
    MapSearchPresenter(GetStopsNearLocation getStopsNearLocation,
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
        hideViewRetry();
        hideViewEmpty();
        mapSearchView.hideStopsView();
        showViewLoading();

        this.lat = lat;
        this.lon = lon;
        this.radiusInMiles = radiusInMiles;

        getStopsNearLocation.setParameters(lat, lon, radiusInMiles);
        getStopsNearLocation.execute(new NearbyStopsSubscriber());
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

    private void showViewEmpty() {
        mapSearchView.showEmpty();
    }

    private void hideViewEmpty() {
        mapSearchView.hideEmpty();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(mapSearchView.getContext(),
                errorBundle.getException());
        mapSearchView.showError(errorMessage);
    }

    private void clearMap() {
        mapSearchView.clearMap();
    }

    private void plotStops(Collection<Stop> stops) {
        final Collection<StopModel> stopModels = stopModelDataMapper.transform(stops);
        mapSearchView.renderStopList(stopModels);
    }

    private void plotCenterMarker() {
        mapSearchView.plotCenterMarker(lat, lon);
    }

    private final class NearbyStopsSubscriber extends DefaultSubscriber<List<Stop>> {

        @Override
        public void onNext(List<Stop> stops) {
            clearMap();
            plotCenterMarker();
            plotStops(stops);
        }

        @Override
        public void onCompleted() {
            hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            hideViewLoading();
            showErrorMessage(new DefaultErrorBundle((Exception) e));
            showViewRetry();
        }

    }

}
