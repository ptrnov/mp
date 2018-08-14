package com.cudo.mproject.Model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 */

public class Project extends RealmObject {
    public static String PROJECT_ID = "project_id";
    public static String PA_ID = "pa_id";
    /*"project_id": "16TL12B0118",
            "id": "90",
            "site_name": "DMT Repeater Indomba",
            "nama_tenant": "TELKOM",
            "area": "A4",
            "regional": "SULMAPUA",
            "flag": "1",
            "wo_id": "139",
            "team_id": "28",
            "release_status": "1",
            "description": "IP Long Haul",
            "lat": "137.32639",
            "long": "-1.69581",
            "radius": "100",
            "progress_status": "RFI",
            "progress_overall": "0",
            "site_id_tower": null,
            "funclock1": null,
            "funclock2": null,
            "customer": null,
            "tower_id": null,
            "tower_no": null,
*/
    @Getter
    @Setter
    @PrimaryKey
    String project_id;
    @Getter
    @Setter
    String pa_id = "";
    @Getter
    @Setter
    String id;
    @Getter
    @Setter
    String site_name;
    @Getter
    @Setter
    String nama_tenant;
    @Getter
    @Setter
    String area;
    @Getter
    @Setter
    String regional;
    @Getter
    @Setter
    String sow;
    @Getter
    @Setter
    String template;
    @Getter
    @Setter
    String flag;
    @Getter
    @Setter
    String wo_id;
    @Getter
    @Setter
    String team_id;
    @Getter
    @Setter
    String release_status;
    @Getter
    @Setter
    String description;
    @Getter
    @Setter
    String radius;
    @Getter
    @Setter
    String progress_status;
    @Getter
    @Setter
    String progress_overall;
    @Getter
    @Setter
    String site_id_tower;
    @Getter
    @Setter
    String funclock1;
    @Getter
    @Setter
    String funclock2;
    @Getter
    @Setter
    String customer;
    @Getter
    @Setter
    String tower_id;
    @Getter
    @Setter
    String tower_no;
    @Getter
    @Setter
    String program_code;
    @Getter
    @Setter
    String alamat;
    @Getter
    @Setter
    String pf_code;
    @Getter
    @Setter
    String plan_date_acrue;
    @Getter
    @Setter
    String actual_date;
    @Getter
    @Setter
    String tipe_revenue;

    @Getter
    @Setter
    String tower_type;
    @Getter
    @Setter
    String tower_tinggi;
    @Getter
    @Setter
    String sub_program_code;
    @Getter
    @Setter
    String site_id_tenant;
    @Getter
    @Setter
    String site_name_tenant;
    //    @Getter
//    @Setter
//    String siten_name_actual;
    @Getter
    @Setter
    String target_rfi;
    @Getter
    @Setter
    String rfi;
    @Getter
    @Setter
    String alamat_site_jl;
    @Getter
    @Setter
    String alamat_site_desa;
    @Getter
    @Setter
    String alamat_site_kecamatan;
    @Getter
    @Setter
    String alamat_site_kota;
    @Getter
    @Setter
    String alamat_site_kab;
    @Getter
    @Setter
    String alamat_site_prov;
    @Getter
    @Setter
    String alamat_site_zipcode;
    @Getter
    @Setter
    String flag_schedule;
    @Getter
    @Setter
    String flag_resource;
    @Getter
    @Setter
    String plan_revenue;
    @Getter
    @Setter
    String guid_id;
    @Getter
    @Setter
    String RFI_APV;
    @Getter
    @Setter
    @SerializedName("long")
    double lng;
    @Getter
    @Setter
    @SerializedName("lat")
    double lat;
    @Getter
    @Setter
    int progress = 0;
    @Getter
    @Setter
    private String alamat_actual;
    @Getter
    @Setter
    private String lat_actual;
    @Getter
    @Setter
    private String long_actual;
    @Getter
    @Setter
    private String actual_revenue;
    @Getter
    @Setter
    private String site_name_actual;
    //
    @Getter
    @Setter
    WorkPkg workPkg;
    @Getter
    @Setter
    MActivity mActivity;
//    @Getter
//    @Setter
//    private OfflineDataTransaction offTransaction;
//    @Getter
//    @Setter
//    String activity_plan_start;
//    @Getter
//    @Setter
//    String activity_plan_finish;
    @Getter
    @Setter
    private String actualStartDate;


