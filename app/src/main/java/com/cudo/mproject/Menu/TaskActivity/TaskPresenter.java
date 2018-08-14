package com.cudo.mproject.Menu.TaskActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.API.URL;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.Response.ResponseActivity;
import com.cudo.mproject.Model.Response.ResponseLogin;
import com.cudo.mproject.Model.Response.ResponseSubmit;
import com.cudo.mproject.Model.Response.ResponseWorkPkg;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.Model.WorkPkg;
import com.cudo.mproject.R;
//import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.FileUtils;
import com.cudo.mproject.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cudo.mproject.API.URL.URL_LOGIN;
import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.io.FileUtils.copyFileToDirectory;

/**
 * Created by adsxg on 12/12/2017.
 */

public class TaskPresenter implements TaskActivityInterface.Presenter {
    String TAG = getClass().getSimpleName();

    TaskActivityInterface.View view;
    BaseActivity baseActivity;
    ProgressDialog progressDialog;
    int currentLoop = 0;
    static int max = 0;
    Realm realm;
    //List<Photo> listUploaded;
    List<Photo> list;
    List<Integer> ids;

    //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeF = new SimpleDateFormat(" hh:mm:ss");
    Date now = new Date();

    String curent_date = sdf.format(now);
    String time_now = timeF.format(now);
    String idOffline = null;
    String versionApk = BuildConfig.VERSION_NAME;

    public TaskPresenter(TaskActivityInterface.View view, BaseActivity baseActivity, String project_id, String wpId, String activityId, String progress) {

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
//        }


        //listUploaded = baseActivity.getRealm().where(Photo.class).equalTo(Photo.PROJECT_ID,project_id).equalTo(Photo.UPLOADED,true).findAll();
        RealmResults<Photo> asd =
                baseActivity.getRealm().where(Photo.class)
                        .equalTo(Photo.UPLOADED, false)
                        .equalTo(Photo.PROJECT_ID, project_id)
                        .equalTo("wp_id", wpId)
                        .equalTo("activity_id", activityId)
                        .equalTo("status_photo", "0")
                        .findAll();
        list = baseActivity.getRealm().copyFromRealm(asd);
        max = list.size();

    }

    @Override
    public void getWorkPackage(final String project_id, String userid) {
        final Call<ResponseBody> responseWorkPkgCall = ReqService.getAPIInterFace().doGetWorkPkg(URL.URL_GET_WORK_PACKAGE + "/" + project_id + "/" + userid);
        responseWorkPkgCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseString = response.body().string();
//                      Log.d(TAG, "response.body().string(): " + response.body().string());
                        JsonArray jarr = new JsonParser().parse(responseString).getAsJsonArray();
                        ResponseWorkPkg responseWorkPkg = new ResponseWorkPkg();
                        responseWorkPkg.setJsonArray(jarr);
                        view.onSuccessWorkpkg(true, "", responseWorkPkg.getListWorkPkg(project_id));
                        Log.d(TAG, "onResponse: " + responseString);
                    }
//                    else {
//                        view.onError("Tidak dapat terhubung dengan server");
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    view.onSuccessWorkpkg(false, "", null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
//                view.onError("Device Tidak Terhubung dengan server, Cek koneksi internet");
                view.onSuccessWorkpkg(false, "", null);
            }
        });
    }

    @Override
//    public void getActivity(final String wpid, String userid, String sProjectid) {
    public void getActivity(final String userid, final String wbs1_id, final String wbs_id, final String wpid, final String sProjectid) {
        final Call<ResponseBody> responseWorkPkgCall = ReqService.getAPIInterFace().doGetActivity(URL.URL_GET_ACTIVITY + "/" + userid + "/" + wbs1_id + "/" + wbs_id + "/" + wpid + "/" + sProjectid);
        responseWorkPkgCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseString = response.body().string();
                        JsonArray jarr = new JsonParser().parse(responseString).getAsJsonArray();
                        ResponseActivity responseWorkPkg = new ResponseActivity();
                        responseWorkPkg.setJsonArray(jarr);
                        view.onSuccessActivity(true, wpid, responseWorkPkg.getMActivityList());
                        Log.d(TAG, "onResponse: " + new Gson().toJson(responseString));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    view.onSuccessActivity(false, wpid, null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
//                view.onError("Device Tidak Terhubung dengan server, Cek koneksi internet");
                view.onSuccessActivity(false, wpid, null);
            }
        });


    }

