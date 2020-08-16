package com.example.inote.Controller;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class myApp extends Application {
    public static final String ToDoTime="To Do Time";
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config=new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }


}
