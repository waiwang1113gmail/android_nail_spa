package com.waiwang1113.mynailspa.injection;

import com.waiwang1113.mynailspa.AddAppointmentActivity;
import com.waiwang1113.mynailspa.AppointmentListActivity;
import com.waiwang1113.mynailspa.ShopListActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Weige on 2/17/17.
 */

@Singleton
@Component(modules={ApplicationModule.class})
public interface ApplicationComponent {
    void inject(ShopListActivity activity);
    void inject(AppointmentListActivity activity);
    void inject(AddAppointmentActivity activity);
}
