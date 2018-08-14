package com.cudo.mproject.Menu.logout;

/**
 * Created by adsxg on 2/20/2018.
 */

public interface LogoutInterface {
    interface Presenter {
        void logout(String username, String password);
        //void saveSetting(String END_POINT, String key, String saltkey);
    }

    interface View {
//        void onSuccess();
        void onError(boolean status, String msg);
        void onSuccess(String msg);
    }
}
