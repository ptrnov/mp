package com.cudo.mproject.Menu.Project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.Response.ResponseApk;
import com.cudo.mproject.Model.Response.ResponseLogoff;
import com.cudo.mproject.R;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Model.Response.ResponseLogin;
import com.cudo.mproject.Model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by adsxg on 12/12/2017.
 */

public class ProjectListPresenter implements ProjectListInterface.Presenter {
    String TAG = getClass().getSimpleName();
    ProjectListInterface.View view;
    BaseActivity mContext;
    private Context pContext;

    public ProjectListPresenter(BaseActivity context, ProjectListInterface.View view, String userName, String password) {
        this.view = view;
        mContext = context;
//        ButterKnife.bind(this, itemView);
        pContext = this.mContext;
    }

//    public ProjectListPresenter(View itemView) {
//    super(itemView);
//    ButterKnife.bind(this, itemView);

    //}
    @Override
    public void getTask() {
        User user = mContext.getRealm().where(User.class).findFirst();
        if (user != null) {
            String username = user.getUsername();
            String password = user.getPassword();
            Call<ResponseLogin> responseLoginCall = ReqService.getAPIInterFace().doLogin(URL_GETPROJECT + "/" + username + "/" + password);
            responseLoginCall.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    ResponseLogin profileResponse = response.body();
                    if (profileResponse != null) {
                        if (profileResponse.getProjects() != null) {
                            Log.d(TAG, "onResponse Login: " + new Gson().toJson(profileResponse));
                            updateData(profileResponse);
                        } else {
                            view.onFinish(false, "Tidak ada data");
                        }
//                         buttonSet(profileResponse);
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
//            buttonSet(responseLogin);
            mContext.getRealm().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(Project.class);
                    realm.insertOrUpdate(responseLogin.getProjects());
                    //realm.copyToRealmOrUpdate(responseLogin.getProjects());
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
//        buttonSet(responseLogin);
    }

    @Override
    public void logout(final String username, final String password) {
//        Toast.makeText(baseActivity.getBaseContext(), "isi userName:= " + username + ": isiPassword := " + password + ":", Toast.LENGTH_SHORT).show();
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
//                    AlertDialog.Builder builder = new AlertDialog.Builder(hContext);
//                    builder.setTitle("Cek APK");
//                    builder.setMessage(apk_uri);
//                    String isiApk_uri = apk_uri;

                    Log.d(TAG, "onResponse doApk apkResponse.link(): " + apkResponse.link());
                    String apk_uri = null;
                    String title = null;
                    if (apkResponse.link()!=null) {
                        apk_uri = apkResponse.link();
                        title = "Download APK Terbaru";
                    } else {
                        title = "Versi Terbaru";
                        apk_uri = "Versi Terbaru";
                    }

                    Log.d(TAG, "onResponse doApk apkResponse.link(): " + apk_uri);
//
                    AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
                    builder.setTitle(title);
                    builder.setMessage(apk_uri);
                    String isiapk_uri = apk_uri;
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
//                     Show location settings when the user acknowledges the alert dialog
//
                            if (apkResponse.link()!=null) {
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
                view.onError(false, "Tidak dapat terhubung dengan server");//APK SUDAH YANG TERBARU
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