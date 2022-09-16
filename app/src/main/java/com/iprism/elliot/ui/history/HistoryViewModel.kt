package com.iprism.elliot.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.HistoryWithIngredientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    var dateChosen = ""
    var recycledAlpha = 1f
    var backgroundAlpha = 255
    var emptyHistoryAlpha = 0f

    // Backing property to avoid state updates from other classes
    private val _cardUiState = MutableStateFlow(
        CardUiState()
    )
    // val cardUiState = _cardUiState.asStateFlow()

    // private val _oneTimeCardUiState = MutableSharedFlow<List<HistoryWithIngredientsModel>>()
    private val _oneTimeCardUiState = MutableSharedFlow<CardUiState>()

    // The UI collects from this SharedFlow to get its state updates
    val oneTimeCardUiState = _oneTimeCardUiState.asSharedFlow()

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnHistoryLoad -> {
                viewModelScope.launch {
                    repository.getAllHistoryWithIngredients(Locale.getDefault().language)
                        .collect {
                            _cardUiState.value = CardUiState(it.map { m ->
                                HistoryWithIngredientsModel(
                                    foodName = m.key.foodName,
                                    foodDate = m.key.date,
                                    foodTime = m.key.time,
                                    meal = m.key.meal,
                                    ingredients = m.value
                                )
                            })

                            _oneTimeCardUiState.emit(_cardUiState.value)
                        }
                }
            }
            is HistoryEvent.OnDateChoose -> {
                viewModelScope.launch {
                    repository.getHistoryWithIngredientsDate(
                        Locale.getDefault().language,
                        dateChosen
                    ).collect {
                        _cardUiState.value = CardUiState(it.map { m ->
                            HistoryWithIngredientsModel(
                                foodName = m.key.foodName,
                                foodDate = m.key.date,
                                foodTime = m.key.time,
                                meal = m.key.meal,
                                ingredients = m.value
                            )
                        })

                        _oneTimeCardUiState.emit(_cardUiState.value)
                    }
                }
            }
        }
    }

    data class CardUiState(
        val data: List<HistoryWithIngredientsModel> = listOf(),
        val isLoading: Boolean = false
    )
}