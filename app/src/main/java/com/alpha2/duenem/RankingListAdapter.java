package com.alpha2.duenem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpha2.duenem.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class RankingListAdapter extends BaseAdapter {

    private List<User> rankingItems;

    public RankingListAdapter() {
        this.rankingItems = new ArrayList<>();
    }

    public void add(User user) {
        this.rankingItems.add(user);
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
    public User getItem(int position) {
        return this.rankingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.ranking_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        User item = getItem(position);
        assert item != null;
        holder.setName(item.getName());
        holder.setImageUri(item.getPhotoUrl());
        holder.setPoints(item.getPoints());

        return convertView;
    }

    private class ViewHolder {
        private View view;
        private ImageView photo;
        private TextView name;
        private TextView points;
        private ImageViewPicassoTarget target;

        public ViewHolder(View view) {
            this.view = view;
            name = (TextView) view.findViewById(R.id.name);
            points = (TextView) view.findViewById(R.id.points);
            photo = (ImageView) view.findViewById(R.id.photo);
            target = new ImageViewPicassoTarget(view.getContext(), photo);
        }

        public void setImageUri(String photoUrl) {
            Picasso.with(view.getContext())
                    .load(photoUrl)
                    .into(target);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setPoints(int points) {
            this.points.setText(String.valueOf(points));
        }
    }
}
