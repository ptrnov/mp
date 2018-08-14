package com.cudo.mproject.Menu.Tab;

public interface TabInterface {
    interface Presenter{
        void getTask();
        void logout(final String username, final String password);
        void cekApk(final String versionApk);
    }
    interface View {
        void onUserExpired();
        void onFinish(boolean isSucces, String msg);
        void onError(boolean status, String msg);
        void onSuccess(String msg);
    }
}
