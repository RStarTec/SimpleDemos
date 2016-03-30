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

// An example of multithreading.
// In this example, multiple threads compete to leave a message in a shared object called OneOfaKind.
// The message is protected by a synchronized lock. The last thread to leave the message
// also gets to invoke the UI to print the result.
//
// This example use an array of RandomThreads which extend Thread. Host the threads in a fragment.
// Every RandomThread is an individual thread. Thus multiple threads may run in parallel.
// Within each thread, it sleeps for a while, then update the shared object called OneOfaKind.
// The shared object does not involve UI. It is protected using synchronized().
// Only the last thread to finish will invoke the UI to print result using the handler and runnable.

public class DemoMultiThreadSyncActivity extends AppCompatActivity {
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

        TextView textView = null;

        // A single runnable is needed to update UI when the final thread arrives.
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (textView!=null) {
                    textView.setText(oneOfaKind.get()+"Final thread updates UI.\n");
                }
            }
        };

        // A separate object that needs to be protected.
        OneOfaKind oneOfaKind = new OneOfaKind(maxThreads, "Each thread updates protected object when it arrives:\n");

        // Threads
        RandomThread randomThreads[] = new RandomThread[maxThreads];

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            for (int value=0; value<maxThreads; value++) {
                randomThreads[value] = new RandomThread(value);
                randomThreads[value].start(); // start() calls run() internally
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_visible, parent, false);
            textView = (TextView) v.findViewById(R.id.fragment_visible_textview);
            textView.setText(oneOfaKind.get());
            return v;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }


        public class RandomThread extends Thread {
            int value;
            public RandomThread(int value) {
                this.value = value;
            }

            @Override
            public void run() {
                Random rand = new Random();
                long sleepTime = rand.nextInt(maxSleepTime) + 1;
                try {
                    // Sleep for a random amount of time first.
                    Thread.sleep(sleepTime);

                    // Access some object that is protected by synchronized
                    oneOfaKind.set(value);

                    // Now request the UI to update result only when this is the last thread to finish.
                    if (oneOfaKind.isDone()) {
                        handler.post(runnable);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class OneOfaKind {
        int max;
        String message;
        int count = 0;
        Object lock = new Object();

        public OneOfaKind(int max, String message) {
            this.max = max;
            this.message = message;
        }

        public void set(int value) {
            synchronized (lock) {
                count++;
                message += "Thread " + value + " arrived at position " + count + ".\n";
                lock.notifyAll();
            }
        }
        public boolean isDone() {
            return count>=max;
        }
        public String get() {
            return message;
        }
    }
}
