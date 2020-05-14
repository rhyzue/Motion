package com.rhyzue.motion.ui.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.rhyzue.motion.R
import com.rhyzue.motion.ui.schedule.day.DayFragment
import com.rhyzue.motion.ui.schedule.month.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleFragment : Fragment() {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var curFragment: View
    private lateinit var finishedTasksCount: TextView
    private lateinit var unFinishedTasksCount: TextView
    private val monthFragment: MonthFragment = MonthFragment()
    private val dayFragment: DayFragment = DayFragment()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        scheduleViewModel =
                ViewModelProvider(this).get(ScheduleViewModel::class.java)
        curFragment = inflater.inflate(R.layout.fragment_schedule, container, false)

        finishedTasksCount = curFragment.findViewById(R.id.finishedTasks_count)
        unFinishedTasksCount = curFragment.findViewById(R.id.unfinishedTasks_count)

        curFragment.radio_month.setOnClickListener{v -> onScheduleTypeChange(v)}
        curFragment.radio_day.setOnClickListener{v -> onScheduleTypeChange(v)}

        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.replace(R.id.calendar_container, monthFragment)
        ft.commit()

        return curFragment
    }


    private fun onScheduleTypeChange(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            val ft: FragmentTransaction = childFragmentManager.beginTransaction()

            when (view.getId()) {
                R.id.radio_month ->
                    if (checked) {
                        //display month view
                        ft.replace(R.id.calendar_container, monthFragment)
                        ft.commit()
                    }
                R.id.radio_day ->
                    if (checked) {
                        //display day view
                        ft.replace(R.id.calendar_container, dayFragment)
                        ft.commit()
                    }
            }
        }
    }

    fun navToDayView(day: Date){
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.replace(R.id.calendar_container, dayFragment)
        ft.commit()

        val radioButton: RadioButton = curFragment.findViewById(R.id.radio_day)
        radioButton.isChecked = true
        dayFragment.setDay(day)
    }

    fun setTaskCount(unfinished: Int, finished: Int){
        unFinishedTasksCount.text = unfinished.toString()
        finishedTasksCount.text = finished.toString()
    }


}
