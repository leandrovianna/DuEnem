package com.alpha2.duenem.model;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String name;
    private String email;
    private String uid;
    private List<Question> questions;

    // Required!
    public User() {
    }

    public User(@NonNull FirebaseUser user) {
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.uid = user.getUid();
        this.questions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
