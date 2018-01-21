package com.audora.digiroom;

import java.util.ArrayList;

/**
 * Created by ud on 1/17/2018.
 */

public class Quiz {
    private String id;
    private String name;
    private ArrayList<Question> questions;
    private String duration;
    private Integer totalMarks;
    private String code;

    public Quiz(){
        this.totalMarks=0;
        this.id="";
        this.name="";
        this.duration="";
        this.code="";
        this.questions=new ArrayList<>();
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Question> getQuestions(){
        return questions;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
