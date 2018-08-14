package com.cudo.mproject.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.widget.Toast;

import com.cudo.mproject.Menu.Login.LoginActivity;
import com.cudo.mproject.Menu.Photo.PhotoActivity;
import com.cudo.mproject.Menu.Project.ProjectListActivity;
import com.cudo.mproject.Service.GpsService;
import com.cudo.mproject.Utils.ActivityUtils;

import java.io.UnsupportedEncodingException;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;



/**
 * Created by adsxg on 12/11/2017.
 */

public class User extends RealmObject {
    @Getter
    @Setter
    String user_id;
    @Getter
    @Setter
    private  String username;
    @Getter
    @Setter
    private  String pass;
    @Getter
    @Setter
    String real_name;
    @Getter
    @Setter
    String user_group;
    @Getter
    @Setter
    String email;
    @Getter
    @Setter
    String user_desc;
    @Getter
    @Setter
    String active_date;
    @Getter
    @Setter
    String expired_date;
    @Getter
    @Setter
    String last_update;
    @Getter
    @Setter
    String imei;
    @Getter
    @Setter
    String flag;
    @Getter
    @Setter
    String perusahaan;
    @Getter
    @Setter
    String created_by;
    @Getter
    @Setter
    String photo;
    @Getter
    @Setter
    String initial;
    @Getter
    @Setter
    String user_phone;
    @Getter
    @Setter
    String vendor_id;
    @Getter
    @Setter
    String unit_id;
    @Getter
    @Setter
    String position;
    @Getter
    @Setter
    String status;
    @Getter
    @Setter
    String area;
    private String role_name;
    private Boolean is_online;
    @Getter
    @Setter
    private static Context mContext;
    @Getter
    @Setter
    private String rcCode;
    public User() {

    }


    public void setIs_online(Boolean is_online) {
        this.is_online = is_online;
    }

//    public static String getIsOnline() {
//        return isOnline;
//    }
//
//    public static void setIsOnline(String isOnline) {
//        User.isOnline = String.valueOf(getIs_online());
//    }

    public void setPassword(String pwd) {
        setPass(encrypt(pwd));
    }

    public String getPassword() {
        return decrypt(pass);
    }

    String encrypt(String pwd) {
        try {
            byte[] data = pwd.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            return "";
        }

    }

    String decrypt(String pwd) {
        try {
            byte[] data = Base64.decode(pwd, Base64.DEFAULT);
            String text = new String(data, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static boolean isLogin(Realm realm) {
        User user = realm.where(User.class).findFirst();
        return user != null;
    }

    public static void doLogin(Activity activity) {
            ActivityUtils.goToActivity(activity, ProjectListActivity.class, 0, true);

    }

    public static void doLogout(Activity activity, Realm realm) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                 realm.deleteAll();
//               realm.isClosed();
//               realm.close();
                }
            });
        } finally {

        }
        ActivityUtils.goToActivity(activity, LoginActivity.class, 0, true);

    }
    public static void islogout(final Activity activity, Realm realm, final String username) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
//               realm.deleteAll();
//               realm.close();
//               realm.where(User.class).equalTo("username", username)
//               .findFirst()
//               .deleteFromRealm();
                    realm.delete(User.class);
                    realm.delete(Project.class);
                    realm.delete(WorkPkg.class);
                    realm.delete(MActivity.class);
                    realm.delete(DataPLNSite.class);
                    realm.delete(DataIMB.class);
                    realm.delete(DataPenjagaLahan.class);
                    realm.delete(DataAlamatActualOffline.class);
                    realm.close();
                }
            });
        } finally {

        }
        ActivityUtils.goToActivity(activity, LoginActivity.class, 0, true);
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public  String getUsername() {
        return username;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public java.lang.String getUser_group() {
        return user_group;
    }

    public void setUser_group(java.lang.String user_group) {
        this.user_group = user_group;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getArea() {
        return area;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

