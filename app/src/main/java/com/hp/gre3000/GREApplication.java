package com.hp.gre3000;

import android.app.Application;

import com.hp.gre3000.utils.SharedPre;

public class GREApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPre.init(this);
    }
}
