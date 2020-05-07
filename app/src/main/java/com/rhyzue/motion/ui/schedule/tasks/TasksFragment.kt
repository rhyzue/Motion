package com.rhyzue.motion.ui.schedule.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rhyzue.motion.R

class TasksFragment : Fragment(), ConfirmDialog.ConfirmDialogListener {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private lateinit var viewModel: TasksViewModel

    override fun onConfirmDialogPositiveClick(dialog: DialogFragment, option: String, taskId: Int){
        when(option){
            "DELETE" -> viewModel.removeTask(taskId)
            "COMPLETE" -> println("temp")
            "INCOMPLETE" -> println("temp")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
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
        val bundle = Bundle()

        bundle.putInt("TASK_ID", id)
        dialog.arguments = bundle

        dialog.show(childFragmentManager, "addTask")
    }

    fun onRemoveTask(id: Int){
        val dialog = ConfirmDialog()
        val bundle = Bundle()
        bundle.putString("CONFIRM_MESSAGE", "Delete Task?")
        bundle.putString("OPTION", "DELETE")
        bundle.putInt("TASK_ID", id)
        dialog.arguments = bundle
        dialog.setTargetFragment(this,0)

        dialog.show(parentFragmentManager, "confirmDelete")
    }

}
