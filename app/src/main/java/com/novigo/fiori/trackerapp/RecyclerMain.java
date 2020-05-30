package com.novigo.fiori.trackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sap.cloud.mobile.fiori.formcell.FilterFormCell;
import com.sap.cloud.mobile.fiori.object.ObjectCell;

import java.util.ArrayList;

public class RecyclerMain extends AppCompatActivity  {
    Dialog fo_confirm_dialog;
    Button fo_close_confirm_btn;
    ImageView closePopup;
    Long deleteFO;
    String shippinglocation,shippingdate;
    String[] nameArray = {"6000001","6000002","6000003","6000004","6000005","6000006" };

    String[] infoArray = {
            "Planning",
            "Execution",
            "Execution",
            "Planning",
            "Execution",
            "Planning"
    };
    String[] foarray;
    TextView errorText;
    ArrayList<String> foid = new ArrayList<String>();
    ArrayList<String> truckid = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();
    ArrayList<String> fodesc = new ArrayList<String>();
    ArrayList<String> enddate = new ArrayList<String>();
    Boolean fo_found = false;

//    FilterFormCell filterFormCell;
    Button dummy;
    public ArrayList<DataSetFire> arrayList;
    public FirebaseRecyclerOptions<DataSetFire> options;
    public FirebaseRecyclerAdapter<DataSetFire,FirebaseViewHolder> adapter;

    RecyclerView recyclerView;

    public RecyclerMain() {
        super();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_main);
        errorText = findViewById(R.id.error_text);
//        filterFormCell = findViewById(R.id.filterCell);
//        filterFormCell.setValueOptions(new String[]{"Planning","In Execution","Not Planned"});

        //Dialog
        fo_confirm_dialog = new Dialog(RecyclerMain.this);
        //DialogEnd


        shippinglocation = getIntent().getStringExtra("SHIPPING_LOCATION");
        shippingdate = getIntent().getStringExtra("SHIPPING_DATE");

