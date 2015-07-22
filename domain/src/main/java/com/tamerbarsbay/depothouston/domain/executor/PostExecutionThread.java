package com.tamerbarsbay.depothouston.domain.executor;

import rx.Scheduler;

/**
 * Created by Tamer on 7/22/2015.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}