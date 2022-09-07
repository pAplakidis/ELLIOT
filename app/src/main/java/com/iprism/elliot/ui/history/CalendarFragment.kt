package com.iprism.elliot.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.elliot.adapter.FoodsAdapter
import com.iprism.elliot.domain.model.HistoryWithIngredientsModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iprism.elliot.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_foods) {
    private val calendarViewModel: CalendarViewModel by viewModels()

    private fun callRecycleView(
        recyclerView: RecyclerView,
        dataset: List<HistoryWithIngredientsModel>
    ) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL
                )
            )
            setHasFixedSize(true)
            adapter = FoodsAdapter(dataset)
        }
    }

    private fun showNavbar() {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar?.visibility = View.VISIBLE

        val cameraButton = activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)
        cameraButton?.visibility = View.VISIBLE
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
                .setTitleText(getString(R.string.select_date))
                .setCalendarConstraints(constraintsCalendar().build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(R.style.ThemeOverlay_Elliot_DatePicker)
                .build()

        datePicker.show(childFragmentManager, "tag")

        datePicker.addOnPositiveButtonClickListener {
            calendarViewModel.dateChosen = SimpleDateFormat("yyyy-MM-dd").format(Date(it))
            calendarViewModel.onEvent(CalendarEvent.OnDateChoose)
        }

        return datePicker
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showNavbar()

        val view = inflater.inflate(R.layout.fragment_foods, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.meal_recycler)
        recyclerView.alpha = calendarViewModel.recycledAlpha

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.oneTimeCardUiState.collect { cardUiState ->
                    if (cardUiState.data.isNotEmpty()) {
                        calendarViewModel.emptyHistoryAlpha = 0f
                        activity?.findViewById<TextView>(R.id.empty_history)?.alpha =
                            calendarViewModel.emptyHistoryAlpha

                        calendarViewModel.recycledAlpha = 1f
                        recyclerView.alpha = calendarViewModel.recycledAlpha

                        calendarViewModel.backgroundAlpha = 0

                        callRecycleView(recyclerView, cardUiState.data)
                    } else {
                        calendarViewModel.emptyHistoryAlpha = 1f
                        activity?.findViewById<TextView>(R.id.empty_history)?.alpha =
                            calendarViewModel.emptyHistoryAlpha

                        calendarViewModel.recycledAlpha = 0f
                        recyclerView.alpha = calendarViewModel.recycledAlpha

                        calendarViewModel.backgroundAlpha = 255
                        callRecycleView(recyclerView, emptyList())
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val recyclerView: RecyclerView = view.findViewById(R.id.meal_recycler)
        val calendarButton = view.findViewById<ImageButton>(R.id.calendar_button)
        calendarButton.setOnClickListener {
            initializeCalendar()
        }

        activity?.findViewById<TextView>(R.id.empty_history)?.alpha =
            calendarViewModel.emptyHistoryAlpha
//        recyclerView.alpha = calendarViewModel.recycledAlpha
//        recyclerView.background.alpha = calendarViewModel.backgroundAlpha

        if (calendarViewModel.dateChosen == "") {
            calendarViewModel.onEvent(CalendarEvent.OnHistoryLoad)
        } else {
            calendarViewModel.onEvent(CalendarEvent.OnDateChoose)
        }

//        lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                calendarViewModel.oneTimeCardUiState.collect { cardUiState ->
//                    if (cardUiState.data.isNotEmpty()) {
//                        calendarViewModel.emptyHistoryAlpha = 0f
//                        activity?.findViewById<TextView>(R.id.empty_history)?.alpha =
//                            calendarViewModel.emptyHistoryAlpha
//                        calendarViewModel.recycledAlpha = 1f
//                        recyclerView.alpha = calendarViewModel.recycledAlpha
//
//                        calendarViewModel.backgroundAlpha = 0
//                        recyclerView.background.alpha = calendarViewModel.backgroundAlpha
//
//                        callRecycleView(recyclerView, cardUiState.data)
//                    } else {
//                        calendarViewModel.emptyHistoryAlpha = 1f
//                        activity?.findViewById<TextView>(R.id.empty_history)?.alpha =
//                            calendarViewModel.emptyHistoryAlpha
//                        calendarViewModel.recycledAlpha = 0f
//                        recyclerView.alpha = calendarViewModel.recycledAlpha
//
//                        calendarViewModel.backgroundAlpha = 255
//                        recyclerView.background.alpha = calendarViewModel.backgroundAlpha
//                    }
//                }
//            }
//        }
    }
}