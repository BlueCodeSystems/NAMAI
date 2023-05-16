package org.smartregister.anc.library.activity;

import static org.smartregister.anc.library.activity.ReportListAdapter.selectedMonth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.anc.library.R;
import org.smartregister.anc.library.util.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import android.util.LruCache;

import io.fabric.sdk.android.services.concurrency.AsyncTask;


public class ReportAdapter3 extends RecyclerView.Adapter< ReportAdapter3.ViewHolder> {
    Context context;

    List<ReportModel1> reportitems;
    LruCache<Integer, ReportModel1> cache;
//    List<ReportModel1> feedbackItems;
    String monthNumber;
    String f1first = null;
    String f2first = null;
    String f3first = null;
    String f4first = null;
    String f1second = null;
    String f2second = null;
    String f3second = null;
    String f4second = null;
    String f1third = null;
    String f2third = null;
    String f3third = null;
    String f4third = null;
    String totalfemalesfirst = null;
    String totalfemalessecond = null;
    String totalfemalesthird = null;
    String origin = null;//null last check
    String firstc = null;//null last check
    String secondc = null;//null last check
    String thirdc = null;//null last check
    String fourthToSeventhC = null;
    String eighthAboveC = null;
    String highRiskC = null;
    String syphScreened = null;
    String syphPositive = null;
    String HepBScreened = null;
    String HepBPositive = null;
    String AnaemiaScreened = null;
    String AnaemiaPositive = null;
    String IPTP1 = null;
    String IPTP2 = null;
    String IPTP3 = null;
    String IPTP4 = null;
    String providedITN = null;
    String providedIron = null;
    String dewormed = null;
    String startedOnPrep = null;
    String alreadyOnPrep = null;//null last check
    String startedOnART = null;
    String alreadyOnART = null;
    String followUp = null;
    String discordant = null;
    String maleStartedART = null;
    String maleAlreadyPositive = null;
    String malePositive = null;
    String maleTested = null;//null last check
    String viralLoad = null;
    String suppressedVL = null;//null last check
    String onART = null;
    String testedPositive = null;
    String alreadyPositive = null;//null last check
    String testedHIV = null;
    String ttcvPlusTwo = null;
    String referredTB = null;
    String contactCount = null;
    String screenedTB = null;
    String[] monthData = new String[53];










    public ReportAdapter3(String monthNumber, List<ReportModel1> reportitems, Context context){

        super();

  //      this.feedbackItems = feedbackItems;
        this.reportitems = reportitems;
        this.monthNumber = monthNumber;
        this.context = context;
        int localMonth = selectedMonth;
        loadMonthDataFromFile();
    }

