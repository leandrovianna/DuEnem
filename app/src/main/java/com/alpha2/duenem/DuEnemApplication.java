package com.alpha2.duenem;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class DuEnemApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
