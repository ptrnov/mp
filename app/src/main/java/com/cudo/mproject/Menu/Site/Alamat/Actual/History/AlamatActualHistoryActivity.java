package com.cudo.mproject.Menu.Site.Alamat.Actual.History;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Site.Alamat.Actual.AlamatActualActivity;
import com.cudo.mproject.Menu.Site.Alamat.Actual.AlamatActualPresenter;
import com.cudo.mproject.Model.DataAlamatActualOffline;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AlamatActualHistoryActivity extends BaseActivity implements  AlamatActualHistoryInterface.View {
    String TAG = getClass().getSimpleName();

    @BindView(R.id.site_name_actual_history)
    TextView site_name_actual_history;
    @BindView(R.id.alamat_actual_history)
    TextView alamat_actual_history;
    @BindView(R.id.long_actual_history)
    TextView long_actual_history;
    @BindView(R.id.lat_actual_history)
    TextView lat_actual_history;
    @BindView(R.id.provinsi_actual_history)
    TextView provinsi_actual_history;
    @BindView(R.id.kabupaten_actual_history)
    TextView kabupaten_actual_history;
    @BindView(R.id.kecamatan_actual_history)
    TextView kecamatan_actual_history;
//    @BindView(R.id.status_approval_actual_history)
//    TextView status_approval_actual_history;
//    @BindView(R.id.id_alamat_actual)
//    TextView id_alamat_actual;

    Realm realm;
//    Project dataProject;
    DataAlamatActualOffline dataAlamatActualOffline;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    AlamatActualHistoryPresenter alamatActuaHistorylPresenter;
    String userName = null;
    String userId = null;
    String projectId = null;
    //
    @BindView(R.id.submit_alamatActual_history)
    Button submit_alamatActual_history;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alamat_actual_history);
        Intent intent = getIntent();
        ButterKnife.bind(this);
        String Id = intent.getStringExtra("projectId");
        String is_offline = intent.getStringExtra("is_offline");
        initExtra(Id, is_offline);

    }
//
void initExtra(String Id, String is_offline) {
    realm = Realm.getDefaultInstance();
    try {
//
        User user = realm.where(User.class).findFirst();
//
        dataAlamatActualOffline  = realm.where(DataAlamatActualOffline.class)
                .equalTo("project_id", Id)
                .equalTo("userName", user.getUsername())
                .findFirst();
//            dataProject = realm.where(Project.class)
//                    .equalTo(Project.PROJECT_ID, Id)
//                    .findFirst();

        Log.d(TAG, "onResponse: dataAlamatActualOffline:" + dataAlamatActualOffline);
//
        setView(dataAlamatActualOffline, is_offline);
//            Log.d(TAG, "onResponse: " + dataProject.getProject_id());
        Log.d(TAG, "onResponse: " + is_offline);

    } catch (Exception e) {

    }
    if(dataAlamatActualOffline!=null) {
        if (dataAlamatActualOffline.getStatus_alamat_actual_offline().contains("1")) {

            submit_alamatActual_history.setEnabled(false);
            submit_alamatActual_history.setVisibility(View.INVISIBLE);
//            approval_layout.setVisibility(LinearLayout.VISIBLE);
//            status_approval.setText("Request Approval");
        } else {
            submit_alamatActual_history.setEnabled(true);
            submit_alamatActual_history.setVisibility(View.VISIBLE);
//            submit_alamatActual_history.setVisibility(LinearLayout.GONE);
//            approval_layout.setVisibility(LinearLayout.INVISIBLE);
//            status_approval.setEnabled(false);
        }
    }else{
        submit_alamatActual_history.setEnabled(false);
        submit_alamatActual_history.setVisibility(View.INVISIBLE);
//        submit_alamatActual_history.setVisibility(LinearLayout.GONE);
    }
}
    void setView(DataAlamatActualOffline dataAlamatActualOffline, String is_offline) {
        Log.d(TAG, "onResponse: view " + dataAlamatActualOffline.getProject_id());
//        checkUser();
        projectId = dataAlamatActualOffline.getProject_id();
        checkUser();

        if(dataAlamatActualOffline.getSiteName() !=null){
            site_name_actual_history.setText(dataAlamatActualOffline.getSiteName());
        }
        if(dataAlamatActualOffline.getAlamat() !=null){
            alamat_actual_history.setText(dataAlamatActualOffline.getAlamat());
        }
        if(dataAlamatActualOffline.getLongitude() !=null){
            long_actual_history.setText(dataAlamatActualOffline.getLongitude());
        }
        if(dataAlamatActualOffline.getLatitude() !=null){
            lat_actual_history.setText(dataAlamatActualOffline.getLatitude());
        }
        if(dataAlamatActualOffline.getProvinsi() !=null){
            provinsi_actual_history.setText(dataAlamatActualOffline.getProvinsi());
        }
        if(dataAlamatActualOffline.getKabupaten()!=null){
            kabupaten_actual_history.setText(dataAlamatActualOffline.getKabupaten());
        }
        if(dataAlamatActualOffline.getKecamatan() !=null){
            kecamatan_actual_history.setText(dataAlamatActualOffline.getKecamatan());
        }
//        if(dataAlamatActualOffline.getIdAlamatActualOffline() !=0){
//            id_alamat_actual.setText(dataAlamatActualOffline.getIdAlamatActualOffline());
//        }
        if(dataAlamatActualOffline.getStatus_alamat_actual_offline()=="0"){

        }
//         status_approval_actual_history;

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
        if (!ConnectivityReceiverService.isConnected(AlamatActualHistoryActivity.this)) {
            showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, true, "");
//            submit_alamatActual_history.setEnabled(false);
//            submit_alamatActual_history.setVisibility(View.INVISIBLE);
//            TextView linearLayout = (TextView) findViewById(R.id.linear_alamat_actual_history);
//            linearLayout.setTextColor(Color.parseColor("#80F44336"));
//            return;
        } else {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    submit_alamatActual_history.setBackgroundColor(Color.parseColor("#B71C1C"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_alamat_actual_history);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    submit_alamatActual_history.setBackgroundColor(Color.parseColor("#80F44336"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_alamat_actual_history);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onSuccessSubmit(boolean isSucces, String msg) {

    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }
}
