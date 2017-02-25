package com.waiwang1113.mynailspa.worker;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Weige on 2/20/17.
 */

public class WorkerThread extends Thread {
    public Handler getHandler() {
        return handler;
    }

    private Handler handler;
    @Override
    public void run(){
        Looper.prepare();
        handler = new Handler();
        Looper.loop();

    }
}
