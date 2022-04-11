package com.iprism.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iprism.elliot.R

class IngredientsAdapter(
    private val ingredientList: Array<String>?
) : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    class IngredientsViewHolder(ingredientDetailsView: View) :
        RecyclerView.ViewHolder(ingredientDetailsView) {
        val ingredient: TextView = ingredientDetailsView.findViewById(R.id.ingredient_name)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientsViewHolder {
        val ingredientsLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_ingredients, parent, false)

        return IngredientsViewHolder(ingredientsLayout)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.apply {
            ingredient.text = ingredientList?.get(position).toString()
        }
    }

    override fun getItemCount(): Int = ingredientList?.count() ?: 0
}