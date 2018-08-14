package com.cudo.mproject.Utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.R;

/**
 * Created by adsxg on 12/11/2017.
 */

public class GetAllPermissionV2 {
    BaseActivity baseActivity;
    public GetAllPermissionV2(BaseActivity baseActivity)
    {
        this.baseActivity=baseActivity;
    }

    public static final int PERMISSION_ALL = 101;


    public static final String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.RECORD_AUDIO
    };


    public boolean checkPermission(){
        if(!hasPermissions(baseActivity, PERMISSIONS)){
            Log.d("getallpermission", "checkPermission: " +hasPermissions(baseActivity, PERMISSIONS));
            baseActivity.showAlertDialog(baseActivity.getString(R.string.warning),baseActivity.getString(R.string.getPermissionConfirm),confirmClick,false);
            return false;
        }
        else
        {
            return true;
        }
    }

    private Dialog.OnClickListener confirmClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ActivityCompat.requestPermissions(baseActivity, PERMISSIONS, PERMISSION_ALL);
            dialog.dismiss();
        }
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        Log.d("checking", "hasPermissions: test");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("checking", "hasPermissions: hahahaha "+permission );
                    return false;
                }
            }
        }
        return true;
    }


}

