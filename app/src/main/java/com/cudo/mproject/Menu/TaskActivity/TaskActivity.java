package com.cudo.mproject.Menu.TaskActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Menu.Audio.AudioActivity;
import com.cudo.mproject.Menu.Login.LoginActivity;
import com.cudo.mproject.Menu.Photo.PhotoActivity;
import com.cudo.mproject.Menu.PhotoDocument.PhotoDocumentActivity;
import com.cudo.mproject.Menu.Video.VideoActivity;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.Response.ResponseLogin;
import com.cudo.mproject.Model.WorkPkg;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;
import com.cudo.mproject.Utils.ActivityUtils;
import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static java.lang.System.currentTimeMillis;

/**
 * date          editing by        method                            Description
 * 02/02/2018     newbiecihuy
 * 02/02/2018     newbiecihuy
 * 03/03/2018     newbiecihuy
 * 25/04/2018     newbiecihuy
 */
//public class TaskActivity extends BaseActivity implements TaskActivityInterface.View, View.OnClickListener {
public class TaskActivity extends BaseActivity implements TaskActivityInterface.View, PopupMenu.OnMenuItemClickListener {
    public static TaskActivity taskActivity;
    String TAG = getClass().getSimpleName();
    TaskActivityInterface.View view;
    //    @BindView(R.id.project_id)
//    TextView project_id;
    @BindView(R.id.sitename)
    TextView sitename;
    @BindView(R.id.tenant)
    TextView tenant;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.regional)
    TextView regional;
    @BindView(R.id.sow)
    TextView sow;
    @BindView(R.id.work_package)
    Spinner spinner_work_package;
    @BindView(R.id.activity)
    Spinner spinner_activity;
    @BindView(R.id.progress)
    AppCompatSeekBar progress_sb;
    @BindView(R.id.progress_tx)
    TextView progress_tx;
    @BindView(R.id.img)
    View img;
    @BindView(R.id.count_photo)
    TextView count_photo;
    //
//    String sProjectid;
    String swp_id;
    String sactivity_id;
    TaskPresenter presenter;
    Project mProject;
    MActivity mActivity;
    //
    @BindView(R.id.plan_start_date)
    TextView plan_start_date;
    @BindView(R.id.plan_finish_date)
    TextView plan_finish_date;
    //
//    @BindView(R.id.actualStartDateHidden)
//    TextView actualStartDateHidden;
//    @BindView(R.id.actualStartDate)
//    TextView actualStartDate;
    //

    @BindView(R.id.submit)
    Button btnSubmit;
    private Realm realm;
    //    static Activity activity;
    public BaseActivity baseActivity;
    public GpsCache gpsCache;
    //
