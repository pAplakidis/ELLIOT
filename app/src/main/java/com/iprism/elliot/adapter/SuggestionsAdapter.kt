package com.iprism.elliot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iprism.elliot.R

class SuggestionsAdapter(
    private val suggestionsList: List<String>
) : RecyclerView.Adapter<SuggestionsAdapter.SuggestionsViewHolder>() {

    class SuggestionsViewHolder(suggestionDetailsView: View) :
        RecyclerView.ViewHolder(suggestionDetailsView) {
        val suggestion: TextView = suggestionDetailsView.findViewById(R.id.suggestion)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestionsViewHolder {
        val suggestionsLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_suggestions, parent, false)

        return SuggestionsViewHolder(suggestionsLayout)
    }

    override fun onBindViewHolder(
        holder: SuggestionsViewHolder,
        position: Int
    ) {
        holder.apply {
            suggestion.text = suggestionsList[position]
        }
    }

    override fun getItemCount(): Int = suggestionsList.size
}