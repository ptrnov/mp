package com.cudo.mproject.Menu.Video;

import java.io.File;

/**
 * Created by adsxg on 2/13/2018.
 */

public interface VideoInterface {
    interface View{
        void onFinishSubmit(boolean isFinish,String msg);
        void onFinishUpload(boolean isFinish,String msg);
        void onFInishUpdate(boolean isFinish,String msg);
        //        void onFInishPhoto(boolean isFinish,String msg,ContentValues values);
        void onFInishVideo(boolean isFinish,String msg);
    }
    interface Presenter{
        void doTakeVideo(File path);
        void doProcessVideo(String path,String id, String site_name, String latlng);
        void doSubmit(String dptid,String progress);
        void onFinishUploadVideo(String pa_id,String project_id,String base64,String comment);
        void onFInishUpdateVideo(String pa_id);
    }
}
