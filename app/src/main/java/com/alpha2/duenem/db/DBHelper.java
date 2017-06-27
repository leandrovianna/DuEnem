package com.alpha2.duenem.db;

import com.alpha2.duenem.model.Discipline;
import com.alpha2.duenem.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public abstract class DBHelper {

    public static Query getDisciplines() {
        return FirebaseDatabase.getInstance()
                .getReference("discipline");
    }
    public static Query getTopicsFromDiscipline(Discipline discipline) {
        return FirebaseDatabase.getInstance()
                .getReference("topic").child(discipline.getUid());
    }

    public static Query getLessonsFromTopic(String topicUid) {
        return FirebaseDatabase.getInstance()
                .getReference("lesson").child(topicUid);
    }

    public static Query getLesson(String topicUid, String lessonUid) {
        return FirebaseDatabase.getInstance().getReference()
                .child("lesson").child(topicUid).child(lessonUid);
    }

    public static Query getMaterialsFromLesson(String lessonUid) {
        return FirebaseDatabase.getInstance()
                .getReference("material").child(lessonUid);
    }

    public static DatabaseReference getUsers() {
        return FirebaseDatabase.getInstance().getReference()
                .child("user");
    }

    public static DatabaseReference getLessonUsersByUser(String userUid) {
        return FirebaseDatabase.getInstance().getReference()
                .child("lessonUser").child(userUid);
    }

    public static Query getQueryRanking() {
        return DBHelper.getUsers().orderByChild("points").limitToFirst(100);
    }

    public static DatabaseReference getRoot() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
