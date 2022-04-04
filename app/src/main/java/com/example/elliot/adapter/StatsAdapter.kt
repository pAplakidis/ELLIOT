package com.example.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.domain.model.Statistic

class StatsAdapter(
    private val statistics: Statistic
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
            listStatistics.adapter = SubStatsAdapter(statistics.subStatList)
        }
    }

    override fun getItemCount(): Int = 1

}
