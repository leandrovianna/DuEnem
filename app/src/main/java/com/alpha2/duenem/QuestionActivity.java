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
import com.alpha2.duenem.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends BaseActivity {

    private static final String TAG = QuestionActivity.class.getSimpleName();
    public static final String LESSON_EXTRA = "lesson_extra";

    private int correctAlternative;
    private int currentMaterial;
    private List<Material> materials;
    private int contCorrect = 0;
    private boolean isQuestion = false;
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

        final Query materialsQuery = DBHelper.getMaterialsFromLesson(mLesson.getUid());
        materialsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLesson.getMaterial().clear();
                materialsQuery.removeEventListener(this);

                for (DataSnapshot materialSnap : dataSnapshot.getChildren()) {
                    if (materialSnap.child("alternatives").exists()) {
                        isQuestion = true;
                        Question q = materialSnap.getValue(Question.class);
                        mLesson.addMaterial(q);
                    } else {
                        Material m = materialSnap.getValue(Material.class);
                        mLesson.addMaterial(m);
                    }
                }

                initiate();
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
                    int iSelected = -1;
                    if (selectedId == findViewById(R.id.radioBt1).getId())
                        iSelected = 0;
                    else if (selectedId == findViewById(R.id.radioBt2).getId())
                        iSelected = 1;
                    else if (selectedId == findViewById(R.id.radioBt3).getId())
                        iSelected = 2;
                    else if (selectedId == findViewById(R.id.radioBt4).getId())
                        iSelected = 3;
                    else if (selectedId == findViewById(R.id.radioBt5).getId())
                        iSelected = 4;

                    if (iSelected == correctAlternative) {
                        contCorrect++;
                        ShowCorrect();
                        ChangeButtonState();
                    } else {
                        ShowIncorrect(correctAlternative);
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
        User user = DuEnemApplication.getInstance().getUser();

        if (user != null) {
            String userUid = user.getUid();
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
            currentMaterial = -1;
            nextQuestion();
        }
    }

    public void nextQuestion(){
        currentMaterial++;

        ((ProgressBar)findViewById(R.id.progressBarQuestion)).setProgress((currentMaterial *100)/ materials.size());

        if (currentMaterial >= materials.size()) {
            endLesson();
        }
        else {
            setContent(materials.get(currentMaterial));
        }
    }

    private void setContent(Material material) {
        TextView textTitle = (TextView) findViewById(R.id.textTitleQuestion);
        TextView textContent = (TextView) findViewById(R.id.textContentQuestion);

        textTitle.setText(getString(R.string.material_title, currentMaterial +1));
        textContent.setText(material.getText());

        if (material instanceof Question) {
            setContentQuestion((Question) material, currentMaterial +1);
        } else {
            ChangeButtonState();
            findViewById(R.id.radioGroupQuestion).setVisibility(View.GONE);
            contCorrect++; //material count like question corrected
        }
    }

    private void setContentQuestion(Question question, int l) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupQuestion);
        radioGroup.clearCheck();
        radioGroup.setVisibility(View.VISIBLE);

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
            if(list.get(i) == 0)
                correctAlternative = i;
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
        radioGroup.setVisibility(View.GONE);
        int grade = (contCorrect * 100) / materials.size();

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


        if(!isQuestion){
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

        text2.setText(getString(R.string.lesson_result_message, contCorrect, materials.size()));

        setUserLessonResult(grade);
    }

    private void setUserLessonResult(int grade) {
        int q = 0;
        if(grade >= 90) q = 5;
        else if(grade >= 80) q = 4;
        else if(grade >= 65) q = 3;
        else if(grade >= 50) q = 2;
        else if(grade >= 30) q = 1;

        if (mLessonUser == null) {
            if (q == 0) return;
            else mLessonUser = new LessonUser();
        }

        mLessonUser.setNextInterval(q, mLesson.isDone());

        mLessonUser.setLastDate(new Date());
        mLessonUser.setNextDate(calculateNextDate(mLessonUser));
        mLessonUser.setUidTopic(mLesson.getTopic().getUid());

        User user = DuEnemApplication.getInstance().getUser();

        DatabaseReference root = DBHelper.getRoot();

        Map<String, Object> updates = new HashMap<>();
        updates.put(String.format("lessonUser/%s/%s", user.getUid(), mLesson.getUid()), mLessonUser);

        if (!mLesson.isDone())
            updates.put(String.format("user/%s/points", user.getUid()), user.getPoints() + q * 20);

        root.updateChildren(updates);
    }

    private Date calculateNextDate(LessonUser lessonUser) {
        Date date =  new Date();
        int interval = lessonUser.getInterval();
        date = new Date(date.getTime() + TimeUnit.DAYS.toMillis(interval));
        return date;
    }

}
