package com.cudo.mproject.Menu.Photo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BaseApp;
import com.cudo.mproject.Dialog.ShowPhotoDialog;
import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.WorkPkg;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.cudo.mproject.Utils.FileUtils.rootpath;


/**
 * Created by Oeganz on 11/8/2017.
 */
/**
 *   date          editing by        method                            Description
 * 02/02/2018     newbiecihuy
 * 02/02/2018     newbiecihuy
 * 03/03/2018     newbiecihuy
 * 25/04/2018     newbiecihuy
 *
 */
public class PhotoActivity extends BaseActivity implements PhotoInterface.View {
    String TAG = getClass().getSimpleName();
    AdapterPhotoRealm adapter;
    @BindView(R.id.recview)
    RecyclerView recyclerView;

    @OnClick(R.id.back)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.add)
    void takePhoto() {
        initakePic();
    }


    Project project;
    MActivity activity;
    WorkPkg workPkg;
    OfflineDataTransaction dataOffline;
    String mCurrentPhotoPath;
    String status_photo = "";
    PhotoPresenter presenter;
    String typeFile = "";//photo, video, audio, document
    static String extFile = "";
    /*
     *
        image -> jpeg, png
        video -> mp4, 3gp
        audio -> mp3, amr, acc
        doc -> pdf, doc
    * */


    @BindView(R.id.add_photo)
    TextView add_photo;

    //
    String getFolderPath() {
        return rootpath(this, project.getProject_id());
    }

    String getPathPhoto() throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + project.getId() + "_" + Photo.getNextID(realm), ".jpg");
    }

    String getPathExt() throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + project.getId() + "_" + Photo.getNextID(realm), ".ext");
    }

    String getPathPhoto(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + id + "_" + Photo.getNextID(realm), ".jpg");
    }

    String getPathExt(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + id + "_" + Photo.getNextID(realm), ".ext");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
//
        ButterKnife.bind(this);
//
        Intent intent = getIntent();
        String sProjectid = intent.getStringExtra("sProjectid");
        String swp_id = intent.getStringExtra("swp_id");
        String sactivity_id = intent.getStringExtra("sactivity_id");
        String isi_statusPhoto = intent.getStringExtra("status_photo");
        String status_photo = "";
        String date_photo = "";
        String time_photo = "";
        String idOffline = "";
        typeFile = intent.getStringExtra("typeFile");
