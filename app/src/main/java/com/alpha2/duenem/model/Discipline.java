package com.alpha2.duenem.model;

import java.io.Serializable;

public class Discipline implements Serializable {
    private String uid;
    private String name;

    public Discipline() {
    }

    public Discipline(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
