package org.smartregister.anc.library.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.anc.library.R;
import org.smartregister.anc.library.domain.AttentionFlag;
import org.smartregister.anc.library.model.AttentionFlagModel;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder>  {

    Context context;

    List<MonthModel> months;

    public ReportListAdapter(List<MonthModel> months, Context context){

        super();

        this.months = months;
        this.context = context;

    }

    @Override
    public ReportListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_month, parent, false);

        ReportListAdapter.ViewHolder viewHolder = new ReportListAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReportListAdapter.ViewHolder holder, final int position) {

        final MonthModel month = months.get(position);


        holder.txtMonthName.setText(month.getMonthName() + " 2023 Report");

        holder.monthLayout.setOnClickListener(v -> {

            if (v.getId() == R.id.monthLayout) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Monthly Report Form");

                String attentionFlagArrayList = ClientDao.getFirstContact("gest_age_openmrs","8","12");


                String[] stringArray = new String[]{"1. Monthly detailed reports [Click to Open]"/*, "2. FP Information, Education and Communication [Click to Open]", "3. Referrals to Health Facilities [Click to Open]", "4. Stockouts of Family Planning Commodities [Click to Open]"*/};

                builder.setItems(stringArray, (dialog, which) -> {


                    if(which == 0){
                        Intent i = new Intent(context, ReportActivity3.class);
                        i.putExtra("month_name", month.getMonthName());
                        i.putExtra("month_number", month.getMonthNumber());
                        i.putExtra("report_type", "1");
                        context.startActivity(i);
                    }/* else if(which == 1){
                        Intent i = new Intent(context, ReportActivity3.class);
                        i.putExtra("month_name", month.getMonthName());
                        i.putExtra("month_number", month.getMonthNumber());
                        i.putExtra("report_type", "2");
                        context.startActivity(i);
                    } else if(which == 2){
                        Intent i = new Intent(context, ReportActivity3.class);
                        i.putExtra("month_name", month.getMonthName());
                        i.putExtra("month_number", month.getMonthNumber());
                        i.putExtra("report_type", "3");
                        context.startActivity(i);
                    } else if(which == 3){
                        Intent i = new Intent(context, ReportActivity3.class);
                        i.putExtra("month_name", month.getMonthName());
                        i.putExtra("month_number", month.getMonthNumber());
                        i.putExtra("report_type", "4");
                        context.startActivity(i);
                    }
*/
                    else {
                        //Toasty.info(context, "Coming Soon", Toasty.LENGTH_SHORT).show();
                    }

                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return months.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtMonthName;

        RelativeLayout monthLayout;


        public ViewHolder(View itemView) {

            super(itemView);

            monthLayout = itemView.findViewById(R.id.monthLayout);
            txtMonthName = itemView.findViewById(R.id.month);

        }


        @Override
        public void onClick(View v) {

        }
    }

}
