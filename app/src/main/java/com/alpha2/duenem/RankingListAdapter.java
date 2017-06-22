package com.alpha2.duenem;

import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.alpha2.duenem.model.RankingItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

class RankingListAdapter implements ListAdapter {

    private List<RankingItem> rankingItemList;

    public RankingListAdapter(List<RankingItem> rankingList) {
        this.rankingItemList = rankingList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return rankingItemList.size();
    }

    @Override
    public RankingItem getItem(int position) {
        return rankingItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = inflater.inflate(R.layout.ranking_list_item, parent, false);
        }

        TextView nameText = (TextView) convertView.findViewById(R.id.name);
        TextView pointsText = (TextView) convertView.findViewById(R.id.points);
        final ImageView photoView = (ImageView) convertView.findViewById(R.id.photo);

        RankingItem item = getItem(position);
        nameText.setText(item.getUser().getName());
        pointsText.setText(String.valueOf(item.getPoints()));

        Picasso.with(parent.getContext())
                .load(item.getUser().getPhotoUrl())
                .into(new ImageViewPicassoTarget(parent.getContext(), photoView));

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return rankingItemList.isEmpty();
    }
}
