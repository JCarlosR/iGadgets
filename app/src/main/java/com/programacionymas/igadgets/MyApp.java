package com.programacionymas.igadgets;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Juan Carlos on 30/07/2017.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}