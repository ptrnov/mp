package com.cudo.mproject.Menu.Site.IMB.History;

public interface IMBHistoryInterface {
    interface Presenter {
        void submitDataIMB(String projectId, String namaPemilikLahan, String noIMB, String alamatIMB, String tglAwalMasaBerlaku, String tglAkhirMasaBerlaku, String isOnline, int idIMB);
        void doneSubmit(String projectId, String isOnline);
    }

    interface View {
        void onSuccessSubmit(boolean isSucces, String msg);

        void onErrorSubmit(boolean isSucces, String msg);
    }
}
