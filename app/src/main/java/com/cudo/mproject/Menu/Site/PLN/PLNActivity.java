package com.cudo.mproject.Menu.Site.PLN;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Model.DataPLNSite;
import com.cudo.mproject.Model.PhotoPLN;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;
import com.cudo.mproject.Utils.Utils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class PLNActivity extends BaseActivity implements PLNInterface.View, View.OnClickListener {
    public static PLNActivity plnActivity;
    String TAG = getClass().getSimpleName();
//    BaseActivity baseActivity;


    @BindView(R.id.id_pelangan_pln)
    TextView id_pelangan_pln;
    @BindView(R.id.nama_pelanggan_pln)
    TextView nama_pelanggan_pln;
    @BindView(R.id.daya_listrik)
    TextView daya_listrik;
    @BindView(R.id.count_photo_pln)
    TextView count_photo_pln;
    @BindView(R.id.submit_pln)
    Button btnSubmit_submit_pln;
    //
    String projectId = null;
    String Id = null;
    int photo = 0;
    Realm realm;
    DataPLNSite dataPLNSite;
    Project dataProject;
    PLNPresenter plnPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pln);

        Intent intent = getIntent();
        ButterKnife.bind(this);
        String Id = intent.getStringExtra("projectId");
        String is_offline = "0";//intent.getStringExtra("is_offline");
        initExtra(Id, is_offline);
        findViewById(R.id.img_pln).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(Id);
            }
        });
    }

    void takePhoto(String projectId) {

        Intent myIntent = new Intent(this, PLNPhotoActivity.class);
        myIntent.putExtra("projectId", projectId);
        myIntent.putExtra("is_offline", "0");
        startActivity(myIntent);
    }

    void initExtra(String projectId, String is_offline) {
        realm = Realm.getDefaultInstance();
        Id = getIntent().getStringExtra(Project.PROJECT_ID);

        User user = realm.where(User.class).findFirst();
        dataProject = realm.where(Project.class)
                .equalTo(Project.PROJECT_ID, projectId)
                .findFirst();
        Log.d(TAG, "initExtra: dataProject:" + dataProject);
//
        setView(dataProject, is_offline);
        Log.d(TAG, "initExtra: " + dataProject.getProject_id());
        Log.d(TAG, "initExtra: " + is_offline);
        dataPLNSite = realm.where(DataPLNSite.class)
                .equalTo("project_id", projectId)
                .equalTo("userName", user.getUsername())
                .findFirst();
        if (dataPLNSite != null) {
            if (dataPLNSite.getIs_submited().contains("1")) {

                btnSubmit_submit_pln.setEnabled(false);
                btnSubmit_submit_pln.setVisibility(View.INVISIBLE);
            }
        }
    }

    void setView(Project dataProject, String is_offline) {
        Log.d(TAG, "setView: " + dataProject.getProject_id());
        checkUser();
//        projectId = dataProject.getProject_id();
    }

    @Override
    protected void onStart() {
        super.onStart();
        thischeckInet();
        try {
            countFoto();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    void thischeckInet() {
        if (ConnectivityReceiverService.isConnected(PLNActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit_submit_pln.setBackgroundColor(Color.parseColor("#B71C1C"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_pln);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit_submit_pln.setBackgroundColor(Color.parseColor("#80F44336"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_pln);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit_submit_pln.setBackgroundColor(Color.parseColor("#80F44336"));
            TextView linearAct = (TextView) findViewById(R.id.linear_pln);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
        }
    }

    @OnClick(R.id.submit_pln)
    void setSubmitDataPerson() {
        if (ConnectivityReceiverService.isConnected(PLNActivity.this)) {
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

        int photoPln = getRealm().where(PhotoPLN.class)
                .equalTo(PhotoPLN.PROJECT_ID, dataProject.getProject_id())
                .equalTo("status_photo", "0")
                .equalTo("uploaded", false)
                .findAll().size();
//        Log.d(TAG, "countFile: Id" + Id);
//        Log.d(TAG, "countFile: Id" + dataProject.getProject_id());
        Log.d(TAG, "countFile: " + photoPln);
        count_photo_pln.setText(photoPln + " Photo");
        photo = photoPln;
    }

    void doSubmitDataPLN(String isOnline) {
        if (id_pelangan_pln.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            id_pelangan_pln.setError("Field  Id Pelanggan tidak boleh null!");
            return;
        }
        if (nama_pelanggan_pln.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            nama_pelanggan_pln.setError("Field  Nama Pelanggan tidak boleh null!");
            return;
        }
        if (daya_listrik.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            daya_listrik.setError("Field  Daya Listrik tidak boleh null!");
            return;
        }
        if (photo == 0) {
            Utils.showToast(this, "Data Evidence Belum di Isi");
//            count_photo_person.setError("Data Evidence Belum di Isi");
            showAlertDialog("ATTENTION", "Data Evidence Belum di Isi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, true);
            return;
        }

        plnPresenter = new PLNPresenter(this, this, dataProject.getProject_id());
        plnPresenter.submitDataPLN(dataProject.getProject_id(), id_pelangan_pln.getText().toString(), nama_pelanggan_pln.getText().toString(), daya_listrik.getText().toString(), isOnline);
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
                    btnSubmit_submit_pln.setBackgroundColor(Color.parseColor("#80F44336"));
                    btnSubmit_submit_pln.setEnabled(false);
                }
            }, true);
        }
    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }

    @OnClick(R.id.pic_pln)
    void setPic() {
//        Intent plnIntent = new Intent(this, PLNPhotoActivity.class);
//        startActivity(plnIntent);
    }


    @Override
    public void onClick(View v) {

    }
}
