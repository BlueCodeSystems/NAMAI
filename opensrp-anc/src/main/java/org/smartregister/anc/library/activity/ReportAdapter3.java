package org.smartregister.anc.library.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.anc.library.R;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import android.util.LruCache;


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
    String origin = null;
    String firstc = null;
    String secondc = null;
    String thirdc = null;
    String fourthToSeventhC = null;
    String eighthAboveC = null;
    String highRiskC = null;






    public ReportAdapter3(String monthNumber, List<ReportModel1> reportitems, Context context){

        super();

  //      this.feedbackItems = feedbackItems;
        this.reportitems = reportitems;
        this.monthNumber = monthNumber;
        this.context = context;
    }

    @Override
    public ReportAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_report_1, parent, false);

        ReportAdapter3.ViewHolder viewHolder = new ReportAdapter3.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReportAdapter3.ViewHolder holder, final int position) {


        ReportModel1 data = reportitems.get(position);
        //int feedback = feedbackItems.size();


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

        if(data.getTrimester() != null && data.getTrimester().contains("IRH1-005 / IRH1-010 / IRH1-015 / IRH1-020 / IRH1-025 : WOMEN WHO CAME FOR ANC DURING THE FIRST TRIMESTER")) {
            //10-14
            if(f1first == null){
                f1first = ClientDao.getFirstContact("gest_age_openmrs", "8", "12");
            }
            String totalFemaleSeen10_14 = f1first;
            //15-19
            if(f2first == null){
                f2first = ClientDao.getFirstContactAbove15("gest_age_openmrs", "8", "12");
            }
            String totalFemaleSeen15_19 = f2first;

            //20-24
            if(f3first == null){
                f3first = ClientDao.getFirstContactAbove20("gest_age_openmrs", "8", "12");
            }
            String totalFemaleSeen20_24 = f3first;

            //25-49
            if(f4first == null){
                f4first = ClientDao.getFirstContactAbove25("gest_age_openmrs", "8", "12");
            }
            String totalFemaleSeen25_49 = f4first;

            holder.f1.setText(totalFemaleSeen10_14);
            //holder.m1.setText(totalMaleSeen10_14);

            holder.f2.setText(totalFemaleSeen15_19);
            //holder.m2.setText(totalMaleSeen15_19);

            holder.f3.setText(totalFemaleSeen20_24);
            //holder.m3.setText(totalMaleSeen20_24);

            holder.f4.setText(totalFemaleSeen25_49);

            int womenSeen = Integer.parseInt(totalFemaleSeen10_14) + Integer.parseInt(totalFemaleSeen15_19) + Integer.parseInt(totalFemaleSeen25_49) + Integer.parseInt(totalFemaleSeen20_24);
            if(totalfemalesfirst == null){
                totalfemalesfirst = String.valueOf(womenSeen);
            }
            //String WT = String.valueOf(womenSeen);
            holder.txtTotalFemaleSeen.setText(totalfemalesfirst);
        }
        else if(data.getTrimester() != null && data.getTrimester().contains("IRH1-030 / IRH1-035 / IRH1-040 / IRH1-045 / IRH1-050 : WOMEN WHO CAME FOR ANC DURING THE SECOND TRIMESTER")) {
            //10-14
            if(f1second == null){
                f1second = ClientDao.getFirstContact("gest_age_openmrs", "13", "26");
            }
            String totalFemaleSeen10_14 = f1second;

            //15-19
            if(f2second == null){
                f2second = ClientDao.getFirstContactAbove15("gest_age_openmrs", "13", "26");
            }
            String totalFemaleSeen15_19 = f2second;

            //20-24
            if(f3second == null){
                f3second = ClientDao.getFirstContactAbove20("gest_age_openmrs", "13", "26");
            }
            String totalFemaleSeen20_24 = f3second;

            //25-49
            if(f4second == null){
                f4second = ClientDao.getFirstContactAbove25("gest_age_openmrs", "13", "26");
            }
            String totalFemaleSeen25_49 = f4second;

            holder.f1.setText(totalFemaleSeen10_14);
            //holder.m1.setText(totalMaleSeen10_14);

            holder.f2.setText(totalFemaleSeen15_19);
            //holder.m2.setText(totalMaleSeen15_19);

            holder.f3.setText(totalFemaleSeen20_24);
            //holder.m3.setText(totalMaleSeen20_24);

            holder.f4.setText(totalFemaleSeen25_49);

            int womenSeen = Integer.parseInt(totalFemaleSeen10_14) + Integer.parseInt(totalFemaleSeen15_19) + Integer.parseInt(totalFemaleSeen25_49) + Integer.parseInt(totalFemaleSeen20_24);
            if(totalfemalessecond == null){
                totalfemalessecond = String.valueOf(womenSeen);
            }
            //String WT = String.valueOf(womenSeen);
            holder.txtTotalFemaleSeen.setText(totalfemalessecond);
        }
        else if(data.getTrimester() != null && data.getTrimester().contains("IRH1-055 / IRH1-060 / IRH1-065 / IRH1-070 / IRH1-075 : WOMEN WHO CAME FOR ANC DURING THE THIRD TRIMESTER")) {
            //10-14
            if(f1third == null){
                f1third = ClientDao.getFirstContact("gest_age_openmrs", "27", "40");
            }
            String totalFemaleSeen10_14 = f1third;

            //15-19
            if(f2third == null){
                f2third = ClientDao.getFirstContactAbove15("gest_age_openmrs", "27", "40");
            }
            String totalFemaleSeen15_19 = f2third;

            //20-24
            if(f3third == null){
                f3third = ClientDao.getFirstContactAbove20("gest_age_openmrs", "27", "40");
            }
            String totalFemaleSeen20_24 = f3third;

            //25-49
            if(f4third == null){
                f4third = ClientDao.getFirstContactAbove25("gest_age_openmrs", "27", "40");
            }
            String totalFemaleSeen25_49 = f4third;

            holder.f1.setText(totalFemaleSeen10_14);
            //holder.m1.setText(totalMaleSeen10_14);

            holder.f2.setText(totalFemaleSeen15_19);
            //holder.m2.setText(totalMaleSeen15_19);

            holder.f3.setText(totalFemaleSeen20_24);
            //holder.m3.setText(totalMaleSeen20_24);

            holder.f4.setText(totalFemaleSeen25_49);

            int womenSeen = Integer.parseInt(totalFemaleSeen10_14) + Integer.parseInt(totalFemaleSeen15_19) + Integer.parseInt(totalFemaleSeen25_49) + Integer.parseInt(totalFemaleSeen20_24);
            if(totalfemalesthird == null){
                totalfemalesthird = String.valueOf(womenSeen);
            }
            //String WT = String.valueOf(womenSeen);
            holder.txtTotalFemaleSeen.setText(totalfemalesthird);
        }

        else if(data.getOrigin() != null){
            holder.txtProductName.setText("IRH1-105 : Pregnant contacts outside catchment area");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if(origin == null) {
                origin = String.valueOf(ClientDao.getAllOutside());
            }
            holder.txtTotalFemaleSeen.setText(origin);

        }

        else if(data.getFirstC() != null){
            holder.txtProductName.setText("IRH1-100 : Total 1st ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if(firstc == null) {
                firstc = String.valueOf(ClientDao.getAllFirstContact());
            }
            holder.txtTotalFemaleSeen.setText(firstc);
        }

        else if(data.getSecondC() != null){
            holder.txtProductName.setText("IRH1-115 : 2nd ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if(secondc == null) {
                secondc = String.valueOf(ClientDao.getAllSecondContact());
            }
            holder.txtTotalFemaleSeen.setText(secondc);
        }

        else if(data.getThirdC() != null){
            holder.txtProductName.setText("IRH1-120 : 3rd ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if(thirdc == null) {
                thirdc = String.valueOf(ClientDao.getAllThirdContact());
            }
            holder.txtTotalFemaleSeen.setText(thirdc);
        }

        else if(data.getFourthToSeventhC() != null){
            holder.txtProductName.setText("IRH1-125 : 4th to 7th ANC contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (fourthToSeventhC == null) {
                fourthToSeventhC = String.valueOf(ClientDao.getAllFourthToSeventhContact());
            }
            holder.txtTotalFemaleSeen.setText(fourthToSeventhC);
        }

        else if(data.getEighthAboveC() != null){
            holder.txtProductName.setText("IRH1-130 : 8th+ ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if(eighthAboveC == null) {
                eighthAboveC = String.valueOf(ClientDao.getAllEighthAboveContact());
            }
            holder.txtTotalFemaleSeen.setText(eighthAboveC);
        }

        else if(data.getHighRiskC() != null){
            holder.txtProductName.setText("IRH1-110 : High-risk Pregnancies");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllHighRiskContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getSyphScreenedC() != null){
            holder.txtProductName.setText("IRH1-155 : Initial syphilis screening");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllSyphScreenedContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getSyphPositiveC() != null){
            holder.txtProductName.setText("IRH1-160 : Reactive to Initial syphilis Test");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllSyphPositiveContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getHepbScreenedC() != null){
            holder.txtProductName.setText("IRH1-175 : Screened for Hepatitis B");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllHepBScreenedContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getHepBPositiveC() != null){
            holder.txtProductName.setText("IRH1-180 : Positive for Hepatitis B");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllHepBPositiveContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getAnaemiaScreenedC() != null){
            holder.txtProductName.setText("IRH1-145 : Initial anaemia screening");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllAnaemiaScreenedContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getAnaemiaPositiveC() != null){
            holder.txtProductName.setText("IRH1-150 : Diagnosed with Anaemia");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllAnaemiaPositiveContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getIPTP1C() != null){
            holder.txtProductName.setText("IRH1-190 : IPTp 1");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllIPTP1Contact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getIPTP2C() != null){
            holder.txtProductName.setText("IRH1-195 : IPTp 2");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllIPTP2Contact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getIPTP3C() != null){
            holder.txtProductName.setText("IRH1-200 : IPTp 3");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllIPTP3Contact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getIPTP4C() != null){
            holder.txtProductName.setText("IRH1-205 : IPTp 4");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllIPTP4Contact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getProvidedITNC() != null){
            holder.txtProductName.setText("IRH1-210 : Provided with ITN");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllProvidedITNContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getProvidedIronC() != null){
            holder.txtProductName.setText("IRH1-215 : Provided with Iron");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllProvidedIronContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getDewormedC() != null){
            holder.txtProductName.setText("IRH1-230 : Dewormed");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllDewormedContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getStartedOnPrepC() != null){
            holder.txtProductName.setText("HIV2-091 : Started on PrEP in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllStartedOnPrepContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getAlreadyOnPrepC() != null){
            holder.txtProductName.setText("HIV2-092 : Currently on PrEP in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllAlreadyOnPrepContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getStartedARTC() != null){
            holder.txtProductName.setText("HIV2-065 : Started on ART in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllStartedARTinANCContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getAlreadyOnARTC() != null){
            holder.txtProductName.setText("HIV2-060 : Already on ART at 1st ANC visit");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllAlreadyARTinANCContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getFollowUpC() != null){
            holder.txtProductName.setText("IRH1-135 : Total follow up contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllFollowUpContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getDiscordantC() != null){
            holder.txtProductName.setText("HIV2-130 : Discordant Test Results");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllDiscordantContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getMaleStartedARTC() != null){
            holder.txtProductName.setText("HIV2-126 : Male Partner started on ART in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllMaleStartedARTinANCContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getMaleAlreadyPositiveC() != null){
            holder.txtProductName.setText("HIV2-115 : With known status at 1st visit to MCH");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllMaleAlreadyPositiveContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getMalePositiveC() != null){
            holder.txtProductName.setText("HIV2-125 : Positive results (MCH only)");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllMalePositiveFirstContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getMaleTestedC() != null){
            holder.txtProductName.setText("HIV2-120 : Tested in MCH");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllMaleTestFirstContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getViralLoadC() != null){
            holder.txtProductName.setText("HIV2-105 : With VL results");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllViralLoadResultsContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getSuppressedViralLoadC() != null){
            holder.txtProductName.setText("HIV2-110 : VL suppressed");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllSuppressedViralLoadResultsContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getOnARTC() != null){
            holder.txtProductName.setText("HIV2-090 : Total mothers on ART");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllOnARTContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getTestedPositiveC() != null){
            holder.txtProductName.setText("HIV2-040 : Positive - Initial test in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllTestedPositiveFirstContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getAlreadyPositiveC() != null){
            holder.txtProductName.setText("HIV2-035 : Known HIV+ at first ANC visit");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllAlreadyPositiveFirstContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getTestedHIVC() != null){
            holder.txtProductName.setText("HIV2-005 : Tested - Initial test in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllTestedHIVFirstContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getScreenedTBC() != null){
            holder.txtProductName.setText("IRH1-235 : Presumptive TB");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllScreenedForTBContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getTTCVPlusTwoC() != null){
            holder.txtProductName.setText("IRH1-225 : Tetanus Toxoid (TT2+)");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllTTCVPlusTwoContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getReferredTBC() != null){
            holder.txtProductName.setText("IRH1-245 : Referred for TB Treatment");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllReferredTBContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }

        else if(data.getContactCountC() != null){
            holder.txtProductName.setText("IRH1-140 : Total ANC contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllContactCount();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
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

        TextView txtProductName, txtTotalMaleSeen, txtTotalFemaleSeen, txtTotalMaleServices, txtTotalFemaleServices, m1,f1, m2, f2, m3, f3, m4, f4, m5, f5;

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


        }


        @Override
        public void onClick(View v) {

        }
    }
}