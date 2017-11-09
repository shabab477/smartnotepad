package com.appmaker.smartnotepad.model;


//This is a container for temporary storage
//of note information.
public class Note {
    private int id;
    private String text;
    private long time;

    public Note(String text, long time) {
        this.text = text;
        this.time = time;
        id = -1;
    }

    public Note(int id, String text, long time) {
        this.id = id;
        this.text = text;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
