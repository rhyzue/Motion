package com.rhyzue.motion.ui.schedule.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.rhyzue.motion.R

class AddTaskFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.add_task_fragment, null))
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart(){
        super.onStart()

        val dialog = requireDialog()

        //create spinner
        val spinner: Spinner = dialog.findViewById(R.id.types_spinner)
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.types_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
        spinner ?. let {spinner.onItemSelectedListener = this}


    }


    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        println("selected type")
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        println("didn't select type")
    }
}
