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
import com.tamerbarsbay.depothouston.presentation.util.DistanceUtils;
import com.tamerbarsbay.depothouston.presentation.view.MapSearchView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

@PerActivity
public class MapSearchPresenter implements Presenter {

    private MapSearchView mapSearchView;

    private final GetStopsNearLocation getStopsNearLocation;
    private final StopModelDataMapper stopModelDataMapper;

    private double lat;
    private double lon;
    private String centerAddress;

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

    public void initialize(String centerAddress, double lat, double lon, String radiusInMiles) {
        mapSearchView.hideRetryView();
        mapSearchView.hideEmptyView();
        mapSearchView.hideStopsView();
        mapSearchView.showLoadingView();

        this.lat = lat;
        this.lon = lon;
        this.centerAddress = centerAddress;

        getStopsNearLocation.setParameters(lat, lon, radiusInMiles);
        getStopsNearLocation.execute(new NearbyStopsSubscriber());
    }

    public void onStopClicked(StopModel stopModel) {
        mapSearchView.viewStop(stopModel);
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(mapSearchView.getContext(),
                errorBundle.getException());
        mapSearchView.showError(errorMessage);
    }

    private final class NearbyStopsSubscriber extends DefaultSubscriber<List<Stop>> {

        @Override
        public void onNext(List<Stop> stops) {
            final Collection<StopModel> stopModels = stopModelDataMapper.transform(stops);

            // Sort the stops by distance from the current center location
            Collections.sort(
                    (List<StopModel>) stopModels,
                    new StopComparator());

            mapSearchView.clearMap();
            mapSearchView.plotCenterMarker(centerAddress, lat, lon);
            mapSearchView.renderStopList(stopModels);
        }

        @Override
        public void onCompleted() {
            mapSearchView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            mapSearchView.hideLoadingView();
            showErrorMessage(new DefaultErrorBundle((Exception) e));
            mapSearchView.showRetryView();
        }

    }

    private class StopComparator implements Comparator<StopModel> {

        /**
         * Compares two transit stops to see which is further from the current center
         * location. This is used to order the stops before rendering them in a list.
         * @param stop1
         * @param stop2
         * @return
         */
        public int compare(StopModel stop1, StopModel stop2) {
            double stop1Distance = DistanceUtils.calculateDistanceBetweenCoordinates(
                    stop1.getLat(),
                    stop1.getLon(),
                    lat,
                    lon);
            double stop2Distance = DistanceUtils.calculateDistanceBetweenCoordinates(
                    stop2.getLat(),
                    stop2.getLon(),
                    lat,
                    lon);
            return Double.compare(stop1Distance, stop2Distance);
        }
    }

}
