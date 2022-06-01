package com.iprism.elliot.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.HistoryWithIngredientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    application: Application,
    private val repository: FoodRepository
) : AndroidViewModel(application) {

    var dateChosen = ""

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
                    repository.getAllHistoryWithIngredients(Locale.getDefault().toString())
                        .collect {
                            _cardUiState.value = it.map { m ->
                                HistoryWithIngredientsModel(
                                    foodName = m.key.foodName,
                                    foodDate = m.key.date,
                                    foodTime = m.key.time,
                                    meal = m.key.meal,
                                    ingredients = m.value
                                )
                            }

                            _oneTimeCardUiState.emit(_cardUiState.value)
                        }
                }
            }
            is CalendarEvent.OnDateChoose -> {
                viewModelScope.launch {
                    repository.getHistoryWithIngredientsDate(Locale.getDefault().toString(), dateChosen).collect {
                        _cardUiState.value = it.map { m ->
                            HistoryWithIngredientsModel(
                                foodName = m.key.foodName,
                                foodDate = m.key.date,
                                foodTime = m.key.time,
                                meal = m.key.meal,
                                ingredients = m.value
                            )
                        }

                        _oneTimeCardUiState.emit(_cardUiState.value)
                    }
                }
            }
        }
    }
}