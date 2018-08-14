package com.cudo.mproject.Menu.logout;

import android.content.Context;
import android.util.Log;

import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.History.HistoryActivityInterface;
import com.cudo.mproject.Model.Response.ResponseLogoff;
import com.cudo.mproject.Model.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cudo.mproject.API.URL.URL_LOGOFF;

public class LogoutPresenter implements LogoutInterface.Presenter {
    String TAG = getClass().getSimpleName();
    HistoryActivityInterface.View view;
    BaseActivity mContext;
    private Context hContext;

    @Override
    public void logout(String username, String password) {
        final Call<ResponseLogoff> retrofitLogin =
                ReqService.getAPIInterFace().doLogout(URL_LOGOFF + "/" + username + "/" + password);
        retrofitLogin.enqueue(new Callback<ResponseLogoff>() {
            @Override
            public void onResponse(Call<ResponseLogoff> call, Response<ResponseLogoff> response) {
                ResponseLogoff profileResponse = response.body();
                Log.d(TAG, "onResponse doLogout: " + new Gson().toJson(profileResponse));
                if (profileResponse != null) {
                    if (profileResponse.getUser() != null) {
                        updateProfile(profileResponse, username, password);
                    }
                } else {
                    view.onError(false, "GAGAL");
                }
            }

            @Override
            public void onFailure(Call<ResponseLogoff> call, Throwable t) {
                t.printStackTrace();
                view.onError(false, "Tidak dapat terhubung dengan server");
            }
        });
    }
    private void updateProfile(final ResponseLogoff profileResponse, String username, String password) {
        if (profileResponse != null) {
            final User user = profileResponse.getUser();
            user.setPassword(password);
            user.setUsername(username);
            user.islogout(mContext, mContext.getRealm(), username);
//            view.onSuccess("succes");
        }
    }
}
