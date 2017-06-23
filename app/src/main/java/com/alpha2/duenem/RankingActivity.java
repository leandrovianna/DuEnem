package com.alpha2.duenem;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.alpha2.duenem.model.RankingItem;
import com.alpha2.duenem.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends BaseActivity {

    private static final String TAG = RankingActivity.class.getSimpleName();
    private RankingListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.content_ranking);

        ListView listView = (ListView) findViewById(R.id.rankingList);
        mAdapter = new RankingListAdapter(this);
        listView.setAdapter(mAdapter);

        DatabaseReference rankingRef = FirebaseDatabase.getInstance().getReference("ranking");
        Query rankingQuery = rankingRef.orderByValue().limitToFirst(30);
        rankingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAdapter.clear();
                for (DataSnapshot rankingItemSnap : dataSnapshot.getChildren())
                    addItemToList(rankingItemSnap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });
    }

    private void addItemToList(DataSnapshot dataSnapshot) {
        final Double points = dataSnapshot.getValue(Double.class);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(dataSnapshot.getKey());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    RankingItem rankingItem = new RankingItem(user, points.intValue());
                    mAdapter.addFront(rankingItem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });
    }

}
