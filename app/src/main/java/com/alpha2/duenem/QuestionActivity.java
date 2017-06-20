package com.alpha2.duenem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.alpha2.duenem.model.Question;

public class QuestionActivity extends AppCompatActivity {

    public static final String QUESTION_EXTRA = "com.alpha2.duenem.question_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_question);

        Question question = (Question)getIntent().getSerializableExtra("QUESTION");
        question.addText("Questao 1\n jkdfjkkdjfkkjfdz\njkjfdk\n");
        LinearLayout layout = (LinearLayout) findViewById(R.id.content_question);
        question.buildContent(this, layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(com.alpha2.duenem.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do something here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
