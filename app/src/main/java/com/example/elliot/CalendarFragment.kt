package com.example.elliot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.adapter.MealAdapter
import com.example.elliot.data.Datasource
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.TimeZone

class CalendarFragment: Fragment(R.layout.fragment_food_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myDataset = Datasource().loadMeals()
        val recyclerView: RecyclerView = view.findViewById(R.id.meal_recycler)
        val divider = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.JANUARY
        val janThisYear = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val decThisYear = calendar.timeInMillis

        // Build constraints.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(janThisYear)
                .setEnd(decThisYear)

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(R.style.ThemeOverlay_Elliot_DatePicker)
                .build()

        datePicker.show(childFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            recyclerView.apply {
                adapter = MealAdapter(myDataset)
                addItemDecoration(divider)
                setHasFixedSize(true)
            }
        }
    }
}