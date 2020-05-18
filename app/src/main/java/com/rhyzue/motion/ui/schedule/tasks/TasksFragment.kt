package com.rhyzue.motion.ui.schedule.tasks

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.data.Type
import com.rhyzue.motion.ui.schedule.day.DayFragment
import org.apache.commons.lang3.ClassUtils.getPackageName
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class TasksFragment : Fragment(), ConfirmDialog.ConfirmDialogListener {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private lateinit var viewModel: TasksViewModel
    private lateinit var  dayFrag: DayFragment

    override fun onConfirmDialogPositiveClick(dialog: DialogFragment, option: String, taskId: Int){
        val task = viewModel.getTaskById(taskId)
        when(option){
            "DELETE" -> viewModel.removeTask(taskId)
            "COMPLETE","INCOMPLETE" -> {
                val newTask = Task(id=task.id,
                    name=task.name,
                    type_id=task.type_id,
                    date_assigned = task.date_assigned,
                    complete=!task.complete,
                    deadline = task.deadline,
                    goal_id = task.goal_id,
                    starred = false
                )
                viewModel.modifyTask(newTask)
            }
        }
    }

    override fun onConfirmDialogNegativeClick(dialog: DialogFragment, option: String, taskId: Int){
        val task = viewModel.getTaskById(taskId)
        viewModel.modifyTask(task)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        dayFrag = parentFragment as DayFragment

        val recyclerView = view.findViewById<RecyclerView>(R.id.task_recycler)
        val contx = context
        if(contx!=null) {
            val adapter = TaskListAdapter(contx, this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(contx)

            viewModel = activity?.let { ViewModelProvider(it).get(TasksViewModel::class.java) }!!
            arguments?.let{ x ->
                val date = x.getSerializable("DATE") as Date?
                if (date != null) {
                    println("DATE")
                    println(date)
                    viewModel.onSwitchDay(date)
                }
            }
            activity?.let {
                viewModel.todayTasks.observe(it, Observer{ tasks ->
                    tasks?.let { t-> adapter.setTasks(t)
                        val unfinished: Int = t.count { c -> !c.complete }
                        val finished: Int = t.count{ c-> c.complete}
                        dayFrag.setTaskCount(unfinished, finished)
                    }
                })
            }
        }

        return view
    }

    fun onEditTask(id: Int){
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

    fun onCompleteTask(id: Int){
        val task = viewModel.getTaskById(id)
        if(task!=null){
            val complete = task.complete
            val dialog = ConfirmDialog()
            val bundle = Bundle()
            bundle.putString("CONFIRM_MESSAGE", "Mark task as "+ if(complete) "Incomplete" else "Complete" + "?")
            bundle.putString("OPTION", "COMPLETE")
            bundle.putInt("TASK_ID", id)
            dialog.arguments = bundle
            dialog.setTargetFragment(this,0)

            dialog.show(parentFragmentManager, "confirmComplete")
        }
    }

    fun onSwitchDay(day: Date){
        viewModel.onSwitchDay(day)
    }

    fun onStarTask(id: Int){
        val t: Task = viewModel.getTaskById(id)
        t.starred = !t.starred

        viewModel.modifyTask(t)
    }

    fun getTypeColor(taskId: Int): ColorStateList{
        val task: Task = viewModel.getTaskById(taskId)
        val type: Type = viewModel.getTypeById(task.type_id)
        return ColorStateList.valueOf(ResourcesCompat.getColor(resources,
            resources.getIdentifier(type.color, "color", context?.packageName),null))
    }

}
