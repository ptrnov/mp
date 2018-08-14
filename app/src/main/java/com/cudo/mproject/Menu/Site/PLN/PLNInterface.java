package com.cudo.mproject.Menu.Site.PLN;

/**
 * Created by adsxg on 1/11/2018.
 */

public interface PLNInterface {
    interface Presenter {
        void submitDataPLN(String projectId,  String idPelanggan,  String namaPelanggan,  String dayaListrik,  String isOnline);
//        void doneSubmit(int penjaga_lahan_site_project_id, String projectId, String isOnline, boolean is_upload);
        void doneSubmit(String projectId, String isOnline);
    }

    interface View {
        void onSuccessSubmit(boolean isSucces, String msg);

        void onErrorSubmit(boolean isSucces, String msg);
    }
}
