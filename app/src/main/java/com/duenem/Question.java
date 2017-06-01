package com.duenem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.Serializable;

/**
 * Created by misael on 01/06/17.
 */

public class Question  implements Serializable {
    private StringBuffer text;
    public Question(){
        text = new StringBuffer();
    }

    public void buildContent(Activity activity, ViewGroup parent){
        TextView text = (TextView) activity.getLayoutInflater().inflate(R.layout.simple_text_content, parent, false);
        text.setText(this.text);
        parent.addView(text);
    }

    public void addText(CharSequence s){
        this.text.append(s);
    }
}
