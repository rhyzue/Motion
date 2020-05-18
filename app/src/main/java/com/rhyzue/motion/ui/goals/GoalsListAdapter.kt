package com.rhyzue.motion.ui.goals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rhyzue.motion.R
import com.rhyzue.motion.data.Goal

class GoalListAdapter internal constructor(
    context: Context,
    fragment: GoalsFragment
) : RecyclerView.Adapter<GoalListAdapter.GoalViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tasks = emptyList<Goal>()
    private val parent = fragment

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalItemView: TextView = itemView.findViewById(R.id.)
        val expandGoalBtn: ImageButton = itemView.findViewById(R.id.downDrop_btn)
        val removeGoalBtn: ImageButton = itemView.findViewById(R.id.)
        val moreInfo: ImageButton = itemView.findViewById(R.id.)
        val completeIndicator: View = itemView.findViewById(R.id.goal_status)
    }

}