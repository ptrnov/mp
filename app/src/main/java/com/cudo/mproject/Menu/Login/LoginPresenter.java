package com.cudo.mproject.Menu.Login;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.cudo.mproject.API.ReqService;

import com.cudo.mproject.API.URL;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Model.Response.ResponseActivity;
import com.cudo.mproject.Model.Response.ResponseLogin;
import com.cudo.mproject.Model.Response.ResponseWorkPkg;
import com.cudo.mproject.Model.User;
import com.google.gson.Gson;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.cudo.mproject.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;

import static com.cudo.mproject.API.URL.URL_LOGIN;

/**
 * Created by adsxg on 12/12/2017.
 */

public class LoginPresenter implements LoginInterface.Presenter {
    LoginInterface.View view;
    Context context;
    Realm realm;
    BaseActivity mContext;/*added 13/12/2017*/

    //  ResponseLogin profileResponse = new ResponseLogin();
    //Response<ResponseLogin> response;
    String jsonData = "{\"code\":300,\"message\":\"Login Failed\",\"data\":{}}";

    public LoginPresenter(LoginInterface.View view, Context context, Realm realm) {
        this.view = view;
        this.context = context;
        this.realm = realm;
    }
     public void linkApk(){

     }
    @Override
    public void login(String username, String password, String noImei, String versionApk) {
        //String versionApk =   BuildConfig.VERSION_NAME;
        Log.d(TAG, "checkLogin: " + username + "__password:" + password + "__noImei:" + noImei + "__versionApk:" + versionApk);
        if (username.equals("")) {
            view.onUsernameError();
            return;
        }

        if (password.equals("")) {
            view.onPasswordError();
            return;
        }
        doLogin(username, password, versionApk);
    }

    String TAG = getClass().getSimpleName();

    private void doLogin(final String username, final String password, final String versionApk) {
        view.showProgress(true);

        final Call<ResponseLogin> retrofitLogin =
                ReqService.getAPIInterFace().doLogin(URL_LOGIN + "/" + username + "/" + password + "/" + versionApk);
        retrofitLogin.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin profileResponse = response.body();
                Log.d(TAG, "onResponse Login: " + new Gson().toJson(profileResponse));
                if (profileResponse != null && profileResponse.rcCode().matches("1")) {
//                    if (profileResponse.rcCode()) {
//                        view.onError("Login User dengan username =: "+username+" : status aktif");
//                    }
                    if (profileResponse.getUser() != null) {
                        updateProfile(profileResponse, username, password);
//                        getWorkpackage("Hello");
//                        getActivity("World");
                    } else {
                        view.onError(context.getString(R.string.global_error_response));
                    }
                } else if (profileResponse.rcCode().matches("x001")) {
//                    view.onError(context.getString(R.string.global_error_response));
//                    view.onError(context.getString(R.string.global_error_response));
                    view.onError("Login User dengan username =: " + username + " : status aktif");
//
                } else {
                    view.onError(context.getString(R.string.global_error_response));
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                t.printStackTrace();
                view.onError("Tidak dapat terhubung dengan server");
//                view.onError("Login User dengan username =: "+username+" : status aktif");
            }
        });
    }

    private void updateProfile(final ResponseLogin profileResponse, String username, String password) {
        if (profileResponse != null) {
            final User user = profileResponse.getUser();
            user.setPassword(password);
            user.setUsername(username);
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(user);//copyToRealmOrUpdate
                    realm.copyToRealm(profileResponse.getProjects());//copyToRealmOrUpdate
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    view.onSuccess();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    view.onError("Kesalahan System");
                }
            });

        }
//        else {
//            final User user = profileResponse.getUser();
//                user.setPassword(password);

//            realm.executeTransactionAsync(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                      final User user = profileResponse.getUser();
//                      realm.copyToRealm(user);
//                      realm.copyToRealm(profileResponse.getProjects());
//                }
//            }, new Realm.Transaction.OnSuccess() {
//                @Override
//                public void onSuccess() {
//                    view.onSuccess();
//                }
//            });
//        }
    }
}




