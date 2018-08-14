package com.cudo.mproject.Menu.Site;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.History.HistoryActivityInterface;
import com.cudo.mproject.Menu.Project.ProjectListActivity;
import com.cudo.mproject.Menu.TaskActivity.TaskActivity;
import com.cudo.mproject.Menu.TaskHistory.TaskHistoryInterface;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class SiteProfileActivity extends BaseActivity implements SiteProfileInterface.View {
    String TAG = getClass().getSimpleName();
    Realm realm;
    /*data nominal*/
    @BindView(R.id.project_id_nominal)
    TextView project_id_nominal;
    @BindView(R.id.alamat_nominal)
    TextView alamat_nominal;
    @BindView(R.id.provinsi_nominal)
    TextView provinsi_nominal;
    @BindView(R.id.kabupaten_nominal)
    TextView kabupaten_nominal;
    @BindView(R.id.kecamatan_nominal)
    TextView kecamatan_nominal;
    @BindView(R.id.sitename_nominal)
    TextView sitename_nominal;
    @BindView(R.id.long_site_nominal)
    TextView long_site_nominal;
    @BindView(R.id.lat_site_nominal)
    TextView lat_site_nominal;
    @BindView(R.id.sow_nominal)
    TextView sow_nominal;
    /*data actual*/
//    @BindView(R.id.site_name_actual)
//    EditText site_name_actual;
//    @BindView(R.id.revenue_actual)
//    EditText revenue_actual;
//    @BindView(R.id.alamat_actual)
//    EditText alamat_actual;
//    @BindView(R.id.long_actual)
//    EditText long_actual;
//    @BindView(R.id.lat_actual)
//    EditText lat_actual;

    //    String sProjectid;
    SiteProfilePresenter siteProfilePresenter;
    Project dataProject;
    BaseActivity baseActivity;
    public GpsCache gpsCache;

//    Location current = new Location("current");
//    Location site = new Location("site");
//    float jarak = current.distanceTo(site);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_site_profile);
        ButterKnife.bind(this);
//        buttonSet();
        Intent intent = getIntent();
        String Id = intent.getStringExtra("projectId");
        String is_offline = intent.getStringExtra("is_offline");
        initExtra(Id, is_offline);
        gpsCache = getRealm().where(GpsCache.class).findFirst();
//        cekGPS(dataProject);
    }
//    void cekGPS(Project dataProject){
//        if (gpsCache == null && gpsCache.isTooFar(this.dataProject, this)) {
//            baseActivity.showAlertDialog("Warning..",
//                    "Lokasi Anda terlalu jauh dari site.. \r\n" +
//                            "Jarak Anda " + jarak + "M dari site \r\n" +
//                            "Lokasi site berada di " + dataProject.getLat() + "," + dataProject.getLng()
//                    , null, true);
//            return;
//        }
//
//    }
    void initExtra(String Id, String is_offline) {
        try {
//            sProjectid = getIntent().getStringExtra(Project.PROJECT_ID);
            realm = Realm.getDefaultInstance();
            dataProject = realm.where(Project.class)
                    .equalTo(Project.PROJECT_ID, Id)
                    .findFirst();
            Log.d(TAG, "initExtra: dataProject:" + dataProject);
            setView(dataProject, is_offline);
//            if (id == null) {
//
//                id = getIntent().getStringExtra(Project.PROJECT_ID);
//            }
            Log.d(TAG, "initExtra: " + dataProject.getProject_id());
            Log.d(TAG, "initExtra: " + is_offline);

        } catch (Exception e) {

        }
    }

    void setView(Project dataProject, String is_offline) {
        Log.d(TAG, "setView: " + dataProject.getProject_id());
        checkUser();
        /*data nominal*/
        project_id_nominal.setText(dataProject.getProject_id());
        alamat_nominal.setText(dataProject.getAlamat());
        sitename_nominal.setText(dataProject.getSite_name());
        String isi_long = Double.toString(dataProject.getLng());
        long_site_nominal.setText(isi_long);
        String isi_lat = Double.toString(dataProject.getLat());
        lat_site_nominal.setText(isi_lat);
        sow_nominal.setText(dataProject.getSow());
        provinsi_nominal.setText(dataProject.getAlamat_site_prov());
        kabupaten_nominal.setText(dataProject.getAlamat_site_kab());
        kecamatan_nominal.setText(dataProject.getAlamat_site_kecamatan());
/*data actual*/
//         revenue_actual.setText(dataProject.getActual_revenue());
//         site_name_actual.setText(dataProject.getSite_name_actual());
//         alamat_actual.setText(dataProject.getAlamat_actual());
//         long_actual.setText(dataProject.getLong_actual());
//         lat_actual.setText(dataProject.getLat_actual());
    }

//    @OnClick(R.id.back_site)
//    void setBack() {
//        onBackPressed();
//    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onSuccessSubmit(boolean isSucces, String msg) {

    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }
}
