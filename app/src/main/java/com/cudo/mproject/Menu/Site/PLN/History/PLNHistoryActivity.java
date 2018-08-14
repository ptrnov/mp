package com.cudo.mproject.Menu.Site.PLN.History;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Site.IMB.History.IMBHistoryActivity;
import com.cudo.mproject.Menu.Site.IMB.IMBPhotoActivity;
import com.cudo.mproject.Model.DataPLNSite;
import com.cudo.mproject.Model.PhotoPLN;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class PLNHistoryActivity extends BaseActivity implements PLNHistoryInterface.View, View.OnClickListener {
    String TAG = getClass().getSimpleName();

    @BindView(R.id.id_pelangan_pln_history)
    TextView id_pelangan_pln_history;
    @BindView(R.id.nama_pelanggan_pln_history)
    TextView nama_pelanggan_pln_history;
    @BindView(R.id.daya_listrik_history)
    TextView daya_listrik_history;
    @BindView(R.id.count_photo_pln_history)
    TextView count_photo_pln_history;
    @BindView(R.id.submit_pln_history)
    Button btnSubmit_submit_pln_history;

    String projectId = null;
    String Id = null;
    int photo = 0;
    Realm realm;
    DataPLNSite dataPLNSite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pln_history);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        String Id = intent.getStringExtra("projectId");
        String is_offline = "0";//intent.getStringExtra("is_offline");
        initExtra(Id, is_offline);

        findViewById(R.id.img_pln_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(Id);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        thischeckInet();
        try {
            countFoto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initExtra(String projectId, String is_offline) {
        realm = Realm.getDefaultInstance();
        try {
            User user = realm.where(User.class).findFirst();
            dataPLNSite = realm.where(DataPLNSite.class)
                    .equalTo("project_id", projectId)
                    .equalTo("userName", user.getUsername())
                    .findFirst();

            Log.d(TAG, "initExtra: dataPLNSite:" + dataPLNSite);
            Id = dataPLNSite.getProject_id();
            setView(dataPLNSite, is_offline);
//
            Log.d(TAG, "initExtra: " + dataPLNSite.getProject_id());
            Log.d(TAG, "initExtra: " + is_offline);
        } catch (Exception e) {

        }
        if (dataPLNSite != null) {
            if (dataPLNSite.getIs_submited().contains("1")) {

                btnSubmit_submit_pln_history.setEnabled(false);
                btnSubmit_submit_pln_history.setVisibility(View.INVISIBLE);
            } else {

                btnSubmit_submit_pln_history.setEnabled(true);
                btnSubmit_submit_pln_history.setVisibility(View.VISIBLE);
//                btnSubmit_submit_pln_history.setVisibility(LinearLayout.GONE);
            }
        } else {
            btnSubmit_submit_pln_history.setEnabled(false);
            btnSubmit_submit_pln_history.setVisibility(View.INVISIBLE);
//            btnSubmit_submit_pln_history.setVisibility(LinearLayout.GONE);
        }
    }
    void takePhoto(String projectId) {

        Intent myIntent = new Intent(this, IMBPhotoActivity.class);
        myIntent.putExtra("projectId", projectId);
        myIntent.putExtra("is_offline", "0");
        startActivity(myIntent);
    }
    void setView(DataPLNSite dataPLNSite, String is_offline) {
        Log.d(TAG, "setView: " + dataPLNSite.getProject_id());
//        projectId = dataProject.getProject_id();
        checkUser();
        if (dataPLNSite.getIdPelanggan() != null) {
            id_pelangan_pln_history.setText(dataPLNSite.getIdPelanggan());
        }
        if (dataPLNSite.getNamaPelanggan() != null) {
            nama_pelanggan_pln_history.setText(dataPLNSite.getNamaPelanggan());
        }
        if (dataPLNSite.getDaya() != null) {
            daya_listrik_history.setText(dataPLNSite.getDaya());
        }
    }
    void thischeckInet() {
        if (ConnectivityReceiverService.isConnected(PLNHistoryActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit_submit_pln_history.setBackgroundColor(Color.parseColor("#B71C1C"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_pln_history);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit_submit_pln_history.setBackgroundColor(Color.parseColor("#80F44336"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_pln_history);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit_submit_pln_history.setBackgroundColor(Color.parseColor("#80F44336"));
            TextView linearAct = (TextView) findViewById(R.id.linear_pln_history);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
        }
    }

    @OnClick(R.id.submit_pln_history)
    void setSubmitDataPLN() {
        if (ConnectivityReceiverService.isConnected(PLNHistoryActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    doSubmitDataPLN("1");
                } else {
                    showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, data Akan disimpan di local storage..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                                offlineSubmit();
                            doSubmitDataPLN("2");
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
                    doSubmitDataPLN("2");
                    dialog.dismiss();
                }
            }, true, "");
        }
    }
    void countFoto() {
        int photoPLN = 0;
        if (dataPLNSite.getIs_submited() == "0") {
            photoPLN = getRealm().where(PhotoPLN.class)
                    .equalTo(PhotoPLN.PROJECT_ID, dataPLNSite.getProject_id())
                    .equalTo("status_photo", "1")
                    .equalTo("uploaded", false)
                    .findAll().size();

        } else {
            photoPLN = getRealm().where(PhotoPLN.class)
                    .equalTo(PhotoPLN.PROJECT_ID, dataPLNSite.getProject_id())
                    .equalTo("status_photo", "1")
                    .equalTo("uploaded", true)
                    .findAll().size();
//        Log.d(TAG, "countFile: Id" + Id);
//        Log.d(TAG, "countFile: Id" + dataProject.getProject_id());
        }
        Log.d(TAG, "countFile: " + photoPLN);
        count_photo_pln_history.setText(photoPLN + " Files");
        photo = photoPLN;
    }

    void doSubmitDataPLN(String isOnline) {

    }
    @Override
    public void onClick(View v) {

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
                    btnSubmit_submit_pln_history.setBackgroundColor(Color.parseColor("#80F44336"));
                    btnSubmit_submit_pln_history.setEnabled(false);
                }
            }, true);
        }
    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }
    @OnClick(R.id.pic_pln_history)
    void setPic() {
//        Utils.showToast(baseActivity, Id);
//        Intent personIntent = new Intent(this, PersonPhotoActivity.class);
//        personIntent.putExtra("projectId", Id);
//        personIntent.putExtra("is_offline", "0");
//        startActivity(personIntent);
    }
}
