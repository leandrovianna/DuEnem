package com.alpha2.duenem.model;

import java.util.Date;

/**
 * Created by misael on 25/06/17.
 */

public class LessonHistoric {
    private int contCorrect;
    private Date lastMade;

    public void setLessonMade(Date date){
        this.lastMade = date;
        contCorrect++;
    }

    public void WrongLesson(){
        contCorrect = 0;
    }

    public LessonHistoric(){
        contCorrect = 0;
    }

    public Date getNextDate(){
        Date date = new Date();
        return date;
    }
}
