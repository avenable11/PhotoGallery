package edu.ivytech.photogallery;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.work.ExistingPeriodicWorkPolicy;
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
                .Builder(PhotoWorker.class,15, TimeUnit.MINUTES).build();

        WorkManager.getInstance(this.getApplicationContext())
                .enqueueUniquePeriodicWork("photoRequest",ExistingPeriodicWorkPolicy.KEEP,photoRequest);

        //WorkManager.getInstance(this.getApplicationContext()).cancelAllWork();

    }
}
