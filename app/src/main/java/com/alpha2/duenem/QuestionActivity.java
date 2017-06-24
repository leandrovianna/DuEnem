package com.alpha2.duenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.model.Material;
import com.alpha2.duenem.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class QuestionActivity extends BaseActivity {

    public static final String QUESTION_EXTRA = "com.alpha2.duenem.question_extra";
    private static final String TAG = QuestionActivity.class.getSimpleName();

    private int current_lesson;
    private List<Material> questions;
    private int cont_correct = 0;
    int buttonState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_question);
        final Lesson lesson = (Lesson) getIntent().getSerializableExtra("LESSON");

        Query materialsQuery = DBHelper.getMaterialsFromLesson(lesson.getUid());
        materialsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot materialSnap : dataSnapshot.getChildren()) {
                    if (materialSnap.child("alternatives").exists()) {
                        Question q = materialSnap.getValue(Question.class);
                        lesson.addMaterial(q);
                    } else {
                        //make activity accept material too
//                        Material m = materialSnap.getValue(Material.class);
//                        lesson.addMaterial(m);
                    }
                    initiate(lesson);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });

        Button bt = (Button) findViewById(R.id.buttonQuestion);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonState == 0) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupQuestion);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    int i_selected = -1;
                    if (selectedId == findViewById(R.id.radioBt1).getId())
                        i_selected = 0;
                    else if (selectedId == findViewById(R.id.radioBt2).getId())
                        i_selected = 1;
                    else if (selectedId == findViewById(R.id.radioBt3).getId())
                        i_selected = 2;
                    else if (selectedId == findViewById(R.id.radioBt4).getId())
                        i_selected = 3;
                    else if (selectedId == findViewById(R.id.radioBt5).getId())
                        i_selected = 4;
                    int correct_alternative = ((Question) questions.get(current_lesson)).getCorrect_alternative();
                    if (i_selected == correct_alternative) {
                        cont_correct++;
                        ShowCorrect();
                        ChangeButtonState();
                    } else {
                        ShowIncorrect(correct_alternative);
                        ChangeButtonState();
                    }
                }
                else{
                    ChangeButtonState();
                    nextQuestion();
                }
            }
        });
    }

    private void initiate(Lesson lesson) {
        questions = lesson.getMaterial();
        current_lesson = -1;
        nextQuestion();
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
        ((RadioButton)findViewById(R.id.radioBt1) ).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorQuestionDefault, null));

        ((RadioButton)findViewById(R.id.radioBt2) ).setText(question.getTextAlternative(1));
        ((RadioButton)findViewById(R.id.radioBt2) ).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorQuestionDefault, null));

        ((RadioButton)findViewById(R.id.radioBt3) ).setText(question.getTextAlternative(2));
        ((RadioButton)findViewById(R.id.radioBt3) ).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorQuestionDefault, null));

        ((RadioButton)findViewById(R.id.radioBt4) ).setText(question.getTextAlternative(3));
        ((RadioButton)findViewById(R.id.radioBt4) ).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorQuestionDefault, null));

        ((RadioButton)findViewById(R.id.radioBt5) ).setText(question.getTextAlternative(4));
        ((RadioButton)findViewById(R.id.radioBt5) ).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorQuestionDefault, null));

    }


    private void ShowCorrect(){
        TextView textView = (TextView) findViewById(R.id.textState);
        textView.setText("Parabéns, você acertou a questao");
    }

    private void ShowIncorrect(int correct_alternative){
        TextView textView = (TextView) findViewById(R.id.textState);
        textView.setText("Que pena, você errou a questao");

        int id = -1;
        switch (correct_alternative){
            case 0: id = R.id.radioBt1;
                break;
            case 1: id = R.id.radioBt2;
                break;
            case 2: id = R.id.radioBt3;
                break;
            case 3: id = R.id.radioBt4;
                break;
            case 4: id = R.id.radioBt5;
                break;
        }
        if(id != -1) ((RadioButton)findViewById(id) ).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorQuestionCorrect, null));
    }

    private void ChangeButtonState(){
        Button bt = (Button) findViewById(R.id.buttonQuestion);
        if(buttonState == 0){
            bt.setText("Continuar");
            buttonState = 1;
        }
        else{
            TextView textView = (TextView) findViewById(R.id.textState);
            textView.setText("");
            bt.setText("Verificar");
            buttonState = 0;
        }
    }
    private void endLesson(){
        int grade = (cont_correct * 10) / questions.size();
        Intent intent = new Intent(this, LessonEndedActivity.class);
        intent.putExtra("GRADE", grade);

        startActivity(intent);
        finish();
    }
}
