package org.smartregister.anc.library.reporting.dropuout

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_report_register.*
import org.apache.commons.lang3.tuple.Triple
import org.smartregister.child.ChildLibrary
import org.smartregister.child.activity.BaseActivity
import org.smartregister.child.toolbar.LocationSwitcherToolbar
import org.smartregister.anc.library.R
import org.smartregister.anc.library.activity.ChildRegisterActivity
import org.smartregister.anc.library.application.ZeirApplication
import org.smartregister.anc.library.reporting.DropoutReportGroup
import org.smartregister.anc.library.reporting.DropoutReportGroupingModel
import org.smartregister.anc.library.reporting.dropuout.job.DropoutIntentServiceJob
import org.smartregister.anc.library.reporting.stock.job.StockSyncIntentServiceJob
import org.smartregister.anc.library.util.AppUtils
import org.smartregister.anc.library.view.NavigationMenu

class DropoutReportsActivity : BaseActivity(), DropoutReportContract.View, DropoutReportRegisterTypeAdapter.OnDropoutItemClick {

    private lateinit var reportRegisterPresenter: DropoutReportRegisterPresenter

    lateinit var navigationMenu: NavigationMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePresenter()
        navigationMenu = NavigationMenu.getInstance(this)
        reportRegisterListView.apply {
            val context = this@DropoutReportsActivity
            adapter = DropoutReportRegisterTypeAdapter(context, R.layout.dropout_reports_item,
                    DropoutReportGroupingModel(context).reportGroupings, context)
            divider = ColorDrawable(ContextCompat.getColor(context, R.color.light_grey))
            dividerHeight = 1
        }

        reportSyncBtn.apply {
            setOnClickListener {
                val allSharedPreferences = ZeirApplication.getInstance().context().allSharedPreferences()
                val reportJobExecutionTime = allSharedPreferences.getPreference("report_job_execution_time")
                if (reportJobExecutionTime.isBlank() || AppUtils.timeBetweenLastExecutionAndNow(30, reportJobExecutionTime)) {
                    allSharedPreferences.savePreference("report_job_execution_time", System.currentTimeMillis().toString())
                    DropoutIntentServiceJob.scheduleJobImmediately(DropoutIntentServiceJob.TAG)
                    StockSyncIntentServiceJob.scheduleJobImmediately(StockSyncIntentServiceJob.TAG)
                    Toast.makeText(context, "Reporting Job Has Started, It will take some time", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Reporting Job Has Already Been Started, Try again in 30 mins", Toast.LENGTH_LONG).show()
                }
            }
        }
        backButton.apply { setOnClickListener { navigationMenu.openDrawer() } }
    }

    public override fun getContentView() = R.layout.activity_dropout_reports

    public override fun getDrawerLayoutId() = navigationMenu.drawer.id

    override fun getToolbarId() = LocationSwitcherToolbar.TOOLBAR_ID

    @Suppress("UNCHECKED_CAST")
    public override fun onBackActivity(): Class<ChildRegisterActivity> {
        navigationMenu.drawer
        return ChildLibrary.getInstance().metadata().childRegisterActivity as Class<ChildRegisterActivity>
    }

    override fun onUniqueIdFetched(triple: Triple<String, Map<String, String>, String>, s: String) = Unit

    override fun onNoUniqueId() = Unit

    override fun onRegistrationSaved(b: Boolean) = Unit

    override fun onSyncStart() {
        super.onSyncStart()
        reportSyncBtn.visibility = View.GONE
        AppUtils.updateSyncStatus(false)
    }

    override fun initializePresenter() {
        reportRegisterPresenter = DropoutReportRegisterPresenter(this)
    }

    override fun onItemClick(group: DropoutReportGroup) {
        reportRegisterPresenter.startReport(group)
    }
}