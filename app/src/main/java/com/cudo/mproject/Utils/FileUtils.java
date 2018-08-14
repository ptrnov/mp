package com.cudo.mproject.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by adsxg on 12/12/2017.
 * date            edited by             method                           Desc
 * 13/12/2017     newbiecihuy                                  create static field latitude, longitude
 * 13/12/2017     newbiecihuy                                  create field private LocationManager mLocationManager = null;
 * 13/12/2017     newbiecihuy      create method chekGPS()     Set lat, long value when GPS disabled
 * 13/12/2017     newbiecihuy      static String trimlatlong() Set lat, long value when GPS disabled
 */

public class FileUtils {
    public static String TAG = FileUtils.class.getSimpleName();
    //public static int MAX_SIZE_PIC= 480;
    //public static int MAX_SIZE_PIC= 1024;
    public static String SYSFOLDER = Environment.getExternalStorageDirectory() + File.separator;
    public static String ROOT_FOLDER = BuildConfig.APPNAME + File.separator;
    public static LocationManager mLocationManager = null;
    public static String latitude, longitude;
    public static Rect rectangle;

    public static String rootpath(Context mContext, String folder) {
        File newFile = new File(SYSFOLDER + ROOT_FOLDER + File.separator + folder);

        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }

        if (!newFile.exists()) {
            if (newFile.mkdir()) {
                return newFile.getAbsolutePath();
            } else {
                Utils.showToast(mContext, "CANT CREATE FOLDER2");
                return null;
            }

        } else {
            return newFile.getAbsolutePath();
        }
    }

    public static String path(Context mContext, String folder, String name, String ext) throws IOException {
        /*
         File newFile = new File(folder+File.separator+folder);
         Log.d(TAG, "path: "+newFile.getAbsolutePath());
        */
        File path = new File(folder);
        //File newFile =  File.createTempFile(name+"_",ext,path);
        //File newFile = new File(mContext.getFilesDir(), "images");
        File newFile = new File(folder + File.separator + name + "_" + ext);
        boolean hasPermission = (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(((Activity) mContext),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    112);
        }
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                newFile = File.createTempFile(
                        name,  /* prefix */
                        ext,      /* suffix */
                        path      /* directory */
                );
            }
        }
        return newFile.getAbsolutePath();
    }

    public static Uri getUri(Context mContext, String path) {
        File photoFile = new File(path);
        Uri photoURI = FileProvider.getUriForFile(mContext,
                "com.cudo.mproject.provider",
                photoFile);

        return photoURI;
    }


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static void delete(Context context, String path) throws IOException {

        File fdelete = new File(path);

        System.out.println("file canWrite :" + fdelete.canWrite());
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + fdelete.getPath());
            } else {
                System.out.println("file not Deleted :" + fdelete.getPath());

            }
        }
        if (fdelete.exists()) {
            if (fdelete.getCanonicalFile().delete()) {
                System.out.println("file Deleted :" + fdelete.getPath());
            } else {
                System.out.println("file not Deleted :" + fdelete.getPath());

            }
            if (fdelete.exists()) {
                context.getApplicationContext().deleteFile(fdelete.getName());
            }
        }
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static void writeToFile(String spath, String data) throws Exception {
        // Get the directory for the user's public pictures directory.
        final File file = new File(spath);

        // Make sure the path directory exists.
        if (!file.exists()) {
            // Make it, if it doesn't exit
            file.mkdirs();
        }

        // Save your stream, don't forget to flush() it before closing it.

        file.createNewFile();
        FileOutputStream fOut = new FileOutputStream(file);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(data);

        myOutWriter.close();

        fOut.flush();
        fOut.close();

    }

    public static Bitmap getResizedBitmap(Bitmap bm) {

        float aspectRatio = bm.getWidth() /
                (float) bm.getHeight();
        // int newWidth = 480;
        int newWidth = 1280;
        int newHeight = Math.round(newWidth / aspectRatio);
        if (bm.getWidth() < bm.getHeight()) {
            //newHeight = 480;
            newHeight = 720;
            newWidth = Math.round(newHeight * aspectRatio);
        }

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public static boolean timestampItAndSave2(String toEdit, String id, String site_name, String latlong, String typePhoto, String jenisPhoto) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int type ;


        Bitmap bitmap = getResizedBitmap(BitmapFactory.decodeFile(toEdit));
        //Bitmap bitmap = BitmapFactory.decodeFile(toEdit);
        //Bitmap src = BitmapFactory.decodeResource(); // the original file is cuty.jpg i added in resources
        Bitmap dest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Canvas cs = new Canvas(dest);
//        Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG );
        Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);

        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setColor(Color.YELLOW);
        tPaint.setElegantTextHeight(true);

//     Draw background
        Paint paint = new Paint(Paint.HINTING_OFF);
        paint.setStyle(Paint.Style.FILL);
        // create the Paint and set its color
        paint.setColor(Color.parseColor("#80ffffff"));
        // create a rectangle that we'll draw later
