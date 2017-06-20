package com.alpha2.duenem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alpha2.duenem.signin.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        Intent intent;

        if (user != null) {
            intent = new Intent(this, HomeActivity.class);
            intent.putExtra(BaseActivity.SELECTED_ITEM_ID_EXTRA, R.id.materialestudo);
        } else {
            intent = new Intent(this, SignInActivity.class);
            intent.putExtra(BaseActivity.SELECTED_ITEM_ID_EXTRA, R.id.perfil);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
