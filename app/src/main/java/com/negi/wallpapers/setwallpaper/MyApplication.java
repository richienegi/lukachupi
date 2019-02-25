package com.negi.wallpapers.setwallpaper;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;

import static com.negi.wallpapers.setwallpaper.Activity.ShowPostActivity.WRITE_EXTERNAL_STORAGE_CODE;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));

    }
}