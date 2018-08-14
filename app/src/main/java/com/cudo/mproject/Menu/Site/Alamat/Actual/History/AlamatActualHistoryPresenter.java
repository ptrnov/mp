package com.cudo.mproject.Menu.Site.Alamat.Actual.History;

import android.app.ProgressDialog;
import android.util.Log;

import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.API.URL;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Site.Alamat.Actual.AlamatActualInterface;
import com.cudo.mproject.Model.Post.DataActualPost;
import com.cudo.mproject.Model.Response.ResponseSubmitAlamatActual;
import com.cudo.mproject.Model.User;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;

public class AlamatActualHistoryPresenter implements AlamatActualHistoryInterface.Presenter {
    String TAG = getClass().getSimpleName();
    AlamatActualHistoryInterface.View view;
    BaseActivity baseActivity;
    ProgressDialog progressDialog;
    DataActualPost dataActualPost = null;

    public static Realm realm;
    //    public User user;
    //
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeF = new SimpleDateFormat(" hh:mm:ss");
    Date now = new Date();

    String curent_date = sdf.format(now);
    String time_now = timeF.format(now);

    public AlamatActualHistoryPresenter(AlamatActualHistoryInterface.View view,  BaseActivity baseActivity, String projectId){
        this.view = view;
        this.baseActivity = baseActivity;
        progressDialog = new ProgressDialog(baseActivity);
//        if (Integer.parseInt(progress) <= 0) {
//            view.onErrorSubmit(false, "Nilai progress Tidak boleh <=0");
//            return;
//        }
//        if (Integer.parseInt(progress) != 0) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setProgress(0);

    }
    @Override
    public void submitDataActual(String projectId, String siteName, String alamat, String provinsi, String kabupaten, String kecamatan, String longitude, String latitude, String isOnline, int idAlamatAktual) {
        dataActualPost = new DataActualPost();
//        realm.beginTransaction();
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userId = user.getUser_id();
        progressDialog.show();
        final int totalProgressTime = 100;
        int jumpTime = 0;

        while (jumpTime < totalProgressTime) {
            try {
                sleep(100);
                jumpTime += 5;
                progressDialog.setProgress(jumpTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Log.d(TAG, "onResponse: data ==  " + user.getUser_id() + user.getUsername()+user.getArea() + projectId + siteName + alamat +
                provinsi + kabupaten + kecamatan + latitude + longitude + isOnline);
        Call<ResponseSubmitAlamatActual> responseBodyCall = ReqService.getAPIInterFace()
                .dataActualPost(URL.URL_Submit_Alamat_Actual, user.getUser_id(), user.getUsername(),user.getArea(), projectId, siteName, alamat,
                        provinsi, kabupaten, kecamatan, latitude, longitude, isOnline);

        responseBodyCall.enqueue(new Callback<ResponseSubmitAlamatActual>() {
            @Override
            public void onResponse(Call<ResponseSubmitAlamatActual> call, Response<ResponseSubmitAlamatActual> response) {
                try {
                    Log.d(TAG, "onResponse: void");
//                        Log.d(TAG, "onResponse: responseSubmitAlamatActual " + response.body());
//                        String rs = response.body().toString();
                    ResponseSubmitAlamatActual responseSubmitAlamatActual = response.body();
                    Log.d(TAG, "onResponse: isi rs" + new Gson().toJson(responseSubmitAlamatActual));
                    if (responseSubmitAlamatActual.getMessage().matches("saPidx001")) {
                        view.onSuccessSubmit(false, "PROEJCT ID" + projectId + "TIDAK TERDAFTAR");
                        progressDialog.dismiss();
                    } else if (responseSubmitAlamatActual.getMessage().matches("saUiNx001")) {
                        view.onSuccessSubmit(false, "User dengan Username" + user.getUsername() + "TIDAK TERDAFTAR");
                        progressDialog.dismiss();
                    } else if (responseSubmitAlamatActual.getMessage().matches("SUBMIT")) {
                        inputAlamatActual(user.getUser_id(), user.getUsername(), projectId, siteName, alamat, provinsi, kabupaten, kecamatan, longitude, latitude, isOnline);
                    }
                    if (responseSubmitAlamatActual.getMessage().matches("FAILED")) {
                        view.onSuccessSubmit(false, "FAILED");
                        progressDialog.dismiss();
                    }
                    try {
//                        Log.e("response-success", response.body().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    view.onSuccessSubmit(false, e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseSubmitAlamatActual> call, Throwable t) {
                view.onSuccessSubmit(false, "FAILED");
                progressDialog.dismiss();
            }
        });
    }
    void inputAlamatActual(String userId, String userName, String projectId, String
            siteName, String alamat, String provinsi, String kabupaten, String kecamatan, String
                                   longitude, String latitude, String isOnline) {
        baseActivity.getRealm().beginTransaction();
//

    }
}