//    DatePicker datePicker;
    LinearLayout actualStartDate_layout;
    private Calendar calendar;
    private int year, month, day;
    static String isitglActual = "";
    //    @BindView(R.id.btn_date)
    Button displayDate;
    String projectId=null;
    //
    Context context;
    String plan_start = "";
    String plan_finish = "";
    WorkPkg countryWp = null;
    MActivity country = null;
    //
    static final int CAPTURE_VIDEO = 1;
    //    SimpleDateFormat timeF = new SimpleDateFormat(" hh:mm:ss");
    SimpleDateFormat timeF = new SimpleDateFormat(" HH:mm:ss");
    //    Date now = new Date();
    Calendar calender = Calendar.getInstance();
    String time_now;

    public TaskActivity() {
        time_now = timeF.format(calender.getTime());
    }

    public void button() {
        Log.d(TAG, "insert buttonSet() disabled");
        btnSubmit.setEnabled(true);
        btnSubmit.setClickable(true);
        img.setEnabled(false);
    }

    void buttonSet(boolean i) {
        Log.d(TAG, "insert buttonSet()");
        /* sourece check internet connection
         * https://stackoverflow.com/questions/15714122/checking-internet-connection-in-every-activity
         * datepicker control
         * https://developer.android.com/reference/android/widget/DatePicker.html#DatePicker(android.content.Context
         * https://www.tutorialspoint.com/android/android_datepicker_control
         * https://www.javatpoint.com/android-datepicker-example
         *
         */
        if (i == true) {
            btnSubmit.setClickable(true);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (progress_tx.getText().toString().isEmpty()) {
//                        view.onErrorSubmit(false, "Nilai progress Tidak boleh <=0");
                        progress_tx.setError("Nilai progress Tidak boleh <=0 !");
                        return;
                    }
                    if (ConnectivityReceiverService.isConnected(TaskActivity.this)) {
                        try {
                            Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                            int returnVal = p1.waitFor();
                            boolean reachable = (returnVal == 0);
                            Log.d(TAG, "isi reachable" + reachable);
                            if (reachable == true) {

                                doSubmit();


                            } else {
                                showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, data Akan disimpan di local storage..", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        offlineSubmit();
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
                                offlineSubmit();
                                dialog.dismiss();
                            }
                        }, true, "");
                    }
                }
            });
        }

    }


    void doSubmit() {
        if (progress_tx.getText().toString().isEmpty()) {
//            view.onErrorSubmit(false, "Nilai progress Tidak boleh <=0");
            progress_tx.setError("Nilai progress Tidak boleh <=0 !");
//            Toast.makeText(baseActivity.getBaseContext(),
//                    "Nilai progress Tidak boleh <=0",
//                    Toast.LENGTH_SHORT).show();
            return;
        }
        String inputProgress_tx = progress_tx.getText().toString();

        if (mProject != null && mProject.getPa_id() != null && !mProject.getPa_id().equals("")) {
            Log.d(TAG, "setSubmit: RESUMMMMEE pa id" + mProject.getPa_id());
            country = (MActivity) spinner_activity.getSelectedItem();
            Log.d(TAG, "doSubmit: from BEGININGGGGG if" + country.getDpt_id());
            Log.d(TAG, "doSubmit:  country.getActivity_id()" + country.getActivity_id());
            countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
            Log.d(TAG, "doSubmit: countryWp.getWp_id()" + countryWp.getWp_id());
            List<WorkPkg> workPkgs = getRealm().where(WorkPkg.class)
                    .equalTo(WorkPkg.WP_ID, countryWp.getWp_id())
                    .findAll();
            Log.d(TAG, "doSubmit: workPkgs" + workPkgs);
            presenter = new TaskPresenter(this, this, projectId, countryWp.getWp_id(), country.getActivity_id(), String.valueOf(progress_sb.getProgress()));
            presenter.uploadPhoto(mProject.getPa_id(), projectId, countryWp.getWp_id(), country.getActivity_id(), String.valueOf(progress_sb.getProgress()), true, country.getDpt_id());
//
        } else {

            country = (MActivity) spinner_activity.getSelectedItem();
            Log.d(TAG, "doSubmit: from BEGININGGGGG" + country.getDpt_id());
            Log.d(TAG, "doSubmit:  country.getActivity_id()" + country.getActivity_id());

            countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
            Log.d(TAG, "doSubmit: countryWp.getWp_id()" + countryWp.getWp_id());
            List<WorkPkg> workPkgs = getRealm().where(WorkPkg.class)
                    .equalTo(WorkPkg.WP_ID, countryWp.getWp_id())
                    .findAll();
            Log.d(TAG, "doSubmit: workPkgs" + workPkgs);

            presenter = new TaskPresenter(this, this, projectId, countryWp.getWp_id(), country.getActivity_id(), String.valueOf(progress_sb.getProgress()));
//            presenter.doSubmit(country.getDpt_id(), sProjectid, String.valueOf(progress_sb.getProgress()), countryWp.getWp_id(), country.getActivity_id(), inputProgress_tx, isitglActual);
            presenter.doSubmit(country.getDpt_id(), projectId, String.valueOf(progress_sb.getProgress()), countryWp.getWp_id(), country.getActivity_id(), inputProgress_tx);

        }
//
        btnSubmit.setEnabled(false);
        btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
//
    }

    void offlineSubmit() {
//
        String inputProgress_tx = progress_tx.getText().toString();
        if (progress_tx.getText().toString().isEmpty()) {
//                        view.onErrorSubmit(false, "Nilai progress Tidak boleh <=0");
            progress_tx.setError("Nilai progress Tidak boleh <=0 !");
            return;
        }
        if (mProject != null && mProject.getPa_id() != null && !mProject.getPa_id().equals("")) {
            Log.d(TAG, "offlineSubmit: RESUMMMMEE pa id" + mProject.getPa_id());
            country = (MActivity) spinner_activity.getSelectedItem();
            Log.d(TAG, "offlineSubmit: from BEGININGGGGG" + country.getDpt_id());
            Log.d(TAG, "offlineSubmit:  country.getActivity_id()" + country.getActivity_id());

            countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
            Log.d(TAG, "offlineSubmit: countryWp.getWp_id()" + countryWp.getWp_id());
            List<WorkPkg> workPkgs = getRealm().where(WorkPkg.class)
                    .equalTo(WorkPkg.WP_ID, countryWp.getWp_id())
                    .findAll();
            Log.d(TAG, "offlineSubmit: workPkgs" + workPkgs);
            presenter = new TaskPresenter(this, this, projectId, countryWp.getWp_id(), country.getActivity_id(), String.valueOf(progress_sb.getProgress()));
            presenter.uploadPhoto(mProject.getPa_id(), projectId, countryWp.getWp_id(), country.getActivity_id(), String.valueOf(progress_sb.getProgress()), false, country.getDpt_id());
//
        } else {
//            MActivity country = (MActivity) spinner_activity.getSelectedItem();
            country = (MActivity) spinner_activity.getSelectedItem();
            Log.d(TAG, "offlineSubmit: from BEGININGGGGG" + country.getDpt_id());
//            String inputProgress_tx = progress_tx.getText().toString();
            Log.d(TAG, "offlineSubmit:  country.getActivity_id()" + country.getActivity_id());
//
            countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
            Log.d(TAG, "offlineSubmit: countryWp.getWp_id()" + countryWp.getWp_id());
            List<WorkPkg> workPkgs = getRealm().where(WorkPkg.class)
                    .equalTo(WorkPkg.WP_ID, countryWp.getWp_id())
                    .findAll();
            Log.d(TAG, "offlineSubmit: workPkgs" + workPkgs);
            presenter = new TaskPresenter(this, this, projectId, countryWp.getWp_id(), country.getActivity_id(), String.valueOf(progress_sb.getProgress()));
//            presenter.offlineSubmit(country.getDpt_id(), sProjectid, String.valueOf(progress_sb.getProgress()), countryWp.getWp_id(), country.getActivity_id(), inputProgress_tx, false, isitglActual);
            presenter.offlineSubmit(country.getDpt_id(), projectId, String.valueOf(progress_sb.getProgress()), countryWp.getWp_id(), country.getActivity_id(), inputProgress_tx, false, country.getDpt_id());
        }
//        btnSubmit.setEnabled(false);
//        btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
//
    }

