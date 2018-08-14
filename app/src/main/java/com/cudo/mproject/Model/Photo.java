package com.cudo.mproject.Model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 */

public class Photo extends RealmObject {
    static String TAG = Photo.class.getSimpleName();
    public static String ID = "id";
    public static String PROJECT_ID = "projectid";
    public static String WP_ID = "wp_id";
    public static String Activity_ID = "activity_id";
    public static String UPLOADED = "uploaded";

    @PrimaryKey
    @Getter
    @Setter
    int id;

    @Getter
    @Setter
    String path = "";

    @Getter
    @Setter
    String description = " ";
    @Getter
    @Setter
    String projectid = "";
    @Getter
    @Setter
//    boolean uploaded = false;
    private boolean uploaded;
    private String status_photo;
    private String wp_id;
    private String activity_id;
    private String date_photo;
    private String time_photo;
    private String jenis_file_photo;
    private int progress_photo;

    public String getStatus_photo() {
        return status_photo;
        /*
           0 --> addphoto
           1 --> submited
           2 --> submited
         */
    }

    public void setStatus_photo(String status_photo) {
        this.status_photo = status_photo;
    }

    public String getWp_id() {
        return wp_id;
    }

    public void setWp_id(String wp_id) {
        this.wp_id = wp_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getDate_photo() {
        return date_photo;
    }

    public void setDate_photo(String date_photo) {
        this.date_photo = date_photo;
    }

    public String getTime_photo() {
        return time_photo;
    }

    public void setTime_photo(String time_photo) {
        this.time_photo = time_photo;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getProgress_photo() {
        return progress_photo;
    }

    public void setProgress_photo(int progress_photo) {
        this.progress_photo = progress_photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getJenis_file_photo() {
        return jenis_file_photo;
    }

    public void setJenis_file_photo(String jenis_file_photo) {
        this.jenis_file_photo = jenis_file_photo;
    }

    public static void updatePhoto(Realm realm, final Photo photo) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(photo); // using insert API
                }
            });
        } finally {

        }
    }

    public static void updateDesc(Realm realm, final Photo photo, final String description, final String extFile) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                   photo.setDescription(description);
//                    realm.insertOrUpdate(photo);
                }
            });
        } finally {

        }
    }
    public static void updateType(Realm realm, final Photo photo, final String description, final String extFile) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    Photo updatePhoto = new Photo();
                    updatePhoto = photo;
                    Log.d(TAG, "here jpg");
//                    /* photo.setDescription("."+extFile); */
//                    if (extFile.contentEquals("png")) {
//                        Log.d(TAG, "extFile jpg");
//                        try {
//                            Bitmap bmp = null;
//                            File dataPhoto = new File(updatePhoto.getPath());
//                            FileOutputStream out = new FileOutputStream(dataPhoto.getName());
////                          String isi_out = dataPhoto.getName();
//                            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); //100-best quality
//                            out.close();
//                            updatePhoto.setPath(photo.getPath() + dataPhoto.getName()+"."+extFile);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (extFile.contentEquals("jpg")) {
//                        Log.d(TAG, "extFile jpg");
//                        try {
//                            Bitmap bmp = null;
//                            File dataPhoto = new File(photo.getPath());
//                            FileOutputStream out = new FileOutputStream(dataPhoto.getName());
////                          String isi_out = dataPhoto.getName();
//                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); //100-best quality
//                            out.close();
//                            updatePhoto.setPath(photo.getPath() + dataPhoto.getName()+"."+extFile);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                    updatePhoto.setDescription(description);
                    realm.insertOrUpdate(photo);
                }
            });
        } finally {

        }
    }
    public static void uploaded(Realm realm, final Photo photo) {
        // increment index

        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    Photo xxx = new Photo();
                    xxx = photo;
                    xxx.setUploaded(true);
                    xxx.setStatus_photo("1");

                    realm.copyToRealmOrUpdate(xxx);
                    Log.d(TAG, "onResHistory uploaded update: photo.java");
                }
            });
        } finally {

        }
    }

    public static void updatePath(Realm realm, final Photo photo, final String wp_id, final String activity_id, final String curent_date, final String time_now, final String path, final boolean uploaded, final boolean is_online, final int inputProgress_tx) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    Photo photoPath = new Photo();
                    photoPath = photo;
//                    if(is_online==true){
//                        photoPath.setUploaded(true);
//                    }
//                    if(is_online==false){
//                        photoPath.setUploaded(false);
//                    }
                    Log.d(TAG, "photoPath: photo.java" + uploaded + "/n" + photoPath.getId());
//                  OfflineDataTransaction dataLocal = realm.where(OfflineDataTransaction.class)
//                            .equalTo("project_id", photoPath.getProjectid())
//                            .equalTo("wp_id", wp_id)
//                            .equalTo("activity_id", activity_id)
//                            .equalTo("progrees", String.valueOf(inputProgress_tx))
//                            .findFirst();
//                    WorkPkg workPkg = realm.where(WorkPkg.class)
//                            .equalTo("wp_id", wp_id)
//                            .findFirst();
                    photoPath.setUploaded(true);
                    photoPath.setWp_id(wp_id);
