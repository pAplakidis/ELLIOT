package com.example.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.model.FoodDetail

class FoodDetailsAdapter(
    private val foodDetails: List<FoodDetail>
) : RecyclerView.Adapter<FoodDetailsAdapter.FoodDetailsViewHolder>() {

    class FoodDetailsViewHolder(foodDetailsView: View) : RecyclerView.ViewHolder(foodDetailsView) {
        val foodName: TextView = foodDetailsView.findViewById(R.id.food_name)
        val time: TextView = foodDetailsView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodDetailsViewHolder {
        val foodDetailsLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_food_details, parent, false)

        return FoodDetailsViewHolder(foodDetailsLayout)
    }

    override fun onBindViewHolder(holder: FoodDetailsViewHolder, position: Int) {
        holder.apply {
            foodName.text = foodDetails[position].foodName
            time.text = foodDetails[position].time
        }
    }

    override fun getItemCount(): Int = foodDetails.size
}