        recyclerView = findViewById(R.id.recyclemain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        if(shippingdate.matches("X")){
            arrayList = new ArrayList<DataSetFire>();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("frieght_orders");
            ref.keepSynced(true);
            options = new FirebaseRecyclerOptions.Builder<DataSetFire>().setQuery(ref,DataSetFire.class).build();

            adapter = new FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull DataSetFire model) {
                    errorText.setText("");
                    holder.objectCell.setHeadline(String.valueOf(model.getFo_id()));
                    String sloc = model.getSloc_name();
                    String dloc = model.getDloc_name();
                    holder.objectCell.setSubheadline(sloc + " - " + dloc);
                    holder.objectCell.setFootnote("Ends on "+model.getEnding_date());

                    //
                    holder.objectCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ObjectCell p = (ObjectCell) v;
//                            Toast toast = Toast.makeText(v.getContext(),
//                                    "Item: " + p.getHeadline() + " is clicked.",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Intent intent = new Intent(RecyclerMain.this,Maps_show.class);
                            intent.putExtra("FO_ID",p.getHeadline());
                            startActivity(intent);
                        }
                    });
                    holder.objectCell.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            //DialogLogic
                            ObjectCell objectCellfo = (ObjectCell) v;
                            showFOConfirmPopup(objectCellfo.getHeadline());
                            //EndDialogLogic

                            return true;
                        }
                    });
                    if(model.getExecution().matches("Planning")){
                        holder.objectCell.setStatus(R.drawable.ic_warning_black_24dp,0,R.string.application_name);
                        holder.objectCell.setStatusColor(Color.rgb(233,115,12),0);
                        holder.objectCell.setStatus("Planning",  1);
                        holder.objectCell.setDynamicStatusWidth(true);
                        holder.objectCell.setStatusColor(Color.rgb(233,115,12),1);

                    }
                    else if(model.getExecution().matches("Closed")){
                        holder.objectCell.setStatus(R.drawable.ic_check_circle_black_24dp,0,R.string.application_name);
                        holder.objectCell.setStatusColor(Color.rgb(238, 82, 83),0);
                        holder.objectCell.setStatus("Closed",  1);
                        holder.objectCell.setDynamicStatusWidth(true);
                        holder.objectCell.setStatusColor(Color.rgb(238, 82, 83),1);
                    }
                    else{
                        holder.objectCell.setStatus(R.drawable.ic_local_shipping_black_24dp,0,R.string.application_name);
                        holder.objectCell.setStatusColor(Color.rgb(16,126,62),0);
                        holder.objectCell.setStatus("In Execution",  1);
                        holder.objectCell.setStatusColor(Color.rgb(16,126,62),1);
                        holder.objectCell.setDynamicStatusWidth(true);
                    }

                }

                @NonNull
                @Override
                public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new FirebaseViewHolder(LayoutInflater.from(RecyclerMain.this).inflate(R.layout.my_row,parent,false));
                }
            };
            recyclerView.setAdapter(adapter);
        }else {
            arrayList = new ArrayList<DataSetFire>();
            DatabaseReference ref = (DatabaseReference) FirebaseDatabase.getInstance().getReference();
            Query query = ref.child("frieght_orders").orderByChild("starting_date").equalTo(shippingdate);
            ref.keepSynced(true);
            options = new FirebaseRecyclerOptions.Builder<DataSetFire>().setQuery(query,DataSetFire.class).build();

            adapter = new FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull DataSetFire model) {
                    errorText.setText("");
                    holder.objectCell.setHeadline(String.valueOf(model.getFo_id()));
                    String sloc = model.getSloc_name();
                    String dloc = model.getDloc_name();
                    holder.objectCell.setSubheadline(sloc + " - " + dloc);
                    holder.objectCell.setFootnote("Ends on "+model.getEnding_date());
                    //
                    holder.objectCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ObjectCell p = (ObjectCell) v;
//                            Toast toast = Toast.makeText(v.getContext(),
//                                    "Item: " + p.getHeadline() + " is clicked.",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Intent intent = new Intent(RecyclerMain.this,Maps_show.class);
                            intent.putExtra("FO_ID",p.getHeadline());
                            startActivity(intent);
                        }
                    });
                    holder.objectCell.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            //DialogLogic
                            ObjectCell objectCellfo = (ObjectCell) v;
                            showFOConfirmPopup(objectCellfo.getHeadline());
                            //EndDialogLogic

                            return true;
                        }
                    });
                    //
                    if(model.getExecution().matches("Planning")){
                        holder.objectCell.setStatus(R.drawable.ic_warning_black_24dp,0,R.string.application_name);
                        holder.objectCell.setStatusColor(Color.rgb(233,115,12),0);
                        holder.objectCell.setStatus("Planning",  1);
                        holder.objectCell.setDynamicStatusWidth(true);
                        holder.objectCell.setStatusColor(Color.rgb(233,115,12),1);

                    }
                    else if(model.getExecution().matches("Closed")){
                        holder.objectCell.setStatus(R.drawable.ic_check_circle_black_24dp,0,R.string.application_name);
                        holder.objectCell.setStatusColor(Color.rgb(238, 82, 83),0);
                        holder.objectCell.setStatus("Closed",  1);
                        holder.objectCell.setDynamicStatusWidth(true);
                        holder.objectCell.setStatusColor(Color.rgb(238, 82, 83),1);
                    }
                    else{
                        holder.objectCell.setStatus(R.drawable.ic_local_shipping_black_24dp,0,R.string.application_name);
                        holder.objectCell.setStatusColor(Color.rgb(16,126,62),0);
                        holder.objectCell.setStatus("In Execution",  1);
                        holder.objectCell.setStatusColor(Color.rgb(16,126,62),1);
                        holder.objectCell.setDynamicStatusWidth(true);
                    }
                }

                @NonNull
                @Override
                public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new FirebaseViewHolder(LayoutInflater.from(RecyclerMain.this).inflate(R.layout.my_row,parent,false));
                }
            };
            recyclerView.setAdapter(adapter);

        }



    }

    private void showFOConfirmPopup(CharSequence headline) {
        fo_confirm_dialog.setContentView(R.layout.popup_close_fo);
        fo_close_confirm_btn = fo_confirm_dialog.findViewById(R.id.confirm_close);
        closePopup = (ImageView) fo_confirm_dialog.findViewById(R.id.imageView5);
        fo_confirm_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        fo_close_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerMain.this,headline,Toast.LENGTH_SHORT).show();
                DatabaseReference changeStatusRef = FirebaseDatabase.getInstance().getReference().child("frieght_orders").child((String) headline);
                changeStatusRef.child("execution").setValue("Closed");
                fo_confirm_dialog.dismiss();
            }
        });
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fo_confirm_dialog.dismiss();
            }
        });
        fo_confirm_dialog.show();
    }
}
