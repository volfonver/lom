package com.example.lom.player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.lom.R;
import com.example.lom.model.vo.Track;

import java.util.Objects;

public class PlayerService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private static final int NOTIFICATION_ID = 1;
    protected RemoteViews remoteViews;
    protected NotificationCompat.Builder builder;
    protected NotificationManager manager;
    public static PlayerService playerService;

    @Override
    public void onCreate() {
        playerService = this;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onCom Service");
        createNotificationChannel();
        // 5 sec to start ForegroundService - else we catch Exception
        startForeground(NOTIFICATION_ID, new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_cassette).build());

        remoteViews = new RemoteViews(getPackageName(), R.layout.player_notification);
        remoteViews.setOnClickPendingIntent(R.id.previous_track, PendingIntent.getBroadcast(this, 99, new Intent(PlayerReceiver.PREVIEW), 0));
        remoteViews.setOnClickPendingIntent(R.id.next_track, PendingIntent.getBroadcast(this, 99, new Intent(PlayerReceiver.NEXT), 0));
        remoteViews.setOnClickPendingIntent(R.id.pause_track, PendingIntent.getBroadcast(this, 99, new Intent(PlayerReceiver.PAUSE), 0));
        remoteViews.setOnClickPendingIntent(R.id.save_track, PendingIntent.getBroadcast(this, 99, new Intent(PlayerReceiver.SAVE), 0));
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_cassette)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContent(remoteViews)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());


        return START_NOT_STICKY;
    }

    public void updatePlayer(Track track) {
        if (track == null) System.out.println("nnnnnnn");
        remoteViews.setTextViewText(R.id.signer, track.getSinger());
        remoteViews.setTextViewText(R.id.name_track, track.getTitle());

        manager.notify(NOTIFICATION_ID, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("bind service");
        return null;
    }

    protected void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "This is Player",
                    NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
//            notificationChannel.enableVibration(true);

            manager = (NotificationManager) Objects.requireNonNull(getSystemService(Context.NOTIFICATION_SERVICE));
            manager.createNotificationChannel(notificationChannel);
        }
    }
}
