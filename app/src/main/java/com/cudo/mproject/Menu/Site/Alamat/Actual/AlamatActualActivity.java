package com.cudo.mproject.Menu.Site.Alamat.Actual;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Site.SiteProfileInterface;
import com.cudo.mproject.Menu.TaskActivity.TaskActivity;
import com.cudo.mproject.Model.DataAlamatActualOffline;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;
import com.cudo.mproject.Service.GpsService;
import com.cudo.mproject.Utils.SessionManagerGPS;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
//http://www.viralandroid.com/2015/12/how-to-get-current-gps-location-programmatically-in-android.html
//public class AlamatActualActivity extends BaseActivity implements AlamatActualInterface.View {

public class AlamatActualActivity extends BaseActivity implements LocationListener, AlamatActualInterface.View, View.OnClickListener {
    public static AlamatActualActivity alamatActualActivity;
    String TAG = getClass().getSimpleName();
//    BaseActivity baseActivity;
    /*data actual*/
    LocationManager locationManager;
    String mprovider;
    @BindView(R.id.site_name_actual)
    EditText site_name_actual;
    @BindView(R.id.alamat_actual)
    EditText alamat_actual;
    @BindView(R.id.long_actual)
    EditText long_actual;
    @BindView(R.id.lat_actual)
    EditText lat_actual;
    @BindView(R.id.provinsi_actual)
    EditText provinsi_actual;
    @BindView(R.id.kabupaten_actual)
    EditText kabupaten_actual;
    @BindView(R.id.kecamatan_actual)
    EditText kecamatan_actual;
    //    @BindView(R.id.btnLocation)
//    Button btnLocation;
    DataAlamatActualOffline dataAlamatActualOffline;
    Realm realm;
    Project dataProject;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    AlamatActualPresenter alamatActualPresenter;
    String userName = null;
    String userId = null;
    String projectId = null;
    //
    @BindView(R.id.submit_alamatActual)
    Button btnSubmit_alamatActual;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alamat_actual);
        Intent intent = getIntent();
        ButterKnife.bind(this);
//
        String Id = intent.getStringExtra("projectId");
        String is_offline = intent.getStringExtra("is_offline");
        initExtra(Id, is_offline);
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

