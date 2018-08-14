package com.cudo.mproject.Menu.ListSite;

public interface HistorySiteActivityInterface {
    interface Presenter{
        void getTask();
        void logout(final String username, final String password);
        void cekApk(final String versionApk);
    }
    interface View{
        void onUserExpired();
        void onFinish(boolean isSucces, String msg);
        void onError(boolean status, String msg);
        void onSuccess(String msg);
    }
}
