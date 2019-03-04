package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<FamCareMessage> dataSet;
    //Need context to get the context of activity/fragment to get resources
    private Context msgContext;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView content;

        public MessageViewHolder(View v) {
            super(v);
            content = (TextView) v.findViewById(R.id.content);
        }

    }

    public MessageAdapter(List<FamCareMessage> ds, Context c) {
        dataSet = ds;
        msgContext = c;
    }

    public MessageAdapter(Context c) {
        dataSet = new ArrayList<>();
        msgContext = c;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addMessage(FamCareMessage m) {
        dataSet.add(m);
    }

    //Inflate the View Holder
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
        return new MessageViewHolder(v);
    }

    //Set value for the View Holder's Items
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        FamCareMessage msgItem = dataSet.get(i);
//        messageViewHolder.user.setText(msgItem.getUserName());
        messageViewHolder.content.setText(msgItem.getText());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) messageViewHolder.content.getLayoutParams();
        if(msgItem.getUserName().equals("Clinic")) {
            messageViewHolder.content.setTextColor(msgContext.getResources().getColor(R.color.white));
            messageViewHolder.content.setBackground(msgContext.getResources().getDrawable(R.drawable.roundedrec));
            messageViewHolder.content.getBackground().setColorFilter(msgContext.getResources().getColor(R.color.barColor), PorterDuff.Mode.SRC_ATOP);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        } else {
            messageViewHolder.content.setTextColor(BLACK);
            messageViewHolder.content.getBackground().setColorFilter(msgContext.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
    }
}
