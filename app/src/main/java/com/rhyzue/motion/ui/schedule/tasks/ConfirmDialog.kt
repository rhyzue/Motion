package com.rhyzue.motion.ui.schedule.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.rhyzue.motion.R
import kotlinx.android.synthetic.main.dialog_date_time_picker.*
import java.util.*


class ConfirmDialog() : DialogFragment() {
    lateinit var listener: ConfirmDialogListener
    private var message: String = ""
    private var option: String = ""
    private var taskId: Int = -1

    interface ConfirmDialogListener {
        fun onConfirmDialogPositiveClick(dialog: DialogFragment, option: String, taskId: Int)
        fun onConfirmDialogNegativeClick(dialog: DialogFragment, option: String, taskId: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = targetFragment as ConfirmDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement ConfirmDialogListener"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            arguments?.let{ x ->
                message = x.getString("CONFIRM_MESSAGE").toString()
                option = x.getString("OPTION").toString()
                taskId = x.getInt("TASK_ID")

                builder.setMessage(message)
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { _, _ ->
                            if(taskId!=-1)
                                listener.onConfirmDialogPositiveClick(this, option, taskId)
                        })
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener { dialog, _ ->
                            if(taskId!=-1)
                                listener.onConfirmDialogNegativeClick(this, option, taskId)
                            dialog.cancel()
                        })
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
