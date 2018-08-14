package com.cudo.mproject.Menu.Site.Person;

import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BaseApp;
import com.cudo.mproject.Model.PhotoPerson;
import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.Utils;

import java.io.File;

import io.realm.Realm;

public class PersonPhotoPresenter implements PersonPhotoInterface.Presenter {

    PersonPhotoInterface.View view;
    BaseActivity mContext;

    public PersonPhotoPresenter(PersonPhotoInterface.View view, BaseActivity context) {
        this.view = view;
        mContext = context;
    }

    @Override
    public void doTakePic(File path) {
//        Utils.showToast(mContext, "doTakePic()"+path);
        dispatchTakePictureIntent(path);
    }

    @Override
    public void doProcessPic(final String path, final String id, final String siteName) {

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work
//                        return ImageUtils.timestampItAndSave2(path, id);
                        return FileUtils.timestampItAndSave2(path, id, siteName, null, "jpg", "p0");
//                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        mContext.baseshowProgress(false);
                        view.onFInishPhoto(result, result ? path : "Simpan GAGAL... coba kembali..");
                    }
                }).create().start();

    }

    void dispatchTakePictureIntent(File file) {
        Log.d("error", "dispatchTakePictureIntent..");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        try {
            if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                // Create the File where the photo should go
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, FileUtils.getUri(mContext, file.getAbsolutePath()));
                mContext.startActivityForResult(takePictureIntent, BaseApp.REQUEST_ID);

            }
        } catch (Exception e) {
            Utils.showToast(mContext, "cant Create Photo please check storage or permission..");
            Log.d("error", "cant Create Photo please check storage or permission..");
        }
    }

    public void checkFiletoDB(String projectid, final String path, final Realm realm) {
        mContext.baseshowProgress(true);
        if (path != null)
            doCheckfile(projectid, path, realm);
        mContext.baseshowProgress(false);
    }

    void doCheckfile(String projectid, String path, Realm realm) {
        File[] listFile = new File(path).listFiles();
        if (listFile.length != 0) {
            try {

                for (File aListFile : listFile) {
                    PhotoPerson photoPerson = new PhotoPerson();
                    if (aListFile.getName().contains("jpg")) {
                        photoPerson.setPathPhotoPerson(aListFile.getAbsolutePath());
                    } else if (aListFile.getName().contains("ext")) {
                        photoPerson.setDescription(FileUtils.getStringFromFile(aListFile.getAbsolutePath()));
                    }
                    PhotoPerson.updatePhoto(realm, photoPerson);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void doSubmit(String dptid, String progress) {

    }

    @Override
    public void onFinishUpload(String pa_id, String project_id, String base64, String comment) {

    }

    @Override
    public void onFInishUpdate(String pa_id) {

    }
}