//    @OnClick(R.id.back)
//    void setBack() {
//        onBackPressed();
//    }

    @OnClick(R.id.pic)
    void setPic() {
//        countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
//        country = (MActivity) spinner_activity.getSelectedItem();
//
//       //gpsCache = getRealm().where(GpsCache.class).findFirst();
//        //Utils.showToast(this, "Nilai countryWp Dan  country " + spinner_work_package.getSelectedItem() +  spinner_activity.getSelectedItem());

//        if (countryWp != null && country != null) {
//
//            Intent myIntent = new Intent(this, PhotoActivity.class);
//            myIntent.putExtra("sProjectid", countryWp.getProjectid());
//            myIntent.putExtra("swp_id", countryWp.getWp_id());
//            myIntent.putExtra("sactivity_id", country.getActivity_id());
//            myIntent.putExtra("status_photo", "0");
////          myIntent.putExtra("idOffline", "0");
////          myIntent.putExtra("date_photo", "0");
////          myIntent.putExtra("time_photo", "0");
//            startActivity(myIntent);
//        } else {
//            Utils.showToast(this, "Nilai Work Package Dan  Activity == null");
//            return;
//        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_layout);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
         projectId = intent.getStringExtra("projectId");
//       datePicker = findViewById(R.id.actualStartDate);
//        actualStartDate_layout = (LinearLayout) this.findViewById(R.id.actualStartDate_layout);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//        }
//        displayDate = (Button) findViewById(R.id.btn_date);
//        displayDate.setOnClickListener(this);
        initExtra();
        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(TaskActivity.this, view);
                popupMenu.setOnMenuItemClickListener(TaskActivity.this);
                popupMenu.inflate(R.menu.create_document_menu);
                popupMenu.show();
            }
        });
    }

    //datePicker
