package com.app.homework.ui.custom

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.app.homework.util.FormatUtil


class ThousandSeparatorTextWatcher(private val editText : EditText,
                                   private val accountBalance: String,
                                   private val listener : AmountFormatTextWatcherListener) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {

        try {
            if (p0.toString().isNotBlank() && p0.toString().length < 20) {
                val roundAmount = p0.toString().replace(",", "")
                if (roundAmount.toDouble() > accountBalance.toDouble())
                    listener.showInputError()
                else if(roundAmount.toDouble() == 0.00)
                    listener.showInvalidInputError()
                else
                    listener.showTransferButton()

                editText.removeTextChangedListener(this)
                var s = roundAmount.split(".")
                var s1 = ""
                s1 = FormatUtil.doubleToStringWithoutCurrency(s[0].toDouble())
                if (s.size > 1) {
                    if (s[1].length > 2) {
                        s1 = s1 + "." + s[1].substring(0, 2)
                    } else
                        s1 = s1 + "." + s[1]
                }

                editText.setText(s1)
                editText.setSelection(s1.length)
                editText.addTextChangedListener(this)
            }
            else{
                editText.removeTextChangedListener(this)
                listener.showInvalidInputError()
                editText.addTextChangedListener(this)
            }
        } catch (e: Exception) {
            listener.showInvalidInputError()
        }
    }

   interface AmountFormatTextWatcherListener{
       fun showInputError()
       fun showInvalidInputError()
       fun showTransferButton()
   }
}