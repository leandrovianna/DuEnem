package com.alpha2.duenem;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class DuEnemApplication extends Application {

    private static final String TAG = DuEnemApplication.class.getSimpleName();

    private User mActualUser;

    private ValueEventListener mUserQueryListener;
    private Query mUserQuery;
    private FirebaseAuth.AuthStateListener authListener;

    private static DuEnemApplication instance;

    public DuEnemApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) mActualUser = new User(auth.getCurrentUser());

        mUserQueryListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mActualUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        };

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    if (mUserQuery != null)
                        mUserQuery.removeEventListener(mUserQueryListener);
                }
                else {
                    mUserQuery = DBHelper.getUsers().child(firebaseAuth.getCurrentUser().getUid());
                    mUserQuery.addValueEventListener(mUserQueryListener);
                }
            }
        };

        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mUserQuery.removeEventListener(mUserQueryListener);
        FirebaseAuth.getInstance().removeAuthStateListener(authListener);
    }

    public User getUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return mActualUser;
        }

        return null;
    }

    public static DuEnemApplication getInstance() {
        return instance;
    }
}
