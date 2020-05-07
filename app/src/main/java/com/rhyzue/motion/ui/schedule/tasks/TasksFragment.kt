package com.rhyzue.motion.ui.schedule.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rhyzue.motion.R

class TasksFragment : Fragment() {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private lateinit var viewModel: TasksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tasks_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.task_recycler)
        val contx = context
        if(contx!=null) {
            val adapter = TaskListAdapter(contx, this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(contx)

            viewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
            viewModel.allTasks.observe(viewLifecycleOwner, Observer { tasks ->
                tasks?.let { adapter.setTasks(it) }
            })
        }

        return view
    }

    fun onViewTask(id: Int){
        val dialog = EditTaskDialog()
        val bundle: Bundle = Bundle()

        bundle.putInt("TASK_ID", id)
        dialog.arguments = bundle

        dialog.show(childFragmentManager, "addTask")
    }

}
