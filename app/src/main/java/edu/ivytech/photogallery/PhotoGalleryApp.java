package edu.ivytech.photogallery;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PhotoGalleryApp extends Application {
    long feedMillis;

    public void setFeedMillis(long feedMillis) {
        this.feedMillis = feedMillis;
    }

    public long getFeedMillis() {
        return feedMillis;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PeriodicWorkRequest photoRequest = new PeriodicWorkRequest
                .Builder(PhotoWorker.class,10, TimeUnit.SECONDS).build();
        final OneTimeWorkRequest photoFileRequest = new OneTimeWorkRequest.Builder(PhotoFileWorker.class)
                .build();

        Executor mainThread = new Executor() {
            @Override
            public void execute(Runnable runnable) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(runnable);
            }
        };
        WorkManager.getInstance(this.getApplicationContext())
                .enqueue(photoRequest).getResult().addListener(new Runnable() {
            @Override
            public void run() {
                WorkManager.getInstance(getApplicationContext()).beginWith(photoFileRequest);
            }
        }, mainThread);

    }
}
