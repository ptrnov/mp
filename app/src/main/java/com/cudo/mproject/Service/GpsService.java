package com.cudo.mproject.Service;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Dialog.AlertGPSDialog;
import com.cudo.mproject.Dialog.ShowPhotoDialog;
import com.cudo.mproject.Menu.Login.LoginActivity;
import com.cudo.mproject.Menu.Project.ProjectListActivity;
import com.cudo.mproject.Menu.Tab.AndroidTabLayoutActivity;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.ActivityUtils;
import com.cudo.mproject.Utils.Utils;

import java.util.List;

import static com.cudo.mproject.BaseApp.getContext;


/**
 * Created by adsxg on 12/11/2017.
 * date             editing by          function                           description
 * 12/12/2017      newbiecihuy   void onProviderDisabled     make sure login if gps not actived
 */

public class GpsService extends Service {
    private static final String TAG = "GPS Search";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 500;
    private static final float LOCATION_DISTANCE = 10f;
    public float acc = 0;
    public int process = 0;

    public Location mLastLocation;
    public static String GPSSEND = "GPSSEND";
    private final IBinder mBinder = new LocalBinder();
    private static final int TWO_MINUTES = 1000;
    public static String ACC = "GPS";
    public static String LAT = "LAT";
    public static String LON = "LON";
    public static String TIME = "TIME";
    Intent intent;
    private BaseActivity baseActivity;
    private Context mpContext = null;
    private AlertGPSDialog alertGPSDialog;
    static boolean invalidGPS=true;
    public static boolean isInvalid(){

        return invalidGPS;
    }

    //    public GpsService(Context context){
//        this.mpContext=context;
//    }
    public class LocationListener implements android.location.LocationListener {


        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

//            gpContext = getApplicationContext();
        }

//        public boolean isMockSettingsON(Context context) {
//            // returns true if mock location enabled, false if not enabled.
//            if (Settings.Secure.getString(context.getContentResolver(),
//                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
//                return false;
//            else
//                return true;
//        }
//
//        public boolean areThereMockPermissionApps(Context context) {
//            int count = 0;
//
//            PackageManager pm = context.getPackageManager();
//            List<ApplicationInfo> packages =
//                    pm.getInstalledApplications(PackageManager.GET_META_DATA);
//
//            for (ApplicationInfo applicationInfo : packages) {
//                try {
//                    PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
//                            PackageManager.GET_PERMISSIONS);
//
//                    // Get Permissions
//                    String[] requestedPermissions = packageInfo.requestedPermissions;
//
//                    if (requestedPermissions != null) {
//                        for (int i = 0; i < requestedPermissions.length; i++) {
//                            if (requestedPermissions[i]
//                                    .equals("android.permission.ACCESS_MOCK_LOCATION")
//                                    && !applicationInfo.packageName.equals(context.getPackageName())) {
//                                count++;
//                            }
//                        }
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//                    Log.e("Got exception ", e.getMessage());
//                }
//            }
//
//            if (count > 0)
//                return true;
//            return false;
//        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location.getAccuracy());

