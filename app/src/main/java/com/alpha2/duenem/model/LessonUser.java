package com.alpha2.duenem.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessonUser implements Serializable {

    private Date lastDate;
    private int correctStreak;
    private Date nextDate;

    private static SimpleDateFormat dateFormat;

    public LessonUser() {
        if (dateFormat == null)
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public LessonUser(Date lastDate, int correctStreak, Date nextDate) {
        this();
        this.lastDate = lastDate;
        this.correctStreak = correctStreak;
        this.nextDate = nextDate;
    }

    @Exclude
    public Date getLastDate() {
        return lastDate;
    }

    @Exclude
    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public int getCorrectStreak() {
        return correctStreak;
    }

    public void setCorrectStreak(int correctStreak) {
        this.correctStreak = correctStreak;
    }

    @Exclude
    public Date getNextDate() {
        return nextDate;
    }

    @Exclude
    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    // Firebase Database Getters and Setters

    @PropertyName("lastDate")
    public String getLastDateString() {
        return dateFormat.format(this.lastDate);
    }

    @PropertyName("nextDate")
    public String getNextDateString() {
        return dateFormat.format(this.nextDate);
    }

    public void setNextDate(String nextDate) throws ParseException {
        this.nextDate = dateFormat.parse(nextDate);
    }

    public void setLastDate(String lastDate) throws ParseException {
        this.lastDate = dateFormat.parse(lastDate);
    }
}
