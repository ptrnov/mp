package com.cudo.mproject.Menu.TaskHistory;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Menu.Photo.PhotoActivity;
import com.cudo.mproject.Menu.TaskActivity.TaskActivity;
import com.cudo.mproject.Menu.TaskActivity.TaskPresenter;
import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.Model.WorkPkg;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class TaskHistoryActivity extends BaseActivity implements TaskHistoryInterface.View {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.project_id_history)
    TextView project_id_history;
    @BindView(R.id.sitename_history)
    TextView sitename_history;
    @BindView(R.id.tenant_history)
    TextView tenant_history;
    @BindView(R.id.area_histoy)
    TextView area_histoy;
    @BindView(R.id.regional_history)
    TextView regional_history;
    @BindView(R.id.sow_history)
    TextView sow_history;
    @BindView(R.id.wp_name_history)
    TextView wp_name_history;
    @BindView(R.id.activity_name_history)
    TextView activity_name_history;
    //
    @BindView(R.id.plan_start_date_history)
    TextView plan_start_date_history;
    @BindView(R.id.plan_finish_date_history)
    TextView plan_finish_date_history;
    //
    @BindView(R.id.progress_history)
    TextView progress_history;
    @BindView(R.id.status_approval)
    TextView status_approval;
    LinearLayout approval_layout;
    //    @BindView(R.id.plan_date)
//    TextView plan_date;
//    @BindView(R.id.progress)
//    AppCompatSeekBar progress_sb;
//    @BindView(R.id.progress_tx)
//    TextView progress_tx;
    @BindView(R.id.img_history)
    View img_history;
    @BindView(R.id.count_photo_history)
    TextView count_photo_history;
//    @BindView(R.id.actualStartDateHiddenHistory)
//    TextView actualStartDateHiddenHistory;

    //
//    List<Photo> list;
//    static int count = 0;
    //
    String idOfflineTrans;
    String isiIdOfflineTrans=null;
    TaskHistoryPresenter taskHistoryPresenter;
    OfflineDataTransaction dataLocal;
    //    @BindView(R.id.submit_history)
