package com.cudo.mproject.Menu.Site.IMB;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BaseApp;
import com.cudo.mproject.Dialog.ShowPhotoIMBDialog;
import com.cudo.mproject.Model.DataIMB;
import com.cudo.mproject.Model.DataPenjagaLahan;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.PhotoIMB;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.Utils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class IMBPhotoActivity extends BaseActivity implements IMBPhotoInterface.View {
    String TAG = getClass().getSimpleName();
    AdapterPhotoIMBRealm adapterPhotoIMBRealm;
    @BindView(R.id.recviewIMB)
    RecyclerView recviewIMB;


    IMBPhotoPresenter imbPhotoPresenter;
    DataIMB dataIMB;
    PhotoIMB photoIMB;
    Project project;
    String mCurrentPhotoPath;
    String status_photo = "";

    @BindView(R.id.add_photo_imb)
    TextView add_photo_imb;

    @OnClick(R.id.backIMB)
    void back() {
        onBackPressed();
    }
    @OnClick(R.id.addPhotoIMB)
    void takePhoto() {
        initakePic();
    }

    //
    String getFolderPath() {
        return FileUtils.rootpath(this,"imb_image"+"_"+ project.getProject_id());
    }

    String getPathPhoto() throws IOException {
        return FileUtils.path(this, getFolderPath(),  project.getProject_id() + "_" + project.getId() + "_" + PhotoIMB.getNextID(realm), ".jpg");
    }

    String getPathExt() throws IOException {
        return FileUtils.path(this, getFolderPath(),  project.getProject_id() + "_" + project.getId() + "_" + PhotoIMB.getNextID(realm), ".ext");
    }

    String getPathPhoto(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() +"_"+ id + "_" + PhotoIMB.getNextID(realm), ".jpg");
    }

    String getPathExt(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() +"_"+ id + "_" + PhotoIMB.getNextID(realm), ".ext");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_imb);
        ButterKnife.bind(this);
        ButterKnife.bind(this);
//
        Intent intent = getIntent();
        String projectId = intent.getStringExtra("projectId");
        String id_offline = "";
        String is_offline = intent.getStringExtra("is_offline");
        String date_photo = "";
        String time_photo = "";
        if (!intent.getStringExtra("is_offline").isEmpty()) {
            is_offline = intent.getStringExtra("is_offline");
        }
