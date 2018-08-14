package com.cudo.mproject.Model;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class DataPLNSite extends RealmObject {
    static String TAG = Photo.class.getSimpleName();
    public static String idDataPLN = "idPLN";
    @PrimaryKey
    private int idPLN;
    private String userName;
    private String userId;
    private String dateSubmit;
    private String timeSubmit;
    private String project_id;
    //
    private String idPelanggan;
    private String namaPelanggan;
    private String daya;
    private String is_submited;

    public int getIdPLN() {
        return idPLN;
    }

    public void setIdPLN(int idPLN) {
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

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getDaya() {
        return daya;
    }

    public void setDaya(String daya) {
        this.daya = daya;
    }

    public String getIs_submited() {
        return is_submited;
    }

    public void setIs_submited(String is_submited) {
        this.is_submited = is_submited;
    }

    public static void insertDataPLNSite(Realm realm, final DataPLNSite dataPLNSite, final int nextId) {

    }

    public static int cekIdPelanggan(Realm realm, final String idPelanggan) {
        int isi_data = 0;
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();

        List<DataPLNSite> list;
        RealmResults<DataPLNSite> asd = realm.where(DataPLNSite.class)
                .equalTo("idPelanggan", idPelanggan)
                .findAll();
        list = realm.copyFromRealm(asd);
        isi_data = list.size();
        Log.d(TAG, "isi_data" + isi_data);
        return isi_data;
    }


    public static void deleteDataPLNSite(Realm realm, final int idPLN) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<DataPLNSite> result = realm.where(DataPLNSite.class)
                            .equalTo("idPLN", idPLN)
                            .findAll();
                    result.deleteAllFromRealm();
                }
            });
        } finally {

        }
    }

    public static int cekDataOffline(Realm realm) {
        User user = realm.where(User.class).findFirst();
        int isi_dataOff = 0;
        if (user != null) {
            List<DataPLNSite> list;
            if (realm.isClosed())
                realm = Realm.getDefaultInstance();
            RealmResults<DataPLNSite> asd = realm.where(DataPLNSite.class)
                    .equalTo("is_submited", "0")
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
