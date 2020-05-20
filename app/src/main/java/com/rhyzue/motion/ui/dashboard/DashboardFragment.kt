package com.rhyzue.motion.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.pnikosis.materialishprogress.ProgressWheel
import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var dateTextView: TextView
    private lateinit var progressTextView: TextView
    private lateinit var progressBar: ProgressWheel
    private val df: SimpleDateFormat = SimpleDateFormat("MMM dd yyyy")

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val dashboard = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dateTextView = dashboard.findViewById(R.id.today_date)
        progressTextView = dashboard.findViewById(R.id.progress_text)
        progressBar = dashboard.findViewById(R.id.progressBar)

        dashboardViewModel.setTasks(Calendar.getInstance().time)
        dateTextView.text = df.format(Calendar.getInstance().time)

        activity?.let{
            dashboardViewModel.todayTasks.observe(it, Observer{ tasks: List<Task> ->
                tasks?.let { t->
                    if(t.isNotEmpty()){
                        val unfinished: Float = t.count { c -> !c.complete }.toFloat()
                        val finished: Float = t.count{ c-> c.complete}.toFloat()
                        val finPercent:Float = (finished/t.size)*100
                        progressTextView.text = finPercent.toInt().toString()+"% done \n"+unfinished.toInt().toString()+" tasks left"
                        progressBar.progress = finPercent/100
                    }
                    else{
                        progressBar.progress = 100F
                        progressTextView.text = "No Tasks Today"
                    }
                }
            })
        }
        return dashboard
    }

    override fun onStart() {
        super.onStart()
        dashboardViewModel.setTasks(Calendar.getInstance().time)
        dateTextView.text = df.format(Calendar.getInstance().time)
    }
}
