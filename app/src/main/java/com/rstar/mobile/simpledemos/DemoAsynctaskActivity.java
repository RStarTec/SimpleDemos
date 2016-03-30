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


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

// An example of using a fragment to host an async task.
// The fragment has simple logic. Therefore, it is convenient to declare it within the activity.
// The async task is suitable for doing background jobs such as disk or network access.
// The task done in this example is to initialize the LRU disk cache.
//
// Note that the async task is not appropriate for nested threading or waiting.
// Use threads instead those purpose.

public class DemoAsynctaskActivity extends AppCompatActivity {

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
        private static final int appVersion = 1;
        private static final int valueCount = 5; // number of values allowed in the cache
        private static final long cacheSize = 1024;

        DiskLruCache mDiskCache = null;
        BackgroundTask task;
        String message = "Setting up cache...";

        TextView textView;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Get the directory for the cache
            File cacheDir = new File(getActivity().getCacheDir() + File.separator + "demo");
            // Now initiate the cache
            task = new BackgroundTask(this);
            task.execute(cacheDir);
            setRetainInstance(true);
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
            if (task!=null) task.cancel(true);
        }

        public void initCallback(DiskLruCache diskLruCache) {
            mDiskCache = diskLruCache;
            if (mDiskCache!=null) {
                long size = mDiskCache.size();
                String name = mDiskCache.getDirectory().getAbsolutePath();
                message = "Cache setup successful!\nSize=" + size + "\nAt: " + name;
            }
            else {
                message = "Failed to initiate cache.";
            }
            textView.setText(message);
        }


        public static class BackgroundTask extends AsyncTask<Object, String, DiskLruCache> {
            private static final String TAG = BackgroundTask.class.getSimpleName();
            private final WeakReference<InternalFragment> hostFragmentReference;
            private Exception err;

            public BackgroundTask(InternalFragment hostFragment) {
                hostFragmentReference = new WeakReference<InternalFragment>(hostFragment);
            }

            // The task to be done in the background
            @Override
            protected DiskLruCache doInBackground(Object... params) {
                DiskLruCache diskLruCache;
                File cacheDir = (File) params[0];
                try {
                    diskLruCache = DiskLruCache.open(cacheDir, appVersion, valueCount, cacheSize);
                    return diskLruCache;
                } catch (IOException e) {
                    err = e;
                    return null;
                }
            }

            // Background task may use this to call the foreground thread for update
            @Override
            protected void onProgressUpdate(String... params) {
                String update = params[0];
                Log.d(TAG, update);
            }

            // Return to foreground thread when background task is completed.
            @Override
            protected void onPostExecute(DiskLruCache diskLruCache) {
                final InternalFragment hostFragment = hostFragmentReference.get();
                if (hostFragment!=null) {
                    // Pass the initialized disk cache back to fragment.
                    // cache will be null if initialization failed.
                    hostFragment.initCallback(diskLruCache);
                }
            }
            @Override
            protected void onCancelled() {
                Log.d(TAG, "Task cancelled");
            }
        }
    }
}
