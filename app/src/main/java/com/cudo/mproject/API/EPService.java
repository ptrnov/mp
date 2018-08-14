package com.cudo.mproject.API;

import com.cudo.mproject.Model.Post.DataActualPost;
import com.cudo.mproject.Model.Response.ResponseApk;
import com.cudo.mproject.Model.Response.ResponseIMB;
import com.cudo.mproject.Model.Response.ResponseLogin;
import com.cudo.mproject.Model.Response.ResponseLogoff;
import com.cudo.mproject.Model.Response.ResponsePLN;
import com.cudo.mproject.Model.Response.ResponsePerson;
import com.cudo.mproject.Model.Response.ResponseSubmit;
import com.cudo.mproject.Model.Response.ResponseSubmitAlamatActual;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by adsxg on 12/12/2017.
 */

public interface EPService {

    @GET()
    Call<ResponseLogin> doLogin(@Url String url);

    @GET()
    Call<ResponseBody> doGetActivity(@Url String url);

    @GET()
    Call<ResponseBody> doGetWorkPkg(@Url String url);

    @GET()
    Call<ResponseSubmit> doSubmit(@Url String url);

    @GET()
    Call<ResponseLogoff> doLogout(@Url String url);

    @GET()
    Call<ResponseApk> doApk(@Url String url);

//    @GET()
//    Call<ResponsePerson> dataPenjagaLahanPost(@Url String url);

//    @FormUrlEncoded
//    @POST
//    Call<ResponseBody> doLogout(@Url String url,
//                                @Field("username") String username,
//                                @Field("password") String password
// );

    @FormUrlEncoded
    @POST
    Call<ResponseBody> doUploadImg(@Url String url,
                                   @Field("pa_id") String pa_id,
                                   @Field("project_id") String project_id,
//                                 @Field("userName") String userName,
//                                 @Field("password") String password,
                                   @Field("base64") String base64,
                                   @Field("comment") String comment);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> doneSubmit(@Url String url,
                                  @Field("pa_id") String pa_id);

    //
    @FormUrlEncoded
    @POST
    Call<ResponseBody> siteProf(@Field("project_id") String project_id,
                                @Field("userId") long userId);
//    @FormUrlEncoded
//    @POST("/post")
//    @Headers({
//            "Accept: application/json",
//            "Content-Type: application/json"
//    })
    @FormUrlEncoded
    @POST
    Call<ResponseSubmitAlamatActual> dataActualPost(
            @Url String url,
            @Field("userId") String userId,
            @Field("userName") String userName,
            @Field("area") String area,
            @Field("projectId") String projectId,
            @Field("siteName") String siteName,
            @Field("alamat") String alamat,
            @Field("provinsi") String provinsi,
            @Field("kabupaten") String kabupaten,
            @Field("kecamatan") String kecamatan,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("isOnline") String isOnline
    );

    @FormUrlEncoded
    @POST
    Call<ResponsePerson> dataPenjagaLahanPost(
            @Url String url,
            @Field("userId") String userId,
            @Field("userName") String userName,
            @Field("area") String area,
            @Field("projectId") String projectId,
            @Field("namaPenjagaLahan") String namaPenjagaLahan,
            @Field("alamat") String alamat,
            @Field("provinsi") String provinsi,
            @Field("kabupaten") String kabupaten,
            @Field("kecamatan") String kecamatan,
            @Field("noKTP") String noKTP,
            @Field("noTlp") String noTlp,
            @Field("isOnline") String isOnline
    );
    @FormUrlEncoded
    @POST
    Call<ResponseBody> doUploadDataPenjagaLahanImg(
            @Url String url,
            @Field("penjaga_lahan_site_project_id") Integer penjaga_lahan_site_project_id,
            @Field("deskripsi") String deskripsi,
            @Field("base64PenjagaLahan") String base64PenjagaLahan
    );
//    @POST("{dataActualPost}")
//    Call<ResponseSubmitAlamatActual> dataActualPost(
//            @HeaderMap Map<String, String> headers,
//            @Path("dataPath")String dataActualPath,
//            @Query("userId") String userId,
//            @Query("userName") String userName,
//            @Query("projectId") String projectId,
//            @Query("siteName") String siteName,
//            @Query("alamat") String alamat,
//            @Query("provinsi") String provinsi,
//            @Query("kabupaten") String kabupaten,
//            @Query("kecamatan") String kecamatan,
//            @Query("latitude") String latitude,
//            @Query("longitude") String longitude,
//            @Query("isOnline") String isOnline,
//            @Query("api-type") String type
//    );

    //

//
@FormUrlEncoded
@POST
Call<ResponseIMB> dataIMBSitePost(
        @Url String url,
        @Field("userId") String userId,
        @Field("userName") String userName,
        @Field("area") String area,
        @Field("projectId") String projectId,
        @Field("namaPemilikIMB") String namaPemilikLahan,
        @Field("noIMB") String noIMB,
        @Field("alamatIMB") String alamatIMB,
        @Field("tanggalAwalBerlaku") String tanggalAwalBerlaku,
        @Field("tanggalAkhirBerlaku") String tanggalAkhirBerlaku,
        @Field("isOnline") String isOnline
);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> doUploadIMBImg(@Url String url,
                                      @Field("data_imb_site_id") Integer data_imb_site_id,
                                      @Field("deskripsi") String deskripsi,
                                      @Field("base64IMB") String base64IMB
    );
//
    @FormUrlEncoded
    @POST
    Call<ResponsePLN> dataPLNSitePost(
            @Url String url,
            @Field("userId") String userId,
            @Field("userName") String userName,
            @Field("area") String area,
            @Field("projectId") String projectId,
            @Field("idPelanggan") String idPelanggan,
            @Field("namaPelanggan") String namaPelanggan,
            @Field("dayaListrik") String dayaListrik,
            @Field("isOnline") String isOnline
    );

    @FormUrlEncoded
    @POST
    Call<ResponseBody> doUploadPLNImg(@Url String url,
                                      @Field("data_pln_site_id") Integer data_pln_site_id,
                                      @Field("deskripsi")String deskripsi,
                                      @Field("base64PLN") String base64PLN
    );
//
//@FormUrlEncoded
//@POST
//Call<ResponseSiteExisiting> dataSiteExisitingPost(
//        @Url String url,
//        @Field("userId") String userId,
//        @Field("userName") String userName,
//        @Field("siteName") String siteName,
//        @Field("alamat") String alamat,
//        @Field("provinsi") String provinsi,
//        @Field("kabupaten") String kabupaten,
//        @Field("kecamatan") String kecamatan,
//        @Field("latitude") String latitude,
//        @Field("longitude") String longitude,
//        @Field("isOnline") String isOnline
//);
//
//    @FormUrlEncoded
//    @POST
//    Call<ResponseBody> doUploadSiteExisitingImg(@Url String url,
//                                      @Field("data_existing_id") String data_existing_id,
//                                      @Field("base64Existing) String base64Existing
//    );

}

