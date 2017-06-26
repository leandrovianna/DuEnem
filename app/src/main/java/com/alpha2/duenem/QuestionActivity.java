package com.alpha2.duenem;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.model.LessonUser;
import com.alpha2.duenem.model.Material;
import com.alpha2.duenem.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends BaseActivity {

    private static final String TAG = QuestionActivity.class.getSimpleName();
    public static final String LESSON_EXTRA = "lesson_extra";

    private int correct_alternative;
    private int current_material;
    private List<Material> materials;
    private int cont_correct = 0;
    private boolean is_question = false;
    private SUBMIT_BUTTON_STATES buttonState;
    private Lesson mLesson;
    private LessonUser mLessonUser;

    private enum SUBMIT_BUTTON_STATES {
        CONTINUE, VERIFY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_question);
        mLesson = (Lesson) getIntent().getSerializableExtra(LESSON_EXTRA);
        this.setTitle(mLesson.getTitle());

        Query materialsQuery = DBHelper.getMaterialsFromLesson(mLesson.getUid());
        materialsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLesson.getMaterial().clear();

                for (DataSnapshot materialSnap : dataSnapshot.getChildren()) {
                    if (materialSnap.child("alternatives").exists()) {
                        is_question = true;
                        Question q = materialSnap.getValue(Question.class);
                        mLesson.addMaterial(q);
                    } else {
                        Material m = materialSnap.getValue(Material.class);
                        mLesson.addMaterial(m);
                    }
                    initiate();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });

        buttonState = SUBMIT_BUTTON_STATES.VERIFY;

        Button bt = (Button) findViewById(R.id.buttonQuestion);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonState == SUBMIT_BUTTON_STATES.VERIFY) {
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

                    if (i_selected == correct_alternative) {
                        cont_correct++;
                        ShowCorrect();
                        ChangeButtonState();
                    } else {
                        ShowIncorrect(correct_alternative);
                        ChangeButtonState();
                    }
                }
                else {
                    ChangeButtonState();
                    nextQuestion();
                }
            }
        });
    }

    private void initiate() {
        String userUid = mAuth.getCurrentUser().getUid();
        final Query lessonUsersQuery = DBHelper.getLessonUsersByUser(userUid)
                .child(mLesson.getUid());

        lessonUsersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLessonUser = dataSnapshot.getValue(LessonUser.class);
                lessonUsersQuery.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
                mLessonUser = null;
            }
        });

        materials = mLesson.getMaterial();
        current_material = -1;
        nextQuestion();
    }

    public void nextQuestion(){
        current_material++;
        ((ProgressBar)findViewById(R.id.progressBarQuestion)).setProgress((current_material *100)/ materials.size());
        if(current_material >= materials.size()){
            endLesson();
        }
        else{
            setContent(materials.get(current_material));
        }
    }

    private void setContent(Material material) {
        TextView textTitle = (TextView) findViewById(R.id.textTitleQuestion);
        TextView textContent = (TextView) findViewById(R.id.textContentQuestion);

        textTitle.setText(getString(R.string.material_title, current_material+1));
        textContent.setText(material.getText());

        findViewById(R.id.radioGroupQuestion).setVisibility(View.GONE);

        if (material instanceof Question) {
            setContentQuestion((Question) material, current_material+1);
        } else {
            ChangeButtonState();
            cont_correct++; //material count like question corrected
        }
    }

    private void setContentQuestion(Question question, int l){
        findViewById(R.id.radioGroupQuestion).setVisibility(View.VISIBLE);

        TextView textTitle = (TextView) findViewById(R.id.textTitleQuestion);
        textTitle.setText(getString(R.string.question_title, l));

        int[] ListId = new int[]{R.id.radioBt1, R.id.radioBt2, R.id.radioBt3, R.id.radioBt4, R.id.radioBt5};
        Integer[] order = new Integer[]{0, 1, 2, 3, 4};
        List<Integer> list = Arrays.asList(order);
        Collections.shuffle(list);

        for(int i = 0; i < 5; i++){
            RadioButton radioButton = (RadioButton)findViewById(ListId[i]);
            radioButton.setText(question.getTextAlternative(list.get(i)));
            radioButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorQuestionDefault, null));
            radioButton.setChecked(false);
            if(list.get(i) == 0)
                correct_alternative = i;
        }
    }

    private void ShowCorrect(){
        TextView textView = (TextView) findViewById(R.id.textState);
        textView.setText(R.string.correct_answer_message);
    }

    private void ShowIncorrect(int correct_alternative){
        TextView textView = (TextView) findViewById(R.id.textState);
        textView.setText(R.string.incorrect_answer_message);

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
        if(buttonState == SUBMIT_BUTTON_STATES.VERIFY){
            bt.setText(R.string.bt_submit_continue_message);
            buttonState = SUBMIT_BUTTON_STATES.CONTINUE;
        }
        else{
            TextView textView = (TextView) findViewById(R.id.textState);
            textView.setText("");
            bt.setText(R.string.bt_submit_verify_message);
            buttonState = SUBMIT_BUTTON_STATES.VERIFY;
        }
    }
    private void endLesson(){
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupQuestion);
        radioGroup.setVisibility(View.INVISIBLE);
        int grade = (cont_correct * 100) / materials.size();

        TextView text1 = (TextView) findViewById(R.id.textTitleQuestion);
        TextView text2 = (TextView) findViewById(R.id.textContentQuestion);
        ((ProgressBar)findViewById(R.id.progressBarQuestion)).setProgress(100);
        Button bt = (Button) findViewById(R.id.buttonQuestion);
        bt.setText(getString(R.string.bt_submit_continue_message));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(!is_question){
            text1.setText(R.string.lesson_conclude);
            text2.setText("");
            return;
        }

        if(grade >= 70){
            text1.setText(R.string.end_lesson_success_message);
        }
        else{
            text1.setText(R.string.end_lesson_fail_message);
        }

        text2.setText(getString(R.string.lesson_result_message, cont_correct, materials.size()));

        setUserLessonResult(grade);
    }

    private void setUserLessonResult(int grade) {
        if (mLessonUser == null) {
            mLessonUser = new LessonUser();
        }

        int q = 0;
        if(grade >= 90) q = 5;
        else if(grade >= 80) q = 4;
        else if(grade >= 65) q = 3;
        else if(grade >= 50) q = 2;
        else if(grade >= 30) q = 1;

        mLessonUser.setNextInterval(q);

        mLessonUser.setLastDate(new Date());
        mLessonUser.setNextDate(calculateNextDate(mLessonUser));
        mLessonUser.setUidTopic(mLesson.getTopic().getUid());

        String userUid = mAuth.getCurrentUser().getUid();
        DatabaseReference lessonUserRef = DBHelper.getLessonUsersByUser(userUid)
                .child(mLesson.getUid());

        lessonUserRef.setValue(mLessonUser);
    }

    private Date calculateNextDate(LessonUser lessonUser) {
        Date date =  new Date();
        int interval = lessonUser.getInterval();
        date = new Date(date.getTime() + TimeUnit.DAYS.toMillis(interval));
        return date;
    }

}
