package org.smartregister.anc.library.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.anc.library.R;
import org.smartregister.anc.library.model.MeModel;
import org.smartregister.anc.library.util.Utils;
import org.smartregister.view.contract.MeContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportActivity3 extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView1;
    RecyclerView.Adapter recyclerViewadapter1;
    ProgressBar loading1;
    private ArrayList<ReportModel1> reportList = new ArrayList<>();
    private ArrayList<ReportModel1> feedbackList = new ArrayList<>();
    private TextView txtToday, txtPeriod, txtReportType, facilityDB, nameDB;
    String monthName, monthNumber, reportType;
    List<String> emptyList = new ArrayList<>();
    private MeContract.Model model;

    public static int firstTrimesterCounter = 0;
    public static int secondTrimesterCounter = 0;
    public static int thirdTrimesterCounter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report3);

        toolbar = findViewById(R.id.toolbarx);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitle("Monthly Reports");
        toolbar.setTitleTextColor(Color.WHITE);

        recyclerView1 = findViewById(R.id.recyclerView1);
        loading1 = findViewById(R.id.loading1);
        txtToday = findViewById(R.id.todaydate);
        txtPeriod = findViewById(R.id.period);
        txtReportType = findViewById(R.id.report_type);
        facilityDB = findViewById(R.id.facility);
        nameDB = findViewById(R.id.phone);

        monthName = getIntent().getExtras().getString("month_name");
        monthNumber = getIntent().getExtras().getString("month_number");
        reportType = getIntent().getExtras().getString("report_type");

        txtPeriod.setText(monthName + " 2023");
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String todaysDate = dateFormatter.format(calendar.getTime());

        txtToday.setText(todaysDate);
        String facility = Utils.locationId;
        String result = facility.replaceAll("_", " ");
        facilityDB.setText(result);

        model = new MeModel();
        String name = model.getName();
        nameDB.setText(name);

        //NavigationMenu.getInstance(this, null, toolbar);

        loadData1();
    }

    public void loadData1(){

        //reportList.addAll(ClientDao.getReport3());

        List<ReportModel1> report3Data = ClientDao.getReport3();
        for (ReportModel1 data : report3Data) {
            if (data.getTrimester() != null) {
                reportList.add(data);
            }
        }

        List<ReportModel1> originData = ClientDao.getOrigin();
        for (ReportModel1 data : originData) {
            if (data.getOrigin() != null) {
                reportList.add(data);
            }
        }

        List<ReportModel1> firstData = ClientDao.getFirstC();
        for (ReportModel1 data : firstData) {
            if (data.getFirstC() != null) {
                reportList.add(data);
            }
        }

        List<ReportModel1> secondData = ClientDao.getSecondC();
        for (ReportModel1 data : secondData) {
            if (data.getSecondC() != null) {
                reportList.add(data);
            }
        }

        List<ReportModel1> thirdData = ClientDao.getThirdC();
        for (ReportModel1 data : thirdData) {
            if (data.getThirdC() != null) {
                reportList.add(data);
            }
        }

        List<ReportModel1> FourthToSeventhData = ClientDao.getFourthToSeventhC();
        for (ReportModel1 data : FourthToSeventhData) {
            if (data.getFourthToSeventhC() != null) {
                reportList.add(data);
            }
        }

        List<ReportModel1> EighthAboveData = ClientDao.getEighthAboveC();
        for (ReportModel1 data : EighthAboveData) {
            if (data.getEighthAboveC() != null) {
                reportList.add(data);
            }
        }
        //feedbackList.addAll(ClientDao.getFeedbackCount());

        //int countlist = feedbackList.size();

        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(eLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        /*txtReportType.setText("TYPE OF CONTRACEPTIVE");
        recyclerViewadapter1 = new ReportAdapter1(monthNumber, reportList, ReportActivity.this);*/


        txtReportType.setText("ANC VISITS FOR : " + monthName);
        recyclerViewadapter1 = new ReportAdapter3(monthNumber, reportList, ReportActivity3.this);
        recyclerView1.setAdapter(recyclerViewadapter1);


        loading1.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reportmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.send) {/*    Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"mtaps@mtaps.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, txtPeriod.getText().toString() + " Report");
                i.putExtra(Intent.EXTRA_TEXT   , "1. Type of Contraceptive");

                try {
                    startActivity(Intent.createChooser(i, "Send Report..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ReportActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

                return true;*/
            //Toasty.info(ReportActivity3.this, "Coming Soon", Toasty.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}