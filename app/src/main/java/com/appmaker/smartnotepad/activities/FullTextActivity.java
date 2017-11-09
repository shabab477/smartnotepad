package com.appmaker.smartnotepad.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.appmaker.smartnotepad.R;

import org.w3c.dom.Text;

//This screen is responsible for showing the full
//note in big format.

//PLEASE LOOK INTO JAVA DOCS FOR OVERRIDEN METHODS
public class FullTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_text);

        Intent intent = getIntent();
        String text = intent.getStringExtra("note_text");
        ((TextView)findViewById(R.id.note_text_details)).setText(text);
    }
}
