package com.cudo.mproject.Menu.Tab;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Menu.History.HistoryActivity;
import com.cudo.mproject.Menu.ListSite.HistorySiteActivity;
import com.cudo.mproject.Menu.Project.ProjectListActivity;
import com.cudo.mproject.Menu.Site.Alamat.Actual.AlamatActualActivity;
import com.cudo.mproject.Menu.Site.IMB.IMBActivity;
import com.cudo.mproject.Menu.Site.PLN.PLNActivity;
import com.cudo.mproject.Menu.Site.Person.PersonActivity;
import com.cudo.mproject.Menu.Site.SiteProfileActivity;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.GpsService;
import com.cudo.mproject.Utils.AnimUtils;
import com.cudo.mproject.Utils.Utils;

import butterknife.BindView;
import io.realm.Realm;

/*
https://www.androidhive.info/2011/08/android-tab-layout-tutorial/
 */
public class AndroidTabLayoutActivity extends BaseActivity implements TabInterface.View, NavigationView.OnNavigationItemSelectedListener {

    String TAG = getClass().getSimpleName();

    @BindView(R.id.indikator)
    ImageView indikator;
    GpsCache gpsCache;
    NavigationView navigationView;
    static boolean isOnHistory = true;
    private Toolbar toolbar;
    private TabActivity tabActivity;
    LocalActivityManager mLocalActivityManager;
    TabPresenter tabPresenter;
//    private TabHost tabHost;
    /*
    https://stackoverflow.com/questions/35335265/inflate-the-actionbar-from-menu-in-tabhost-activity
    https://stackoverflow.com/questions/22059519/android-app-keeps-crashing-with-tabhost
    https://gist.github.com/jpt1122/f724ceeb23c1c76b58aef54ea94b8887
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tab);
        setContentView(R.layout.activity_main_tab_layout);
//      toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view_tab_layout);
        // assigning the listener to the NavigationView
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

//      android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.header);//header
        toolbar = (Toolbar) findViewById(R.id.header);//header
//      setSupportActionBar(toolbar);
//      getSupportActionBar().setElevation(0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
//

        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userId = user.getUser_id();
        String realName = user.getReal_name();
        String userGroup = user.getUser_group();
        String area = user.getArea();
////
        if (user.getPhoto() != null) {
            ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_img);
        }

        TextView txtRealName = (TextView) headerView.findViewById(R.id.username_nav);
        txtRealName.setText(realName);
        TextView txtArea = (TextView) headerView.findViewById(R.id.user_area);
        txtArea.setText(area);
        TextView txtUserGroup = (TextView) headerView.findViewById(R.id.user_group);
        txtUserGroup.setText(userGroup);
//
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
//        tabHost = tabActivity.getTabHost();


        mLocalActivityManager = new LocalActivityManager(this, false);
        tabHost.setup(mLocalActivityManager);
//        mLocalActivityManager.dispatchCreate(null); // state will be bundle your activity state which you get in onCreate
        mLocalActivityManager.dispatchCreate(savedInstanceState);


//
        Intent intent = getIntent();
        String Id = intent.getStringExtra("projectId");
        String is_offline = "0";
//      String is_offline = intent.getStringExtra("is_offline");
//
        //tab 1 icon Home
        TabHost.TabSpec tabNominal = tabHost.newTabSpec("Nominal");
        tabNominal.setIndicator("", getResources().getDrawable(R.drawable.icon_ceklist_tab));
        Intent nominalIntent = new Intent(this, SiteProfileActivity.class);
        nominalIntent.putExtra("projectId", Id);
        nominalIntent.putExtra("is_offline", is_offline);
        tabNominal.setContent(nominalIntent);

        // Tab 2 icon koordinat /alamat
        TabHost.TabSpec tabAlamatActual = tabHost.newTabSpec("Alamat Actual");
        tabAlamatActual.setIndicator("", getResources().getDrawable(R.drawable.icon_location_tab));
        Intent koordinatIntent = new Intent(this, AlamatActualActivity.class);
        koordinatIntent.putExtra("projectId", Id);
        koordinatIntent.putExtra("is_offline", is_offline);
        tabAlamatActual.setContent(koordinatIntent);

        // Tab 3 icon police, Data Pemilik Lahan
        TabHost.TabSpec tabDataPenjagaLahan = tabHost.newTabSpec("Data Penjaga Lahan");
        tabDataPenjagaLahan.setIndicator("", getResources().getDrawable(R.drawable.icon_person_tab));
        Intent lahanIntent = new Intent(this, PersonActivity.class);
        lahanIntent.putExtra("projectId", Id);
        lahanIntent.putExtra("is_offline", is_offline);
        tabDataPenjagaLahan.setContent(lahanIntent);

        // Tab 4 IMB
        TabHost.TabSpec tabIMB = tabHost.newTabSpec("IMB");
        tabIMB.setIndicator("", getResources().getDrawable(R.drawable.icon_event_tab));
        Intent imbIntent = new Intent(this, IMBActivity.class);
        imbIntent.putExtra("projectId", Id);
        imbIntent.putExtra("is_offline", is_offline);
        tabIMB.setContent(imbIntent);

        // Tab 5 PLN
        TabHost.TabSpec tabPLN = tabHost.newTabSpec("PLN");
        tabPLN.setIndicator("", getResources().getDrawable(R.drawable.icon_videos_tab));
        Intent plnIntent = new Intent(this, PLNActivity.class);
        plnIntent.putExtra("projectId", Id);
        plnIntent.putExtra("is_offline", is_offline);
        tabPLN.setContent(plnIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(tabNominal);
        tabHost.addTab(tabAlamatActual); // Adding koordinat tab
        tabHost.addTab(tabDataPenjagaLahan); // Adding lahan tab
        tabHost.addTab(tabIMB); // Adding IMB tab
        tabHost.addTab(tabPLN); // Adding PLN tab

        setupView();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(isFinishing()); // you have to
        // manually dispatch
        // the pause msg
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume(); // you have to manually dispatch
        // the resume msg
    }

    BroadcastReceiver gps_reciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            realm = Realm.getDefaultInstance();
            GpsCache.updateGPSCache(realm,
                    intent.getDoubleExtra(GpsService.LAT, 0.0),
                    intent.getDoubleExtra(GpsService.LON, 0.0),
                    intent.getFloatExtra(GpsService.ACC, 0),
                    intent.getLongExtra(GpsService.TIME, 0)
            );
            thischeckGPS();
        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkUser();
//        mLocalActivityManager.dispatchResume();
//        thischeckGPS();
//        startGPSservice();
//        registerReceiver(gps_reciever, new IntentFilter(GpsService.GPSSEND));
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        stopGPSservice();
//        try {
//            unregisterReceiver(gps_reciever);
//        } catch (Exception e ) {
//            e.printStackTrace();
//        }
//    }

    void thischeckGPS() {
        AnimUtils.animateRotate(indikator);
        realm = Realm.getDefaultInstance();
        gpsCache = realm.where(GpsCache.class).findFirst();
        if (gpsCache != null && !gpsCache.isExpired()) {
            indikator.setBackground(getStatusBackground(gpsCache.getAcc()));
            Log.d(TAG, "checkGPS: asdadssad");
        } else {

            indikator.setBackground(getStatusBackground(0));
            Log.d(TAG, "checkGPS: null");
        }
    }

    Drawable getStatusBackground(float value) {
        if (value > 100) {
            return getResources().getDrawable(R.drawable.circle_btn_red);
        }
        if (value > GpsCache.ACC && value < 100) {
            return getResources().getDrawable(R.drawable.circle_btn_yellow);
        } else if (value == 0.0) {
            return getResources().getDrawable(R.drawable.circle_btn_grey);
        } else {
            return getResources().getDrawable(R.drawable.circle_btn_green);
        }

    }

    private void setupView() {
        Utils.setToolbar(this, "");
        gpsCache = getRealm().where(GpsCache.class).findFirst();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.nav_lis_project:
                Intent intentProject = new Intent(this, ProjectListActivity.class);
                startActivity(intentProject);
                break;
            case R.id.nav_history:
//                Toast.makeText(getApplicationContext(),"nav_camera telah dipilih",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_list_profile:
//                Toast.makeText(getApplicationContext(),"nav_gallery telah dipilih",Toast.LENGTH_SHORT).show();
                Intent intentSite = new Intent(this, HistorySiteActivity.class);
                startActivity(intentSite);
                break;
            case R.id.nav_cek_version:
//                Toast.makeText(getApplicationContext(),"nav_slideshow telah dipilih",Toast.LENGTH_SHORT).show();
                String versionApk = BuildConfig.VERSION_NAME;
                cekAPK(versionApk);
                break;
            case R.id.nav_logout:
                if (menuItem.getItemId() == R.id.nav_logout) {
                    if (isOnHistory == false) {
                        Utils.showToast(this, "Anda Masih Memiliki Project Progress dengan Status PENDING");
                        return false;
                    } else {
                        showAlertDialog("Warning..", "Anda Akan Keluar dari Aplikasi?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doLogout();
                            }
                        }, false, "");
                    }
                }
                break;
        }
        return true;
    }

    public void cekAPK(String versionApk) {
        Toast.makeText(getApplicationContext(), "versionApk" + versionApk, Toast.LENGTH_SHORT).show();
//      presenter.cekApk(versionApk);
        tabPresenter.cekApk(versionApk);
    }

    public void doLogout() {
        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userPass = user.getPassword();
        tabPresenter = new TabPresenter(this, this, userName, userPass);
        tabPresenter.logout(userName, userPass);
    }

    @Override
    public void onUserExpired() {

    }

    @Override
    public void onFinish(boolean isSucces, String msg) {

    }

    @Override
    public void onError(boolean status, String msg) {

    }

    @Override
    public void onSuccess(String msg) {

    }
}