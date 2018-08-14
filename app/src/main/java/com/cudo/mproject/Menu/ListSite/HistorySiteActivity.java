package com.cudo.mproject.Menu.ListSite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Menu.History.AdapterHistoryActivityRealm;
import com.cudo.mproject.Menu.History.HistoryActivity;
import com.cudo.mproject.Menu.History.HistoryActivityInterface;
import com.cudo.mproject.Menu.History.HistoryPresenter;
import com.cudo.mproject.Menu.Project.ProjectListActivity;
import com.cudo.mproject.Menu.Project.ProjectListPresenter;
import com.cudo.mproject.Model.DataAlamatActualOffline;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.GpsService;
import com.cudo.mproject.Utils.AnimUtils;
import com.cudo.mproject.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistorySiteActivity extends BaseActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener, HistorySiteActivityInterface.View {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.recview_history_site)
    RecyclerView recview_history_site;

    @BindView(R.id.swipeRefresh_history_site)
    SwipeRefreshLayout swipeRefresh_history_site;

    @BindView(R.id.indikator)
    ImageView indikator_history_site;

    @BindView(R.id.toolbar_title)
//    TextView searchView;
    SearchView searchView;
    AdapterHistorySiteActivityRealm adapterHistorySiteActivity;
    HistorySitePresenter historySitePresenter;
    Context context;
    //    BaseActivity mContext;
    MenuItem menuLogoutHistory;
    NavigationView navigationView;
    static boolean isOnSiteHistory = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history_site);
        setContentView(R.layout.activity_main_history_site);
        ButterKnife.bind(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view_history_site);
        View headerView = navigationView.getHeaderView(0);
        // assigning the listener to the NavigationView
        navigationView.setNavigationItemSelectedListener(this);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.header);//header
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

//        setHasOptionsMenu(true);
//        if (OfflineDataTransaction.cekDataOffline(getRealm()) > 0) {
//            isOn = false;
//        } else {
//            isOn = true;
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userId = user.getUser_id();
        String realName = user.getReal_name();
        String userGroup = user.getUser_group();
        String area = user.getArea();
////
        if(user.getPhoto()!=null){
            ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_img);
        }

        TextView txtRealName = (TextView) headerView.findViewById(R.id.username_nav);
        txtRealName.setText(realName);
        TextView txtArea = (TextView) headerView.findViewById(R.id.user_area);
        txtArea.setText(area);
        TextView txtUserGroup = (TextView) headerView.findViewById(R.id.user_group);
        txtUserGroup.setText(userGroup);

        if (cekDataOffline() == false) {
            isOnSiteHistory = false;
        } else {
            isOnSiteHistory = true;
        }
        setupView();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
//        if (OfflineDataTransaction.cekDataOffline(getRealm()) > 0) {
//            isOnHistory = false;
//        } else {
//            isOnHistory = true;
//        }
        if (cekDataOffline() == false) {
            isOnSiteHistory = false;
        } else {
            isOnSiteHistory = true;
        }
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.nav_lis_project:
                Intent intentProject = new Intent(HistorySiteActivity.this, ProjectListActivity.class);
                startActivity(intentProject);
                break;
            case R.id.nav_history:
//                Toast.makeText(getApplicationContext(),"nav_camera telah dipilih",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HistorySiteActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_list_profile:
//                Toast.makeText(getApplicationContext(),"nav_gallery telah dipilih",Toast.LENGTH_SHORT).show();
                Intent intentSite = new Intent(HistorySiteActivity.this, HistorySiteActivity.class);
                startActivity(intentSite);
                break;
            case R.id.nav_cek_version:
//                Toast.makeText(getApplicationContext(),"nav_slideshow telah dipilih",Toast.LENGTH_SHORT).show();
                String versionApk = BuildConfig.VERSION_NAME;
                cekAPK(versionApk);
                break;

            case R.id.nav_logout:
                if (menuItem.getItemId() == R.id.nav_logout) {
                    if (isOnSiteHistory == false) {
                        Utils.showToast(this, "Anda Masih Memiliki Project Progress dengan Status PENDING");
                        return false;
                    } else {
                        showAlertDialog("Warning..", "Anda Akan Keluar dari Aplikasi?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doLogout();
//                    User.doLogout(ProjectListActivity.this, getRealm());
                            }
                        }, false, "");
                    }
                }
                break;
        }

        return true;
    }

    BroadcastReceiver gps_reciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            GpsCache.updateGPSCache(getRealm(),
                    intent.getDoubleExtra(GpsService.LAT, 0.0),
                    intent.getDoubleExtra(GpsService.LON, 0.0),
                    intent.getFloatExtra(GpsService.ACC, 0),
                    intent.getLongExtra(GpsService.TIME, 0)
            );
            thischeckGPS();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        checkUser();
        thischeckGPS();
        startGPSservice();
