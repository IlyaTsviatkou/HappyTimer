package com.example.happytimer;

public class Period {
    private int id;
    private String title;
    private int seconds ;
    private int timerID;
    private String colour;

    public Period(){}

    public Period(String title, int seconds,String colour, int timerID){
        this.title=title;
        this.seconds=seconds;
        this.timerID = timerID;
        this.colour=colour;
    }

    public Period(int id, String title, int seconds,String colour,int timerID){
        this.id = id;
        this.title=title;
        this.seconds=seconds;
        this.timerID = timerID;
        this.colour=colour;
    }

    public int getTimerID() {return this.timerID;}

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id ;
    }

    public void setTimerID(int id){ this.timerID = id;}

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

}