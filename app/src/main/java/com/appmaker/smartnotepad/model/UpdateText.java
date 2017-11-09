package com.appmaker.smartnotepad.model;

//The new text that needs to be updated
public class UpdateText {
    private String text;
    private int id;

    public UpdateText(String text, int id) {
        this.text = text;
        this.id = id;
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
}
