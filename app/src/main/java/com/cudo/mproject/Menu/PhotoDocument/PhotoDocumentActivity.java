package com.cudo.mproject.Menu.PhotoDocument;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.cudo.mproject.Dialog.ShowPhotoDocumentDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.cudo.mproject.Utils.FileUtils.rootpath;

/**
 * date          editing by        method                            Description
 * 02/02/2018     newbiecihuy
 * 02/02/2018     newbiecihuy
 * 03/03/2018     newbiecihuy
 * 25/04/2018     newbiecihuy
 */
public class PhotoDocumentActivity extends BaseActivity implements PhotoDocumentInterface.View {
    String TAG = getClass().getSimpleName();
    AdapterPhotoDocumentRealm adapter;
    @BindView(R.id.recview_doc)
    RecyclerView recyclerView;

    Project project;
    MActivity activity;
    WorkPkg workPkg;
    OfflineDataTransaction dataOffline;
    String mCurrentPhotoPath;
    String status_photo = "";
    PhotoDocumentPresenter photoDocumentPresenter;
    String typeFile = "";//photo, video, audio, document
    static String extFile = "";


    @OnClick(R.id.back_photoDoc)
    void back() {
        onBackPressed();
    }

    @BindView(R.id.add_photoDoc)
    TextView add_photoDoc;

    @OnClick(R.id.addPhotoDoc)
    void takePhoto() {
        initakePic();
    }

//    @OnClick(R.id.add_photoDoc)
//    void takePhoto() {
//        initakePic();
//    }

    //
    String getFolderPath() {
        return rootpath(this, "photo_document" + project.getProject_id());
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
        setContentView(R.layout.activity_photo_document);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
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

    void iniStringExtra(String sProjectid, String swp_id, String sactivity_id, String status_photo, String date_photo, String time_photo, String idOffline) {

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
            photoDocumentPresenter = new PhotoDocumentPresenter(this, this);
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
            add_photoDoc.setText("Document Photo");
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
                adapter = new AdapterPhotoDocumentRealm(realm.where(Photo.class)
                        .equalTo(Photo.PROJECT_ID, sProjectid)
                        .equalTo("wp_id", swp_id)
                        .equalTo("activity_id", sactivity_id)
                        .equalTo("status_photo", "1")
                        .equalTo("date_photo", date_photo)
                        .equalTo("time_photo", time_photo)
                        .equalTo("uploaded", true)
                        .findAll());
            } else {
                adapter = new AdapterPhotoDocumentRealm(realm.where(Photo.class)
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
            adapter = new AdapterPhotoDocumentRealm(realm.where(Photo.class)
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
            if (gpsCache.isValidGPS() || !gpsCache.isValidGPS()) {
                try {
                    photoDocumentPresenter.doTakePic(createImageFile());
                } catch (Exception e) {
                    Utils.showToast(this, "FILE something wrong.. please try again..");
                    e.printStackTrace();
                }
            } else {
                try {
                    photoDocumentPresenter.doTakePic(createImageFile());
                } catch (Exception e) {
                    Utils.showToast(this, "FILE something wrong.. please try again..");
                    e.printStackTrace();
                }
//                Utils.showToast(this, "GPS something wrong.. please try again..");
//                onBackPressed();
//                onBackPressed();
            }
        } else {
            Utils.showToast(this, "Ups something wrong.. please try again..");
            finish();
        }
    }

    public void ShowPhotoDocumentPhoto(Photo m) {
        new ShowPhotoDocumentDialog(this, this, m, realm).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BaseApp.REQUEST_ID && resultCode == RESULT_OK) {
            photoDocumentPresenter.doProcessPic(mCurrentPhotoPath, project.getProject_id(), project.getSite_name(), gpsCache.getLatLng());
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
            Log.d(TAG, " status_project_offline isFinish" + msg);
            addPhotoDB(msg);
        } else Utils.showToast(this, msg);
    }

    public void addPhotoDB(String path) {

//        Photo m = getRealm().where(Photo.class)
//                .equalTo(Photo.ID, Photo.getstringid(path))
//                .equalTo(Photo.PROJECT_ID, project.getProject_id())
//                .equalTo("wp_id", workPkg.getWp_id())
//                .equalTo("activity_id", activity.getActivity_id())
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
            Log.d(TAG, "add photo null:" + m.getPath());

        } else {
            Log.d(TAG, "initakePic path set status==1");
        }

        Log.d(TAG, "addPhotoDB: " + m.getPath());
        saveState(m);
    }


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
                    FileUtils.delete(PhotoDocumentActivity.this, m.getPath());
                    FileUtils.delete(PhotoDocumentActivity.this, getPathExt(m.getId()));
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