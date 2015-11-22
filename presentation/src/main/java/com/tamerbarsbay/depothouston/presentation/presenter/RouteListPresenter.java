package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.domain.Route;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
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
    private final RouteModelDataMapper routeModelDataMapper;

    @Inject
    RouteListPresenter(@Named("routeList") UseCase getRouteListUseCase,
                       RouteModelDataMapper routeModelDataMapper) {
        this.getRouteListUseCase = getRouteListUseCase;
        this.routeModelDataMapper = routeModelDataMapper;
    }

    public void setView(@NonNull RouteListView routeListView) {
        this.routeListView = routeListView;
    }

    @Override public void resume() {}

    @Override public void pause() {}

    @Override public void destroy() {
        this.getRouteListUseCase.unsubscribe();
    }

    public void initialize() {
        this.loadRouteList();
    }

    private void loadRouteList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getRouteList();
    }

    public void onRouteClicked(RouteModel routeModel) {
        this.routeListView.viewRoute(routeModel);
    }

    private void showViewLoading() {
        this.routeListView.showLoadingView();
    }

    private void hideViewLoading() {
        this.routeListView.hideLoadingView();
    }

    private void showViewRetry() {
        this.routeListView.showRetryView();
    }

    private void hideViewRetry() {
        this.routeListView.hideRetryView();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.routeListView.getContext(),
                errorBundle.getException());
        this.routeListView.showError(errorMessage);
    }

    private void showRoutesInView(Collection<Route> routes) {
        final Collection<RouteModel> routeModels = this.routeModelDataMapper.transform(routes);
        this.routeListView.renderRouteList(routeModels);
    }

    private void getRouteList() {
        this.getRouteListUseCase.execute(new RouteListSubscriber());
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
