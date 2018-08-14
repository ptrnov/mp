package com.cudo.mproject.Menu.History;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.Response.ResponseApk;
import com.cudo.mproject.Model.Response.ResponseLogin;
import com.cudo.mproject.Model.Response.ResponseLogoff;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cudo.mproject.API.URL.URL_APK;
import static com.cudo.mproject.API.URL.URL_GETPROJECT;
import static com.cudo.mproject.API.URL.URL_LOGIN;
import static com.cudo.mproject.API.URL.URL_LOGOFF;

/**
 * Created by adsxg on 12/14/2017.
 */

public class HistoryPresenter implements HistoryActivityInterface.Presenter {
    String TAG = getClass().getSimpleName();
    HistoryActivityInterface.View view;
    BaseActivity mContext;
    private Context hContext;
//    private PopupWindow mPopupWindow;


    public HistoryPresenter(BaseActivity context, HistoryActivityInterface.View view, String userName, String password) {
        this.view = view;
        mContext = context;
//        ButterKnife.bind(this, itemView);
        hContext = this.mContext;

    }

    //    public HistoryPresenter(View itemView) {
////      super(itemView);
//        ButterKnife.bind(this, itemView);
//        hContext = itemView.getContext();
//    }

 /*
   Swap screen method
 */
    @Override
    public void getTask() {
        User user = mContext.getRealm().where(User.class).findFirst();
        if (user != null) {
            String username = user.getUsername();
            String password = user.getPassword();
            String noImei = "";
            Call<ResponseLogin> responseLoginCall = ReqService.getAPIInterFace().doLogin(URL_GETPROJECT + "/" + username + "/" + password);
            responseLoginCall.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    ResponseLogin profileResponse = response.body();
                    if (profileResponse != null) {
                        if (profileResponse.getProjects() != null)
                            updateData(profileResponse);
                        else
                            view.onFinish(false, "Tidak ada data");
                    } else {
                        view.onFinish(false, mContext.getString(R.string.global_error_response));
                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    t.printStackTrace();
                    view.onFinish(false, "Tidak dapat terhubung dengan server");
                    //ResponseLogin responseLogin  =null;
                    //updateData(responseLogin);
                }
            });
        } else {
            view.onUserExpired();
        }
    }

    void updateData(final ResponseLogin responseLogin) {
        if (responseLogin != null) {
            mContext.getRealm().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
//                    realm.delete(Project.class);
                    realm.insertOrUpdate(responseLogin.getProjects());
//                    realm.copyToRealmOrUpdate(responseLogin.getProjects());
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    view.onFinish(true, "");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    error.printStackTrace();
                    view.onFinish(false, "Ups ada yang salah");
                }
            });
        }
    }

    @Override
    public void logout(final String username, final String password) {
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

    @Override
    public void cekApk(String versionApk) {
        final Call<ResponseApk> retrofitApk =
                ReqService.getAPIInterFace().doApk(URL_APK + "/" + versionApk);
        retrofitApk.enqueue(new Callback<ResponseApk>() {
            @Override
            public void onResponse(Call<ResponseApk> call, Response<ResponseApk> response) {
                ResponseApk apkResponse = response.body();
                Log.d(TAG, "onResponse doApk: " + new Gson().toJson(apkResponse));
                if (apkResponse != null) {
//                    String isiTitle = null;
//                    String apk_uri = null;
//                    if (apkResponse.status() == false) {
//                        isiTitle = apkResponse.text();
//                    } else {
//                        isiTitle = "Download New APK";
//                        Log.d(TAG, "onResponse doApk apkResponse.link(): " + apkResponse.link());
//                        apk_uri = apkResponse.link();
//                        Log.d(TAG, "onResponse doApk apkResponse.link(): " + apk_uri);
//                    }
//
                    Log.d(TAG, "onResponse doApk apkResponse.link(): " + apkResponse.link());
                    String apk_uri = null;
                    String title = null;
                    if (apkResponse.link() != null) {
                        apk_uri = apkResponse.link();
                        title = "Download APK Terbaru";
                    } else {
                        title = "Versi Terbaru";
                        apk_uri = "Versi Terbaru";
                    }

                    Log.d(TAG, "onResponse doApk apkResponse.link(): " + apk_uri);
//
                    AlertDialog.Builder builder = new AlertDialog.Builder(hContext);
                    builder.setTitle(title);
                    builder.setMessage(apk_uri);
                    String isiapk_uri = apk_uri;
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
//                     Show location settings when the user acknowledges the alert dialog
//
                            if (apkResponse.link() != null) {
                                Intent browserIntent = new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(isiapk_uri));
                                mContext.startActivity(browserIntent);
                            }
                        }
                    });
                    Dialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    view.onError(false, "APK SUDAH YANG TERBARU");
                }
            }

            @Override
            public void onFailure(Call<ResponseApk> call, Throwable t) {
                t.printStackTrace();
                view.onError(false, "APK SUDAH YANG TERBARU");//Tidak dapat terhubung dengan server
            }

//            @Override
//            public void onFailure(Call<ResponseLogoff> call, Throwable t) {
//                t.printStackTrace();
//                view.onError(false, "Tidak dapat terhubung dengan server");
//            }
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
