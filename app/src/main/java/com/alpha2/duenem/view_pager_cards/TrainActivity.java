package com.alpha2.duenem.view_pager_cards;

import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.alpha2.duenem.BaseActivity;
import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.R;
import com.alpha2.duenem.model.LessonUser;
import com.alpha2.duenem.model.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class TrainActivity extends BaseActivity {
    public static final String TOPIC_EXTRA = "com.alpha2.duenem.topic_extra";
    private static final String TAG = LessonActivity.class.getSimpleName();
    private ViewPager mViewPager;

    private Lesson train_lesson;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = setContentLayout(R.layout.content_lesson);

        mViewPager = (ViewPager) contentView.findViewById(R.id.viewPagerLesson);

        mCardAdapter = new CardPagerAdapter(this);
        populateAdapter();

        setTitle("Treinar");
        initiateList();
    }

    private void initiateList() {
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }

    public void populateAdapter(){

        String userUid = mAuth.getCurrentUser().getUid();
        final Query lessonUsersQuery = DBHelper.getLessonsByUser(userUid).orderByChild("nextDate");
        ValueEventListener mLessonsUserListener;

        mLessonsUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int cont = 0;
                for (DataSnapshot topicSnap : dataSnapshot.getChildren()) {
                    Lesson lesson = getLessonByUid(topicSnap.getKey());
                    LessonUser lessonUser = topicSnap.getValue(LessonUser.class);
                    Date nextDate = lessonUser.getNextDate();
                    if (lesson != null && !nextDate.after(new Date()) && cont <= 10 ) {
                        mCardAdapter.addLesson(lesson);
                        cont++;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                Toast.makeText(TrainActivity.this, "É necessário estar logado para usar o app.", Toast.LENGTH_LONG)
                        .show();
            }

        };
        lessonUsersQuery.addValueEventListener(mLessonsUserListener);


    }

    private Lesson getLessonByUid(final String lessonUid){
        train_lesson = null;
        ValueEventListener mLessonListener;
        Query mLesson;

        mLesson = DBHelper.getLessonByUid(lessonUid);
        mLessonListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                train_lesson = dataSnapshot.getValue(Lesson.class);
                if (train_lesson != null) {
                    train_lesson.setUid(dataSnapshot.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                Toast.makeText(TrainActivity.this,
                        "É necessário estar logado para usar o app.", Toast.LENGTH_LONG)
                        .show();
            }

        };
        mLesson.addValueEventListener(mLessonListener);

        return train_lesson;
    }
}
