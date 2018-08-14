package com.cudo.mproject.Menu.Site.Person.History;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Site.Person.PersonPhotoActivity;
import com.cudo.mproject.Model.DataPenjagaLahan;
import com.cudo.mproject.Model.PhotoPerson;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class PersonHistoryActivity extends BaseActivity implements PersonHistoryInterface.View {
    String TAG = getClass().getSimpleName();
    //    BaseActivity baseActivity;
    @BindView(R.id.no_phone_penjaga_lahan_history)
    TextView no_phone_penjaga_lahan_history;

    @BindView(R.id.no_ktp_penjaga_lahan_history)
    TextView no_ktp_penjaga_lahan_history;

    @BindView(R.id.nama_penjaga_lahan_history)
    TextView nama_penjaga_lahan_history;

    @BindView(R.id.alamat_penjaga_lahan_history)
    TextView alamat_penjaga_lahan_history;

    @BindView(R.id.provinsi_penjaga_lahan_history)
    TextView provinsi_penjaga_lahan_history;

    @BindView(R.id.kabupaten_penjaga_lahan_history)
    TextView kabupaten_penjaga_lahan_history;

    @BindView(R.id.kecamatan_penjaga_lahan_history)
    TextView kecamatan_penjaga_lahan_history;

    @BindView(R.id.submit_person_history)
    Button btnSubmit_submit_person_history;
    //    PersonHistoryPresenter prsonHistoryPresenter;
    @BindView(R.id.count_photo_person_history)
    TextView count_photo_person_history;

    //    String projectId = null;
    String Id = null;
    Realm realm;
    //    Project dataProject;
    DataPenjagaLahan dataPenjagaLahan;
    PhotoPerson photoPerson;
    int photo = 0;

    @BindView(R.id.img_person_history)
    View img_person_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_history);
        Intent intent = getIntent();
        ButterKnife.bind(this);

        String Id = intent.getStringExtra("projectId");
        String is_offline = "1";//intent.getStringExtra("is_offline");

        initExtra(Id, is_offline);

        findViewById(R.id.img_person_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(Id);
            }
        });
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

    void initExtra(String projectId, String is_offline) {
        realm = Realm.getDefaultInstance();
        try {
            User user = realm.where(User.class).findFirst();
            dataPenjagaLahan = realm.where(DataPenjagaLahan.class)
                    .equalTo("project_id", projectId)
                    .equalTo("userName", user.getUsername())
                    .findFirst();

            Log.d(TAG, "initExtra: dataPenjagaLahan:" + dataPenjagaLahan);
            Id = dataPenjagaLahan.getProject_id();
            setView(dataPenjagaLahan, is_offline);
//
            Log.d(TAG, "initExtra: " + dataPenjagaLahan.getProject_id());
            Log.d(TAG, "initExtra: " + is_offline);
        } catch (Exception e) {

        }
        if (dataPenjagaLahan != null) {
//            if(dataPenjagaLahan.getIs_submited()) != nul
            if (dataPenjagaLahan.getIs_submited().contains("1")) {

                btnSubmit_submit_person_history.setEnabled(false);
                btnSubmit_submit_person_history.setVisibility(View.INVISIBLE);
            } else {

                btnSubmit_submit_person_history.setEnabled(true);
                btnSubmit_submit_person_history.setVisibility(View.VISIBLE);
            }
        } else {
            btnSubmit_submit_person_history.setEnabled(false);
            btnSubmit_submit_person_history.setVisibility(View.INVISIBLE);
        }
    }

    void takePhoto(String projectId) {
        User user = getRealm().where(User.class).findFirst();

        dataPenjagaLahan = getRealm().where(DataPenjagaLahan.class)
                .equalTo("project_id", projectId)
                .equalTo("userName", user.getUsername())
                .findFirst();
//
        photoPerson  = getRealm().where(PhotoPerson.class)
                .equalTo("projectid", dataPenjagaLahan.getProject_id())
                .equalTo("userName", user.getUsername())
                .findFirst();
//
        Intent myIntent = new Intent(this, PersonPhotoActivity.class);
        myIntent.putExtra("projectId", projectId);
        myIntent.putExtra("is_offline", "1");
        myIntent.putExtra("dateSubmit", dataPenjagaLahan.getDateSubmit());
        myIntent.putExtra("timeSubmit", dataPenjagaLahan.getTimeSubmit());
        startActivity(myIntent);
    }

    void setView(DataPenjagaLahan dataPenjagaLahan, String is_offline) {//Project dataProject, String is_offline
        Log.d(TAG, "setView: " + dataPenjagaLahan.getProject_id());
//        projectId = dataProject.getProject_id();
        checkUser();
        if (dataPenjagaLahan.getNamaPenjagaLahan() != null) {
            nama_penjaga_lahan_history.setText(dataPenjagaLahan.getNamaPenjagaLahan());
            nama_penjaga_lahan_history.setFocusable(false);
            nama_penjaga_lahan_history.setClickable(false);
        }
        if (dataPenjagaLahan.getAlamat() != null) {
            alamat_penjaga_lahan_history.setText(dataPenjagaLahan.getAlamat());
            alamat_penjaga_lahan_history.setFocusable(false);
            alamat_penjaga_lahan_history.setClickable(false);
        }
        if (dataPenjagaLahan.getProvinsi() != null) {
            provinsi_penjaga_lahan_history.setText(dataPenjagaLahan.getProvinsi());
            provinsi_penjaga_lahan_history.setFocusable(false);
            provinsi_penjaga_lahan_history.setClickable(false);
        }
        if (dataPenjagaLahan.getKabupaten() != null) {
            kabupaten_penjaga_lahan_history.setText(dataPenjagaLahan.getKabupaten());
            kabupaten_penjaga_lahan_history.setFocusable(false);
            kabupaten_penjaga_lahan_history.setClickable(false);
        }
        if (dataPenjagaLahan.getKecamatan() != null) {
            kecamatan_penjaga_lahan_history.setText(dataPenjagaLahan.getKecamatan());
            kecamatan_penjaga_lahan_history.setFocusable(false);
            kecamatan_penjaga_lahan_history.setClickable(false);
        }
        if (dataPenjagaLahan.getNoTlp() != null) {
            no_phone_penjaga_lahan_history.setText(dataPenjagaLahan.getNoTlp());
            no_phone_penjaga_lahan_history.setFocusable(false);
            no_phone_penjaga_lahan_history.setClickable(false);
        }
        if (dataPenjagaLahan.getNoKtp() != null) {
            no_ktp_penjaga_lahan_history.setText(dataPenjagaLahan.getNoKtp());
            no_ktp_penjaga_lahan_history.setFocusable(false);
            no_ktp_penjaga_lahan_history.setClickable(false);
        }
    }


    void thischeckInet() {
        if (ConnectivityReceiverService.isConnected(PersonHistoryActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit_submit_person_history.setBackgroundColor(Color.parseColor("#B71C1C"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_data_penjaga_lahan);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit_submit_person_history.setBackgroundColor(Color.parseColor("#80F44336"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_data_penjaga_lahan);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit_submit_person_history.setBackgroundColor(Color.parseColor("#80F44336"));
            TextView linearAct = (TextView) findViewById(R.id.linear_data_penjaga_lahan);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
        }
    }

    @OnClick(R.id.submit_person_history)
    void setSubmitDataPerson() {
        if (ConnectivityReceiverService.isConnected(PersonHistoryActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    doSubmitDataperson("1");
                } else {
                    showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, data Akan disimpan di local storage..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                                offlineSubmit();
                            doSubmitDataperson("2");
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
                    doSubmitDataperson("2");
                    dialog.dismiss();
                }
            }, true, "");
        }
    }

    void countFoto() {
        int photoPerson = 0;
        if (dataPenjagaLahan.getIs_submited() == "0") {
            photoPerson = getRealm().where(PhotoPerson.class)
                    .equalTo(PhotoPerson.PROJECT_ID, dataPenjagaLahan.getProject_id())
                    .equalTo("statusPhoto", "1")
                    .equalTo("uploaded", false)
                    .findAll().size();

        } else {
            photoPerson = getRealm().where(PhotoPerson.class)
                    .equalTo(PhotoPerson.PROJECT_ID, dataPenjagaLahan.getProject_id())
                    .equalTo("statusPhoto", "1")
                    .equalTo("uploaded", true)
                    .findAll().size();
//        Log.d(TAG, "countFile: Id" + Id);
//        Log.d(TAG, "countFile: Id" + dataProject.getProject_id());
        }
        Log.d(TAG, "countFile: " + photoPerson);
        count_photo_person_history.setText(photoPerson + " Files");
        photo = photoPerson;
    }

    void doSubmitDataperson(String isOnline) {


//        PersonPresenter = new PersonPresenter(this, this, dataProject.getProject_id());
//        PersonPresenter.submitDataPerson(dataProject.getProject_id(), nama_penjaga_lahan.getText().toString(), alamat_penjaga_lahan.getText().toString(),provinsi_penjaga_lahan.getText().toString(),
//                kabupaten_penjaga_lahan.getText().toString(),kecamatan_penjaga_lahan.getText().toString(),no_ktp_penjaga_lahan.getText().toString(), no_phone_penjaga_lahan.getText().toString(), isOnline);

    }

//    @Override
//    public void onClick(View v) {
//
//    }


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
                    btnSubmit_submit_person_history.setBackgroundColor(Color.parseColor("#80F44336"));
                    btnSubmit_submit_person_history.setEnabled(false);
                }
            }, true);
        }
    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }

    @OnClick(R.id.pic_person_history)
    void setPic() {
//        Utils.showToast(baseActivity, Id);
//        Intent personIntent = new Intent(this, PersonPhotoActivity.class);
//        personIntent.putExtra("projectId", Id);
//        personIntent.putExtra("is_offline", "0");
//        startActivity(personIntent);
    }


}

