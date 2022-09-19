package com.iprism.elliot.ui.statistics

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iprism.elliot.R
import com.iprism.elliot.adapter.SubStatsAdapter
import com.iprism.elliot.domain.model.SubStatistic
import dagger.hilt.android.AndroidEntryPoint
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
        val cameraButton = activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)
        cameraButton?.visibility = View.GONE

        initPieChart()

        setButtonListeners(view)

        val calendarButton = view.findViewById<ImageButton>(R.id.calendar_button)
        calendarButton.setOnClickListener {
            initializeCalendar(view)
        }

        statisticsViewModel.onEvent(StatisticsEvent.OnDateChoose)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.dateState.collect {
                    for (value in it.pieValues) {
                        if (value > 0) {
                            setDataToPieChart(it.pieValues)
                            break
                        } else {
                            pieChart.clear()
                            pieChart.invalidate()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.statState.collect {
                    val recyclerView: RecyclerView = view.findViewById(R.id.list_information)
                    val recycleDataset = listOf(
                        SubStatistic(getString(R.string.proteins), it.statList.protein),
                        SubStatistic(getString(R.string.fat), it.statList.fat),
                        SubStatistic(getString(R.string.carbs), it.statList.carbohydrate),
                        SubStatistic(getString(R.string.fiber), it.statList.fiber),
                        SubStatistic(getString(R.string.sodium), it.statList.sodium),
                        SubStatistic(getString(R.string.sugar), it.statList.sugar)
                    )
                    initRecycler(recyclerView, recycleDataset)
                }
            }
        }
    }

    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        // hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        pieChart.setNoDataText(getString(R.string.pie_notification))
        pieChart.setNoDataTextColor(Color.parseColor("#4E342E"))
        val paint: Paint = pieChart.getPaint(Chart.PAINT_INFO)
        paint.textSize = 50f
        // adding padding
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
        colors.add(Color.parseColor("#16BF55"))
        colors.add(Color.parseColor("#A416BF"))
        colors.add(Color.parseColor("#34C0EB"))

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(12f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.textColor =
            context?.let { ContextCompat.getColor(it, R.color.pie_labels) }!!
        pieChart.legend.isWordWrapEnabled = true
        pieChart.legend.yEntrySpace = 20f
        pieChart.setExtraOffsets(60f, 0f, 50f, 15f)
        pieChart.legend.textSize =
            13f // sets the size of the label text in density pixels min = 6f, max = 24f, default is 10f, font size will be in dp

        pieChart.invalidate()
    }

    private fun setDataToPieChart(stat: List<Float>) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()

        dataEntries.add(PieEntry(stat[0], getString(R.string.proteins)))
        dataEntries.add(PieEntry(stat[1], getString(R.string.fat)))
        dataEntries.add(PieEntry(stat[2], getString(R.string.carbs)))
        dataEntries.add(PieEntry(stat[3], getString(R.string.fiber)))
        dataEntries.add(PieEntry(stat[4], getString(R.string.sodium)))
        dataEntries.add(PieEntry(stat[5], getString(R.string.sugar)))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        pieSettings(data, dataSet)
    }

    private fun initRecycler(recyclerView: RecyclerView, statList: List<SubStatistic>) {
        recyclerView.apply {
            adapter = SubStatsAdapter(statList)
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

    private fun initializeCalendar(view: View) {
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
            view.findViewById<Button>(R.id.button1).performClick()
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