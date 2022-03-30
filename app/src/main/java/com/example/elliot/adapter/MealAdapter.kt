package com.example.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.domain.model.CardModel

class MealAdapter(
    private val dataset: List<CardModel>,
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    // Holds references for every view inside the layout we want to inflate
    class MealViewHolder(mealView: View) : RecyclerView.ViewHolder(mealView) {
        val mealTitle: TextView = mealView.findViewById(R.id.meal_title)
        val listFoodDetails: RecyclerView = mealView.findViewById(R.id.list_food_details)
    }

    // Creates the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val mealLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_meal, parent, false)

        return MealViewHolder(mealLayout)
    }

    // Binds the data to every view accordingly
    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.apply {
//            mealTitle.text = dataset[position].timeOfDay
//            listFoodDetails.adapter = FoodsAdapter(dataset[position].foodDetails)
        }
    }

    override fun getItemCount(): Int = dataset.size
}