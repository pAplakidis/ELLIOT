package com.example.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.elliot.R
import com.example.elliot.data.local.HistoryWithIngredients
import com.example.elliot.domain.model.HistoryModel
import com.example.elliot.ui.history.CalendarViewModel

class FoodsAdapter(
    private val historyModel: CalendarViewModel.CardListPick
) : RecyclerView.Adapter<FoodsAdapter.FoodsViewHolder>() {

    inner class FoodsViewHolder(foodDetailsView: View) : RecyclerView.ViewHolder(foodDetailsView) {
        val foodName: TextView = foodDetailsView.findViewById(R.id.food_name)
        val timeEaten: TextView = foodDetailsView.findViewById(R.id.time)
        val dateEaten: TextView = foodDetailsView.findViewById(R.id.date)

        init {
            foodDetailsView.setOnClickListener {
                val bundle = bundleOf(
                    "foodName" to foodName.text.toString(),
                    "timeEaten" to timeEaten.text.toString(),
                    "dateEaten" to dateEaten.text.toString(),
                    "ingredients" to historyModel.ingredients.joinToString(","),
                    "meal" to historyModel.meal
                )
                Navigation.findNavController(it)
                    .navigate(R.id.action_calendarFragment_to_foodDetailsFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsViewHolder {
        val foodDetailsLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_foods, parent, false)

        return FoodsViewHolder(foodDetailsLayout)
    }

    override fun onBindViewHolder(holder: FoodsViewHolder, position: Int) {
        holder.apply {
            foodName.text = historyModel.foodName
            timeEaten.text = historyModel.foodTime
            dateEaten.text = historyModel.foodDate
        }

    }

    override fun getItemCount(): Int = 1

}