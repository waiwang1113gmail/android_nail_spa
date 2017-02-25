package com.waiwang1113.mynailspa.injection;


import com.waiwang1113.mynailspa.repository.DummyEntityRepository;
import com.waiwang1113.mynailspa.repository.EntityRepository;
import com.waiwang1113.mynailspa.request.DummyRequestDispatcher;
import com.waiwang1113.mynailspa.request.RequestDispatcher;
import com.waiwang1113.mynailspa.worker.WorkerThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Weige on 2/17/17.
 */
@Module
public class ApplicationModule {
    @Provides
    @Singleton
    EntityRepository providesEntityRepository() {
        return new DummyEntityRepository();
    }

    @Provides
    @Singleton
    RequestDispatcher providesRequestDispatcher() {
        return new DummyRequestDispatcher();
    }
    @Provides
    @Singleton
    WorkerThread providesWorkerThread() {
        WorkerThread thread = new WorkerThread();
        thread.start();
        return thread;
    }
}
