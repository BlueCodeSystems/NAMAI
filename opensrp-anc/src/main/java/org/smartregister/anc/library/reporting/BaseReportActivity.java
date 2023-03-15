package org.smartregister.anc.library.reporting;
/*
import org.opensrp.reporting.domain.ANCVisit;
import org.opensrp.reporting.service.ReportService;*/
import org.smartregister.domain.db.EventClient;

import java.util.List;


public class BaseReportActivity {
    /*private ReportService reportService;

    public BaseReportActivity(ReportService reportService) {
        this.reportService = reportService;
    }

    public int countPregnantWomenLessThan15Years() {
        int count = 0;
        List<EventClient> ancVisits = reportService.getANCVisits(null, 0, 0, 12);
        for (ANCVisit ancVisit : ancVisits) {
            int age = ancVisit.getAge();
            int gestAge = ancVisit.getGestationalAge();
            if (age < 15 && gestAge >= 8 && gestAge <= 12) {
                count++;
            }
        }
        return count;
    }*/
}
