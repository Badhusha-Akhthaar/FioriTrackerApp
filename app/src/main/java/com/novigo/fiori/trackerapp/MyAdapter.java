package com.novigo.fiori.trackerapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.target.Target;
import com.sap.cloud.mobile.fiori.object.ObjectCell;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

//    ArrayList<String> d1;
//    ArrayList<String> d2;
//    ArrayList<String> d3;
//    ArrayList<String> d4;
//    ArrayList<String> d5;
    String d1[],d2[];
    Context context;
    public MyAdapter(Context ct, String fo[],String truck[]){
        this.context = ct;
        this.d1 = fo;
        this.d2 = truck;

    }
//    public MyAdapter(Context ct, ArrayList<String> fo, ArrayList<String> truck, ArrayList<String> status, ArrayList<String> desc, ArrayList<String> enddate){
//        this.context = ct;
//        this.d1 = fo;
//        this.d2 = truck;
//        this.d3 = status;
//        this.d4 = desc;
//        this.d5 = enddate;
//        Log.e("CHECK","Inside MyAdapter");
//
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        Log.e("CHECK","Inside MyAdapter onCreateViewHolder");
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.e("CHECK","Inside MyAdapter OnBindViewHolder");
        holder.objectCell.setHeadline(d1[position]);
//        holder.objectCell.setHeadline(d1.get(position));
//        holder.objectCell.setSubheadline(d4.get(position));
////        holder.objectCell.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                ObjectCell p = (ObjectCell) v;
////                Toast toast = Toast.makeText(v.getContext(),
////                        "Item: " + p.getHeadline() + " is clicked.",
////                        Toast.LENGTH_LONG);
////                toast.show();
////            }
////        });
//        holder.objectCell.setFootnote("Ends on : "+ d5.get(position));
//        holder.objectCell.setStatus(d3.get(position),1);
//        holder.objectCell.setStatusColor(Color.rgb(16,126,62),1);
//        if (d3.get(position).matches("Planning")){
//            holder.objectCell.setStatus(R.drawable.ic_warning_black_24dp,0,R.string.sap_logo);
//            holder.objectCell.setStatusColor(Color.rgb(233,115,12),0);
//        }else {
//
//            holder.objectCell.setStatus(R.drawable.ic_check_circle_black_48px,0,R.string.sap_logo);
//            holder.objectCell.setStatusColor(Color.rgb(16,126,62),0);
//        }
//
//
//        holder.objectCell.setDynamicStatusWidth(true);

    }

    @Override
    public int getItemCount() {
        return d1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ObjectCell objectCell;

        public Target target;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            objectCell = itemView.findViewById(R.id.recycle_obj);
            Log.e("CHECK","Inside MyAdapter MyViewHolder");
//            head = itemView.findViewById(R.id.rowname);
//            info = itemView.findViewById(R.id.rowinfo);
        }


    }
}
