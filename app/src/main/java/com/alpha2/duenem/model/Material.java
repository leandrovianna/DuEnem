package com.alpha2.duenem.model;

import java.io.Serializable;

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
