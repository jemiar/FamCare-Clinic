package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VitalAdapter extends RecyclerView.Adapter<VitalAdapter.VitalViewHolder> {

    private List<Vital_Item> dataSet;
    private List<String> responses;
    private Context context;

    public static class VitalViewHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public Button low_button;
        public Button normal_button;
        public Button hi_button;

        public VitalViewHolder(View v) {
            super(v);
            label = v.findViewById(R.id.label);
            low_button = v.findViewById(R.id.low_button);
            normal_button = v.findViewById(R.id.normal_button);
            hi_button = v.findViewById(R.id.hi_button);
        }
    }

    public VitalAdapter(Context c) {
        dataSet = new ArrayList<>();
        responses = new ArrayList<>();
        context = c;
    }

    public List<String> getResponses() {
        return responses;
    }

    public void addItem(Vital_Item i) {
        dataSet.add(i);
        responses.add("NOVALUE");
    }

    public List<Vital_Item> getDataSet() {
        return dataSet;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //IMPORTANT: Needed so each row does not affect other
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public VitalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vital_item, viewGroup, false);
        return new VitalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VitalViewHolder vitalViewHolder, final int i) {
        Vital_Item item = dataSet.get(i);
        vitalViewHolder.label.setText(item.getLabel());
        final Button low_btn = vitalViewHolder.low_button;
        final Button norm_btn = vitalViewHolder.normal_button;
        final Button hi_btn = vitalViewHolder.hi_button;
        low_btn.setText(item.getLow_btn_txt());
        low_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCompat.setBackgroundTintList(low_btn, AppCompatResources.getColorStateList(context, R.color.barColor));
                low_btn.setTextColor(context.getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(norm_btn, AppCompatResources.getColorStateList(context, R.color.white));
                norm_btn.setTextColor(context.getResources().getColor(R.color.black));
                ViewCompat.setBackgroundTintList(hi_btn, AppCompatResources.getColorStateList(context, R.color.white));
                hi_btn.setTextColor(context.getResources().getColor(R.color.black));

                responses.remove(i);
                responses.add(i, low_btn.getText().toString());
            }
        });
        norm_btn.setText(item.getNormal_btn_txt());
        norm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCompat.setBackgroundTintList(norm_btn, AppCompatResources.getColorStateList(context, R.color.barColor));
                norm_btn.setTextColor(context.getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(low_btn, AppCompatResources.getColorStateList(context, R.color.white));
                low_btn.setTextColor(context.getResources().getColor(R.color.black));
                ViewCompat.setBackgroundTintList(hi_btn, AppCompatResources.getColorStateList(context, R.color.white));
                hi_btn.setTextColor(context.getResources().getColor(R.color.black));

                responses.remove(i);
                responses.add(i, norm_btn.getText().toString());
            }
        });
        if(item.get_btn_no()) {
            vitalViewHolder.hi_button.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vitalViewHolder.low_button.getLayoutParams();
            params.setMarginStart(400);
            vitalViewHolder.low_button.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) vitalViewHolder.normal_button.getLayoutParams();
            params2.setMarginStart(600);
            vitalViewHolder.normal_button.setLayoutParams(params2);
        }
        hi_btn.setText(item.getHi_btn_txt());
        hi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCompat.setBackgroundTintList(hi_btn, AppCompatResources.getColorStateList(context, R.color.barColor));
                hi_btn.setTextColor(context.getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(norm_btn, AppCompatResources.getColorStateList(context, R.color.white));
                norm_btn.setTextColor(context.getResources().getColor(R.color.black));
                ViewCompat.setBackgroundTintList(low_btn, AppCompatResources.getColorStateList(context, R.color.white));
                low_btn.setTextColor(context.getResources().getColor(R.color.black));

                responses.remove(i);
                responses.add(i, hi_btn.getText().toString());
            }
        });
    }
}
