package com.example.reciperecommendation

import android.app.Application
import android.util.Log

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // This logs uncaught exceptions so we can see the real stacktrace in logcat.
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("UncaughtException", "Thread ${thread.name} threw: ${throwable?.message}", throwable)
            // Let the system still handle it after logging
        }
    }
}
