package com.cudo.mproject.Menu.TaskHistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.API.URL;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.TaskActivity.TaskActivityInterface;
import com.cudo.mproject.Menu.TaskHistory.TaskHistoryInterface.Presenter;
import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.Response.ResponseActivity;
import com.cudo.mproject.Model.Response.ResponseStatusApproval;
import com.cudo.mproject.Model.Response.ResponseSubmit;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.Utils.FileUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cudo.mproject.Utils.FileUtils.delete;
import static org.apache.commons.io.FileUtils.deleteDirectory;

/**
 * Created by adsxg on 12/22/2017.
 */

public class TaskHistoryPresenter implements TaskHistoryInterface.Presenter {

    String TAG = getClass().getSimpleName();
    BaseActivity baseActivity;
    ProgressDialog progressDialoghistory;
    int currentLoop = 0;
    static int max = 0;
    String project_id = null;
    //List<Photo> listUploaded;
    List<Photo> list;
    List<Integer> ids;
    Realm realm;
    TaskHistoryInterface.View view;
    //
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeF = new SimpleDateFormat(" hh:mm:ss");
    Date now = new Date();

    String curent_date = sdf.format(now);
    String time_now = timeF.format(now);

    public TaskHistoryPresenter(TaskHistoryInterface.View view, BaseActivity baseActivity, String idOfflineTrans) {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        OfflineDataTransaction dataLocals = realm.where(OfflineDataTransaction.class)
                .equalTo("id_offline_transaction", Integer.parseInt(idOfflineTrans))
//                .equalTo("userName", user.getUsername())
                .findFirst();
        Log.d(TAG, "onResHistory TaskHistoryPresenter: dataLocals view:" + dataLocals);
        project_id = dataLocals.getProject_id();
        this.view = view;
        this.baseActivity = baseActivity;
        progressDialoghistory = new ProgressDialog(baseActivity);
        progressDialoghistory.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialoghistory.setIndeterminate(false);
        progressDialoghistory.setTitle("Please Wait");
        progressDialoghistory.setProgress(0);
        RealmResults<Photo> asd = baseActivity.getRealm().where(Photo.class)
                .equalTo(Photo.PROJECT_ID, dataLocals.getProject_id())
                .equalTo("wp_id", dataLocals.getWp_id())
                .equalTo("activity_id", dataLocals.getActivity_id())
                .equalTo("date_photo", dataLocals.getDate_offline())
                .equalTo("time_photo", dataLocals.getTime_offline())
                .equalTo(Photo.UPLOADED, false)
                .equalTo("status_photo", "1")
                .findAll();
        list = baseActivity.getRealm().copyFromRealm(asd);
        max = list.size();
        Log.d(TAG, "onResHistory doSubmit max size: " + max);
    }

