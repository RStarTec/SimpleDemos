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

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// An example of multithreading.
// In this example, multiple threads compete to leave a message with the UI.

// An example of multithreading using thread pool + runnables.
// The thread pool provide the threads. Each such thread needs a runnable (subclassed as RandomRunnable)
// This runnable does not work with the UI.
// When the thread needs to invoke the UI, it needs to post a UiRunnable to the handler.

public class DemoThreadPoolActivity extends AppCompatActivity {
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
        private static final int maxSleepTime = 1000;
        private static final int maxThreads = 10;
        private static final int keepAliveTime = 10;
        private static final TimeUnit timeUnit = TimeUnit.SECONDS;
        private ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(maxThreads);


        TextView textView = null;
        String message = "Each thread updates UI when it arrives:\n";
        int count = 0;
        Handler handler = new Handler();

        // Thread pool
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(maxThreads, maxThreads, keepAliveTime, timeUnit, workQueue);

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            for (int value=0; value<maxThreads; value++) {
                threadPoolExecutor.execute(new RandomRunnable(value));
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_visible, parent, false);
            textView = (TextView) v.findViewById(R.id.fragment_visible_textview);
            textView.setText(message);
            return v;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        // Thread pool provides the thread. Each thread needs its own runnable interface.
        // This runnable does not directly work with the UI.
        public class RandomRunnable implements Runnable {
            int value;
            public RandomRunnable(int value) {
                this.value = value;
            }

            @Override
            public void run() {
                Random rand = new Random();
                long sleepTime = rand.nextInt(maxSleepTime) + 1;
                try {
                    // Sleep for a random amount of time first.
                    Thread.sleep(sleepTime);
                    // Now request the UI to update result
                    handler.post(new UiRunnable(value));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Note that all UI runnables are executed in the same single thread when posted to handler.
        // Therefore, there is no need to worry about protecting the message and count
        // using synchronized.
        public class UiRunnable implements Runnable {
            int value;
            public UiRunnable(int value) {
                this.value = value;
            }
            @Override
            public void run() {
                count++;
                message += "Thread " + value + " arrived at position " + count + ".\n";
                if (textView!=null) {
                    textView.setText(message);
                }
            }
        }
    }

}
