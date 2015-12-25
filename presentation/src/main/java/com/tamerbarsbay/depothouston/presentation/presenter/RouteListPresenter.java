package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.domain.Route;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.GetRoutesNearLocation;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.RouteModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.view.RouteListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class RouteListPresenter implements Presenter {

    private RouteListView routeListView;

    private final UseCase getRouteListUseCase;
    private final GetRoutesNearLocation getRoutesNearLocationUseCase;
    private final RouteModelDataMapper routeModelDataMapper;

    private double lat;
    private double lon;
    private String radiusInMiles;

    @Inject
    RouteListPresenter(@Named("routeList") UseCase getRouteListUseCase,
                       GetRoutesNearLocation getRoutesNearLocationUseCase,
                       RouteModelDataMapper routeModelDataMapper) {
        this.getRouteListUseCase = getRouteListUseCase;
        this.getRoutesNearLocationUseCase = getRoutesNearLocationUseCase;
        this.routeModelDataMapper = routeModelDataMapper;
    }

    public void setView(@NonNull RouteListView routeListView) {
        this.routeListView = routeListView;
    }

    @Override public void resume() {}

    @Override public void pause() {}

    @Override public void destroy() {
        getRouteListUseCase.unsubscribe();
    }

    public void retryLastRequest() {
        if (lat == -1 || lon == -1 || radiusInMiles == null) {
            // Last request was to load ALL routes
            loadRouteList();
        } else {
            loadNearbyRouteList(lat, lon, radiusInMiles);
        }
    }

    public void loadRouteList() {
        hideViewRetry();
        hideViewEmpty();
        showViewLoading();

        lat = -1;
        lon = -1;
        radiusInMiles = null;

        getRouteListUseCase.execute(new RouteListSubscriber());
    }

    public void loadNearbyRouteList(double lat, double lon, String radiusInMiles) {
        hideViewRetry();
        hideViewEmpty();
        showViewLoading();

        this.lat = lat;
        this.lon = lon;
        this.radiusInMiles = radiusInMiles;

        getRoutesNearLocationUseCase.setParameters(lat, lon, radiusInMiles);
        getRoutesNearLocationUseCase.execute(new RouteListSubscriber());
    }

    public void onRouteClicked(RouteModel routeModel) {
        routeListView.viewRoute(routeModel);
    }

    private void showViewLoading() {
        routeListView.showLoadingView();
    }

    private void hideViewLoading() {
        routeListView.hideLoadingView();
    }

    private void showViewRetry() {
        routeListView.showRetryView();
    }

    private void hideViewRetry() {
        routeListView.hideRetryView();
    }

    private void showViewEmpty() {
        routeListView.showEmptyView();
    }

    private void hideViewEmpty() {
        routeListView.hideEmptyView();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(routeListView.getContext(),
                errorBundle.getException());
        routeListView.showError(errorMessage);
    }

    private void showRoutesInView(Collection<Route> routes) {
        final Collection<RouteModel> routeModels = routeModelDataMapper.transform(routes);
        if (routeModels.isEmpty()) {
            showViewEmpty();
        } else {
            hideViewEmpty();
            routeListView.renderRouteList(routeModels);
        };
    }

    private final class RouteListSubscriber extends DefaultSubscriber<List<Route>> {

        @Override
        public void onNext(List<Route> routes) {
            showRoutesInView(routes);
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
