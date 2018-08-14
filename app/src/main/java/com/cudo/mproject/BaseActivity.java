package com.cudo.mproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.API.URL;
import com.cudo.mproject.Menu.History.HistoryActivity;
import com.cudo.mproject.Menu.ListSite.HistorySiteActivity;
import com.cudo.mproject.Menu.Project.ProjectListActivity;
import com.cudo.mproject.Model.DataAlamatActualOffline;
import com.cudo.mproject.Model.DataIMB;
import com.cudo.mproject.Model.DataPLNSite;
import com.cudo.mproject.Model.DataPenjagaLahan;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Dialog.ProgressDialogGanz;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.Service.ConnectivityReceiverService;
import com.cudo.mproject.Service.GpsService;
import com.cudo.mproject.Utils.GetAllPermissionV2;
import com.cudo.mproject.Utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.Realm;

import static com.cudo.mproject.API.URL.URL_CHEK_SERVER;
import static com.cudo.mproject.BuildConfig.END_POINT;

/**
 * date             editing by        method                            Description
 * 02/02/2018     newbiecihuy     void submitDataOff()                  Submit data Offfline to server when connect intternet
 * 02/02/2018     newbiecihuy     logic submit data offline,            make sure submit data offline when connect internet (progress)
 * 03/03/2018     newbiecihuy     cekDataOffline                        cek data offline status not uploaded,
 * <p>
 * 03/03/2018     newbiecihuy     User.islogout                         Destroy data User, Project, WorkPkg, MActivity
 *  param username
 * <p>
 * 25/04/2018     newbiecihuy     isConnectedToServer                   cek status webapp server(disabled).
 * <p>
 * 26/04/2018     newbiecihuy     Auto enable LocationListener        - https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient.OnConnectionFailedListener
 *                                                                    - http://www.digitstory.com/enable-gps-automatically-android/
 * 18/07/2018     newbiecihuy   call method fakeGPS()                - destory user when fake gps detect
 *
*/
public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {//,View.OnClickListener

    String TAG = getClass().getSimpleName();
    public static String MESSAGE_PROGRESS;
    public static boolean IS_FINISH_INTENT = false;
    private ProgressDialogGanz progressDialog;
    //
    public static Realm realm;
    public User user;
    public static User userGroup;
    public GpsCache gpsCache;
    public ConnectivityReceiverService crs;
    static boolean isOnHistory = true;
//    static boolean isi_OnHistory = true;
    public static boolean connectivity;
    //    Context context;
//    Google geoLocation
//    private TextView latitudeTextView,longitudeTextView;

    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    //
    private URL url;
    private HttpURLConnection urlConnection = null;
//    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_one);
//        isConnectedToServer();

        init();
        setUpGClient();
//
//      fakeGPS();
    }
