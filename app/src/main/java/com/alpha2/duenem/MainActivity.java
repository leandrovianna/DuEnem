package com.alpha2.duenem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alpha2.duenem.signin.SignInActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.alpha2.duenem.R.layout.activity_main);
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
