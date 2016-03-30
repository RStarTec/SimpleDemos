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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

// An example of an activity hosting a visible fragment declared in a separate class.
// Declare fragments in separate classes when such fragments have complex logic or are reusable elsewhere.
public class DemoVisibleFragmentActivity extends AppCompatActivity {
    // This activity hosts a fragment, with a fragment id that is associated to the view of this activity
    Fragment fragment;
    int fragmentId = R.id.activity_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        // Try to find the fragment for this activity, if exists
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(fragmentId);

        // If fragment does not already exist, then create it and attach it.
        if (fragment==null) {
            fragment = VisibleFragment.newInstance("Demo Activity with visible fragment.");
            fm.beginTransaction().add(fragmentId, fragment).commit();
        }
    }
}
