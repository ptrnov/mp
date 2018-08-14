package com.cudo.mproject.Model;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class DataPenjagaLahan extends RealmObject {
    static String TAG = PhotoPerson.class.getSimpleName();
    public static String idPenjagaLahan = "idPenjagaLahan";
    @PrimaryKey
    int idPenjagaLahanOffline;
    private String userName;//teknisi
    private String userId;
    private String dateSubmit;
    private String timeSubmit;
    private String project_id;
    //
    private String namaPenjagaLahan;
    private String alamat;
    private String provinsi;
    private String kabupaten;
    private String kecamatan;
    private String noKtp;
    private String noTlp;
    private String status_approval;
    private String is_submited;

    public int getIdPenjagaLahanOffline() {
        return idPenjagaLahanOffline;
    }

    public void setIdPenjagaLahanOffline(int idPenjagaLahanOffline) {
        this.idPenjagaLahanOffline = idPenjagaLahanOffline;
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

    public String getNamaPenjagaLahan() {
        return namaPenjagaLahan;
    }

    public void setNamaPenjagaLahan(String namaPenjagaLahan) {
        this.namaPenjagaLahan = namaPenjagaLahan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getNoTlp() {
        return noTlp;
    }

    public void setNoTlp(String noTlp) {
        this.noTlp = noTlp;
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

    public static void insertDataPenjagaLahan(Realm realm, final DataPenjagaLahan dataPenjagaLahan, final int nextId) {

    }

    public static void deleteDataPenjagaLahan(Realm realm, final int idPenjagaLahan) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<DataPenjagaLahan> result = realm.where(DataPenjagaLahan.class)
                            .equalTo("idPenjagaLahan", idPenjagaLahan)
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
            List<DataPenjagaLahan> list;
            if (realm.isClosed())
                realm = Realm.getDefaultInstance();
            RealmResults<DataPenjagaLahan> asd = realm.where(DataPenjagaLahan.class)
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


