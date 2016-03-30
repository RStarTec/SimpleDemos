/*
 * Copyright (c) 2015,2016 Annie Hui @ RStar Technology Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rstar.mobile.simpledemos;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

// This example provides a Timer as a separate service called TimerService.
// Once created, the service will run on its own to increment a counter on a regular interval.
// The binder inner-class of this service provides a handle for the caller to access this service instance.
// Once bound, the caller may call the getCount() of the service to obtain the counter value.
//
// The counter is incremented using a (handler + runnable) combination.
// This service does not involve UI.

public class TimerService extends Service {
    public static final String TAG = TimerService.class.getSimpleName();

    private final IBinder mBinder = new ServiceBinder();
    private static final long delay = 1000;

    int count = 0;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count++;
            handler.postDelayed(this, delay);
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created. Counting started.");
        handler.postDelayed(runnable, delay);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        Log.d(TAG, "Service destroyed.");
    }

    public int getCount() {
        return count;
    }

    // Provide a means for the caller to get the service instance when binding succeeded.
    public class ServiceBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }
}
