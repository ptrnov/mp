package com.cudo.mproject.Model;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cudo.mproject.BaseActivity;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 */

public class GpsCache extends RealmObject {

    @PrimaryKey
    int id = 1;
    static String TAG = GpsCache.class.getSimpleName();
    private static final int TWO_MINUTES = 10 * 60 * 1000;
    public static final int ACC = 25; //a
    @Getter
    @Setter
    double lat;
    @Getter
    @Setter
    double lng;
    @Getter
    @Setter
    float acc = 0;
    @Getter
    @Setter
    long time;


    public String getLatLng() {
        String sLat = String.valueOf(lat);
        String sLng = String.valueOf(lng);
        return sLat + "," + sLng;
    }

    public boolean isValidGPS() {
        return !isExpired() && (acc != 0.0 && acc <= ACC);
    }

    public boolean isExpired() {
        return time + TWO_MINUTES < System.currentTimeMillis();
    }

    public static void updateGPSCache(Realm realm, double lat, double lng, float acc, long time) {
        Log.d(TAG, "updateGPSCache: " + lat + "," + lng + "||" + acc);
        final GpsCache m = new GpsCache();
        m.setLat(lat);
        m.setLng(lng);
        m.setAcc(acc);
        m.setTime(time);

        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.copyToRealmOrUpdate(m);
                }
            });
        } finally {

        }
    }

    public boolean isTooFar(Project m, BaseActivity baseActivity) {

        Location current = new Location("current");
        Location site = new Location("site");


        current.setLatitude(lat);
        current.setLongitude(lng);
        site.setLatitude(m.getLat());
        site.setLongitude(m.getLng());

        try {
            float jarak = current.distanceTo(site);
            if (acc == 0 && !isValidGPS()) {
                baseActivity.showAlertDialog("Warning..",
                        "GPS Belum akurat.. \r\n" +
                                "Pastikan Indikator Menjadi Hijau atau Pastikan GPS Anda Aktif \r\n"
                        , null, true);
                return true;
            }  else if (jarak > Float.parseFloat(m.getRadius())) {
                baseActivity.showAlertDialog("Warning..",
                        "Lokasi Anda terlalu jauh dari site.. \r\n" +
                                "Jarak Anda " + jarak + "M dari site \r\n" +
                                "Lokasi site berada di Latitude" + m.getLat() + ",Longitude" + m.getLng()
                        , null, true);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Utils.showToast(baseActivity,e.getMessage());
            return false;
        }
    }


}
