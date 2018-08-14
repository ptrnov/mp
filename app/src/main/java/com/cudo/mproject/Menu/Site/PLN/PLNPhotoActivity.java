package com.cudo.mproject.Menu.Site.PLN;

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
import com.cudo.mproject.Dialog.ShowPhotoPersonDialog;
import com.cudo.mproject.Dialog.ShowPhotoPlnDialog;
import com.cudo.mproject.Menu.Site.Person.AdapterPhotoPersonRealm;
import com.cudo.mproject.Menu.Site.Person.PersonPhotoActivity;
import com.cudo.mproject.Menu.Site.Person.PersonPhotoPresenter;
import com.cudo.mproject.Model.DataPLNSite;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.PhotoPLN;
import com.cudo.mproject.Model.PhotoPerson;
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

public class PLNPhotoActivity extends BaseActivity implements PLNPhotoInterface.View {
    String TAG = getClass().getSimpleName();
    AdapterPhotoPlnRealm adapterPhotoPlnRealm;
    @BindView(R.id.recview_photo_pln)
    RecyclerView recview_photo_pln;

    PLNPhotoPresenter plnPhotoPresenter;
    DataPLNSite dataPLNSite;
    PhotoPLN photoPLN;
    Project project;
    String mCurrentPhotoPath;
    String status_photo = "";

    @BindView(R.id.add_photo_pln)
    TextView add_photo_penjaga;

    @OnClick(R.id.backPln)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.addPhotoPln)
    void takePhoto() {
        initakePic();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_photo_pln);
//        ButterKnife.bind(this);
//
//        Intent intent = getIntent();
//    }
//@Override
//    @OnClick(R.id.backPln)
//    void back() {
//        onBackPressed();
//    }
    //
    String getFolderPath() {
        return FileUtils.rootpath(this,"pln_image"+"_"+ project.getProject_id());
    }

    String getPathPhoto() throws IOException {
        return FileUtils.path(this, getFolderPath(),  project.getProject_id() + "_" + project.getId() + "_" + PhotoPLN.getNextID(realm), ".jpg");
    }

    String getPathExt() throws IOException {
        return FileUtils.path(this, getFolderPath(),  project.getProject_id() + "_" + project.getId() + "_" + PhotoPLN.getNextID(realm), ".ext");
    }

    String getPathPhoto(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() +"_"+ id + "_" + PhotoPLN.getNextID(realm), ".jpg");
    }

    String getPathExt(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() +"_"+ id + "_" + PhotoPLN.getNextID(realm), ".ext");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pln);
