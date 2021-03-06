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
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    // Required!
    public User() {
    }

    public User(@NonNull FirebaseUser user) {
        copyFromFirebaseUser(user);
    }

    public void copyFromFirebaseUser(@NonNull FirebaseUser firebaseUser) {
        this.name = firebaseUser.getDisplayName();
        this.email = firebaseUser.getEmail();
        this.uid = firebaseUser.getUid();
        this.photoUrl = firebaseUser.getPhotoUrl();
        this.phoneNumber = firebaseUser.getPhoneNumber();
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



    public void updateLastTimeLesson(String idLesson){

    }
}
