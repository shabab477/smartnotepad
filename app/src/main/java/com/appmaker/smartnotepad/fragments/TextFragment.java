package com.appmaker.smartnotepad.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appmaker.smartnotepad.R;
import com.appmaker.smartnotepad.model.MessageEvent;
import com.appmaker.smartnotepad.model.Note;
import com.appmaker.smartnotepad.model.UpdateText;
import com.appmaker.smartnotepad.utils.DBHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */

//This screen is responsible for taking input as note
//and saving it to the database.

//PLEASE LOOK INTO JAVA DOCS FOR OVERRIDEN METHODS
public class TextFragment extends Fragment {

    private EditText editText;
    private boolean isUpdate;
    private UpdateText text;

    public TextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_text, container, false);
        AppCompatButton button = v.findViewById(R.id.save_button);
        editText = v.findViewById(R.id.note_edit);
        button.setOnClickListener(new SaveButtonListener());
        handleUpdateLogic();
        return v;
    }

    //Logic for handling event bus sticky posts
    private void handleUpdateLogic() {
        text = EventBus.getDefault().getStickyEvent(UpdateText.class);
        EventBus.getDefault().removeAllStickyEvents();
        isUpdate = text != null;
        if(isUpdate)
            editText.setText(text.getText());
    }

    //Listener for save button
    class SaveButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            String str = editText.getText().toString();
            if(str == null || str.length() == 0)
            {
                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("MUST GIVE AN INPUT")
                        .positiveText("OK")
                        .show();
            }
            else {
                if(!isUpdate) {
                    long time = Calendar.getInstance().getTimeInMillis();
                    Note note = new Note(str, time);
                    DBHelper.getInstance(getContext()).saveNote(note);
                    EventBus.getDefault().post(new MessageEvent("Successfully added note"));
                }
                else
                {
                    int id = text.getId();
                    EventBus.getDefault().post(new MessageEvent("Successfully updated note"));
                    DBHelper.getInstance(getContext()).updateNote(id, str);
                }
            }
        }
    }

}
