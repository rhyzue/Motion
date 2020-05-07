package com.rhyzue.motion.ui.schedule.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.data.Type
import kotlinx.android.synthetic.main.add_task_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class EditTaskDialog : DialogFragment(), DateTimePickerDialog.DateTimeDialogListener{

    private val dateTimePicker = DateTimePickerDialog()
    private var types = emptyList<Type>()
    private var deadline: Date? = null
    private lateinit var viewModel: TasksViewModel
    private lateinit var typesAdapter: ArrayAdapter<String>
    private lateinit var fragment: DialogFragment
    private lateinit var deadlineTextView: TextView
    private val df: SimpleDateFormat = SimpleDateFormat("MMM dd yyyy hh:mm aa")
    private var taskId: Int? = null

    private lateinit var task: Task

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDateTimeDialogPositiveClick(dialog: DialogFragment){
        deadline = dateTimePicker.ddate
        setDeadline()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
        fragment = this

        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            arguments?.let{ x ->
                taskId = x.getInt("TASK_ID")
                task = viewModel.getTaskById(taskId!!)!!
            }
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

    //set the data for all the components
    override fun onStart(){
        super.onStart()

        val dialog = requireDialog()

        dialog.title_textView.text = "Edit Task"

        val editText: EditText = dialog.findViewById(R.id.task_name_editText)
        editText.setText(task.name)
        editText.setOnFocusChangeListener{v, hasFocus -> if(!hasFocus){hideSoftKeyboard(v)}}

        //create goals spinner
        val goalsSpinner: Spinner = dialog.findViewById(R.id.goals_spinner)
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.goals_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                goalsSpinner.adapter = adapter
            }
        }
        goalsSpinner.setSelection(task.goal_id)

        //create goals spinner
        val typesSpinner: Spinner = dialog.findViewById(R.id.types_spinner)
        context?.let {
            typesAdapter = ArrayAdapter(it,android.R.layout.simple_spinner_item,types.map{t-> t.name })
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    typesSpinner.adapter = adapter
                }
        }

        viewModel.allTypes.observe(fragment, Observer { t ->
            t?.let {
                types = t
                typesAdapter.clear()
                typesAdapter.addAll(t.map{x->x.name})
            }
        })

        types.find { t -> t.id == task.type }?.id?.let { typesSpinner.setSelection(it) }

        dialog.findViewById<ImageButton>(R.id.select_deadline_btn).setOnClickListener { showDatePicker() }

        deadlineTextView = dialog.findViewById(R.id.deadline_textView)
        if(task.deadline==null)
            deadlineTextView.text = "No Deadline"
        else
            deadlineTextView.text = df.format(task.deadline)

        dialog.complete_checkbox.isChecked = task.complete
    }


    private fun onSubmit(){

        val dialog = requireDialog()

        val name: String = dialog.findViewById<EditText>(R.id.task_name_editText).text.toString()
        val typeName: String = dialog.findViewById<Spinner>(R.id.types_spinner).selectedItem.toString()

        val complete: Boolean = dialog.findViewById<CheckBox>(R.id.complete_checkbox).isChecked

        val typePkey = types.find{ x -> x.name==typeName}?.id

        if(typePkey!=null){
            val newTask = Task(id=task.id,name=name,type = typePkey,goal_id = 0,date_assigned = task.date_assigned,complete=complete,deadline=deadline)
            viewModel.modifyTask(newTask)
        }

    }

    private fun showDatePicker(){
        dateTimePicker.setTargetFragment(this,0)
        parentFragmentManager?.let { dateTimePicker.show(it, "dateTimePicker") }

    }

    private fun setDeadline(){

        if (deadline != null) {
            deadlineTextView.text="Deadline: "+df.format(deadline)
        }
        else{
            deadlineTextView.text="No Deadline"
        }
    }

    private fun hideSoftKeyboard(view: View){
        context?.let{
            val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
