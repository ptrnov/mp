package com.cudo.mproject.Menu.Project;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Menu.History.HistoryActivity;
import com.cudo.mproject.Menu.ListSite.HistorySiteActivity;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.GpsService;
import com.cudo.mproject.Utils.AnimUtils;
import com.cudo.mproject.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by adsxg on 12/11/2017.
 * date             editing by        method                                   description
 * <p>
 * 14/12/2017      newbiecihuy     onOptionsItemSelected        - load history activity
 */
public class ProjectListActivity extends BaseActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener,
        ProjectListInterface.View {

    String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    DrawerLayout drawer;

//    FragmentManager fragmentManager;
//    Fragment fragment = null;

    @BindView(R.id.recview)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.indikator)
    ImageView indikator;
    @BindView(R.id.toolbar_title)
//    TextView searchView;
            SearchView searchView;
    AdapterActivityRealm adapter;
    ProjectListPresenter presenter;
    Context context;
    //    @BindView(R.id.logout_project)
    MenuItem menuLogout;
    //    @BindView(R.id.logout_project);
    //    BaseActivity mContext;
//    LogoutProjectListPresenter logoutPresenter;
    static boolean isOn = true;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_project_list);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//
        realm = Realm.getDefaultInstance();
//
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        // assigning the listener to the NavigationView
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
//


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.header);//header
        setSupportActionBar(toolbar);
//        getSupportActionBar().setElevation(0);

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
        }else{
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
            isOn = false;
        } else {
            isOn = true;
        }
        setupView();
//

    }

    /*
    setTextColorForMenuItem
    https://hanihashemi.com/2017/05/06/change-text-color-of-menuitem-in-navigation-drawer/
     */
