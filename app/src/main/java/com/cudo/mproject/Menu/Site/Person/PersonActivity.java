package com.cudo.mproject.Menu.Site.Person;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Photo.PhotoActivity;
import com.cudo.mproject.Model.DataPenjagaLahan;
import com.cudo.mproject.Model.PhotoPerson;
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

public class PersonActivity extends BaseActivity implements PersonInterface.View{//, View.OnClickListener {
    public static PersonActivity personActivity;
    String TAG = getClass().getSimpleName();
    @BindView(R.id.no_phone_penjaga_lahan)
    TextView no_phone_penjaga_lahan;

    @BindView(R.id.no_ktp_penjaga_lahan)
    TextView no_ktp_penjaga_lahan;

    @BindView(R.id.nama_penjaga_lahan)
    TextView nama_penjaga_lahan;

    @BindView(R.id.alamat_penjaga_lahan)
    TextView alamat_penjaga_lahan;

    @BindView(R.id.provinsi_penjaga_lahan)
    TextView provinsi_penjaga_lahan;

    @BindView(R.id.kabupaten_penjaga_lahan)
    TextView kabupaten_penjaga_lahan;

    @BindView(R.id.kecamatan_penjaga_lahan)
    TextView kecamatan_penjaga_lahan;

    @BindView(R.id.submit_person)
    Button btnSubmit_submit_person;
    PersonPresenter PersonPresenter;
    @BindView(R.id.count_photo_person)
    TextView count_photo_person;

    //    String projectId = null;
    String Id = null;
    DataPenjagaLahan dataPenjagaLahan;
    Realm realm;
    Project dataProject;
    int photo=0;

    @BindView(R.id.img_person)
    View img_person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Intent intent = getIntent();
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
//
        String Id = intent.getStringExtra("projectId");
        String is_offline = "0";//intent.getStringExtra("is_offline");
//
        initExtra(Id, is_offline);
//        countFoto();
        findViewById(R.id.img_person).setOnClickListener(new View.OnClickListener() {
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
        countFoto();
        try {
            countFoto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
    void initExtra(String projectId, String is_offline) {
        try {
//
            User user = realm.where(User.class).findFirst();
            Id = getIntent().getStringExtra(Project.PROJECT_ID);
//            realm = Realm.getDefaultInstance();
            dataProject = realm.where(Project.class)
                    .equalTo(Project.PROJECT_ID, projectId)
                    .findFirst();
            Log.d(TAG, "initExtra: dataProject:" + dataProject);
//
            setView(dataProject, is_offline);
            Log.d(TAG, "initExtra: " + dataProject.getProject_id());
            Log.d(TAG, "initExtra: " + is_offline);
            dataPenjagaLahan = realm.where(DataPenjagaLahan.class)
                    .equalTo("project_id", projectId)
                    .equalTo("userName", user.getUsername())
                    .findFirst();
            if (dataPenjagaLahan!=null) {
                if (dataPenjagaLahan.getIs_submited().contains("1")) {

                    btnSubmit_submit_person.setEnabled(false);
                    btnSubmit_submit_person.setVisibility(View.INVISIBLE);
                }
            }
//            countFoto();
        } catch (Exception e) {

        }
    }

    void takePhoto(String projectId) {

        Intent myIntent = new Intent(this, PersonPhotoActivity.class);
        myIntent.putExtra("projectId", projectId);
        myIntent.putExtra("is_offline", "0");
        startActivity(myIntent);
    }

    void setView(Project dataProject, String is_offline) {
        Log.d(TAG, "setView: " + dataProject.getProject_id());
        checkUser();
//        projectId = dataProject.getProject_id();
    }



    void thischeckInet() {
        if (ConnectivityReceiverService.isConnected(PersonActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit_submit_person.setBackgroundColor(Color.parseColor("#B71C1C"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_data_penjaga_lahan);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit_submit_person.setBackgroundColor(Color.parseColor("#80F44336"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_data_penjaga_lahan);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit_submit_person.setBackgroundColor(Color.parseColor("#80F44336"));
            TextView linearAct = (TextView) findViewById(R.id.linear_data_penjaga_lahan);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
        }
    }

    @OnClick(R.id.submit_person)
    void setSubmitDataPerson() {
        if (ConnectivityReceiverService.isConnected(PersonActivity.this)) {
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
//        realm = Realm.getDefaultInstance();
        int photoPerson = realm.where(PhotoPerson.class)
                .equalTo("projectid", dataProject.getProject_id())
                .equalTo("statusPhoto", "0")
                .equalTo("uploaded", false)
                .findAll().size();
        Log.d(TAG, "countFile: " + photoPerson);
        count_photo_person.setText(photoPerson + " Photo");
        photo = photoPerson;
//      .equalTo("uploaded", false)
    }

    void doSubmitDataperson(String isOnline) {
        if (nama_penjaga_lahan.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            nama_penjaga_lahan.setError("Field  Nama Penjaga Lahan tidak boleh null!");
            return;

        }
        if (alamat_penjaga_lahan.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            alamat_penjaga_lahan.setError("Field  ALamat tidak boleh null!");
            return;

        }
        if (provinsi_penjaga_lahan.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            provinsi_penjaga_lahan.setError("Field  Provinsi tidak boleh null!");
            return;

        }
        if (kabupaten_penjaga_lahan.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            kabupaten_penjaga_lahan.setError("Field  Kabupaten tidak boleh null!");
            return;

        }
        if (kecamatan_penjaga_lahan.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            kecamatan_penjaga_lahan.setError("Field  Kecamatan tidak boleh null!");
            return;

        }
        if (no_phone_penjaga_lahan.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            no_phone_penjaga_lahan.setError("Field  No Tlp tidak boleh null!");
            return;

        }

        if (no_ktp_penjaga_lahan.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            no_ktp_penjaga_lahan.setError("Field  No KTP tidak boleh null!");
            return;

        }
        if(photo==0){
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

        PersonPresenter = new PersonPresenter(this, this, dataProject.getProject_id());
        PersonPresenter.submitDataPerson(dataProject.getProject_id(), nama_penjaga_lahan.getText().toString(), alamat_penjaga_lahan.getText().toString(),provinsi_penjaga_lahan.getText().toString(),
                kabupaten_penjaga_lahan.getText().toString(),kecamatan_penjaga_lahan.getText().toString(),no_ktp_penjaga_lahan.getText().toString(), no_phone_penjaga_lahan.getText().toString(), isOnline);

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
                    btnSubmit_submit_person.setBackgroundColor(Color.parseColor("#80F44336"));
                    btnSubmit_submit_person.setEnabled(false);
                }
            }, true);
        }
    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }

    @OnClick(R.id.pic_person)
    void setPic() {
//        Utils.showToast(baseActivity, Id);
//        Intent personIntent = new Intent(this, PersonPhotoActivity.class);
//        personIntent.putExtra("projectId", Id);
//        personIntent.putExtra("is_offline", "0");
//        startActivity(personIntent);
    }


}
