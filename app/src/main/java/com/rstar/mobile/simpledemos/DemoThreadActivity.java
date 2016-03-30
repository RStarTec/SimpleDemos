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

// An example to regularly increment a counter and update the UI.
// This example uses only the (Handler + Thread) combination.
// In this example, exactly one plain thread is used (which does about the same thing as a runnable).
// The thread is responsible for scheduling the next event by calling the Handler.post() on itself.
//
// Reference:
// For a difference between implementing runnables and extending threads, see
// http://stackoverflow.com/questions/541487/implements-runnable-vs-extends-thread
// In short:
// by extending Thread, each of your threads has a unique object associated with it,
// whereas implementing Runnable, many threads can share the same object instance.

public class DemoThreadActivity extends AppCompatActivity {
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
        private static final long interval = 1000;

        int count = 0;
        TextView textView = null;

        // A handler with a runnable to be called at UI thread.
        Handler handler = new Handler();

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (textView!=null) {
                    count++;
                    textView.setText("Count = " + count);
                    // After counting, post another call to thread (this) after the same delay.
                }
                // if encountered a view change, skip once count.
                handler.postDelayed(this, interval);
            }
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_visible, parent, false);
            textView = (TextView) v.findViewById(R.id.fragment_visible_textview);
            // Only start when textView is ready
            if (count==0) {
                handler.post(thread);
            } // else, already started.
            return v;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(thread);
        }
    }
}