//    Button btnSubmit;
    @BindView(R.id.submit_history)
    Button btnSubmitHistory;
    Realm realm;
    String versionApk = BuildConfig.VERSION_NAME;

    void initExtra() {
        realm = Realm.getDefaultInstance();
//        User user = realm.where(User.class).findFirst();
        try {

//            idOfflineTrans = Integer.parseInt(getIntent().getExtras().get(OfflineDataTransaction.idOffline).toString());
//            idOfflineTrans = getIntent().getStringExtra("id_offline_transaction");//OfflineDataTransaction.idOffline
//            idOfflineTrans = idOffline;
            idOfflineTrans = getIntent().getStringExtra("id_offline");//OfflineDataTransaction.idOffline
            Log.d(TAG, "onResponse: idOfflineTrans -> " + Integer.parseInt(idOfflineTrans));
//            Log.d(TAG, "initExtra: idOfflineTrans -> " + Integer.parseInt(idOfflineTrans));
//            realm = Realm.getDefaultInstance();
//            dataLocals = realm.where(OfflineDataTransaction.class).equalTo(OfflineDataTransaction.idOffline, idOfflineTrans).endGroup().findFirst();
//            dataLocals = realm.where(OfflineDataTransaction.class).equalTo(OfflineDataTransaction.idOffline, idOfflineTrans).findFirst();
            dataLocal = realm.where(OfflineDataTransaction.class)
                    .equalTo("id_offline_transaction", Integer.parseInt(idOfflineTrans))
                    .findFirst();
            Log.d(TAG, "initExtra: dataLocals:" + dataLocal);
            setView(dataLocal);
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
    }

    void setView(OfflineDataTransaction dataLocal) {//OfflineDataTransaction  dataLocals// String idOfflineTrans
        Log.d(TAG, "initExtra: setView");
        taskHistoryPresenter = new TaskHistoryPresenter(this, this, idOfflineTrans);
        checkUser();
        realm = Realm.getDefaultInstance();
//      dataLocals = realm.where(OfflineDataTransaction.class).equalTo("id_offline_transaction", idOfflineTrans).findFirst();
//      dataLocals = realm.where(OfflineDataTransaction.class).equalTo(OfflineDataTransaction.idOffline, idOfflineTrans).findFirst();
        Log.d(TAG, "setView: dataLocals.getId_offline_transaction()" + dataLocal.getId_offline_transaction());
        Log.d(TAG, "setView : datalocal project_id" + dataLocal.getProject_id());
        Log.d(TAG, "setView : datalocal wp_id" + dataLocal.getWp_id());
        Log.d(TAG, "setView : datalocal activity_id" + dataLocal.getActivity_id());

        MActivity mActivity = realm.where(MActivity.class).equalTo("activity_id", dataLocal.getActivity_id()).findFirst();
        WorkPkg wpg = realm.where(WorkPkg.class).equalTo("wp_id", dataLocal.getWp_id()).findFirst();
        Project mproject = realm.where(Project.class).equalTo("project_id", dataLocal.getProject_id()).findFirst();
//

//        actualStartDateHiddenHistory.setText(dataLocal.getActualStartDate() + dataLocal.getActualTimeStartDate());
//        OfflineDataTransaction dataLocalActualStartDate = realm.where(OfflineDataTransaction.class)
//                .equalTo("activity_id", dataLocal.getActivity_id())
//                .findFirst();
//        if(dataLocalActualStartDate.getActualStartDate()==""){
//            actualStartDateHiddenHistory.setText("-");
//        }else{
//            actualStartDateHiddenHistory.setText(dataLocal.getActualStartDate());
//        }
        if (dataLocal.getProject_id() == null) {
            project_id_history.setText(mproject.getProject_id());
        } else {
            project_id_history.setText(dataLocal.getProject_id());
        }
        if (dataLocal.getSite_name() == null) {
            sitename_history.setText(mproject.getSite_name());
        } else {
            sitename_history.setText(dataLocal.getSite_name());
        }
        if (dataLocal.getNamaTenant() == null) {
            tenant_history.setText(mproject.getNama_tenant());
        } else {
            tenant_history.setText(dataLocal.getNamaTenant());
        }
        if (dataLocal.getArea() == null) {
            area_histoy.setText(mproject.getArea());
        } else {
            area_histoy.setText(dataLocal.getArea());
        }
        if (dataLocal.getRegional() == null) {
            regional_history.setText(mproject.getRegional());
        } else {
            regional_history.setText(dataLocal.getRegional());
        }
        if (dataLocal.getSow() == null) {
            sow_history.setText(mproject.getSow());
        } else {
            sow_history.setText(dataLocal.getSow());
        }

        if (dataLocal.getActivity_name() == null) {
            activity_name_history.setText(mActivity.getActivity_name());
        } else {
            activity_name_history.setText(dataLocal.getActivity_name());
        }
        if (dataLocal.getActivity_plan_start() == null) {
            plan_start_date_history.setText(mActivity.getActivity_plan_start());
        } else {
            plan_start_date_history.setText(dataLocal.getActivity_plan_start());
        }
        if (dataLocal.getActivity_plan_finish() == null) {
            plan_finish_date_history.setText(mActivity.getActivity_plan_finish());
        } else {
            plan_finish_date_history.setText(dataLocal.getActivity_plan_finish());
        }

        if (dataLocal.getWp_name() == null) {
            wp_name_history.setText(wpg.getWp_name());
        } else {
            wp_name_history.setText(dataLocal.getWp_name());
        }


        progress_history.setText(String.valueOf(dataLocal.getProgrees()) + "%");

        if (!ConnectivityReceiverService.isConnected(TaskHistoryActivity.this)) {
            showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, true, "");
//            Button btnSubmitHistory = (Button) findViewById(R.id.submit_history);
            btnSubmitHistory.setEnabled(false);
            btnSubmitHistory.setVisibility(View.INVISIBLE);
//
            TextView linearLayout = (TextView) findViewById(R.id.linear_detail_history);
            linearLayout.setTextColor(Color.parseColor("#80F44336"));
            return;
        } else {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);
                if (reachable == true) {
//                    btnSubmitHistory.setEnabled(true);
//                    btnSubmitHistory.setVisibility(View.VISIBLE);

                    if (dataLocal.getStatus_project_offline().contains("1")) {

                        btnSubmitHistory.setEnabled(false);
                        btnSubmitHistory.setVisibility(View.INVISIBLE);

                    } else {
                        btnSubmitHistory.setEnabled(true);
                        btnSubmitHistory.setVisibility(View.VISIBLE);
                    }
//
                    TextView linearLayout = (TextView) findViewById(R.id.linear_detail_history);
                    linearLayout.setTextColor(Color.parseColor("#B71C1C"));
                } else {
                    showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, true, "");
                    btnSubmitHistory.setEnabled(false);
                    btnSubmitHistory.setVisibility(View.INVISIBLE);
//
//            TextView projectDetail = (TextView) findViewById(R.id.project_detail_history);
//            projectDetail.setBackgroundColor(Color.parseColor("#80F44336"));
//
                    TextView linearLayout = (TextView) findViewById(R.id.linear_detail_history);
                    linearLayout.setTextColor(Color.parseColor("#80F44336"));
                    return;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }

        if (dataLocal.getStatus_project_offline().contains("1")) {

            btnSubmitHistory.setEnabled(false);
            btnSubmitHistory.setVisibility(View.INVISIBLE);
            approval_layout.setVisibility(LinearLayout.VISIBLE);
            status_approval.setText("Request Approval");
        } else {
            btnSubmitHistory.setEnabled(true);
            btnSubmitHistory.setVisibility(View.VISIBLE);
            approval_layout.setVisibility(LinearLayout.GONE);
//            approval_layout.setVisibility(LinearLayout.INVISIBLE);
            status_approval.setEnabled(false);
        }

