package com.rhyzue.motion.ui.schedule.day

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.rhyzue.motion.R
import com.rhyzue.motion.ui.schedule.ScheduleFragment
import com.rhyzue.motion.ui.schedule.tasks.AddTaskDialog
import com.rhyzue.motion.ui.schedule.tasks.TasksFragment
import com.rhyzue.motion.ui.schedule.tasks.TasksViewModel
import kotlinx.android.synthetic.main.fragment_day.view.*
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DayFragment : Fragment() {

    companion object {
        fun newInstance() = DayFragment()
    }

    private lateinit var viewModel: DayViewModel
    private lateinit var tasksViewModel: TasksViewModel
    private var day: Date = Date()
    private lateinit var dateTextView: TextView
    private val c: Calendar = Calendar.getInstance();
    private val df: SimpleDateFormat = SimpleDateFormat("MMM dd yyyy")
    private lateinit var taskFragment: TasksFragment
    private lateinit var scheduleFrag: ScheduleFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        scheduleFrag = parentFragment as ScheduleFragment

        view.add_task_button.setOnClickListener{onAddTask()}
        view.btn_prev_day.setOnClickListener{onSwitchDay("prev")}
        view.btn_next_day.setOnClickListener{onSwitchDay("next")}
        dateTextView = view.findViewById(R.id.date_textView)
        dateTextView.text=df.format(day)

        tasksViewModel = activity?.let { ViewModelProvider(it).get(TasksViewModel::class.java) }!!

        taskFragment = TasksFragment()
        val bundle: Bundle = Bundle()
        bundle.putSerializable("DATE", day)
        taskFragment.arguments = bundle

        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.replace(R.id.tasks_container, taskFragment)
        ft.commit()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DayViewModel::class.java)
    }

    private fun onAddTask(){
        val dialog = AddTaskDialog()
        val bundle: Bundle = Bundle()
        bundle.putSerializable("DATE_ASSIGNED", day)
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "addTask")
    }


    private fun onSwitchDay(option: String){
        if(option=="prev"){
            c.time = day
            c.add(Calendar.DATE, -1)
            day = c.time
        }
        if(option=="next"){
            c.time = day
            c.add(Calendar.DATE, 1)
            day = c.time
        }
        dateTextView.text = df.format(day)
        taskFragment.onSwitchDay(day)
    }

    fun setDay(day: Date){
        this.day = day
    }

    fun setTaskCount(unfinished: Int, finished: Int){
        scheduleFrag.setTaskCount(unfinished, finished)
    }

}
