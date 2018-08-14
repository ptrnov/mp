package com.cudo.mproject.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.cudo.mproject.R;
/**
 * Created by adsxg on 12/11/2017.
 */

public class Utils {

    public static void showToast(Context context, String toast)
    {
        Toast.makeText(context,toast, Toast.LENGTH_SHORT).show();
    }
    public static String getBase64Img(String photoPath)throws Exception
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

        return encodeImage(bitmap);
    }
    public static String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,90,baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static void setToolbar(BaseActivity activity,String title)
    {

        Toolbar toolbar = (Toolbar)  activity.findViewById(R.id.toolbar);//header
        activity.setSupportActionBar(toolbar);
        if ( activity.getSupportActionBar()!=null) {
            try {
                TextView tx = (TextView) toolbar.findViewById(R.id.toolbar_title);
                tx.setText(title);
            } catch (Exception e) {
            }
            /*if(title.equals(activity.getString(R.string.title_selector)))
            {
                TypedValue typedValue = new TypedValue();

                // I used getActivity() as if you were calling from a fragment.
                // You just want to call getTheme() on the current activity, however you can get it
                activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);

                // it's probably a good idea to check if the color wasn't specified as a resource
                if (typedValue.resourceId != 0) {
                    //tx.setPadding(typedValue.resourceId,0,typedValue.resourceId,0);
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) tx
                            .getLayoutParams();

                    mlp.setMargins(0, 0,0, 0);
                } else {
                    // this should work whether there was a resource id or not
                    //tx.setPadding(typedValue.resourceId,0,typedValue.resourceId,0);
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) tx
                            .getLayoutParams();

                    mlp.setMargins(0, 0,0, 0);
                }
            }*/

        }
    }
    public static void setToolbar(BaseActivity activity, String title, boolean isAsup)
    {

        Toolbar toolbar = (Toolbar)  activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        if ( activity.getSupportActionBar()!=null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(isAsup);
            TextView tx = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tx.setText(title);
        }
    }

    public static String getDateString()
    {
        Calendar c = Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        return df.format(c.getTime());
    }

   /* public static void getDateList()
    {
        List<String> list_month = new ArrayList<>();
        Calendar c = Calendar.getInstance();
       // int currentMonth =5;
        int currentMonth = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);

        for (int i = 0; i <12; i++) {
            currentMonth--;
            if(currentMonth==0)
            {
                currentMonth=12;
                year--;
            }
          list_month.add(getMonth(currentMonth)+" "+year);
            Log.d(Utils.class.getSimpleName(), "getDateList: "+getIntMonth(getMonth(currentMonth))+"||"+getMonth(currentMonth) );

        }



        //return list_month;

    }*/

    public static List<String> getDateList()
    {
        List<String> list_month = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        // int currentMonth =5;
        int currentMonth = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);
        list_month.add(getMonth(currentMonth)+" "+year);
        for (int i = 0; i <2; i++) {
            currentMonth--;
            if(currentMonth==0)
            {
                currentMonth=12;
                year--;
            }
            list_month.add(getMonth(currentMonth)+" "+year);
            Log.d(Utils.class.getSimpleName(), "getDateList: "+getMonth(currentMonth) +year);

        }



        return list_month;

    }

    public static int getIntMonth(String m)
    {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("MMMM").parse(m));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.get(Calendar.MONTH) + 1;
    }
    private static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
    /* public static String getMonth(int month) {
         return String.format(Locale.US,"%tB",month);
     }*/
    public static void showSnack(final View view, final String message)
    {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view,message, Snackbar.LENGTH_LONG).show();
            }
        }, 2000);

    }

    public static String stringBuilderApproval(String checkin, String jam, String nik, String username)
    {
        return nik+" - "+username+"\n"+checkin+" pada pukul "+jam;
    }


    public static boolean isFakeGPS(Context context)
    {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }

    public static boolean isEmulator() {
        try {

            boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");

            boolean emu = getSystemProperty("ro.kernel.qemu").length() > 0;

            boolean sdk = getSystemProperty("ro.product.model").equals("sdk");

            if (/*emu ||*/ goldfish || sdk) {
                Log.d("", "isEmulator:1 "+goldfish+emu+sdk);
                return true;

            }

        } catch (Exception e) {

        }

        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static int unusualInfoDevice(Context context)
    {
        int temp = ((Activity)context).getIntent().getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);

        return temp;
    }

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
    private static String getSystemProperty(String name)

            throws Exception {

        Class systemPropertyClazz = Class .forName("android.os.SystemProperties");


        return (String) systemPropertyClazz.getMethod("get", new Class[]{String.class})

                .invoke(systemPropertyClazz, new Object[]{name});
    }
}
