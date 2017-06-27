package com.alpha2.duenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alpha2.duenem.db.DBHelper;
import com.alpha2.duenem.model.Discipline;
import com.alpha2.duenem.model.Topic;
import com.alpha2.duenem.view_pager_cards.LessonActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisciplineActivity extends BaseActivity {

    private static final String TAG = DisciplineActivity.class.getSimpleName();
    private ArrayAdapter<Topic> mAdapter;

    private ValueEventListener mTopicRefListener;
    private Query mTopicRef;

    public static final String DISCIPLINE_EXTRA = "discipline_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View contentView = setContentLayout(R.layout.content_discipline);

        Discipline discipline = (Discipline) getIntent().getSerializableExtra(DISCIPLINE_EXTRA);
        mTopicRef = null;

        if (discipline != null) {

            setTitle(discipline.getName());

            mAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, new ArrayList<Topic>());

            ListView listView = (ListView) findViewById(R.id.listViewTopics);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Topic t = (Topic) parent.getItemAtPosition(position);
                    Intent intent = new Intent(DisciplineActivity.this, LessonActivity.class);
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
                    Snackbar.make(contentView, R.string.user_unauthorized_message, Snackbar.LENGTH_INDEFINITE)
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
