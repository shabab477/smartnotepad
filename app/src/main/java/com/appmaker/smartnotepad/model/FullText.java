package com.appmaker.smartnotepad.model;

//Used for passing in full text
public class FullText {
    private String text;

    public FullText(String text)
    {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
