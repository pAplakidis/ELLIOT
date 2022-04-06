package com.example.elliot.ui.statistics

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.adapter.StatsAdapter
import com.example.elliot.domain.model.Statistic
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private lateinit var pieChart: PieChart
    private val statisticsViewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart = view.findViewById(R.id.pie_chart)
        initPieChart()

        setButtonListeners(view)

        val calendarButton = view.findViewById<ImageButton>(R.id.calendar_button)
        calendarButton.setOnClickListener {
            initializeCalendar()
        }

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/$month/$year - $day/$month/$year"

        statisticsViewModel.onEvent(StatisticsEvent.OnDateChoose(date))

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.dateState.collect {
                    setDataToPieChart(it.pieValues)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.statState.collect {
                    val recyclerView: RecyclerView = view.findViewById(R.id.list_information)
                    initRecycler(recyclerView, it.statList)
                }
            }
        }

    }

    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true
    }

    private fun pieSettings(data: PieData, dataSet: PieDataSet) {
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#4DD0E1"))
        colors.add(Color.parseColor("#FFF176"))
        colors.add(Color.parseColor("#FF8A65"))

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.textColor = context?.let { ContextCompat.getColor(it, R.color.pie_labels) }!!

        pieChart.invalidate()
    }

    private fun setDataToPieChart(stat: List<Float>) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()

        dataEntries.add(PieEntry(stat[0], "Proteins"))
        dataEntries.add(PieEntry(stat[1], "Carbs"))
        dataEntries.add(PieEntry(stat[2], "Fat"))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        pieSettings(data, dataSet)
    }

    private fun initRecycler(recyclerView: RecyclerView, statList: Statistic) {
        recyclerView.apply {
            adapter = StatsAdapter(statList)
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

    private fun initializeCalendar() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setCalendarConstraints(constraintsCalendar().build())
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .setTheme(R.style.ThemeOverlay_Elliot_DatePicker)
                .build()

        dateRangePicker.show(childFragmentManager, "tag")

        dateRangePicker.addOnPositiveButtonClickListener {
            Log.d("TAG", dateRangePicker.headerText) // Month Day - Month Day
            statisticsViewModel.onEvent(StatisticsEvent.OnDateChoose(dateRangePicker.headerText))
        }

    }

    private fun setButtonListeners(view: View) {
        val buttonContainer = view.findViewById<MaterialButtonToggleGroup>(R.id.buttonContainer)
        buttonContainer.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.button1 -> {
                        statisticsViewModel.onEvent(StatisticsEvent.OnStatLoad("Breakfast"))
                    }

                    R.id.button2 -> {
                        statisticsViewModel.onEvent(StatisticsEvent.OnStatLoad("Lunch"))
                    }

                    R.id.button3 -> {
                        statisticsViewModel.onEvent(StatisticsEvent.OnStatLoad("Dinner"))
                    }

                }

            }
        }
    }

}