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

// An example is to increment a counter regularly and update the UI only when the counter value has
// reached its final value.
// This example uses (Timer + TimerTask + Handler + Runnable) combination.
// The Timer with TimerTask are used to schedule the counter update at regular intervals.
// The UI update is made using a Handler with a Runnable, only when the final counter value is reached,
// at which point, the timer is stopped.

public class DemoQuietTimerActivity extends AppCompatActivity {
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
        private static final int countLimit = 10;
        int count = 0;

        Timer timer;
        TimerTask timerTask;

        TextView textView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (textView!=null) {
                        textView.setText("Time's up!");
                    }
                }
            };

            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    // When counting, no need to involve UI thread.
                    count++;

                    if (count==countLimit) {
                        timer.cancel();
                        // Counting finished. Now get UI thread to do some work.
                        handler.post(runnable);
                    }
                }
            };
            timer.schedule(timerTask, after, interval);
         }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_visible, parent, false);
            textView = (TextView) v.findViewById(R.id.fragment_visible_textview);
            textView.setText("This text will change when time's up.");
            return v;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (timer!=null) timer.cancel();
            if (timerTask!=null) timerTask.cancel();
        }
    }
}
