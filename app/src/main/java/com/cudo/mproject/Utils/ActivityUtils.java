package com.cudo.mproject.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

/**
 * Created by adsxg on 12/11/2017.
 */

public class ActivityUtils {

    public static void goToActivity(Activity from, Class to, int REQUEST_CODE, boolean isClear) {
        Intent i = new Intent(from, to);
        if (isClear) {
            from.finish();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                from.overridePendingTransition(android.R.transition.slide_left, android.R.transition.slide_right);
            }
        }
        if (REQUEST_CODE == 0) {
            from.startActivity(i);
        } else {
            from.startActivityForResult(i, REQUEST_CODE);
        }
    }

    public static void goToActivity(Activity from, Class to, int REQUEST_CODE, String json, String keyExtra) {
        Intent i = new Intent(from, to);
        i.putExtra(keyExtra, json);
        if (REQUEST_CODE == 0) {
            from.startActivity(i);
        } else {
            from.startActivityForResult(i, REQUEST_CODE);
        }
    }

}

