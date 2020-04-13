package com.rhyzue.motion.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rhyzue.motion.R
import kotlinx.android.synthetic.main.fragment_schedule.view.*

class ScheduleFragment : Fragment() {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var childView: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        scheduleViewModel =
                ViewModelProvider(this).get(ScheduleViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        childView = inflater.inflate(R.layout.calendar_month, container, false)

        view.radio_month.setOnClickListener{view -> onScheduleTypeChange(view)}
        view.radio_week.setOnClickListener{view -> onScheduleTypeChange(view)}
        view.radio_day.setOnClickListener{view -> onScheduleTypeChange(view)}
        return view
    }

    private fun onScheduleTypeChange(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_month ->
                    if (checked) {
                        //display month view
                        Log.i("LOG", "button month selected")
                        println("MONTH")

                        var stub: ViewStub? = childView.findViewById(R.id.calendar_month)
                        if (stub != null) {
                            stub.layoutResource = R.layout.calendar_month
                        }
                        else{
                            println("NO STUB FOUND")
                        }
                        val inflated: View? = stub?.inflate()
                    }
                R.id.radio_week ->
                    if (checked) {
                        //display week view
                        Log.i("LOG", "button month selected")
                        println("WEEK")
                    }
                R.id.radio_day ->
                    if (checked) {
                        //display day view
                        Log.i("LOG", "button month selected")
                        println("DAY")
                    }
            }
        }
        else{
            Log.i("LOG", "NOT RADIOBUTTON")
            println("NOT RADIOBUTTON")
        }
    }
}
