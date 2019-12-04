package edu.ivytech.photogallery;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.testing.SynchronousExecutor;
import androidx.work.testing.TestDriver;
import androidx.work.testing.WorkManagerTestInitHelper;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.concurrent.TimeUnit.MINUTES;
//import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PhotoWorkerTest {
    private Context myContext;
   // private TestDriver mTestDriver;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        myContext = context;
        WorkManagerTestInitHelper.initializeTestWorkManager(myContext);
    }
    @Test
    public void testPeriodicWork() throws Exception {

        RSSFeed feed = RSSFeed.get();
        PhotoGalleryApp app = (PhotoGalleryApp) myContext;
        FileIO.getFileIO(app).readFile(true);
        app.setFeedMillis(feed.getPubDateMillis());

        // Create request
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(PhotoWorker.class, 15, MINUTES)
                        .build();

        WorkManager workManager = WorkManager.getInstance(myContext);
        TestDriver testDriver;
        testDriver = WorkManagerTestInitHelper.getTestDriver(myContext);
        // Enqueue
        workManager.cancelAllWork();
        workManager.enqueueUniquePeriodicWork("photoRequest", ExistingPeriodicWorkPolicy.KEEP,request)
                .getResult().get();
        // Tells the testing framework the period delay is met
        testDriver.setPeriodDelayMet(request.getId());
        // Get WorkInfo and outputData
        WorkInfo workInfo = workManager.getWorkInfoById(request.getId()).get();
        // Assert
        assertThat(workInfo.getState(), is(WorkInfo.State.ENQUEUED));
    }

}