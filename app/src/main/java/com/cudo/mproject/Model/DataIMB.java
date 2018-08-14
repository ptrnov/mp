package com.cudo.mproject.Model;

import android.util.Log;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import lombok.NonNull;

public class DataIMB extends RealmObject {
    static String TAG = Photo.class.getSimpleName();
    public static String idDataIMB = "idIMB";
    @PrimaryKey
    private int idIMB;
    private String userName;
    private String userId;
    private String dateSubmit;
    private String timeSubmit;
    private String nama_imb;
    private String no_imb;
    private String alamat_imb;
    private String tglMasaBerlakuAwal;
    private String tglMasaBerlakuAkhir;
    private String project_id;
    private String status_approval;
    private String is_submited;


    public int getIdIMB() {
        return idIMB;
    }

    public void setIdIMB(int idIMB) {
        this.idIMB = idIMB;
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

    public String getNama_imb() {
        return nama_imb;
    }

    public void setNama_imb(String nama_imb) {
        this.nama_imb = nama_imb;
    }

    public String getNo_imb() {
        return no_imb;
    }

    public void setNo_imb(String no_imb) {
        this.no_imb = no_imb;
    }

    public String getAlamat_imb() {
        return alamat_imb;
    }

    public void setAlamat_imb(String alamat_imb) {
        this.alamat_imb = alamat_imb;
    }

    public String getTglMasaBerlakuAwal() {
        return tglMasaBerlakuAwal;
    }

    public void setTglMasaBerlakuAwal(String tglMasaBerlakuAwal) {
        this.tglMasaBerlakuAwal = tglMasaBerlakuAwal;
    }

    public String getTglMasaBerlakuAkhir() {
        return tglMasaBerlakuAkhir;
    }

    public void setTglMasaBerlakuAkhir(String tglMasaBerlakuAkhir) {
        this.tglMasaBerlakuAkhir = tglMasaBerlakuAkhir;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getStatus_approval() {
        return status_approval;
    }

    public void setStatus_approval(String status_approval) {
        this.status_approval = status_approval;
    }

    public String getIs_submited() {
        return is_submited;
    }

    public void setIs_submited(String is_submited) {
        this.is_submited = is_submited;
    }

    public static void insertData(Realm realm, final DataIMB dataIMB, final int nextId) {
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
                }
            });
        } finally {

        }
    }

    //
    public static void deleteDataIMB(Realm realm, final int idIMB) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<DataIMB> result = realm.where(DataIMB.class)
                            .equalTo("idIMB", idIMB)
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
            List<DataIMB> list;
            if (realm.isClosed())
                realm = Realm.getDefaultInstance();
            RealmResults<DataIMB> asd = realm.where(DataIMB.class)
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

