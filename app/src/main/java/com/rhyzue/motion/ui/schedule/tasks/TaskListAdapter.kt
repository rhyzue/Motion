package com.rhyzue.motion.ui.schedule.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task

class TaskListAdapter internal constructor(
    context: Context,
    fragment: TasksFragment
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tasks = emptyList<Task>()
    private val parent = fragment

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskItemView: TextView = itemView.findViewById(R.id.task_text)
        val viewTaskBtn: ImageButton = itemView.findViewById(R.id.edit_task_btn)
        val removeTaskBtn: ImageButton = itemView.findViewById(R.id.remove_task_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = inflater.inflate(R.layout.task_recycler_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = tasks[position]
        holder.taskItemView.text = current.name
        holder.viewTaskBtn.setOnClickListener { parent.onViewTask(current.id) }
        holder.removeTaskBtn.setOnClickListener { parent.onRemoveTask(current.id) }
    }

    internal fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun getItemCount() = tasks.size
}