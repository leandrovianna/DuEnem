package com.alpha2.duenem.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Question extends Material {
    private static final int ALTERNATIVES_MAX = 6;
    private List<String> alternatives;
    private int number_of_alternatives;
    private int correct_alternative;

    public Question(){
        super();
        number_of_alternatives = 0;
        alternatives = new ArrayList<>();
    }

    public Question(String title, String text, List<String> alternatives, int correct_alternative){
        super(title, text);
        this.alternatives = alternatives;
        number_of_alternatives = alternatives.size();
        if(number_of_alternatives > ALTERNATIVES_MAX) number_of_alternatives = 6;
        this.correct_alternative = correct_alternative;

    }

    public void addAlternative(String alternative_content){
        this.alternatives.add(alternative_content);
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    public String getTextAlternative(int i){
        return alternatives.get(i);
    }

    public int getCorrect_alternative() {
        return correct_alternative;
    }

    public void setCorrect_alternative(int correct_alternative) {
        this.correct_alternative = correct_alternative;
    }
}
