package com.example.elliot.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private lateinit var pieChart: PieChart
    private val statisticsViewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO MAKE CALENDAR, PUT LISTENER AND PASS THE DATE CHOICE TO VIEWMODEL WITH SIMILAR TO 48 LINE CALL

        pieChart = view.findViewById(R.id.pie_chart)
        initPieChart()

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/$month/$year"

        statisticsViewModel.onEvent(StatisticsEvent.OnDateChoose(date))

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

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        //add text in center
        pieChart.setDrawCenterText(true)
        pieChart.centerText = "Macronutrients Information"

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
}