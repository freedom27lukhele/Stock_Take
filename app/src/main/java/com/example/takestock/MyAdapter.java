package com.example.takestock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;

    ArrayList<User> list;

    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView deviceName,personsName,deviceSerial,date;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            deviceName = itemView.findViewById(R.id.tvdevname);
            personsName = itemView.findViewById(R.id.tvholdersname);
            deviceSerial = itemView.findViewById(R.id.tvserialnum);
            date = itemView.findViewById(R.id.tvdatescanned);


        }
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        User user = list.get(position);
        holder.deviceName.setText(user.getDeviceName());
        holder.personsName.setText(user.getPersonsName());
        holder.deviceSerial.setText(user.getDeviceSerial());
        holder.date.setText(user.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
