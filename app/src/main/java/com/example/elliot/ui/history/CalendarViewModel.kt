package com.example.elliot.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elliot.data.local.entity.HistoryIngredient
import com.example.elliot.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _cardUiState = MutableStateFlow(CardListPick("", "", "", "", emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val cardUiState: StateFlow<CardListPick> = _cardUiState

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.OnHistoryLoad -> {
                viewModelScope.launch {
                    repository.getHistoryWithIngredients(event.foodName).collect {
                        _cardUiState.value = cardUiState.value.copy(
                            foodName = it.toHistoryWithIngredientsModel().foodName,
                            foodDate = it.toHistoryWithIngredientsModel().foodDate,
                            foodTime = it.toHistoryWithIngredientsModel().foodTime,
                            meal = it.toHistoryWithIngredientsModel().meal,
                            ingredients = it.toHistoryWithIngredientsModel().ingredients
                        )
                    }
                }
            }
        }
    }

    data class CardListPick(
        val foodName : String,
        val foodDate : String?,
        val foodTime : String?,
        val meal : String?,
        val ingredients : List<HistoryIngredient>
    )
}