    public void update(Realm realm, final Project project) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(project);
            }
        });
    }


    public static void deletePAID(Realm realm, String project_id, final String paid) {
        final Project m = realm.where(Project.class).equalTo(PROJECT_ID, project_id).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                m.setPa_id(paid);
            }
        });

    }
//    public String getPa_id() {
//        return pa_id;
//    }
//
//    public void setPa_id(String paid) {
//        this.pa_id = paid;
//    }

    public void updateWorkPkg(Realm realm, final WorkPkg project) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    setWorkPkg(project);
                }
            });
        } finally {

        }
    }

    public void updateActivity(Realm realm, final MActivity project) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    setMActivity(project);
                }
            });
        } finally {

        }
    }

    //    public static void updateProgress(Realm realm, final int progress) {
    public static void updateProgress(Realm realm, final int progress, String project_id) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        final Project m = realm.where(Project.class).equalTo(PROJECT_ID, project_id).findFirst();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    m.setProgress(progress);
                }
            });
        } finally {

        }
    }

    public static void updatePAID(Realm realm, String project_id, final String paid) {
        final Project m = realm.where(Project.class).equalTo(PROJECT_ID, project_id).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                m.setPa_id(paid);
            }
        });
    }

    public String getAlamat_actual() {
        return alamat_actual;
    }

    public void setAlamat_actual(String alamat_actual) {
        this.alamat_actual = alamat_actual;
    }

    public String getLat_actual() {
        return lat_actual;
    }

    public void setLat_actual(String lat_actual) {
        this.lat_actual = lat_actual;
    }

    public String getLong_actual() {
        return long_actual;
    }

    public void setLong_actual(String long_actual) {
        this.long_actual = long_actual;
    }

    public String getActual_revenue() {
        return actual_revenue;
    }

    public void setActual_revenue(String actual_revenue) {
        this.actual_revenue = actual_revenue;
    }

    public String getSite_name_actual() {
        return site_name_actual;
    }

    public void setSite_name_actual(String site_name_actual) {
        this.site_name_actual = site_name_actual;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public static String getProjectId() {
        return PROJECT_ID;
    }

    public static void setProjectId(String projectId) {
        PROJECT_ID = projectId;
    }

    public String getAlamat_site_kecamatan() {
        return alamat_site_kecamatan;
    }

    public void setAlamat_site_kecamatan(String alamat_site_kecamatan) {
        this.alamat_site_kecamatan = alamat_site_kecamatan;
    }

    public String getAlamat_site_kab() {
        return alamat_site_kab;
    }

    public void setAlamat_site_kab(String alamat_site_kab) {
        this.alamat_site_kab = alamat_site_kab;
    }

    public String getAlamat_site_prov() {
        return alamat_site_prov;
    }

    public void setAlamat_site_prov(String alamat_site_prov) {
        this.alamat_site_prov = alamat_site_prov;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(String actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSow() {
        return sow;
    }

    public void setSow(String sow) {
        this.sow = sow;
    }

    public String getNama_tenant() {
        return nama_tenant;
    }

    public void setNama_tenant(String nama_tenant) {
        this.nama_tenant = nama_tenant;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public String getPa_id() {
        return pa_id;
    }

    public void setPa_id(String pa_id) {
        this.pa_id = pa_id;
    }

    public WorkPkg getWorkPkg() {
        return workPkg;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
//    public MActivity getMActivity() {
//        return MActivity;
//    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

}
