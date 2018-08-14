package com.cudo.mproject.Menu.Site.Person;

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
import com.cudo.mproject.Dialog.ShowPhotoPersonDialog;
import com.cudo.mproject.Model.DataPenjagaLahan;
import com.cudo.mproject.Model.Photo;
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

public class PersonPhotoActivity extends BaseActivity implements PersonPhotoInterface.View {
    String TAG = getClass().getSimpleName();
    AdapterPhotoPersonRealm adapterPhotoPersonRealm;
    @BindView(R.id.recview_photo_person)
    RecyclerView recview_photo_person;


    PersonPhotoPresenter personPhotoPresenter;
    DataPenjagaLahan dataPenjagaLahan;
    PhotoPerson photoPerson;
    Project project;
    String mCurrentPhotoPath;
    String status_photo = "";

    @BindView(R.id.add_photo_penjaga)
    TextView add_photo_penjaga;

    @OnClick(R.id.backPerson)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.addPhotoPerson)
    void takePhoto() {
        initakePic();
    }

    //
    String getFolderPath() {
        return FileUtils.rootpath(this, "person_image" + "_" + project.getProject_id());
    }

    String getPathPhoto() throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + project.getId() + "_" + PhotoPerson.getNextID(realm), ".jpg");
    }

    String getPathExt() throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + project.getId() + "_" + PhotoPerson.getNextID(realm), ".ext");
    }

    String getPathPhoto(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + id + "_" + PhotoPerson.getNextID(realm), ".jpg");
    }

    String getPathExt(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + id + "_" + PhotoPerson.getNextID(realm), ".ext");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_person);
//        recview_photo_person = (RecyclerView) findViewById(R.id.recview_photo_person);
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
            project = getRealm().where(Project.class).equalTo(Project.PROJECT_ID, projectId).findFirst();
//            User user = getRealm().where(User.class).findFirst();

//            dataPenjagaLahan = getRealm().where(DataPenjagaLahan.class)
//                    .equalTo("project_id", projectId)
//                    .equalTo("userName", user.getUsername())
//                    .findFirst();
//            photoPerson  = getRealm().where(PhotoPerson.class)
//                    .equalTo("projectid", projectId)
//                    .equalTo("userName", user.getUsername())
//                    .findFirst();
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
            personPhotoPresenter = new PersonPhotoPresenter(this, this);
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
            PhotoPerson dataPhoto = getRealm().where(PhotoPerson.class)
                    .equalTo(PhotoPerson.PROJECT_ID, projectId)
                    .equalTo("statusPhoto", "1")
                    .equalTo("dateSubmit", date_photo)
                    .equalTo("timeSubmit", time_photo)
                    .findFirst();
            Log.d(TAG, "dataPhoto 0: status_photo.contentEquals(\"1\")" + dataPhoto);