//    @Override
//    public void onClick(View v) {
//        if (v == displayDate) {
////            Get Current Date
//            calendar = Calendar.getInstance();
////            calendar.setMinDate(System.currentTimeMillis() - 1000);
//            year = calendar.get(Calendar.YEAR);
//            month = calendar.get(Calendar.MONTH);
//            day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                    new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
////                          actualStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_now);
//                            actualStartDate.setText("-");
//                            isitglActual= year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_now;
//                            actualStartDateHidden.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_now);
//
//                        }
//                    }, year, month, day);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//            datePickerDialog.show();
//        }
//    }


//    @Override
//    protected void onStart()
//    {
//        if (ConnectivityReceiverService.isConnected(TaskActivity.this)) {
////            Button btnSubmit = (Button) findViewById(R.id.submit);
//            try {
//                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
//                int returnVal = p1.waitFor();
//                boolean reachable = (returnVal == 0);
//                Log.d(TAG, "isi reachable" + reachable);
//                if (reachable == true) {
//                    btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));
////
//                    LinearLayout linearAct = (LinearLayout) findViewById(R.id.linear_activity_progress);
//                    linearAct.setBackgroundColor(Color.parseColor("#B71C1C"));
////
//                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.topPanel);
//                    linearLayout.setBackgroundColor(Color.parseColor("#B71C1C"));
//
//                } else {
//
//                    btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
//
//                    LinearLayout linearAct = (LinearLayout) findViewById(R.id.linear_activity_progress);
//                    linearAct.setBackgroundColor(Color.parseColor("#80F44336"));
////
//                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.topPanel);
//                    linearLayout.setBackgroundColor(Color.parseColor("#80F44336"));
//                }
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
//            btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
//
//            LinearLayout linearAct = (LinearLayout) findViewById(R.id.linear_activity_progress);
//            linearAct.setBackgroundColor(Color.parseColor("#80F44336"));
////
//            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.topPanel);
//            linearLayout.setBackgroundColor(Color.parseColor("#80F44336"));
//        }
//    }

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

    void initExtra() {
        try {
//            sProjectid = getIntent().getStringExtra(Project.PROJECT_ID);
            Log.d(TAG, "initExtra: " + projectId);
//            mProject = getRealm().where(Project.class).beginGroup().equalTo(Project.PROJECT_ID, sProjectid).endGroup().findFirst();
            mProject = getRealm().where(Project.class).beginGroup().equalTo(Project.PROJECT_ID, projectId).endGroup().findFirst();
            setView(mProject);
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog("Warning", "ups.. ada yang salah coba lah beberapa saat lagi..", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }, false);
        }
        if (ConnectivityReceiverService.isConnected(TaskActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));
//
                    TextView linearAct = (TextView) findViewById(R.id.linear_activity_progress);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));
//
                    TextView linearLayout = (TextView) findViewById(R.id.topPanel);
                    linearLayout.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));

                    TextView linearAct = (TextView) findViewById(R.id.linear_activity_progress);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
//
                    TextView linearLayout = (TextView) findViewById(R.id.topPanel);
                    linearLayout.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));

            TextView linearAct = (TextView) findViewById(R.id.linear_activity_progress);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
