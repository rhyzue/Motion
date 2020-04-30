package com.rhyzue.motion.ui.schedule.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.rhyzue.motion.R
import java.util.*


class DateTimePickerDialog : DialogFragment() {
    private var addTaskDialog = AddTaskDialog()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.fragment_date_time_picker,null))
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, _ ->
                        onOk()
                        dialog.cancel()
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, _ ->
                        addTaskDialog.setDeadline(null)
                        dialog.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onOk(){
        val dialogView = requireDialog()
        val datePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.time_picker)

        val date: Calendar = GregorianCalendar(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth,
            timePicker.hour,
            timePicker.minute
        )

        addTaskDialog.setDeadline(date.time)
    }
}
