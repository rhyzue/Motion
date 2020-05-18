package com.rhyzue.motion.ui.schedule.month

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.data.Type
import com.rhyzue.motion.ui.schedule.ScheduleFragment
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class MonthFragment : Fragment() {

    companion object {
        fun newInstance() = MonthFragment()
    }

    private lateinit var viewModel: MonthViewModel
    private lateinit var calendarView: CalendarView
    private lateinit var noTaskView: TextView
    private lateinit var navToDayView: ImageButton
    private var curDate: Date = Date()
    private lateinit var navBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_month, container, false)
        calendarView = view.findViewById(R.id.month_calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth -> onDateSelected( year, month, dayOfMonth) }
        navToDayView = view.findViewById(R.id.nav_to_dayView)
        navToDayView.setOnClickListener { navToDayView(curDate) }
        noTaskView = view.findViewById(R.id.no_tasks_view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.task_recycler_mini)

        val contx = context
        if(contx!=null) {
            val adapter = TaskAdapterMini(contx, this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(contx)

            viewModel = activity?.let { ViewModelProvider(it).get(MonthViewModel::class.java) }!!
            viewModel.onSwitchDay(Date())

            activity?.let {
                viewModel.todayTasks.observe(it, Observer{ tasks: List<Task> ->
                    tasks.let { t->
                        adapter.setTasks(t)
                        if(t.isEmpty()){
                            noTaskView.visibility=View.VISIBLE
                        }
                        else{
                            noTaskView.visibility=View.GONE
                        }
                        //val unfinished: Int = t.count { c -> !c.complete }
                        //val finished: Int = t.count{ c-> c.complete}
                        //setTaskCount(unfinished, finished)
                    }
                })
            }
        }
        return view
    }

    override fun onResume(){
        super.onResume()
        viewModel.onSwitchDay(Date())
        calendarView.setDate(Calendar.getInstance().timeInMillis, true, true)
    }

    private fun onDateSelected( year:Int, month: Int, dayOfMonth: Int){
        val c: Calendar = Calendar.getInstance()
        c.set(year, month, dayOfMonth)
        viewModel.onSwitchDay(c.time)
        curDate = c.time
    }

    fun getTypeColor(taskId: Int): ColorStateList {
        val task: Task = viewModel.getTaskById(taskId)
        val type: Type = viewModel.getTypeById(task.type_id)
        return ColorStateList.valueOf(
            ResourcesCompat.getColor(resources,
            resources.getIdentifier(type.color, "color", context?.packageName),null))
    }

    private fun navToDayView(day: Date){
        val scheduleFrag = parentFragment as ScheduleFragment
        scheduleFrag.navToDayView(day)
    }

    private fun setTaskCount(unfinished: Int, finished: Int){
        val scheduleFrag = parentFragment as ScheduleFragment
        scheduleFrag.setTaskCount(unfinished, finished)
    }

}