//          return;
//
//            if (dataPhoto.isUploaded() == true) {
            if (dataPhoto.isUploaded()) {

                adapterPhotoPersonRealm = new AdapterPhotoPersonRealm(getRealm().where(PhotoPerson.class)
                        .equalTo(PhotoPerson.PROJECT_ID, projectId)
                        .equalTo("statusPhoto", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", true)
                        .findAll());
            } else {

                adapterPhotoPersonRealm = new AdapterPhotoPersonRealm(getRealm().where(PhotoPerson.class)
                        .equalTo(PhotoPerson.PROJECT_ID, projectId)
                        .equalTo("statusPhoto", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", false)
                        .findAll());

                Log.d(TAG, "adapterPhotoPersonRealm upload flase, status_photo 1 " + adapterPhotoPersonRealm);
            }
            if (dataPhoto.isUploaded() == true) {

                adapterPhotoPersonRealm = new AdapterPhotoPersonRealm(getRealm().where(PhotoPerson.class)
                        .equalTo(PhotoPerson.PROJECT_ID, projectId)
                        .equalTo("statusPhoto", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", true)
                        .findAll());
            } else {

                adapterPhotoPersonRealm = new AdapterPhotoPersonRealm(getRealm().where(PhotoPerson.class)
                        .equalTo(PhotoPerson.PROJECT_ID, projectId)
                        .equalTo("statusPhoto", "1")
                        .equalTo("dateSubmit", date_photo)
                        .equalTo("timeSubmit", time_photo)
                        .equalTo("uploaded", false)
                        .findAll());

                Log.d(TAG, "adapterPhotoPersonRealm upload flase, status_photo 1 " + adapterPhotoPersonRealm);
            }

        } else if (status_photo.contentEquals("0")) {
            Log.d(TAG, "dataPhoto 0: status_photo.contentEquals(\"0\")");
            adapterPhotoPersonRealm = new AdapterPhotoPersonRealm(getRealm().where(PhotoPerson.class)
                    .equalTo("projectid", projectId)
                    .equalTo("statusPhoto", "0")
                    .equalTo(PhotoPerson.UPLOADED, false)
                    .findAll());
        }
        recview_photo_person.setLayoutManager(new GridLayoutManager(this, 2));
        recview_photo_person.setAdapter(adapterPhotoPersonRealm);
    }

    public void initakePic() {
        if (checkPermission()) {
            try {
                personPhotoPresenter.doTakePic(createImageFile());
//                Utils.showToast(this, "show()" + createImageFile());
            } catch (Exception e) {
                Utils.showToast(this, "FILE something wrong.. please try again..");
                e.printStackTrace();
            }
        } else {
            Utils.showToast(this, "Ups something wrong.. please try again..");
            finish();
        }
    }

    public void ShowPhotoPersonDialog(PhotoPerson m) {
        new ShowPhotoPersonDialog(this, this, m, realm).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BaseApp.REQUEST_ID && resultCode == RESULT_OK) {
//            personPhotoPresenter.doProcessPic(mCurrentPhotoPath, dataPenjagaLahan.getProject_id());//projectId
            personPhotoPresenter.doProcessPic(mCurrentPhotoPath, project.getProject_id(), project.getSite_name());
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
        PhotoPerson photoPerson = getRealm().where(PhotoPerson.class)
                .equalTo(PhotoPerson.ID, PhotoPerson.getstringid(path))
                .equalTo("pathPhotoPerson", path)
                .equalTo(PhotoPerson.PROJECT_ID, project.getProject_id())
                .findFirst();


        if (photoPerson == null) {
//                boolean is_dataoffLine =values.containsKey(dataOffline.getStatus_project_offline());
//                if (!is_dataoffLine == false) {
            Log.d(TAG, "initakePic path set status==0");
            Log.d(TAG, "initakePic path" + path);
            Log.d(TAG, "initakePic path" + project.getProject_id());
            photoPerson = new PhotoPerson();
            photoPerson.setPathPhotoPerson(path);
            photoPerson.setProjectid(project.getProject_id());
            photoPerson.setId(Photo.getstringid(path));
            photoPerson.setStatusPhoto("0");
            photoPerson.setUploaded(false);
//            m.setProgress_photo(project.getProgress());
//            m.setDate_photo("0");
//            m.setTime_photo("0");
            Log.d(TAG, "add photo null:" + photoPerson.getPathPhotoPerson());

        } else {
            Log.d(TAG, "initakePic path set status==1");
//            m.setProjectid(project.getProject_id());
//            m.setId(Photo.getstringid(path));
//            m.setWp_id(workPkg.getWp_id());
//            m.setActivity_id(activity.getActivity_id());
//            m.setStatus_photo("1");
//            m.setUploaded(true);
        }

        Log.d(TAG, "addPhotoDB: " + photoPerson.getPathPhotoPerson());
        saveState(photoPerson);
    }

    public void saveState(PhotoPerson photoPerson) {
        PhotoPerson.updatePhoto(realm, photoPerson);
        try {
            // if(!m.getDescription().equals(""))
            // FileUtils.writeToFile(getPathExt(m.getId()),m.getDescription());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void deletePic(final PhotoPerson photoPerson) {
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
                    FileUtils.delete(PersonPhotoActivity.this, photoPerson.getPathPhotoPerson());
                    FileUtils.delete(PersonPhotoActivity.this, getPathExt(photoPerson.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PhotoPerson.delete(realm, photoPerson);
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