//        thischeckDataOffline();
        registerReceiver(gps_reciever, new IntentFilter(GpsService.GPSSEND));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopGPSservice();
        try {
            unregisterReceiver(gps_reciever);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void thischeckGPS() {
        AnimUtils.animateRotate(indikator_history_site);
        gpsCache = getRealm().where(GpsCache.class).findFirst();
        if (gpsCache != null && !gpsCache.isExpired()) {
            indikator_history_site.setBackground(getStatusBackground(gpsCache.getAcc()));
            Log.d(TAG, "checkGPS: asdadssad");
        } else {
            indikator_history_site.setBackground(getStatusBackground(0));
            Log.d(TAG, "checkGPS: null");
        }
    }
    Drawable getStatusBackground(float value) {
        if (value > 600) {
            return getResources().getDrawable(R.drawable.circle_btn_red);
        }
        if (value > GpsCache.ACC && value < 600) {
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
        historySitePresenter = new HistorySitePresenter(this, this, "", "");
//        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextListener(this);
        adapterHistorySiteActivity = new AdapterHistorySiteActivityRealm(getRealm()
                .where(DataAlamatActualOffline.class)
                .findAll());
        recview_history_site.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recview_history_site.setAdapter(adapterHistorySiteActivity);
//
        swipeRefresh_history_site.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doGetTask();
            }
        });
    }

    private void doGetTask() {
        baseshowProgress(true);
        historySitePresenter.getTask();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_site_profile, menu);
//        menuLogoutHistory = menu.findItem(R.id.logout_site_prof);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.project_list_site_prof) {
//            Intent intent = new Intent(HistorySiteActivity.this, ProjectListActivity.class);
//            startActivity(intent);
//        }
//        if (item.getItemId() == R.id.history_site_prof) {
//            Intent intent = new Intent(HistorySiteActivity.this, HistoryActivity.class);
//            startActivity(intent);
//        }
//        if (item.getItemId() == R.id.version_apk_site_prof) {
//            String versionApk =   BuildConfig.VERSION_NAME;
//            cekAPK(versionApk);
//        }
////        OfflineDataTransaction.cekDataOffline(getRealm());
////        if (OfflineDataTransaction.cekDataOffline(getRealm())  > 0) {
//
//        if (item.getItemId() == R.id.logout_site_prof) {
//            if (isOnSiteHistory == false) {
//                Utils.showToast(this, "Anda Masih Memiliki Project Progress dengan Status PENDING");
//                return false;
//            } else {
//                showAlertDialog("Warning..", "Anda Akan Keluar dari Aplikasi?", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        doLogout();
////                    User.doLogout(ProjectListActivity.this, getRealm());
//                    }
//                }, false, "");
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            doGetTask();
        }
    }
    public void cekAPK(String versionApk){
        Toast.makeText(getApplicationContext(),"versionApk"+versionApk, Toast.LENGTH_SHORT).show();
        historySitePresenter.cekApk(versionApk);
    }
    public void doLogout() {

        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userPass = user.getPassword();

//         Toast.makeText(getApplicationContext(), "isi userName:= "+userName+": isiPassword := "+userPass+":", Toast.LENGTH_SHORT).show();
        historySitePresenter = new HistorySitePresenter(this, this, userName, userPass);
        historySitePresenter.logout(userName, userPass);


//        User.doLogout(ProjectListActivity.this, getRealm());
//        User user = realm.where(User.class).findFirst();
//        String userName = user.getUsername();
//        String userPass = user.getPassword();
////        Toast.makeText(getApplicationContext(), "isi userName:= "+userName+": isiPassword := "+userPass+":", Toast.LENGTH_SHORT).show();
//        historySitePresenter = new ProjectListPresenter(this, this, userName, userPass);
//        historySitePresenter.logout(userName, userPass);
////        User.doLogout(ProjectListActivity.this, getRealm());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onUserExpired() {

    }

    @Override
    public void onFinish(boolean isSucces, String msg) {
        if (swipeRefresh_history_site.isRefreshing()) {
            swipeRefresh_history_site.setRefreshing(false);
        }
            if (!isSucces)
                Utils.showToast(this, msg);
    }

    @Override
    public void onError(boolean status, String msg) {

    }

    @Override
    public void onSuccess(String msg) {

    }
}
