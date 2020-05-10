package com.rhyzue.motion.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rhyzue.motion.R

class GoalsFragment : Fragment() {

    private lateinit var goalsViewModel: GoalsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        goalsViewModel =
                ViewModelProviders.of(this).get(GoalsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_goals, container, false)

        goalsViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        val goals_filter : Spinner = root.findViewById(R.id.goals_filter)
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.goals_filter,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                goals_filter.adapter = adapter
            }
        }

        goals_filter.setSelection(0)
        println("DJKAHD")
        return root


    }
}
