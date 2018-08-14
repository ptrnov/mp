package com.cudo.mproject.Model;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 */

public class MActivity extends RealmObject {

    public static String WPID = "wp_id";
    public static String activity_ID = "activity_id";
    @PrimaryKey
    @Getter
    @Setter
    String activity_id;
    @Getter
    @Setter
    String wbs1_id;
    @Getter
    @Setter
    String wbs_id;
    @Getter
    @Setter
    String wp_id;
    @Getter
    @Setter
    String activity_plan_date;
    @Getter
    @Setter
    String dpt_id;
    @Getter
    @Setter
    String activity_name;
    @Getter
    @Setter
    String duration;
    @Getter
    @Setter
    String activity_desc;
    @Getter
    @Setter
    String last_update;
    @Getter
    @Setter
    String lead_time;
    @Getter
    @Setter
    String pf_id;
    @Getter
    @Setter
    String cost_refference;
    @Getter
    @Setter
    String cost_plan;
    @Getter
    @Setter
    String material_id;
    @Getter
    @Setter
    String form_id;
    @Getter
    @Setter
    String user_id;
    @Getter
    @Setter
    String activity_code;
    @Getter
    @Setter
    String wg_id;
    @Getter
    @Setter
    String ga_id;
    @Getter
    @Setter
    String gfa_id;
    @Getter
    @Setter
    String at_id;
    @Getter
    @Setter
    String activity_plan_start;
    @Getter
    @Setter
    String activity_plan_finish;
    @Getter
    @Setter
    boolean is_active;
    @Getter
    @Setter
    int progress_activity;
    @Getter
    @Setter
   private int as_id;

    @Override
    public String toString() {

        return activity_name;
    }

    public String getActivity_name() {
        return activity_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MActivity) {
            MActivity c = (MActivity) obj;
            if (c.getActivity_name().equals(activity_name) && c.getDpt_id() == dpt_id) return true;
//            if (c.getActivity_name().equals(activity_name) && c.getActivity_id() == activity_id) return true;
        }
        return false;
    }

    //public static void updateWpkgData(Realm realm, final List<MActivity> workPkgs) {
    public static void updateActivityData(Realm realm,  final List<MActivity> activities) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
//                    realm.copyToRealmOrUpdate(workPkgs);
//                    realm.commitTransaction();
                    realm.copyToRealmOrUpdate(activities);
                }
            });
        } finally {

        }
    }
    public static void updateProgress(Realm realm, final int progress, final String activity_id) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        final MActivity ma = realm.where(MActivity.class).equalTo(activity_ID, activity_id).findFirst();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    ma.setProgress_activity(progress);
                    ma.setIs_active(true);
                }
            });
        } finally {

        }
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public int getProgress_activity() {
        return progress_activity;
    }

    public void setProgress_activity(int progress_activity) {
        this.progress_activity = progress_activity;
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

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getDpt_id() {
        return dpt_id;
    }

    public void setDpt_id(String dpt_id) {
        this.dpt_id = dpt_id;
    }

    public int getAs_id() {
        return as_id;
    }

    public void setAs_id(int as_id) {
        this.as_id = as_id;
    }
}
