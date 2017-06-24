package com.alpha2.duenem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Discipline;
import com.alpha2.duenem.model.Topic;
import com.alpha2.duenem.view_pager_cards.LessonActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private ArrayAdapter<Topic> mAdapter;

    private ValueEventListener mTopicRefListener;
    private Query mTopicRef;

    public static final String DISCIPLINE_EXTRA = "discipline_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_home);

        Discipline discipline = (Discipline) getIntent().getSerializableExtra(DISCIPLINE_EXTRA);
        mTopicRef = null;

        if (discipline != null) {

            mAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, new ArrayList<Topic>());

            ListView listView = (ListView) findViewById(R.id.listViewTopics);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Topic t = (Topic) parent.getItemAtPosition(position);
                    Intent intent = new Intent(HomeActivity.this, LessonActivity.class);
                    intent.putExtra(LessonActivity.TOPIC_EXTRA, t);
                    startActivity(intent);
                }
            });

            mTopicRef = DBHelper.getTopicsFromDiscipline(discipline);

            mTopicRefListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mAdapter.clear();
                    for (DataSnapshot topicSnap : dataSnapshot.getChildren()) {
                        Topic topic = topicSnap.getValue(Topic.class);

                        if (topic != null) {
                            topic.setUid(topicSnap.getKey());
                            mAdapter.add(topic);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                    mAdapter.clear();
                    Toast.makeText(HomeActivity.this,
                            "É necessário estar logado para usar o app.", Toast.LENGTH_LONG)
                            .show();
                }

            };
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mTopicRef != null)
            mTopicRef.addValueEventListener(mTopicRefListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTopicRef != null)
            mTopicRef.removeEventListener(mTopicRefListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
