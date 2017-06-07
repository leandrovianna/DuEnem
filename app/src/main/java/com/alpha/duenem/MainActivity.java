package com.alpha.duenem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.alpha.duenem.R.layout.activity_main);
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("QUESTION", new Question());
        startActivity(intent);
    }
}
