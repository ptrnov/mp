package com.cudo.mproject.Menu.Site.Alamat.Actual;

/**
 * Created by adsxg on 1/11/2018.
 */

public interface AlamatActualInterface {
    interface Presenter {
      void submitDataActual(String projectId, String siteName, String alamat, String provinsi, String kabupaten, String kecamatan, String longitude, String latitude, String isOnline);
    }
    interface View {
        void onSuccessSubmit(boolean isSucces, String msg);
        void onErrorSubmit(boolean isSucces, String msg);

    }
}
