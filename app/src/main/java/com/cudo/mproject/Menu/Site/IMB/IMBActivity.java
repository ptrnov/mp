package com.cudo.mproject.Menu.Site.IMB;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Dialog.DialogPickerDateAfter;
import com.cudo.mproject.Dialog.DialogPickerDateBefore;
import com.cudo.mproject.Model.DataIMB;
import com.cudo.mproject.Model.PhotoIMB;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;
import com.cudo.mproject.Utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;


/**
 * date           create/editing by            method                                                   Description
 * 17/05/2018         newbiecihuy            - create static EditText pickerDateBefore
 * - create static EditText pickerDateAfter
 * - create static Date isiDateBefore
 * - create static Date isiDateAfter
 * <p>
 * 17/05/2018         newbiecihuy            - create static Method setDateBeforespinnerDialog
 * - create static Method setDateAfterspinnerDialog
 **/
public class IMBActivity extends BaseActivity implements IMBInterface.View {
    public static IMBActivity imbActivity;
    String TAG = getClass().getSimpleName();

    @BindView(R.id.nama_imb)
    TextView nama_imb;
    @BindView(R.id.no_imb)
    TextView no_imb;
    @BindView(R.id.alamat_imb)
    TextView alamat_imb;
    @BindView(R.id.submit_imb)
    Button btnSubmit_submit_imb;
    @BindView(R.id.count_photo_imb)
    TextView count_photo_imb;
    //
//    DatePicker pickerDateBefore;
//    DatePicker pickerDateAfter;
    private static EditText pickerDateBefore;
    private static Date isiDateBefore = null;
    private static EditText pickerDateAfter;
    private static Date isiDateAfter = null;
    //
//    private Calendar calendar;
//    private int year, month, dayOfMonth;
//    static String isiPickerDateAfter = "";
//    static String isiPickerDateBefore = "";
    //
//    BaseActivity baseActivity;
    DataIMB dataIMB;
    IMBPresenter imbPresenter;
    String Id = null;
    int photo = 0;
    String projectId = null;
    Realm realm;
    Project dataProject;
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // source Datepicker
    /*
     *  https://android--examples.blogspot.co.id/2015/05/how-to-use-datepicker-in-android.html
     *  https://www.tutlane.com/tutorial/android/android-datepicker-with-examples
     *  http://abhiandroid.com/ui/datepicker
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_imb);
        ButterKnife.bind(this);
//        pickerDateBefore = (DatePicker) findViewById(R.id.date_before);
//        pickerDateAfter = (DatePicker) findViewById(R.id.date_after);
        pickerDateBefore = findViewById(R.id.date_before);
        pickerDateAfter = findViewById(R.id.date_after);

        datePicker();
//
        Intent intent = getIntent();

        String Id = intent.getStringExtra("projectId");
        String is_offline = "0";// intent.getStringExtra("is_offline");
        initExtra(Id, is_offline);
        findViewById(R.id.img_imb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(Id);
            }
        });
    }

    private void datePicker() {

        pickerDateBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dateBeforespinnerDialog();

            }
        });


        pickerDateAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dateAfterspinnerDialog();

            }
        });

    }

    public void dateBeforespinnerDialog() {
        new DialogPickerDateBefore(this).show();
    }

    public static void setDateBeforespinnerDialog(String dateBeforepinnerDialog) {
        pickerDateBefore.setText(dateBeforepinnerDialog.toString());
        try {
            isiDateBefore = sdf.parse(dateBeforepinnerDialog);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void dateAfterspinnerDialog() {
        new DialogPickerDateAfter(this).show();
    }

    public static void setDateAfterspinnerDialog(String dateAfterpinnerDialog) {
        pickerDateAfter.setText(dateAfterpinnerDialog.toString());
        try {
            isiDateAfter = sdf.parse(dateAfterpinnerDialog);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        User user = realm.where(User.class).findFirst();
        Id = getIntent().getStringExtra(Project.PROJECT_ID);
        dataProject = realm.where(Project.class)
                .equalTo(Project.PROJECT_ID, projectId)
                .findFirst();

        dataIMB = realm.where(DataIMB.class)
                .equalTo("project_id", dataProject.getProject_id())
                .equalTo("userName", user.getUsername())
                .findFirst();

        Log.d(TAG, "initExtra: dataProject:" + dataProject);
        setView(dataProject, is_offline);
        Log.d(TAG, "initExtra: " + dataProject.getProject_id());
        Log.d(TAG, "initExtra: " + is_offline);

        if(dataIMB !=null){
            if (dataIMB.getIs_submited().contains("1")) {

                btnSubmit_submit_imb.setEnabled(false);
                btnSubmit_submit_imb.setVisibility(View.INVISIBLE);
            }
        }

    }

    void takePhoto(String projectId) {

        Intent myIntent = new Intent(this, IMBPhotoActivity.class);
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
        if (ConnectivityReceiverService.isConnected(IMBActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit_submit_imb.setBackgroundColor(Color.parseColor("#B71C1C"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_imb);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit_submit_imb.setBackgroundColor(Color.parseColor("#80F44336"));
                    TextView linearAct = (TextView) findViewById(R.id.linear_imb);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit_submit_imb.setBackgroundColor(Color.parseColor("#80F44336"));
            TextView linearAct = (TextView) findViewById(R.id.linear_imb);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
        }
    }

    @OnClick(R.id.submit_imb)
    void setSubmitIMB() {
        if (ConnectivityReceiverService.isConnected(IMBActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    doSubmitIMB("1");
                } else {
                    showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, data Akan disimpan di local storage..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                                offlineSubmit();
                            doSubmitIMB("2");
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
                    doSubmitIMB("2");
                    dialog.dismiss();
                }
            }, true, "");
        }

    }

    void countFoto() {
        realm = Realm.getDefaultInstance();
        int photoImb = realm.where(PhotoIMB.class)
                .equalTo(PhotoIMB.PROJECT_ID, dataProject.getProject_id())
                .equalTo("status_photo", "0")
                .equalTo("uploaded", false)
                .findAll().size();

        Log.d(TAG, "countFile: " + photoImb);
        count_photo_imb.setText(photoImb + " Photo");
        photo = photoImb;
    }

    void doSubmitIMB(String isOnline) {

        if (nama_imb.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            nama_imb.setError("Field NAMA PEMILIK IMB tidak boleh null!");
            return;

        }
        if (no_imb.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            no_imb.setError("Field No IMB tidak boleh null!");
            return;

        }
        if (alamat_imb.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            alamat_imb.setError("Field ALAMAT IMB tidak boleh null!");
            return;

        }
        if (pickerDateBefore.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            pickerDateBefore.setError("Field TANGGAL AWAL tidak boleh null!");
            return;
        }
        if (pickerDateAfter.getText().toString().length() == 0) {
//            Toast.makeText(this, "You did not enter a site name", Toast.LENGTH_SHORT).show();
            pickerDateAfter.setError("Field TANGGAL AKHIR tidak boleh null!");

            return;
        }
        if (isiDateAfter.before(isiDateBefore)) {
            pickerDateBefore.setError("lebih besar");
            pickerDateAfter.setError("lebih kecil");
            return;
        }
        if (isiDateBefore.equals(isiDateAfter)) {
            pickerDateBefore.setError("--");
            pickerDateAfter.setError("--");
            return;
        }
        if (photo == 0) {
            Utils.showToast(this, "Data Evidence Belum di Isi");
//            count_photo_imb.setError("Data Evidence Belum di Isi");
            showAlertDialog("ATTENTION", "Data Evidence Belum di Isi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, true);
            return;
        }
        imbPresenter = new IMBPresenter(this, this, dataProject.getProject_id());
        imbPresenter.submitDataIMB(dataProject.getProject_id(), nama_imb.getText().toString(), no_imb.getText().toString(), alamat_imb.getText().toString(), pickerDateBefore.getText().toString(), pickerDateAfter.getText().toString(), isOnline);
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
                    btnSubmit_submit_imb.setBackgroundColor(Color.parseColor("#80F44336"));
                    btnSubmit_submit_imb.setEnabled(false);
                }
            }, true);

        }
    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {

    }

    @OnClick(R.id.pic_imb)
    void setPic() {

//        Intent imbIntent = new Intent(this, IMBPhotoActivity.class);
//        startActivity(imbIntent);
    }


}
