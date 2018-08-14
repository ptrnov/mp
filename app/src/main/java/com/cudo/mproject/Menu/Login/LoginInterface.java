package com.cudo.mproject.Menu.Login;

import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.WorkPkg;

import java.util.List;

/**
 * Created by adsxg on 12/12/2017.
 */

public interface LoginInterface {
    interface Presenter {
        void login(String username, String password, String noImei, String versionApk);
        //void saveSetting(String END_POINT, String key, String saltkey);
    }

    interface View {
//        void onSuccessWorkpkg(boolean isSucces, String msg, List<WorkPkg> list);
//        void onSuccessActivity(boolean isSucces, String msg, List<MActivity> list);
        void onUsernameError();
        void onPasswordError();
        void onSuccess();
        void showProgress(boolean show);
        void onError(String error);
        void onToast(String toast);

    }
}