//
        if (!intent.getStringExtra("idOffline").isEmpty()) {
            idOffline = intent.getStringExtra("idOffline");
        }

        if (idOffline == "") {
            status_photo = "0";
            date_photo = "";
            time_photo = "";
//          idOffline = "0";
            Log.d(TAG, "id_offline_transaction 0: " + idOffline);
//            return;
            iniStringExtra(sProjectid, swp_id, sactivity_id, status_photo, date_photo, time_photo, null);

        } else {
            Log.d(TAG, "id_offline_transaction: " + idOffline);
            status_photo = "1";
            date_photo = intent.getStringExtra("date_photo");
            time_photo = intent.getStringExtra("time_photo");
            iniStringExtra(sProjectid, swp_id, sactivity_id, status_photo, date_photo, time_photo, idOffline);
        }

    }

    //    void iniStringExtra(final String sProjectid,final String swp_id,final String sactivity_id,final String status_photo,final String date_photo,final String time_photo) {
    void iniStringExtra(String sProjectid, String swp_id, String sactivity_id, String status_photo, String date_photo, String time_photo, String idOffline) {
//        try {
//            String sproject = getIntent().getStringExtra(Project.PROJECT_ID);
//            project = realm.where(Project.class).equalTo(Project.PROJECT_ID, sproject).findFirst();
//            String swp_id = getIntent().getStringExtra(WorkPkg.WP_ID);
//            Log.d(TAG, "iniStringExtra swp_id: " + swp_id);
//            workPkg = realm.where(WorkPkg.class).equalTo(WorkPkg.WP_ID, swp_id).findFirst();
//            String sactivity_id = getIntent().getStringExtra(MActivity.activity_ID);
        realm = Realm.getDefaultInstance();
        if (idOffline == null) {
            activity = realm.where(MActivity.class).equalTo(MActivity.activity_ID, sactivity_id).findFirst();
            Log.d(TAG, "iniStringExtra sactivity_id: " + activity.getActivity_id());
            workPkg = realm.where(WorkPkg.class).equalTo(WorkPkg.WP_ID, swp_id).findFirst();
            Log.d(TAG, "iniStringExtra activity.getWp_id(): " + workPkg.getWp_id());
            project = realm.where(Project.class).equalTo(Project.PROJECT_ID, sProjectid).findFirst();
            Log.d(TAG, "iniStringExtra workPkg.getProject_id(): " + project.getProject_id());
            Log.d(TAG, "iniStringExtra status_photo: " + status_photo);
            Log.d(TAG, "iniStringExtra date_photo: " + date_photo);
            Log.d(TAG, "iniStringExtra time_photo: " + time_photo);
//
            presenter = new PhotoPresenter(this, this);
            Log.d(TAG, "iniStringExtra: " + getFolderPath());
            //presenter.checkFiletoDB(project.getProject_id(),getFolderPath(),realm);
            checkUser();
            checkGPS();
            initView(sProjectid, swp_id, sactivity_id, status_photo, date_photo, time_photo);
        } else {
            Log.d(TAG, "id_offline_transaction: " + idOffline);
            int isiIdOffline = Integer.parseInt(idOffline);
            Log.d(TAG, "isiIdOffline: " + isiIdOffline);
//
            dataOffline = realm.where(OfflineDataTransaction.class)
                    .equalTo("id_offline_transaction", isiIdOffline).findFirst();
            Log.d(TAG, "isiIdOffline: " + dataOffline);
//          return;
//
//            activity = realm.where(MActivity.class).equalTo("activity_id", dataOffline.getActivity_id()).findFirst();
            String projectId, wpId, activityId = null;
            if (dataOffline.getActivity_id() != null) {//activity
//                Log.d(TAG, "iniStringExtra sactivity_id: " + activity.getActivity_id());
//                workPkg = realm.where(WorkPkg.class).equalTo("wp_id", dataOffline.getWp_id()).findFirst();
//                Log.d(TAG, "iniStringExtra activity.getWp_id(): " + workPkg.getWp_id());
//                project = realm.where(Project.class).equalTo("project_id", dataOffline.getProject_id()).findFirst();
//                Log.d(TAG, "iniStringExtra workPkg.getProject_id(): " + project.getProject_id());
//                Log.d(TAG, "iniStringExtra status_photo: " + status_photo);
//                Log.d(TAG, "iniStringExtra date_photo: " + date_photo);
//                Log.d(TAG, "iniStringExtra time_photo: " + time_photo);
//                presenter = new PhotoPresenter(this, this);
//                Log.d(TAG, "iniStringExtra: " + getFolderPath());

                if (dataOffline.getProject_id() != null) {
                    projectId = dataOffline.getProject_id();
                } else {
                    projectId = sProjectid;
                }
                if (dataOffline.getWp_id() != null) {
                    wpId = dataOffline.getWp_id();
                } else {
                    wpId = swp_id;
                }
                if (dataOffline.getActivity_id() != null) {
                    activityId = dataOffline.getActivity_id();
                } else {
                    activityId = sactivity_id;
                }
                checkUser();
//              checkGPS();
//              initView(dataOffline.getProject_id(), dataOffline.getWp_id(), dataOffline.getActivity_id(), status_photo, date_photo, time_photo);
                initView(projectId, wpId, activityId, status_photo, date_photo, time_photo);
            } else {
                Utils.showToast(this, "Cant Create Photo Please Try again");
                finish();
            }
        }
//        } catch (Exception e) {
//            Utils.showToast(this, "Cant Create Photo Please Try again");
//            finish();
//        }
    }

    void initView(String sProjectid, String swp_id, String sactivity_id, String status_photo, String date_photo, String time_photo) {

        if (status_photo.contentEquals("1")) {
            FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
            add.setVisibility(RelativeLayout.GONE);
            add.setVisibility(RelativeLayout.INVISIBLE);
            add_photo.setText(typeFile);
//
            Photo dataPhoto = realm.where(Photo.class)
                    .equalTo(Photo.PROJECT_ID, sProjectid)
                    .equalTo("wp_id", swp_id)
                    .equalTo("activity_id", sactivity_id)
                    .equalTo("status_photo", "1")
                    .equalTo("date_photo", date_photo)
                    .equalTo("time_photo", time_photo)
                    .findFirst();
//
//            if (dataPhoto.isUploaded() == true) {
            if (dataPhoto.isUploaded()) {
                adapter = new AdapterPhotoRealm(realm.where(Photo.class)
                        .equalTo(Photo.PROJECT_ID, sProjectid)
                        .equalTo("wp_id", swp_id)
                        .equalTo("activity_id", sactivity_id)
                        .equalTo("status_photo", "1")
                        .equalTo("date_photo", date_photo)
                        .equalTo("time_photo", time_photo)
                        .equalTo("uploaded", true)
                        .findAll());
            } else {
                adapter = new AdapterPhotoRealm(realm.where(Photo.class)
                        .equalTo(Photo.PROJECT_ID, sProjectid)
                        .equalTo("wp_id", swp_id)
                        .equalTo("activity_id", sactivity_id)
                        .equalTo("status_photo", "1")
                        .equalTo("date_photo", date_photo)
                        .equalTo("time_photo", time_photo)
                        .equalTo("progress_photo", dataPhoto.getProgress_photo())
                        .equalTo(Photo.UPLOADED, false)
                        .findAll());
                Log.d(TAG, "adapter upload flase, status_photo 1 " + adapter);
            }

        } else if (status_photo.contentEquals("0")) {
            adapter = new AdapterPhotoRealm(realm.where(Photo.class)
                    .equalTo(Photo.PROJECT_ID, sProjectid)
                    .equalTo("wp_id", swp_id)
                    .equalTo("status_photo", "0")
//                    .equalTo(Photo.UPLOADED, false)
                    .equalTo("activity_id", sactivity_id)
                    .findAll());
        }
//
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }


    public void initakePic() {
        if (checkPermission()) {
            Log.d(TAG, "initakePic: " + !gpsCache.isExpired());
           if (gpsCache.isValidGPS()) {
                try {
                    presenter.doTakePic(createImageFile());
                } catch (Exception e) {
                    Utils.showToast(this, "FILE something wrong.. please try again..");
                    e.printStackTrace();
                }
            } else {
                try {
                    presenter.doTakePic(createImageFile());
                } catch (Exception e) {
                    Utils.showToast(this, "FILE something wrong.. please try again..");
                    e.printStackTrace();
                }
                Utils.showToast(this, "GPS something wrong.. please try again..");
                onBackPressed();
                onBackPressed();
                return;
            }
        } else {
            Utils.showToast(this, "Ups something wrong.. please try again..");
            finish();
        }
    }

    public void ShowPhoto(Photo m) {
        new ShowPhotoDialog(this, this, m, realm).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BaseApp.REQUEST_ID && resultCode == RESULT_OK) {
            presenter.doProcessPic(mCurrentPhotoPath, project.getProject_id(), project.getSite_name(), gpsCache.getLatLng());
        }
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
    public void onFInishPhoto(boolean isFinish, String msg) {
        if (isFinish) {
            Log.d(TAG, " msg isFinish" + msg);
            addPhotoDB(msg);
        } else Utils.showToast(this, msg);
    }

    public void addPhotoDB(String path) {
        Log.d(TAG, "initakePic project.getProject_id(): " + project.getProject_id());
//        workPkg = realm.where(WorkPkg.class).equalTo("wp_id", workPkg.getWp_id()).findFirst();
        Log.d(TAG, "initakePic workPkg.getWp_id(): " + workPkg.getWp_id());
//        activity = realm.where(MActivity.class).equalTo("activity_id",  activity.getActivity_id()).findFirst();
        Log.d(TAG, "initakePic activity.getActivity_id(): " + activity.getActivity_id());
        Log.d(TAG, "initakePic status_photo: " + status_photo);
//        Photo m = null;
//        if (status_photo == "0") {
        Log.d(TAG, "initakePic path set status==0");
        Photo m = getRealm().where(Photo.class)
                .equalTo(Photo.ID, Photo.getstringid(path))
                .equalTo("path", path)
                .equalTo(Photo.PROJECT_ID, project.getProject_id())
                .equalTo("wp_id", workPkg.getWp_id())
                .equalTo("activity_id", activity.getActivity_id())
                .findFirst();
//                  .equalTo("date_photo", dataOffline.getDate_offline())
//                  .equalTo("time_photo", dataOffline.getTime_offline())
//                  .equalTo("status_photo", 0)

//        }
//        if (status_photo == "1") {
//            Log.d(TAG, "initakePic path set status==1");
//            m = getRealm().where(Photo.class)
////                    .equalTo(Photo.ID, Photo.getstringid(path))
//                    .equalTo("path", path)
//                    .equalTo(Photo.PROJECT_ID, project.getProject_id())
//                    .equalTo("wp_id", workPkg.getWp_id())
//                    .equalTo("activity_id", activity.getActivity_id())
//                    .notEqualTo("status_photo", "1")
//                    .findFirst();
//        }else{
//            Log.d(TAG, "initakePic path set status==1");
//            m = getRealm().where(Photo.class)
////                    .equalTo(Photo.ID, Photo.getstringid(path))
//                    .equalTo("path", path)
//                    .equalTo(Photo.PROJECT_ID, project.getProject_id())
//                    .equalTo("wp_id", workPkg.getWp_id())
//                    .equalTo("activity_id", activity.getActivity_id())
//                    .notEqualTo("status_photo", "1")
//                    .findFirst();
//        }

//        Log.d(TAG, "initakePic status_photo: " + m.getStatus_photo());
//       if (m == null && !m.getStatus_photo().contentEquals("1")){
        if (m == null) {
//                boolean is_dataoffLine =values.containsKey(dataOffline.getStatus_project_offline());
//                if (!is_dataoffLine == false) {
            Log.d(TAG, "initakePic path set status==0");
            Log.d(TAG, "initakePic path" + path);
            m = new Photo();
            m.setPath(path);
            m.setProjectid(project.getProject_id());
            m.setId(Photo.getstringid(path));
            m.setWp_id(workPkg.getWp_id());
            m.setActivity_id(activity.getActivity_id());
            m.setStatus_photo("0");
            m.setUploaded(false);
            m.setJenis_file_photo(typeFile);
//            m.setProgress_photo(project.getProgress());
//            m.setDate_photo("0");
//            m.setTime_photo("0");
            Log.d(TAG, "add photo null:" + m.getPath());

        } else {
            Log.d(TAG, "initakePic path set status==1");
//            m.setProjectid(project.getProject_id());
//            m.setId(Photo.getstringid(path));
//            m.setWp_id(workPkg.getWp_id());
//            m.setActivity_id(activity.getActivity_id());
//            m.setStatus_photo("1");
//            m.setUploaded(true);
        }

        Log.d(TAG, "addPhotoDB: " + m.getPath());
        saveState(m);
    }
    //Photo.newPhoto(realm,project.getProject_id());
//        }
//    }

    public void saveState(Photo m) {
        Photo.updatePhoto(realm, m);
        try {
            // if(!m.getDescription().equals(""))
            // FileUtils.writeToFile(getPathExt(m.getId()),m.getDescription());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void updatePhoto(Photo m, String desc, String extFile) {
        try {
            Photo.updateType(realm, m, desc, extFile);
/*
            if(!m.getDescription().equals(""))
                FileUtils.writeToFile(getPathExt(m.getId()),m.getDescription());*/
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void deletePic(final Photo m) {
//        if (m.getStatus_photo().contentEquals("1")) {
//            showAlertDialog("Warning", "Photo Tidak Bisa diHapus", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            }, true, "");
//            return;
//        } else {
        showAlertDialog("Warning", "Hapus Photo ?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    FileUtils.delete(PhotoActivity.this, m.getPath());
                    FileUtils.delete(PhotoActivity.this, getPathExt(m.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Photo.delete(realm, m);
            }
        }, true, "");
//        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = getPathPhoto();
        Log.d(TAG, "createImageFile: " + mCurrentPhotoPath);
        return new File(mCurrentPhotoPath);
    }

}
