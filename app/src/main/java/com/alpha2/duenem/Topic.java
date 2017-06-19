package com.alpha2.duenem;

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



    public Topic(String title, String description, List<Lesson> lessons){
        this.title = title;
        this.description = description;
        this.lessons = lessons;
    }

    public Topic(String title, String description){
        this.title = title;
        this.description = description;
        lessons = new ArrayList<>();
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
        return this.getDescription();
    }

    public List<Lesson> getLessons(){
        return this.lessons;
    }
}
