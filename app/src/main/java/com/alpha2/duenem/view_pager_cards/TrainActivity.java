package com.alpha2.duenem.view_pager_cards;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.alpha2.duenem.BaseActivity;
import com.alpha2.duenem.DuEnemApplication;
import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.R;
import com.alpha2.duenem.model.LessonUser;
import com.alpha2.duenem.model.Topic;
import com.alpha2.duenem.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class TrainActivity extends BaseActivity {
    private static final String TAG = TrainActivity.class.getSimpleName();
    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = setContentLayout(R.layout.content_lesson);
        mViewPager = (ViewPager) mContentView.findViewById(R.id.viewPagerLesson);
        mCardAdapter = new CardPagerAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateAdapter();
        initiateList();
    }

    private void initiateList() {
        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }

    public void populateAdapter() {
        User user = DuEnemApplication.getInstance().getUser();

        if (user != null) {
            String userUid = user.getUid();
            final Query lessonUsersQuery = DBHelper.getLessonUsersByUser(userUid).orderByChild("nextDate");
            ValueEventListener lessonsUserListener;

            lessonsUserListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCardAdapter.clearLessons();

                    for (DataSnapshot lessonUserSnap : dataSnapshot.getChildren()) {
                        getLessonAndAddToList(lessonUserSnap);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            };

            lessonUsersQuery.addListenerForSingleValueEvent(lessonsUserListener);
        } else {
            Snackbar.make(mContentView, R.string.user_unauthorized_message, Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
    }

    private void getLessonAndAddToList(DataSnapshot lessonUserSnap) {
        LessonUser lessonUser = lessonUserSnap.getValue(LessonUser.class);
        final Date nextDate = lessonUser.getNextDate();
        final Date nowDate = new Date();
        final Topic topic = new Topic();
        topic.setUid(lessonUser.getUidTopic());

        Query lessonQuery = DBHelper.getLesson(lessonUser.getUidTopic(), lessonUserSnap.getKey());

        lessonQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lesson lesson = dataSnapshot.getValue(Lesson.class);

                Log.d(TAG, nextDate.toString() + " " + nowDate.toString());
                if (lesson != null && !nextDate.after(nowDate)) {
                    lesson.setUid(dataSnapshot.getKey());
                    lesson.setTopic(topic);
                    lesson.setIsDone(false);
                    mCardAdapter.addLesson(lesson);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });
    }
}
