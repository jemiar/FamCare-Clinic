package com.project.hoangminh.clinicapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<NoteItem> dataSet;

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTime;
        public TextView noteContent;

        public NoteViewHolder(View v) {
            super(v);
            noteTime = (TextView) v.findViewById(R.id.noteTime);
            noteContent = (TextView) v.findViewById(R.id.noteContent);
        }
    }

    public NoteAdapter() {
        dataSet = new ArrayList<>();
    }

    public NoteAdapter(List<NoteItem> ds) {
        dataSet = ds;
    }

    public void addItem(NoteItem i) {
        dataSet.add(i);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        NoteItem item = dataSet.get(i);
        noteViewHolder.noteTime.setText(item.getTime());
        noteViewHolder.noteContent.setText(item.getContent());
    }
}
