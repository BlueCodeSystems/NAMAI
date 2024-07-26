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
    static Context context;

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
    static String[] monthData = new String[53];
    public ReportModel1 data;










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


        data = reportitems.get(position);
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

        if (data.getTrimester() != null && data.getTrimester().contains("IRH1-005 / IRH1-010 / IRH1-015 / IRH1-020 / IRH1-025 : WOMEN WHO CAME FOR ANC DURING THE FIRST TRIMESTER")) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("First antenatal contacts");
            if (monthData[4] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            }
            if (monthData[1] != null) {
                holder.f1.setText(monthData[1]);
            }
            if (monthData[2] != null) {
                holder.f2.setText(monthData[2]);
            }
            if (monthData[3] != null) {
                holder.f3.setText(monthData[3]);
            }
            if (monthData[4] != null) {
                holder.f4.setText(monthData[4]);
            }
            if (monthData[1] != null && monthData[2] != null && monthData[3] != null && monthData[4] != null)
            {
                String womenSeen = String.valueOf(Integer.parseInt(monthData[1]) + Integer.parseInt(monthData[2]) + Integer.parseInt(monthData[3]) + Integer.parseInt(monthData[4]));
                holder.txtTotalFemaleSeen.setText(womenSeen);

        }


            } else if (data.getTrimester() != null && data.getTrimester().contains("IRH1-030 / IRH1-035 / IRH1-040 / IRH1-045 / IRH1-050 : WOMEN WHO CAME FOR ANC DURING THE SECOND TRIMESTER")) {
                if (monthData[8] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                }
                if (monthData[5] != null) {
                    holder.f1.setText(monthData[5]);
                }
                    if (monthData[6] != null) {
                        holder.f2.setText(monthData[6]);
                    }
                    if (monthData[7] != null) {
                        holder.f3.setText(monthData[7]);
                    }
                    if (monthData[8] != null) {
                        holder.f4.setText(monthData[8]);
                    }

            if (monthData[5] != null && monthData[6] != null && monthData[7] != null && monthData[8] != null) {
                String womenSeen = String.valueOf(Integer.parseInt(monthData[5]) + Integer.parseInt(monthData[6]) + Integer.parseInt(monthData[7]) + Integer.parseInt(monthData[8]));

                holder.txtTotalFemaleSeen.setText(womenSeen);
            }


            } else if (data.getTrimester() != null && data.getTrimester().contains("IRH1-055 / IRH1-060 / IRH1-065 / IRH1-070 / IRH1-075 : WOMEN WHO CAME FOR ANC DURING THE THIRD TRIMESTER")) {
                if (monthData[12] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                }
                if (monthData[9] != null) {
                    holder.f1.setText(monthData[9]);
                }
                if (monthData[10] != null) {
                    holder.f2.setText(monthData[10]);
                }
                if (monthData[11] != null) {
                    holder.f3.setText(monthData[11]);
                }
                if (monthData[12] != null) {
                    holder.f4.setText(monthData[12]);
                }
            if (monthData[9] != null && monthData[10] != null && monthData[11] != null && monthData[12] != null) {
                String womenSeen = String.valueOf(Integer.parseInt(monthData[9]) + Integer.parseInt(monthData[10]) + Integer.parseInt(monthData[11]) + Integer.parseInt(monthData[12]));

                holder.txtTotalFemaleSeen.setText(womenSeen);
            }


            } else if (data.getFirstC() != null) {
            holder.txtProductName.setText("IRH1-100 : Total 1st ANC contact");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[14] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[14]);
            }
        } else if (data.getOrigin() != null) {
                holder.txtProductName.setText("IRH1-105 : Women from outside the catchment area");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[13] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[13]);
                }

        } else if (data.getHighRiskC() != null) {
            holder.txtProductName.setText("IRH1-110 : High-risk Pregnancies");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[19] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[19]);
            }

        } else if (data.getSecondC() != null) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Follow-up antenatal contacts");
                holder.txtProductName.setText("IRH1-115 : 2nd ANC contact");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[15] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[15]);
                }

            } else if (data.getThirdC() != null) {
                holder.txtProductName.setText("IRH1-120 : 3rd ANC contact");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[16] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[16]);
                }

            } else if (data.getFourthToSeventhC() != null) {
                holder.txtProductName.setText("IRH1-125 : 4th to 7th ANC contacts");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[17] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[17]);
                }
            } else if (data.getEighthAboveC() != null) {
                holder.txtProductName.setText("IRH1-130 : 8th+ ANC contact");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[18] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[18]);
                }
            } else if (data.getFollowUpC() != null) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Follow-up Antenantal Contacts");
            holder.txtProductName.setText("IRH1-135 : Total follow up contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[37] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[37]);
            }

        } else if (data.getContactCountC() != null) {
            holder.txtProductName.setText("IRH1-140 : Total ANC contacts");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[52] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[52]);
            }

        } else if (data.getAnaemiaScreenedC() != null) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Antenatal Screening");
            holder.txtProductName.setText("IRH1-145 : Initial anaemia screening");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[24] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[24]);
            }
        } else if (data.getAnaemiaPositiveC() != null) {
            holder.txtProductName.setText("IRH1-150 : Diagnosed with Anaemia");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[25] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[25]);
            }
        } else if (data.getSyphScreenedC() != null) {
                holder.txtProductName.setText("IRH1-155 : Initial syphilis screening");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[20] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[20]);
                }
            } else if (data.getSyphPositiveC() != null) {
                holder.txtProductName.setText("IRH1-160 : Reactive to Initial syphilis Test");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[21] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[21]);
                }
            } else if (data.getHepbScreenedC() != null) {
                holder.txtProductName.setText("IRH1-175 : Screened for Hepatitis B");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[22] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[22]);
                }
            } else if (data.getHepBPositiveC() != null) {
                holder.txtProductName.setText("IRH1-180 : Positive for Hepatitis B");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[23] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[23]);
                }
            } else if (data.getIPTP1C() != null) {
                holder.groupLabel.setVisibility(View.VISIBLE);
                holder.groupLabel.setText("Non-HIV Prophylaxis and Services provided");
                holder.txtProductName.setText("IRH1-190 : IPTp 1");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[26] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[26]);
                }

            } else if (data.getIPTP2C() != null) {
                holder.txtProductName.setText("IRH1-195 : IPTp 2");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[27] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[27]);
                }

            } else if (data.getIPTP3C() != null) {
                holder.txtProductName.setText("IRH1-200 : IPTp 3");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[28] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[28]);
                }

            } else if (data.getIPTP4C() != null) {
                holder.txtProductName.setText("IRH1-205 : IPTp 4");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[29] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[29]);
                }

            } else if (data.getProvidedITNC() != null) {
                holder.txtProductName.setText("IRH1-210 : Provided with ITN");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[30] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[30]);
                }

            } else if (data.getProvidedIronC() != null) {
                holder.txtProductName.setText("IRH1-215 : Provided with Iron");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[31] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[31]);
                }

            } else if (data.getTTCVPlusTwoC() != null) {
            holder.txtProductName.setText("IRH1-225 : Tetanus Toxoid (TT2+)");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[50] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[50]);
            }

        } else if (data.getDewormedC() != null) {
                holder.txtProductName.setText("IRH1-230 : Dewormed");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[32] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[32]);
                }

            }else if (data.getScreenedTBC() != null) {
            holder.txtProductName.setText("IRH1-235 : Presumptive TB");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[49] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[49]);
            }

        } else if (data.getReferredTBC() != null) {
            holder.txtProductName.setText("IRH1-245 : Referred for TB Treatment");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[51] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[51]);
            }

        } else if (data.getTestedHIVC() != null) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Elimination of Mother-to-Child Transmission of HIV");
            holder.txtProductName.setText("HIV2-005 : Tested - Initial test in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[48] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[48]);
            }

        } else if (data.getAlreadyPositiveC() != null) {
            holder.txtProductName.setText("HIV2-035 : Known HIV+ at first ANC visit");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[47] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[47]);
            }

        } else if (data.getTestedPositiveC() != null) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("HIV Positive Results");
            holder.txtProductName.setText("HIV2-040 : Positive - Initial test in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[46] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[46]);
            }

        } else if (data.getAlreadyOnARTC() != null) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Maternal ART");
            holder.txtProductName.setText("HIV2-060 : Already on ART at 1st ANC visit");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[36] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[36]);
            }

        } else if (data.getStartedARTC() != null) {
            holder.txtProductName.setText("HIV2-065 : Started on ART in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[35] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[35]);
            }

        } else if (data.getOnARTC() != null) {
            holder.txtProductName.setText("HIV2-090 : Total mothers on ART");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[45] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[45]);
            }

        } else if (data.getStartedOnPrepC() != null) {
                holder.txtProductName.setText("HIV2-091 : Started on PrEP in ANC");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[33] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[33]);
                }

            } else if (data.getAlreadyOnPrepC() != null) {
                holder.txtProductName.setText("HIV2-092 : Currently on PrEP in ANC");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[34] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[34]);
                }

        } else if (data.getViralLoadC() != null) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Monitoring");
            holder.txtProductName.setText("HIV2-105 : With VL results");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[43] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[43]);
            }

        } else if (data.getSuppressedViralLoadC() != null) {
            holder.txtProductName.setText("HIV2-110 : VL suppressed");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[44] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[44]);
            }

        } else if (data.getMaleAlreadyPositiveC() != null) {
            holder.groupLabel.setVisibility(View.VISIBLE);
            holder.groupLabel.setText("Male Partner Involvement");
            holder.txtProductName.setText("HIV2-115 : With known status at 1st visit to MCH");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[40] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[40]);
            }

        } else if (data.getMaleTestedC() != null) {
            holder.txtProductName.setText("HIV2-120 : Tested in MCH");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[42] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[42]);
            }

        } else if (data.getMalePositiveC() != null) {
            holder.txtProductName.setText("HIV2-125 : Positive results (MCH only)");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[41] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[41]);
            }

        } else if (data.getMaleStartedARTC() != null) {
            holder.txtProductName.setText("HIV2-126 : Male Partner started on ART in ANC");
            holder.f1.setBackgroundResource(R.drawable.na_round_button);
            holder.f2.setBackgroundResource(R.drawable.na_round_button);
            holder.f3.setBackgroundResource(R.drawable.na_round_button);
            holder.f4.setBackgroundResource(R.drawable.na_round_button);
            holder.f5.setBackgroundResource(R.drawable.na_round_button);

            if (monthData[39] == null) {
                holder.txtTotalFemaleSeen.setText("Loading...");
            } else {
                holder.txtTotalFemaleSeen.setText(monthData[39]);
            }

        } else if (data.getDiscordantC() != null) {
                holder.txtProductName.setText("HIV2-130 : Discordant Test Results");
                holder.f1.setBackgroundResource(R.drawable.na_round_button);
                holder.f2.setBackgroundResource(R.drawable.na_round_button);
                holder.f3.setBackgroundResource(R.drawable.na_round_button);
                holder.f4.setBackgroundResource(R.drawable.na_round_button);
                holder.f5.setBackgroundResource(R.drawable.na_round_button);

                if (monthData[38] == null) {
                    holder.txtTotalFemaleSeen.setText("Loading...");
                } else {
                    holder.txtTotalFemaleSeen.setText(monthData[38]);
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

    /*private void saveMonthDataToFile() throws IOException {
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
    }*/

    static void loadMonthDataFromFile() {
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

    /*private boolean isMonthDataComplete(String[] monthData) {
        for (String data : monthData) {
            if (data == null || data.isEmpty()) {
                System.out.println("Data collection is not complete");
                return false;  // Found an empty or null element
            }
        }
        System.out.println("Data collection has been completed");
        return true;  // All elements are filled with data
    }*/


}