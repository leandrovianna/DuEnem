package com.alpha2.duenem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alpha2.duenem.view_pager_cards.LessonActivity;
import com.google.firebase.database.ChildEventListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_home);

        List<Topic> topics = new ArrayList<>();
        mAdapter = new ArrayAdapter<Topic>(this,
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

        DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference()
                .child("topic");
        final DatabaseReference lessonRef = FirebaseDatabase.getInstance().getReference()
                .child("lesson");

        topicRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Topic topic = dataSnapshot.getValue(Topic.class);
                if (topic != null) {
                    lessonRef.child(dataSnapshot.getKey())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot lessonSnapshot : dataSnapshot.getChildren()) {
                                        topic.addLesson(lessonSnapshot.getValue(Lesson.class));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, databaseError.getMessage());
                                }
                            });
                    mAdapter.add(topic);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }
}
