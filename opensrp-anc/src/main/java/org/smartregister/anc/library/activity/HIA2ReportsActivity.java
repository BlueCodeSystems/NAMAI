package org.smartregister.anc.library.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.anc.library.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HIA2ReportsActivity extends AppCompatActivity {

    ArrayList<MonthModel> months = new ArrayList<>();
    private RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewadapter;
    private Toolbar toolbar;
    private TextView dateGenerated;
    public static int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        toolbar = findViewById(R.id.toolbarx);
        dateGenerated = findViewById(R.id.dateGenerated);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitle("Reports");
        toolbar.setTitleTextColor(Color.WHITE);
        String lastGeneratedOn = retrieveTimestampFromSharedPreferences(getApplicationContext());
        dateGenerated.setText("Last Generated on : "+ lastGeneratedOn);


        Button regenerateButton = findViewById(R.id.regenerate);
        regenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Reloading querries, Please Wait...", Toast.LENGTH_SHORT).show();
                BaseHomeRegisterActivity.reloadQueries();
            }
        });

        //NavigationMenu.getInstance(this, null, toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        month = cal.get(Calendar.MONTH) + 1;
        if(month == 1)
        {
            months.add(new MonthModel("January", 1));
        }else if(month == 2){
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
        }else if(month == 3) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
        }else if(month == 4) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
        }else if(month == 5) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
            months.add(new MonthModel("May", 5));
        }else if(month == 6) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
            months.add(new MonthModel("May", 5));
            months.add(new MonthModel("June", 6));
        }else if(month == 7) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
            months.add(new MonthModel("May", 5));
            months.add(new MonthModel("June", 6));
            months.add(new MonthModel("July", 7));
        }else if(month == 8) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
            months.add(new MonthModel("May", 5));
            months.add(new MonthModel("June", 6));
            months.add(new MonthModel("July", 7));
            months.add(new MonthModel("August", 8));
        }else if(month == 9) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
            months.add(new MonthModel("May", 5));
            months.add(new MonthModel("June", 6));
            months.add(new MonthModel("July", 7));
            months.add(new MonthModel("August", 8));
            months.add(new MonthModel("September", 9));
        } else if(month == 10) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
            months.add(new MonthModel("May", 5));
            months.add(new MonthModel("June", 6));
            months.add(new MonthModel("July", 7));
            months.add(new MonthModel("August", 8));
            months.add(new MonthModel("September", 9));
            months.add(new MonthModel("October", 10));
        } else if(month == 11) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
            months.add(new MonthModel("May", 5));
            months.add(new MonthModel("June", 6));
            months.add(new MonthModel("July", 7));
            months.add(new MonthModel("August", 8));
            months.add(new MonthModel("September", 9));
            months.add(new MonthModel("October", 10));
            months.add(new MonthModel("November", 11));
        } else if(month == 12) {
            months.add(new MonthModel("January", 1));
            months.add(new MonthModel("February", 2));
            months.add(new MonthModel("March", 3));
            months.add(new MonthModel("April", 4));
            months.add(new MonthModel("May", 5));
            months.add(new MonthModel("June", 6));
            months.add(new MonthModel("July", 7));
            months.add(new MonthModel("August", 8));
            months.add(new MonthModel("September", 9));
            months.add(new MonthModel("October", 10));
            months.add(new MonthModel("November", 11));
            months.add(new MonthModel("December", 12));
        }

/*
        months.add(new MonthModel("January", 1));
        months.add(new MonthModel("February", 2));
        months.add(new MonthModel("March", 3));
        months.add(new MonthModel("April", 4));
        months.add(new MonthModel("May", 5));
        months.add(new MonthModel("June", 6));
        months.add(new MonthModel("July", 7));
        months.add(new MonthModel("August", 8));
        months.add(new MonthModel("September", 9));
        months.add(new MonthModel("October", 10));
        months.add(new MonthModel("November", 11));
        months.add(new MonthModel("December", 12));*/

        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(HIA2ReportsActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(eLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewadapter = new ReportListAdapter(months, HIA2ReportsActivity.this);
        recyclerView.setAdapter(recyclerViewadapter);

    }
    // Retrieve the timestamp from SharedPreferences

    private String retrieveTimestampFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("timestamp", "");
    }
}