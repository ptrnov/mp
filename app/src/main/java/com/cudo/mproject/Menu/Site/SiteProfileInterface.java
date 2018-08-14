package com.cudo.mproject.Menu.Site;

/**
 * Created by adsxg on 1/7/2018.
 */

public interface SiteProfileInterface {
    interface Presenter {
        void submitDataProfile();
    }
    interface View {
        void onSuccessSubmit(boolean isSucces, String msg);
        void onErrorSubmit(boolean isSucces, String msg);
    }
}
