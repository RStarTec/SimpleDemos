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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    final int ids[] = {
            R.id.demo_fragment_visible,
            R.id.demo_fragment_invisible,
            R.id.demo_fragment_internal,
            R.id.demo_threading_asynctask,
            R.id.demo_threading_timer,
            R.id.demo_threading_quietTimer,
            R.id.demo_threading_runnable,
            R.id.demo_threading_thread,
            R.id.demo_threading_multiThread,
            R.id.demo_threading_multiThreadSync,
            R.id.demo_threading_threadPool,
            R.id.demo_service
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (final int id : ids) {
            Button button = (Button) findViewById(id);
            button.setOnClickListener(new ButtonOnClickListener(this, id));
        }
    }


    private class ButtonOnClickListener implements View.OnClickListener {
        int id;
        Activity hostActivity;
        public ButtonOnClickListener(Activity hostActivity, int id) {
            this.id = id;
            this.hostActivity = hostActivity;
        }

        @Override
        public void onClick(View v) {
            if (id==ids[0]) {
                Intent intent = new Intent(hostActivity, DemoVisibleFragmentActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[1]) {
                Intent intent = new Intent(hostActivity, DemoInvisibleFragmentActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[2]) {
                Intent intent = new Intent(hostActivity, DemoInternalFragmentActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[3]) {
                Intent intent = new Intent(hostActivity, DemoAsynctaskActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[4]) {
                Intent intent = new Intent(hostActivity, DemoTimerActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[5]) {
                Intent intent = new Intent(hostActivity, DemoQuietTimerActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[6]) {
                Intent intent = new Intent(hostActivity, DemoRunnableActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[7]) {
                Intent intent = new Intent(hostActivity, DemoThreadActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[8]) {
                Intent intent = new Intent(hostActivity, DemoMultiThreadActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[9]) {
                Intent intent = new Intent(hostActivity, DemoMultiThreadSyncActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[10]) {
                Intent intent = new Intent(hostActivity, DemoThreadPoolActivity.class);
                hostActivity.startActivity(intent);
            }
            else if (id==ids[11]) {
                Intent intent = new Intent(hostActivity, DemoServiceActivity.class);
                hostActivity.startActivity(intent);
            }
            else {
            }
        }
    }

}
