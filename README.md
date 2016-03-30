## SimpleDemos
Simple Android demos on the use of fragments, asynctasks, threads, runnables, handlers, thread pool and services

### Purpose
For the study of multitasking and multithreading on Android.

### Topics included
- The use of fragment as a host to background threads that may be running independently;
- Simple examples of various types of multithread implementations: AsyncTask; Handler&Runnable; Timer&TimerTask; Threads; ThreadPools; Service;
- Communications between background threads and UI thread.

### Overview

#### 1. Study of the use of fragments

- <b>DemoVisibleFragmentActivity:</b> This activity provides simple basic template for hosting a visible fragment. The visible fragment is declared as a separate class outside of the activity. This approach is suitable for cases where the fragment supports complex operations, or where the fragment is shared by multiple activities, or where the activity needs to be adaptive to various device layouts.
- <b>DemoInvisibleFragmentActivity:</b> This activity hosts an invisible fragment which is declared as a separate class. Invisible fragments are useful for hosting objects that persist in orientation change and do not require a visible layout. In this example, the TextToSpeech module is hosted by the invisible fragment.
- <b>DemoInternalFragmentActivity:</b> This activity provides a simple template for hosting a visible fragment that is declared within the activity. This design approach is suitable when the activity only provides very basic support for the fragment.

#### 2. Study of background tasks

- <b>DemoAsynctaskActivity:</b> In this activity, an internal fragment is used to host an <i>AsyncTask</i>. The internal fragment provides a suitable UI for the AsyncTask to interact with. The <i>AsyncTask</i> is a very specific form of thread for running intensive background jobs, such as disk access and internet. This demo shows the use of the AsyncTask to initialize the LRU disk cache. 
- <b>DemoTimerActivity:</b> This activity implements a counter that increments itself at regular intervals. For such a task, the use of the <i>Timer + TimerTask</i> and <i>Runnable + Handler</i> combination is preferred. The <i>Timer + TimerTask</i> takes care of the scheduling of event while the <i>Runnable + Handler</i> is responsible for invoking UI update.
- <b>DemoQuietTimerActivity:</b> This activity is similar to the previous one, except that the UI update is performed when the background thread completes. 
- <b>DemoRunnableActivity:</b> This activity provides an alternative implementation of the counter incrementation. The Timer and TimerTask are replaceable by the basic <i>Runnable + Handler</i> pair. However, this implementation would require more care.
- <b>DemoThreadActivity:</b> This activity is similar to the previous one except that the Runnable is replaced by a Thread.

#### 3. Study of multithreading

- <b>DemoMultiThreadActivity:</b> This activity hosts an internal fragment which hosts an array of threads. The threads perform the very simple task of logging a message after they sleep for a random amount of time. The UI is invoked when the thread logs its message.
- <b>DemoMultiThreadSyncActivity:</b> This activity is a variation of the previous one. In this example, the threads still log a message after they sleep for a random amount of time. However, the UI is not invoked until the last thread has logged. To protect the logged messages, a lock is used.
- <b>DemoThreadPoolActivity:</b> This activity is similar to the <b>DemoMultiThreadActivity:</b> except that a thread pool is used.

#### 4. Study of services

- <b>DemoServiceActivity:</b> This activity hosts an internal fragment which hosts a local service. The service is a counter. Once the fragment has binded to the service, it sends probes to the service at random times to check the value of the counter.
