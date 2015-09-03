package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.Nullable;

import com.tamerbarsbay.depothouston.domain.Vehicle;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.VehicleModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.VehicleModel;
import com.tamerbarsbay.depothouston.presentation.view.fragment.VehicleListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Tamer on 7/24/2015.
 */
@PerActivity
public class VehicleListPresenter extends DefaultSubscriber<List<Vehicle>> implements Presenter {

    private VehicleListView vehicleListView;

    private final UseCase getVehiclesByRoute;
    private final VehicleModelDataMapper vehicleModelDataMapper;

    @Inject
    public VehicleListPresenter(@Named("vehiclesByRoute") UseCase getVehiclesByRoute,
                                VehicleModelDataMapper vehicleModelDataMapper) {
        this.getVehiclesByRoute = getVehiclesByRoute;
        this.vehicleModelDataMapper = vehicleModelDataMapper;
    }

    public void setView(@Nullable VehicleListView vehicleListView) {
        this.vehicleListView = vehicleListView;
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {
        this.getVehiclesByRoute.unsubscribe();
    }

    public void initialize() {
        this.loadVehicleList();
    }

    private void loadVehicleList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getVehicleList();
    }

    public void onVehicleClicked(VehicleModel vehicleModel) {
        //TODO
    }

    private void showViewLoading() {
        this.vehicleListView.showLoading();
    }

    private void hideViewLoading() {
        this.vehicleListView.hideLoading();
    }

    private void showViewRetry() {
        this.vehicleListView.showRetry();
    }

    private void hideViewRetry() {
        this.vehicleListView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.vehicleListView.getContext(),
                errorBundle.getException());
        this.vehicleListView.showError(errorMessage);
    }

    private void showVehiclesInView(Collection<Vehicle> vehicles) {
        final Collection<VehicleModel> vehicleModels = this.vehicleModelDataMapper.transform(vehicles);
        this.vehicleListView.renderVehicleList(vehicleModels);
    }

    private void getVehicleList() {
        this.getVehiclesByRoute.execute(this);
    }

    @Override
    public void onNext(List<Vehicle> vehicles) {
        this.showVehiclesInView(vehicles);
    }

    @Override
    public void onCompleted() {
        this.hideViewLoading();
    }

    @Override
    public void onError(Throwable e) {
        this.hideViewLoading();
        this.showErrorMessage(new DefaultErrorBundle((Exception)e));
        this.showViewRetry();
    }
}