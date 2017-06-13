package com.alpha2.duenem;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by root on 12/06/17.
 */

public class Text {
    private StringBuffer text;
    public Text(){
        text = new StringBuffer();
    }

    public void buildContent(Activity activity, ViewGroup parent){
        TextView text = (TextView) activity.getLayoutInflater().inflate(com.alpha2.duenem.R.layout.simple_text_content, parent, false);
        text.setText(this.text);
        parent.addView(text);
    }

    public void addText(CharSequence s){
        this.text.append(s);
    }
}
