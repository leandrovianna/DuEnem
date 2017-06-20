package com.alpha2.duenem;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_home);

        List<Topic> topics = getTopics();
        ArrayAdapter<Topic> adapter = new ArrayAdapter<Topic>(this,
                android.R.layout.simple_list_item_1, topics);

        ListView listView = (ListView) findViewById(R.id.listViewTopics);
        listView.setAdapter(adapter);

    }

    protected List<Topic> getTopics(){
        List<Topic> topics = new ArrayList<>();
        return topics;
    }
}
