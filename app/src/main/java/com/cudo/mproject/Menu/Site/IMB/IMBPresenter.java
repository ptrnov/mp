package com.cudo.mproject.Menu.Site.IMB;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.API.URL;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Model.DataIMB;
import com.cudo.mproject.Model.PhotoIMB;
import com.cudo.mproject.Model.Response.ResponseIMB;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.Utils.FileUtils;
import com.google.gson.Gson;

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

import static org.apache.commons.io.FileUtils.copyFileToDirectory;

public class IMBPresenter implements IMBInterface.Presenter {
    String TAG = getClass().getSimpleName();
    IMBInterface.View view;
    BaseActivity baseActivity;
    ProgressDialog progressDialog;
    public static Realm realm;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeF = new SimpleDateFormat(" hh:mm:ss");
    Date now = new Date();

    String curent_date = sdf.format(now);
    String time_now = timeF.format(now);
    int currentLoop = 0;
    static int max = 0;

    List<PhotoIMB> list;

    public IMBPresenter(IMBInterface.View view, BaseActivity baseActivity, String projectId) {
        this.view = view;
        this.baseActivity = baseActivity;
        progressDialog = new ProgressDialog(baseActivity);
        RealmResults<PhotoIMB> asd =
                baseActivity.getRealm().where(PhotoIMB.class)
                        .equalTo(PhotoIMB.UPLOADED, false)
                        .equalTo(PhotoIMB.PROJECT_ID, projectId)
                        .findAll();
        list = baseActivity.getRealm().copyFromRealm(asd);
        max = list.size();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setProgress(0);
    }

