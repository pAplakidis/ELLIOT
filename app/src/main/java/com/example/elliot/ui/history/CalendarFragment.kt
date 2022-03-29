package com.example.elliot.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.adapter.MealAdapter
import com.example.elliot.domain.model.CardModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_foods) {
    private val calendarViewModel: CalendarViewModel by viewModels()

    private fun callRecycleView(recyclerView: RecyclerView, cards: List<CardModel>) {
        recyclerView.apply {
            adapter = MealAdapter(cards)
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL
                )
            )
            setHasFixedSize(true)
        }
    }

    private fun constraintsCalendar(): CalendarConstraints.Builder {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.JANUARY
        val janThisYear = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val decThisYear = calendar.timeInMillis

        // Build constraints.
        return CalendarConstraints.Builder()
            .setStart(janThisYear)
            .setEnd(decThisYear)
    }

    private fun initializeCalendar(): MaterialDatePicker<Long> {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(constraintsCalendar().build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(R.style.ThemeOverlay_Elliot_DatePicker)
                .build()

        datePicker.show(childFragmentManager, "tag")

        return datePicker
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.meal_recycler)
        val datePicker = initializeCalendar()

        datePicker.addOnPositiveButtonClickListener {
            calendarViewModel.onEvent(CalendarEvent.OnDateClick(datePicker.headerText))
        }

        lifecycleScope.launch {
            calendarViewModel.cardUiState.collect { cardUiState ->
                when (cardUiState) {
                    is CalendarViewModel.CalendarUiState.CardListPick -> {
                        callRecycleView(recyclerView, cardUiState.cardsChosen)
//                        clickListener(recyclerView, 0)
                    }
                }
            }
        }
    }
}