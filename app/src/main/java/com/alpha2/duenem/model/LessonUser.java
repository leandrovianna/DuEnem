package com.alpha2.duenem.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessonUser implements Serializable {
    private String uidTopic;
    private Date lastDate;
    private int correctStreak;
    private Date nextDate;
    private double EF;
    private int interval;
    private static SimpleDateFormat dateFormat;

    public LessonUser() {
        if (dateFormat == null)
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.EF = 1.3;
        this.interval = 1;
    }

    public LessonUser(Date lastDate, int correctStreak, Date nextDate) {
        this();
        this.lastDate = lastDate;
        this.correctStreak = correctStreak;
        this.nextDate = nextDate;
        this.EF = 1.3;
        this.interval = 1;
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

    public double getEF(){
        return this.EF;
    }

    private void setNextEF(int q){
        EF = EF+(0.1-(5-q)*(0.08+(5-q)*0.02));
        if(EF < 1.3) EF = 1.3;
    }

    public void setNextInterval(int q){
        if (q < 3) {
            interval = 1;
            correctStreak = 0;
        }
        else if(correctStreak == 0) {
            interval = 1;
            correctStreak++;
        }
        else if(correctStreak == 1) {
            interval = 4;
            correctStreak++;
        }
        else {
            setNextEF(q);
            interval = (int) (interval * EF);
            correctStreak++;
        }
    }
    public int getInterval(){
        return interval;
    }

    public String getUidTopic() {
        return uidTopic;
    }

    public void setUidTopic(String uidTopic) {
        this.uidTopic = uidTopic;
    }
}
