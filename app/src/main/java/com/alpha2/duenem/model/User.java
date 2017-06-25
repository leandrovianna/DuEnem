package com.alpha2.duenem.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String name;
    private String email;
    private String uid;
    private String phoneNumber;
    private Uri photoUrl;
    private List<LessonHistoric> historic;

    // Required!
    public User() {
    }

    public User(@NonNull FirebaseUser user) {
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.uid = user.getUid();
        this.photoUrl = user.getPhotoUrl();
        this.phoneNumber = user.getPhoneNumber();
        this.historic = new ArrayList<>();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhotoUrl() {
        return photoUrl.toString();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhotoUrl(String photoUrl) {
        this.setPhotoUrl(Uri.parse(photoUrl));
    }

    @Exclude
    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<LessonHistoric> getHistoric() {
        return historic;
    }

    public void updateLastTimeLesson(String idLesson){

    }
}
