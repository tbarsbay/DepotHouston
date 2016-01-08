package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tamerbarsbay.depothouston.domain.Arrival;
import com.tamerbarsbay.depothouston.domain.Route;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.GetRoutesByStop;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.ArrivalModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.mapper.RouteModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.view.ArrivalListView;
import com.tamerbarsbay.depothouston.presentation.view.ActiveTrackingMenuView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class ArrivalListPresenter implements Presenter {

    private ArrivalListView arrivalListView;
    private ActiveTrackingMenuView activeTrackingMenuView;

    private String stopId;

    private final UseCase getArrivalsByStopUseCase;
    private GetRoutesByStop getRoutesByStopUseCase;
    private final ArrivalModelDataMapper arrivalModelDataMapper;
    private final RouteModelDataMapper routeModelDataMapper;

    @Inject
    ArrivalListPresenter(@Named("arrivalsByStop") UseCase getArrivalsByStopUseCase,
                         GetRoutesByStop getRoutesByStopUseCase,
                         ArrivalModelDataMapper arrivalModelDataMapper,
                         RouteModelDataMapper routeModelDataMapper) {
        this.getArrivalsByStopUseCase = getArrivalsByStopUseCase;
        this.getRoutesByStopUseCase = getRoutesByStopUseCase;
        this.arrivalModelDataMapper = arrivalModelDataMapper;
        this.routeModelDataMapper = routeModelDataMapper;
    }

    public void setViews(@NonNull ArrivalListView arrivalListView, ActiveTrackingMenuView activeTrackingMenuView) {
        this.arrivalListView = arrivalListView;
        this.activeTrackingMenuView = activeTrackingMenuView;
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {
        getArrivalsByStopUseCase.unsubscribe();
    }

    public void loadRoutesByStop(String stopId) {
        activeTrackingMenuView.showActiveTrackingRoutesLoadingView();
        activeTrackingMenuView.hideActiveTrackingRoutesErrorView();

        this.stopId = stopId;

        getRoutesByStopUseCase.setParameters(stopId);
        getRoutesByStopUseCase.execute(new RoutesByStopSubscriber());
        Log.d("ArrivalListPresenter", "executing getRoutesByStop use case"); //TODO temp log
    }

    public void loadArrivalList() {
        hideViewRetry();
        showViewLoading();
        //TODO hide empty
        getArrivalList();
    }

    private void showViewLoading() {
        arrivalListView.showLoadingView();
    }

    private void hideViewLoading() {
        arrivalListView.hideLoadingView();
    }

    private void showViewRetry() {
        arrivalListView.showRetryView();
    }

    private void hideViewRetry() {
        arrivalListView.hideRetryView();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(arrivalListView.getContext(),
                errorBundle.getException());
        arrivalListView.showError(errorMessage);
    }

    private void showArrivalsInView(Collection<Arrival> arrivals) {
        final Collection<ArrivalModel> arrivalModels = arrivalModelDataMapper.transform(arrivals);
        Collections.sort((List<ArrivalModel>)arrivalModels, new ArrivalComparator());
        arrivalListView.renderArrivalList(arrivalModels);
    }

    private void showRouteTrackingOptionsInView(Collection<Route> routes) {
        Collection<RouteModel> routeModels = routeModelDataMapper.transform(routes);
        activeTrackingMenuView.populateActiveTrackingRouteOptions(routeModels);
    }

    private void getArrivalList() {
        getArrivalsByStopUseCase.execute(new ArrivalListSubscriber());
    }

    private final class ArrivalListSubscriber extends DefaultSubscriber<List<Arrival>> {

        @Override
        public void onNext(List<Arrival> arrivals) {
            showArrivalsInView(arrivals);
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

    private final class RoutesByStopSubscriber extends DefaultSubscriber<List<Route>> {

        @Override
        public void onNext(List<Route> routes) {
            Log.d("ArrivalListPresenter", "onNext routes by stop: " + routes.size() + " routes"); //TODO temp log
            showRouteTrackingOptionsInView(routes);
        }

        @Override
        public void onCompleted() {
            activeTrackingMenuView.hideActiveTrackingRoutesLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            Log.d("ArrivalListPresenter", "onError routes by stop: " + e.getMessage()); //TODO temp log
            activeTrackingMenuView.hideActiveTrackingRoutesLoadingView();
            activeTrackingMenuView.showActiveTrackingRoutesErrorView();
        }
    }

    private class ArrivalComparator implements Comparator<ArrivalModel> {

        /**
         * Sorts arrivals by arrival time (soonest to latest).
         * @param arrival1
         * @param arrival2
         * @return
         */
        public int compare(ArrivalModel arrival1, ArrivalModel arrival2) {
            return Long.valueOf(arrival1.getMinsUntilArrival())
                    .compareTo(arrival2.getMinsUntilArrival());
        }
    }

}