//    void disabledButton() {
//        TaskActivity.taskActivity.button();
//    }

    @Override
    public void doSubmit(final String dpid, final String projectid, final String progress, final String wpId, final String activityId, final String inputProgress_tx) {
        //progressDialog.setMessage("Preparing..");
        if (max == 0) {
            view.onErrorSubmit(false, "Data Evidence Belum di isi");
            progressDialog.dismiss();
            return;
        }
        Log.d(TAG, "doSubmit: " + dpid + "===" + progress);
        if (Integer.parseInt(progress) != 0) {
        if (!progressDialog.isShowing())
            progressDialog.show();
        }
        Log.d(TAG, "doSubmit: " + URL.URL_SUBMIT_PROGRESS_ACTIVITY + "/" + dpid + "/" + progress);//dpid == dpt_id
        Call<ResponseSubmit> responseBodyCall = ReqService.getAPIInterFace().doSubmit(URL.URL_SUBMIT_PROGRESS_ACTIVITY + "/" + dpid + "/" + progress);
        responseBodyCall.enqueue(new Callback<ResponseSubmit>() {
            @Override
            public void onResponse(Call<ResponseSubmit> call, Response<ResponseSubmit> response) {
//                realm = Realm.getDefaultInstance();
                Log.d(TAG, "doSubmit: " + call);
                try {
                    ResponseSubmit responseSubmit = response.body();
                    Log.d(TAG, "doSubmit: responseSubmit.getPa_id() " + responseSubmit.getPa_id());
                    Log.d(TAG, "doSubmit: responseSubmit.isSucces() " + responseSubmit.isSucces());
                    if (responseSubmit != null && responseSubmit.isSucces()) {
                        Log.d(TAG, "doSubmit: responseSubmit.isSucces() " + baseActivity.getRealm() + "/ " + responseSubmit.isSucces());
                        Project.updatePAID(baseActivity.getRealm(), projectid, responseSubmit.getPa_id());
                        Log.d(TAG, "doSubmit: uploadPhoto() " + responseSubmit.getPa_id() + "/ " + projectid + "/ " + wpId + "/ " + activityId + "/ " + progress + "/ " + true);
                        uploadPhoto(responseSubmit.getPa_id(), projectid, wpId, activityId, progress, true, dpid);
//                        Log.d(TAG, "isitglActual " + isitglActual);
//                        offlineSubmit(dpid, projectid, progress, wpId, activityId, inputProgress_tx, true);

                    } else {
                        progressDialog.dismiss();
                        view.onSuccessSubmit(false, "Upload Failed Please try again... x31");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.onSuccessSubmit(false, "Upload Failed Please try again... x39");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseSubmit> call, Throwable t) {
                t.printStackTrace();
                view.onSuccessSubmit(false, t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void uploadPhoto(String pa_id, String project_id, String wpId, String activityId, String inputProgress_tx, boolean is_online, String dpid) {
        Log.d(TAG, "void uploadPhoto : project_id+ wpId+ activityId" + ":" + project_id + ":" + wpId + ":" + activityId);
//        if (Integer.parseInt(inputProgress_tx) <= 0) {
//            view.onErrorSubmit(false, "Nilai progress Tidak boleh <=0");
////            Toast.makeText(baseActivity.getBaseContext(),
////                    "Nilai progress Tidak boleh <=0",
////                    Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (max == 0) {
//            view.onError("Data Evidence Belum di isi");
            view.onErrorSubmit(false, "Data Evidence Belum di isi");
//            progressDialog.dismiss();
            return;
        }
//        Toast.makeText(baseActivity.getBaseContext(), "inputProgress_tx"+inputProgress_tx, Toast.LENGTH_SHORT).show();
        if (Integer.parseInt(inputProgress_tx) <= 0) {
            view.onErrorSubmit(false, "Nilai progress Tidak boleh <=0");
            return;
        }
//
        int i = currentLoop;
        if (!progressDialog.isShowing())
            progressDialog.show();
        progressDialog.setMessage("Uploading Data..");

        if (is_online == true) {
//                online transcation, when device connect internet.
            if (currentLoop == max) {
                doneSubmit(pa_id, project_id, wpId, activityId, true);
            } else {
                Log.d(TAG, "tag uploadPhoto: " + (currentLoop + 1) + "/" + max);
//                realm = Realm.getDefaultInstance();
/*
                 upadate status photo when connect internet
 */
                try {
                    Log.d(TAG, "uploadPhoto: ");
                    Photo m = list.get(i);
                    //progressDialog.setMessage("Uploading.. "+ currentLoop+"/"+max +" more files...");
                    doUploadPhoto(pa_id, project_id, wpId, activityId, inputProgress_tx, m, dpid);
//                    MActivity.updateProgress(realm, m.getProgress_photo(), m.getActivity_id());
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    view.onErrorSubmit(false, "Upload upload suksesFailed Please try again... x311");
                }
            }
        }
        if (is_online == false) {

            if (currentLoop == max) {
//                doneSubmit(pa_id, project_id, wpId, activityId, false);
            } else {
                Log.d(TAG, "uploadPhoto offline: " + (currentLoop + 1) + "/" + max);
//                realm = Realm.getDefaultInstance();

                /*                upadate status photo when not connect internet
                 */
//                baseActivity.getRealm().beginTransaction();
//              List<Photo> list = baseActivity.getRealm().where(Photo.class).equalTo(Photo.PROJECT_ID, project_id).findAll();
//              int count = baseActivity.getRealm().copyFromRealm(baseActivity.getRealm().where(Photo.class).equalTo(Photo.PROJECT_ID, project_id).findAll()).size();

//                List<Photo> list = baseActivity.getRealm().where(Photo.class)
//                        .equalTo(Photo.PROJECT_ID, project_id)
//                        .equalTo("wp_id", wpId)
//                        .equalTo("activity_id", activityId)
//                        .equalTo("status_photo", "0")
//                        .findAll();
//                int count = baseActivity.getRealm().copyFromRealm(realm.where(Photo.class)
//                        .equalTo(Photo.PROJECT_ID, project_id)
//                        .equalTo("wp_id", wpId)
//                        .equalTo("activity_id", activityId)
//                        .equalTo("status_photo", "0")
//                        .findAll()).size();
                List<Photo> list = baseActivity.getRealm().where(Photo.class)
                        .equalTo(Photo.PROJECT_ID, project_id)
                        .equalTo("wp_id", wpId)
                        .equalTo("activity_id", activityId)
                        .equalTo("status_photo", "0")
                        .findAll();
                int count = baseActivity.getRealm().copyFromRealm(baseActivity.getRealm().where(Photo.class)
                        .equalTo(Photo.PROJECT_ID, project_id)
                        .equalTo("wp_id", wpId)
                        .equalTo("activity_id", activityId)
                        .equalTo("status_photo", "0")
                        .findAll()).size();
                List<Integer> list1 = new ArrayList<>();
                for (int k = 0; k < count; k++) {
                    list1.add(list.get(k).getId());
                }
                for (int k = 0; k < count; k++) {
                    Log.d(TAG, "isi uploadPhoto : count" + k);
                    Photo m = baseActivity.getRealm().where(Photo.class)
                            .equalTo(Photo.PROJECT_ID, project_id)
                            .equalTo(Photo.ID, list1.get(k)).findFirst();
//                    MActivity mActivity = realm.where(MActivity.class)
//                            .equalTo("activity_id", activityId).findFirst();
//
                    if (m != null) {
//                        realm.beginTransaction();
                        Photo.offlinePhoto(baseActivity.getRealm(), m, curent_date, time_now, Integer.parseInt(inputProgress_tx));

//                        m.setStatus_photo("1");
//                        m.setUploaded(false);
//                        m.setDate_photo(curent_date);
//                        m.setTime_photo(time_now);
//                        m.setProgress_photo(Integer.parseInt(inputProgress_tx));
//                        m.setProgress_photo(Integer.parseInt(dataLocal.getProgrees()));
                        Log.d(TAG, "isi uploadPhoto m.getTime_photo() " + m.getDate_photo());
                        Log.d(TAG, "isi uploadPhoto m.getProjectid() " + m.getProjectid());
                        Log.d(TAG, "isi uploadPhoto m.getWp_id() " + m.getWp_id());
                        Log.d(TAG, "isi uploadPhoto m.getActivity_id() " + m.getActivity_id());
                        Log.d(TAG, "isi uploadPhoto m.getTime_photo() " + m.getTime_photo());
                        Log.d(TAG, "isi uploadPhoto setProgress_photo " + m.getProgress_photo());
//                        realm.commitTransaction();
                        Log.d(TAG, "doSubmit: Merge data foto off,");
                        try {
                            moveToDirOff(baseActivity.getRealm(), m, m.getWp_id(), m.getActivity_id(), Integer.parseInt(inputProgress_tx), false);
                            MActivity.updateProgress(baseActivity.getRealm(), m.getProgress_photo(), m.getActivity_id());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                progressDialog.dismiss();
            }
        }
    }

    public void doUploadPhoto(final String pa_id, final String project_id, final String wpId, final String activityId, final String inputProgress_tx, final Photo m, final String dpid) {
        String imgBase64 = null;
        try {
            imgBase64 = FileUtils.getBase64Img(m.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onResponse: pa_id->  " + pa_id + "| project_id-> |" + project_id);
        Call<ResponseBody> responseBodyCall = ReqService.getAPIInterFace().doUploadImg(URL.URL_UPLOAD_IMG, pa_id, project_id, imgBase64, m.getDescription());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d(TAG, "onResponse: doUploadPhoto " + response.body());
//                    if(!response.body().string().isEmpty()) {
                    String sRes = response.body().string();
//                    Log.d(TAG, "onResponse: doUploadPhoto sRes" + sRes);
                    Log.d(TAG, "onResponse: isi sRes" + new Gson().toJson(sRes));
                    if (sRes.toLowerCase().contains("success")) {
                        currentLoop++;
                        float progress = ((float) currentLoop / max) * 100;
                        Log.d(TAG, "onResponse: " + progress);
                        progressDialog.setMessage("Uploading.. " + currentLoop + "/" + max + " more files...");
                        progressDialog.setProgress((int) progress);
                        uploadPhoto(pa_id, project_id, wpId, activityId, inputProgress_tx, true, dpid);
                        moveFotoToDir(baseActivity.getRealm(), m, wpId, activityId, true, inputProgress_tx, dpid);
//                        MActivity.updateProgress(realm.getDefaultInstance(), m.getProgress_photo(), m.getActivity_id());
//                        MActivity.updateProgress(realm, Integer.parseInt(inputProgress_tx), m.getActivity_id());
                    } else {
                        progressDialog.dismiss();
                        currentLoop = 0;
                        view.onSuccessSubmit(false, "Upload Failed Please try again... x32");
                    }
//                    }
                } catch (IOException e) {
                    progressDialog.dismiss();
                    currentLoop = 0;
                    view.onSuccessSubmit(false, "Upload Failed Please try again... x33");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onSuccessSubmit(false, t.getMessage());
                progressDialog.dismiss();
                currentLoop = 0;
            }
        });
    }


    @Override
    public void doneSubmit(String dpid, final String projectid, final String wpId, final String activityId, final boolean is_upload) {
        if (!progressDialog.isShowing())
            progressDialog.show();
        Call<ResponseBody> responseBodyCall = ReqService.getAPIInterFace().doneSubmit(URL.URL_UPDATE_STATUS, dpid);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String ss = response.body().string();
                    Log.d(TAG, "onResponse: isi ss" + ss);
                    if (ss.toLowerCase().contains("ok")
                            || ss.toLowerCase().contains("success")) {
                        new AsyncJob.AsyncJobBuilder<Boolean>()
                                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                    @Override
                                    public Boolean doAsync() {
                                        // Do some background
                                        realm = Realm.getDefaultInstance();
                                        try {
//                                          Project.updatePAID(realm,  projectid,inputProgress_tx, "");
                                            Project.updatePAID(realm, projectid, "");
                                            clearFoto(realm, projectid);
                                            return true;
                                        } catch (IOException e) {

                                            return false;
                                        } finally {
                                            realm.close();
                                        }
                                    }
                                }).doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                            @Override
                            public void onResult(Boolean result) {
                                progressDialog.dismiss();
                                view.onSuccessSubmit(true, result ? "" : "Some Foto Cant Delete must delete mannually..");
                            }
                        }).create().start();

                    } else {
                        progressDialog.dismiss();
                        view.onSuccessSubmit(false, "Upload Failed Please try again... x35");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    view.onSuccessSubmit(false, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onResponse: "+","+ t.getMessage());
                progressDialog.dismiss();
                view.onSuccessSubmit(false, t.getMessage());
            }

        });

    }

    void moveToDirOff(Realm realm, Photo m, String wp_id, String activity_id, int inputProgress_tx, boolean is_uploaded) throws IOException {
        Log.d(TAG, "tag isi m.getId() : count" + m.getId());

        InputStream in = null;
        OutputStream out = null;
        File newPath = new File(FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + "_" + "save_dir_off" + "_" + inputProgress_tx);
        String delPath = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + m.getProjectid() + "/";
        Log.e("tag dataPhoto", m.toString());

        Log.e("tag newPath", newPath.toString());
        String outputPath = newPath.toString();
        Log.e("tag outputPath", newPath.toString());

        try {
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
//            in = new FileInputStream(dataPhoto.getPath() + dataPhoto.getPath());
            in = new FileInputStream(m.getPath());
            Log.e("tag in", in.toString());
            File dataPhoto = new File(m.getPath());
            Log.e("tag dataPhoto", dataPhoto.getName());
//            out = new FileOutputStream(outputPath + dataPhoto.getPath());
            out = new FileOutputStream(m.getPath() + m.getId() + ".jpg");
            String isi_out = dataPhoto.getName() + m.getId() + ".jpg";
            Log.e("tag out", out.toString());
//
            String isiPath = newPath + "/" + dataPhoto.getName();
            Log.e("tag isiPath", isiPath.toString());
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            // write the output file
            out.flush();
            out.close();
            out = null;
            copyFileToDirectory(dataPhoto, newPath);
//
            Log.d(TAG, "isi m moveToDirOff := " + m);
            Log.d(TAG, "isi m inputProgress_tx moveToDirOff := " + inputProgress_tx);
            Photo.updatePathOff(realm, m, wp_id, activity_id, curent_date, time_now, isiPath, false, false, inputProgress_tx);
            // delete the original file
            new File(delPath + dataPhoto.getName()).delete();
            new File(delPath + isi_out).delete();
            File directory = new File(delPath);
            Log.d(TAG, "isi directory.list().length out" + directory.list().length);
            if (directory.list().length == 0) {
                view.onSuccessSubmit(false, "Data tersimpan di local storage.");
                closeRealm();
            }
        } catch (FileNotFoundException fnfe1) {
            Log.e("tag fnfe1", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag e", e.getMessage());
        }
    }

    /*
       https://stackoverflow.com/questions/300559/move-copy-file-operations-in-java
       https://stackoverflow.com/questions/4178168/how-to-programmatically-move-copy-and-delete-files-and-directories-on-sd

     */
    void moveFotoToDir(Realm realm, Photo m, String wp_id, String activity_id, boolean is_uploaded, String inputProgress_tx, String dpid) throws IOException {
        Log.d(TAG, "tag isi m.getId() : count" + m.getId());
        InputStream in = null;
        OutputStream out = null;
        Log.e("tag dataPhoto", m.toString());
//        String outputPath= dataPhoto.getPath();tid(
        File newPath = new File(FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + m.getProjectid() + "_" + "save_dir");
        String delPath = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + m.getProjectid() + "/";
//
        Log.e("tag newPath", newPath.toString());
        String outputPath = newPath.toString();
        Log.e("tag outputPath", newPath.toString());
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
//            in = new FileInputStream(dataPhoto.getPath() + dataPhoto.getPath());
            in = new FileInputStream(m.getPath());
            Log.e("tag in", in.toString());
            File dataPhoto = new File(m.getPath());
            Log.e("tag dataPhoto", dataPhoto.getName());
//
            out = new FileOutputStream(m.getPath() + m.getId() + ".jpg");
            String isi_out = dataPhoto.getName() + m.getId() + ".jpg";
            Log.e("isi m tag out", out.toString());
//
            String isiPath = newPath + "/" + dataPhoto.getName();
            Log.e("isi m isiPath", isiPath.toString());
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            // write the output file
            out.flush();
            out.close();
            out = null;
            copyFileToDirectory(dataPhoto, newPath);
//
            Log.d(TAG, "isi m moveFotoToDir :=" + m.getProjectid());
            Log.d(TAG, "isi m moveFotoToDir :=" + wp_id);
            Log.d(TAG, "isi m moveFotoToDir :=" + activity_id);
            Log.d(TAG, "isi m inputProgress_tx moveFotoToDir :=" + inputProgress_tx);

            Photo.updatePath(realm, m, wp_id, activity_id, curent_date, time_now, isiPath, true, true, Integer.parseInt(inputProgress_tx));
            // delete the original file
            new File(delPath + dataPhoto.getName()).delete();
            new File(delPath + isi_out).delete();

            File directory = new File(delPath);
            Log.d(TAG, "isi directory.list().length out" + directory.list().length);
            if (directory.list().length == 0) {
                offlineSubmit(dpid, m.getProjectid(), inputProgress_tx, wp_id, activity_id, inputProgress_tx, true, dpid);
                deletDir(baseActivity, delPath);
                newPath.setWritable(true, true);
            }
        } catch (FileNotFoundException fnfe1) {
            Log.e("tag fnfe1", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag e", e.getMessage());
            view.onSuccessSubmit(false, e.getMessage());
        }

    }

    void deletDir(Context context, String delPath) {
        try {
            FileUtils.delete(baseActivity, delPath);
            closeRealm();
        } catch (IOException e) {
            e.printStackTrace();
            view.onSuccessSubmit(false, e.getMessage());
        }
    }

    void closeRealm() {
        baseActivity.getRealm().close();
    }

    void clearFoto(Realm realm, String projectid) throws IOException {
        Log.d(TAG, "onRes isi projectid : count" + projectid);
//
    }


    //    @Override
//    public void offlineSubmit(String dpt_id, final String projectid, final String progress, final String wpId, final String activityId, final String inputProgress_tx, boolean is_online) {
//        //progressDialog.setMessage("Preparing..");
//        realm = Realm.getDefaultInstance();
//
////     OfflineDataTransaction local = realm.createObject(OfflineDataTransaction.class);
//       /*
//        * source : http://en.proft.me/2016/12/30/realm-database-tutorial-android/
//        */
////        AtomicLong nextId = new AtomicLong(realm.where(OfflineDataTransaction.class).max("id_offline_transaction").longValue()+ 1);
////        Long id = nextId.getAndIncrement();
////        Photo m = realm.where(Photo.class).equalTo(Photo.ID, Photo.getstringid(path)).equalTo(Photo.PROJECT_ID, project.getProject_id()).findFirst();
//
//        if (is_online == true) {
//
//            realm.beginTransaction();
//            OfflineDataTransaction local = new OfflineDataTransaction();
//            String id = UUID.randomUUID().toString();
//            local.setId_offline_transaction(id);
//            Project mproject = realm.where(Project.class).equalTo("project_id", projectid).findFirst();
//            local.setStatus_project_offline("1");
//            local.setDate_offline(curent_date);
//            local.setTime_offline(time_now);
//            local.setProject_id(projectid);
//            Log.d(TAG, "isi activityId : activityId" + activityId);
//            Log.d(TAG, "isi dptId : " + dpt_id + "===" + progress);
//            Log.d(TAG, "isi inputProgress_tx:" + inputProgress_tx);
//            local.setProgrees(inputProgress_tx);
//            local.setActivity_id(activityId);//dpt_id
//            Log.d(TAG, "isi wpId : mproject" + wpId);
//            local.setWp_id(wpId);
////          Log.d(TAG, "setSite_name : photo.getPath" + m.getPath());
//            local.setPhoto_path("tester");
////          Project mproject = realm.where(Project.class).equalTo("project_id", projectid).findFirst();
//            Log.d(TAG, "setSite_name : mproject" + mproject.getSite_name());
//            local.setSite_name(mproject.getSite_name());
//            realm.insertOrUpdate(local);
////            realm.copyToRealm(local);
//            realm.commitTransaction();
//            Log.d(TAG, "offlineSubmit : Added");
//            Log.d(TAG, "offlineSubmit : data tersimpan di local db" + local);
//        }else if (is_online == false) {
//            OfflineDataTransaction local = new OfflineDataTransaction();
//            OfflineDataTransaction cekData = realm.where(OfflineDataTransaction.class)
//                    .equalTo("project_id", projectid)
//                    .equalTo("wp_id", wpId)
//                    .equalTo("activity_id", activityId)
//                    .equalTo("progrees", inputProgress_tx)
//                    .equalTo("status_project_offline", "0")
//                    .findFirst();
//
//            Project mproject = realm.where(Project.class).equalTo("project_id", projectid).findFirst();
//
//            if (cekData != null) {
//                idOffline = cekData.getId_offline_transaction();
//            } else {
//                idOffline = null;
//            }
//            if (idOffline == null) {
//
//                realm.beginTransaction();
//                String id = UUID.randomUUID().toString();
//                local.setId_offline_transaction(id);
//                local.setStatus_project_offline("0");
//                Log.d(TAG, "Merge data foto is_offline" + is_online);
//                local.setDate_offline(curent_date);
//                local.setTime_offline(time_now);
//                local.setProject_id(projectid);
//                Log.d(TAG, "isi activityId : activityId" + activityId);
//                Log.d(TAG, "isi dptId : " + dpt_id + "===" + progress);
//                Log.d(TAG, "isi inputProgress_tx:" + inputProgress_tx);
//                local.setProgrees(inputProgress_tx);
//                local.setActivity_id(activityId);//dpt_id
//                Log.d(TAG, "isi wpId : mproject" + wpId);
//                local.setWp_id(wpId);
//                local.setPhoto_path("tester");
////          Project mproject = realm.where(Project.class).equalTo("project_id", projectid).findFirst();
//                Log.d(TAG, "setSite_name : mproject" + mproject.getSite_name());
//                local.setSite_name(mproject.getSite_name());
//                realm.insertOrUpdate(local);
////            realm.copyToRealm(local);
//                realm.commitTransaction();
//                Log.d(TAG, "offlineSubmit : Added");
//                Log.d(TAG, "offlineSubmit : data tersimpan di local db" + local);
//
//                if (is_online == false) {
//                    Log.d(TAG, "offlineSubmit : ");
//                    uploadPhoto(mproject.getPa_id(), projectid, wpId, activityId, progress, false);
//                    view.onSuccessSubmit(false, "data tersimpan di local,");
//                }
//
//            } else {
//                Log.d(TAG, "offlineSubmit : idOffline" + idOffline);
//                OfflineDataTransaction editData = realm.where(OfflineDataTransaction.class)
//                        .equalTo("id_offline_transaction", idOffline)
//                        .findFirst();
//                realm.beginTransaction();
////                if (is_online == false) {
//                editData.setStatus_project_offline("0");
//                Log.d(TAG, "Merge data foto is_offline" + is_online);
//
//                editData.setProgrees(progress);
//                editData.setProject_id(projectid);
//                editData.setWp_id(wpId);
//                editData.setActivity_id(activityId);
//                realm.copyToRealm(editData);
//                realm.commitTransaction();
//                if (is_online == false) {
//                    Log.d(TAG, "offlineSubmit : merge");
//                    uploadPhoto(mproject.getPa_id(), projectid, wpId, activityId, progress, false);
//                    view.onSuccessSubmit(false, "data tersimpan di local storage ,");
//
//                }
//
//            }
//        }
//    }
    @Override
    public void offlineSubmit(String dpt_id, final String projectid, final String progress, final String wpId, final String activityId, final String inputProgress_tx, boolean is_online, String dpid) {
        //progressDialog.setMessage("Preparing..");

        if (Integer.parseInt(progress) <= 0) {
            view.onErrorSubmit(false, "Nilai progress Tidak boleh <=0");
//            Toast.makeText(baseActivity.getBaseContext(),
//                    "Nilai progress Tidak boleh <=0",
//                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (max == 0) {
//            view.onError("Data Evidence Belum di isi");
            view.onErrorSubmit(false, "Data Evidence Belum di isi");
            progressDialog.dismiss();
            return;
        }
        baseActivity.getRealm().beginTransaction();
        Number currentIdNum = baseActivity.getRealm().where(OfflineDataTransaction.class).max(OfflineDataTransaction.idOffline);
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }

        OfflineDataTransaction dataOffline = new OfflineDataTransaction();
//        baseActivity.getRealm().beginTransaction();
        dataOffline.setId_offline_transaction(nextId);
        Project mproject = baseActivity.getRealm().where(Project.class).equalTo("project_id", projectid).findFirst();
        MActivity mActivity = baseActivity.getRealm().where(MActivity.class).equalTo("activity_id", activityId).findFirst();
        WorkPkg workPkg = baseActivity.getRealm().where(WorkPkg.class).equalTo("wp_id", wpId).findFirst();
        User user = baseActivity.getRealm().where(User.class).findFirst();
//
//        if (is_online == false) {
////            local.setActualStartDate(isitglActual);
//            Log.d(TAG, "Merge data foto is_offline" + is_online);
//            OfflineDataTransaction.insertOfflineData(baseActivity.getRealm(), dataOffline , nextId, projectid, progress, wpId, workPkg.getWp_name(), activityId, mActivity.getActivity_name(),
//                    false, mproject.getSite_name(), mproject.getArea(), mproject.getSow(), mproject.getRegional(), mproject.getNama_tenant(), "0",
//                    curent_date, time_now, user.getUsername(), mActivity.getActivity_plan_start(), mActivity.getActivity_plan_finish());
////            local.setStatus_project_offline("0");
//            uploadPhoto(mproject.getPa_id(), projectid, wpId, activityId, progress, false);
//            MActivity.updateProgress(baseActivity.getRealm(), Integer.parseInt(progress), activityId);
//        }
//        if (is_online == true) {
////            OfflineDataTransaction.insertOfflineData(baseActivity.getRealm(),nextId,);
//            Log.d(TAG, "Merge data foto is_online" + is_online);
//            OfflineDataTransaction.insertOfflineData(baseActivity.getRealm(),dataOffline, nextId, projectid, progress, wpId, workPkg.getWp_name(), activityId, mActivity.getActivity_name(),
//                    true, mproject.getSite_name(), mproject.getArea(), mproject.getSow(), mproject.getRegional(), mproject.getNama_tenant(), "1",
//                    curent_date, time_now, user.getUsername(), mActivity.getActivity_plan_start(), mActivity.getActivity_plan_finish());
////
////            uploadPhoto(mproject.getPa_id(), projectid, wpId, activityId, progress, true);
//            MActivity.updateProgress(baseActivity.getRealm(), Integer.parseInt(progress), activityId);
////            local.setStatus_project_offline("1");
////            local.setActualStartDate(isitglActual);
//        }

        dataOffline.setDate_offline(curent_date);
        dataOffline.setTime_offline(time_now);
        dataOffline.setProject_id(projectid);
        dataOffline.setUserName(user.getUsername());
        dataOffline.setUserId(user.getUser_id());
        Log.d(TAG, "isi activityId : activityId" + activityId);
        Log.d(TAG, "isi dptId : " + dpt_id + "===" + progress);
        Log.d(TAG, "isi inputProgress_tx:" + inputProgress_tx);
        dataOffline.setProgrees(progress);
        dataOffline.setActivity_id(activityId);//dpt_id
        if (is_online == false) {

            dataOffline.setStatus_project_offline("0");
            Log.d(TAG, "Merge data foto is_offline" + is_online);
        }
        if (is_online == true) {
            Log.d(TAG, "Merge data foto is_online" + is_online);
            dataOffline.setStatus_project_offline("1");
        }
//

        dataOffline.setActivity_name(mActivity.getActivity_name());
        dataOffline.setActivity_plan_start(mActivity.getActivity_plan_start());
        dataOffline.setActivity_plan_finish(mActivity.getActivity_plan_finish());
        Log.d(TAG, "isi wpId : mproject" + wpId);
        dataOffline.setWp_id(wpId);
        dataOffline.setWp_name(workPkg.getWp_name());
        dataOffline.setProject_id(projectid);
        dataOffline.setSow(mproject.getSow());
        dataOffline.setNamaTenant(mproject.getNama_tenant());
        dataOffline.setRegional(mproject.getRegional());
        dataOffline.setArea(mproject.getArea());
        dataOffline.setPhoto_path("tester");
        Log.d(TAG, "setSite_name : mproject" + mproject.getSite_name());
        dataOffline.setSite_name(mproject.getSite_name());
        baseActivity.getRealm().insertOrUpdate(dataOffline);
        baseActivity.getRealm().commitTransaction();
        Log.d(TAG, "offlineSubmit : Added");
        Log.d(TAG, "offlineSubmit : data tersimpan di local db" + dataOffline);

        if (is_online == false) {
            Log.d(TAG, "offlineSubmit : merge");
            uploadPhoto(mproject.getPa_id(), projectid, wpId, activityId, progress, false, dpid);
            MActivity.updateProgress(baseActivity.getRealm(), Integer.parseInt(progress), activityId);
//          view.onSuccessSubmit(false, "data tersimpan di local");
        }
        if (is_online == true) {
            MActivity.updateProgress(baseActivity.getRealm(), Integer.parseInt(progress), activityId);
        }
    }
}

