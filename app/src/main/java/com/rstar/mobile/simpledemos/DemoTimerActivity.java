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

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

// An example is to increment a counter regularly and update the UI whenever the counter value has changed.
// This example uses (Timer + TimerTask + Handler + Runnable) combination.
// The Timer with TimerTask are used to schedule counter update at regular intervals.
// The UI update is made using a handler with a runnable.

public class DemoTimerActivity extends AppCompatActivity{
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
        private static final long after = 1;
        private static final long interval = 1000;

        Timer timer;
        TimerTask timerTask;
        int count = 0;
        TextView textView = null;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            // A handler with a runnable to be called at UI thread.
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (textView!=null) {
                        textView.setText("Count = " + count);
                        count++;
                    }
                }
            };

            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    // Need UI thread to do some work.
                    handler.post(runnable);
                }
            };
            // schedule a task to be done repeated at the given interval after the given delay
            timer.schedule(timerTask, after, interval);
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
            timer.cancel();
            timerTask.cancel();
        }
    }
}
