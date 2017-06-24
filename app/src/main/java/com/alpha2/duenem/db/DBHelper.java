package com.alpha2.duenem.db;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public abstract class DBHelper {

    public static Query getDisciplines() {
        return FirebaseDatabase.getInstance()
                .getReference("discipline");
    }
    public static Query getTopicsFromDiscipline(String disciplineUid) {
        return FirebaseDatabase.getInstance()
                .getReference("topic").child(disciplineUid);
    }

    public static Query getLessonsFromTopic(String topicUid) {
        return FirebaseDatabase.getInstance()
                .getReference("lesson").child(topicUid);
    }

    public static Query getMaterialsFromLesson(String lessonUid) {
        return FirebaseDatabase.getInstance()
                .getReference("material").child(lessonUid);
    }
}