//      recview_photo_person = (RecyclerView) findViewById(R.id.recview_photo_person);
//
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
//        initStringExtra(projectId, is_offline);
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
            dataPLNSite = getRealm().where(DataPLNSite.class)
                    .equalTo("project_id", projectId)
                    .equalTo("userName", user.getUsername())
                    .findFirst();
            photoPLN = getRealm().where(PhotoPLN.class)
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
            plnPhotoPresenter = new PLNPhotoPresenter(this, this);
            initView(projectId, status_photo, date_photo, time_photo);
        } else {
            checkUser();
            checkGPS();
            initView(projectId, status_photo, date_photo, time_photo);
        }

    }

    void initView(String projectId, String status_photo, String date_photo, String time_photo) {

        if (status_photo.contentEquals("1")) {
            FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addPhotoPerson);
            add.setVisibility(RelativeLayout.GONE);
            add.setVisibility(RelativeLayout.INVISIBLE);
            add_photo_penjaga.setText("View Photo");
//
            PhotoPLN dataPhoto = getRealm().where(PhotoPLN.class)
                    .equalTo(PhotoPLN.PROJECT_ID, projectId)
                    .equalTo("status_photo", "1")
                    .equalTo("dateSubmit", date_photo)
                    .equalTo("timeSubmit", time_photo)
                    .findFirst();
            Log.d(TAG, "dataPhoto 0: status_photo.contentEquals(\"1\")" + dataPhoto);
//          return;
//
//            if (dataPhoto.isUploaded() == true) {
            if (dataPhoto.isUploaded()) {

                adapterPhotoPlnRealm = new AdapterPhotoPlnRealm(getRealm().where(PhotoPLN.class)
                        .equalTo(PhotoPLN.PROJECT_ID, projectId)
                        .equalTo("status_photo", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", true)
                        .findAll());
            } else {

                adapterPhotoPlnRealm = new AdapterPhotoPlnRealm(getRealm().where(PhotoPLN.class)
                        .equalTo(PhotoPLN.PROJECT_ID, projectId)
                        .equalTo("status_photo", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", false)
                        .findAll());

                Log.d(TAG, "adapterPhotoPersonRealm upload flase, status_photo 1 " + adapterPhotoPlnRealm);
            }
            if (dataPhoto.isUploaded() == true) {

                adapterPhotoPlnRealm = new AdapterPhotoPlnRealm(getRealm().where(PhotoPLN.class)
                        .equalTo(PhotoPLN.PROJECT_ID, projectId)
                        .equalTo("status_photo", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", true)
                        .findAll());
            } else {

                adapterPhotoPlnRealm = new AdapterPhotoPlnRealm(getRealm().where(PhotoPLN.class)
                        .equalTo(PhotoPLN.PROJECT_ID, projectId)
                        .equalTo("status_photo", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", false)
                        .findAll());

                Log.d(TAG, "adapterPhotoPlnRealm upload flase, status_photo 1 " + adapterPhotoPlnRealm);
            }

        } else if (status_photo.contentEquals("0")) {
            Log.d(TAG, "dataPhoto 0: status_photo.contentEquals(\"0\")");
            adapterPhotoPlnRealm = new AdapterPhotoPlnRealm(getRealm().where(PhotoPLN.class)
                    .equalTo("projectid", projectId)
                    .equalTo("status_photo", "0")
                    .equalTo(PhotoPerson.UPLOADED, false)
                    .findAll());
        }
        recview_photo_pln.setLayoutManager(new GridLayoutManager(this, 2));
        recview_photo_pln.setAdapter(adapterPhotoPlnRealm);
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
                plnPhotoPresenter.doTakePic(createImageFile());
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
    //public void initakePic() {
//    if (checkPermission()) {
//        Log.d(TAG, "initakePic: " + !gpsCache.isExpired());
//        if (gpsCache.isValidGPS()||!gpsCache.isValidGPS() ) {
//            try {
//                personPhotoPresenter.doTakePic(createImageFile());
//            } catch (Exception e) {
//                Utils.showToast(this, "FILE something wrong.. please try again..");
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                personPhotoPresenter.doTakePic(createImageFile());
//            } catch (Exception e) {
//                Utils.showToast(this, "FILE something wrong.. please try again....");
//                e.printStackTrace();
//            }
////                Utils.showToast(this, "GPS something wrong.. please try again..");
////                onBackPressed();
////                onBackPressed();
//        }
//    } else {
//        Utils.showToast(this, "Ups something wrong.. please try again..");
//        finish();
//    }
//}
    public void ShowPhotoPlnDialog(PhotoPLN m) {
        new ShowPhotoPlnDialog(this, this, m, realm).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BaseApp.REQUEST_ID && resultCode == RESULT_OK) {
//            personPhotoPresenter.doProcessPic(mCurrentPhotoPath, dataPenjagaLahan.getProject_id());//projectId
            plnPhotoPresenter.doProcessPic(mCurrentPhotoPath, project.getProject_id(),project.getSite_name());
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
        PhotoPLN photoPLN = getRealm().where(PhotoPLN.class)
                .equalTo(PhotoPLN.ID, PhotoPLN.getstringid(path))
                .equalTo("pathPhotoPLN", path)
                .equalTo(PhotoPLN.PROJECT_ID, project.getProject_id())
                .findFirst();


        if (photoPLN == null) {
//                boolean is_dataoffLine =values.containsKey(dataOffline.getStatus_project_offline());
//                if (!is_dataoffLine == false) {
            Log.d(TAG, "initakePic path set status==0");
            Log.d(TAG, "initakePic path" + path);
            photoPLN = new PhotoPLN();
            photoPLN.setPathPhotoPLN(path);
            photoPLN.setProjectid(project.getProject_id());
            photoPLN.setId(Photo.getstringid(path));
            photoPLN.setStatus_photo("0");
            photoPLN.setUploaded(false);
//            m.setProgress_photo(project.getProgress());
//            m.setDate_photo("0");
//            m.setTime_photo("0");
            Log.d(TAG, "add photo null:" + photoPLN.getPathPhotoPLN());

        } else {
            Log.d(TAG, "initakePic path set status==1");
//            m.setProjectid(project.getProject_id());
//            m.setId(Photo.getstringid(path));
//            m.setWp_id(workPkg.getWp_id());
//            m.setActivity_id(activity.getActivity_id());
//            m.setStatus_photo("1");
//            m.setUploaded(true);
        }

        Log.d(TAG, "addPhotoDB: " + photoPLN.getPathPhotoPLN());
        saveState(photoPLN);
    }

    public void saveState(PhotoPLN photoPLN) {
        PhotoPLN.updatePhoto(realm, photoPLN);
        try {
            // if(!m.getDescription().equals(""))
            // FileUtils.writeToFile(getPathExt(m.getId()),m.getDescription());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void deletePic(final PhotoPLN photoPLN) {
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
                    FileUtils.delete(PLNPhotoActivity.this, photoPLN.getPathPhotoPLN());
                    FileUtils.delete(PLNPhotoActivity.this, getPathExt(photoPLN.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PhotoPLN.delete(realm, photoPLN);
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