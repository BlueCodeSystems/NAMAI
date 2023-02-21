package org.smartregister.anc.library.reporting.coverage

import org.smartregister.anc.library.reporting.CoverageReportGroup


/**
 * Contract for reporting
 */
interface CoverageReportContract {

    interface Presenter {
        /**
         * Start report for the provided [reportGroup] which can be of a coverage report
         *
         */
        fun startReport(reportGroup: CoverageReportGroup)

        val coverageReportRegisterView: View
    }

    interface View {
        /**
         * Initialize dropout reporting [Presenter]
         */
        fun initializePresenter()
    }
}