//        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.add);
//        fab2.setEnabled(false);
//        fab2.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
//        isiIdOfflineTrans = intent.getStringExtra("id_offline");
//        Log.d(TAG, "onResponse: isiIdOfflineTrans:" + isiIdOfflineTrans);
//        isiIdOfflineTrans = Integer.parseInt(idOfflineTrans);
        setContentView(R.layout.task_history_layout);
        ButterKnife.bind(this);
        approval_layout = (LinearLayout) this.findViewById(R.id.approval_layout);
        initExtra();

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

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
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
        } else {
            showAlertDialog("UPLOAD SUKSES", "", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setResult(RESULT_OK);
                    //finish();
                    dialog.dismiss();
                }
            }, true);
        }

    }

    @Override
    public void onErrorSubmit(boolean isSucces, String msg) {


    }


    @OnClick(R.id.pic_history)
    void setPic() {
        realm = Realm.getDefaultInstance();
//        idOfflineTrans;
//        idOfflineTrans = getIntent().getStringExtra(OfflineDataTransaction.idOffline);

        dataLocal = realm.where(OfflineDataTransaction.class)
                .equalTo("id_offline_transaction", Integer.parseInt(idOfflineTrans)).findFirst();
        Log.d(TAG, "initExtra: dataLocals:" + dataLocal);

        Intent myIntent = new Intent(this, PhotoActivity.class);
        myIntent.putExtra("sProjectid", dataLocal.getProject_id());
        myIntent.putExtra("swp_id", dataLocal.getWp_id());
        myIntent.putExtra("sactivity_id", dataLocal.getActivity_id());
        myIntent.putExtra("tanggal", dataLocal.getDate_offline());
        myIntent.putExtra("status_photo", "1");
        myIntent.putExtra("date_photo", dataLocal.getDate_offline());
        myIntent.putExtra("time_photo", dataLocal.getTime_offline());
        myIntent.putExtra("idOffline", idOfflineTrans);
        startActivity(myIntent);
    }

    //
    @OnClick(R.id.submit_history)
    void setSubmit() {
        doSubmit();

    }

    void doSubmit() {
//        taskHistoryPresenter = new TaskHistoryPresenter(this, this, dataLocals.getProject_id());
        taskHistoryPresenter = new TaskHistoryPresenter(this, this, String.valueOf(idOfflineTrans));
//        if (dataLocals != null && mProject.getPa_id() != null && !mProject.getPa_id().equals("")) {
        dataLocal = realm.where(OfflineDataTransaction.class)
                .equalTo("id_offline_transaction", Integer.parseInt(idOfflineTrans)).findFirst();
        Log.d(TAG, "doSubmit: dataLocals view:" + dataLocal);
        Project dataProject = realm.where(Project.class)
                .equalTo("project_id", dataLocal.getProject_id())
                .findFirst();
        if (dataProject != null && dataProject.getPa_id() != null && !dataProject.getPa_id().equals("")) {
            Log.d(TAG, "doSubmit:  dataLocals.getProject_id():" + dataLocal.getProject_id());
            Log.d(TAG, "doSubmit:  dataLocals.getWp_id():" + dataLocal.getWp_id());
            Log.d(TAG, "doSubmit:  dataLocals.getActivity_id():" + dataLocal.getActivity_id());
            Log.d(TAG, "doSubmit:  dataLocals.getDate_offline():" + dataLocal.getDate_offline());
            Log.d(TAG, "doSubmit:  dataLocals.getTime_offline():" + dataLocal.getTime_offline());
            WorkPkg dataWorkPkg = realm.where(WorkPkg.class)
                    .equalTo("wp_id", dataLocal.getWp_id())
                    .findFirst();
            MActivity dataActivity = realm.where(MActivity.class)
                    .equalTo("activity_id", dataLocal.getActivity_id())
                    .findFirst();
//
            Log.d(TAG, "doSubmit:  dataLocals.getPa_id():" + dataLocal.getPa_id());
            Log.d(TAG, "doSubmit:  dataProject.getProject_id():" + dataProject.getProject_id());
            Log.d(TAG, "doSubmit:  dataWorkPkg.getWp_id(): " + dataWorkPkg.getWp_id());
            Log.d(TAG, "doSubmit:  dataActivity.getActivity_id():" + dataActivity.getActivity_id());
            taskHistoryPresenter.uploadPhoto(dataProject.getPa_id(), String.valueOf(dataLocal.getId_offline_transaction()), true);
        } else {
            Log.d(TAG, "doSubmit:  dataLocals.getProject_id():" + dataLocal.getProject_id());
//             dataProject = realm.where(Project.class)
//                    .equalTo("project_id", dataLocals.getProject_id())
//                    .findFirst();
            WorkPkg dataWorkPkg = realm.where(WorkPkg.class)
                    .equalTo("wp_id", dataLocal.getWp_id())
                    .findFirst();
            MActivity dataActivity = realm.where(MActivity.class)
                    .equalTo("activity_id", dataLocal.getActivity_id())
                    .findFirst();
            Log.d(TAG, "doSubmit:  dataActivity.getDpt_id() +" +
                    " dataProject.getProject_id() + " +
                    "dataWorkPkg.getWp_id() +" +
                    "dataActivity.getActivity_id()" +
                    "dataLocals.getProgrees()"
                    + ":" + dataActivity.getDpt_id()
                    + ":" + dataProject.getProject_id()
                    + ":" + dataWorkPkg.getWp_id()
                    + ":" + dataActivity.getActivity_id()
                    + ":" + dataLocal.getProgrees());
//        taskHistoryPresenter.uploadPhoto(dataProject.getPa_id(), dataLocals.getId_offline_transaction(),true);
            taskHistoryPresenter.doSubmit(dataActivity.getDpt_id(), dataProject.getProject_id(), dataWorkPkg.getWp_id(), dataActivity.getActivity_id(), dataLocal.getDate_offline(), dataLocal.getTime_offline(), String.valueOf(dataLocal.getId_offline_transaction()), String.valueOf(dataActivity.getProgress_activity()));
//      taskHistoryPresenter.updateDataLocals(dataActivity.getDpt_id(), dataProject.getProject_id(), dataWorkPkg.getWp_id(), dataActivity.getActivity_id(), dataLocals.getProgrees(), true);
        }
//        Button btnSubmitHistory = (Button) findViewById(R.id.submit_history);
        btnSubmitHistory.setEnabled(false);
        btnSubmitHistory.setBackgroundColor(Color.parseColor("#80F44336"));
    }

    void thischeckInet() {
        if (!ConnectivityReceiverService.isConnected(TaskHistoryActivity.this)) {
//            showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, ", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }, true, "");
//            Button btnSubmitHistory = (Button) findViewById(R.id.submit_history);
            btnSubmitHistory.setEnabled(false);
            btnSubmitHistory.setVisibility(View.INVISIBLE);
//
//            TextView projectDetail = (TextView) findViewById(R.id.project_detail_history);
//            projectDetail.setBackgroundColor(Color.parseColor("#80F44336"));
//
            TextView linearLayout = (TextView) findViewById(R.id.linear_detail_history);
            linearLayout.setTextColor(Color.parseColor("#80F44336"));
            return;
        } else {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 3 180.250.19.206");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                Log.d(TAG, "isi reachable" + reachable);

                if (reachable == true) {
//                    btnSubmitHistory.setEnabled(true);
//                    btnSubmitHistory.setVisibility(View.VISIBLE);
                    if (dataLocal.getStatus_project_offline().equals("1")) {
                        btnSubmitHistory.setEnabled(false);
                        btnSubmitHistory.setVisibility(View.INVISIBLE);
                        approval_layout.setVisibility(LinearLayout.VISIBLE);
                        status_approval.setText("Request Approval");
                    } else {
                        btnSubmitHistory.setEnabled(true);
                        btnSubmitHistory.setVisibility(View.VISIBLE);
                        approval_layout.setVisibility(LinearLayout.GONE);
                        approval_layout.setVisibility(LinearLayout.INVISIBLE);
                        status_approval.setEnabled(false);
                    }

                    TextView linearLayout = (TextView) findViewById(R.id.linear_detail_history);
                    linearLayout.setTextColor(Color.parseColor("#D52B38"));
                } else {
                    showAlertDialog("OFFLINE MODE", "Device Anda tidak terkoneksi Internet, ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, true, "");
//                 Button btnSubmitHistory = (Button) findViewById(R.id.submit_history);
                    btnSubmitHistory.setEnabled(false);
                    btnSubmitHistory.setVisibility(View.INVISIBLE);
                    TextView linearLayout = (TextView) findViewById(R.id.linear_detail_history);
                    linearLayout.setTextColor(Color.parseColor("#80F44336"));
                    return;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    void countFoto() {
        realm = Realm.getDefaultInstance();
//        idOfflineTrans = getIntent().getStringExtra("id_offline_transaction");
        dataLocal = realm.where(OfflineDataTransaction.class)
                .equalTo("id_offline_transaction", Integer.parseInt(idOfflineTrans)).findFirst();
        Log.d(TAG, "initExtra: dataLocals:" + dataLocal);
        int photo = 0;
        if (dataLocal.getStatus_project_offline().contentEquals("0")) {
            Log.d(TAG, "initExtra: dataLocals dataLocal.getStatus_project_offline()==> 0");
            photo = realm.where(Photo.class)
                    .equalTo(Photo.PROJECT_ID, dataLocal.getProject_id())
                    .equalTo("wp_id", dataLocal.getWp_id())
                    .equalTo("status_photo", "1")
                    .equalTo("activity_id", dataLocal.getActivity_id())
                    .equalTo("date_photo", dataLocal.getDate_offline())
                    .equalTo("time_photo", dataLocal.getTime_offline())
                    .equalTo("time_photo", dataLocal.getTime_offline())
//                    .equalTo(Photo.UPLOADED, true)
//                    .or()
                    .equalTo(Photo.UPLOADED, false)
                    .findAll().size();

        } else {
            Log.d(TAG, "initExtra: dataLocals dataLocal.getStatus_project_offline()==> 1");
            photo = realm.where(Photo.class)
                    .equalTo(Photo.PROJECT_ID, dataLocal.getProject_id())
                    .equalTo("wp_id", dataLocal.getWp_id())
                    .equalTo("status_photo", "1")
                    .equalTo("activity_id", dataLocal.getActivity_id())
                    .equalTo("date_photo", dataLocal.getDate_offline())
                    .equalTo("time_photo", dataLocal.getTime_offline())
//                    .equalTo("progress_photo", Integer.parseInt(dataLocal.getProgrees()))
                    .equalTo(Photo.UPLOADED, true)
//                    .or()
//                    .equalTo(Photo.UPLOADED, false)
                    .findAll().size();
        }
        Log.d(TAG, "initExtra: count_history_Foto : " + photo);
        count_photo_history.setText(photo + " Files");
    }
}
