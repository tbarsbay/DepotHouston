package com.tamerbarsbay.depothouston.presentation.presenter;

import com.tamerbarsbay.depothouston.domain.Itinerary;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

@PerActivity
public class BuildItineraryPresenter extends DefaultSubscriber<Itinerary> implements Presenter {

    //TODO

    //TODO this initialize has to have parameters because they can't come from the activity
    //TODO or we can just make it so that the dagger module injection doesn't happen until the user clicks "build" or whatever
    //TODO but we'd have to do null checks around anything with the presenter or just move it around

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onNext(Itinerary itinerary) {
        super.onNext(itinerary);
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }
}