//
            TextView linearLayout = (TextView) findViewById(R.id.topPanel);
            linearLayout.setTextColor(Color.parseColor("#80F44336"));
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    void setView(Project m) {
        Log.d(TAG, "setView: " + m.getProject_id());
        presenter = new TaskPresenter(this, this, projectId, swp_id, sactivity_id, String.valueOf(progress_sb.getProgress()));
        checkUser();
        baseshowProgress(true);
        presenter.getWorkPackage(m.getProject_id(), user.getUser_id());
//        project_id.setText(m.getProject_id());
        sitename.setText(m.getSite_name());
        tenant.setText(m.getNama_tenant());
        area.setText(m.getArea());
        regional.setText(m.getRegional());
        sow.setText(m.getSow());

        if (ConnectivityReceiverService.isConnected(TaskActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    if (progress_sb.getProgress() <= 0) {
                        btnSubmit.setEnabled(true);// btnSubmit.setEnabled(false);
                        btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
                    } else {
                        btnSubmit.setEnabled(true);
                        btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));
                    }
//                btnSubmit.setEnabled(true);
//                btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));
                } else {
                    if (progress_sb.getProgress() <= 0) {
                        btnSubmit.setEnabled(true);// btnSubmit.setEnabled(false);
                        btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
                    } else {
                        btnSubmit.setEnabled(true);
                        btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
//            }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }


//      progress_tx.setText(mProject.getProgress() + "%");
        progress_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
                progress_tx.setText(progress + "%");
                if (ConnectivityReceiverService.isConnected(TaskActivity.this)) {
                    try {
                        Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                        int returnVal = p1.waitFor();
                        boolean reachable = (returnVal == 0);
                        Log.d(TAG, "isi reachable" + reachable);
                        if (reachable == true) {

                            if (progress <= 0) {
                                btnSubmit.setEnabled(false);
                                btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
                            } else {
                                btnSubmit.setEnabled(true);
                                btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));
                            }
                        } else {
                            if (progress_sb.getProgress() <= 0) {
                                btnSubmit.setEnabled(false);
                                btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
                            } else {
                                btnSubmit.setEnabled(true);
                                btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() >= mProject.getProgress()) {
                    mProject.updateProgress(getRealm(), seekBar.getProgress(), projectId);//sProjectid
//                    MActivity.updateProgress(getRealm(), seekBar.getProgress(), sactivity_id);
                } else {
//                if (seekBar.getProgress() < mProject.getProgress()) {
//                    Toast.makeText(getApplicationContext(), "Progress poroject := " + seekBar.getProgress() + "<" + mProject.getProgress(), Toast.LENGTH_SHORT).show();

                }
            }

        });
    }

    void initSpinnerWpkg() {
        try {
            List<WorkPkg> workPkgs = getRealm().where(WorkPkg.class).equalTo(WorkPkg.PROJECTID, projectId).findAll();//sProjectid
            ArrayAdapter<WorkPkg> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, workPkgs);
            spinner_work_package.setAdapter(adapter);
            spinner_activity.setSelection(adapter.getPosition(mProject.getWorkPkg()), false);
            spinner_work_package.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    WorkPkg country = (WorkPkg) parent.getSelectedItem();
                    baseshowProgress(true);
                    presenter.getActivity(user.getUser_id(), country.getWbs1_id(), country.getWbs_id(), country.getWp_id(), projectId);//sProjectid
                    //update mproject workpackage
                    mProject.updateWorkPkg(getRealm(), country);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            sactivity_id = getIntent().getStringExtra(MActivity.activity_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //spinner_work_package.setSelection(adapter.getPosition(myItem));//Optional to set the selected item.
    }

    void initSpinnerMact(String wpid) {
        boolean index = false;
        try {
            List<MActivity> workPkgs = getRealm().where(MActivity.class)
                    .equalTo(MActivity.WPID, wpid)
                    .findAll();
            ArrayAdapter<MActivity> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, workPkgs);

            if (adapter != null) {
                index = true;
                buttonSet(index);
            }
            spinner_activity.setAdapter(adapter);
            spinner_activity.setSelection(adapter.getPosition(mProject.getMActivity()), false);
            spinner_activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    MActivity country = (MActivity) parent.getSelectedItem();
                    Log.d(TAG, "onItemSelected: " + country.getDpt_id() + "||" + country.getActivity_name());
//                    update activity
                    mProject.updateActivity(getRealm(), country);
                    sactivity_id = country.getActivity_id();
                    //
                    if (((MActivity) parent.getSelectedItem()).isValid()) {

                        if (country.getActivity_plan_start() == null) {
                            plan_start = "-";
                            plan_start_date.setText(plan_start);
                        } else {
                            plan_start = country.getActivity_plan_start();
                            plan_start_date.setText(plan_start);
                        }
                        if (country.getActivity_plan_finish() == null) {
                            plan_finish = "-";
                            plan_finish_date.setText(plan_finish);
                        } else {
                            plan_finish = country.getActivity_plan_finish();
                            plan_finish_date.setText(plan_finish);
                        }
                        OfflineDataTransaction dataLocal = realm.where(OfflineDataTransaction.class)
                                .equalTo("activity_id", sactivity_id)
                                .findFirst();
//                        DatePicker dp2 = findViewById(R.id.actualStartDate);
//                        if (dataLocal != null) {
//
//                            actualStartDateHidden.setText(dataLocal.getActualStartDate());
//                            displayDate.setVisibility(View.GONE);
//                            actualStartDate_layout.setVisibility(LinearLayout.GONE);
//                            actualStartDate_layout.setVisibility(LinearLayout.INVISIBLE);
//
//                        } else {
//                            actualStartDate.setText("-");
//                            actualStartDateHidden.setText("-");
//                            displayDate.setVisibility(View.VISIBLE);
//                            actualStartDate_layout.setVisibility(LinearLayout.VISIBLE);
//                            actualStartDate_layout.setVisibility(LinearLayout.VISIBLE);
//                        }

                    }
                    progress_sb.setProgress(country.getProgress_activity());
                    progress_tx.setText(country.getProgress_activity() + "%");
//                    progress_sb.setProgress(country.getProgress_project());
//                    if (country.getProgress_project() <= 0) {
//                        progress_tx.setText(country.getProgress_project() + "%");
//                        Button btnSubmit = (Button) findViewById(R.id.submit);
//                        btnSubmit.setEnabled(false);
//                        btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
//                    } else {
//                        Button btnSubmit = (Button) findViewById(R.id.submit);
//                        btnSubmit.setEnabled(true);
//                        btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));
//                    }
//                    progress_tx.setText(country.getProgress_project() + "%");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        //spinner_work_package.setSelection(adapter.getPosition(myItem));//Optional to set the selected item.
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

//            MActivity.updateProgress(realm, progress_sb.getProgress(), sactivity_id);
        } else {
            showAlertDialog("UPLOAD SUKSES", "", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setResult(RESULT_OK);
                    //finish();
                    dialog.dismiss();
                    btnSubmit.setEnabled(false);
                }
            }, true);
            mProject.updateProgress(realm, progress_sb.getProgress(), projectId);//sProjectid
            MActivity.updateProgress(realm, progress_sb.getProgress(), sactivity_id);
