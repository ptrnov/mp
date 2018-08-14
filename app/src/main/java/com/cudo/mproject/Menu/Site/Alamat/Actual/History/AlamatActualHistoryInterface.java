package com.cudo.mproject.Menu.Site.Alamat.Actual.History;

public interface AlamatActualHistoryInterface {
    interface Presenter {
        void submitDataActual(String projectId, String siteName, String alamat, String provinsi, String kabupaten, String kecamatan, String longitude, String latitude, String isOnline, int idAlamatAktual);
    }
    interface View {
        void onSuccessSubmit(boolean isSucces, String msg);
        void onErrorSubmit(boolean isSucces, String msg);

    }
}
