package edu.ivytech.photogallery;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        PhotoGalleryApp app = (PhotoGalleryApp) getApplicationContext();
        RSSFeed feed = RSSFeed.get();
        FileIO feedIO = FileIO.getFileIO(app);
        Log.d("Photo Gallery", "The worker is trying to download something.");
        feedIO.downloadFeed();
        Log.d("Photo Gallery", "Attempted a download. Unsure of success but made it here!");
        feedIO.readFile(true);
        if (app.getFeedMillis() != feed.getPubDateMillis()) {
            app.setFeedMillis(feed.getPubDateMillis());
            sendNotification("Select to view new photos");
        } else {
            sendNotification("Select to view existing photos");
        }

        return Result.success();
    }

    private void sendNotification(String text) {
        Log.d("Photo Gallery",text + " Notification displayed");
        Intent notificationIntent = new Intent(this.getApplicationContext(), PhotoGalleryActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent, flags);
        int icon = R.drawable.ic_launcher_foreground;
        CharSequence tickerText = "Updated photo feed is available";
        CharSequence contentTitle = getApplicationContext().getText(R.string.app_name);
        CharSequence contentText = text;
        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager manager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        final int NOTIFICATION_ID = 1;
        manager.notify(NOTIFICATION_ID, notification);
    }
}