    @Override
    public void doSubmit(final String dpid, final String projectid, final String wpId, final String activityId, final String tglHistory, final String timeHistory, final String idLocal, final String inputProgress_tx) {
        String progress = inputProgress_tx.replaceAll("[^0-9]", "");
        Log.d(TAG, "onResHistory doSubmit History: " + dpid + "===" + progress);
        if (!progressDialoghistory.isShowing())
            progressDialoghistory.show();
        Log.d(TAG, "onResHistory doSubmit History: " + URL.URL_SUBMIT_PROGRESS_ACTIVITY + "/" + dpid + "/" + progress);
        Call<ResponseSubmit> responseBodyCall = ReqService.getAPIInterFace().doSubmit(URL.URL_SUBMIT_PROGRESS_ACTIVITY + "/" + dpid + "/" + progress);
        responseBodyCall.enqueue(new Callback<ResponseSubmit>() {
            @Override
            public void onResponse(Call<ResponseSubmit> call, Response<ResponseSubmit> response) {
                Log.d(TAG, "onResHistory doSubmit History: " + call);
                try {
                    ResponseSubmit responseSubmit = response.body();
                    Log.d(TAG, "onResHistory : responseSubmit.getPa_id() " + responseSubmit.getPa_id());
                    if (responseSubmit != null && responseSubmit.isSucces()) {
                        Log.d(TAG, "onResHistory History: responseSubmit.isSucces()) " + responseSubmit.isSucces());
                        Log.d(TAG, "onResHistory History: responseSubmit.getPa_id() " + responseSubmit.getPa_id());
                        OfflineDataTransaction dataLocal = realm.where(OfflineDataTransaction.class)
                                .equalTo("id_offline_transaction", Integer.parseInt(idLocal)).findFirst();
                        Log.d(TAG, "onResHistory TaskHistoryPresenter: projectid view:" + dataLocal.getProject_id());
                        Log.d(TAG, "onResHistory TaskHistoryPresenter: projectid view:" + projectid);
//                        OfflineDataTransaction.updateStatus(baseActivity.getRealm(), dataLocal);
                        Project.updatePAID(baseActivity.getRealm(), dataLocal.getProject_id(), responseSubmit.getPa_id());
                        uploadPhoto(responseSubmit.getPa_id(), idLocal, true);

                    } else {
                        progressDialoghistory.dismiss();
                        view.onSuccessSubmit(false, "Upload Failed Please try again... x31");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    view.onSuccessSubmit(false, "Upload Failed Please try again... x39");
                    progressDialoghistory.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseSubmit> call, Throwable t) {
                t.printStackTrace();
                view.onSuccessSubmit(false, t.getMessage());
                progressDialoghistory.dismiss();

            }

        });
    }

    @Override
    public void uploadPhoto(final String pa_id, String idLocal, boolean is_online) {

        OfflineDataTransaction dataLocal = realm.where(OfflineDataTransaction.class)
//                .equalTo(OfflineDataTransaction.idOffline, idLocal)
                .equalTo("id_offline_transaction", Integer.parseInt(idLocal))
                .findFirst();
        Log.d(TAG, "onResHistory void uploadPhoto History : pa_id + project_id+ wpId + activityId + tglHistory + timeHistory" +
                ":" + dataLocal.getPa_id() +
                ":" + dataLocal.getProject_id() +
                ":" + dataLocal.getWp_id() +
                ":" + dataLocal.getActivity_id() +
                ":" + dataLocal.getDate_offline() +
                ":" + dataLocal.getTime_offline());
        Log.d(TAG, "onResHistory pa_id" + pa_id);
        progressDialoghistory.setMessage("Uploading Data..");
        if (!progressDialoghistory.isShowing())
            progressDialoghistory.show();
        int i = currentLoop;
        Log.d(TAG, "onResHistory uploadPhoto currentLoop : " + currentLoop);
        if (is_online == true) {
//                online transcation, when device connect internet.
            if (currentLoop == max) {
                Log.d(TAG, "onResHistory currentLoop == max : " + max);
                Log.d(TAG, "onResHistory currentLoop == max : " + max + pa_id);
                doneSubmit(pa_id, project_id);
            } else {

                Log.d(TAG, "onResHistory uploadPhoto: " + (currentLoop + 1) + "/" + max);
//                List<Photo> list = realm.where(Photo.class).equalTo(Photo.PROJECT_ID, project_id).findAll();
//                int count = realm.copyFromRealm(realm.where(Photo.class).equalTo(Photo.PROJECT_ID, project_id).findAll()).size();

                List<Photo> list = realm.where(Photo.class)
                        .equalTo(Photo.PROJECT_ID, dataLocal.getProject_id())
                        .equalTo("wp_id", dataLocal.getWp_id())
                        .equalTo("activity_id", dataLocal.getActivity_id())
                        .equalTo("date_photo", dataLocal.getDate_offline())
                        .equalTo("time_photo", dataLocal.getTime_offline())
                        .equalTo("status_photo", "1")
                        .findAll();
                int count = realm.copyFromRealm(realm.where(Photo.class)
                        .equalTo(Photo.PROJECT_ID, dataLocal.getProject_id())
                        .equalTo("wp_id", dataLocal.getWp_id())
                        .equalTo("activity_id", dataLocal.getActivity_id())
                        .equalTo("date_photo", dataLocal.getDate_offline())
                        .equalTo("time_photo", dataLocal.getTime_offline())
                        .equalTo("status_photo", "1")
                        .findAll()).size();
                Log.d(TAG, "onResHistory isi uploadPhoto : count.size()" + count);
                List<Integer> list1 = new ArrayList<>();
                for (int k = 0; k < count; k++) {
                    list1.add(list.get(k).getId());
                }
                for (int k = 0; k < count; k++) {
                    Log.d(TAG, "onResHistory isi uploadPhoto : count" + k);
//                    Photo m = realm.where(Photo.class)
//                            .equalTo(Photo.PROJECT_ID, dataLocal.getProject_id())
//                            .equalTo(Photo.ID, list1.get(k)).findFirst();
                    Photo m = realm.where(Photo.class)
                            .equalTo(Photo.PROJECT_ID, dataLocal.getProject_id())
                            .equalTo(Photo.ID, list1.get(k))
//                            .equalTo("wp_id", dataLocal.getWp_id())
//                            .equalTo("activity_id", dataLocal.getActivity_id())
//                            .equalTo("date_photo", dataLocal.getDate_offline())
//                            .equalTo("time_photo", dataLocal.getTime_offline())
//                            .equalTo("status_photo", "1")
                            .findFirst();
//                  Log.d(TAG, "onResHistory isi uploadPhoto m " + m);
                    if (m != null) {
//                        baseActivity.getRealm().beginTransaction();
//                        m.setStatus_photo("1");
//                        m.setUploaded(true);
//                        m.setDate_photo(m.getDate_photo());
                        Log.d(TAG, "onResHistory uploadPhoto m.getDate_photo() " + m.getDate_photo());
                        Log.d(TAG, "onResHistory uploadPhoto m.getProjectid() " + m.getProjectid());
                        Log.d(TAG, "onResHistory uploadPhoto m.getWp_id() " + m.getWp_id());
                        Log.d(TAG, "onResHistory uploadPhoto m.getStatus_photo() " + m.getStatus_photo());
                        Log.d(TAG, "onResHistory uploadPhoto m.getActivity_id() " + m.getActivity_id());

                    }

                }

                try {
                    Log.d(TAG, "onResHistory uploadPhoto: ");
                    Photo m = list.get(i);
//                  progressDialoghistory.setMessage("Uploading.. " + currentLoop + "/" + max + " more files...");
//                  doUploadPhoto(dataLocal.getPa_id(), dataLocal.getProject_id(), dataLocal.getWp_id(), dataLocal.getActivity_id(), dataLocal.getDate_offline(), dataLocal.getTime_offline(), dataLocal, m);
                    doUploadPhoto(pa_id, dataLocal.getProject_id(), dataLocal, m);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialoghistory.dismiss();
                    view.onSuccessSubmit(false, "Upload upload suksesFailed Please try again... x311");

                }
            }
        }
    }


    public void doUploadPhoto(final String pa_id, final String project_id, final OfflineDataTransaction dataLocal, final Photo m) {
        String imgBase64 = null;
        try {
            imgBase64 = FileUtils.getBase64Img(m.getPath());
            Log.d(TAG, "onResHistory doUploadPhoto History: m.getPath() ->  " + m.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onResHistory doUploadPhoto: pa_id->  " + pa_id + "| project_id-> |" + project_id);
        Call<ResponseBody> responseBodyCall = ReqService.getAPIInterFace().doUploadImg(URL.URL_UPLOAD_IMG, pa_id, project_id, imgBase64, m.getDescription());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d(TAG, "onResHistory: doUploadPhoto History" + response.body());
                    String sRes = response.body().string();
                    Log.d(TAG, "onResHistory: doUploadPhoto History sRes" + sRes);
                    if (sRes.toLowerCase().contains("success")) {
                        currentLoop++;
                        float progress = ((float) currentLoop / max) * 100;
                        Log.d(TAG, "onResHistory progress : " + progress);
                        progressDialoghistory.setMessage("Uploading.. " + currentLoop + "/" + max + " more files...");
                        progressDialoghistory.setProgress((int) progress);
                        Photo.uploaded(baseActivity.getRealm(), m);
                        Log.d(TAG, "onResHistory uploadPhoto update: ");
                        OfflineDataTransaction.updateStatus(baseActivity.getRealm(), String.valueOf(dataLocal.getId_offline_transaction()));
//                      OfflineDataTransaction.updateStatus(dataLocal.getId_offline_transaction());
                        MActivity.updateProgress(realm, m.getProgress_photo(), m.getActivity_id());
                        uploadPhoto(pa_id, String.valueOf(dataLocal.getId_offline_transaction()), true);
                        moveFotoToDir(realm, m, dataLocal.getWp_id(), dataLocal.getActivity_id(), dataLocal.getDate_offline(), dataLocal.getTime_offline(), m.getProgress_photo(), true);
                    } else {
                        progressDialoghistory.dismiss();
                        currentLoop = 0;
                        view.onSuccessSubmit(false, "Upload Failed Please try again... x32");
                    }

                } catch (IOException e) {
                    progressDialoghistory.dismiss();
                    currentLoop = 0;
                    view.onSuccessSubmit(false, "Upload Failed Please try again... x33");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onSuccessSubmit(false, t.getMessage());
                progressDialoghistory.dismiss();
                currentLoop = 0;
            }
        });
    }


    @Override
    public void doneSubmit(String dpid, final String project_id) {

        Log.d(TAG, "onResHistory: isi history dpid " + dpid);
        Log.d(TAG, "onResHistory: isi history project_id " + project_id);

        if (!progressDialoghistory.isShowing())
            progressDialoghistory.show();
        Call<ResponseBody> responseBodyCall = ReqService.getAPIInterFace().doneSubmit(URL.URL_UPDATE_STATUS, dpid);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResHistory: isi responseBodyCall " + response);
                try {
                    String ss = response.body().string();
                    Log.d(TAG, "onResHistory: isi history ss " + ss);
                    if (ss.toLowerCase().contains("ok")
                            || ss.toLowerCase().contains("success")) {
                        new AsyncJob.AsyncJobBuilder<Boolean>()
                                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                    @Override
                                    public Boolean doAsync() {
                                        // Do some background
                                        Realm realm = Realm.getDefaultInstance();
                                        try {
                                            Project.updatePAID(realm, project_id, "");
                                            clearFoto(realm, project_id);
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
                                progressDialoghistory.dismiss();
                                view.onSuccessSubmit(true, result ? "" : "Some Foto Cant Delete must delete mannually..");
                            }
                        }).create().start();

                    } else {
                        progressDialoghistory.dismiss();
                        view.onSuccessSubmit(false, "Upload Failed Please try again... x35");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialoghistory.dismiss();
                    view.onSuccessSubmit(false, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialoghistory.dismiss();
                view.onSuccessSubmit(false, t.getMessage());
            }

        });

    }

//    @Override
//    public void getStatusApproval(final String activityId, final String dpt_id, final String userid) {
//        final Call<ResponseBody> responseStatusApproval = ReqService.getAPIInterFace().doGetActivity(URL.URL_GET_STATUS_APP + "/" + activityId + "/" + dpt_id + "/" + userid);
//        responseStatusApproval.enqueue(new Callback<ResponseBody>() {
//
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    if (response.isSuccessful()) {
//                        String responseString = response.body().string();
//                        JsonArray jarr = new JsonParser().parse(responseString).getAsJsonArray();
//                         ResponseStatusApproval responseStatusApproval = new ResponseStatusApproval();
//                        responseStatusApproval.setJsonArray(jarr);
//                        view.onSuccess(true, dpt_id, responseStatusApproval.getAs_id());
//                        Log.d(TAG, "onResponse: " + new Gson().toJson(responseString));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    view.onErrorSubmit(false, "error");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        }
//    }

    // moveFoto to another directory
    void moveFotoToDir(Realm realm, Photo m, String wp_id, String activity_id, String tgl_local, String time_local, int progressPhoto, boolean is_uploaded) throws IOException {
        Log.d(TAG, "tag isi m.getId() : count" + m.getId());

        InputStream in = null;
        OutputStream out = null;
        Log.e("tag dataPhoto", m.toString());
//        String outputPath= dataPhoto.getPath();
        File newPath = new File(FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + m.getProjectid() + "_" + "save_dir");
//        String delPath = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + m.getProjectid() + "/";
        String delPathOff = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + "_" + "save_dir_off" + "_" + progressPhoto + "/";

//                File newPath = new File( m.getPath()+"_"+"save_dir");
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
            org.apache.commons.io.FileUtils.copyFileToDirectory(dataPhoto, newPath);
//
            Photo.updatePath(realm, m, wp_id, activity_id, tgl_local, time_local, isiPath, true, true, 0);
            // delete the original file
            Log.d(TAG, "isi delPathOff + dataPhoto.getName()" + delPathOff + dataPhoto.getName());
            new File(delPathOff + dataPhoto.getName()).delete();
            new File(delPathOff + isi_out).delete();
            // delete the original directory
//            https://stackoverflow.com/questions/3987921/not-able-to-delete-the-directory-through-java
            File directory = new File(delPathOff);
            Log.d(TAG, "isi directory.list().length out" + directory.list().length);
            if (directory.list().length == 0) {
                Log.d(TAG, "isi directory.list().length in" + directory.list().length);

                deletDir(baseActivity, delPathOff);
            }
            // delete the original file
//            new File(delPath + dataPhoto.getName()).delete();
//            new File(delPath + isi_out).delete();
//            new File(delPath + dataPhoto.getName()).delete();
//            new File(delPath + isi_out).delete();
//            String value_delte_1 = delPath + out.toString();
//            String value_delte_2 = delPath + dataPhoto.getName();

//            Photo.updatePath(realm, m, wp_id, activity_id, isiPath);
        } catch (FileNotFoundException fnfe1) {
            Log.e("tag fnfe1", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag e", e.getMessage());
        }

    }

    void deletDir(Context context, String delPathOff) {
        try {
            FileUtils.delete(baseActivity, delPathOff);
            closeRealm();
        } catch (IOException e) {
            e.printStackTrace();
            view.onSuccessSubmit(true, e.getMessage());
        }
    }

    void closeRealm() {
        baseActivity.getRealm().close();
    }

//   void deletDir(File fileOrDirectory) {
//       try {
//           org.apache.commons.io.FileUtils.forceDeleteOnExit(fileOrDirectory);
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
//   }
//    public void deleteRecursive(File fileOrDirectory) {
//
//        if (fileOrDirectory.isDirectory()) {
//            for (File child : fileOrDirectory.listFiles()) {
//                deleteRecursive(child);
//            }
//        }
//
//        fileOrDirectory.delete();
//    }

    void clearFoto(Realm realm, String projectid) throws IOException {
        Log.d(TAG, "onResHistory isi projectid : count" + projectid);
//        List<Photo> list = realm.where(Photo.class).equalTo(Photo.PROJECT_ID, projectid).findAll();
//        int count = realm.copyFromRealm(realm.where(Photo.class).equalTo(Photo.PROJECT_ID, projectid).findAll()).size();
//        List<Integer> list1 = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            list1.add(list.get(i).getId());
//        }
//        for (int i = 0; i < count; i++) {
//            Log.d(TAG, "onResHistory isi activityId : count" + i);
//            Photo m = realm.where(Photo.class)
//                    .equalTo(Photo.PROJECT_ID, projectid)
//                    .equalTo(Photo.ID, list1.get(i)).findFirst();
//
//            if (m != null) {
//                realm.beginTransaction();
//                FileUtils.delete(baseActivity, m.getPath());
//                Photo.delete(realm, m);
//                m.setStatus_photo("1");
//                realm.commitTransaction();
//                sendBroadcastUpdate();
//            }
//        }
    }

}




