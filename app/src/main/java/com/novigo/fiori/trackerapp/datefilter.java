package com.novigo.fiori.trackerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sap.cloud.mobile.fiori.formcell.DateTimePickerFormCell;
import com.sap.cloud.mobile.fiori.formcell.FilterFormCell;
import com.sap.cloud.mobile.fiori.formcell.FormCell;
import com.sap.cloud.mobile.fiori.formcell.SimplePropertyFormCell;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class datefilter extends AppCompatActivity {
    String strDate;
    Button buttongo;
    FilterFormCell filterFormCell;
    SimplePropertyFormCell simplePropertyFormCell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datefilter);
        simplePropertyFormCell = findViewById(R.id.SimplePropertyFormCell1);

        filterFormCell = findViewById(R.id.filterCell);
        filterFormCell.setValueOptions(new String[]{"Planning","In Execution","Not Planned"});
        DateTimePickerFormCell dateTimePickerFormCell;
        dateTimePickerFormCell = findViewById(R.id.dateCell);
//        dateTimePickerFormCell.setDateTimeFormatter(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.DATE_FIELD,Locale.getDefault()));
        dateTimePickerFormCell.setCellValueChangeListener(new FormCell.CellValueChangeListener<Date>() {
            @Override
            protected void cellChangeHandler(@Nullable Date date) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
                dateTimePickerFormCell.setDateTimeFormatter(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.DATE_FIELD,Locale.getDefault()));
                strDate = dateFormat.format(date);
                Toast toast = Toast.makeText(datefilter.this,strDate,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        dateTimePickerFormCell.getCellValueChangeListener().setCellChangeListenerMode(FormCell.CellChangeListenerMode.ON_CELL_CHANGE);

        buttongo = findViewById(R.id.filtergo);
        buttongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(datefilter.this,RecyclerMain.class);

                Date dats = dateTimePickerFormCell.getValue();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");

                strDate = dateFormat.format(dats);
                intent.putExtra("SHIPPING_DATE",strDate);

                startActivity(intent);
            }
        });




    }
}
