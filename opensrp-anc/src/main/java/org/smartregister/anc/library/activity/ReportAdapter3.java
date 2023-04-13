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


public class ReportAdapter3 extends RecyclerView.Adapter< ReportAdapter3.ViewHolder> {
    Context context;

    List<ReportModel1> reportitems;
//    List<ReportModel1> feedbackItems;
    String monthNumber;


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

        final ReportModel1 data = reportitems.get(position);
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

        if(data.getTrimester() != null && data.getTrimester().contains("First Trimester")) {
            //10-14
            String totalFemaleSeen10_14 = ClientDao.getFirstContact("gest_age_openmrs", "8", "12");

            //15-19
            String totalFemaleSeen15_19 = ClientDao.getFirstContactAbove15("gest_age_openmrs", "8", "12");

            //20-24
            String totalFemaleSeen20_24 = ClientDao.getFirstContactAbove20("gest_age_openmrs", "8", "12");

            //25-49
            String totalFemaleSeen25_49 = ClientDao.getFirstContactAbove25("gest_age_openmrs", "8", "12");

            holder.f1.setText(totalFemaleSeen10_14);
            //holder.m1.setText(totalMaleSeen10_14);

            holder.f2.setText(totalFemaleSeen15_19);
            //holder.m2.setText(totalMaleSeen15_19);

            holder.f3.setText(totalFemaleSeen20_24);
            //holder.m3.setText(totalMaleSeen20_24);

            holder.f4.setText(totalFemaleSeen25_49);

            int womenSeen = Integer.parseInt(totalFemaleSeen10_14) + Integer.parseInt(totalFemaleSeen15_19) + Integer.parseInt(totalFemaleSeen25_49) + Integer.parseInt(totalFemaleSeen20_24);
            String WT = String.valueOf(womenSeen);
            holder.txtTotalFemaleSeen.setText(WT);
        }
        else if(data.getTrimester() != null && data.getTrimester().contains("Second Trimester")) {
            //10-14
            String totalFemaleSeen10_14 = ClientDao.getFirstContact("gest_age_openmrs", "13", "26");

            //15-19
            String totalFemaleSeen15_19 = ClientDao.getFirstContactAbove15("gest_age_openmrs", "13", "26");

            //20-24
            String totalFemaleSeen20_24 = ClientDao.getFirstContactAbove20("gest_age_openmrs", "13", "26");

            //25-49
            String totalFemaleSeen25_49 = ClientDao.getFirstContactAbove25("gest_age_openmrs", "13", "26");

            holder.f1.setText(totalFemaleSeen10_14);
            //holder.m1.setText(totalMaleSeen10_14);

            holder.f2.setText(totalFemaleSeen15_19);
            //holder.m2.setText(totalMaleSeen15_19);

            holder.f3.setText(totalFemaleSeen20_24);
            //holder.m3.setText(totalMaleSeen20_24);

            holder.f4.setText(totalFemaleSeen25_49);

            int womenSeen = Integer.parseInt(totalFemaleSeen10_14) + Integer.parseInt(totalFemaleSeen15_19) + Integer.parseInt(totalFemaleSeen25_49) + Integer.parseInt(totalFemaleSeen20_24);
            String WT = String.valueOf(womenSeen);
            holder.txtTotalFemaleSeen.setText(WT);
        }
        else if(data.getTrimester() != null && data.getTrimester().contains("Third Trimester")) {
            //10-14
            String totalFemaleSeen10_14 = ClientDao.getFirstContact("gest_age_openmrs", "27", "40");

            //15-19
            String totalFemaleSeen15_19 = ClientDao.getFirstContactAbove15("gest_age_openmrs", "27", "40");

            //20-24
            String totalFemaleSeen20_24 = ClientDao.getFirstContactAbove20("gest_age_openmrs", "27", "40");

            //25-49
            String totalFemaleSeen25_49 = ClientDao.getFirstContactAbove25("gest_age_openmrs", "27", "40");

            holder.f1.setText(totalFemaleSeen10_14);
            //holder.m1.setText(totalMaleSeen10_14);

            holder.f2.setText(totalFemaleSeen15_19);
            //holder.m2.setText(totalMaleSeen15_19);

            holder.f3.setText(totalFemaleSeen20_24);
            //holder.m3.setText(totalMaleSeen20_24);

            holder.f4.setText(totalFemaleSeen25_49);

            int womenSeen = Integer.parseInt(totalFemaleSeen10_14) + Integer.parseInt(totalFemaleSeen15_19) + Integer.parseInt(totalFemaleSeen25_49) + Integer.parseInt(totalFemaleSeen20_24);
            String WT = String.valueOf(womenSeen);
            holder.txtTotalFemaleSeen.setText(WT);
        }else if(data.getOrigin() != null){
            holder.txtProductName.setText("Outside Catchment Area");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllOutside();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));

        }else if(data.getFirstC() != null){
            holder.txtProductName.setText("First ANC Contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllFirstContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }else if(data.getSecondC() != null){
            holder.txtProductName.setText("Second ANC Contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllSecondContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }else if(data.getThirdC() != null){
            holder.txtProductName.setText("Third ANC Contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllThirdContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }else if(data.getFourthToSeventhC() != null){
            holder.txtProductName.setText("Fourth To Seventh ANC Contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllFourthToSeventhContact();
            holder.txtTotalFemaleSeen.setText(String.valueOf(totalFemaleSeen));
        }else if(data.getEighthAboveC() != null){
            holder.txtProductName.setText("Eighth Above ANC Contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            int totalFemaleSeen = ClientDao.getAllEighthAboveContact();
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

        holder.txtTotalMaleSeen.setText("0");
        holder.m1.setText("0");
        holder.m2.setText("0");
        holder.m3.setText("0");
        holder.m4.setText("0");
        holder.m5.setText("0");
        holder.txtTotalMaleServices.setText("0");
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
