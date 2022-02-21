package com.example.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<payinfo> list;


    public MyAdapter(Context context, ArrayList<payinfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        payinfo user = list.get(position);
        holder.Name.setText(user.getName());
        holder.Money.setText(String.valueOf(user.getMoney())+".00฿");
        holder.plate.setText(user.getPlate());
        holder.Timein.setText(user.getTime_in());
        holder.Timeout.setText(user.getTime_out());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name, Time,Timein,Timeout, Money,plate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Timein = itemView.findViewById(R.id.Text_timein);
            Timeout = itemView.findViewById(R.id.Text_timeout);
            Name = itemView.findViewById(R.id.Text_name);
            plate =itemView.findViewById(R.id.Text_plate);
            Money = itemView.findViewById(R.id.Text_money);


        }
    }

}
