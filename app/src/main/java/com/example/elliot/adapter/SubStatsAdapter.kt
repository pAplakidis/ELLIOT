package com.example.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.domain.model.SubStatistic

class SubStatsAdapter(
    private val subStatistics: List<SubStatistic>
) : RecyclerView.Adapter<SubStatsAdapter.SubStatsViewHolder>() {

    class SubStatsViewHolder(SubStatsView: View) : RecyclerView.ViewHolder(SubStatsView) {
        val statName: TextView = SubStatsView.findViewById(R.id.stat_name)
        val statValue: TextView = SubStatsView.findViewById(R.id.stat_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubStatsViewHolder {
        val statsLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_substatistics, parent, false)

        return SubStatsViewHolder(statsLayout)
    }

    override fun onBindViewHolder(holder: SubStatsViewHolder, position: Int) {
        holder.apply {
            statName.text = subStatistics[position].statName
            statValue.text = subStatistics[position].statEval
        }
    }

    override fun getItemCount(): Int = subStatistics.size

}