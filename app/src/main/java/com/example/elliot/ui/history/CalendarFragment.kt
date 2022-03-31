package com.example.elliot.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.adapter.FoodsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_foods) {
//    motionLayout.getTransition(R.id.yourTransition).setEnable(false)
    //  scrollable cardview + design

    private val calendarViewModel: CalendarViewModel by viewModels()

    private fun callRecycleView(
        recyclerView: RecyclerView,
        dataset: CalendarViewModel.CardListPick
    ) {
        recyclerView.apply {
            adapter = FoodsAdapter(dataset)
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL
                )
            )
            setHasFixedSize(true)
        }
    }

    private fun showNavbar() {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar?.visibility = View.VISIBLE

        val cameraButton = activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)
        cameraButton?.visibility = View.VISIBLE
    }

//    private fun constraintsCalendar(): CalendarConstraints.Builder {
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
//        return CalendarConstraints.Builder()
//            .setStart(janThisYear)
//            .setEnd(decThisYear)
//    }

//    private fun initializeCalendar(): MaterialDatePicker<Long> {
//        val datePicker =
//            MaterialDatePicker.Builder.datePicker()
//                .setTitleText("Select date")
//                .setCalendarConstraints(constraintsCalendar().build())
//                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//                .setTheme(R.style.ThemeOverlay_Elliot_DatePicker)
//                .build()
//
//        datePicker.show(childFragmentManager, "tag")
//
//        return datePicker
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        showNavbar()

        return inflater.inflate(R.layout.fragment_foods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.meal_recycler)

//        val datePicker = initializeCalendar()

        calendarViewModel.onEvent(CalendarEvent.OnHistoryLoad("baklava"))

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.cardUiState.collect {
                    callRecycleView(recyclerView, it)
                }
            }
        }
    }

}