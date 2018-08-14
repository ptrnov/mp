package com.cudo.mproject.Menu.Site.Alamat.Actual;

import android.app.ProgressDialog;
import android.util.Log;

import com.cudo.mproject.API.IRetrofit;
import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.API.URL;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Model.DataAlamatActualOffline;
import com.cudo.mproject.Model.Post.DataActualPost;
import com.cudo.mproject.Model.Response.ResponseSubmitAlamatActual;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.Utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;

public class AlamatActualPresenter implements AlamatActualInterface.Presenter {
    String TAG = getClass().getSimpleName();
    AlamatActualInterface.View view;
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

    //
    public AlamatActualPresenter(AlamatActualInterface.View view, BaseActivity baseActivity, String projectId) {
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
    public void submitDataActual(final String projectId, final String siteName, final String alamat, String provinsi, String kabupaten, String kecamatan, String longitude, String latitude, String isOnline) {
        dataActualPost = new DataActualPost();
//        realm.beginTransaction();
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userId = user.getUser_id();
//
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
        //
        //creating the json object to send
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("userName", user.getUsername());
//        jsonObject.addProperty("userId", user.getUser_id());
//        jsonObject.addProperty("projectId", projectId);
//        jsonObject.addProperty("siteName", siteName);
//        jsonObject.addProperty("alamat", alamat);
//        jsonObject.addProperty("provinsi", provinsi);
//        jsonObject.addProperty("kabupaten", kabupaten);
//        jsonObject.addProperty("kecamatan", kecamatan);
//        jsonObject.addProperty("latitude", latitude);
//        jsonObject.addProperty("longitude", longitude);
//        jsonObject.addProperty("isOnline", isOnline);
//        dataActualPost.setUserName(user.getUsername());
//        dataActualPost.setUserId(user.getUser_id());
//        dataActualPost.setProjectId(projectId);
//        dataActualPost.setSiteName(siteName);
//        dataActualPost.setAlamat(alamat);
//        dataActualPost.setProvinsi(provinsi);
//        dataActualPost.setKabupaten(kabupaten);
//        dataActualPost.setKecamatan(kecamatan);
//        dataActualPost.setLatitude(latitude);
//        dataActualPost.setLongitude(longitude);
//        dataActualPost.setIsOnline(isOnline);
//
//        dataActualPost.toString();

//        Log.d(TAG, "doSubmit_URL_Submit: " + new Gson().toJson(jsonObject));
//        return;
        if (isOnline == "1") {
            Log.d(TAG, "onResponse: isOnline ==  " + "1");
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
                        if (responseSubmitAlamatActual.getMessage().matches("saPidx001")){
                            view.onSuccessSubmit(false, "PROEJCT ID"+projectId+"TIDAK TERDAFTAR");
                            progressDialog.dismiss();
                        }
                        else if (responseSubmitAlamatActual.getMessage().matches("saUiNx001")){
                            view.onSuccessSubmit(false, "User dengan Username"+user.getUsername()+"TIDAK TERDAFTAR");
                            progressDialog.dismiss();
                        }
                        else if (responseSubmitAlamatActual.getMessage().matches("SUBMIT")) {
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
        } else {
            inputAlamatActual(user.getUser_id(), user.getUsername(), projectId, siteName, alamat, provinsi, kabupaten, kecamatan, longitude, latitude, isOnline);
        }
    }

    void inputAlamatActual(String userId, String userName, String projectId, String
            siteName, String alamat, String provinsi, String kabupaten, String kecamatan, String
                                   longitude, String latitude, String isOnline) {
        baseActivity.getRealm().beginTransaction();
        Number currentIdNum = baseActivity.getRealm().where(DataAlamatActualOffline.class).max("idAlamatActualOffline");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
//
        DataAlamatActualOffline dataAlamatActualOffline = new DataAlamatActualOffline();
//
        dataAlamatActualOffline.setIdAlamatActualOffline(nextId);
        dataAlamatActualOffline.setUserId(userId);
        dataAlamatActualOffline.setUserName(userName);
        dataAlamatActualOffline.setDateSubmit(curent_date);
        dataAlamatActualOffline.setTimeSubmit(time_now);
        dataAlamatActualOffline.setProject_id(projectId);
        dataAlamatActualOffline.setSiteName(siteName);
        dataAlamatActualOffline.setAlamat(alamat);
        dataAlamatActualOffline.setProvinsi(provinsi);
        dataAlamatActualOffline.setKabupaten(kabupaten);
        dataAlamatActualOffline.setKecamatan(kecamatan);
        dataAlamatActualOffline.setLongitude(longitude);
        dataAlamatActualOffline.setLatitude(latitude);
        dataAlamatActualOffline.setStatus_alamat_actual_offline(isOnline);
        dataAlamatActualOffline.setStatus_approval("1");
//
        baseActivity.getRealm().insertOrUpdate(dataAlamatActualOffline);
        baseActivity.getRealm().commitTransaction();
        Log.d(TAG, "onResponse: dataAlamatActualOffline : Added");
        Log.d(TAG, "onResponse: dataAlamatActualOffline : data tersimpan di local db" + dataAlamatActualOffline);
//      dataAlamatActualOffline.set
        if (isOnline == "1") {
            view.onSuccessSubmit(true, "Data Alamat Actual Submited");
            closeRealm();
        }
        if (isOnline == "2") {
            view.onSuccessSubmit(true, "Data Submited");
            closeRealm();
        }
    }

    void closeRealm() {
        baseActivity.getRealm().close();
        progressDialog.dismiss();
    }
}
