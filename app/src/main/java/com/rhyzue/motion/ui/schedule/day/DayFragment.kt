package com.rhyzue.motion.ui.schedule.day

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider

import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task
import kotlinx.android.synthetic.main.day_fragment.view.*
import java.text.SimpleDateFormat

class DayFragment : Fragment() {

    companion object {
        fun newInstance() = DayFragment()
    }

    private lateinit var viewModel: DayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.day_fragment, container, false)
        view.add_task_button.setOnClickListener{onAddTask()}
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DayViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun onAddTask(){
        println("Adding task")
        var format = SimpleDateFormat("yyyy-mm-dd")
        var task = Task(null,"test", 1, format.parse("2020-04-05"), false, null, false, null)
        viewModel.insert(task)
        val tasks: List<Task>? = viewModel.allTasks.value

        if (tasks != null) {
            for(task in tasks){
                println(task.name)
            }
        }

    }

}
