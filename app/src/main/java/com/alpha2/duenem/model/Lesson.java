package com.alpha2.duenem.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 13/06/17.
 */

public class Lesson implements Serializable {
    private String title;
    private String description;

    private List<Material> materials;

    public Lesson() {}

    public Lesson(String title, String description){
        this.title = title;
        this.description = description;
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
}
