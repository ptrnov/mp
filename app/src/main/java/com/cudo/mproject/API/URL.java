package com.cudo.mproject.API;

import java.net.URLConnection;

/**
 * Created by adsxg on 12/12/2017.
 */
public class URL {
//    public static String urlConnection2 ="http://180.250.19.206/pmis_2017/GetSiteProfile/";//production
  public static String urlConnection2 ="http://103.77.78.169/pmis_2017/GetSiteProfile/";//development1
  public static String urlConnection ="http://103.77.78.169/pmis_2017/mobile_2/";//development1
//  public static String urlConnection ="http://180.250.19.206/mobile_2/";//production
//  public static String urlConnection ="http://180.250.19.206/dev_2018/mobile_2/";//staging server
//  public static String urlConnection ="http://103.77.78.251/mobile_2/";//"testing development2"
    public static String URL_CHEK_SERVER = urlConnection;
    public static String URL_GET_STATUS_APP = urlConnection + "getStatusApp/";
//
    public static String URL_LOGIN = urlConnection+ "native_login/";
    public static String URL_GETPROJECT = urlConnection + "projectList/";
    public static String URL_GET_ACTIVITY = urlConnection + "getActivity_rev1/";//getActivity
    public static String URL_GET_WORK_PACKAGE = urlConnection + "getWorkPackage/";
    public static String URL_LOGOFF = urlConnection+ "logoutMobile/";
    public static String URL_Submit_Alamat_Actual = urlConnection+ "submitAlamatActual/";
    public static String URL_Submit_Data_IMB = urlConnection + "submitDataIMB/";
    public static String URL_Submit_Data_PenjagaLahan = urlConnection + "submitDataPenjagaLahan/";
    public static String URL_Submit_Data_PLN = urlConnection+ "submitDataPLN/";
    public static String URL_APK = urlConnection+ "getLastVersion/";

    public static String URL_Upload_Image_IMB = urlConnection+ "uploadImageIMB/";
    public static String URL_Upload_Image_PenjagaLahan = urlConnection + "uploadImagePenjagaLahan/";
    public static String URL_Upload_Image_PLN = urlConnection + "uploadImageDataPLN/";
    /**
     * ../dptid/progress
     */
    public static String URL_SUBMIT_PROGRESS_ACTIVITY =urlConnection+ "submit_progress_activity/";// URL_SUBMIT_INFO
    /**
     * paid projectid base64 comment
     */
    public static String URL_UPLOAD_IMG = urlConnection+ "upload_image/";
    /**
     * pa_id,project_id
     */
    public static String URL_UPDATE_STATUS = urlConnection+ "update_status/";
     /*
      Sync data
     */
    public static String URL_History = urlConnection + "historyProject/";
    public static String URL_HistorySite = urlConnection2 + "historySiteProject/";

//    public static String URL_CHEK_SERVER = BuildConfig.END_POINT ;
//    public static String URL_LOGIN = BuildConfig.END_POINT + "native_login/";
//    public static String URL_GETPROJECT = BuildConfig.END_POINT + "projectList/";
//    public static String URL_GET_ACTIVITY = BuildConfig.END_POINT + "getActivity_rev1/";//getActivity
//    public static String URL_GET_STATUS_APP = BuildConfig.END_POINT + "getStatusApp/";
//    public static String URL_GET_WORK_PACKAGE = BuildConfig.END_POINT + "getWorkPackage/";
//    public static String URL_LOGOFF = BuildConfig.END_POINT + "logoutMobile/";
//    public static String URL_History = BuildConfig.END_POINT + "historyProject/";
//    public static String URL_Submit_Alamat_Actual = BuildConfig.END_POINT + "submitAlamatActual/";
//    public static String URL_Submit_Data_IMB = BuildConfig.END_POINT + "submitDataIMB/";
//    public static String URL_Upload_Image_IMB = BuildConfig.END_POINT + "uploadImageIMB/";
//    public static String URL_Submit_Data_PenjagaLahan = BuildConfig.END_POINT + "submitDataPenajagaLahan/";
//    public static String URL_Upload_Image_PenjagaLahan = BuildConfig.END_POINT + "uploadImagePenjagaLahan/";
//    public static String URL_Submit_Data_PLN = BuildConfig.END_POINT + "submitDataPLN/";
//    public static String URL_Upload_Image_PLN = BuildConfig.END_POINT + "uploadImageDataPLN/";
//    public static String URL_APK = BuildConfig.END_POINT + "getLastVersion/";
//    /**
//     * ../dptid/progress
//     */
//    public static String URL_SUBMIT_PROGRESS_ACTIVITY = BuildConfig.END_POINT + "submit_progress_activity/";// URL_SUBMIT_INFO
//    /**
//     * paid projectid base64 comment
//     */
//    public static String URL_UPLOAD_IMG = BuildConfig.END_POINT + "upload_image/";
//    /**
//     * pa_id,project_id
//     */
//    public static String URL_UPDATE_STATUS = BuildConfig.END_POINT + "update_status/";

}

