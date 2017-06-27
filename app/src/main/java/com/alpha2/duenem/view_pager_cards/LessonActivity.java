package com.alpha2.duenem.view_pager_cards;

import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


import com.alpha2.duenem.BaseActivity;
import com.alpha2.duenem.DuEnemApplication;
import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.R;
import com.alpha2.duenem.model.Topic;
import com.alpha2.duenem.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LessonActivity extends BaseActivity {
    public static final String TOPIC_EXTRA = "com.alpha2.duenem.topic_extra";
    private static final String TAG = LessonActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = setContentLayout(R.layout.content_lesson);

        mViewPager = (ViewPager) mContentView.findViewById(R.id.viewPagerLesson);

        mCardAdapter = new CardPagerAdapter(this);

        initiateList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (DuEnemApplication.getInstance().getUser() != null) {

            final Topic topic = (Topic) getIntent().getSerializableExtra(TOPIC_EXTRA);

            setTitle(topic.getTitle());

            Query lessonQuery = DBHelper.getLessonsFromTopic(topic.getUid());
            lessonQuery.keepSynced(true);
            lessonQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCardAdapter.clearLessons();

                    for (DataSnapshot lessonSnap : dataSnapshot.getChildren()) {
                        Lesson l = lessonSnap.getValue(Lesson.class);
                        if (l != null) {
                            l.setUid(lessonSnap.getKey());
                            l.setTopic(topic);
                            verifyIfUserDoneLesson(l);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, databaseError.getMessage());
                }
            });
        } else {
            Snackbar.make(mContentView, R.string.user_unauthorized_message, Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
    }

    private void verifyIfUserDoneLesson(final Lesson lesson) {
        User user = DuEnemApplication.getInstance().getUser();

        Query lessonQuery = DBHelper.getLessonUsersByUser(user.getUid());
        lessonQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lesson.setIsDone(dataSnapshot.hasChild(lesson.getUid()));
                mCardAdapter.addLesson(lesson);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });

    }

    private void initiateList() {
        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }
}
