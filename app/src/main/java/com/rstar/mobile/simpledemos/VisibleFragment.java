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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Example of a fragment with visible layout.
// Declare such fragments in a separate class when such fragments have complex logic or are reusable elsewhere.
public class VisibleFragment extends Fragment {
    private static final String EXTRA_message = VisibleFragment.class.getSimpleName() + ".mMessage";

    String mMessage;

    public static VisibleFragment newInstance(String message) {
        Bundle args = new Bundle();
        args.putString(EXTRA_message, message);

        VisibleFragment fragment = new VisibleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessage = getArguments().getString(EXTRA_message);
        Log.d(EXTRA_message, mMessage);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_visible, parent, false);
        TextView textView = (TextView) v.findViewById(R.id.fragment_visible_textview);
        textView.setText(mMessage);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
