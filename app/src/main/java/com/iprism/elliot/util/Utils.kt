package com.iprism.elliot.util

import java.util.Calendar

object Utils {
    fun Calendar.getDateAndTime(number: Int, isMonth: Boolean = false): String {
        var correctNumber = if (isMonth) {
            (this.get(number) + 1).toString()
        } else {
            this.get(number).toString()
        }

        if (correctNumber.length < 2) {
            correctNumber = "0$correctNumber"
        }

        return correctNumber
    }
}