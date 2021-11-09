package com.example.todoapplication.util

import android.app.DatePickerDialog
import android.os.Build
import androidx.fragment.app.FragmentActivity
import java.util.*

class MyDatePickerDialog (
    // context needed to create the dialog
    activity: FragmentActivity,
    date:String,
    // callback for the caller of this dialog
    onShowDateClicked: (Int, Int, Int) -> Unit
) {
    private val calendar = Calendar.getInstance()
    val year: Int
    val month: Int
    val day: Int

    init {
        if(date.isEmpty()){
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }else {
            val oldDate = date.split("/").toTypedArray()
            year = oldDate[0].toInt()
            month = oldDate[1].toInt()
            day = oldDate[2].toInt()
        }
    }

    private val dialog = DatePickerDialog(activity,
        { _, year, month, day -> onShowDateClicked.invoke(year, month, day) }, year, month, day);

    fun show() {
        dialog.show()
    }
}