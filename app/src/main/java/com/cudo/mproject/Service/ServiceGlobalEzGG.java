package com.cudo.mproject.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;

import com.cudo.mproject.Menu.Login.LoginActivity;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.SessionManagerGPS;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by adsxg on 12/20/2017.
 */

public class ServiceGlobalEzGG  extends Service {
    Intent intentFirebase ;
    Intent intentConnectivy;
    private static final String TAG = ServiceGlobalEzGG.class.getSimpleName();
    private NotificationManager mNM;
    Service messageService;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    public int NOTIFICATION = 1174;
    boolean serviceBound=false;
    boolean constatusBound=false;
    Notification notification;
    public int counter=0;
    Service gpsService;
    boolean gpsBound=false;
    // GPSReciever gpsReciever;
    MessegeReciever msreciever;
    List<String> ntfMsg = new ArrayList<>();
    public static String GPSSENDFromGGTY = "GPSSENDggty";
    ConnReceiver connReceiver;
    public static String ConnString="GANZSTRING";
    //gps
    String tag = "";
    SessionManagerGPS smGPS;
    public Location mLastLocation;
    private static final int TWO_MINUTES = 1000 * 30;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */

    public ServiceGlobalEzGG(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public ServiceGlobalEzGG(){

    }
    public class LocalBinder extends Binder {
        public ServiceGlobalEzGG getService() {
            return ServiceGlobalEzGG.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // Display a notification about us starting.  We put an icon in the status bar.
        intentFirebase = new Intent(this,
                MyFirebaseMessagingService.class);
        intentConnectivy = new Intent(this,ConnectivityReceiverService.class);
        initMessageService();
        //showNotification();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand Received start id " + startId + ": " + intent+" : "+gpsBound);
        //startTimer();
        return START_STICKY;
    }
    public boolean isForeground(String myPackage) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        Log.d(TAG, "onStartCommand isForeground: "+componentInfo.getPackageName()+" "+myPackage);
        return componentInfo.getPackageName().equals(myPackage);
    }
    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
                // showNotification(String.valueOf(counter));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            messageService = ((MyFirebaseMessagingService.LocalBinder)service).getService();
            serviceBound=true;
            Log.d(TAG, "onServiceConnected: message");

        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "onDisconnected: message");
            stopService(intentFirebase);
            serviceBound=false;
            mConnection = null;
        }
    };


    void initMessageService(){
        if (!serviceBound) {
            Log.d(TAG, "initMessageService: created");
            msreciever = new MessegeReciever();





            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MyFirebaseMessagingService.ACTION_MESSAGE);
            registerReceiver(msreciever, intentFilter);

            startService(intentFirebase);
            bindService(intentFirebase, mConnection, Context.BIND_AUTO_CREATE);
            serviceBound = true;
            buildNotification("Connected");
            startForeground(NOTIFICATION,notification);
            initConnectionStatus();
            LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        }
    }
    void initConnectionStatus(){
        if (!constatusBound) {
            connReceiver = new ConnReceiver();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnString);
            startService(intentConnectivy);
            registerReceiver(connReceiver, intentFilter);

            constatusBound = true;
            //buildNotification("Connected");
            //startForeground(NOTIFICATION,notification);
        }
    }
    void stopMessageService(){
        if(serviceBound)
        {
            try {
                stopService(intentFirebase);
                unregisterReceiver(msreciever);
                unbindService(mConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                serviceBound=false;
            }
        }
    }
    void stopConnService(){
        if(constatusBound)
        {

            try {
                stopService(intentConnectivy);
                unregisterReceiver(connReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String title = intent.getStringExtra("title");
                String body = intent.getStringExtra("text");

                boolean cansend =false;
                if(!tag.equals(intent.getStringExtra("tag")))
                {
                    tag=intent.getStringExtra("tag");
                    cansend=true;
                }
                Log.d(TAG, "onReceive: notice"+"||"+title+"||"+body);
                if(cansend && (body!="Connected" || body!="offline mode"))
                {

                    ntfMsg.add(title+", "+body);
                    Log.d(TAG, "onReceive: "+ntfMsg.size());

                    if(ntfMsg.size()>1)
                    {
                        sendNotificationV2(ntfMsg);
                    }
                    else
                    {
                        sendNotification(title,body);
                    }
                    cansend=false;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

    private class MessegeReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            //int datapassed = arg1.getIntExtra("DATAPASSED", 0);

            Log.d(TAG, "onReceive: MyFirebaseMsgService > Servicew"+arg1.getExtras());

            if(arg1.getStringExtra("title")!=null &&arg1.getStringExtra("body")!=null)
            {

                String title= arg1.getStringExtra("title");
                String body= arg1.getStringExtra("body");
                ntfMsg.add(title+", "+body);
                Log.d(TAG, "onReceive: "+ntfMsg.size());

                if(ntfMsg.size()>1)
                {
                    sendNotificationV2(ntfMsg);
                }
                else
                {
                    sendNotification(title,body);
                }

            }else
            {
                if(arg1.getStringExtra("body")!=null)
                {
                    String title= getString(R.string.app_name);
                    String body= arg1.getStringExtra("body");
                    ntfMsg.add(title+", "+body);
                    Log.d(TAG, "onReceive: "+ntfMsg.size());

                    if(ntfMsg.size()>1)
                    {
                        sendNotificationV2(ntfMsg);
                    }
                    else
                    {
                        sendNotification(title,body);
                    }
                }
            }

            //GPSacc=Float.parseFloat(loc);
            //UpdateGPS(GPSacc);


        }
    }
    private class ConnReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: con rece trigger"+intent.getExtras());
            String sv= "Offline Mode";
            if(intent.getExtras()!=null && intent.getBooleanExtra("data",false))
            {
                Log.d(TAG, "onReceive: con rece trigger"+intent.getBooleanExtra("data",false));
                sv="Connected";
            }
            if(ntfMsg.size()==0)
            {
                buildNotification(sv);
                mNM.notify(NOTIFICATION,notification);
            }

        }

    }

   /* private class GPSReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            //int datapassed = arg1.getIntExtra("DATAPASSED", 0);
            Log.d(TAG, "onReceive: GPS ada msg");


            if(arg1.getStringExtra("GPS")!=null)
            {
                Intent intent = new Intent(GPSSENDFromGGTY);
                intent.putExtra("GPS",arg1.getStringExtra("GPS"));
                sendBroadcast(intent);
            }

            if(arg1.getStringExtra("status")!=null)
            {
                Log.d(TAG, "onReceive: GPS ready");
            }

            //GPSacc=Float.parseFloat(loc);
            //UpdateGPS(GPSacc);


        }
    }*/

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        // Tell the user we stopped.
        stoptimertask();

        // Tell the user we stopped.

        Intent broadcastIntent = new Intent("RestartSensor");
        sendBroadcast(broadcastIntent);
        stopMessageService();
        stopConnService();
        //stopForeground(true);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
  /*  @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();*/

    /**
     * Show a notification while this service is running.
     */

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
                .setLargeIcon(icon)// the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getString(R.string.app_name))  // the label of the entry
                .setContentText(textBody)  // the contents of the entry
                .setContentIntent(pendingIntent)  // The intent to send when the entry is clicked
                .build();
    }
    private void showNotification(String textBody) {
        // In this sample, we'll use the same text for the ticker and the expanded notification

        // The PendingIntent to launch our activity if the user selects this notification
        // Set the info for the views that show in the notification panel.

        // Send the notification.
        buildNotification(textBody);
        mNM.notify(NOTIFICATION, notification);
    }
    private void sendNotification(String title,String messageBody) {
        Log.d(TAG, "sendNotification: ");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setPriority(2)
                .setColor(getResources().getColor(R.color.md_white_1000))
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        /* Add Big View Specific Configuration */

        // NotificationCompat.MessagingStyle msgStyle = new NotificationCompat.MessagingStyle();

       /* NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);*/

        //mNM.notify(NOTIFICATION, notification);
        startForeground(NOTIFICATION,notification.build());
    }
    private void sendNotificationV2(List<String> msg) {
        Log.d(TAG, "sendNotification: ");
        String msgbody = "You have "+msg.size()+" Task";
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("notif","ada");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setPriority(2)
                .setColor(getResources().getColor(R.color.md_white_1000))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msgbody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();



        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle(msgbody);

        // Moves events into the big view
        for (int i=0; i < msg.size(); i++) {

            String value = msg.get(i);
            Spannable sb = new SpannableString(value);/*
            sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 8, 40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
            inboxStyle.addLine(value);
            //inboxStyle.addLine(msg.get(i));
            Log.d(TAG, "sendNotificationV2: "+msg.get(i));
        }

        notification.setStyle(inboxStyle);

        // NotificationCompat.MessagingStyle msgStyle = new NotificationCompat.MessagingStyle();

       /* NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);*/

        //mNM.notify(NOTIFICATION, notification);
        startForeground(NOTIFICATION,notification.build());
    }
}
