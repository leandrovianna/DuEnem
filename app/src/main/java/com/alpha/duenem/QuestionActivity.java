package com.alpha.duenem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(com.alpha.duenem.R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(com.alpha.duenem.R.layout.activity_question);

        Question question = (Question)getIntent().getSerializableExtra("QUESTION");
        question.addText((CharSequence)"Questao 1\n jkdfjkkdjfkkjfdz\njkjfdk\n");
        LinearLayout layout = (LinearLayout) findViewById(com.alpha.duenem.R.id.content_question);
        question.buildContent(this, layout);


        FloatingActionButton fab = (FloatingActionButton) findViewById(com.alpha.duenem.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

    }

}
