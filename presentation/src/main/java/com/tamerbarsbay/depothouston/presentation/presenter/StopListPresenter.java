package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.domain.Stop;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsNearLocationByRoute;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.StopModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.StopListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class StopListPresenter implements Presenter {

    private StopListView stopListView;

    private final UseCase getStopsByRouteUseCase;
    private final GetStopsNearLocationByRoute getStopsNearLocationByRouteUseCase;
    private final StopModelDataMapper stopModelDataMapper;

    private String routeId;
    private double lat;
    private double lon;
    private String radiusInMiles;

    @Inject
    StopListPresenter(@Named("stopsByRoute") UseCase getStopsByRouteUseCase,
                      GetStopsNearLocationByRoute getStopsNearLocationByRouteUseCase,
                      StopModelDataMapper stopModelDataMapper) {
        this.getStopsByRouteUseCase = getStopsByRouteUseCase;
        this.getStopsNearLocationByRouteUseCase = getStopsNearLocationByRouteUseCase;
        this.stopModelDataMapper = stopModelDataMapper;
    }

    public void setView(@NonNull StopListView stopListView) {
        this.stopListView = stopListView;
    }

    @Override public void resume() {}

    @Override public void pause() {}

    @Override public void destroy() {
        getStopsByRouteUseCase.unsubscribe();
    }

    public void retryLastRequest() {
        if (lat == -1 || lon == -1 || radiusInMiles == null) {
            // Last request was to load ALL routes
            loadStopList();
        } else {
            loadNearbyStopsByRoute(routeId, lat, lon, radiusInMiles);
        }
    }

    /**
     * Loads all stops of the given route.
     */
    public void loadStopList() {
        hideViewRetry();
        hideViewEmpty();
        showViewLoading();

        routeId = null;
        lat = -1;
        lon = -1;
        radiusInMiles = null;

        getStopsByRouteUseCase.execute(new StopListSubscriber());
    }

    /**
     * Loads all stops of a given route that are near a given location.
     * @param routeId
     * @param lat
     * @param lon
     * @param radiusInMiles
     */
    public void loadNearbyStopsByRoute(String routeId, double lat, double lon, String radiusInMiles) {
        hideViewRetry();
        hideViewEmpty();
        showViewLoading();

        this.routeId = routeId;
        this.lat = lat;
        this.lon = lon;
        this.radiusInMiles = radiusInMiles;

        getStopsNearLocationByRouteUseCase.setParameters(routeId, lat, lon, radiusInMiles);
        getStopsNearLocationByRouteUseCase.execute(new StopListSubscriber());
    }

    public void onStopClicked(StopModel stopModel) {
        stopListView.viewStop(stopModel);
    }

    private void showViewLoading() {
        stopListView.showLoadingView();
    }

    private void hideViewLoading() {
        stopListView.hideLoadingView();
    }

    private void showViewRetry() {
        stopListView.showRetryView();
    }

    private void hideViewRetry() {
        stopListView.hideRetryView();
    }

    private void showViewEmpty() {
        stopListView.showEmptyView();
    }

    private void hideViewEmpty() {
        stopListView.hideEmptyView();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(stopListView.getContext(),
                errorBundle.getException());
        stopListView.showError(errorMessage);
    }

    private void showStopsInView(Collection<Stop> stops) {
        final Collection<StopModel> stopModels = stopModelDataMapper.transform(stops);
        if (stopModels.isEmpty()) {
            showViewEmpty();
        } else {
            hideViewEmpty();
            stopListView.renderStopList(stopModels);
        };
    }

    private final class StopListSubscriber extends DefaultSubscriber<List<Stop>> {

        @Override
        public void onNext(List<Stop> stops) {
            showStopsInView(stops);
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
