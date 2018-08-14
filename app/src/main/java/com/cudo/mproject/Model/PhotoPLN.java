package com.cudo.mproject.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PhotoPLN extends RealmObject {
    static String TAG = Photo.class.getSimpleName();
    public static String ID = "id";
    public static String PROJECT_ID = "projectid";
    public static String UPLOADED = "uploaded";
    @PrimaryKey
    int id;
    String projectid;
    private String idPLN;
    private String userName;
    private String userId;
    private String dateSubmit;
    private String timeSubmit;
    private String pathPhotoPLN;
    private boolean uploaded;
    private String status_photo;
    private String jenis_photo="photoPLN";
    private String description;

    public int getId() {
        return id;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdPLN() {
        return idPLN;
    }

    public void setIdPLN(String idPLN) {
        this.idPLN = idPLN;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPathPhotoPLN() {
        return pathPhotoPLN;
    }

    public void setPathPhotoPLN(String pathPhotoPLN) {
        this.pathPhotoPLN = pathPhotoPLN;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getStatus_photo() {
        return status_photo;
    }

    public void setStatus_photo(String status_photo) {
        this.status_photo = status_photo;
    }

    public String getJenis_photo() {
        return jenis_photo;
    }

    public void setJenis_photo(String jenis_photo) {
        this.jenis_photo = jenis_photo;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void updateType(Realm realm, final PhotoPLN photoPLN, final String description, final String extFile) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    PhotoPLN updatePhoto = new PhotoPLN();
                    updatePhoto = photoPLN;
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
                    realm.insertOrUpdate(photoPLN);
                }
            });
        } finally {

        }
    }
    public static void uploaded(Realm realm, final PhotoPLN photoPLN) {
        // increment index

        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    PhotoPLN xxx = new PhotoPLN();
                    xxx = photoPLN;
                    xxx.setUploaded(true);
                    xxx.setStatus_photo("1");

                    realm.copyToRealmOrUpdate(xxx);
                    Log.d(TAG, "onResHistory uploaded update: photoPerson.java");
                }
            });
        } finally {

        }
    }
    public static void updatePath(Realm realm, final PhotoPLN photoPLN,  final String curent_date, final String time_now, final String path, final boolean uploaded) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    PhotoPLN photoPath = new PhotoPLN();
                    photoPath = photoPLN;
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

                    photoPath.setStatus_photo("1");
                    photoPath.setTimeSubmit(time_now);
                    photoPath.setDateSubmit(curent_date);
//                    if (inputProgress_tx != 0) {
//                        photoPath.setProgress_photo(inputProgress_tx);
//                    }
                    photoPath.setPathPhotoPLN(path);
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
    public static void updatePathOff(Realm realm, final PhotoPLN photoPLN, final String curent_date, final String time_now, final String path, final boolean uploaded, final boolean is_online) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //photo.setUploaded(true);
                    PhotoPLN photoPathOff = new PhotoPLN();
                    photoPathOff = photoPLN;
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
                    photoPathOff.setStatus_photo("1");
                    photoPathOff.setTimeSubmit(time_now);
                    photoPathOff.setDateSubmit(curent_date);
                    photoPathOff.setPathPhotoPLN(path);
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
    public static void offlinePhoto(Realm realm, final PhotoPLN photoPLN, final String curent_date, final String time_now, final int progress) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    //photo.setUploaded(true);
                    PhotoPLN offlinePath = new PhotoPLN();
                    offlinePath = photoPLN;
                    offlinePath.setStatus_photo("1");
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
    public static void updatePhoto(Realm realm, final PhotoPLN photoPLN) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(photoPLN); // using insert API
                }
            });
        } finally {

        }
    }
    public static void delete(Realm realm, final PhotoPLN photoPLN) {
        // increment index
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    photoPLN.deleteFromRealm();
                }
            });
        } finally {

        }

    }
    public static int getNextID(Realm realm) {
        Number currentIdNum = realm.where(PhotoPLN.class).max(PhotoPLN.ID);
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
