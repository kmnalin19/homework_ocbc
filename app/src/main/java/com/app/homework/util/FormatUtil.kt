package com.app.homework.util

import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object FormatUtil {

    fun doubleToStringNoDecimal(d: Double): String? {
        val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###.##")
        return "SGD "+ formatter.format(d)
    }

    fun getDisplayDateString(date : String) : String{

        return try {
            val dateArray = date.split("-")
            dateArray[0] + " " +
                    DateFormatSymbols(Locale.ENGLISH).months[dateArray[1].toInt() - 1].substring(
                        0,
                        3
                    ).toUpperCase() + " " +
                    dateArray[2]
        } catch (e : Exception){
            date
        }
    }
}