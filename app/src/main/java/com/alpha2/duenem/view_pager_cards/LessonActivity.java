package com.alpha2.duenem.view_pager_cards;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import com.alpha2.duenem.BaseActivity;
import com.alpha2.duenem.Lesson;
import com.alpha2.duenem.R;
import com.alpha2.duenem.Topic;

import java.util.List;

public class LessonActivity extends BaseActivity {
    public static final String TOPIC_EXTRA = "com.alpha2.duenem.topic_extra";
    private Button mButton;
    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    private ShadowTransformer mFragmentCardShadowTransformer;

    private boolean mShowingFragments = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = setContentLayout(R.layout.content_lesson);

        mViewPager = (ViewPager) contentView.findViewById(R.id.viewPagerLesson);

        mCardAdapter = new CardPagerAdapter();
        Topic topic = (Topic) getIntent().getSerializableExtra(TOPIC_EXTRA);
        List<Lesson> lessons = topic.getLessons();
        for(int i = 0; i < lessons.size(); i++)
            mCardAdapter.addLesson(lessons.get(i));


        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }


}
