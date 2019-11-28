package edu.ivytech.photogallery;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class PhotoWorker extends Worker {

    public PhotoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        FileIO.getFileIO((PhotoGalleryApp)getApplicationContext()).downloadFeed();

        return Result.success();
    }
}
