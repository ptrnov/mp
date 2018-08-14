package com.cudo.mproject.Model;

import android.util.Log;

import com.cudo.mproject.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Created by newbiecihuy on 12/13/2017.
 */

public class OfflineDataTransaction extends RealmObject {
    static String TAG = Photo.class.getSimpleName();
    public static String idOffline = "id_offline_transaction";
    @PrimaryKey
    private int id_offline_transaction;
    private String date_offline;
    private String time_offline;
    //    private boolean is_offline;
    private String status_project_offline;
    private String wp_id;
    private String wp_name;
    private String project_id;
    private String site_name;
    private String activity_id;
    private String activity_name;
    private String photo_path;
    private String progrees;
    private String pa_id;
    private String actualStartDate;
    private String activity_plan_start;
    private String activity_plan_finish;
    private String sow;
    private String namaTenant;
    private String area;
    private String regional;
    private String userName;
    private String userId;
//    private String actualTimeStartDate;

//    public String getId_offline_transaction() {
//        return id_offline_transaction;
//    }
//
//    public void setId_offline_transaction(String id_offline_transaction) {
//        this.id_offline_transaction = id_offline_transaction;
//    }


//    public OfflineDataTransaction(int id_offline_transaction, String date_offline, boolean is_offline, String wp_id, String wp_name, String project_id, String site_name, String activity_id, String activity_name, String photo_path) {
//        this.id_offline_transaction = id_offline_transaction;
//        this.date_offline = date_offline;
//        this.is_offline = is_offline;
//        this.wp_id = wp_id;
//        this.wp_name = wp_name;
//        this.project_id = project_id;
//        this.site_name = site_name;
//        this.activity_id = activity_id;
//        this.activity_name = activity_name;
//        this.photo_path = photo_path;
//    }


    public static String getIdOffline() {
        return idOffline;
    }

    public static void setIdOffline(String idOffline) {
        OfflineDataTransaction.idOffline = idOffline;
    }

    public int getId_offline_transaction() {
        return id_offline_transaction;
    }

    public void setId_offline_transaction(int id_offline_transaction) {
        this.id_offline_transaction = id_offline_transaction;
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

    public String getWp_id() {
        return wp_id;
    }

    public void setWp_id(String wp_id) {
        this.wp_id = wp_id;
    }

    public String getWp_name() {
        return wp_name;
    }

    public void setWp_name(String wp_name) {
        this.wp_name = wp_name;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getProgrees() {
        return progrees;
    }

    public void setProgrees(String progrees) {
        this.progrees = progrees;
    }

    public String getPa_id() {
        return pa_id;
    }

    public void setPa_id(String pa_id) {
        this.pa_id = pa_id;
    }

    public String getActivity_plan_start() {
        return activity_plan_start;
    }

    public void setActivity_plan_start(String activity_plan_start) {
        this.activity_plan_start = activity_plan_start;
    }

    public String getActivity_plan_finish() {
        return activity_plan_finish;
    }

    public void setActivity_plan_finish(String activity_plan_finish) {
        this.activity_plan_finish = activity_plan_finish;
    }

    public String getSow() {
        return sow;
    }

    public void setSow(String sow) {
        this.sow = sow;
    }

    public String getNamaTenant() {
        return namaTenant;
    }

    public void setNamaTenant(String namaTenant) {
        this.namaTenant = namaTenant;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public String getDate_offline() {
        return date_offline;
    }

    public void setDate_offline(String date_offline) {
        this.date_offline = date_offline;
    }

    public String getTime_offline() {
        return time_offline;
    }

    public void setTime_offline(String time_offline) {
        this.time_offline = time_offline;
    }

    public String getStatus_project_offline() {
        return status_project_offline;
    }

    public void setStatus_project_offline(String status_project_offline) {
        this.status_project_offline = status_project_offline;
        /*
          0. pending
          1. submit to server
          2. project approval
          3. project Reject
         */
    }
    public String getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(String actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    // public void updateOffline(Realm realm, final OfflineDataTransaction localData) {
//        if (realm.isClosed())
//            realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealmOrUpdate(localData);
//            }
//        });
//    }
    public static void insertOfflineData(Realm realm, final OfflineDataTransaction dataOffline, final int nextId, final String projectid, final String progress, final String wpId, final String wpName, final String activityId, final String activityName, final boolean is_online, final String sitename, final String area,
                                         final String sow, final String regional, final String namaTenant, final String statusProjectOff,
                                         final String curent_date, final String time_now, final String userName, final String planStart, final String planFinish) {//, String isitglActual
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
//                  User user = realm.where(User.class).findFirst();
                    dataOffline.setId_offline_transaction(nextId);
                    dataOffline.setProject_id(projectid);
                    dataOffline.setProgrees(progress);
                    dataOffline.setWp_id(wpId);
                    dataOffline.setWp_name(wpName);
                    dataOffline.setActivity_id(activityId);//dpt_id
                    dataOffline.setActivity_name(activityName);//
                    dataOffline.setSite_name(sitename);
                    dataOffline.setArea(area);
                    dataOffline.setSow(sow);
                    dataOffline.setRegional(regional);
                    dataOffline.setNamaTenant(namaTenant);
                    dataOffline.setStatus_project_offline(statusProjectOff);
                    dataOffline.setDate_offline(curent_date);
                    dataOffline.setTime_offline(time_now);
                    dataOffline.setUserName(userName);
                    dataOffline.setPhoto_path("com.cudo.mproject/save_dir/");
                    dataOffline.setActivity_plan_start(planStart);
                    dataOffline.setActivity_plan_finish(planFinish);

//                    realm.insertOrUpdate(dataOffline); // using insert API
                    realm.copyToRealmOrUpdate(dataOffline); // using insert API
//                    realm.commitTransaction();
//                    realm.close();
//                    realm.commitTransaction();
                }
            });
        } finally {

        }
    }

