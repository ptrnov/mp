package com.cudo.mproject;

import android.app.Application;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by adsxg on 12/11/2017.
 */

public class BaseApp extends Application {
    private static BaseApp context;
    public static int REQUEST_ID= 102;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context =this;
        realmInit();
        fileInit();

    }
    void realmInit()
    {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    void fileInit()
    {
        try {
            //Allowing Strict mode policy for Nougat support
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {Fabric.getLogger().e(getClass().getSimpleName(),"file init",e);}
    }
    public static BaseApp getContext()
    {
        return context;
    }
}
