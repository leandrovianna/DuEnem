package com.alpha2.duenem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.model.Material;
import com.alpha2.duenem.model.Question;

import java.util.List;

public class QuestionActivity extends BaseActivity {

    public static final String QUESTION_EXTRA = "com.alpha2.duenem.question_extra";

    int current_lesson;
    List<Material> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_home);
        Lesson lesson = (Lesson) getIntent().getSerializableExtra("LESSON");

        questions = lesson.getMaterial();
        current_lesson = 0;

        setContentQuestion((Question) questions.get(0));
    }

    public void setContentQuestion(Question question){
        TextView textTitle = (TextView) findViewById(R.id.textTitleQuestion);
        TextView textContent = (TextView) findViewById(R.id.textContentQuestion);

        textTitle.setText(question.getTitle());
        textContent.setText(question.getText());

        ((RadioButton)findViewById(R.id.radioBt1) ).setText(question.getTextAlternative(0));
        ((RadioButton)findViewById(R.id.radioBt2) ).setText(question.getTextAlternative(1));
        ((RadioButton)findViewById(R.id.radioBt3) ).setText(question.getTextAlternative(2));
        ((RadioButton)findViewById(R.id.radioBt4) ).setText(question.getTextAlternative(3));
        ((RadioButton)findViewById(R.id.radioBt5) ).setText(question.getTextAlternative(4));

    }

}
