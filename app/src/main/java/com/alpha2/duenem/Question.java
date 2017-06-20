package com.alpha2.duenem;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;


import java.io.Serializable;
import java.util.List;

/**
 * Created by misael on 01/06/17.
 */

public class Question extends Material {
    private Text text;
    private Text[] alternatives = new Text[6];
    int number_of_alternatives;

    public Question(){
        text = new Text();
        title = "";
        number_of_alternatives = 0;
    }

    public Question(String title, Text text, List<Text> alternatives){
        this.title = title;
        this.text = text;
        number_of_alternatives = alternatives.size();
        if(number_of_alternatives > 6) number_of_alternatives = 6;

        for(int i = 0; i < 6 && i < alternatives.size(); i++)
            this.alternatives[i] = alternatives.get(i);

    }

    public void buildContent(Activity activity, ViewGroup parent){
        TextView text = (TextView) activity.getLayoutInflater().inflate(com.alpha2.duenem.R.layout.simple_text_content, parent, false);
        text.setText(this.title);
        text.setTextSize(text.getTextSize() * 2); // example, we must change
        parent.addView(text);

        this.text.buildContent(activity, parent);



    }

    public void setTitle(String title){
        this.title = title;
    }
    public void addText(CharSequence s){
        this.text.addText(s);
    }
    public void addAlternative(Text alternative_content){
        if(number_of_alternatives < 6)
            alternatives[number_of_alternatives++] = alternative_content;
    }
}
