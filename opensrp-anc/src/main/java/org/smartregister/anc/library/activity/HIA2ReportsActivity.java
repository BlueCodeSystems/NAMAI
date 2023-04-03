package org.smartregister.anc.library.activity;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.anc.library.R;


import java.util.ArrayList;

public class HIA2ReportsActivity extends AppCompatActivity {

    ArrayList<MonthModel> months = new ArrayList<>();
    private RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewadapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        toolbar = findViewById(R.id.toolbarx);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitle("Reports");
        toolbar.setTitleTextColor(Color.WHITE);
        //NavigationMenu.getInstance(this, null, toolbar);

        recyclerView = findViewById(R.id.recyclerView);

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

        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(HIA2ReportsActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(eLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewadapter = new ReportListAdapter(months, HIA2ReportsActivity.this);
        recyclerView.setAdapter(recyclerViewadapter);

    }
}