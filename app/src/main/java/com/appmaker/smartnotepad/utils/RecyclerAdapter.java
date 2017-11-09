package com.appmaker.smartnotepad.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appmaker.smartnotepad.R;
import com.appmaker.smartnotepad.model.FullText;
import com.appmaker.smartnotepad.model.Note;
import com.appmaker.smartnotepad.model.RemoveEvent;
import com.appmaker.smartnotepad.model.UpdateText;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

//This contains logic for what to show in the list
//and also what to do when someone presses one of the
//three buttons on any of the list item.
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private List<Note> list;
    private Context context;

    public RecyclerAdapter(Context context, List<Note> list)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);

        return new RecyclerViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Note note = list.get(position);
        if (note.getText().length() < 20) {
            holder.textView.setText(note.getText());
        }
        else {
            String text = note.getText().substring(0, 17) + "...";
            holder.textView.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //View holder for each items in a recycler view
    class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView editView;
        ImageView deleteView;
        ImageView showView;

        EditButtonListener editListener = new EditButtonListener();
        DeleteButtonListener deleteListener = new DeleteButtonListener();
        ShowFullListener showFullListener = new ShowFullListener();

        RecyclerViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            editView = itemView.findViewById(R.id.edit_view);
            deleteView = itemView.findViewById(R.id.delete_view);
            showView = itemView.findViewById(R.id.show_full);

            editView.setOnClickListener(editListener);
            deleteView.setOnClickListener(deleteListener);
            showView.setOnClickListener(showFullListener);
        }

        class ShowFullListener implements View.OnClickListener
        {

            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                Note note = list.get(pos);
                EventBus.getDefault().post(new FullText(note.getText()));
            }
        }

        //Edit button listener
        class EditButtonListener implements View.OnClickListener
        {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                Note note = list.get(pos);
                EventBus.getDefault().postSticky(new UpdateText(note.getText(), note.getId()));
            }
        }

        //Positive button listener
        class PositiveButtonCallback implements MaterialDialog.SingleButtonCallback {
            private int position;

            PositiveButtonCallback(int position)
            {
                this.position = position;
            }

            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Note note = list.get(position);
                list.remove(position);
                DBHelper.getInstance(context).deleteNote(note.getId());
                notifyItemRemoved(position);
                dialog.dismiss();
                EventBus.getDefault().post(new RemoveEvent());
            }
        }

        //Delete button listener
        class DeleteButtonListener implements View.OnClickListener
        {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                Note note = list.get(pos);
                Log.e("note_removing", note.getText());
                new MaterialDialog.Builder(context)
                        .title("Remove")
                        .content("Are you sure you want to remove this note?")
                        .positiveText("Agree")
                        .negativeText("Disagree")
                        .onPositive(new PositiveButtonCallback(getAdapterPosition()))
                        .show();
            }
        }
    }
}
