package com.waiwang1113.mynailspa;

import android.app.Application;

import com.waiwang1113.mynailspa.injection.ApplicationComponent;
import com.waiwang1113.mynailspa.injection.ApplicationModule;
import com.waiwang1113.mynailspa.injection.DaggerApplicationComponent;

/**
 * Created by Weige on 2/18/17.
 */

public class MainApplication extends Application {
    private ApplicationComponent mComponeent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponeent = DaggerApplicationComponent.builder()
                // list of modules that are part of this component need to be created here too
                .applicationModule(new ApplicationModule())
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

    public ApplicationComponent getApplicationComponent() {
        return mComponeent;
    }
}
