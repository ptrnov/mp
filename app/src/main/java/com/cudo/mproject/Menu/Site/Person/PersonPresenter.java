package com.cudo.mproject.Menu.Site.Person;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.cudo.mproject.API.ReqService;
import com.cudo.mproject.API.URL;
import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Model.DataPenjagaLahan;
import com.cudo.mproject.Model.PhotoPerson;
import com.cudo.mproject.Model.Response.ResponsePerson;
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

/**
 * date          editing by        method                            Description
 * 02/02/2018     newbiecihuy
 * 02/02/2018     newbiecihuy
 * 03/03/2018     newbiecihuy
 * 25/04/2018     newbiecihuy
 */
public class PersonPresenter implements PersonInterface.Presenter {
    String TAG = getClass().getSimpleName();
    PersonInterface.View view;
    BaseActivity baseActivity;
    ProgressDialog progressDialog;

    int currentLoop = 0;
    static int max = 0;
    public static Realm realm;
    List<PhotoPerson> list;
    //    public User user;
    //
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeF = new SimpleDateFormat(" hh:mm:ss");
    Date now = new Date();

    String curent_date = sdf.format(now);
    String time_now = timeF.format(now);

    public PersonPresenter(PersonInterface.View view, BaseActivity baseActivity, String projectId) {
        this.view = view;
        this.baseActivity = baseActivity;
        progressDialog = new ProgressDialog(baseActivity);
        RealmResults<PhotoPerson> asd =
                baseActivity.getRealm().where(PhotoPerson.class)
                        .equalTo(PhotoPerson.UPLOADED, false)
                        .equalTo(PhotoPerson.PROJECT_ID, projectId)
                        .findAll();
        list = baseActivity.getRealm().copyFromRealm(asd);
        max = list.size();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setProgress(0);

    }

