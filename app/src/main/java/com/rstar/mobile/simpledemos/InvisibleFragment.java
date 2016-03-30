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

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;

import java.util.Locale;

// Example of a fragment without a layout.
// This fragment does not need to override onCreateView because there is no view.
// This fragment hosts a TextToSpeech object, which should be immutable to orientation change.
// Declare such fragments in a separate class when such fragments have complex logic or are reusable elsewhere.
//
// Note: The TextToSpeech initialization may take a few seconds.

public class InvisibleFragment extends Fragment {
    private static final String EXTRA_message = InvisibleFragment.class.getSimpleName() + ".message";
    private static String messageId = "messageId";

    TextToSpeech mTextToSpeech;
    String mMessage;

    public static InvisibleFragment newInstance(String message) {
        Bundle args = new Bundle();
        args.putString(EXTRA_message, message);

        InvisibleFragment fragment = new InvisibleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = getArguments().getString(EXTRA_message);

        mTextToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            if (status!=TextToSpeech.ERROR) {
                mTextToSpeech.setLanguage(Locale.ENGLISH);
                // When the fragment is just created, wait for TTS to set up before speaking.
                startSpeaking();
            }
            }
        });
        setRetainInstance(true);
    }

    // There is no view for this fragment.

    @Override
    public void onStart() {
        super.onStart();
        // When fragment is started, start speaking.

        // If onInit() is not yet done, then this call to isSpeaking() will result in error:
        // "speak failed: not bound to TTS engine"
        // Just ignore it for now.
        // Once TTS is initiated, it will speak again whenever screen rotates or wakes up.
        if (!mTextToSpeech.isSpeaking()) {
            startSpeaking();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onStop() {
        super.onStop();
        // When fragment is stopped, stop speaking
        mTextToSpeech.stop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Destroy TTS when fragment is destroyed.
        mTextToSpeech.shutdown();
        mTextToSpeech = null;
    }


    private void startSpeaking() {
        if (mTextToSpeech !=null) {
            // New versions (21 or above)
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                mTextToSpeech.speak(mMessage, TextToSpeech.QUEUE_FLUSH, null, messageId);
            }
            // Old versions (below 21)
            else {
                mTextToSpeech.speak(mMessage, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }
}
