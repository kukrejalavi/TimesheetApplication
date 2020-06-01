package com.example.timesheetapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private final String ADMIN_CHANNEL_ID ="admin_channel";


    public MyFirebaseMessagingService() {
        super(); // That was the lacking constructor
    }
    @Override
    public void onNewToken(String s) {
        Log.e("NEW_TOKEN", s);
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        Map<String, String> params = remoteMessage.getData();
//        JSONObject object = new JSONObject(params);
//        Log.e("JSON_OBJECT", object.toString());
//
//        String NOTIFICATION_CHANNEL_ID = "Nilesh_channel";
//
//        long pattern[] = {0, 1000, 500, 1000};
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
//                    NotificationManager.IMPORTANCE_HIGH);
//
//            notificationChannel.setDescription("");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(pattern);
//            notificationChannel.enableVibration(true);
//            mNotificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        // to diaplay notification in DND Mode
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
//            channel.canBypassDnd();
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//
//        notificationBuilder.setAutoCancel(true)
//                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
//                .setContentTitle(remoteMessage.getData().get("title"))
//                .setContentText(remoteMessage.getData().get("body"))
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setAutoCancel(true);
//
//
//        mNotificationManager.notify(1000, notificationBuilder.build());






        final Intent intent = new Intent(this, MainActivity.class);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = new Random().nextInt(3000);



      /*

        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications

        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel

      */

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            setupChannels(notificationManager);

        }



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,

                PendingIntent.FLAG_ONE_SHOT);



        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),

                R.drawable.arrowshape);



        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)

                .setSmallIcon(R.drawable.arrowshape)

                .setLargeIcon(largeIcon)

                .setContentTitle(remoteMessage.getData().get("title"))

                .setContentText(remoteMessage.getData().get("message"))

                .setAutoCancel(true)

                .setSound(notificationSoundUri)

                .setContentIntent(pendingIntent);



        //Set notification color to match your app color template

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));

        }

        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    private void setupChannels(NotificationManager notificationManager){

        CharSequence adminChannelName = "New notification";

        String adminChannelDescription = "Device to devie notification";



        NotificationChannel adminChannel;

        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);

        adminChannel.setDescription(adminChannelDescription);

        adminChannel.enableLights(true);

        adminChannel.setLightColor(Color.RED);

        adminChannel.enableVibration(true);

        if (notificationManager != null) {

            notificationManager.createNotificationChannel(adminChannel);

        }

    }
}