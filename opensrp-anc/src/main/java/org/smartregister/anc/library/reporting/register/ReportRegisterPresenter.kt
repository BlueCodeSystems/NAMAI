package org.smartregister.anc.library.reporting.register

import android.app.Activity
import android.content.Intent
import org.smartregister.anc.library.reporting.ReportGroup
import org.smartregister.anc.library.reporting.annual.AnnualReportActivity
import org.smartregister.anc.library.reporting.monthly.MonthlyReportsActivity
import java.lang.ref.WeakReference

class ReportRegisterPresenter(override val reportRegisterView: ReportRegisterContract.View) : ReportRegisterContract.Presenter {

    private val reportingActivity: WeakReference<Activity> = WeakReference<Activity>(reportRegisterView as Activity)

    override fun startReport(reportGroup: ReportGroup) {
        reportingActivity.get()?.let {
            when (reportGroup) {
                ReportGroup.MONTHLY_REPORTS -> it.startActivity(Intent(it, MonthlyReportsActivity::class.java))
                ReportGroup.ANNUAL_COVERAGE_REPORTS -> it.startActivity(Intent(it, AnnualReportActivity::class.java))
            }
        }
    }
}