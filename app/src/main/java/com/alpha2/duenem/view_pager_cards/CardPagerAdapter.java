package com.alpha2.duenem.view_pager_cards;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alpha2.duenem.QuestionActivity;
import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.R;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<Lesson> mData;
    private float mBaseElevation;
    private Context context;

    public CardPagerAdapter(Context context) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.context = context;
    }

    public void addLesson(Lesson item) {
        mViews.add(null);
        mData.add(item);
        this.notifyDataSetChanged();
    }

    public void clearLessons() {
        mViews.clear();
        mData.clear();
        this.notifyDataSetChanged();
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);

        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(final Lesson item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextViewAdapter);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextViewAdapter);
        Button bt = (Button) view.findViewById(R.id.buttonAdapter);
        view.findViewById(R.id.bookmark).setVisibility(View.GONE);
        if(item.isDone()){
            bt.setText(R.string.refazer);
            view.findViewById(R.id.bookmark).setVisibility(View.VISIBLE);
        }
        titleTextView.setText(item.getTitle());
        contentTextView.setText(item.getDescription());

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra(QuestionActivity.LESSON_EXTRA, item);
                context.startActivity(intent);
            }
        });
    }

}