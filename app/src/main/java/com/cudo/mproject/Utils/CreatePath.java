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
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;

import com.cudo.mproject.BuildConfig;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreatePath {
    public static String TAG = CreatePath.class.getSimpleName();

    public static String SYSFOLDER = Environment.getExternalStorageDirectory() + File.separator;
    public static String ROOT_FOLDER = BuildConfig.APPNAME + File.separator;
    public static LocationManager mLocationManager = null;

    public static String rootPath(Context mContext, String folder) {
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
    public static String path(Context context, String folder, String name, String ext) throws IOException {
//        try {
        File path = new File(folder);
//            File newPath = new File(FileUtils.SYSFOLDER + FileUtils.ROOT_FOLDER + File.separator + "_" + "photo_person" + "_");
        File newPath = new File(folder + File.separator + name + "_" + ext);
        boolean hasPermission = (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(((Activity) context),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    112);
        }
        String outputPath = newPath.toString();
        Log.e("tag outputPath", outputPath.toString());
        File dir = new File(outputPath);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }else {
//                Toast.makeText(context, newPath+" can't be created.", Toast.LENGTH_SHORT).show();
//            }
        if (!dir.exists()) {
            try {
                dir.createNewFile();
            } catch (IOException e) {
                dir = File.createTempFile(
                        name,  /* prefix */
                        ext,      /* suffix */
                        path      /* directory */
                );
            }
        }
        return dir.getAbsolutePath();
//        } catch (Exception e) {
//            Log.e("tag e", e.getMessage());
//        }
//        return folder;
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

    public static boolean writeFile(String toEdit, String id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;

        Bitmap bitmap = getResizedBitmap(BitmapFactory.decodeFile(toEdit));
        Bitmap dest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
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
        try {
            return dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(toEdit)));
//          return dest.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(toEdit))); //100-best quality

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    public static String getBase64Img(String photoPath) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
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
