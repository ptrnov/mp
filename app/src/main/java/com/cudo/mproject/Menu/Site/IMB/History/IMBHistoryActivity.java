package com.cudo.mproject.Menu.Site.IMB.History;

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
import com.cudo.mproject.Menu.Site.IMB.IMBPhotoActivity;
import com.cudo.mproject.Menu.Site.Person.History.PersonHistoryActivity;
import com.cudo.mproject.Model.DataIMB;
import com.cudo.mproject.Model.PhotoIMB;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class IMBHistoryActivity extends BaseActivity implements IMBHistoryInterface.View {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.nama_imb_history)
    TextView nama_imb_history;
    @BindView(R.id.no_imb_history)
    TextView no_imb_history;
    @BindView(R.id.alamat_imb_history)
    TextView alamat_imb_history;
    @BindView(R.id.submit_imb_history)
    Button btnSubmit_submit_imb_history;
    @BindView(R.id.count_photo_imb_history)
    TextView count_photo_imb_history;
    @BindView(R.id.date_before_history)
    TextView date_before_history;
    @BindView(R.id.date_after_history)
    TextView date_after_history;

    String Id = null;
    Realm realm;
    //    Project dataProject;
    DataIMB dataIMB;
    int photo = 0;

    @BindView(R.id.img_imb_history)
    View img_imb_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_imb_history);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        String Id = intent.getStringExtra("projectId");
        String is_offline = "1";//intent.getStringExtra("is_offline");
        initExtra(Id, is_offline);

        findViewById(R.id.img_imb_history).setOnClickListener(new View.OnClickListener() {
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
            dataIMB = realm.where(DataIMB.class)
                    .equalTo("project_id", projectId)
                    .equalTo("userName", user.getUsername())
                    .findFirst();

            Log.d(TAG, "initExtra: dataIMB:" + dataIMB);
            Id = dataIMB.getProject_id();
            setView(dataIMB, is_offline);
//
            Log.d(TAG, "initExtra: " + dataIMB.getProject_id());
            Log.d(TAG, "initExtra: " + is_offline);
        } catch (Exception e) {

        }
        if(dataIMB !=null){
            if (dataIMB.getIs_submited().contains("1")) {

                btnSubmit_submit_imb_history.setEnabled(false);
                btnSubmit_submit_imb_history.setVisibility(View.INVISIBLE);
            } else {

                btnSubmit_submit_imb_history.setEnabled(true);
                btnSubmit_submit_imb_history.setVisibility(View.VISIBLE);
//                btnSubmit_submit_imb_history.setVisibility(LinearLayout.GONE);
            }
        } else {
            btnSubmit_submit_imb_history.setEnabled(false);
            btnSubmit_submit_imb_history.setVisibility(View.INVISIBLE);
        }
    }

    void takePhoto(String projectId) {

        Intent myIntent = new Intent(this, IMBPhotoActivity.class);
        myIntent.putExtra("projectId", projectId);
        myIntent.putExtra("is_offline", "0");
        myIntent.putExtra("dateSubmit", dataIMB.getDateSubmit());
        myIntent.putExtra("timeSubmit", dataIMB.getTimeSubmit());
        startActivity(myIntent);
    }

    void setView(DataIMB dataIMB, String is_offline) {
        Log.d(TAG, "setView: " + dataIMB.getProject_id());
//        projectId = dataProject.getProject_id();
        checkUser();
        if (dataIMB.getNama_imb() != null) {
            nama_imb_history.setText(dataIMB.getNama_imb());
        }
        if (dataIMB.getNo_imb() != null) {
            no_imb_history.setText(dataIMB.getNo_imb());
        }
        if (dataIMB.getAlamat_imb() != null) {
            alamat_imb_history.setText(dataIMB.getAlamat_imb());
        }
        if (dataIMB.getTglMasaBerlakuAwal() != null) {
            date_before_history.setText(dataIMB.getTglMasaBerlakuAwal());
        }
        if (dataIMB.getTglMasaBerlakuAkhir() != null) {
            date_after_history.setText(dataIMB.getTglMasaBerlakuAkhir());
        }


    }

    void thischeckInet() {
        if (ConnectivityReceiverService.isConnected(IMBHistoryActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit_submit_imb_history.setBackgroundColor(Color.parseColor("#B71C1C"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_imb_history);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit_submit_imb_history.setBackgroundColor(Color.parseColor("#80F44336"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_imb_history);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit_submit_imb_history.setBackgroundColor(Color.parseColor("#80F44336"));
            TextView linearAct = (TextView) findViewById(R.id.linear_imb_history);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
        }
    }

    @OnClick(R.id.submit_imb_history)
    void setSubmitDataIMB() {
        if (ConnectivityReceiverService.isConnected(IMBHistoryActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    doSubmitDataIMB("1");
                } else {
                    showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, data Akan disimpan di local storage..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                                offlineSubmit();
                            doSubmitDataIMB("2");
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
                    doSubmitDataIMB("2");
                    dialog.dismiss();
                }
            }, true, "");
        }
    }
    void countFoto() {
        int photoIMB = 0;
        if (dataIMB.getIs_submited() == "0") {
            photoIMB = getRealm().where(PhotoIMB.class)
                    .equalTo(PhotoIMB.PROJECT_ID, dataIMB.getProject_id())
                    .equalTo("status_photo", "1")
                    .equalTo("uploaded", false)
                    .findAll().size();

        } else {


            photoIMB = getRealm().where(PhotoIMB.class)
                    .equalTo(PhotoIMB.PROJECT_ID, dataIMB.getProject_id())
                    .equalTo("status_photo", "1")
                    .equalTo("uploaded", true)
                    .findAll().size();
//        Log.d(TAG, "countFile: Id" + Id);
//        Log.d(TAG, "countFile: Id" + dataProject.getProject_id());
        }
        Log.d(TAG, "countFile: " + photoIMB);
        count_photo_imb_history.setText(photoIMB + " Files");
        photo = photoIMB;
    }
    void doSubmitDataIMB(String isOnline) {

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
                    btnSubmit_submit_imb_history.setBackgroundColor(Color.parseColor("#80F44336"));
                    btnSubmit_submit_imb_history.setEnabled(false);
                }
            }, true);
        }
    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }
    @OnClick(R.id.pic_imb_history)
    void setPic() {
//        Utils.showToast(baseActivity, Id);
//        Intent personIntent = new Intent(this, PersonPhotoActivity.class);
//        personIntent.putExtra("projectId", Id);
//        personIntent.putExtra("is_offline", "0");
//        startActivity(personIntent);
    }
}
