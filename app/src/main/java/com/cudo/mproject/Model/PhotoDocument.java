package com.cudo.mproject.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 3/21/2018.
 */

public class PhotoDocument extends RealmObject {
    public static String ID = "id";
    static String TAG = PhotoDocument.class.getSimpleName();
    @PrimaryKey
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String dateSubmit;
    private String timeSubmit;
    private String path = "";
    private boolean uploaded;
    @Getter
    @Setter
    private String statusPhoto = "";
    private String description;

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        PhotoDocument.ID = ID;
    }

    public static String getTAG() {
        return TAG;
    }

    public static void setTAG(String TAG) {
        PhotoDocument.TAG = TAG;
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

    public String getDateSubmit() {
        return dateSubmit;
    }

    public void setDateSubmit(String dateSubmit) {
        this.dateSubmit = dateSubmit;
    }

    public String getTimeSubmit() {
        return timeSubmit;
    }

    public void setTimeSubmit(String timeSubmit) {
        this.timeSubmit = timeSubmit;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getStatusPhoto() {
        return statusPhoto;
    }

    public void setStatusPhoto(String statusPhoto) {
        this.statusPhoto = statusPhoto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void updateType(Realm realm, final PhotoDocument photoDocument, final String description, final String extFile) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    PhotoDocument updatePhoto = new PhotoDocument();
                    updatePhoto = photoDocument;
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
                    realm.insertOrUpdate(photoDocument);
                }
            });
        } finally {

        }
    }
    public static void uploaded(Realm realm, final PhotoDocument photoDocument) {
        // increment index

        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    PhotoDocument xxx = new PhotoDocument();
                    xxx = photoDocument;
                    xxx.setUploaded(true);
                    xxx.setStatusPhoto("1");

                    realm.copyToRealmOrUpdate(xxx);
                    Log.d(TAG, "onResHistory uploaded update: photoPerson.java");
                }
            });
        } finally {

        }
    }
    public static void updatePath(Realm realm, final PhotoDocument photoDocument,  final String curent_date, final String time_now, final String path, final boolean uploaded) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    PhotoDocument photoPath = new PhotoDocument();
                    photoPath = photoDocument;
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

                    photoPath.setStatusPhoto("1");
                    photoPath.setTimeSubmit(time_now);
                    photoPath.setDateSubmit(curent_date);
//                    if (inputProgress_tx != 0) {
//                        photoPath.setProgress_photo(inputProgress_tx);
//                    }
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
    public static void updatePathOff(Realm realm, final PhotoDocument photoDocument, final String curent_date, final String time_now, final String path, final boolean uploaded, final boolean is_online) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    PhotoDocument photoPathOff = new PhotoDocument();
                    photoPathOff = photoDocument;
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
                    photoPathOff.setStatusPhoto("1");
                    photoPathOff.setTimeSubmit(time_now);
                    photoPathOff.setDateSubmit(curent_date);
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
    public static void offlinePhoto(Realm realm, final PhotoDocument photoDocument, final String curent_date, final String time_now, final int progress) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    //photo.setUploaded(true);
                    PhotoDocument offlinePath = new PhotoDocument();
                    offlinePath = photoDocument;
                    offlinePath.setStatusPhoto("1");
                    offlinePath.setTimeSubmit(time_now);
                    offlinePath.setDateSubmit(curent_date);
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
    public static void updatePhoto(Realm realm, final PhotoDocument photoDocument) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(photoDocument); // using insert API
                }
            });
        } finally {

        }
    }
    public static void delete(Realm realm, final PhotoDocument photoDocument) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    photoDocument.deleteFromRealm();
                }
            });
        } finally {

        }

    }
    public static int getNextID(Realm realm) {
        Number currentIdNum = realm.where(PhotoDocument.class).max(PhotoDocument.ID);
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
}

