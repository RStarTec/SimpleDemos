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
// In this example, multiple threads compete to leave a message with the UI.
//
// This example use an array of RandomThreads which extend Thread. Host the threads in a fragment.
// Every RandomThread is an individual thread. Thus multiple threads may run in parallel.
// Within each thread, when it's time to update the UI, post a runnable to the handler.
// All runnables run on the same thread when posted to handler.
//
// Reference:
// http://stackoverflow.com/questions/8579657/java-whats-the-difference-between-thread-start-and-runnable-run

public class DemoMultiThreadActivity extends AppCompatActivity {
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
        String message = "Each thread updates UI when it arrives:\n";
        int count = 0;
        Handler handler = new Handler();

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
            textView.setText(message);
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
                    // Now request the UI to update result
                    handler.post(new UiRunnable(value));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Note that all runnables are executed in the same single thread when posted to handler.
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
