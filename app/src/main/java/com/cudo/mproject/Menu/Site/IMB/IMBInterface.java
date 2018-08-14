package com.cudo.mproject.Menu.Site.IMB;

/**
 * Created by adsxg on 1/11/2018.
 */

public interface IMBInterface {
    interface Presenter {
        void submitDataIMB(String projectId, String namaPemilikLahan, String noIMB, String alamatIMB, String tglAwalMasaBerlaku, String tglAkhirMasaBerlaku, String isOnline);

        //        void doneSubmit(String projectId, String isOnline, boolean is_upload);
        void doneSubmit(String projectId, String isOnline);
    }

    interface View {
        void onSuccessSubmit(boolean isSucces, String msg);

        void onErrorSubmit(boolean isSucces, String msg);
    }
}
