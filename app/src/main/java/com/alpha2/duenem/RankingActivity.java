package com.alpha2.duenem;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends BaseActivity {

    private static final String TAG = RankingActivity.class.getSimpleName();
    private RankingListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.content_ranking);

        ListView listView = (ListView) findViewById(R.id.rankingList);
        mAdapter = new RankingListAdapter();
        listView.setAdapter(mAdapter);

        Query rankingQuery = DBHelper.getQueryRanking();

        rankingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();

                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    users.add(user);
                }

                populateList(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });
    }

    private void populateList(List<User> users) {
        mAdapter.clear();
        Collections.reverse(users);
        for (User u : users)
            mAdapter.add(u);
    }

}
