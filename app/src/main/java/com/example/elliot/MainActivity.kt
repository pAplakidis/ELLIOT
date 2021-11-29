package com.example.elliot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.adapter.MealAdapter
import com.example.elliot.data.Datasource
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragmet_food_details)

        val myDataset = Datasource().loadMeals()
        val recyclerView: RecyclerView = findViewById(R.id.meal_recycler)
//        val divider = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)

        recyclerView.adapter = MealAdapter(myDataset)
//        recyclerView.addItemDecoration(divider)
        recyclerView.setHasFixedSize(true)

//        val today = MaterialDatePicker.todayInUtcMilliseconds()
//        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//
//        calendar.timeInMillis = today
//        calendar[Calendar.MONTH] = Calendar.JANUARY
//        val janThisYear = calendar.timeInMillis
//
//        calendar.timeInMillis = today
//        calendar[Calendar.MONTH] = Calendar.DECEMBER
//        val decThisYear = calendar.timeInMillis
//
//        // Build constraints.
//        val constraintsBuilder =
//            CalendarConstraints.Builder()
//                .setStart(janThisYear)
//                .setEnd(decThisYear)
//
//        val datePicker =
//            MaterialDatePicker.Builder.datePicker()
//                .setTitleText("Select date")
//                .setCalendarConstraints(constraintsBuilder.build())
//                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//                .setTheme(R.style.ThemeOverlay_Elliot_DatePicker)
//                .build()
//
//        datePicker.show(supportFragmentManager, "tag")

    }
}