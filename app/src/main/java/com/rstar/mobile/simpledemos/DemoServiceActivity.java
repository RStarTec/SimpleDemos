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


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

// This example provides a Timer as a separate service called TimerService.
// The lifespan of TimerService is bound to the lifespan of the fragment hosted in this activity.
// Once created, the service will run on its own to increment a counter on a regular interval.
// This fragment has probe to check counter value in the service at random times.
// The random probe is implemented as a (handler + runnable) combination with access the UI to
// report the result and schedule the next probe based on some random time delay.

public class DemoServiceActivity extends AppCompatActivity {
    Fragment fragment;
    int fragmentId = R.id.activity_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        FragmentManager fm = getSupportFragmentManager();

        fragment = fm.findFragmentById(fragmentId);
        if (fragment==null) {
            fragment = new InternalFragment();
            fm.beginTransaction().add(fragmentId, fragment).commit();
        }
    }

    public static class InternalFragment extends Fragment {
        public static final String TAG = InternalFragment.class.getSimpleName();
        private static final int maxSleepTime = 3000;

        TextView textView = null;


        // Provide connection to the service.
        Context context;
        TimerService timerService;
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
                Log.d(TAG, "Connected to service");
                timerService = ((TimerService.ServiceBinder) serviceBinder).getService();
                if (textView!=null) {
                    textView.setText("Connected to service");
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "Disconnected from service");
                timerService = null;
            }
        };

        // Create a probe that will probe the service for data at random times
        Random rand = new Random();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long sleepTime = rand.nextInt(maxSleepTime) + 1;
                if (timerService!=null) {
                    int count = timerService.getCount();
                    if (textView!=null) {
                        textView.setText("Service reporting count="+count + ".\nNext probe coming in " + sleepTime + "ms.");
                    }
                }
                handler.postDelayed(this, sleepTime);
            }
        };


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            context = getActivity().getApplicationContext();
            // Bind (and automatically create) the service.
            context.bindService(new Intent(context, TimerService.class), connection, BIND_AUTO_CREATE);
            // First post to probe service. Provide a delay.
            handler.postDelayed(runnable, maxSleepTime);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_visible, parent, false);
            textView = (TextView) v.findViewById(R.id.fragment_visible_textview);
            return v;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            // Remove the probe to the service.
            handler.removeCallbacks(runnable);
            // Always stop and unbind any service
            context.unbindService(connection);
            context.stopService(new Intent(context, TimerService.class));
        }
    }
}
