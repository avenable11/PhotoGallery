package edu.ivytech.photogallery;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class PhotoFileWorker extends Worker {

    public PhotoFileWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        PhotoGalleryApp app = (PhotoGalleryApp)getApplicationContext();
        FileIO.getFileIO(app).readFile(true);
        RSSFeed feed = RSSFeed.get();

        if(app.getFeedMillis() == feed.getPubDateMillis()) {
            sendNotification("Select to view the current photos");
            return Result.success();
        } else {
            app.setFeedMillis(feed.getPubDateMillis());
            sendNotification("Select to view new photos");

        }
        return Result.success();
    }

    private void sendNotification(String text) {
        Intent notificationIntent = new Intent (this.getApplicationContext(),PhotoGalleryActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,notificationIntent, flags);
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
        NotificationManager manager = (NotificationManager)getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        final int NOTIFICATION_ID = 1;
        manager.notify(NOTIFICATION_ID,notification);


    }
}
