package com.rhyzue.motion.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val textView: TextView = root.findViewById(R.id.text_goals)
        goalsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
