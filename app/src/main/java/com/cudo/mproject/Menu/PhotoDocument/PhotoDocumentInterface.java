package com.cudo.mproject.Menu.PhotoDocument;

import java.io.File;

public interface PhotoDocumentInterface {
    interface View{
        void onFinishSubmit(boolean isFinish,String msg);
        void onFinishUpload(boolean isFinish,String msg);
        void onFInishUpdate(boolean isFinish,String msg);
        //        void onFInishPhoto(boolean isFinish,String msg,ContentValues values);
        void onFInishPhoto(boolean isFinish,String msg);
    }
    interface Presenter{
        void doTakePic(File path);
        void doProcessPic(String path,String id, String site_name, String latlng);
        void doSubmit(String dptid,String progress);
        void onFinishUpload(String pa_id,String project_id,String base64,String comment);
        void onFInishUpdate(String pa_id);
    }
}