//    @Override
//    public void onClick(View v) {
//
//    }

    private void init() {
        realm = Realm.getDefaultInstance();
        intentGps = new Intent(this,
                GpsService.class);
        progressDialog = new ProgressDialogGanz(this);
//
        cekDataOffline();

//
        intentInternet = new Intent(this, ConnectivityReceiverService.class);
//      logic to submit data offline when connect to internet
        if (ConnectivityReceiverService.isConnected(BaseActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 10 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
//                    submitDataOff();
//                    isConnectedToServer();
                } else {
//                    Utils.showToast(this, "OFFLINE MODE");
//                    submitDataOff();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Utils.showToast(this, "OFFLINE MODE");
        }
    }

    public boolean isConnectedToServer() {
//        try {
//            java.net.URL myUrl = new java.net.URL(URL.URL_CHEK_SERVER);//192.168.3.1
//            URLConnection connection = myUrl.openConnection();
//            connection.setConnectTimeout(5);
//            connection.connect();
//            return true;
//        } catch (Exception e) {
////      Toast.makeText(getApplicationContext(), "500 Internal Server Error", Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("500");
//            builder.setMessage("500 Internal Server Error")
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .setNegativeButton("CANCELL", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            //  Action for 'NO' Button
//                            dialog.cancel();
//                        }
//                    });
//            //Creating dialog box
//            AlertDialog alert = builder.create();
//            alert.setIcon(R.drawable.ic_warning_black_24dp);
//            alert.show();
//            return false;
//        }
        return true;
    }

    //
    public static boolean cekDataOffline() {
        realm = Realm.getDefaultInstance();
        if (OfflineDataTransaction.cekDataOffline(realm) > 0) {
            isOnHistory = false;
        } else {
            isOnHistory = true;
        }
        if(DataAlamatActualOffline.cekDataOffline(realm) > 0){
            isOnHistory = false;
        } else {
            isOnHistory = true;
        }
        if(DataPenjagaLahan.cekDataOffline(realm) > 0){
            isOnHistory = false;
        } else {
            isOnHistory = true;
        }
        if(DataPLNSite.cekDataOffline(realm) > 0){
            isOnHistory = false;
        } else {
            isOnHistory = true;
        }
        if(DataIMB.cekDataOffline(realm) > 0){
            isOnHistory = false;
        } else {
            isOnHistory = true;
        }

        return isOnHistory;
    }

    /*
       method submit dataOffline;
       create 12/12/2017
     */
    void submitDataOff() {
        OfflineDataTransaction cekData = realm.where(OfflineDataTransaction.class)
                .equalTo("status_project_offline", "0") // select data when status equals to 0 (pending)
                .findFirst();
        if (cekData != null) {
//
            List<OfflineDataTransaction> list = realm.where(OfflineDataTransaction.class)
                    .equalTo("status_project_offline", "0")
                    .findAll();
            int count = realm.copyFromRealm(realm.where(OfflineDataTransaction.class)
                    .equalTo("status_project_offline", "0")
                    .findAll()).size();
        } else {

        }
    }

    public Realm getRealm() {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        return realm;
    }

    public Realm closeRelam() {
        if (!realm.isClosed())
            realm.close();
        return realm;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    public void checkUser() {
        user = realm.where(User.class).findFirst();
        if (user == null) {
            Utils.showToast(this, "User Not Valid .. Please Relogin ..");
            User.doLogout(this, realm);
        }
    }

    public static String chekUserGroup(Realm realm) {
        String user_group = "";
        userGroup = realm.where(User.class).findFirst();
        if (userGroup != null) {
            user_group = userGroup.getUser_group();
        }
        return user_group;
    }

    public void checkGPS() {
        user = realm.where(User.class).findFirst();
        gpsCache = realm.where(GpsCache.class).findFirst();
        if (gpsCache == null) {
            Utils.showToast(this, "Something wrong.. Please Relogin ..");
//          User.doLogout(this, realm);

            User.islogout(this, realm, user.getUsername());
        }

    }
//    public void fakeGPS() {
//        if(GpsService.isInvalid()==true){
//            User.islogout(this, realm, user.getUsername());
//        }
//    }

    public boolean checkPermission() {
        GetAllPermissionV2 getAllPermissionV2 = new GetAllPermissionV2(this);
        return getAllPermissionV2.checkPermission();
    }

    public void showAlertDialog(String title, String message, DialogInterface.OnClickListener positive, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title.equals("") ? getString(R.string.warning) : title).setMessage(message);

        builder.setPositiveButton(android.R.string.ok, positive != null ? positive : new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setIcon(R.drawable.ic_warning_black_24dp);
        AlertDialog alert11 = builder.create();
        // alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        alert11.setCancelable(cancelable);
        alert11.show();
    }

    public void showAlertDialog(String title, String message, DialogInterface.OnClickListener positive, boolean cancelable, String x) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title.equals("") ? getString(R.string.warning) : title)
                .setMessage(message);
        if (positive != null)
            builder.setPositiveButton(android.R.string.ok, positive);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        AlertDialog alert11 = builder.create();
        // alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        alert11.setCancelable(cancelable);
        alert11.show();
    }

    public void baseshowProgress(boolean isShow) {
        if (isShow)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }

    public void setToolbar() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    Intent intentInternet;
    Intent intentGps;
    boolean gpsBound = false;
    //region Description

    public void startGPSservice() {
        try {
            Log.d(TAG, "startGPSservice: ");
            startService(intentGps);
            bindService(intentGps, mConnection, Context.BIND_AUTO_CREATE);
            gpsBound = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopGPSservice() {
        try {
            if (gpsBound) {
                unbindService(mConnection);
                gpsBound = false;
            }
            Intent intent = new Intent(this,
                    GpsService.class);
            stopService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Service gpsService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            gpsService = ((GpsService.LocalBinder) service).getService();
            gpsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "onServiceConnected: ");
            mConnection = null;
        }
    };

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == GetAllPermissionV2.PERMISSION_ALL) {
//            for (int i = 0; i < permissions.length; i++) {
//                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    checkPermission();
//                } else
//                    checkPermission();
//            }
//        }
//    }


//     method implements GoogleApiClient.ConnectionCallbacks,
//     GoogleApiClient.OnConnectionFailedListener, LocationListener


    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            Double latitude = mylocation.getLatitude();
            Double longitude = mylocation.getLongitude();

            //Or Do whatever you want with your location
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(BaseActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(BaseActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//            getMyLocation();
//        }
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GetAllPermissionV2.PERMISSION_ALL) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    checkPermission();
                }
            }
        }
    }

}