//        iniStringExtra(projectId, is_offline);
        if (is_offline.contentEquals("0")) {
            status_photo = "0";
            date_photo = "";
            time_photo = "";
//          idOffline = "0";
            Log.d(TAG, "is_offline 0: " + is_offline);
//            return;
            project = getRealm().where(Project.class).equalTo(Project.PROJECT_ID, projectId).findFirst();
            initStringExtra(projectId, status_photo, date_photo, time_photo);

        } else if (is_offline.contentEquals("1")) {
            User user = getRealm().where(User.class).findFirst();
            dataIMB = getRealm().where(DataIMB.class)
                    .equalTo("project_id", projectId)
                    .equalTo("userName", user.getUsername())
                    .findFirst();
            photoIMB = getRealm().where(PhotoIMB.class)
                    .equalTo("projectid", projectId)
                    .equalTo("userName", user.getUsername())
                    .findFirst();
            status_photo = "1";
            date_photo = intent.getStringExtra("dateSubmit");
            time_photo = intent.getStringExtra("timeSubmit");
            initStringExtra(projectId, status_photo, date_photo, time_photo);
        }
    }

    void initStringExtra(String projectId, String is_offline, String date_photo, String time_photo) {
//
        realm = Realm.getDefaultInstance();
        if (is_offline == "0") {
            checkUser();
            checkGPS();
            project = realm.where(Project.class).equalTo(Project.PROJECT_ID, projectId).findFirst();
            imbPhotoPresenter = new IMBPhotoPresenter(this, this);
            initView(projectId, status_photo, date_photo, time_photo);
        } else {
            checkUser();
            checkGPS();
            initView(projectId, status_photo, date_photo, time_photo);
        }

    }

    void initView(String projectId, String status_photo, String date_photo, String time_photo) {

        if (status_photo.contentEquals("1")) {
            FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addPhotoIMB);
            add.setVisibility(RelativeLayout.GONE);
            add.setVisibility(RelativeLayout.INVISIBLE);
            add_photo_imb.setText("View Photo");
//
            PhotoIMB dataPhotoIMB = getRealm().where(PhotoIMB.class)
                    .equalTo(PhotoIMB.PROJECT_ID, projectId)
                    .equalTo("status_photo", "1")
                    .equalTo("dateSubmit", date_photo)
                    .equalTo("timeSubmit", time_photo)
                    .findFirst();
            Log.d(TAG, "dataPhoto 0: status_photo.contentEquals(\"1\")" + dataPhotoIMB);
//          return;
//
//            if (dataPhoto.isUploaded() == true) {
            if (dataPhotoIMB.isUploaded()) {

                adapterPhotoIMBRealm = new AdapterPhotoIMBRealm(getRealm().where(PhotoIMB.class)
                        .equalTo(PhotoIMB.PROJECT_ID, projectId)
                        .equalTo("status_photo", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", true)
                        .findAll());
            } else {

                adapterPhotoIMBRealm = new AdapterPhotoIMBRealm(getRealm().where(PhotoIMB.class)
                        .equalTo(PhotoIMB.PROJECT_ID, projectId)
                        .equalTo("status_photo", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", false)
                        .findAll());

                Log.d(TAG, "adapterPhotoPersonRealm upload flase, status_photo 1 " + adapterPhotoIMBRealm);
            }
            if (dataPhotoIMB.isUploaded() == true) {

                adapterPhotoIMBRealm = new AdapterPhotoIMBRealm(getRealm().where(PhotoIMB.class)
                        .equalTo(PhotoIMB.PROJECT_ID, projectId)
                        .equalTo("status_photo", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", true)
                        .findAll());
            } else {

                adapterPhotoIMBRealm = new AdapterPhotoIMBRealm(getRealm().where(PhotoIMB.class)
                        .equalTo(PhotoIMB.PROJECT_ID, projectId)
                        .equalTo("status_photo", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", false)
                        .findAll());

                Log.d(TAG, "adapterPhotoPersonRealm upload flase, status_photo 1 " + adapterPhotoIMBRealm);
            }

        } else if (status_photo.contentEquals("0")) {
            Log.d(TAG, "dataPhoto 0: status_photo.contentEquals(\"0\")");
            adapterPhotoIMBRealm = new AdapterPhotoIMBRealm(getRealm().where(PhotoIMB.class)
                    .equalTo("projectid", projectId)
                    .equalTo("status_photo", "0")
                    .equalTo(PhotoIMB.UPLOADED, false)
                    .findAll());
        }
        recviewIMB.setLayoutManager(new GridLayoutManager(this, 2));
        recviewIMB.setAdapter(adapterPhotoIMBRealm);
    }

        public void initakePic() {
        if (checkPermission()) {
//            Log.d(TAG, "initakePic: " + !gpsCache.isExpired());
//            if (!gpsCache.isValidGPS()) {
//                try {
//                    personPhotoPresenter.doTakePic(createImageFile());
//                } catch (Exception e) {
//                    Utils.showToast(this, "FILE something wrong.. please try again..");
//                    e.printStackTrace();
//                }
//            } else {
            try {
                imbPhotoPresenter.doTakePic(createImageFile());
//                Utils.showToast(this, "show()" + createImageFile());
            } catch (Exception e) {
                Utils.showToast(this, "FILE something wrong.. please try again..");
                e.printStackTrace();
            }
//            }
//                Utils.showToast(this, "GPS something wrong.. please try again..");
//                onBackPressed();
//                onBackPressed();
//                return;
//            }
        } else {
            Utils.showToast(this, "Ups something wrong.. please try again..");
            finish();
        }
    }
//    public void initakePic() {
//        if (checkPermission()) {
//            Log.d(TAG, "initakePic: " + !gpsCache.isExpired());
//            if (gpsCache.isValidGPS()||!gpsCache.isValidGPS() ) {
//                try {
//                    imbPhotoPresenter.doTakePic(createImageFile());
//                } catch (Exception e) {
//                    Utils.showToast(this, "FILE something wrong.. please try again..");
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    imbPhotoPresenter.doTakePic(createImageFile());
//                } catch (Exception e) {
//                    Utils.showToast(this, "FILE something wrong.. please try again....");
//                    e.printStackTrace();
//                }
////                Utils.showToast(this, "GPS something wrong.. please try again..");
////                onBackPressed();
////                onBackPressed();
//            }
//        } else {
//            Utils.showToast(this, "Ups something wrong.. please try again..");
//            finish();
//        }
//    }
    public void ShowPhotoIMBDialog(PhotoIMB m) {
        new ShowPhotoIMBDialog(this, this, m, realm).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BaseApp.REQUEST_ID && resultCode == RESULT_OK) {
//            imbPhotoPresenter.doProcessPic(mCurrentPhotoPath, dataIMB.getProject_id());
            imbPhotoPresenter.doProcessPic(mCurrentPhotoPath, project.getProject_id(),project.getSite_name());
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
        PhotoIMB photoIMB = getRealm().where(PhotoIMB.class)
                .equalTo(PhotoIMB.ID, PhotoIMB.getstringid(path))
                .equalTo("pathPhotoIMB", path)
                .equalTo(PhotoIMB.PROJECT_ID, project.getProject_id())
                .findFirst();

        if (photoIMB == null) {
//                boolean is_dataoffLine =values.containsKey(dataOffline.getStatus_project_offline());
//                if (!is_dataoffLine == false) {
            Log.d(TAG, "initakePic path set status==0");
            Log.d(TAG, "initakePic path" + path);
            photoIMB = new PhotoIMB();
            photoIMB.setPathPhotoIMB(path);
            photoIMB.setProjectid(project.getProject_id());
            photoIMB.setId(Photo.getstringid(path));
            photoIMB.setStatus_photo("0");
            photoIMB.setUploaded(false);
//            m.setProgress_photo(project.getProgress());
//            m.setDate_photo("0");
//            m.setTime_photo("0");
            Log.d(TAG, "add photo null:" + photoIMB.getPathPhotoIMB());

        } else {
            Log.d(TAG, "initakePic path set status==1");
//            m.setProjectid(project.getProject_id());
//            m.setId(Photo.getstringid(path));
//            m.setWp_id(workPkg.getWp_id());
//            m.setActivity_id(activity.getActivity_id());
//            m.setStatus_photo("1");
//            m.setUploaded(true);
        }

        Log.d(TAG, "addPhotoDB: " + photoIMB.getPathPhotoIMB());
        saveState(photoIMB);
    }

    public void saveState(PhotoIMB photoIMB) {
        PhotoIMB.updatePhoto(realm, photoIMB);
        try {
            // if(!m.getDescription().equals(""))
            // FileUtils.writeToFile(getPathExt(m.getId()),m.getDescription());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    public void deletePic(final PhotoIMB photoIMB) {
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
                    FileUtils.delete(IMBPhotoActivity.this, photoIMB.getPathPhotoIMB());
                    FileUtils.delete(IMBPhotoActivity.this, getPathExt(photoIMB.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PhotoIMB.delete(realm, photoIMB);
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

