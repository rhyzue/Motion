package com.rhyzue.motion.ui.schedule.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.data.Type
import androidx.lifecycle.Observer
import java.util.*

class AddTaskDialog : DialogFragment(){

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

        dialog.findViewById<Button>(R.id.select_deadline_btn).setOnClickListener { showDatePicker() }
    }

    private fun onSubmit(){
        val dialog = requireDialog()

        val name: String = dialog.findViewById<EditText>(R.id.task_name_editText).text.toString()
        val type_name: String = dialog.findViewById<Spinner>(R.id.types_spinner).selectedItem.toString()

        val deadline: Date? = null
        val complete: Boolean = dialog.findViewById<CheckBox>(R.id.complete_checkbox).isChecked

        val type_pkey = types.find{ x -> x.name==type_name}?.id

        val task = type_pkey?.let { Task(name=name,type = it,goal_id = 0,date_assigned = Date(),complete=complete,deadline=deadline) }

        if (task != null) {
            viewModel.insertTask(task)
        }
    }

    private fun showDatePicker(){
        val dateTimePicker = DateTimePickerDialog()
        dateTimePicker.show(childFragmentManager, "dateTimePicker")
    }

    private fun hideSoftKeyboard(view: View){
        context?.let{
            val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
