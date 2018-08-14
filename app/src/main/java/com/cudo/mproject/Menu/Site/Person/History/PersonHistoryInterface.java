package com.cudo.mproject.Menu.Site.Person.History;

public interface PersonHistoryInterface {
    interface Presenter {
        void submitDataPerson(String projectId, String namaPenjagaLahan, String alamat, String provinsi, String kabupaten, String kecamatan, String noKTP, String noTlp, String isOnline, int idPenjagaLahan);

        //        void doneSubmit(int penjaga_lahan_site_project_id, String projectId, String isOnline, boolean is_upload);
        void doneSubmit(String projectId, String isOnline);    }
    interface View {
        void onSuccessSubmit(boolean isSucces, String msg);
        void onErrorSubmit(boolean isSucces, String msg);

    }
}