    @Override
    public void submitDataPerson(String projectId, String namaPenjagaLahan, String alamat, String provinsi
            , String kabupaten, String kecamatan, String noKTP, String noTlp, String isOnline) {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        String userName = user.getUsername();
        String userId = user.getUser_id();

        if (isOnline == "1") {
            Log.d(TAG, "onResponse: isi projectId, isOnline, list photo" + "," + projectId + "," + isOnline + "," + max);
            Call<ResponsePerson> responseBodyCall = ReqService.getAPIInterFace()
                    .dataPenjagaLahanPost(URL.URL_Submit_Data_PenjagaLahan, user.getUser_id(), user.getUsername(),
                            user.getArea(),projectId, namaPenjagaLahan,
                            alamat, provinsi, kabupaten, kecamatan, noKTP, noTlp, isOnline);
            Log.d(TAG, "onResponse: 2" + URL.URL_Submit_Data_PenjagaLahan + "," + user.getUser_id() + "," + user.getUsername()
                    + "," + projectId +"," + namaPenjagaLahan + "," + alamat + "," + provinsi + "," +
                    kabupaten + "," + kecamatan + "," + noKTP + "," + noTlp + "," + isOnline);
            responseBodyCall.enqueue(new Callback<ResponsePerson>() {
                @Override
                public void onResponse(Call<ResponsePerson> call, Response<ResponsePerson> response) {
                    try {
                        Log.d(TAG, "onResponse: void");
                        ResponsePerson responsePerson = response.body();
                        Log.d(TAG, "onResponse: isi rs" + new Gson().toJson(responsePerson));
                        if (responsePerson.getMessage().matches("saPidx001")) {
                            view.onSuccessSubmit(false, "PROEJCT ID" + projectId + "TIDAK TERDAFTAR");
                            progressDialog.dismiss();
                        } else if (responsePerson.getMessage().matches("saUiNx001")) {
                            view.onSuccessSubmit(false, "User dengan Username" + user.getUsername() + "TIDAK TERDAFTAR");
                            progressDialog.dismiss();
                        } else if (responsePerson.getMessage().matches("SUBMIT")) {
//                            view.onSuccessSubmit(false, "SUBMIT");
                            //                        PhotoPerson photoPerson = list.get(i);
                            Integer penjaga_lahan_site_project_id = Integer.parseInt(responsePerson.penjaga_lahan_site_project_id());
//                            Log.d(TAG, "onResponse: responsePerson.getMessage()" + responsePerson.getMessage().toString());
                            uploadDataPerson(user.getUser_id(), user.getUsername(), projectId, namaPenjagaLahan, alamat,provinsi,kabupaten,kecamatan, noKTP, noTlp, isOnline, penjaga_lahan_site_project_id);
//                            inputDataPerson(penjaga_lahan_site_project_id,user.getUser_id().toString(), user.getUsername(), projectId, namaPenjagaLahan, alamat, noKTP, noTlp, isOnline);
                        }
                        if (responsePerson.getMessage().matches("FAILED")) {
                            view.onSuccessSubmit(false, "FAILED");
                            progressDialog.dismiss();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + "," + e.getMessage());
                        progressDialog.dismiss();
                        view.onSuccessSubmit(false, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponsePerson> call, Throwable t) {
                    Log.d(TAG, "onResponse: " + "," + t.getMessage());
                    view.onSuccessSubmit(false, "FAILED");//"FAILED"
                    progressDialog.dismiss();
                }
            });
        } else {
            //is_online=2
            inputDataPerson(user.getUser_id(), user.getUsername(), projectId, namaPenjagaLahan, alamat,provinsi,kabupaten,kecamatan, noKTP, noTlp, isOnline);
        }
    }

    public void uploadDataPerson(String userId, String userName, String projectId, String namaPenjagaLahan,
                                 String alamat, String provinsi, String kabupaten, String kecamatan,
                                 String noKtp, String noTlp, String isOnline, int penjaga_lahan_site_project_id) {
        int i = currentLoop;
        if (!progressDialog.isShowing())
            progressDialog.show();
        progressDialog.setMessage("Uploading Data..");

        if (isOnline == "1") {
            if (currentLoop == max) {
                doneSubmit(projectId,isOnline);
//                inputDataPerson(penjaga_lahan_site_project_id,userId, userName, projectId, namaPenjagaLahan, alamat, noKtp, noTlp, isOnline);
            } else {
                Log.d(TAG, "tag uploadPhoto: " + (currentLoop + 1) + "/" + max);
//                realm = Realm.getDefaultInstance();
/*
                 upadate status photo when connect internet
 */
                try {
                    Log.d(TAG, "uploadPhoto: ");
                    PhotoPerson photoPerson = list.get(i);
                    //progressDialog.setMessage("Uploading.. "+ currentLoop+"/"+max +" more files...");
                    doUploadPhoto(photoPerson, userId, userName, projectId, namaPenjagaLahan, alamat, provinsi,kabupaten, kecamatan,
                            noKtp, noTlp, isOnline, penjaga_lahan_site_project_id);
//                    MActivity.updateProgress(realm, m.getProgress_photo(), m.getActivity_id());
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    view.onErrorSubmit(false, "Upload upload suksesFailed Please try again... x311");
                }
            }
        } else {

            if (currentLoop == max) {
//                int penjaga_lahan_site_project_id, String projectId, String isOnline, boolean is_upload
                doneSubmit(projectId,isOnline);
            } else {
                List<PhotoPerson> list = baseActivity.getRealm().where(PhotoPerson.class)
                        .equalTo(PhotoPerson.PROJECT_ID, projectId)
                        .equalTo("statusPhoto", "0")
                        .findAll();
                int count = baseActivity.getRealm().copyFromRealm(baseActivity.getRealm().where(PhotoPerson.class)
                        .equalTo(PhotoPerson.PROJECT_ID, projectId)
                        .equalTo("statusPhoto", "0")
                        .findAll()).size();
                List<Integer> list1 = new ArrayList<>();
                for (int k = 0; k < count; k++) {
                    list1.add(list.get(k).getId());
                }
                for (int k = 0; k < count; k++) {
                    Log.d(TAG, "isi uploadPhoto : count" + k);
                    PhotoPerson m = baseActivity.getRealm().where(PhotoPerson.class)
                            .equalTo(PhotoPerson.PROJECT_ID, projectId)
                            .equalTo(PhotoPerson.ID, list1.get(k)).findFirst();
//                    MActivity mActivity = realm.where(MActivity.class)
//                            .equalTo("activity_id", activityId).findFirst();
//
                    if (m != null) {
//                        realm.beginTransaction();
                        PhotoPerson.offlinePhoto(baseActivity.getRealm(), m, curent_date, time_now, 0);

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
//            inputDataPerson(userId, userName, projectId, namaPenjagaLahan, alamat, noKtp, noTlp, isOnline);
        }
    }

    public void doUploadPhoto(PhotoPerson photoPerson, String userId, String userName, String projectId, String
            namaPenjagaLahan, String alamat,String provinsi,String kabupaten,String kecamatan, String noKtp, String noTlp, String isOnline,
                              Integer penjaga_lahan_site_project_id) {
        String imgBase64 = null;
        try {
            imgBase64 = FileUtils.getBase64Img(photoPerson.getPathPhotoPerson());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<ResponseBody> responseBodyCall = ReqService.getAPIInterFace().
                doUploadDataPenjagaLahanImg(URL.URL_Upload_Image_PenjagaLahan, penjaga_lahan_site_project_id, photoPerson.getDescription(), imgBase64);
        Log.d(TAG, "onResponse: 3" + "," + URL.URL_Upload_Image_PenjagaLahan + "," + penjaga_lahan_site_project_id + "," + imgBase64);
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
                        uploadDataPerson(userId, userName, projectId,
                                namaPenjagaLahan, alamat, provinsi,kabupaten, kecamatan,
                                noKtp, noTlp, isOnline, penjaga_lahan_site_project_id);
                        moveFotoToDir(baseActivity.getRealm(), photoPerson, true);
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
        inputDataPerson(userId, userName, projectId, namaPenjagaLahan, alamat,provinsi,kabupaten,kecamatan, noKtp, noTlp, isOnline);
    }


    public void inputDataPerson(String userId, String userName, String projectId, String
            namaPenjagaLahan, String alamat,String provinsi,String kabupaten,String kecamatan, String noKtp, String noTlp, String isOnline) {
        baseActivity.getRealm().beginTransaction();
        Number currentIdNum = baseActivity.getRealm().where(DataPenjagaLahan.class).max("idPenjagaLahanOffline");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
//
        DataPenjagaLahan dataPenjagaLahan = new DataPenjagaLahan();
        dataPenjagaLahan.setIdPenjagaLahanOffline(nextId);//penjaga_lahan_site_project_id
        dataPenjagaLahan.setUserName(userName);
        dataPenjagaLahan.setUserId(userId);
        dataPenjagaLahan.setProject_id(projectId);
        dataPenjagaLahan.setNamaPenjagaLahan(namaPenjagaLahan);
        dataPenjagaLahan.setAlamat(alamat);
        dataPenjagaLahan.setProvinsi(provinsi);
        dataPenjagaLahan.setKabupaten(kabupaten);
        dataPenjagaLahan.setKecamatan(kecamatan);
        dataPenjagaLahan.setNoKtp(noKtp);
        dataPenjagaLahan.setNoTlp(noTlp);
        dataPenjagaLahan.setIs_submited(isOnline);
        dataPenjagaLahan.setStatus_approval("1");
        baseActivity.getRealm().insertOrUpdate(dataPenjagaLahan);
        baseActivity.getRealm().commitTransaction();
        Log.d(TAG, "onResponse: dataAlamatActualOffline : Added");
        Log.d(TAG, "onResponse: dataAlamatActualOffline : data tersimpan di local db" + dataPenjagaLahan);
//      dataAlamatActualOffline.set

//        if (isOnline == "1") {
//            view.onSuccessSubmit(true, "Data Penjaga Lahan Submited");
////            closeRealm();
//        }
//        if (isOnline == "2") {
//            view.onSuccessSubmit(true, "Data Penjaga Lahan Submited");
////            closeRealm();
//        }
    }

    @Override
    public void doneSubmit(String projectId, String isOnline) {
        if (isOnline == "1") {
            view.onSuccessSubmit(true, "Data Alamat Actual Submited");
//            closeRealm();
        }
        if (isOnline == "2") {
            view.onSuccessSubmit(true, "Data Submited");
//            closeRealm();
        }
    }

    void moveToDirOff(Realm realm, PhotoPerson m) throws IOException {
        Log.d(TAG, "onResponse m.getId() : count" + m.getId());

        InputStream in = null;
        OutputStream out = null;
        File newPath = new File(FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + "_" + "save_dir_off_person" + "_" + m.getProjectid());
        String delPath = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + "person_image" + "_" + m.getProjectid() + "/";
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
            in = new FileInputStream(m.getPathPhotoPerson());
            Log.e("onResponse in", in.toString());
            File dataPhoto = new File(m.getPathPhotoPerson());
            Log.e("onResponse dataPhoto", dataPhoto.getName());
//            out = new FileOutputStream(outputPath + dataPhoto.getPath());
            out = new FileOutputStream(m.getPathPhotoPerson() + m.getId() + ".jpg");
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
            PhotoPerson.updatePathOff(realm, m, curent_date, time_now, isiPath, false, false);
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

    void moveFotoToDir(Realm realm, PhotoPerson m, boolean is_uploade) throws IOException {
        Log.d(TAG, "tag isi m.getId() : count" + m.getId());
        InputStream in = null;
        OutputStream out = null;
        Log.e("tag dataPhoto", m.toString());
//        String outputPath= dataPhoto.getPath();tid(
        File newPath = new File(FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + m.getProjectid() + "_" + "save_dir");
        String delPath = FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + "person_image" + "_" + m.getProjectid() + "/";
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
            in = new FileInputStream(m.getPathPhotoPerson());
            Log.e("tag in", in.toString());
            File dataPhoto = new File(m.getPathPhotoPerson());
            Log.e("tag dataPhoto", dataPhoto.getName());
//
            out = new FileOutputStream(m.getPathPhotoPerson() + m.getId() + ".jpg");
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

            PhotoPerson.updatePath(realm, m, curent_date, time_now, m.getPathPhotoPerson(), true);
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