//    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
//        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
//        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
//        menuItem.setTitle(spanString);
//    }
//
//    private void resetAllMenuItemsTextColor(NavigationView navigationView) {
//        for (int i = 0; i < navigationView.getMenu().size(); i++)
//            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.md_grey_f3);
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);
//        resetAllMenuItemsTextColor(navigationView);
//        setTextColorForMenuItem(menuItem, R.color.md_grey_f3);
        switch (menuItem.getItemId()) {
            case R.id.nav_lis_project:
//                setTextColorForMenuItem(menuItem, R.color.md_white_1000);
                Intent intentProject = new Intent(ProjectListActivity.this, ProjectListActivity.class);
                startActivity(intentProject);
                break;
            case R.id.nav_history:
//                setTextColorForMenuItem(menuItem, R.color.md_white_1000);
//                Toast.makeText(getApplicationContext(),"nav_camera telah dipilih",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProjectListActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_list_profile:
//                setTextColorForMenuItem(menuItem, R.color.md_white_1000);
//                Toast.makeText(getApplicationContext(),"nav_gallery telah dipilih",Toast.LENGTH_SHORT).show();
                Intent intentSite = new Intent(ProjectListActivity.this, HistorySiteActivity.class);
                startActivity(intentSite);
                break;
            case R.id.nav_cek_version:
//                setTextColorForMenuItem(menuItem, R.color.md_white_1000);
//                Toast.makeText(getApplicationContext(),"nav_slideshow telah dipilih",Toast.LENGTH_SHORT).show();
                String versionApk = BuildConfig.VERSION_NAME;
                cekAPK(versionApk);
                break;
            case R.id.nav_logout:
                if (menuItem.getItemId() == R.id.nav_logout) {
                    if (isOn == false) {
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


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        if (cekDataOffline() == false) {
            isOn = false;
        } else {
            isOn = true;
        }
        super.onStart();
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
        thischeckDataOffline();
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
        AnimUtils.animateRotate(indikator);
        gpsCache = getRealm().where(GpsCache.class).findFirst();
        if (gpsCache != null && !gpsCache.isExpired()) {
            indikator.setBackground(getStatusBackground(gpsCache.getAcc()));
            Log.d(TAG, "checkGPS: asdadssad");
        } else {

            indikator.setBackground(getStatusBackground(0));
            Log.d(TAG, "checkGPS: null");
        }
    }

    void thischeckDataOffline() {
        if (cekDataOffline() == false) {
            isOn = false;
        } else {
            isOn = true;
        }
        super.onStart();
    }

    Drawable getStatusBackground(float value) {
        if (chekUserGroup(realm).matches("site_engineer")) {
//            Toast.makeText(getApplicationContext(), "chekUserGroup(realm) := " + chekUserGroup(realm), Toast.LENGTH_SHORT).show();

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
        } else {
//            Toast.makeText(getApplicationContext(), "chekUserGroup(realm) := " + chekUserGroup(realm), Toast.LENGTH_SHORT).show();
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
    }


    private void setupView() {
        Utils.setToolbar(this, "");
        gpsCache = getRealm().where(GpsCache.class).findFirst();
        presenter = new ProjectListPresenter(this, this, "", "");
        searchView.setOnQueryTextListener(this);
        adapter = new AdapterActivityRealm(getRealm().where(Project.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doGetTask();
            }
        });


    }

    private void doGetTask() {
        baseshowProgress(true);
        presenter.getTask();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filterResults(newText, getRealm());
        return false;
    }

    @Override
    public void onUserExpired() {

    }

    @Override
    public void onFinish(boolean isSucces, String msg) {
        baseshowProgress(false);
        if (swrefresh.isRefreshing()) {
            swrefresh.setRefreshing(false);
        }
        if (!isSucces)
            Utils.showToast(this, msg);
    }


//    public void onClickItem(Project m) {
//        if (chekUserGroup(realm).matches("site_engineer")) {
////            Toast.makeText(getApplicationContext(), "chekUserGroup(realm) := " + chekUserGroup(realm), Toast.LENGTH_SHORT).show();
//
//            if (gpsCache != null && !gpsCache.isTooFar(m, this)) {
////        if (!gpsCache.isTooFar(m, this)) {
//                ActivityUtils.goToActivity(this, TaskActivity.class, 101, m.getProject_id(), Project.PROJECT_ID);
//            } else {
//                Utils.showToast(this, "Pastikan GPS anda Valid..");
////             ActivityUtils.goToActivity(this, TaskActivity.class, 101, m.getProject_id(), Project.PROJECT_ID);
//            }
//        } else {
////            Toast.makeText(getApplicationContext(), "chekUserGroup(realm) := " + chekUserGroup(realm), Toast.LENGTH_SHORT).show();
//            if (gpsCache != null) {
//                ActivityUtils.goToActivity(this, TaskActivity.class, 101, m.getProject_id(), Project.PROJECT_ID);
//            } else {
//                Utils.showToast(this, "Pastikan GPS anda Valid..");
////            ActivityUtils.goToActivity(this, TaskActivity.class, 101, m.getProject_id(), Project.PROJECT_ID);
//            }
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        menuLogout = menu.findItem(R.id.logout_project);
////        if (isOn == false) {
////            Utils.showToast(this, "isOn false");
////            menuLogout.setVisible(false);
////        } else {
////            Utils.showToast(this, "isOn true");
////            menuLogout.setVisible(true);
////        }
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.history) {
//            Intent intent = new Intent(ProjectListActivity.this, HistoryActivity.class);
//            startActivity(intent);
////            Intent intent = new Intent(ProjectListActivity.this, TabHistoryActivity.class);
////            startActivity(intent);
////            Intent myIntent = new Intent(context, TabHistoryActivity.class);
////            myIntent.putExtra("projectId", m.getProject_id());
////            myIntent.putExtra("is_offline", "0");
////            context.startActivity(myIntent);
//        }
//        if (item.getItemId() == R.id.update_site) {
//            Intent intent = new Intent(ProjectListActivity.this, HistorySiteActivity.class);
//            startActivity(intent);
//        }
//        if (item.getItemId() == R.id.version_apk_history) {
//            String versionApk = BuildConfig.VERSION_NAME;
//            cekAPK(versionApk);
//        }
////        OfflineDataTransaction.cekDataOffline(getRealm());
////        if (OfflineDataTransaction.cekDataOffline(getRealm())  > 0) {
//
//        if (item.getItemId() == R.id.logout_project) {
//            if (isOn == false) {
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

    public void cekAPK(String versionApk) {
        Toast.makeText(getApplicationContext(), "versionApk" + versionApk, Toast.LENGTH_SHORT).show();
        presenter.cekApk(versionApk);
    }

    public void doLogout() {

        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userPass = user.getPassword();
//         Toast.makeText(getApplicationContext(), "isi userName:= "+userName+": isiPassword := "+userPass+":", Toast.LENGTH_SHORT).show();
        presenter = new ProjectListPresenter(this, this, userName, userPass);
        presenter.logout(userName, userPass);


//        User.doLogout(ProjectListActivity.this, getRealm());
//        User user = realm.where(User.class).findFirst();
//        String userName = user.getUsername();
//        String userPass = user.getPassword();
////        Toast.makeText(getApplicationContext(), "isi userName:= "+userName+": isiPassword := "+userPass+":", Toast.LENGTH_SHORT).show();
//        presenter = new ProjectListPresenter(this, this, userName, userPass);
//        presenter.logout(userName, userPass);
////        User.doLogout(ProjectListActivity.this, getRealm());
    }

    @Override
    public void onError(boolean status, String msg) {
        Utils.showToast(this, msg);
    }

    @Override
    public void onSuccess(String msg) {

    }
}