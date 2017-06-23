package com.alpha2.duenem.model;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alpha2.duenem.R;

import java.io.Serializable;

/**
 * Created by root on 13/06/17.
 */

public class Material implements Serializable {
    private String title;
    private String text;

    public Material() {
        this.title = "Title";
        this.text = "Text";
    }

    public Material(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addText(String text) {
        this.text += text;
    }



}
