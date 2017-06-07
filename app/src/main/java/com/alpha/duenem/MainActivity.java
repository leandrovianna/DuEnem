package com.alpha.duenem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.alpha.duenem.R.layout.activity_main);
    }

    public void testSignIn(View v) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void testQuestionActivity(View v) {
        Intent intent;
        intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("QUESTION", new Question());
        startActivity(intent);
    }
}
