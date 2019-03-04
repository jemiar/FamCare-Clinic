package com.project.hoangminh.clinicapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewItem> dataSet;

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public TextView value;

        public ReviewViewHolder(View v) {
            super(v);
            label = v.findViewById(R.id.review_label);
            value = v.findViewById(R.id.review_value);
        }
    }

    public ReviewAdapter() {
        dataSet = new ArrayList<>();
    }

    public void addItem(ReviewItem i) {
        dataSet.add(i);
    }

    public List<ReviewItem> getDataSet() {
        return dataSet;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup, false);
        return new ReviewAdapter.ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder reviewViewHolder, int i) {
        ReviewItem item = dataSet.get(i);
        reviewViewHolder.label.setText(item.getLabel());
        reviewViewHolder.value.setText(item.getVal());
    }
}
