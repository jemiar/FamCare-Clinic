package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<String> dataset;
    private Context context;

    public static class UserHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView remove;

        public UserHolder(View v) {
            super(v);
            name = v.findViewById(R.id.userName);
            remove = v.findViewById(R.id.removeBtn);
        }
    }

    public UserAdapter(Context c) {
        context = c;
        dataset = new ArrayList<>();
    }

    public void addItem(String s) {
        dataset.add(s);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        return new UserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int i) {
        String n = dataset.get(i);
        userHolder.name.setText(n);
        userHolder.remove.setImageResource(R.drawable.remove);
        userHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DialogFragment passDiag = new EnterPassCodeDialog();
//                passDiag.show(((AppCompatActivity)context).getSupportFragmentManager(), "PasscodeDialog");
                Intent intent = new Intent(context, PassCode.class);
                Log.i("Here", "Right here");
                context.startActivity(intent);
//                Toast.makeText(context, "Hime", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