            if (isBetterLocation(location, mLastLocation) || location.getAccuracy() < 100) {//1000 meters
                Time now = new Time();
                now.setToNow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2||Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT||
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP||Build.VERSION.SDK_INT >= Build.VERSION_CODES.M||
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N|| Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (location.isFromMockProvider()) {
                        showInvalid();
                    }
                } else if (Utils.isFakeGPS(getApplicationContext())) {
                    showInvalid();
                }
                intent.putExtra("GPS", location.getAccuracy());
                intent.putExtra("LAT", location.getLatitude());
                intent.putExtra("LON", location.getLongitude());
                intent.putExtra("TIME", location.getTime());
                sendBroadcast(intent);
                mLastLocation = location;
            }

        }

        void showInvalid() {
            Toast.makeText(getApplicationContext(), "WARNING..FRAUD GPS Terdeteksi!! " +
                    "mohon copot pemasangan aplikasi yang mempengaruhi GPS Service..", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(getContext(), LoginActivity.class);//
            getContext().startActivity(myIntent);
//            Utils.showToast(getApplicationContext(), "WARNING..FRAUD GPS Terdeteksi!! " +
//                    "mohon copot pemasangan aplikasi yang mempengaruhi GPS Service");
//            Log.e(TAG, "WARNING..FRAUD GPS Terdeteksi");
//            baseActivity.showAlertDialog("WARNING..FRAUD GPS Terdeteksi",
//                    "mohon copot pemasangan aplikasi yang mempengaruhi GPS Service..", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //dialog.dismiss();
//                   return;
//                }
//            }, true);
            intent.putExtra("GPS", 0);
            intent.putExtra("LAT", 0);
            intent.putExtra("LON", 0);
            intent.putExtra("TIME", 0);
            sendBroadcast(intent);
            isInvalid();
            return;
            //            Builder builder = new Builder(getContext());
//            builder.setTitle("WARNING");
//            builder.setMessage("FRAUD GPS Terdeteksi!! mohon copot pemasangan aplikasi yang mempengaruhi GPS Service..");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    // Show location settings when the user acknowledges the alert dialog
////                  Toast.makeText(getApplicationContext(), "GPS NOT Enabled", Toast.LENGTH_SHORT).show();
////                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                    startActivity(intent);
//                    return;
//                }
//            });
//            builder.setIcon(R.drawable.ic_warning_black_24dp);
//            Dialog alertDialog = builder.create();
//            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
////          alertDialog.setCancelable(false);
//            alertDialog.show();


        }

        @Override
        public void onProviderDisabled(String provider) {
//            mLastLocation.reset();
            showAlertGPS();
            getLastLocation();

//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//            if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER ) ) {
//               Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                 Toast.makeText( getApplicationContext(), "Gps Disabled Please Make Sure Your GPS is Enable", Toast.LENGTH_SHORT ).show();

            Log.e(TAG, "onProviderDisabled: " + provider);
        }


//        public void showAlertGPS() {
        // Build the alert dialog
//        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//           !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
//           !mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
//            // Build the alert dialog
//            Context gpContext;

//        mpContext = gpContext.getApplicationContext();
//            Utils.showToast(getApplicationContext(), "GPS NOT Enabled..");
//            Toast.makeText(getApplicationContext(), "GPS NOT Enabled", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//            Intent intent = new Intent(String.valueOf(LoginActivity.class));
//            startActivity(intent);
//
//            baseActivity.showAlertDialog("ATTENTION", "GPS NOT Enabled", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //dialog.dismiss();
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
//                }
//            }, true);
//          Builder builder = new Builder(getContext());
////        AlertDialog.Builder builder = new AlertDialog.Builder(activityObj.getApplicationContext());
////        Notification.Builder builder = new Notification.Builder(context).setContentTitle("custom title");
//            builder.setTitle("Location Services Not Active");
//            builder.setMessage("Please enable Location Services and GPS");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    // Show location settings when the user acknowledges the alert dialog
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
//                }
//            });
//            builder.setIcon(R.drawable.ic_warning_black_24dp);
//            Dialog alertDialog = builder.create();
//            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
////          alertDialog.setCancelable(false);
//            alertDialog.show();

//        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();

            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.NETWORK_PROVIDER),
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.PASSIVE_PROVIDER)

    };

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public GpsService getService() {
            return GpsService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        intent = new Intent(GPSSEND);
        initializeLocationManager();
        mpContext = this.getBaseContext();
//        baseActivity = new BaseActivity();
        getLocation();
    }

    /*
       Best way to get user GPS location in background in Android
       https://stackoverflow.com/questions/28535703/best-way-to-get-user-gps-location-in-background-in-android
     */
    public void getLocation() {
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        /*
        How can I get location without internet in android, using only GPS
        https://stackoverflow.com/questions/6013521/how-can-i-get-location-without-internet-in-android-using-only-gps
         */
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
/*
      Android location using Cell Tower
      https://stackoverflow.com/questions/23710045/android-location-using-cell-tower
 */
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
//
            int cellid = 0;
            int celllac = 0;
            if (cellLocation != null) {
                cellid = cellLocation.getCid();
                celllac = cellLocation.getLac();
                Log.d("CellLocation", cellLocation.toString());
                Log.d("GSM CELL ID", String.valueOf(cellid));
                Log.d("GSM Location Code", String.valueOf(celllac));
            }
//

        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /*
       getLastLlocation GPS
       http://en.proft.me/2017/04/17/how-get-location-latitude-longitude-gps-android/
     */
    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = mLocationManager.getBestProvider(criteria, false);
            Location location = mLocationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }

    public static boolean areThereMockPermissionApps(Context context) {
        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception ", e.getMessage());
            }
        }

        if (count > 0)
            return true;
        return false;
    }

    private void showAlertGPS() {
//        new AlertGPSDialog(baseActivity.getBaseContext()).show();
//        Toast.makeText(getApplicationContext(), "GPS NOT Enabled", Toast.LENGTH_SHORT).show();
//        ntent myIntent = new Intent(getContext(), AlertGPSDialog.class);//
//        Intent intent = new Intent(getContext(), ProjectListActivity.class);
//        startActivity(intent);
//        Intent myIntent = new Intent(getContext(), AlertGPSDialog.class);//
//        getContext().startActivity(myIntent);

    }
//    private void showAlertGPS() {
//    // Build the alert dialog
////        mpContext = context;
////            Utils.showToast(getApplicationContext(), "GPS NOT Enabled..");
////
////
////
////        baseActivity.showAlertDialog("ATTENTION", "GPS NOT Enabled",
////                new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        //dialog.dismiss();
////                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                        startActivity(intent);
////                    }
////                }, true);
////        Context gpContext = getContext() ;
////        Context gpContext = this.getApplicationContext();
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        // set title
//        alertDialogBuilder.setTitle("Location Services Not Active");
//        // set dialog message
//        alertDialogBuilder
//                .setMessage("Please enable Location Services and GPS")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // if this button is clicked, close
//                        // current activity
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // if this button is clicked, just close
//                        // the dialog box and do nothing
//                        dialog.cancel();
//                    }
//                });
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
//        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        // show it
//        alertDialog.show();
//    }


}