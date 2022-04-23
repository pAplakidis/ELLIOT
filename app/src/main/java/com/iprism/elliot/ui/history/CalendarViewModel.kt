package com.iprism.elliot.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.HistoryWithIngredientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _cardUiState = MutableStateFlow(
        listOf(HistoryWithIngredientsModel("", "", "", "", emptyList()))
    )
//    val cardUiState = _cardUiState.asStateFlow()

    private val _oneTimeCardUiState = MutableSharedFlow<List<HistoryWithIngredientsModel>>()

    // The UI collects from this SharedFlow to get its state updates
    val oneTimeCardUiState = _oneTimeCardUiState.asSharedFlow()

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.OnHistoryLoad -> {
                viewModelScope.launch {
                    repository.getAllHistoryWithIngredients().collect {
                        _cardUiState.value = it.map { historyWithIngredients ->
                            historyWithIngredients.toHistoryWithIngredientsModel()
                        }

                        _oneTimeCardUiState.emit(_cardUiState.value)
                    }
                }
            }
        }
    }
}