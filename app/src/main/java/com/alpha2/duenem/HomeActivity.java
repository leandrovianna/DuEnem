package com.alpha2.duenem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.model.Topic;
import com.alpha2.duenem.signin.SignInActivity;
import com.alpha2.duenem.view_pager_cards.LessonActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private ArrayAdapter<Topic> mAdapter;

    private ValueEventListener mTopicRefListener;
    private DatabaseReference mTopicRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_home);

        List<Topic> topics = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, topics);

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

        mTopicRef = FirebaseDatabase.getInstance().getReference()
                .child("topic");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mTopicRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setListData(dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                openSignInActivity();
            }
        };

        mTopicRef.addValueEventListener(mTopicRefListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTopicRef.removeEventListener(mTopicRefListener);
    }

    private void openSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra(SELECTED_ITEM_ID_EXTRA, R.id.perfil);
        startActivity(intent);
    }

    private void setListData(Iterable<DataSnapshot> children) {
        mAdapter.clear();
        for (DataSnapshot topicSnap : children) {
            Topic topic = topicSnap.getValue(Topic.class);

            if (topic != null) {
                for (DataSnapshot lessonSnap : topicSnap.child("lessons").getChildren()) {
                    topic.addLesson(lessonSnap.getValue(Lesson.class));
                }
                mAdapter.add(topic);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
