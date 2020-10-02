package com.example.happytimer;

public class Timer {
    private int id;
    private String title;
    private String colour;

    public Timer(){}

    public Timer(String title, String colour){
        this.title=title;
        this.colour=colour;
    }

    public Timer(int id, String title, String colour){
        this.id = id;
        this.title=title;
        this.colour=colour;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id ;
    }


    
}