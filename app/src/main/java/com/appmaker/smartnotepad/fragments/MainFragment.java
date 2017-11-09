package com.appmaker.smartnotepad.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.appmaker.smartnotepad.R;
import com.appmaker.smartnotepad.model.Note;
import com.appmaker.smartnotepad.model.RemoveEvent;
import com.appmaker.smartnotepad.utils.DBHelper;
import com.appmaker.smartnotepad.utils.RecyclerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

//This is the screen for showing all the lists of notes

//PLEASE LOOK INTO JAVA DOCS FOR OVERRIDEN METHODS
public class MainFragment extends Fragment {

    RecyclerView recyclerView;
    
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = v.findViewById(R.id.recycler);
        setupRecycler();

        return v;
    }

    //This method initializes the list with data from the database
    private void setupRecycler() {
        List<Note> list = DBHelper.getInstance(getContext()).getAvailableNotes();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerAdapter adapter = new RecyclerAdapter(getContext(), list == null? new ArrayList<Note>() : list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }


}