    @Override
    public ReportAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_report_1, parent, false);

        ReportAdapter3.ViewHolder viewHolder = new ReportAdapter3.ViewHolder(v);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ReportAdapter3.ViewHolder holder, final int position) {


        ReportModel1 data = reportitems.get(position);
        //int feedback = feedbackItems.size();

        if (monthData[0] == null) {
                monthData[0] = String.valueOf(selectedMonth);
        }

        holder.setIsRecyclable(false);

        holder.txtProductName.setText(data.getTrimester());

            holder.m1.setBackgroundResource(R.drawable.na_round_button);
            holder.m2.setBackgroundResource(R.drawable.na_round_button);
            holder.m3.setBackgroundResource(R.drawable.na_round_button);
            holder.m4.setBackgroundResource(R.drawable.na_round_button);
            holder.m5.setBackgroundResource(R.drawable.na_round_button);
            holder.txtTotalMaleSeen.setBackgroundResource(R.drawable.na_round_button);


        holder.txtTotalMaleServices.setBackgroundResource(R.drawable.na_round_button);
        holder.txtTotalFemaleServices.setBackgroundResource(R.drawable.na_round_button);

        //List<ClientreportModel> totalMaleSeen = ClientDao.getRefVisitedClientsMale(data.getQuerry_drug());
        //List<ClientreportModel> totalFemaleSeen = ClientDao.getRefVisitedClientsFemale(data.getQuerry_drug());

        if (data.getTrimester() != null && data.getTrimester().contains("IRH1-005 / IRH1-010 / IRH1-015 / IRH1-020 / IRH1-025 : WOMEN WHO CAME FOR ANC DURING THE FIRST TRIMESTER")) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("First antenatal contacts");
            if(f4first == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            }
            new AsyncTask<Void, Void, List<String>>() {
                @Override
                protected List<String> doInBackground(Void... voids) {
                    List<String> results = new ArrayList<>();
                    if(monthData[1] == null) {
                        if (f1first != null) {
                            monthData[1] = f1first;
                        }
                    }else{
                        if(f1first == null) {
                            f1first = monthData[1];
                        }
                    }
                    if (f1first == null) {
                        f1first = ClientDao.getFirstContact("gest_age_openmrs", "8", "12");
                    }
                    results.add(f1first);

                    if(monthData[2] == null) {
                        if (f2first != null) {
                            monthData[2] = f2first;
                        }
                    }else {
                        if(f2first == null) {
                            f2first = monthData[2];
                        }
                    }
                    if (f2first == null) {
                        f2first = ClientDao.getFirstContactAbove15("gest_age_openmrs", "8", "12");
                    }
                    results.add(f2first);

                    if(monthData[3] == null) {
                        if (f3first != null) {
                            monthData[3] = f3first;
                        }
                    }else {
                        if(f3first == null) {
                            f3first = monthData[3];
                        }
                    }
                    if (f3first == null) {
                        f3first = ClientDao.getFirstContactAbove20("gest_age_openmrs", "8", "12");
                    }
                    results.add(f3first);

                    if(monthData[4] == null) {
                        if (f4first != null) {
                            monthData[4] = f4first;
                        }
                    }else {
                        if(f4first == null) {
                            f4first = monthData[4];
                        }
                    }
                    if (f4first == null) {
                        f4first = ClientDao.getFirstContactAbove25("gest_age_openmrs", "8", "12");
                    }
                    results.add(f4first);

                    return results;
                }

                @Override
                protected void onPostExecute(List<String> results) {
                    holder.f1.setText(results.get(0));
                    holder.f2.setText(results.get(1));
                    holder.f3.setText(results.get(2));
                    holder.f4.setText(results.get(3));

                    int womenSeen = Integer.parseInt(results.get(0)) + Integer.parseInt(results.get(1)) + Integer.parseInt(results.get(2)) + Integer.parseInt(results.get(3));

                    if (totalfemalesfirst == null) {
                        totalfemalesfirst = String.valueOf(womenSeen);
                    }
                    holder.txtTotalFemaleSeen.setText(totalfemalesfirst);
                }
            }.execute();
        }
        else if(data.getTrimester() != null && data.getTrimester().contains("IRH1-030 / IRH1-035 / IRH1-040 / IRH1-045 / IRH1-050 : WOMEN WHO CAME FOR ANC DURING THE SECOND TRIMESTER")) {
            if(f4second == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            }
            new AsyncTask<Void, Void, List<String>>() {
                @Override
                protected List<String> doInBackground(Void... voids) {
                    List<String> results = new ArrayList<>();
                    if(monthData[5] == null) {
                        if (f1second != null) {
                            monthData[5] = f1second;
                        }
                    }else {
                        if(f1second == null) {
                            f1second = monthData[5];
                        }
                    }
                    if (f1second == null) {
                        f1second = ClientDao.getFirstContact("gest_age_openmrs", "13", "26");
                    }
                    results.add(f1second);

                    if(monthData[6] == null) {
                        if (f2second != null) {
                            monthData[6] = f2second;
                        }
                    }else {
                        if(f2second == null) {
                            f2second = monthData[6];
                        }
                    }
                    if (f2second == null) {
                        f2second = ClientDao.getFirstContactAbove15("gest_age_openmrs", "13", "26");
                    }
                    results.add(f2second);

                    if(monthData[7] == null) {
                        if (f3second != null) {
                            monthData[7] = f3second;
                        }
                    }else {
                        if(f3second == null) {
                            f3second = monthData[7];
                        }
                    }
                    if (f3second == null) {
                        f3second = ClientDao.getFirstContactAbove20("gest_age_openmrs", "13", "26");
                    }
                    results.add(f3second);


                    if(monthData[8] == null) {
                        if (f4second != null) {
                            monthData[8] = f4second;
                        }
                    }else {
                        if(f4second == null) {
                            f4second = monthData[8];
                        }
                    }
                    if (f4second == null) {
                        f4second = ClientDao.getFirstContactAbove25("gest_age_openmrs", "13", "26");
                    }
                    results.add(f4second);


                    return results;
                }

                @Override
                protected void onPostExecute(List<String> results) {
                    holder.f1.setText(results.get(0));
                    holder.f2.setText(results.get(1));
                    holder.f3.setText(results.get(2));
                    holder.f4.setText(results.get(3));

                    int womenSeen = Integer.parseInt(results.get(0)) + Integer.parseInt(results.get(1)) + Integer.parseInt(results.get(2)) + Integer.parseInt(results.get(3));

                    if (totalfemalessecond == null) {
                        totalfemalessecond = String.valueOf(womenSeen);
                    }
                    holder.txtTotalFemaleSeen.setText(totalfemalessecond);
                }
            }.execute();
        }
        else if(data.getTrimester() != null && data.getTrimester().contains("IRH1-055 / IRH1-060 / IRH1-065 / IRH1-070 / IRH1-075 : WOMEN WHO CAME FOR ANC DURING THE THIRD TRIMESTER")) {
            if(f4third == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            }
            new AsyncTask<Void, Void, List<String>>() {
                @Override
                protected List<String> doInBackground(Void... voids) {
                    List<String> results = new ArrayList<>();
                    if(monthData[9] == null) {
                        if (f1third != null) {
                            monthData[9] = f1third;
                        }
                    }else {
                        if(f1third == null) {
                            f1third = monthData[9];
                        }
                    }
                    if (f1third == null) {
                        f1third = ClientDao.getFirstContact("gest_age_openmrs", "27", "40");
                    }
                    results.add(f1third);


                    if(monthData[10] == null) {
                        if (f2third != null) {
                            monthData[10] = f2third;
                        }
                    }else {
                        if(f2third == null) {
                            f2third = monthData[10];
                        }
                    }
                    if (f2third == null) {
                        f2third = ClientDao.getFirstContactAbove15("gest_age_openmrs", "27", "40");
                    }
                    results.add(f2third);


                    if(monthData[11] == null) {
                        if (f3third != null) {
                            monthData[11] = f3third;
                        }
                    }else {
                        if(f3third == null) {
                            f3third = monthData[11];
                        }
                    }
                    if (f3third == null) {
                        f3third = ClientDao.getFirstContactAbove20("gest_age_openmrs", "27", "40");
                    }
                    results.add(f3third);


                    if(monthData[12] == null) {
                        if (f4third != null) {
                            monthData[12] = f4third;
                        }
                    }else {
                        if(f4third == null) {
                            f4third = monthData[12];
                        }
                    }
                    if (f4third == null) {
                        f4third = ClientDao.getFirstContactAbove25("gest_age_openmrs", "27", "40");
                    }
                    results.add(f4third);


                    return results;
                }

                @Override
                protected void onPostExecute(List<String> results) {
                    holder.f1.setText(results.get(0));
                    holder.f2.setText(results.get(1));
                    holder.f3.setText(results.get(2));
                    holder.f4.setText(results.get(3));

                    int womenSeen = Integer.parseInt(results.get(0)) + Integer.parseInt(results.get(1)) + Integer.parseInt(results.get(2)) + Integer.parseInt(results.get(3));

                    if (totalfemalesthird == null) {
                        totalfemalesthird = String.valueOf(womenSeen);
                    }
                    holder.txtTotalFemaleSeen.setText(totalfemalesthird);
                }
            }.execute();
        }

        else if (data.getOrigin() != null) {
            holder.txtProductName.setText("IRH1-100 : Women from outside the catchment area");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if(monthData[13] == null) {
                if (origin != null) {
                    monthData[13] = origin;
                }
            }else{
                if(origin == null) {
                    origin = monthData[13];
                }
            }

            if(origin == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            }else{
                monthData[13] = origin;
            }
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String originStr = "";
                    if (origin == null) {
                        originStr = String.valueOf(ClientDao.getAllOutside());
                        origin = originStr;
                    } else {
                        originStr = origin;
                    }
                    return originStr;
                }

                @Override
                protected void onPostExecute(String origin) {
                    if (origin != null) {
                        holder.txtTotalFemaleSeen.setText(origin);
                    }
                }
            }.execute();
        }

        else if(data.getFirstC() != null) {
            holder.txtProductName.setText("IRH1-100 : Total 1st ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);
            if(monthData[14] == null) {
                if (firstc != null) {
                    monthData[14] = firstc;
                }
            }else{
                if(firstc == null) {
                    firstc = monthData[14];
                }
            }

            if(firstc == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            }else{
                monthData[14] = firstc;
            }
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String firstcStr = "";
                    if(firstc == null) {
                        firstcStr = String.valueOf(ClientDao.getAllFirstContact());
                        firstc = firstcStr;
                    } else {
                        firstcStr = firstc;
                    }
                    return firstcStr;
                }

                @Override
                protected void onPostExecute(String firstc) {

                    if(firstc != null) {
                        holder.txtTotalFemaleSeen.setText(firstc);
                    }
                }
            }.execute();
        }

        else if(data.getSecondC() != null) {
            holder.txtProductName.setText("IRH1-115 : 2nd ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);
            if (monthData[15] == null) {
                if (secondc != null) {
                    monthData[15] = secondc;
                }
            }else{
                if(secondc == null) {
                    secondc = monthData[15];
                }
            }

            if(secondc == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            }else{
                monthData[15] = secondc;
            }
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String secondcStr = "";
                    if(secondc == null) {
                        secondcStr = String.valueOf(ClientDao.getAllSecondContact());
                        secondc = secondcStr;
                    } else {
                        secondcStr = secondc;
                    }
                    return secondcStr;
                }

                @Override
                protected void onPostExecute(String secondc) {

                    if(secondc != null) {
                        holder.txtTotalFemaleSeen.setText(secondc);
                    }
                }
            }.execute();
        }

        else if(data.getThirdC() != null) {
            holder.txtProductName.setText("IRH1-120 : 3rd ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);
            if (monthData[16] == null) {
                if (thirdc != null) {
                    monthData[16] = thirdc;
                }
            }else{
                if(thirdc == null) {
                    thirdc = monthData[16];
                }
            }

            if(thirdc == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            }
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String thirdcStr = "";
                    if(thirdc == null) {
                        thirdcStr = String.valueOf(ClientDao.getAllThirdContact());
                        thirdc = thirdcStr;
                    } else {
                        thirdcStr = thirdc;
                    }
                    return thirdcStr;
                }

                @Override
                protected void onPostExecute(String thirdc) {

                    if(thirdc != null) {
                        holder.txtTotalFemaleSeen.setText(thirdc);
                    }
                }
            }.execute();
        }

        else if(data.getFourthToSeventhC() != null){
            holder.txtProductName.setText("IRH1-125 : 4th to 7th ANC contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[17] == null) {
                if (fourthToSeventhC != null) {
                    monthData[17] = fourthToSeventhC;
                }
            }else{
                if(fourthToSeventhC == null) {
                    fourthToSeventhC = monthData[17];
                }
            }

            if(fourthToSeventhC == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllFourthToSeventhContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        fourthToSeventhC = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(fourthToSeventhC);
            }
        }


        else if(data.getEighthAboveC() != null){
            holder.txtProductName.setText("IRH1-130 : 8th+ ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[18] == null) {
                if (eighthAboveC != null) {
                    monthData[18] = eighthAboveC;
                }
            }else{
                if(eighthAboveC == null) {
                    eighthAboveC = monthData[18];
                }
            }

            if(eighthAboveC == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllEighthAboveContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        eighthAboveC = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(eighthAboveC);
            }
        }


        else if(data.getHighRiskC() != null){
            holder.txtProductName.setText("IRH1-110 : High-risk Pregnancies");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[19] == null) {
                if (highRiskC != null) {
                    monthData[19] = highRiskC;
                }
            }else{
                if(highRiskC == null) {
                    highRiskC = monthData[19];
                }
            }

            if(highRiskC == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllHighRiskContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        highRiskC = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(highRiskC);
            }

        }

        else if(data.getSyphScreenedC() != null){
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Antenatal Screening");
            holder.txtProductName.setText("IRH1-155 : Initial syphilis screening");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[20] == null) {
                if (syphScreened != null) {
                    monthData[20] = syphScreened;
                }
            }else{
                if(syphScreened == null) {
                    syphScreened = monthData[20];
                }
            }

            if(syphScreened == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllSyphScreenedContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        syphScreened = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(syphScreened);
            }
        }

        else if(data.getSyphPositiveC() != null){
            holder.txtProductName.setText("IRH1-160 : Reactive to Initial syphilis Test");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[21] == null) {
                if (syphPositive != null) {
                    monthData[21] = syphPositive;
                }
            }else{
                if(syphPositive == null) {
                    syphPositive = monthData[21];
                }
            }

            if(syphPositive == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllSyphPositiveContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        syphPositive = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(syphPositive);
            }
        }

        else if(data.getHepbScreenedC() != null){
            holder.txtProductName.setText("IRH1-175 : Screened for Hepatitis B");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[22] == null) {
                if (HepBScreened != null) {
                    monthData[22] = HepBScreened;
                }
            }else{
                if(HepBScreened == null) {
                    HepBScreened = monthData[22];
                }
            }

            if(HepBScreened == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllHepBScreenedContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        HepBScreened = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(HepBScreened);
            }
        }

        else if(data.getHepBPositiveC() != null){
            holder.txtProductName.setText("IRH1-180 : Positive for Hepatitis B");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[23] == null) {
                if (HepBPositive != null) {
                    monthData[23] = HepBPositive;
                }
            }else{
                if(HepBPositive == null) {
                    HepBPositive = monthData[23];
                }
            }

            if(HepBPositive == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllHepBPositiveContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        HepBPositive = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(HepBPositive);
            }
        }

        else if(data.getAnaemiaScreenedC() != null){
            holder.txtProductName.setText("IRH1-145 : Initial anaemia screening");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[24] == null) {
                if (AnaemiaScreened != null) {
                    monthData[24] = AnaemiaScreened;
                }
            }else{
                if(AnaemiaScreened == null) {
                    AnaemiaScreened = monthData[24];
                }
            }

            if(AnaemiaScreened == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllAnaemiaScreenedContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        AnaemiaScreened = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(AnaemiaScreened);
            }
        }

        else if(data.getAnaemiaPositiveC() != null){
            holder.txtProductName.setText("IRH1-150 : Diagnosed with Anaemia");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[25] == null) {
                if (AnaemiaPositive != null) {
                    monthData[25] = AnaemiaPositive;
                }
            }else{
                if(AnaemiaPositive == null) {
                    AnaemiaPositive = monthData[25];
                }
            }

            if(AnaemiaPositive == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllAnaemiaPositiveContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        AnaemiaPositive = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(AnaemiaPositive);
            }
        }

        else if(data.getIPTP1C() != null){
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Non-HIV Prophylaxis and Services provided");
            holder.txtProductName.setText("IRH1-190 : IPTp 1");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[26] == null) {
                if (IPTP1 != null) {
                    monthData[26] = IPTP1;
                }
            }else{
                if(IPTP1 == null) {
                    IPTP1 = monthData[26];
                }
            }

            if(IPTP1 == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllIPTP1Contact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        IPTP1 = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(IPTP1);
            }
        }

        else if(data.getIPTP2C() != null){
            holder.txtProductName.setText("IRH1-195 : IPTp 2");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[27] == null) {
                if (IPTP2 != null) {
                    monthData[27] = IPTP2;
                }
            }else{
                if(IPTP2 == null) {
                    IPTP2 = monthData[27];
                }
            }

            if(IPTP2 == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllIPTP2Contact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        IPTP2 = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(IPTP2);
            }
        }

        else if(data.getIPTP3C() != null){
            holder.txtProductName.setText("IRH1-200 : IPTp 3");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[28] == null) {
                if (IPTP3 != null) {
                    monthData[28] = IPTP3;
                }
            }else{
                if(IPTP3 == null) {
                    IPTP3 = monthData[28];
                }
            }

            if(IPTP3 == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllIPTP3Contact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        IPTP3 = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(IPTP3);
            }
        }

        else if(data.getIPTP4C() != null){
            holder.txtProductName.setText("IRH1-205 : IPTp 4");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[29] == null) {
                if (IPTP4 != null) {
                    monthData[29] = IPTP4;
                }
            }else{
                if(IPTP4 == null) {
                    IPTP4 = monthData[29];
                }
            }

            if(IPTP4 == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllIPTP4Contact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        IPTP4 = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(IPTP4);
            }
        }

        else if(data.getProvidedITNC() != null){
            holder.txtProductName.setText("IRH1-210 : Provided with ITN");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[30] == null) {
                if (providedITN != null) {
                    monthData[30] = providedITN;
                }
            }else{
                if(providedITN == null) {
                    providedITN = monthData[30];
                }
            }

            if(providedITN == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllProvidedITNContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        providedITN = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(providedITN);
            }
        }

        else if(data.getProvidedIronC() != null){
            holder.txtProductName.setText("IRH1-215 : Provided with Iron");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[31] == null) {
                if (providedIron != null) {
                    monthData[31] = providedIron;
                }
            }else{
                if(providedIron == null) {
                    providedIron = monthData[31];
                }
            }

            if(providedIron == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllProvidedIronContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        providedIron = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(providedIron);
            }
        }

        else if(data.getDewormedC() != null){
            holder.txtProductName.setText("IRH1-230 : Dewormed");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[32] == null) {
                if (dewormed != null) {
                    monthData[32] = dewormed;
                }
            }else{
                if(dewormed == null) {
                    dewormed = monthData[32];
                }
            }

            if(dewormed == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllDewormedContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        dewormed = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(dewormed);
            }
        }

        else if(data.getStartedOnPrepC() != null){
            holder.txtProductName.setText("HIV2-091 : Started on PrEP in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[33] == null) {
                if (startedOnPrep != null) {
                    monthData[33] = startedOnPrep;
                }
            }else{
                if(startedOnPrep == null) {
                    startedOnPrep = monthData[33];
                }
            }

            if(startedOnPrep == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllStartedOnPrepContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        startedOnPrep = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(startedOnPrep);
            }
        }

        else if(data.getAlreadyOnPrepC() != null){
            holder.txtProductName.setText("HIV2-092 : Currently on PrEP in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[34] == null) {
                if (alreadyOnPrep != null) {
                    monthData[34] = alreadyOnPrep;
                }
            }else{
                if(alreadyOnPrep == null) {
                    alreadyOnPrep = monthData[34];
                }
            }

            if(alreadyOnPrep == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        alreadyOnPrep = String.valueOf(ClientDao.getAllAlreadyOnPrepContact());
                        monthData[34] = alreadyOnPrep;
                        return alreadyOnPrep;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(alreadyOnPrep);
            }
        }

        else if(data.getStartedARTC() != null){
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Maternal ART");
            holder.txtProductName.setText("HIV2-065 : Started on ART in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[35] == null) {
                if (startedOnART != null) {
                    monthData[35] = startedOnART;
                }
            }else{
                if(startedOnART == null) {
                    startedOnART = monthData[35];
                }
            }

            if(startedOnART == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllStartedARTinANCContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        startedOnART = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(startedOnART);
            }
        }

        else if(data.getAlreadyOnARTC() != null){
            holder.txtProductName.setText("HIV2-060 : Already on ART at 1st ANC visit");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[36] == null) {
                if (alreadyOnART != null) {
                    monthData[36] = alreadyOnART;
                }
            }else{
                if(alreadyOnART == null) {
                    alreadyOnART = monthData[36];
                }
            }

            if(alreadyOnART == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllAlreadyARTinANCContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        alreadyOnART = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(alreadyOnART);
            }
        }

        else if(data.getFollowUpC() != null){
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Follow-up Antenantal Contacts");
            holder.txtProductName.setText("IRH1-135 : Total follow up contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[37] == null) {
                if (followUp != null) {
                    monthData[37] = followUp;
                }
            }else{
                if(followUp == null) {
                    followUp = monthData[37];
                }
            }

            if(followUp == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllFollowUpContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        followUp = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(followUp);
            }
        }

        else if(data.getDiscordantC() != null){
            holder.txtProductName.setText("HIV2-130 : Discordant Test Results");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[38] == null) {
                if (discordant != null) {
                    monthData[38] = discordant;
                }
            }else{
                if(discordant == null) {
                    discordant = monthData[38];
                }
            }

            if(discordant == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllDiscordantContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        discordant = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(discordant);
            }
        }

        else if(data.getMaleStartedARTC() != null){
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Male Partner Involvement");
            holder.txtProductName.setText("HIV2-126 : Male Partner started on ART in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[39] == null) {
                if (maleStartedART != null) {
                    monthData[39] = maleStartedART;
                }
            }else{
                if(maleStartedART == null) {
                    maleStartedART = monthData[39];
                }
            }

            if(maleStartedART == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllMaleStartedARTinANCContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        maleStartedART = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(maleStartedART);
            }
        }

        else if(data.getMaleAlreadyPositiveC() != null){
            holder.txtProductName.setText("HIV2-115 : With known status at 1st visit to MCH");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[40] == null) {
                if (maleAlreadyPositive != null) {
                    monthData[40] = maleAlreadyPositive;
                }
            }else{
                if(maleAlreadyPositive == null) {
                    maleAlreadyPositive = monthData[40];
                }
            }

            if(maleAlreadyPositive == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllMaleAlreadyPositiveContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        maleAlreadyPositive = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(maleAlreadyPositive);
            }
        }

        else if(data.getMalePositiveC() != null){
            holder.txtProductName.setText("HIV2-125 : Positive results (MCH only)");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[41] == null) {
                if (malePositive != null) {
                    monthData[41] = malePositive;
                }
            }else{
                if(malePositive == null) {
                    malePositive = monthData[41];
                }
            }

            if(malePositive == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllMalePositiveFirstContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        malePositive = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(malePositive);
            }
        }

        else if(data.getMaleTestedC() != null){
            holder.txtProductName.setText("HIV2-120 : Tested in MCH");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[42] == null) {
                if (maleTested != null) {
                    monthData[42] = maleTested;
                }
            }else{
                if(maleTested == null) {
                    maleTested = monthData[42];
                }
            }

            if(maleTested == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        maleTested = String.valueOf(ClientDao.getAllMaleTestFirstContact());
                        monthData[42] = maleTested;
                        return maleTested;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(maleTested);
            }
        }

        else if(data.getViralLoadC() != null){
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Monitoring");
            holder.txtProductName.setText("HIV2-105 : With VL results");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[43] == null) {
                if (viralLoad != null) {
                    monthData[43] = viralLoad;
                }
            }else{
                if(viralLoad == null) {
                    viralLoad = monthData[43];
                }
            }

            if(viralLoad == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllViralLoadResultsContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        viralLoad = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(viralLoad);
            }
        }

        else if(data.getSuppressedViralLoadC() != null){
            holder.txtProductName.setText("HIV2-110 : VL suppressed");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[44] == null) {
                if (suppressedVL != null) {
                    monthData[44] = suppressedVL;
                }
            }else{
                if(suppressedVL == null) {
                    suppressedVL = monthData[44];
                }
            }

            if(suppressedVL == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        suppressedVL = String.valueOf(ClientDao.getAllSuppressedViralLoadResultsContact());
                        monthData[44] = suppressedVL;
                        return suppressedVL;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(suppressedVL);
            }
        }

        else if(data.getOnARTC() != null){
            holder.txtProductName.setText("HIV2-090 : Total mothers on ART");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[45] == null) {
                if (onART != null) {
                    monthData[45] = onART;
                }
            }else{
                if(onART == null) {
                    onART = monthData[45];
                }
            }

            if(onART == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllOnARTContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        onART = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(onART);
            }
        }

        else if(data.getTestedPositiveC() != null){
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("HIV Positive Results");
            holder.txtProductName.setText("HIV2-040 : Positive - Initial test in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[46] == null) {
                if (testedPositive != null) {
                    monthData[46] = testedPositive;
                }
            }else{
                if(testedPositive == null) {
                    testedPositive = monthData[46];
                }
            }

            if(testedPositive == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllTestedPositiveFirstContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        testedPositive = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(testedPositive);
            }
        }

        else if(data.getAlreadyPositiveC() != null){
            holder.txtProductName.setText("HIV2-035 : Known HIV+ at first ANC visit");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[47] == null) {
                if (alreadyPositive != null) {
                    monthData[47] = alreadyPositive;
                }
            }else{
                if(alreadyPositive == null) {
                    alreadyPositive = monthData[47];
                }
            }

            if(alreadyPositive == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        alreadyPositive = String.valueOf(ClientDao.getAllAlreadyPositiveFirstContact());
                        monthData[47] = alreadyPositive;
                        return alreadyPositive;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(alreadyPositive);
            }
        }

        else if(data.getTestedHIVC() != null){
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Elimination of Mother-to-Child Transmission of HIV");
            holder.txtProductName.setText("HIV2-005 : Tested - Initial test in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[48] == null) {
                if (testedHIV != null) {
                    monthData[48] = testedHIV;
                }
            }else{
                if(testedHIV == null) {
                    testedHIV = monthData[48];
                }
            }

            if(testedHIV == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllTestedHIVFirstContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        testedHIV = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(testedHIV);
            }
        }

        else if(data.getScreenedTBC() != null){
            holder.txtProductName.setText("IRH1-235 : Presumptive TB");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[49] == null) {
                if (screenedTB != null) {
                    monthData[49] = screenedTB;
                }
            }else{
                if(screenedTB == null) {
                    screenedTB = monthData[49];
                }
            }

            if(screenedTB == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllScreenedForTBContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        screenedTB = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(screenedTB);
            }
        }

        else if(data.getTTCVPlusTwoC() != null){
            holder.txtProductName.setText("IRH1-225 : Tetanus Toxoid (TT2+)");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[50] == null) {
                if (ttcvPlusTwo != null) {
                    monthData[50] = ttcvPlusTwo;
                }
            }else{
                if(ttcvPlusTwo == null) {
                    ttcvPlusTwo = monthData[50];
                }
            }

            if(ttcvPlusTwo == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllTTCVPlusTwoContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        ttcvPlusTwo = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(ttcvPlusTwo);
            }
        }

        else if(data.getReferredTBC() != null){
            holder.txtProductName.setText("IRH1-245 : Referred for TB Treatment");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[51] == null) {
                if (referredTB != null) {
                    monthData[51] = referredTB;
                }
            }else{
                if(referredTB == null) {
                    referredTB = monthData[51];
                }
            }

            if(referredTB == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllReferredTBContact());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        referredTB = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(referredTB);
            }
        }

        else if(data.getContactCountC() != null){
            holder.txtProductName.setText("IRH1-140 : Total ANC contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[52] == null) {
                if (contactCount != null) {
                    monthData[52] = contactCount;
                }
            }else{
                if(contactCount == null) {
                    contactCount = monthData[52];
                }
            }

            if(contactCount == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        return String.valueOf(ClientDao.getAllContactCount());
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        contactCount = result;
                        holder.txtTotalFemaleSeen.setText(result);
                    }
                }.execute();
            } else {
                holder.txtTotalFemaleSeen.setText(contactCount);
            }
        }




        //49-150
        //String totalFemaleSeen49_150 = ClientDao.getRefVisitedClientsTotal(data.getQuerry_drug(), "female", 49, 150);
        //String totalMaleSeen49_150 = ClientDao.getRefVisitedClientsTotal(data.getQuerry_drug(), "male", 49, 150);


        //holder.m4.setText(totalMaleSeen25_49);

        //holder.f5.setText(totalFemaleSeen49_150);
        //holder.m5.setText(totalMaleSeen49_150);
