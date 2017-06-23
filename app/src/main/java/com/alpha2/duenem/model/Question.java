package com.alpha2.duenem.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by misael on 01/06/17.
 */

public class Question extends Material {
    private static final int ALTERNATIVES_MAX = 6;
    private List<String> alternativesList;
    private Map<String, Boolean> alternatives;
    private int number_of_alternatives;
    private int correct_alternative;

    public Question(){
        super();
        number_of_alternatives = 0;
    }

    public Question(String title, String text, List<String> alternativesList){
        super(title, text);
        alternativesList = new ArrayList<>();
        number_of_alternatives = alternativesList.size();
        if(number_of_alternatives > ALTERNATIVES_MAX) number_of_alternatives = 6;


    }

    public void addAlternative(String alternative_content){
        this.alternativesList.add(alternative_content);
    }

    public Map<String, Boolean> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(Map<String, Boolean> alternatives) {
        this.alternatives = alternatives;
    }

    public String getTextAlternative(int i){
        return alternativesList.get(i);
    }

    public int getCorrect_alternative() {
        return correct_alternative;
    }

    public void setCorrect_alternative(int correct_alternative) {
        this.correct_alternative = correct_alternative;
    }
}
