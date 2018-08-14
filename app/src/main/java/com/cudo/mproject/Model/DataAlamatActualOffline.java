package com.cudo.mproject.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import lombok.NonNull;

public class DataAlamatActualOffline  extends RealmObject {
    static String TAG = Photo.class.getSimpleName();
    public static String idAlamatActual = "idAlamatActualOffline";
    @PrimaryKey
    private int idAlamatActualOffline;
    private String userName;
    private String userId;
    private String dateSubmit;
    private String timeSubmit;
//
    private String project_id;
    private String siteName;
    private String alamat;
    private String provinsi;
    private String kabupaten;
    private String kecamatan;
    private String latitude;
    private String longitude;
    private String status_approval;
    private String status_alamat_actual_offline;
    //if 1 online -> submit to server
    // 0 -> pending

    public static String getIdAlamatActual() {
        return idAlamatActual;
    }

    public static void setIdAlamatActual(String idAlamatActual) {
        DataAlamatActualOffline.idAlamatActual = idAlamatActual;
    }

    public int getIdAlamatActualOffline() {
        return idAlamatActualOffline;
    }

    public void setIdAlamatActualOffline(int idAlamatActualOffline) {
        this.idAlamatActualOffline = idAlamatActualOffline;
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

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus_approval() {
        return status_approval;
    }

    public void setStatus_approval(String status_approval) {
        this.status_approval = status_approval;
    }

    public String getStatus_alamat_actual_offline() {
        return status_alamat_actual_offline;
    }

    public void setStatus_alamat_actual_offline(String status_alamat_actual_offline) {
        this.status_alamat_actual_offline = status_alamat_actual_offline;
    }

    public static void insertData(Realm realm, final DataAlamatActualOffline dataAlamatActualOffline, final int nextId){
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat timeF = new SimpleDateFormat(" hh:mm:ss");
                    Date now = new Date();
                    String curent_date = sdf.format(now);
                    String time_now = timeF.format(now);
//
                    dataAlamatActualOffline.setIdAlamatActualOffline(nextId);


                    realm.copyToRealmOrUpdate(dataAlamatActualOffline); // using insert API
//                    realm.commitTransaction();
//                    realm.close();
//                    realm.commitTransaction();
                }
            });
        } finally {

        }
    }


    //
    public static void deleteOfflineData(Realm realm, final int idAlamatActualOffline) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<DataAlamatActualOffline> result = realm.where(DataAlamatActualOffline.class)
                            .equalTo("idAlamatActualOffline", idAlamatActualOffline)
                            .findAll();
                    result.deleteAllFromRealm();
                }
            });
        } finally {

        }
//        try {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    RealmResults<OfflineDataTransaction> result = realm.where(OfflineDataTransaction.class)
//                            .equalTo("project_id",projectID)
//                            .equalTo("wp_id",projectID)
//                            .equalTo("activity_id",activity_id)
//                            .equalTo("progrees",progrees)
//                            .findAll();
//                    result.deleteAllFromRealm();
//                }
//            });
//        } finally {
//
//        }
    }
    public static int cekDataOffline(Realm realm) {
        User user = realm.where(User.class).findFirst();
        int isi_dataOff = 0;
        if (user != null) {
            List<DataAlamatActualOffline> list;
            if (realm.isClosed())
                realm = Realm.getDefaultInstance();
            RealmResults<DataAlamatActualOffline> asd = realm.where(DataAlamatActualOffline.class)
                    .equalTo("status_alamat_actual_offline", "0")
                    .equalTo("userName", user.getUsername())
                    .findAll();
            list = realm.copyFromRealm(asd);
            isi_dataOff = list.size();
            Log.d(TAG, "isi_dataOff" + isi_dataOff);
//        if (isi_dataOff > 0) {
//            return isi_dataOff;
//        }
//            return isi_dataOff;
        }
        return isi_dataOff;
    }

}