    //
    public static void deleteOfflineData(Realm realm, final int id_offline_transaction) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<OfflineDataTransaction> result = realm.where(OfflineDataTransaction.class)
                            .equalTo("id_offline_transaction", id_offline_transaction)
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

    public static void updateStatus(Realm realm, final String idDataLocal) {
        // increment index

        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat timeF = new SimpleDateFormat(" hh:mm:ss");
                    Date now = new Date();

                    String curent_date = sdf.format(now);
                    String time_now = timeF.format(now);
//
//                OfflineDataTransaction updateData = new OfflineDataTransaction();
//
//                updateData = dataLocal;
//                updateData.setStatus_project_offline("1");
//                updateData.setStatus_project_offline(curent_date);
//                updateData.setTime_offline(time_now);

                    OfflineDataTransaction updateData = realm.where(OfflineDataTransaction.class)
                            .equalTo(OfflineDataTransaction.idOffline, Integer.parseInt(idDataLocal))
                            .findFirst();
                    User user = realm.where(User.class).findFirst();
                    WorkPkg workPkg = realm.where(WorkPkg.class)
                            .equalTo("wp_id", updateData.getWp_id())
                            .findFirst();
//
                    Project mproject = realm.where(Project.class)
                            .equalTo("project_id", updateData.getProject_id())
                            .findFirst();
//
//                    if(updateData.getActivity_id()== null){
//                        updateData.setActivity_id("");
//                    }
                    MActivity addMA = realm.where(MActivity.class)
                            .equalTo("activity_id", updateData.getActivity_id())
                            .findFirst();
//
                    if (updateData.getActivity_name() == null) {
                        updateData.setActivity_name(addMA.getActivity_name());
                    }
                    if (updateData.getUserName() == null) {
                        updateData.setUserName(user.getUsername());
                    }
                    if (updateData.getWp_name() == null) {
                        updateData.setWp_name(workPkg.getWp_name());
                    }
                    if (updateData.getProject_id() == null) {
                        updateData.setProject_id(updateData.getProject_id());
                    }
                    if (updateData.getSow() == null) {
                        updateData.setSow(mproject.getSow());
                    }
                    if (updateData.getNamaTenant() == null) {
                        updateData.setNamaTenant(mproject.getNama_tenant());
                    }
                    if (updateData.getArea() == null) {
                        updateData.setArea(mproject.getArea());
                    }
                    if (updateData.getSite_name() == null) {
                        updateData.setSite_name(mproject.getSite_name_actual());
                    }
                    if (updateData.getRegional() == null) {
                        updateData.setRegional(mproject.getRegional());
                    }
                    updateData.setStatus_project_offline("1");
                    updateData.setPhoto_path("com.cudo.mproject/"+ mproject.getProject_id() + "_" + "save_dir/");
                    realm.copyToRealmOrUpdate(updateData);
//                    realm.commitTransaction();
//                    realm.close();
                }
            });
        } finally {

        }
    }

//    public static int getNextID(Realm realm) {
//        Number currentIdNum = realm.where(OfflineDataTransaction.class).max(OfflineDataTransaction.idOffline);
//        int nextId;
//        if (currentIdNum == null) {
//            nextId = 1;
//        } else {
//            nextId = currentIdNum.intValue() + 1;
//        }
//        Log.d(TAG, "getNextID:" + nextId);
//        return nextId;
//    }

    public static int cekDataOffline(Realm realm) {
        User user = realm.where(User.class).findFirst();
        int isi_dataOff = 0;
        if (user != null) {
            List<OfflineDataTransaction> list;
            if (realm.isClosed())
                realm = Realm.getDefaultInstance();
            RealmResults<OfflineDataTransaction> asd = realm.where(OfflineDataTransaction.class)
                    .equalTo("status_project_offline", "0")
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
