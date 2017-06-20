package com.alpha2.duenem.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/06/17.
 */

public class Topic implements Serializable {
    private String title;
    private String description;
    private long id;
    private List<Lesson> lessons;

    public Topic() {
        this.lessons = new ArrayList<>();
    }

    public Topic(String title, String description){
        this();
        this.title = title;
        this.description = description;
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }


    @Exclude
    public List<Lesson> getLessons(){
        return this.lessons;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", title, description);
    }
}
