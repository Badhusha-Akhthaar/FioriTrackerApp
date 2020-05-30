package com.novigo.fiori.trackerapp;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sap.cloud.mobile.fiori.object.ObjectCell;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {
    public ObjectCell objectCell;
    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        objectCell= itemView.findViewById(R.id.recycle_obj);
    }
}
