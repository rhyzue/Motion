package com.rhyzue.motion.ui.schedule.tasks

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.data.Type
import androidx.lifecycle.Observer
import java.util.*

class AddTaskFragment : DialogFragment(){

    private var types = emptyList<Type>()
    private lateinit var viewModel: TasksViewModel
    private lateinit var types_adapter: ArrayAdapter<String>
    private lateinit var fragment: DialogFragment

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
        fragment = this

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.add_task_fragment, null))
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, _ ->
                        onSubmit()
                        dialog.cancel()
                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart(){
        super.onStart()

        val dialog = requireDialog()

        val editText: EditText = dialog.findViewById(R.id.task_name_editText)
        editText.setOnFocusChangeListener{v, hasFocus -> if(!hasFocus){hideSoftKeyboard(v)}}

        //create goals spinner
        val goals_spinner: Spinner = dialog.findViewById(R.id.goals_spinner)
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.goals_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                goals_spinner.adapter = adapter
            }
        }
        goals_spinner.setSelection(0)

        //create goals spinner
        val types_spinner: Spinner = dialog.findViewById(R.id.types_spinner)
        context?.let {
            types_adapter = ArrayAdapter(it,android.R.layout.simple_spinner_item,types.map{t-> t.name })
                .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                types_spinner.adapter = adapter
            }
        }

        viewModel.allTypes.observe(fragment, Observer { t ->
            t?.let {
                types = t
                types_adapter.clear()
                types_adapter.addAll(t.map{x->x.name})
            }
        })
        types_spinner.setSelection(0)
    }

    private fun onSubmit(){
        val dialog = requireDialog()

        val name: String = dialog.findViewById<EditText>(R.id.task_name_editText).text.toString()
        val deadline: Date? = null
        val complete: Boolean = dialog.findViewById<CheckBox>(R.id.complete_checkbox).isChecked

        val task = Task(name=name,type=0,goal_id = 0,date_assigned = Date(),complete=complete,deadline=deadline)
        println("SUBMIT "+task.name)
    }

    private fun hideSoftKeyboard(view: View){
        context?.let{
            val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
