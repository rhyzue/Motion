package com.rhyzue.motion.ui.schedule.day

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider

import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.ui.schedule.tasks.AddTaskFragment
import com.rhyzue.motion.ui.schedule.tasks.TasksFragment
import kotlinx.android.synthetic.main.day_fragment.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DayFragment : Fragment() {

    companion object {
        fun newInstance() = DayFragment()
    }

    private lateinit var viewModel: DayViewModel
    private lateinit var day: LocalDateTime
    private lateinit var dateTextView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.day_fragment, container, false)
        day = LocalDateTime.now()

        view.add_task_button.setOnClickListener{onAddTask()}
        view.btn_prev_day.setOnClickListener{onSwitchDay("prev")}
        view.btn_next_day.setOnClickListener{onSwitchDay("next")}
        dateTextView = view.findViewById(R.id.date_textView)
        var format = DateTimeFormatter.ofPattern("dd MMM uuuu")
        dateTextView.text=day.format(format)


        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.replace(R.id.tasks_container, TasksFragment())
        ft.commit()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DayViewModel::class.java)
    }

    private fun onAddTask(){
        val dialog = AddTaskFragment()
        dialog.show(childFragmentManager, "addTask")



        println("Adding task")
        var format = SimpleDateFormat("yyyy-mm-dd")
        var task = Task(name="dayInsertTest",
                        type=1,
                        date_assigned = format.parse("2020-04-05"),
                        complete=false,
                        deadline=null,
                        auto_push = false,
                        goal_id=null)

        viewModel.insert(task)
        val tasks: List<Task>? = viewModel.allTasks.value

        if (tasks != null) {
            for(task in tasks){
                println(task.name)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onSwitchDay(option: String){
        if(option=="prev"){
           day =  day.minusDays(1)
        }
        if(option=="next"){
            day =  day.plusDays(1)
        }
        dateTextView.text = day.toString()
    }

}
