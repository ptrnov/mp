package com.cudo.mproject.Model;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 * date             editing by        method                           description
 * 17/04/2018      newbiecihuy     add field wbs_id, wbs1_id
 */

public class WorkPkg extends RealmObject {

    public static String PROJECTID = "projectid";
    public static String WP_ID = "wp_id";
    @Getter
    @Setter
    @PrimaryKey
    String wp_id;
    @Getter
    @Setter
    String projectid;
    @Getter
    @Setter
    String wp_name;
    @Getter
    @Setter
    String wp_desc;
    @Getter
    @Setter
    String last_update;
    @Getter
    @Setter
    String group_id;
    @Getter
    @Setter
    String wp_code;
    @Getter
    @Setter
    String pf_id;
    @Getter
    @Setter
    String create_by;
    @Getter
    @Setter
    String cost_refference;
    @Getter
    @Setter
    String cost_plan;
    @Getter
    @Setter
    String wbs_id;
    @Setter
    String wbs1_id;
//    @Getter
//    @Setter
//    OfflineDataTransaction offTransaction;

    @Override
    public String toString() {
        return wp_name;
    }

    public String getWp_name() {
        return wp_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WorkPkg) {
            WorkPkg c = (WorkPkg) obj;
            if (c.getWp_name().equals(wp_name) && c.getWp_id() == wp_id) return true;
//            if (c.getCost_plan().equals(cost_plan) && c.getWp_id() == wp_id) return true;
        }

        return false;
    }

    public static void updateWpkgData(Realm realm, final List<WorkPkg> workPkgs) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.copyToRealmOrUpdate(workPkgs);
//                    realm.commitTransaction();
                }
            });
        } finally {

        }
    }

    public String getWp_id() {
        return wp_id;
    }

    public void setWp_id(String wp_id) {
        this.wp_id = wp_id;
    }

    public void setWp_name(String wp_name) {
        this.wp_name = wp_name;
    }

    public String getWp_desc() {
        return wp_desc;
    }

    public void setWp_desc(String wp_desc) {
        this.wp_desc = wp_desc;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getWp_code() {
        return wp_code;
    }

    public void setWp_code(String wp_code) {
        this.wp_code = wp_code;
    }

    public String getPf_id() {
        return pf_id;
    }

    public void setPf_id(String pf_id) {
        this.pf_id = pf_id;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCost_refference() {
        return cost_refference;
    }

    public void setCost_refference(String cost_refference) {
        this.cost_refference = cost_refference;
    }

    public String getCost_plan() {
        return cost_plan;
    }

    public void setCost_plan(String cost_plan) {
        this.cost_plan = cost_plan;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getWbs_id() {
        return wbs_id;
    }

    public void setWbs_id(String wbs_id) {
        this.wbs_id = wbs_id;
    }

    public String getWbs1_id() {
        return wbs1_id;
    }

    public void setWbs1_id(String wbs1_id) {
        this.wbs1_id = wbs1_id;
    }
}
