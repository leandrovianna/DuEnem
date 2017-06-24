package com.alpha2.duenem.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/06/17.
 */

public class Lesson implements Serializable {
    private String title;
    private String description;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private List<Material> materials;

    public Lesson() {
        materials = new ArrayList<>();
        title = "";
        description = "";
    }

    public Lesson(String title, String description){
        this.title = title;
        this.description = description;
        materials = new ArrayList<>();
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
    public List<Material> getMaterial() {
        return this.materials;
    }

    public void addMaterial(Material material) {
        materials.add(material);
    }
}
