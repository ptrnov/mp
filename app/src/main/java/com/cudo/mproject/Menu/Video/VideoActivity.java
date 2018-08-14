package com.cudo.mproject.Menu.Video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Photo.PhotoPresenter;
import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.WorkPkg;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.Utils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by adsxg on 2/13/2018.
 */

public class VideoActivity extends BaseActivity implements VideoInterface.View {
    String TAG = getClass().getSimpleName();
    AdapterVideoRealm adapterVideoRealm;
    @BindView(R.id.recviewVideo)
    RecyclerView recyclerView;


    Project project;
    MActivity activity;
    WorkPkg workPkg;
    //    OfflineDataTransaction dataOffline;
    String mCurrentPhotoPath;
    String status_photo = "";
    VideoPresenter presenter;

    String typeFile = "";//photo, video, audio, document
    static String extFile = "";

    @OnClick(R.id.backVd)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.add_video)
    void takePhoto() {
        initakeVid();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ButterKnife.bind(this);
//
        Intent intent = getIntent();
        String sProjectid = intent.getStringExtra("sProjectid");
        String swp_id = intent.getStringExtra("swp_id");
        String sactivity_id = intent.getStringExtra("sactivity_id");
        String isi_statusPhoto = intent.getStringExtra("status_photo");
        typeFile = intent.getStringExtra("typeFile");
        String isiExtFile = intent.getStringExtra("isiExtFile");
        String status_photo = "";
        String date_photo = "";
        String time_photo = "";
//        String idOffline = "";
        if (isi_statusPhoto.contentEquals("0")) {
            status_photo = "0";
            date_photo = "";
            time_photo = "";
//            idOffline = "";
        } else if (isi_statusPhoto.contentEquals("1")) {
            status_photo = "1";
            date_photo = intent.getStringExtra("date_photo");
            time_photo = intent.getStringExtra("time_photo");
//            idOffline = intent.getStringExtra("idOffline");
        } else {
            status_photo = "";
        }
        if (isiExtFile == "mp4") {
            extFile = "mp4";
        }
        if (isiExtFile == "3gp") {
            extFile = "3gp";
        }

        iniStringExtra(sProjectid, swp_id, sactivity_id, status_photo, date_photo, time_photo);

    }

    void iniStringExtra(String sProjectid, String swp_id, String sactivity_id, String status_photo, String date_photo, String time_photo) {
        try {
            //            String sproject = getIntent().getStringExtra(Project.PROJECT_ID);
//            project = realm.where(Project.class).equalTo(Project.PROJECT_ID, sproject).findFirst();
//            String swp_id = getIntent().getStringExtra(WorkPkg.WP_ID);
//            Log.d(TAG, "iniStringExtra swp_id: " + swp_id);
//            workPkg = realm.where(WorkPkg.class).equalTo(WorkPkg.WP_ID, swp_id).findFirst();
//            String sactivity_id = getIntent().getStringExtra(MActivity.activity_ID);
            activity = realm.where(MActivity.class).equalTo(MActivity.activity_ID, sactivity_id).findFirst();
            Log.d(TAG, "iniStringExtra sactivity_id: " + activity.getActivity_id());
            workPkg = realm.where(WorkPkg.class).equalTo(WorkPkg.WP_ID, swp_id).findFirst();
            Log.d(TAG, "iniStringExtra activity.getWp_id(): " + workPkg.getWp_id());
            project = realm.where(Project.class).equalTo(Project.PROJECT_ID, sProjectid).findFirst();
            Log.d(TAG, "iniStringExtra workPkg.getProject_id(): " + project.getProject_id());
            Log.d(TAG, "iniStringExtra status_photo: " + status_photo);
            Log.d(TAG, "iniStringExtra date_photo: " + date_photo);
            Log.d(TAG, "iniStringExtra time_photo: " + time_photo);
//            dataOffline = realm.where(OfflineDataTransaction.class)
//                    .equalTo(OfflineDataTransaction.idOffline, idOffline).findFirst();
////
            presenter = new VideoPresenter(this, this, extFile);
            Log.d(TAG, "iniStringExtra: " + getFolderPath());
            checkUser();

//            if(chekUserGroup(realm).matches("site_engineer")){
//                Toast.makeText(getApplicationContext(), "chekUserGroup(realm) := " + chekUserGroup(realm), Toast.LENGTH_SHORT).show();
            checkGPS();
//            }
            initView(sProjectid, swp_id, sactivity_id, status_photo, date_photo, time_photo);
        } catch (Exception e) {
            Utils.showToast(this, "Cant Create Video Please Try again");
            finish();
        }
    }
    void initView(String sProjectid, String swp_id, String sactivity_id, String status_photo, String date_photo, String time_photo) {

    }

    public void initakeVid() {
        if (checkPermission()) {
            Log.d(TAG, "initakePic: " + !gpsCache.isExpired());
            if (gpsCache.isValidGPS()) {
                try {
                    presenter.doTakeVideo(createVideoFile());
                } catch (Exception e) {
                    Utils.showToast(this, "FILE something wrong.. please try again..");
                    e.printStackTrace();
                }
            } else {
                try {
                    presenter.doTakeVideo(createVideoFile());
                } catch (Exception e) {
                    Utils.showToast(this, "FILE something wrong.. please try again..");
                    e.printStackTrace();
                }
                Utils.showToast(this, "GPS something wrong.. please try again..");
                onBackPressed();
                onBackPressed();
            }
        } else {
            Utils.showToast(this, "Ups something wrong.. please try again..");
            finish();
        }
    }

    private File createVideoFile() throws IOException {
        // Create an image file name

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = getPathPhoto();
        Log.d(TAG, "createImageFile: " + mCurrentPhotoPath);
        return new File(mCurrentPhotoPath);
    }

    @Override
    public void onFinishSubmit(boolean isFinish, String msg) {

    }

    @Override
    public void onFinishUpload(boolean isFinish, String msg) {

    }

    @Override
    public void onFInishUpdate(boolean isFinish, String msg) {

    }

    @Override
    public void onFInishVideo(boolean isFinish, String msg) {

    }

    String getFolderPath () {
        return FileUtils.rootpath(this, project.getProject_id());
    }

    String getPathPhoto () throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + project.getId() + "_" + Photo.getNextID(realm), "."+"mp4");//".jpg"
    }

    String getPathExt () throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + project.getId() + "_" + Photo.getNextID(realm), ".ext");
    }

    String getPathPhoto ( int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + id + "_" + Photo.getNextID(realm), "."+"mp4");//".jpg"
    }

    String getPathExt ( int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + id + "_" + Photo.getNextID(realm), ".ext");
    }
}
