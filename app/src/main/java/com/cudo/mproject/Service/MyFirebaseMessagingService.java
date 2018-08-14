package com.cudo.mproject.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.cudo.mproject.Menu.Login.LoginActivity;
import com.cudo.mproject.Menu.TaskActivity.TaskActivity;
import com.cudo.mproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adsxg on 12/20/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    private static final String TAG = "MyFirebaseMsgService";
    public static String ACTION_MESSAGE ="messege";
    Intent godIntent= new Intent(ACTION_MESSAGE);;
    public int NOTIFICATION = 1337;
    Notification notification;
    Bitmap bitmap;
    // [START receive_message]



    @Override
    public void onCreate() {
        super.onCreate();
        godIntent = new Intent(ACTION_MESSAGE);
        //godIntent.putExtra("status","Created");
        //sendBroadcast(godIntent);
        Log.d(TAG, "onCreate: created ");/*
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
        //buildNotification("You have new task...");
        //startForeground(NOTIFICATION,notification);
        //godIntent.putExtra("body","You have new task...");
        //sendBroadcast(godIntent);


        /*NotificationManager nmc = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < nmc.getActiveNotifications().length; i++) {
                String pkg = nmc.getActiveNotifications()[i].getPackageName();
                int id = nmc.getActiveNotifications()[i].getId();
                String key = nmc.getActiveNotifications()[i].getKey();
                String groupkey = nmc.getActiveNotifications()[i].getGroupKey();
                String tag = nmc.getActiveNotifications()[i].getTag();
                long time = nmc.getActiveNotifications()[i].getPostTime();
                String sorkey = nmc.getActiveNotifications()[i].getNotification().getSortKey();
                Log.d(TAG, "onCreate: "+pkg+"||"+getPackageName()+">>>>|id "+id
                +"|key "+key
                +"|group key "+groupkey
                +"|tag "+tag
                +"|sorkey "+sorkey
                +"|time "+time);
                if(pkg.equals(getPackageName()) && id==0)
                {
                    Log.d(TAG, "onCreate: trigger nmc ");
                    try {
                        Notification notif = nmc.getActiveNotifications()[i].getNotification();
                        godIntent.putExtra("body",getBodyNotif(notif));
                        sendBroadcast(godIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/
    }

    public class LocalBinder extends Binder {
        public MyFirebaseMessagingService getService() {
            return MyFirebaseMessagingService.this;
        }
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //sendNotification(remoteMessage.getData().get("body"));

            godIntent.putExtra("title",remoteMessage.getData().get("body"));

            godIntent.putExtra("body",remoteMessage.getData().get("title"));

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            {
                sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
            }
            else
            {
                sendBroadcast(godIntent);
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification(remoteMessage.getNotification().getBody());
            godIntent.putExtra("title",remoteMessage.getNotification().getTitle());
            godIntent.putExtra("body",remoteMessage.getNotification().getBody());
            sendBroadcast(godIntent);
            //sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title,String messageBody) {
        Log.d(TAG, "sendNotification: dari fb");
        Intent intent = new Intent(this, TaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  , intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher,0)
                .setLargeIcon(icon)
                .setColor(getResources().getColor(R.color.md_white_1000))
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    void buildNotification(String textBody)
    {
        Log.d(TAG, "buildNotification: builded");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("notif","ada");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        CharSequence text = "Service started";
        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(getResources().getColor(R.color.md_white_1000))
                .setSound(defaultSoundUri)
                .setLargeIcon(icon)// the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getString(R.string.app_name))  // the label of the entry
                .setContentText(textBody)  // the contents of the entry
                .setContentIntent(pendingIntent)  // The intent to send when the entry is clicked
                .build();
    }

    String getBodyNotif(Notification notif)
    {
        // Notification notification = (Notification) event.getParcelableData();
        RemoteViews views = notif.contentView;
        Class secretClass = views.getClass();

        try {
            Map<Integer, String> text = new HashMap<Integer, String>();

            Field outerFields[] = secretClass.getDeclaredFields();
            for (int i = 0; i < outerFields.length; i++) {
                if (!outerFields[i].getName().equals("mActions")) continue;

                outerFields[i].setAccessible(true);

                ArrayList<Object> actions = (ArrayList<Object>) outerFields[i]
                        .get(views);
                for (Object action : actions) {
                    Field innerFields[] = action.getClass().getDeclaredFields();

                    Object value = null;
                    Integer type = null;
                    Integer viewId = null;
                    for (Field field : innerFields) {
                        field.setAccessible(true);
                        if (field.getName().equals("value")) {
                            value = field.get(action);
                        } else if (field.getName().equals("type")) {
                            type = field.getInt(action);
                        } else if (field.getName().equals("viewId")) {
                            viewId = field.getInt(action);
                        }
                    }

                    if (type == 9 || type == 10) {
                        text.put(viewId, value.toString());
                    }
                }

                Log.d(TAG, "getBodyNotif: "+"title is: " + text.get(16908310));
                Log.d(TAG, "getBodyNotif: "+"info is: " + text.get(16909082));
                Log.d(TAG, "getBodyNotif: "+"text is: " + text.get(16908358));
                return text.get(16908310);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
}