    @Override
    public void submitDataIMB(final String projectId, final String namaPemilikLahan, final String noIMB, final String alamatIMB, final String tglAwalMasaBerlaku, final String tglAkhirMasaBerlaku, final String isOnline) {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userId = user.getUser_id();

        if (isOnline == "1") {
            Call<ResponseIMB> responseBodyCall = ReqService.getAPIInterFace()
                    .dataIMBSitePost(URL.URL_Submit_Data_IMB, user.getUser_id(), user.getUsername(), user.getArea(),
                            projectId, namaPemilikLahan,noIMB,
                            alamatIMB, tglAwalMasaBerlaku, tglAkhirMasaBerlaku, isOnline);
            Log.d(TAG, "onResponse: 2" + URL.URL_Submit_Data_IMB + "," + user.getUser_id() + "," +
                    user.getUsername() + "," + projectId + "," + namaPemilikLahan + "," +
                    noIMB + "," + alamatIMB + "," + tglAwalMasaBerlaku + "," + tglAkhirMasaBerlaku + "," + isOnline);
            responseBodyCall.enqueue(new Callback<ResponseIMB>() {
                @Override
                public void onResponse(Call<ResponseIMB> call, Response<ResponseIMB> response) {
                    try {
                            Log.e("onResponse", response.body().toString());
                        ResponseIMB responseIMB = response.body();
                        Log.d(TAG, "onResponse: isi rs" + new Gson().toJson(responseIMB));
                        if (responseIMB.getMessage().matches("saPidx001")) {
                            view.onSuccessSubmit(false, "PROEJCT ID" + projectId + "TIDAK TERDAFTAR");
                            progressDialog.dismiss();
                        } else if (responseIMB.getMessage().matches("saUiNx001")) {
                            view.onSuccessSubmit(false, "User dengan Username" + user.getUsername() + "TIDAK TERDAFTAR");
                            progressDialog.dismiss();
                        } else if (responseIMB.getMessage().matches("SUBMIT")) {
//                            view.onSuccessSubmit(false, "SUBMIT");
                            //                        PhotoPerson photoPerson = list.get(i);
                            Integer data_imb_site_id = Integer.parseInt(responseIMB.data_imb_site_id());
                            uploadImageIMB(userId, userName, projectId, namaPemilikLahan,
                                    noIMB, alamatIMB, tglAwalMasaBerlaku,
                                    tglAkhirMasaBerlaku, isOnline, data_imb_site_id);
//                            Log.d(TAG, "onResponse: responsePerson.getMessage()" + responsePerson.getMessage().toString());
//                            uploadDataPerson(user.getUser_id(), user.getUsername(), projectId, namaPenjagaLahan, alamat, noKTP, noTlp, isOnline, penjaga_lahan_site_project_id);
//                            inputDataPerson(penjaga_lahan_site_project_id,user.getUser_id().toString(), user.getUsername(), projectId, namaPenjagaLahan, alamat, noKTP, noTlp, isOnline);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + "," + e.getMessage());
                        progressDialog.dismiss();
                        view.onSuccessSubmit(false, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseIMB> call, Throwable t) {
                    Log.d(TAG, "onResponse: " + "," + t.getMessage());
                    view.onSuccessSubmit(false, "FAILED");//"FAILED"
                    progressDialog.dismiss();
                }
            });
        } else {
            //is_online=2
            inputDataIMB(user.getUser_id(), user.getUsername(), projectId, namaPemilikLahan, noIMB, alamatIMB, tglAwalMasaBerlaku, tglAkhirMasaBerlaku, isOnline);
        }
    }

    void uploadImageIMB(String userId, String userName, String projectId, String namaPemilikLahan,
                        String noIMB, String alamatIMB, String tglAwalMasaBerlaku,
                        String tglAkhirMasaBerlaku, String isOnline, int data_imb_site_id) {

        int i = currentLoop;
        if (!progressDialog.isShowing())
            progressDialog.show();
        progressDialog.setMessage("Uploading Data..");
        if (isOnline == "1") {
            if (currentLoop == max) {
                doneSubmit(projectId, isOnline);
//                inputDataPerson(penjaga_lahan_site_project_id,userId, userName, projectId, namaPenjagaLahan, alamat, noKtp, noTlp, isOnline);
            } else {
                Log.d(TAG, "tag uploadPhoto: " + (currentLoop + 1) + "/" + max);
//                realm = Realm.getDefaultInstance();
/*
                 upadate status photo when connect internet
 */
                try {
                    Log.d(TAG, "uploadPhoto: ");
                    PhotoIMB photoIMB = list.get(i);
                    //progressDialog.setMessage("Uploading.. "+ currentLoop+"/"+max +" more files...");
                    doUploadPhoto(photoIMB, userId, userName, projectId, namaPemilikLahan, noIMB, alamatIMB, tglAwalMasaBerlaku, tglAkhirMasaBerlaku, isOnline, data_imb_site_id);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    view.onErrorSubmit(false, "Upload upload suksesFailed Please try again... x311");
                }
            }
        } else {
            if (currentLoop == max) {
//                doneSubmit(pa_id, project_id, wpId, activityId, false);
                doneSubmit(projectId, isOnline);
            } else {
                List<PhotoIMB> list = baseActivity.getRealm().where(PhotoIMB.class)
                        .equalTo(PhotoIMB.PROJECT_ID, projectId)
                        .equalTo("status_photo", "0")
                        .findAll();
                int count = baseActivity.getRealm().copyFromRealm(baseActivity.getRealm().where(PhotoIMB.class)
                        .equalTo(PhotoIMB.PROJECT_ID, projectId)
                        .equalTo("status_photo", "0")
                        .findAll()).size();
                List<Integer> list1 = new ArrayList<>();
                for (int k = 0; k < count; k++) {
                    list1.add(list.get(k).getId());
                }
                for (int k = 0; k < count; k++) {
                    Log.d(TAG, "isi uploadPhoto : count" + k);
                    PhotoIMB m = baseActivity.getRealm().where(PhotoIMB.class)
                            .equalTo(PhotoIMB.PROJECT_ID, projectId)
                            .equalTo(PhotoIMB.ID, list1.get(k)).findFirst();
//                    MActivity mActivity = realm.where(MActivity.class)
//                            .equalTo("activity_id", activityId).findFirst();
//
                    if (m != null) {
//                        realm.beginTransaction();
                        PhotoIMB.offlinePhoto(baseActivity.getRealm(), m, curent_date, time_now, 0);

//                        m.setStatus_photo("1");
//                        m.setUploaded(false);
//                        m.setDate_photo(curent_date);
//                        m.setTime_photo(time_now);
//                        m.setProgress_photo(Integer.parseInt(inputProgress_tx));
//                        m.setProgress_photo(Integer.parseInt(dataLocal.getProgrees()));
                        Log.d(TAG, "isi uploadPhoto m.getTime_photo() " + m.getDateSubmit());
                        Log.d(TAG, "isi uploadPhoto m.getProjectid() " + m.getProjectid());
//                        realm.commitTransaction();
                        Log.d(TAG, "doSubmit: Merge data foto off,");
                        try {
                            moveToDirOff(baseActivity.getRealm(), m);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.d(TAG, "uploadPhotoPerson offline: " + (currentLoop + 1) + "/" + max);
            }
        }
    }

    public void doUploadPhoto(PhotoIMB photoIMB, String userId, String userName, String projectId, String
            namaPemilikLahan, String noIMB, String alamatIMB, String tglAwalMasaBerlaku, String tglAkhirMasaBerlaku,
                              String isOnline, Integer data_imb_site_id) {
        String imgBase64 = null;
        try {
            imgBase64 = FileUtils.getBase64Img(photoIMB.getPathPhotoIMB());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<ResponseBody> responseBodyCall = ReqService.getAPIInterFace().
                doUploadIMBImg(URL.URL_Upload_Image_IMB, data_imb_site_id,photoIMB.getDescription(),imgBase64);
        Log.d(TAG, "onResponse: 3" + "," + URL.URL_Upload_Image_IMB + "," + data_imb_site_id + "," + imgBase64);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d(TAG, "onResponse: doUploadPhoto " + response.body());
//                    if(!response.body().string().isEmpty()) {
                    String sRes = response.body().string();
                    Log.d(TAG, "onResponse: doUploadPhoto sRes" + sRes);
//                    Log.d(TAG, "onResponse: isi sRes" + new Gson().toJson(sRes));
                    if (sRes.toLowerCase().contains("success")) {
                        currentLoop++;
                        float progress = ((float) currentLoop / max) * 100;
                        Log.d(TAG, "onResponse: " + progress);
                        progressDialog.setMessage("Uploading.. " + currentLoop + "/" + max + " more files...");
                        progressDialog.setProgress((int) progress);
                        uploadImageIMB(userId, userName, projectId,
                                namaPemilikLahan, noIMB, alamatIMB, tglAwalMasaBerlaku,
                                tglAkhirMasaBerlaku, isOnline, data_imb_site_id);
                        moveFotoToDir(baseActivity.getRealm(),photoIMB,true);
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
        inputDataIMB(userId, userName, projectId, namaPemilikLahan, noIMB, alamatIMB, tglAwalMasaBerlaku, tglAkhirMasaBerlaku, isOnline);
    }

    void inputDataIMB(String userId, String userName, String projectId, String namaPemilikLahan, String noIMB,
                      String alamatIMB, String tglAwalMasaBerlaku, String tglAkhirMasaBerlaku, String isOnline) {
        baseActivity.getRealm().beginTransaction();
        Number currentIdNum = baseActivity.getRealm().where(DataIMB.class).max("idIMB");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }

        DataIMB dataIMB = new DataIMB();
        dataIMB.setIdIMB(nextId);
        dataIMB.setUserId(userId);
        dataIMB.setUserName(userName);
        dataIMB.setProject_id(projectId);
        dataIMB.setNama_imb(namaPemilikLahan);
        dataIMB.setNo_imb(noIMB);
        dataIMB.setAlamat_imb(alamatIMB);
        dataIMB.setTglMasaBerlakuAwal(tglAwalMasaBerlaku);
        dataIMB.setTglMasaBerlakuAkhir(tglAkhirMasaBerlaku);
        dataIMB.setIs_submited(isOnline);
        baseActivity.getRealm().insertOrUpdate(dataIMB);
        baseActivity.getRealm().commitTransaction();
        Log.d(TAG, "dataIMB : Added");
        Log.d(TAG, "dataIMB : data tersimpan di local db" + dataIMB);
//        if (isOnline == "1") {
//            view.onSuccessSubmit(true, "Data IMB Submited");
//            closeRealm();
//        }
//        if (isOnline == "2") {
//            view.onSuccessSubmit(true, "Data Submited");
//            closeRealm();
//        }
    }
    @Override
    public void  doneSubmit(String projectId, String isOnline) {
        if (isOnline == "1") {
            view.onSuccessSubmit(true, "Data IMB Submited");
            closeRealm();
        }
        if (isOnline == "2") {
            view.onSuccessSubmit(true, "Data Submited");
            closeRealm();
        }
    }

    void moveToDirOff(Realm realm, PhotoIMB m) throws IOException {
        Log.d(TAG, "onResponse m.getId() : count" + m.getId());

        InputStream in = null;
        OutputStream out = null;
        File newPath = new File(FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + "_" + "save_dir_off_imb" + "_" + m.getProjectid());
        String delPath = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator +"imb_image"+"_"+m.getProjectid() + "/";
        Log.e("onResponse dataPhoto", m.toString());

        Log.e("onResponse newPath", newPath.toString());
        String outputPath = newPath.toString();
        Log.e("onResponse outputPath", newPath.toString());

        try {
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
//            in = new FileInputStream(dataPhoto.getPath() + dataPhoto.getPath());
            in = new FileInputStream(m.getPathPhotoIMB());
            Log.e("onResponse in", in.toString());
            File dataPhoto = new File(m.getPathPhotoIMB());
            Log.e("onResponse dataPhoto", dataPhoto.getName());
//            out = new FileOutputStream(outputPath + dataPhoto.getPath());
            out = new FileOutputStream(m.getPathPhotoIMB() + m.getId() + ".jpg");
            String isi_out = dataPhoto.getName() + m.getId() + ".jpg";
            Log.e("onResponse out", out.toString());
//
            String isiPath = newPath + "/" + dataPhoto.getName();
            Log.e("onResponse isiPath", isiPath.toString());
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
            Log.d(TAG, "isi onResponse moveToDirOff := " + m);
            PhotoIMB.updatePathOff(realm, m, curent_date, time_now, isiPath, false, false);
            // delete the original file
            new File(delPath + dataPhoto.getName()).delete();
            new File(delPath + isi_out).delete();
            File directory = new File(delPath);
            Log.d(TAG, "onResponse directory.list().length out" + directory.list().length);
            if (directory.list().length == 0) {
                view.onSuccessSubmit(false, "data tersimpan di local storage.");
                closeRealm();
            }
        } catch (FileNotFoundException fnfe1) {
            Log.e("onResponse fnfe1", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("onResponse e", e.getMessage());
        }
    }

    void moveFotoToDir(Realm realm, PhotoIMB m, boolean is_uploade) throws IOException {
        Log.d(TAG, "tag isi m.getId() : count" + m.getId());
        InputStream in = null;
        OutputStream out = null;
        Log.e("tag dataPhoto", m.toString());
//        String outputPath= dataPhoto.getPath();tid(
        File newPath = new File(FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + m.getProjectid() + "_" + "save_dir");
        String delPath = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator +"imb_image"+"_"+m.getProjectid() + "/";
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
            in = new FileInputStream(m.getPathPhotoIMB());
            Log.e("tag in", in.toString());
            File dataPhoto = new File(m.getPathPhotoIMB());
            Log.e("tag dataPhoto", dataPhoto.getName());
//
            out = new FileOutputStream(m.getPathPhotoIMB() + m.getId() + ".jpg");
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

            PhotoIMB.updatePath(realm, m, curent_date, time_now, m.getPathPhotoIMB(), true);
            // delete the original file
            new File(delPath + dataPhoto.getName()).delete();
            new File(delPath + isi_out).delete();

            File directory = new File(delPath);
            Log.d(TAG, "isi directory.list().length out" + directory.list().length);
            if (directory.list().length == 0) {
//                offlineSubmit(dpid, m.getProjectid());
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
        progressDialog.dismiss();
    }
}
