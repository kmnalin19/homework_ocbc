package com.app.homework.ui.custom

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.app.homework.R

object  AcknowledgementDialog  {

    fun showDialog(activity: Activity?, msg: String?,isSuccess : Boolean) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.fund_transfer_confirmation_dialog)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        text.text = msg

        if (!isSuccess){
            dialog.findViewById<View>(R.id.dialog_layout).setBackgroundColor(Color.parseColor("#F65050"))
        }
        val dialogButton = dialog.findViewById<View>(R.id.btn_dialog) as Button
        dialogButton.setOnClickListener(){
            dialog.dismiss()
            activity.onBackPressed()
        }
        dialog.show()
    }
}