//        int x = 1;
//        int y = 0;
//        int sideLength = 250;
//        rectangle = new Rect(x, y, sideLength, sideLength);

//        int color=Color.GRAY;
//        paint.setColor(color);
//        cs.drawRect(210, 80, 360, 200, paint);
//
        cs.drawBitmap(bitmap, 0f, 0f, paint);
        cs.drawColor(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
//
        float height = tPaint.measureText("yY");
        float xx = cs.getWidth();
        float yy = cs.getHeight();
//      Default
        float xdt = 20;
        float ydt = yy - 20;
        float xlat = (xx / 2) + 50;
        float ylat = ydt;
        float xsiteName = 20;
        float ysiteName = -70;
        float xsiteid = xdt = 20;
        float ysiteid = yy - 60;

//        if (bitmap.getWidth() < bitmap.getHeight()) {
//
            xdt = 10;
            ydt = yy - 40;
            xsiteName = 10;
            ysiteName = yy - 60;//102

            xsiteid = xdt = 10;
            ysiteid = yy - 80;

            xlat = 10;
            ylat = ydt + 20;
            tPaint.setTextSize(18);

            // create a rectangle that we'll draw later


            Rect clipBounds = cs.getClipBounds();
            int myRectHeight = 10;
            float left = 100, top = 100; // basically (X1, Y1)
            float right = left + 100; // width (distance from X1 to X2)
            float bottom = top + 500; // height (distance from Y1 to Y2)
            float topi = ylat / 2;
//          cs.drawRect(1, topi, 290, bottom, paint);
//        https://stackoverflow.com/questions/6756975/draw-multi-line-text-to-canvas
            cs.drawRect(1, ylat + 60, 400, top + 500, paint);
//
            cs.drawText("Site Name  : " + site_name, xsiteName, ysiteName, tPaint);
            cs.drawText("Project ID :"+ id, xsiteid, ysiteid, tPaint);
            cs.drawText("Date Time  :" + dateTime, xdt, ydt, tPaint);
        if(jenisPhoto.contentEquals("p1")) {
            cs.drawText("Lat Long   :" + trimlatlong(latlong), xlat, ylat, tPaint);
         }
//        } else {
////
//            xdt = 10;
//            ydt = yy - 40;
//
//            xsiteName = 10;
//            ysiteName = yy - 60;//102
//
//            xsiteid = xdt = 10;
//            ysiteid = yy - 80;
//
//            xlat = 10;
//            ylat = ydt + 20;
//            tPaint.setTextSize(22);
////
//            Rect clipBounds = cs.getClipBounds();
//            int myRectHeight = 10;
//            // create a rectangle that we'll draw later
//
//            float left = 100, top = 100; // basically (X1, Y1)
//            float right = left + 100; // width (distance from X1 to X2)
////            float bottom = top + 500; // height (distance from Y1 to Y2)
//            float bottom = yy + 500; // height (distance from Y1 to Y2)
//
//            int topi = (clipBounds.centerX() + 200)/2;
//            cs.drawRect(1, ylat+100,  326, top + 500, paint);
////
//            cs.drawText("Site Name  : " + site_name, xsiteName, ysiteName, tPaint);
//            cs.drawText("Project ID :" + id, xsiteid, ysiteid, tPaint);
//            cs.drawText("Date Time  :" + dateTime, xdt, ydt, tPaint);
//            cs.drawText("Lat Long   :" + trimlatlong(latlong), xlat, ylat, tPaint);
//
//        }
        if (typePhoto.contentEquals("jpg")){

            try {
                return dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(toEdit)));

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                return false;
            }
        }
        else if (typePhoto.contentEquals("jpg")){
            try {
                return dest.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(toEdit)));

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                return false;
            }
        }
      return true;
    }


    private static String trimGpsString(String gostring) {
        String[] golist = gostring.split("\\.");
        Log.i("Task ac", "trimGpsString: " + gostring + " " + golist[0].toString() + " | " + golist.length);
        String s = golist[1];

        int length = s.length();

        String xresult;
        //Check whether or not the string contains at least four characters; if not, this method is useless

        if (length > 5) {
            xresult = s.substring(0, length - (length - 5)) + "";
            xresult = golist[0] + "." + xresult;
            Log.d("", "trimGpsString: " + xresult);
            return xresult;
        }
        return gostring;

    }


    private static String trimlatlong(String latlong) {
        String[] toparse = latlong.split(",");
        String lat = toparse[0];
        String lon = toparse[1];
        lat = trimGpsString(lat);
        lon = trimGpsString(lon);

        lat = trimGpsString(lat);
        lon = trimGpsString(lon);
//        }

        return lat + "," + lon;
    }


    public static String getBase64Img(String photoPath) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

        return encodeImage(bitmap);
    }

    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
        // return Base64.encodeToString(b, Base64.NO_WRAP);
    }
}
