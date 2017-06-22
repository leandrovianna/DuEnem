package com.alpha2.duenem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alpha2.duenem.model.RankingItem;
import com.alpha2.duenem.model.User;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends BaseActivity {

    private RankingListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.content_ranking);

        ListView listView = (ListView) findViewById(R.id.rankingList);

        List<RankingItem> list = new ArrayList<>();

        list.add(new RankingItem(new User(mAuth.getCurrentUser()), 30492));
        list.add(new RankingItem(new User(mAuth.getCurrentUser()), 23092));

        mAdapter = new RankingListAdapter(list);
        listView.setAdapter(mAdapter);
    }

}