//
            btnSubmit.setEnabled(true);
            btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
        }
//        btnSubmit.setEnabled(false);
//        btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));
    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {
        if (!isSucces) {
            showAlertDialog("ATTENTION", msg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    btnSubmit.setEnabled(true);
                }
            }, true);


//            btnSubmit.setEnabled(true);
//            btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));
        }

//        btnSubmit.setEnabled(true);
//        btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));

    }

    @Override
    public void onSuccessWorkpkg(boolean isSucces, String msg, List<WorkPkg> workPkgs) {
        baseshowProgress(false);
        if (isSucces)
            updateWpkgData(workPkgs);
        initSpinnerWpkg();
    }

    @Override
    public void onSuccessActivity(boolean isSucces, String msg, List<MActivity> activities) {
        baseshowProgress(false);
        if (isSucces)
            updateMActivity(activities);
        initSpinnerMact(msg);
    }

    @Override
    public void onError(String error) {
        Log.d(TaskActivity.class.getSimpleName(), "onError: " + error);
//        showProgress(false);
        showAlertDialog(getString(R.string.warning), error, null, true);
    }

    void updateWpkgData(final List<WorkPkg> workPkgs) {
        WorkPkg.updateWpkgData(getRealm(), workPkgs);
    }

    void updateMActivity(final List<MActivity> mActivities) {
        MActivity.updateActivityData(getRealm(), mActivities);
    }

    void thischeckInet() {
        if (ConnectivityReceiverService.isConnected(TaskActivity.this)) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
                    btnSubmit.setBackgroundColor(Color.parseColor("#B71C1C"));
//
                    TextView linearAct = (TextView) findViewById(R.id.linear_activity_progress);
                    linearAct.setTextColor(Color.parseColor("#D52B38"));
//
                    TextView linearLayout = (TextView) findViewById(R.id.topPanel);
                    linearLayout.setTextColor(Color.parseColor("#D52B38"));

                } else {

                    btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));

                    TextView linearAct = (TextView) findViewById(R.id.linear_activity_progress);
                    linearAct.setTextColor(Color.parseColor("#80F44336"));
