package com.example.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.domain.model.FoodModel

class FoodsAdapter(
    private val foodModels: List<FoodModel>,
) : RecyclerView.Adapter<FoodsAdapter.FoodsViewHolder>() {

    class FoodsViewHolder(foodDetailsView: View) : RecyclerView.ViewHolder(foodDetailsView) {
        val foodName: TextView = foodDetailsView.findViewById(R.id.food_name)
        val timeEaten: TextView = foodDetailsView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsViewHolder {
        val foodDetailsLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_foods, parent, false)

        return FoodsViewHolder(foodDetailsLayout)
    }

    override fun onBindViewHolder(holder: FoodsViewHolder, position: Int) {
        holder.apply {
            foodName.text = foodModels[position].foodName
            timeEaten.text = foodModels[position].foodDate
        }
    }

    override fun getItemCount(): Int = foodModels.size

}