package com.cudo.mproject.Utils;

import android.location.LocationManager;
import android.os.Environment;

import com.cudo.mproject.BuildConfig;

import java.io.File;

public class VideoUtils {
    public static String TAG = VideoUtils.class.getSimpleName();
    public static String SYSFOLDER = Environment.getExternalStorageDirectory() + File.separator;
    public static String ROOT_FOLDER = BuildConfig.APPNAME + File.separator;
    public static LocationManager mLocationManager = null;

}