/*
        if(totalMaleSeen == null){
            holder.txtTotalMaleSeen.setText("0");
            holder.txtTotalMaleServices.setText("0");
        } else {
            if(data.getQuerry_drug().equals("Sterilization") || data.getQuerry_drug().equals("Other")){
                holder.txtTotalMaleSeen.setText(String.valueOf(totalMaleSeen.size()));
            }else{

            }

        }*/

        holder.txtTotalMaleSeen.setVisibility(View.GONE);
        holder.m1.setVisibility(View.GONE);
        holder.m2.setVisibility(View.GONE);
        holder.m3.setVisibility(View.GONE);
        holder.m4.setVisibility(View.GONE);
        holder.m5.setVisibility(View.GONE);
        holder.txtTotalMaleServices.setVisibility(View.GONE);
        /*if(totalFemaleSeen == null){

        } else {
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen.size()));
            holder.txtTotalFemaleServices.setText("0");;
        }
*/
        holder.txtTotalFemaleServices.setText("0");

        try {
            saveMonthDataToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /*public int calculateAge(String birthdate){

        if (birthdate == null || birthdate.isEmpty()){
            return 0;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
            LocalDate dateOfBirth = LocalDate.parse(birthdate, formatter);
            LocalDate currentDate = LocalDate.now();
            Period age = Period.between(dateOfBirth, currentDate);
            return age.getYears();
        } catch (DateTimeParseException e) {
            return 0;
        }
    }*/

    @Override
    public int getItemCount() {

        return reportitems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtProductName, txtTotalMaleSeen, txtTotalFemaleSeen, txtTotalMaleServices, txtTotalFemaleServices, m1,f1, m2, f2, m3, f3, m4, f4, m5, f5, groupLabel;

        RelativeLayout rLayout;


        public ViewHolder(View itemView) {

            super(itemView);

            rLayout = itemView.findViewById(R.id.itemm);
            txtProductName  = itemView.findViewById(R.id.product_name);
            txtTotalMaleSeen= itemView.findViewById(R.id.total_male_seen);
            txtTotalFemaleSeen = itemView.findViewById(R.id.total_female_seen);
            txtTotalMaleServices = itemView.findViewById(R.id.total_male_services);
            txtTotalFemaleServices = itemView.findViewById(R.id.total_female_services);

            m1 = itemView.findViewById(R.id.m1);
            f1 = itemView.findViewById(R.id.f1);

            m2 = itemView.findViewById(R.id.m2);
            f2 = itemView.findViewById(R.id.f2);

            m3 = itemView.findViewById(R.id.m3);
            f3 = itemView.findViewById(R.id.f3);

            m4 = itemView.findViewById(R.id.m4);
            f4 = itemView.findViewById(R.id.f4);

            m5 = itemView.findViewById(R.id.m5);
            f5 = itemView.findViewById(R.id.f5);

            groupLabel = itemView.findViewById(R.id.grouping_label);


        }


        @Override
        public void onClick(View v) {

        }
    }

    private void saveMonthDataToFile() throws IOException {
        if (isMonthDataComplete(monthData)) {
            String FILENAME = selectedMonth + "_" + "monthData.txt";
            String filePath = Utils.getAppPath(context) + FILENAME;
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    System.out.println("Attempting to save data to file");
                    if (monthData != null) {
                        for (String data : monthData) {
                            writer.write(data);
                            writer.newLine();
                            System.out.println("Writing data to file");
                        }
                    }
                    System.out.println("Data saved successfully");
                } catch (IOException e) {
                    System.out.println("Failed to save data to file");
                }
            } else {
                System.out.println("Unable to save incomplete month data");
            }
        }

    }

    private void loadMonthDataFromFile() {
        String FILENAME = selectedMonth+"_"+"monthData.txt";
        String filePath = Utils.getAppPath(context) + FILENAME;
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            System.out.println("Attempting to load data from file");
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < monthData.length) {
                monthData[index++] = line;
                System.out.println("Reading data from file");
            }
        } catch (IOException e) {
            System.out.println("Failed to load data from file");
        }
    }

    private boolean isMonthDataComplete(String[] monthData) {
        for (String data : monthData) {
            if (data == null || data.isEmpty()) {
                System.out.println("Data collection is not complete");
                return false;  // Found an empty or null element
            }
        }
        System.out.println("Data collection has been completed");
        return true;  // All elements are filled with data
    }


}