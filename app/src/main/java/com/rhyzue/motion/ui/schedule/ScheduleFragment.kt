package com.rhyzue.motion.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.rhyzue.motion.R
import com.rhyzue.motion.ui.schedule.day.DayFragment
import com.rhyzue.motion.ui.schedule.month.*
import com.rhyzue.motion.ui.schedule.week.WeekFragment
import kotlinx.android.synthetic.main.fragment_schedule.view.*

class ScheduleFragment : Fragment() {

    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        scheduleViewModel =
                ViewModelProvider(this).get(ScheduleViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)


        view.radio_month.setOnClickListener{view -> onScheduleTypeChange(view)}
        view.radio_week.setOnClickListener{view -> onScheduleTypeChange(view)}
        view.radio_day.setOnClickListener{view -> onScheduleTypeChange(view)}

        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.replace(R.id.calendar_container, MonthFragment())
        ft.commit()

        return view
    }

    private fun onScheduleTypeChange(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            val ft: FragmentTransaction = childFragmentManager.beginTransaction()

            when (view.getId()) {
                R.id.radio_month ->
                    if (checked) {
                        //display month view
                        ft.replace(R.id.calendar_container, MonthFragment())
                        ft.commit()
                    }
                R.id.radio_week ->
                    if (checked) {
                        //display week view
                        ft.replace(R.id.calendar_container, WeekFragment())
                        ft.commit()
                    }
                R.id.radio_day ->
                    if (checked) {
                        //display day view
                        ft.replace(R.id.calendar_container, DayFragment())
                        ft.commit()
                    }
            }
        }
        else{
            Log.i("LOG", "NOT RADIOBUTTON")
            println("NOT RADIOBUTTON")
        }
    }
}
