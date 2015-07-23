package com.tamerbarsbay.depothouston.domain.interactor;

/**
 * Created by Tamer on 7/22/2015.
 */
public class DefaultSubscriber<T> extends rx.Subscriber<T> {

    @Override
    public void onNext(T t) {}

    @Override
    public void onCompleted() {}

    @Override
    public void onError(Throwable e) {}

}