//
                    TextView linearLayout = (TextView) findViewById(R.id.topPanel);
                    linearLayout.setTextColor(Color.parseColor("#80F44336"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            btnSubmit.setBackgroundColor(Color.parseColor("#80F44336"));

            TextView linearAct = (TextView) findViewById(R.id.linear_activity_progress);
            linearAct.setTextColor(Color.parseColor("#80F44336"));
//
            TextView linearLayout = (TextView) findViewById(R.id.topPanel);
            linearLayout.setTextColor(Color.parseColor("#80F44336"));
        }
    }

    void countFoto() {

        int photo = getRealm().where(Photo.class)
                .equalTo(Photo.PROJECT_ID, projectId)//sProjectid
                .equalTo("status_photo", "0")
                .equalTo(Photo.UPLOADED, false)
                .findAll().size();

        Log.d(TAG, "countFile: " + photo);
        count_photo.setText(photo + " Files");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
//                Toast.makeText(this, "action_photo Clicked", Toast.LENGTH_SHORT).show();
                takePhoto();
                return true;
            case R.id.action_video:
//                Toast.makeText(this, "On Progress", Toast.LENGTH_SHORT).show();
                takeVideo();
                return true;
            case R.id.action_audio:
//                Toast.makeText(this, "On Progress", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "action_audio Clicked", Toast.LENGTH_SHORT).show();
                takeAudio();
                return true;
            case R.id.action_document:
//                Toast.makeText(this, "On Progress", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "action_document Clicked", Toast.LENGTH_SHORT).show();
                takeDocument();
                return true;
            default:
                return false;
        }
    }

    void takePhoto() {
        countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
        country = (MActivity) spinner_activity.getSelectedItem();
//        gpsCache = getRealm().where(GpsCache.class).findFirst();
//        Utils.showToast(this, "Nilai countryWp Dan  country " + spinner_work_package.getSelectedItem() +  spinner_activity.getSelectedItem());
        if (countryWp != null && country != null) {

            Intent myIntent = new Intent(this, PhotoActivity.class);
            myIntent.putExtra("sProjectid", countryWp.getProjectid());
            myIntent.putExtra("swp_id", countryWp.getWp_id());
            myIntent.putExtra("sactivity_id", country.getActivity_id());
            myIntent.putExtra("status_photo", "0");
            myIntent.putExtra("idOffline", "");
            myIntent.putExtra("typeFile", "Photo");
//          myIntent.putExtra("isiExtFile", "jpg");
//          myIntent.putExtra("idOffline", "0");
//          myIntent.putExtra("date_photo", "0");
//          myIntent.putExtra("time_photo", "0");
            startActivity(myIntent);
        } else {
            Utils.showToast(this, "Nilai Work Package Dan  Activity == null");
            return;
        }

    }

    void takeVideo() {
//        Toast.makeText(this, "action_video Clicked", Toast.LENGTH_SHORT).show();
        countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
        country = (MActivity) spinner_activity.getSelectedItem();
////        gpsCache = getRealm().where(GpsCache.class).findFirst();
////        Utils.showToast(this, "Nilai countryWp Dan  country " + spinner_work_package.getSelectedItem() +  spinner_activity.getSelectedItem());
        if (countryWp != null && country != null) {
//            Intent myIntent = new Intent(this, VideoActivity.class);
//            myIntent.putExtra("sProjectid", countryWp.getProjectid());
//            myIntent.putExtra("swp_id", countryWp.getWp_id());
//            myIntent.putExtra("sactivity_id", country.getActivity_id());
//            myIntent.putExtra("status_photo", "0");
//            myIntent.putExtra("typeFile", "video");
//            myIntent.putExtra("extFile", "");
//            startActivity(myIntent);
//         / myIntent.putExtra("idOffline", "0");
//         / myIntent.putExtra("date_photo", "0");
//        /  myIntent.putExtra("time_photo", "0");
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(videoViewLoad);
//            videoViewLoad.setMediaController(mediaController);
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, CAPTURE_VIDEO);
            }
            int requestCode = 0;
            int resultCode = 0;

            Intent myIntent = null;
            if (requestCode == CAPTURE_VIDEO && resultCode == Activity.RESULT_OK) {
                Uri videoUri = myIntent.getData();

                //Create folder in Gallery to store your Captured Images/Videos
                String appDirectoryName = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator;
                final File imageRoot = new File(Environment.getExternalStorageDirectory(), appDirectoryName);
                if (!imageRoot.exists()) {
                    imageRoot.mkdir();
                }

                String dirName = Environment.getExternalStorageDirectory().getPath() + appDirectoryName;
                Intent intent = myIntent.putExtra(dirName, videoUri);
//              Timestamp timestamp = new Timestamp(currentTimeMillis());
                File file = new File(dirName, "VID" + time_now.toString() + ".mp4");
                try {
                    AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(intent.getData(), "r");
                    FileInputStream fis = videoAsset.createInputStream();
                    FileOutputStream fOut = null;

                    fOut = new FileOutputStream(file);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = fis.read(buf)) > 0) {
                        fOut.write(buf, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();
                values.put("_data", file.getAbsolutePath());
                ContentResolver cr = getContentResolver();
                cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

                // displays the captured Video on the app screen
//                videoViewLoad.setVideoURI(videoUri);
//                videoViewLoad.requestFocus();
//                videoViewLoad.start();
            }

        } else {
            Utils.showToast(this, "Nilai Work Package Dan  Activity == null");
            return;
        }

    }

    void takeAudio() {
//        Toast.makeText(this, "action_audio Clicked", Toast.LENGTH_SHORT).show();
        countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
        country = (MActivity) spinner_activity.getSelectedItem();
//        gpsCache = getRealm().where(GpsCache.class).findFirst();
//        Utils.showToast(this, "Nilai countryWp Dan  country " + spinner_work_package.getSelectedItem() +  spinner_activity.getSelectedItem());
        if (countryWp != null && country != null) {

            Intent myIntent = new Intent(this, AudioActivity.class);
            myIntent.putExtra("sProjectid", countryWp.getProjectid());
            myIntent.putExtra("swp_id", countryWp.getWp_id());
            myIntent.putExtra("sactivity_id", country.getActivity_id());
            myIntent.putExtra("status_photo", "0");
            myIntent.putExtra("typeFile", "audio");
            myIntent.putExtra("extFile", "");
//          myIntent.putExtra("idOffline", "0");
//          myIntent.putExtra("date_photo", "0");
//          myIntent.putExtra("time_photo", "0");
            startActivity(myIntent);
        } else {
            Utils.showToast(this, "Nilai Work Package Dan  Activity == null");
            return;
        }
    }

    void takeDocument() {
        countryWp = (WorkPkg) spinner_work_package.getSelectedItem();
        country = (MActivity) spinner_activity.getSelectedItem();
//        gpsCache = getRealm().where(GpsCache.class).findFirst();
//        Utils.showToast(this, "Nilai countryWp Dan  country " + spinner_work_package.getSelectedItem() +  spinner_activity.getSelectedItem());
        if (countryWp != null && country != null) {

            Intent myIntent = new Intent(this, PhotoDocumentActivity.class);
            myIntent.putExtra("sProjectid", countryWp.getProjectid());
            myIntent.putExtra("swp_id", countryWp.getWp_id());
            myIntent.putExtra("sactivity_id", country.getActivity_id());
            myIntent.putExtra("status_photo", "0");
            myIntent.putExtra("idOffline", "");
            myIntent.putExtra("typeFile", "Photo Doc");
//          myIntent.putExtra("isiExtFile", "jpg");
//          myIntent.putExtra("idOffline", "0");
//          myIntent.putExtra("date_photo", "0");
//          myIntent.putExtra("time_photo", "0");
            startActivity(myIntent);
        } else {
            Utils.showToast(this, "Nilai Work Package Dan  Activity == null");
            return;
        }

    }
}
