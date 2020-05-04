package com.rhyzue.motion.ui.schedule.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.rhyzue.motion.R
import kotlinx.android.synthetic.main.fragment_date_time_picker.*
import java.util.*


class DateTimePickerDialog() : DialogFragment() {
   lateinit var listener: DateTimeDialogListener
    var ddate: Date? = null

    interface DateTimeDialogListener {
        fun onDateTimeDialogPositiveClick(dialog: DialogFragment)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = targetFragment as DateTimeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement DateTimeDialogListener"))
        }
    }

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
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onOk(){
        val dialog = requireDialog()
        val datePicker = dialog.date_picker
        val timePicker = dialog.time_picker

        val date: Calendar = GregorianCalendar(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth,
            timePicker.hour,
            timePicker.minute
        )

        ddate = date.time
        listener.onDateTimeDialogPositiveClick(this)
    }

}
