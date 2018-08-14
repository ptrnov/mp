package com.cudo.mproject.Menu.TaskHistory;

import com.cudo.mproject.Model.MActivity;

import java.util.List;

/**
 * Created by adsxg on 12/22/2017.
 */

public interface TaskHistoryInterface {
    interface Presenter {
        //        void getWorkPackage(String projectid,String userid);
//        void getActivity(String wpid,String userid);
        void doSubmit(String dpt_id, String projectid, String wpId, String activityId, String tglHistory, String timeHistory, String idLocal, String inputProgress_tx);
//        void offlineSubmit(String idLocal,boolean is_online);
        //        void offlineSubmit(String dpt_id,String projectid,String progress, String wpId, boolean is_online);
//        void updateDataLocals(String dpt_id, String projectid, String wpId, String activityId, String inputProgress_tx, boolean is_online);
//        void uploadPhoto(String pa_id, String project_id, String wpId, String activityId, String tglHistory, String timeHistory,String idLocal, boolean is_online);
        void uploadPhoto(String pa_id, String idLocal, boolean is_online);
        void doneSubmit(String dpid, String project_id);
//        void getStatusApproval(String activityId,String dpt_id, String userid);
        //static void disabledButton(boolean a);
    }

    interface View {
        //  void onFinish(boolean isSucces, String msg);
        // void onUserExpired();
        void onSuccessSubmit(boolean isSucces, String msg);
        void onErrorSubmit(boolean isSucces, String msg);
//        void onSuccess(boolean isSucces, String msg, List<MActivity> list);
//        void onSuccessWorkpkg(boolean isSucces, String msg, List<WorkPkg> list);
//        void onSuccessActivity(boolean isSucces, String msg, List<MActivity> list);
    }
}
