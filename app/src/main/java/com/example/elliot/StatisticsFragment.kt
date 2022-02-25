package com.example.elliot

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.adapter.MealAdapter
import com.example.elliot.adapter.StatsAdapter
import com.example.elliot.data.Datasource
import com.example.elliot.model.Statistic
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

import com.google.android.material.button.MaterialButtonToggleGroup

class StatisticsFragment: Fragment(R.layout.fragment_statistics) {
    private lateinit var pieChart : PieChart
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart = view.findViewById(R.id.pie_chart)

        initPieChart()

        val buttonContainer = view.findViewById<MaterialButtonToggleGroup>(R.id.buttonContainer)
        buttonContainer.addOnButtonCheckedListener { _, checkedId, isChecked ->
            val recyclerView: RecyclerView = view.findViewById(R.id.list_information)

            if (isChecked) {
                lateinit var dataset : List<Statistic>
                when (checkedId) {
                    R.id.button1 -> {
                        dataset = Datasource().loadStatistics1()
                        setDataToPieChart(1)
                    }

                    R.id.button2 -> {
                        dataset = Datasource().loadStatistics2()
                        setDataToPieChart(2)
                    }

                    R.id.button3 -> {
                       dataset = Datasource().loadStatistics3()
                        setDataToPieChart(3)
                    }

                }
                recyclerView.apply {
                    adapter = StatsAdapter(dataset)
                    setHasFixedSize(true)
                }
            }
        }

    }

    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true
        pieChart.legend.textSize = 15f
    }

    private fun setDataToPieChart(choice: Int) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        when(choice) {
            1 -> {
                dataEntries.add(PieEntry(30f, "Test1"))
                dataEntries.add(PieEntry(40f, "Test2"))
                dataEntries.add(PieEntry(30f, "Test3"))
            }

            2 -> {
                dataEntries.add(PieEntry(70f, "New1"))
                dataEntries.add(PieEntry(10f, "New2"))
                dataEntries.add(PieEntry(20f, "New3"))
            }

            3 -> {
                dataEntries.add(PieEntry(85f, "Create1"))
                dataEntries.add(PieEntry(5f, "Create2"))
                dataEntries.add(PieEntry(10f, "Create3"))
            }
        }

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#4DD0E1"))
        colors.add(Color.parseColor("#FFF176"))
        colors.add(Color.parseColor("#FF8A65"))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

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
        pieChart.setHoleColor(Color.WHITE)


        //add text in center
        pieChart.setDrawCenterText(true)
        pieChart.centerText = "Mobile OS Market share"



        pieChart.invalidate()
    }
}