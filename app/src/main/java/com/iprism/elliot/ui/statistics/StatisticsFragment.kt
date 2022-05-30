package com.iprism.elliot.ui.statistics

import android.graphics.Color
import android.os.Bundle
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
import com.iprism.elliot.R
import com.iprism.elliot.adapter.StatsAdapter
import com.iprism.elliot.domain.model.Statistic
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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

        val year = Calendar.getInstance().get(Calendar.YEAR).toString()
        var month = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()

        if (day.length < 2) {
            day = "0$day"
        }

        if (month.length < 2) {
            month = "0$month"
        }

        statisticsViewModel.dateStart = "$year-$month-$day"
        statisticsViewModel.dateEnd = "$year-$month-$day"

        statisticsViewModel.onEvent(StatisticsEvent.OnDateChoose)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.dateState.collect {
                    if (it.pieValues.isNotEmpty()) {
                        setDataToPieChart(it.pieValues)
                    }
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
        pieChart.legend.textColor =
            context?.let { ContextCompat.getColor(it, R.color.pie_labels) }!!

        pieChart.legend.textSize =
            16f //sets the size of the label text in density pixels min = 6f, max = 24f, default is 10f, font size will be in dp

        pieChart.invalidate()
    }

    private fun setDataToPieChart(stat: List<Float>) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()

        dataEntries.add(PieEntry(stat[0], getString(R.string.proteins)))
        dataEntries.add(PieEntry(stat[1], getString(R.string.carbs)))
        dataEntries.add(PieEntry(stat[2], getString(R.string.fat)))

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
                .setTitleText(getString(R.string.select_dates))
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
            statisticsViewModel.dateStart = SimpleDateFormat("yyyy-MM-dd").format(Date(it.first))
            statisticsViewModel.dateEnd = SimpleDateFormat("yyyy-MM-dd").format(Date(it.second))

            statisticsViewModel.onEvent(StatisticsEvent.OnDateChoose)
        }
    }

    private fun setButtonListeners(view: View) {
        val buttonContainer = view.findViewById<MaterialButtonToggleGroup>(R.id.buttonContainer)
        buttonContainer.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.button1 -> {
                        statisticsViewModel.onEvent(StatisticsEvent.OnStatLoad(getString(R.string.breakfast)))
                    }

                    R.id.button2 -> {
                        statisticsViewModel.onEvent(StatisticsEvent.OnStatLoad(getString(R.string.lunch)))
                    }

                    R.id.button3 -> {
                        statisticsViewModel.onEvent(StatisticsEvent.OnStatLoad(getString(R.string.dinner)))
                    }
                }
            }
        }
    }
}