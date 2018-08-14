package com.cudo.mproject.Service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.R;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import io.realm.Realm;

/**
 * Created by adsxg on 12/19/2017.
 */

public class ConnectivityReceiverService extends BroadcastReceiver {
    //public class ConnectivityReceiverService {
    private static final String TAG = ConnectivityReceiverService.class.getSimpleName();
    //
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiverService() {
        super();
    }
    private static String answer = null;
    public static boolean TYPE_WIFI = true;
    public static boolean TYPE_MOBILE = true;
    public static boolean TYPE_NOT_CONNECTED = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Explicitly specify that which service class will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                ServiceGlobalEzGG.class.getName());
        Log.d(TAG, "onReceive: " + isConnected(context));
        Intent i = new Intent();
        i.putExtra("data", isConnected(context));
        i.setAction(ServiceGlobalEzGG.ConnString);
        //startService(context, (intent.setComponent(comp)));
        context.sendBroadcast(i);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting()) {

            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    if ((networkInfo.getState() == NetworkInfo.State.CONNECTED ||
                            networkInfo.getState() == NetworkInfo.State.CONNECTING) &&
                            isInternet())
                        return true;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    if ((networkInfo.getState() == NetworkInfo.State.CONNECTED ||
                            networkInfo.getState() == NetworkInfo.State.CONNECTING) &&
                            isInternet())
                        return true;
                    break;
                default:
                    return false;
            }
            Log.d(TAG, "isConnected: 1 " + networkInfo.getState());
            Log.d(TAG, "isConnected: 2 " + networkInfo.getExtraInfo());
            Log.d(TAG, "isConnected: 3 " + networkInfo.getDetailedState());
            Log.d(TAG, "isConnected: 4 " + networkInfo.getReason());
            Log.d(TAG, "isConnected: 5 " + networkInfo.getSubtypeName());
            return true;

//            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

//            if (InetAddress.getByName("www.google.com").isReachable(10))
//            {
//                // host reachable
//            }
//            else
//            {
//                // host not reachable
//            }

//          static void submitData();
//            private static void doSubmitDataOfline();
        } else {
            Log.v(TAG, "Internet Connection Not Present");
            answer = "Internet Connection Not Present";
//            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return false;
        }
    }

    /*
     * https://stackoverflow.com/questions/4530846/how-to-programmatically-check-availibilty-of-internet-connection-in-android
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
     */
    private static boolean isInternet() {

        Runtime runtime = Runtime.getRuntime(); // this method does not work on some old devices (Galays S3, etc.)
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 -w 10 180.250.19.206");
            int exitValue = ipProcess.waitFor();
            boolean reachable = (exitValue == 0);
            Log.d(TAG, "isi reachable" + reachable);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
