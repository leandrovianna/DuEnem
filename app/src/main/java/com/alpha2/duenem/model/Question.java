package com.alpha2.duenem.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;


import com.alpha2.duenem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by misael on 01/06/17.
 */

public class Question extends Material {
    private static final int ALTERNATIVES_MAX = 6;
    private List<String> alternatives;
    private int number_of_alternatives;

    public Question(){
        super();
        alternatives = new ArrayList<>();
        number_of_alternatives = 0;
    }

    public Question(String title, String text, List<String> alternatives){
        super(title, text);
        alternatives = new ArrayList<>();
        number_of_alternatives = alternatives.size();
        if(number_of_alternatives > ALTERNATIVES_MAX) number_of_alternatives = 6;

        for(int i = 0; i < ALTERNATIVES_MAX && i < alternatives.size(); i++)
            this.alternatives.add(alternatives.get(i));

    }

    @Override
    public void buildContent(Activity activity, ViewGroup parent){
        super.buildContent(activity, parent);

        LayoutInflater inflater = activity.getLayoutInflater();
        for (String alt : alternatives) {
            TextView textView = (TextView) inflater.inflate(R.layout.simple_text_content, parent, false);
            textView.setText(this.getTitle());
            parent.addView(textView);
        }
    }

    public void addAlternative(String alternative_content){
        if(number_of_alternatives < ALTERNATIVES_MAX)
            alternatives.add(alternative_content);
    }
}