//      if (mprovider != null && !mprovider.equals("")) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(mprovider);
        locationManager.requestLocationUpdates(mprovider, 100, 10, this);

        if (location != null) {
            onLocationChanged(location);
        } else {
//            Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        thischeckInet();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void thischeckInet() {
        if (ConnectivityReceiverService.isConnected(AlamatActualActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit_alamatActual.setBackgroundColor(Color.parseColor("#B71C1C"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_alamat_actual);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit_alamatActual.setBackgroundColor(Color.parseColor("#80F44336"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_alamat_actual);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit_alamatActual.setBackgroundColor(Color.parseColor("#80F44336"));
            TextView linearAct = (TextView) findViewById(R.id.linear_alamat_actual);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
        }
    }

    //
    void initExtra(String Id, String is_offline) {
        try {
//
            realm = Realm.getDefaultInstance();
            User user = realm.where(User.class).findFirst();
            dataProject = realm.where(Project.class)
                    .equalTo(Project.PROJECT_ID, Id)
                    .findFirst();
            Log.d(TAG, "initExtra: dataProject:" + dataProject);
//
            setView(dataProject, is_offline);
            Log.d(TAG, "initExtra: " + dataProject.getProject_id());
            Log.d(TAG, "initExtra: " + is_offline);


//
            dataAlamatActualOffline  = realm.where(DataAlamatActualOffline.class)
                    .equalTo("project_id", Id)
                    .equalTo("userName", user.getUsername())
                    .findFirst();
            if(dataAlamatActualOffline!=null) {
                if (dataAlamatActualOffline.getStatus_alamat_actual_offline().contains("1")) {

                    btnSubmit_alamatActual.setEnabled(false);
                    btnSubmit_alamatActual.setVisibility(View.INVISIBLE);
                }
            }

        } catch (Exception e) {

        }
    }

    void setView(Project dataProject, String is_offline) {
        Log.d(TAG, "setView: " + dataProject.getProject_id());
//        checkUser();
        projectId = dataProject.getProject_id();
    }

    @OnClick(R.id.btnLocation)
    void btnLatLon() {
        Location location = new Location("");
        getGPS(location);

    }

    void getGPS(Location location) {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 100, 10, this);
//
            if (location != null) {
//                onLocationChanged(location);
                lat_actual.setText(String.valueOf(location.getLatitude()));
                long_actual.setText(String.valueOf(location.getLongitude()));
            } else {
//                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @OnClick(R.id.back_alamat_actual)
//    void setBack() {
//        onBackPressed();
//    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }


    @Override
    public void onLocationChanged(Location location) {

        lat_actual.setText(String.valueOf(location.getLatitude()));
        long_actual.setText(String.valueOf(location.getLongitude()));

//        longitude.setText("Current Longitude:" + location.getLongitude());
//        latitude.setText("Current Latitude:" + location.getLatitude());
    }


    @OnClick(R.id.submit_alamatActual)
    void setSubmitAlamatActual() {
        if (ConnectivityReceiverService.isConnected(AlamatActualActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    doSubmitAlamatActual("1");
                } else {
                    showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, data Akan disimpan di local storage..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                                offlineSubmit();
                            doSubmitAlamatActual("2");
                            dialog.dismiss();
                        }
                    }, true, "");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, data Akan disimpan di local storage..", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                        offlineSubmit();
                    doSubmitAlamatActual("2");
                    dialog.dismiss();
                }
            }, true, "");
        }
    }

    void doSubmitAlamatActual(String isOnline) {
//        user = realm.where(User.class).findFirst();

        if (site_name_actual.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            site_name_actual.setError("Field Site Name tidak boleh null!");
            return;

        }
        if (alamat_actual.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a alamat ", Toast.LENGTH_SHORT).show();
            alamat_actual.setError("Field Alamat tidak boleh null!");
            return;

        }

        if (provinsi_actual.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a provinsi ", Toast.LENGTH_SHORT).show();
            provinsi_actual.setError("Field Provinsi tidak boleh null!");
            return;

        }
        if (kabupaten_actual.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a kabupaten ", Toast.LENGTH_SHORT).show();
            kabupaten_actual.setError("Field Kabupaten tidak boleh null!");
            return;

        }
        if (kecamatan_actual.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a kabupaten ", Toast.LENGTH_SHORT).show();
            kabupaten_actual.setError("Field Kecamatan tidak boleh null!");
            return;

        }

        if (long_actual.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a longitude ", Toast.LENGTH_SHORT).show();
            long_actual.setError("Field Longitude tidak boleh null!");
            return;

        }
        if (lat_actual.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a latitude ", Toast.LENGTH_SHORT).show();
            lat_actual.setError("Field Latitude tidak boleh null!");
            return;
        }
        alamatActualPresenter = new AlamatActualPresenter(this, this, projectId);
        alamatActualPresenter.submitDataActual(projectId, site_name_actual.getText().toString(), alamat_actual.getText().toString(), provinsi_actual.getText().toString(), kabupaten_actual.getText().toString(), kecamatan_actual.getText().toString(), long_actual.getText().toString(), lat_actual.getText().toString(),isOnline);
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onSuccessSubmit(boolean isSucces, String msg) {
        if (!isSucces) {
            showAlertDialog("ATTENTION", msg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, true);
//            btnSubmit_alamatActual.setEnabled(false);
//            btnSubmit_alamatActual.setBackgroundColor(Color.parseColor("#80F44336"));
        } else {
            showAlertDialog("Submit Data SUKSES", "", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setResult(RESULT_OK);
                    //finish();
                    dialog.dismiss();
                    btnSubmit_alamatActual.setBackgroundColor(Color.parseColor("#80F44336"));
                    btnSubmit_alamatActual.setEnabled(false);
                }
            }, true);
        }
    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }


    @Override
    public void onClick(View v) {

    }
}
