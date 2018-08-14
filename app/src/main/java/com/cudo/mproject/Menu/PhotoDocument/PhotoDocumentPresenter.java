package com.cudo.mproject.Menu.PhotoDocument;

import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BaseApp;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.Utils;

import java.io.File;

import io.realm.Realm;

public class PhotoDocumentPresenter implements PhotoDocumentInterface.Presenter {

    PhotoDocumentInterface.View view;
    BaseActivity mContext;

    public PhotoDocumentPresenter(PhotoDocumentInterface.View view, BaseActivity context) {
        this.view = view;
        mContext = context;
    }

    @Override
    public void doTakePic(File path) {
        dispatchTakePictureIntent(path);
    }

    @Override
    public void doProcessPic(final String path, final String id, final String site_name, final String latlng) {

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work
                        return FileUtils.timestampItAndSave2(path, id, site_name, latlng,"jpg","photo_doc");
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
            Log.d("eror", "cant Create Photo please check storage or permission..");
        }
    }

    public void checkFiletoDB(String projectid, String wpId, String activityId, final String path, final Realm realm) {
        mContext.baseshowProgress(true);
        if (path != null)
            doCheckfile(projectid, wpId, activityId, path, realm);
        mContext.baseshowProgress(false);
    }

    void doCheckfile(String projectid, String wpId, String activityId, String path, Realm realm) {
        File[] listFile = new File(path).listFiles();
        if (listFile.length != 0) {
            try {

                for (File aListFile : listFile) {
                    Photo m = new Photo();
                    if (aListFile.getName().contains("jpg")) {
                        m.setPath(aListFile.getAbsolutePath());
                    } else if (aListFile.getName().contains("ext")) {
                        m.setDescription(FileUtils.getStringFromFile(aListFile.getAbsolutePath()));
                    }
                    Photo.updatePhoto(realm, m);
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
