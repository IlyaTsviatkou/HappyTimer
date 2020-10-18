package com.example.happytimer;

public class Timer {
    private int id;
    private String title;


    public Timer(){}

    public Timer(String title){
        this.title=title;

    }

    public Timer(int id, String title){
        this.id = id;
        this.title=title;

    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id ;
    }


    
}