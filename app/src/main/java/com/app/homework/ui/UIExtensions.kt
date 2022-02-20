package com.app.homework.ui

import androidx.appcompat.widget.AppCompatButton
import com.app.homework.R

internal fun AppCompatButton.enableButton(){
    isEnabled = true
    setBackgroundColor(resources.getColor(R.color.color_blue))
}


internal fun AppCompatButton.disableButton(){
    isEnabled = false
    setBackgroundColor(resources.getColor(R.color.cardview_shadow_start_color))
}