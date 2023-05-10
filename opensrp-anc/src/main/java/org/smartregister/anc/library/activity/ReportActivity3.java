package org.smartregister.anc.library.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.anc.library.R;
import org.smartregister.anc.library.adapter.ReportHashAdapter;
import org.smartregister.anc.library.model.MeModel;
import org.smartregister.anc.library.util.Utils;
import org.smartregister.view.contract.MeContract;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

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
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
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
        ProgressDialog progressDialog = new ProgressDialog(this);
        String todaysDate = dateFormatter.format(calendar.getTime());
        HashMap<String, String> reportAgeData = new HashMap<String, String>();
        txtToday.setText(todaysDate);
        String facility = Utils.locationId;
        String result = facility.replaceAll("_", " ");
        facilityDB.setText(result);

        model = new MeModel();
        String name = model.getName();
        nameDB.setText(name);


      //  loadData1();
        List<ReportModel1> reportList = new ArrayList<>();
        txtReportType.setText("ANC CONTACTS FOR : " + monthName);
       recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
        //recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
        recyclerView1.setAdapter(recyclerViewadapter1);

        Runnable backgroundTask = new Runnable() {
            @Override
            public void run() {
                // Initialize a counter for the callbacks
                AtomicInteger callbackCounter = new AtomicInteger(0);

                // Define the total number of callbacks expected
                final int TOTAL_CALLBACKS = 40;
                ClientDao.getReport3(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> dataList) {
                        // Add data to reportList
                        for (ReportModel1 data : dataList) {
                            if (data.getTrimester() != null) {
                                reportList.add(data);
                            }
                        }
                        // Increment the counter
                        int count = callbackCounter.incrementAndGet();
                        // Notify the adapter that the data has changed
                        // Check if all callbacks have finished
                        if (count == TOTAL_CALLBACKS) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber, reportList, ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (dataList.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    }}
                });
                ClientDao.getOrigin(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getOrigin() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                        if (count == TOTAL_CALLBACKS) {
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber, reportList, ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
                ClientDao.getFirstC(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getFirstC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                        if (count == TOTAL_CALLBACKS) {
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
                ClientDao.getSecondC(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getSecondC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                        if (count == TOTAL_CALLBACKS) {
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
                ClientDao.getThirdC(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getThirdC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                        if (count == TOTAL_CALLBACKS) {
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
                ClientDao.getFourthToSeventhC(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getFourthToSeventhC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                        if (count == TOTAL_CALLBACKS) {
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
                ClientDao.getEighthAboveC(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getEighthAboveC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                        if (count == TOTAL_CALLBACKS) {
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
                ClientDao.getHighRiskContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getHighRiskC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    }
                });
                ClientDao.getSyphPositiveContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getSyphPositiveC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getSyphScreenedContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getSyphScreenedC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getHepBPositiveContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getHepBPositiveC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getHepBScreenedContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getHepbScreenedC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getAnaemiaScreenedContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getAnaemiaScreenedC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();


                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getAnaemiaPositiveContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getAnaemiaPositiveC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getIPTP1Contact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getIPTP1C() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();


                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getIPTP2Contact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getIPTP2C() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();



                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getIPTP3Contact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getIPTP3C() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                        // Check if all callbacks have finished
                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getIPTP4Contact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getIPTP4C() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getProvidedITNContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getProvidedITNC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();


                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getProvidedIronContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getProvidedIronC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getDewormedContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getDewormedC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getStartedOnPrepContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getStartedOnPrepC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();


                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getAlreadyOnPrepContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getStartedOnPrepC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getStartedARTinANCContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getStartedARTC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getAlreadyARTinANCContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getAlreadyOnARTC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getFollowUpContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getFollowUpC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();


                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getDiscordantContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getDiscordantC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getMaleAlreadyPositiveContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getMaleAlreadyPositiveC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getMaleStartedARTinANCContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getMaleStartedARTC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getMalePositiveFirstContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getMalePositiveC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();


                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getViralLoadResultsContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getViralLoadC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getFollowUpContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getFollowUpC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getOnARTContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getOnARTC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getTestedPositiveFirstContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getTestedPositiveC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getTestedHIVFirstContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getTestedHIVC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();


                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
                ClientDao.getScreenedForTBContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getScreenedTBC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getTTCVPlusTwoContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getTTCVPlusTwoC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                });
//                ClientDao.getScreenedForTBContact(new ClientDao.DataCallback() {
//                    @Override
//                    public void onDataRetrieved(List<ReportModel1> originData) {
//                        // Add data to reportList
//                        for (ReportModel1 data : originData) {
//                            if (data.getScreenedTBC() != null) {
//                                reportList.add(data);
//                            }
//                        }
//
//                        int count = callbackCounter.incrementAndGet();
//
//                            // Notify the adapter that the data has changed
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
//                                    recyclerView1.setHasFixedSize(true);
//                                    recyclerView1.setLayoutManager(eLayoutManager);
//                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
//                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
//                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
//                                    recyclerView1.setAdapter(recyclerViewadapter1);
//                                    recyclerViewadapter1.notifyDataSetChanged();
//                                    loading1.setVisibility(View.GONE);
//
//                                    // Show toast only if data has finished loading
//                                    if (originData.size() > 0) {
//                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//                        }
//                });
                ClientDao.getReferredTBContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getReferredTBC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
                ClientDao.getCountContact(new ClientDao.DataCallback() {
                    @Override
                    public void onDataRetrieved(List<ReportModel1> originData) {
                        // Add data to reportList
                        for (ReportModel1 data : originData) {
                            if (data.getContactCountC() != null) {
                                reportList.add(data);
                            }
                        }

                        int count = callbackCounter.incrementAndGet();

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
                                    recyclerView1.setHasFixedSize(true);
                                    recyclerView1.setLayoutManager(eLayoutManager);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    txtReportType.setText("ANC CONTACTS FOR : " + monthName);
                                    recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
                                    recyclerView1.setAdapter(recyclerViewadapter1);
                                    recyclerViewadapter1.notifyDataSetChanged();
                                    loading1.setVisibility(View.GONE);

                                    // Show toast only if data has finished loading
                                    if (originData.size() > 0) {
                                        Toast.makeText(getApplicationContext(), "Data done uploading", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                });
            }

        };



// Execute the background task
        new Thread(backgroundTask).start();


    }
        public void loadData1() {

        //reportList.addAll(ClientDao.getReport3());

//        List<ReportModel1> report3Data = ClientDao.getReport3();
//        for (ReportModel1 data : report3Data) {
//            if (data.getTrimester() != null) {
//                reportList.add(data);
//            }
//        }

//        List<ReportModel1> originData = ClientDao.getOrigin();
//        for (ReportModel1 data : originData) {
//            if (data.getOrigin() != null) {
//                reportList.add(data);
//            }
//        }

//        List<ReportModel1> firstData = ClientDao.getFirstC();
//        for (ReportModel1 data : firstData) {
//            if (data.getFirstC() != null) {
//                reportList.add(data);
//            }
//        }

//        List<ReportModel1> secondData = ClientDao.getSecondC();
//        for (ReportModel1 data : secondData) {
//            if (data.getSecondC() != null) {
//                reportList.add(data);
//            }
//        }

//        List<ReportModel1> thirdData = ClientDao.getThirdC();
//        for (ReportModel1 data : thirdData) {
//            if (data.getThirdC() != null) {
//                reportList.add(data);
//            }
//        }

//        List<ReportModel1> FourthToSeventhData = ClientDao.getFourthToSeventhC();
//        for (ReportModel1 data : FourthToSeventhData) {
//            if (data.getFourthToSeventhC() != null) {
//                reportList.add(data);
//            }
//        }

//        List<ReportModel1> EighthAboveData = ClientDao.getEighthAboveC();
//        for (ReportModel1 data : EighthAboveData) {
//            if (data.getEighthAboveC() != null) {
//                reportList.add(data);
//            }
//        }
//
//        if(ClientDao.getSyphPositiveContact() != null) {
//            List<ReportModel1> SyphPositiveData = ClientDao.getSyphPositiveContact();
//            for (ReportModel1 data : SyphPositiveData) {
//                if (data.getSyphPositiveC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getSyphScreenedContact() != null) {
//            List<ReportModel1> SyphScreenedData = ClientDao.getSyphScreenedContact();
//            for (ReportModel1 data : SyphScreenedData) {
//                if (data.getSyphScreenedC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getHepBPositiveContact() != null) {
//            List<ReportModel1> HepBData = ClientDao.getHepBPositiveContact();
//            for (ReportModel1 data : HepBData) {
//                if (data.getHepbScreenedC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getHepBScreenedContact() != null) {
//            List<ReportModel1> HepBScreenedData = ClientDao.getHepBScreenedContact();
//            for (ReportModel1 data : HepBScreenedData) {
//                if (data.getHepbScreenedC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getAnaemiaScreenedContact() != null) {
//            List<ReportModel1> AnaemiaScreenedData = ClientDao.getAnaemiaScreenedContact();
//            for (ReportModel1 data : AnaemiaScreenedData) {
//                if (data.getAnaemiaScreenedC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }
//
//        if(ClientDao.getAnaemiaPositiveContact() != null) {
//            List<ReportModel1> AnaemiaPositiveData = ClientDao.getAnaemiaPositiveContact();
//            for (ReportModel1 data : AnaemiaPositiveData) {
//                if (data.getAnaemiaPositiveC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getIPTP1Contact() != null) {
//            List<ReportModel1> IPTP1Data = ClientDao.getIPTP1Contact();
//            for (ReportModel1 data : IPTP1Data) {
//                if (data.getIPTP1C() != null) {
//                    reportList.add(data);
//                }
//            }
//        }
//
//        if(ClientDao.getIPTP2Contact() != null) {
//            List<ReportModel1> IPTP2Data = ClientDao.getIPTP2Contact();
//            for (ReportModel1 data : IPTP2Data) {
//                if (data.getIPTP2C() != null) {
//                    reportList.add(data);
//                }
//            }
//        }
//
//        if(ClientDao.getIPTP3Contact() != null) {
//            List<ReportModel1> IPTP3Data = ClientDao.getIPTP3Contact();
//            for (ReportModel1 data : IPTP3Data) {
//                if (data.getIPTP3C() != null) {
//                    reportList.add(data);
//                }
//            }
//        }
//
//        if(ClientDao.getIPTP4Contact() != null) {
//            List<ReportModel1> IPTP4Data = ClientDao.getIPTP4Contact();
//            for (ReportModel1 data : IPTP4Data) {
//                if (data.getIPTP4C() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getProvidedITNContact() != null) {
//            List<ReportModel1> ITNData = ClientDao.getProvidedITNContact();
//            for (ReportModel1 data : ITNData) {
//                if (data.getProvidedITNC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getProvidedIronContact() != null) {
//            List<ReportModel1> IronData = ClientDao.getProvidedIronContact();
//            for (ReportModel1 data : IronData) {
//                if (data.getProvidedIronC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getDewormedContact() != null) {
//            List<ReportModel1> DewormedData = ClientDao.getDewormedContact();
//            for (ReportModel1 data : DewormedData) {
//                if (data.getDewormedC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getStartedOnPrepContact() != null) {
//            List<ReportModel1> StartedOnPrepData = ClientDao.getStartedOnPrepContact();
//            for (ReportModel1 data : StartedOnPrepData) {
//                if (data.getStartedOnPrepC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getAlreadyOnPrepContact() != null) {
//            List<ReportModel1> AlreadyOnPrepData = ClientDao.getAlreadyOnPrepContact();
//            for (ReportModel1 data : AlreadyOnPrepData) {
//                if (data.getAlreadyOnPrepC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getStartedARTinANCContact() != null) {
//            List<ReportModel1> StartedOnARTData = ClientDao.getStartedARTinANCContact();
//            for (ReportModel1 data : StartedOnARTData) {
//                if (data.getStartedARTC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }
//
//        if(ClientDao.getAlreadyARTinANCContact() != null) {
//            List<ReportModel1> AlreadyOnARTData = ClientDao.getAlreadyARTinANCContact();
//            for (ReportModel1 data : AlreadyOnARTData) {
//                if (data.getAlreadyOnARTC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }
//
//        if(ClientDao.getFollowUpContact() != null) {
//            List<ReportModel1> FollowUPData = ClientDao.getFollowUpContact();
//            for (ReportModel1 data : FollowUPData) {
//                if (data.getFollowUpC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getDiscordantContact() != null) {
//            List<ReportModel1> DiscordantData = ClientDao.getDiscordantContact();
//            for (ReportModel1 data : DiscordantData) {
//                if (data.getDiscordantC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }
//
//        if(ClientDao.getMaleStartedARTinANCContact() != null) {
//            List<ReportModel1> MaleStartedARTData = ClientDao.getMaleStartedARTinANCContact();
//            for (ReportModel1 data : MaleStartedARTData) {
//                if (data.getMaleStartedARTC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getMaleAlreadyPositiveContact() != null) {
//            List<ReportModel1> MaleAlreadyPositiveData = ClientDao.getMaleAlreadyPositiveContact();
//            for (ReportModel1 data : MaleAlreadyPositiveData) {
//                if (data.getMaleAlreadyPositiveC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getMalePositiveFirstContact() != null) {
//            List<ReportModel1> MalePositiveData = ClientDao.getMalePositiveFirstContact();
//            for (ReportModel1 data : MalePositiveData) {
//                if (data.getMalePositiveC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getViralLoadResultsContact() != null) {
//            List<ReportModel1> ViralLoadData = ClientDao.getViralLoadResultsContact();
//            for (ReportModel1 data : ViralLoadData) {
//                if (data.getViralLoadC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getSuppressedViralLoadResultsContact() != null) {
//            List<ReportModel1> SuppressedViralLoadData = ClientDao.getSuppressedViralLoadResultsContact();
//            for (ReportModel1 data : SuppressedViralLoadData) {
//                if (data.getSuppressedViralLoadC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getHighRiskContact() != null) {
//            List<ReportModel1> HighRiskData = ClientDao.getHighRiskContact();
//            for (ReportModel1 data : HighRiskData) {
//                if (data.getHighRiskC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getOnARTContact() != null) {
//            List<ReportModel1> OnARTData = ClientDao.getOnARTContact();
//            for (ReportModel1 data : OnARTData) {
//                if (data.getOnARTC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getTestedPositiveFirstContact() != null) {
//            List<ReportModel1> TestedPositiveData = ClientDao.getTestedPositiveFirstContact();
//            for (ReportModel1 data : TestedPositiveData) {
//                if (data.getTestedPositiveC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getAlreadyPositiveFirstContact() != null) {
//            List<ReportModel1> AlreadyPositiveData = ClientDao.getAlreadyPositiveFirstContact();
//            for (ReportModel1 data : AlreadyPositiveData) {
//                if (data.getAlreadyPositiveC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getTestedHIVFirstContact() != null) {
//            List<ReportModel1> TestedHIVData = ClientDao.getTestedHIVFirstContact();
//            for (ReportModel1 data : TestedHIVData) {
//                if (data.getTestedHIVC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getScreenedForTBContact() != null) {
//            List<ReportModel1> ScreenedTBData = ClientDao.getScreenedForTBContact();
//            for (ReportModel1 data : ScreenedTBData) {
//                if (data.getScreenedTBC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getTTCVPlusTwoContact() != null) {
//            List<ReportModel1> TTCVPlusTwoData = ClientDao.getTTCVPlusTwoContact();
//            for (ReportModel1 data : TTCVPlusTwoData) {
//                if (data.getTTCVPlusTwoC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getReferredTBContact() != null) {
//            List<ReportModel1> ReferredTBData = ClientDao.getReferredTBContact();
//            for (ReportModel1 data : ReferredTBData) {
//                if (data.getReferredTBC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

//        if(ClientDao.getCountContact() != null) {
//            List<ReportModel1> CountContactData = ClientDao.getCountContact();
//            for (ReportModel1 data : CountContactData) {
//                if (data.getContactCountC() != null) {
//                    reportList.add(data);
//                }
//            }
//        }

        //feedbackList.addAll(ClientDao.getFeedbackCount());

        //int countlist = feedbackList.size();

        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(ReportActivity3.this);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(eLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        /*txtReportType.setText("TYPE OF CONTRACEPTIVE");
        recyclerViewadapter1 = new ReportAdapter1(monthNumber, reportList, ReportActivity.this);*/


        txtReportType.setText("ANC CONTACTS FOR : " + monthName);
     //   recyclerViewadapter1 = new ReportAdapter3(monthNumber,reportList,ReportActivity3.this);
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
        if (item.getItemId() == R.id.send) {
            // Start a new AsyncTask to perform network operations
            new SendDataTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    private class SendDataTask extends AsyncTask<Void, Void, String> {

       // @Override
        protected String doInBackground(Void... voids) {
            JSONArray dataValues = new JSONArray();
            for (ReportModel1 data : reportList) {
                JSONObject dataValue = new JSONObject();
                try {
                    dataValue.put("dataElement", data.getGeneralBaseEntityID());
                    dataValue.put("period", monthName);
                    dataValue.put("orgUnit", data.getGeneralKey());
                    dataValue.put("value", data.getGeneralValue());
                    dataValues.put(dataValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String data = "{\"dataValues\": " + dataValues.toString() + "}";

            String url = "https://102.23.122.128:8080/dhis2/api/dataValueSets"; // The endpoint for the dataValueSets API
            String username = "dhis2_user";
            String password = "=8hn7$&!4yf9874rR";
            String encoding = android.util.Base64.encodeToString((username + ":" + password).getBytes(), android.util.Base64.DEFAULT);

            URL obj = null;
            HttpsURLConnection con = null;
            try {
                obj = new URL(url);
                con = (HttpsURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set the request method and headers
            try {
                con.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            con.setRequestProperty("Authorization", "Basic " + encoding);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            // Send the data
            con.setDoOutput(true);
            DataOutputStream wr = null;
            try {
                wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Get the response
            StringBuffer response = new StringBuffer();
            try {
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Return the response as a string
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the result here
            System.out.println(result);
        }
    }

}
