package com.cudo.mproject.Menu.Video;

import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BaseApp;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.Utils;

import java.io.File;

import io.realm.Realm;

/**
 * Created by adsxg on 3/27/2018.
 */

public class VideoPresenter implements VideoInterface.Presenter {
    VideoInterface.View view;
    BaseActivity mContext;
    String isiExtFile = null;

    public VideoPresenter(VideoInterface.View view, BaseActivity context, String extFile) {

        this.view = view;
        mContext = context;
        isiExtFile = extFile;
    }

    @Override
    public void doTakeVideo(File path) {
        dispatchTakeVideoIntent(path);
    }

    @Override
    public void doProcessVideo(String path, String id, String site_name, String latlng) {

    }

    @Override
    public void doSubmit(String dptid, String progress) {

    }

    @Override
    public void onFinishUploadVideo(String pa_id, String project_id, String base64, String comment) {

    }

    @Override
    public void onFInishUpdateVideo(String pa_id) {

    }

    void dispatchTakeVideoIntent(File file) {

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        try {
            if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                // Create the File where the photo should go
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, FileUtils.getUri(mContext, file.getAbsolutePath()));
                mContext.startActivityForResult(takePictureIntent, BaseApp.REQUEST_ID);

            }
        } catch (Exception e) {
            Utils.showToast(mContext, "cant record Video please check storage or permission..");
            Log.d("eror", "cant record Video please check storage or permission..");
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
                    if (isiExtFile == "mp4") {
                        if (aListFile.getName().contains("mp4")) {
                            m.setPath(aListFile.getAbsolutePath());
                        } else if (aListFile.getName().contains("ext")) {
                            m.setDescription(FileUtils.getStringFromFile(aListFile.getAbsolutePath()));
                        }
                    }
                    if (isiExtFile == "3gp") {
                        if (aListFile.getName().contains("3gp")) {
                            m.setPath(aListFile.getAbsolutePath());
                        } else if (aListFile.getName().contains("ext")) {
                            m.setDescription(FileUtils.getStringFromFile(aListFile.getAbsolutePath()));
                        }
                    }

                    Photo.updatePhoto(realm, m);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
