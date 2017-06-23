package com.alpha2.duenem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.model.Material;
import com.alpha2.duenem.model.Question;

import org.w3c.dom.Text;

import java.util.List;

public class QuestionActivity extends BaseActivity {

    public static final String QUESTION_EXTRA = "com.alpha2.duenem.question_extra";

    private int current_lesson;
    private List<Material> questions;
    private int cont_correct = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_question);
        Lesson lesson = (Lesson) getIntent().getSerializableExtra("LESSON");

        questions = lesson.getMaterial();
        current_lesson = 0;

        Button bt = (Button) findViewById(R.id.buttonQuestion);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void nextQuestion(){
        current_lesson++;
        if(current_lesson >= questions.size()){
            endLesson();
        }
        else{
            setContentQuestion((Question)questions.get(current_lesson), current_lesson+1);
        }
    }
    public void setContentQuestion(Question question, int i){
        TextView textTitle = (TextView) findViewById(R.id.textTitleQuestion);
        TextView textContent = (TextView) findViewById(R.id.textContentQuestion);

        textTitle.setText("Questão " + i);
        textContent.setText(question.getText());

        ((RadioButton)findViewById(R.id.radioBt1) ).setText(question.getTextAlternative(0));
        ((RadioButton)findViewById(R.id.radioBt2) ).setText(question.getTextAlternative(1));
        ((RadioButton)findViewById(R.id.radioBt3) ).setText(question.getTextAlternative(2));
        ((RadioButton)findViewById(R.id.radioBt4) ).setText(question.getTextAlternative(3));
        ((RadioButton)findViewById(R.id.radioBt5) ).setText(question.getTextAlternative(4));

    }


    private void ShowCorrect(){

    }

    private void endLesson(){

    }
}
