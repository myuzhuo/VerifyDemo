package com.example.record.demo.system;

import android.app.Application;

public class VerifyAppLication extends Application {

    private static VerifyAppLication INSTANCE;

    public VerifyAppLication (){
        super();
        INSTANCE=this;
    }

    public static synchronized VerifyAppLication getInstance(){
        return  INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
