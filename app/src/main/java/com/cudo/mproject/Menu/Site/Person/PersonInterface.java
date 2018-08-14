package com.cudo.mproject.Menu.Site.Person;

/**
 * Created by adsxg on 1/11/2018.
 */

public interface PersonInterface {
    interface Presenter {
        void submitDataPerson(String projectId, String namaPenjagaLahan, String alamat, String provinsi, String kabupaten, String kecamatan, String noKTP, String noTlp, String isOnline);

        //        void doneSubmit(int penjaga_lahan_site_project_id, String projectId, String isOnline, boolean is_upload);
        void doneSubmit(String projectId, String isOnline);
    }


    interface View {
        void onSuccessSubmit(boolean isSucces, String msg);

        void onErrorSubmit(boolean isSucces, String msg);
    }
}