//                    photoPath.setProjectid(workPkg.getProjectid());
                    photoPath.setActivity_id(activity_id);
                    photoPath.setStatus_photo("1");
                    photoPath.setTime_photo(time_now);
                    photoPath.setDate_photo(curent_date);
                    if (inputProgress_tx != 0) {
                        photoPath.setProgress_photo(inputProgress_tx);
                    }
                    photoPath.setPath(path);
                    realm.copyToRealmOrUpdate(photoPath);
//                    realm.insertOrUpdate(photoPath);
//                    realm.commitTransaction();
//                    realm.close();
                    Log.d(TAG, "photoPath: photo.java");
                }
            });
        } finally {

        }
    }

    public static void updatePathOff(Realm realm, final Photo photo, final String wp_id, final String activity_id, final String curent_date, final String time_now, final String path, final boolean uploaded, final boolean is_online, final int inputProgress_tx) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    Photo photoPathOff = new Photo();
                    photoPathOff = photo;
//                    if(is_online==true){
//                        photoPath.setUploaded(true);
//                    }
//                    if(is_online==false){
//                        photoPath.setUploaded(false);
//                    }
//                    OfflineDataTransaction dataLocal = realm.where(OfflineDataTransaction.class)
//                            .equalTo("project_id", photoPathOff.getProjectid())
//                            .equalTo("wp_id", wp_id)
//                            .equalTo("activity_id", activity_id)
//                            .equalTo("progrees", String.valueOf(inputProgress_tx))
//                            .findFirst();
                    photoPathOff.setUploaded(false);
                    photoPathOff.setWp_id(wp_id);
                    photoPathOff.setActivity_id(activity_id);
                    photoPathOff.setStatus_photo("1");
                    photoPathOff.setTime_photo(time_now);
                    photoPathOff.setDate_photo(curent_date);
                    if (inputProgress_tx != 0) {
                        photoPathOff.setProgress_photo(inputProgress_tx);
                    }
                    photoPathOff.setPath(path);
                    realm.copyToRealmOrUpdate(photoPathOff);
//                    realm.insertOrUpdate(photoPathOff);
//                    realm.commitTransaction();
//                    realm.close();
                    Log.d(TAG, "onResHistory photoPath update: photo.java");
                }
            });
        } finally {

        }
    }

    public static void offlinePhoto(Realm realm, final Photo photo, final String curent_date, final String time_now, final int progress) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    //photo.setUploaded(true);
                    Photo offlinePath = new Photo();
                    offlinePath = photo;
                    offlinePath.setStatus_photo("1");
                    offlinePath.setTime_photo(time_now);
                    offlinePath.setDate_photo(curent_date);
                    offlinePath.setProgress_photo(progress);
                    realm.copyToRealmOrUpdate(offlinePath);
//                    realm.insertOrUpdate(offlinePath);
//                    realm.commitTransaction();
//                    realm.close();
                    Log.d(TAG, "onResHistory photoPath update: photo.java");
                }
            });
        } finally {

        }
    }

//    public static void uploadedOff(Realm realm, final Photo photo, final String projectid,
//                                   final String wp_id, final String activity_id, final String datePhoto,
//                                   final String timePhoto) {
//        // increment index
//
//        if (realm.isClosed())
//            realm = Realm.getDefaultInstance();
//        try {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    //photo.setUploaded(true);
//                    Photo updatePhotoOff = new Photo();
//                    updatePhotoOff = photo;
//                    updatePhotoOff.setUploaded(true);
//                    updatePhotoOff.setProjectid(projectid);
//                    updatePhotoOff.setWp_id(wp_id);
//                    updatePhotoOff.setActivity_id(activity_id);
//                    updatePhotoOff.setDate_photo(datePhoto);
//                    updatePhotoOff.setTime_photo(timePhoto);
//                    updatePhotoOff.setStatus_photo("1");
//
//                    realm.copyToRealmOrUpdate(updatePhotoOff);
//                    Log.d(TAG, "onResHistory uploadedOff update: photo.java");
//                }
//            });
//        } finally {
//
//        }
//    }

    public static void newPhoto(Realm realm, final String sprojectid, final String swp_id, final String sactivity_id) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Photo photo = new Photo();
                    photo.setProjectid(sprojectid);
//                    photo.setWp_id(swp_id);
//                    photo.setActivity_id(sactivity_id);
                    photo.setId(getNextID(realm));
                    realm.copyToRealmOrUpdate(photo); // using insert API
                }
            });
        } finally {

        }

    }

    public static void delete(Realm realm, final Photo photo) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    photo.deleteFromRealm();
                }
            });
        } finally {

        }

    }

    public static int getNextID(Realm realm) {
        Number currentIdNum = realm.where(Photo.class).max(Photo.ID);
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        Log.d(TAG, "getNextID:" + nextId);
        return nextId;
    }

    public static int getstringid(String path) {
        File file = new File(path);
        String fname = file.getName();

        String first = fname.split("-")[0];

        Log.d(TAG, "getstringProjectId: " + first);
        String id = first.split("_")[2];
        Log.d(TAG, "getstringProjectId: " + id);
        return Integer.parseInt(id);
    }

    public static String getstringProjectId(String path) {
        File file = new File(path);
        String fname = file.getName();

        String first = fname.split("-")[0];

        Log.d(TAG, "getstringProjectId: " + first);
        String id = first.split("_")[0];
        Log.d(TAG, "getstringProjectId: " + id);
        return id;
    }


}
