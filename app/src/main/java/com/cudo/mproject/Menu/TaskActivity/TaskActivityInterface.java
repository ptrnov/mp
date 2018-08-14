package com.cudo.mproject.Menu.TaskActivity;

import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.WorkPkg;

import java.util.List;

/**
 * Created by adsxg on 12/12/2017.
 */

public interface TaskActivityInterface {
    interface Presenter {
        void getWorkPackage(String projectid, String userid);

//        void getActivity(String wpid, String userid, String sProjectid);
         void getActivity(String userid, String wbs1_id, String wbs_id, String wpid, String sProjectid);
//        void doSubmit(String dpid, String projectid, String progress, String wpId, String activityId, String inputProgress_tx, String isitglActual);
//        void offlineSubmit(String dpt_id, String projectid, String progress, String wpId, String activityId, String inputProgress_tx, boolean is_online, String isitglActual);
        void doSubmit(String dpid, String projectid, String progress, String wpId, String activityId, String inputProgress_tx);
        void offlineSubmit(String dpt_id, String projectid, String progress, String wpId, String activityId, String inputProgress_tx, boolean is_online, String dpid);

        void uploadPhoto(String pa_id, String project_id, String wpId, String activityId,String inputProgress_tx, boolean is_online, String dpid);
        void doneSubmit(String dpid, String project_id, String wpId, String activityId, boolean is_upload);
//        void doneSubmit(String dpid,String project_id);

//        void mergeDataOffline(String dpt_id,String projectid,String progress, String wpId, String activityId);
        //static void disabledButton(boolean a);
    }

    interface View {
        //  void onFinish(boolean isSucces, String msg);
        // void onUserExpired();
        void onSuccessSubmit(boolean isSucces, String msg);
        void onErrorSubmit(boolean isSucces, String msg);

        void onSuccessWorkpkg(boolean isSucces, String msg, List<WorkPkg> list);
        void onSuccessActivity(boolean isSucces, String msg, List<MActivity> list);
        void onError(String error);
    }
}

