package com.iprism.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.elliot.R
import com.iprism.elliot.domain.model.SubStatistic

class StatsAdapter(
    private val statistics: List<SubStatistic>
) : RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    class StatsViewHolder(statsView: View) : RecyclerView.ViewHolder(statsView) {
        val listStatistics: RecyclerView = statsView.findViewById(R.id.list_statistic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val statsLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_statistics, parent, false)

        return StatsViewHolder(statsLayout)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.apply {
            listStatistics.adapter = SubStatsAdapter(statistics)
        }
    }

    override fun getItemCount(): Int = 1
}
