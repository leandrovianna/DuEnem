package com.alpha2.duenem;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alpha2.duenem.model.User;
import com.alpha2.duenem.signin.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.activity_main);

        User user = DuEnemApplication.getInstance().getUser();

        if (user == null) {
            Intent intent = IntentAbstractFactory.createSignInActivityIntent(this);
            startActivity(intent);
            finish();
        }

        mDrawer.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
