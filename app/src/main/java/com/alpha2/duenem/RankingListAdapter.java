package com.alpha2.duenem;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.alpha2.duenem.model.RankingItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

class RankingListAdapter extends BaseAdapter {

    private List<RankingItem> rankingItems;

    public RankingListAdapter(Context context) {
        this.rankingItems = new ArrayList<>();
    }

    public void add(RankingItem rankingItem) {
        this.rankingItems.add(rankingItem);
        this.notifyDataSetChanged();
    }

    public void addFront(RankingItem rankingItem) {
        this.rankingItems.add(0, rankingItem);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.rankingItems.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.rankingItems.size();
    }

    @Nullable
    @Override
    public RankingItem getItem(int position) {
        return this.rankingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = inflater.inflate(R.layout.ranking_list_item, parent, false);
        }

        TextView nameText = (TextView) convertView.findViewById(R.id.name);
        TextView pointsText = (TextView) convertView.findViewById(R.id.points);
        final ImageView photoView = (ImageView) convertView.findViewById(R.id.photo);

        RankingItem item = getItem(position);
        assert item != null;
        nameText.setText(item.getUser().getName());
        pointsText.setText(String.valueOf(item.getPoints()));

        Picasso.with(parent.getContext())
                .load(item.getUser().getPhotoUrl())
                .into(new ImageViewPicassoTarget(parent.getContext(), photoView));

        return convertView;
    }
}
