package com.alpha2.duenem.view_pager_cards;

import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


import com.alpha2.duenem.BaseActivity;
import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.R;
import com.alpha2.duenem.model.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TrainActivity extends BaseActivity {
    public static final String TOPIC_EXTRA = "com.alpha2.duenem.topic_extra";
    private static final String TAG = LessonActivity.class.getSimpleName();
    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = setContentLayout(R.layout.content_lesson);

        mViewPager = (ViewPager) contentView.findViewById(R.id.viewPagerLesson);

        mCardAdapter = new CardPagerAdapter(this);

        final Topic topic = (Topic) getIntent().getSerializableExtra(TOPIC_EXTRA);

        setTitle(topic.getTitle());
        initiateList();
    }

    private void initiateList() {
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }
}
