package com.example.elliot.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elliot.data.repository.FoodRepository
import com.example.elliot.domain.model.CardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _cardUiState = MutableStateFlow(CalendarUiState.CardListPick(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val cardUiState: StateFlow<CalendarUiState> = _cardUiState

//    private val _heightUiState = MutableStateFlow(CalendarUiState.HeightSetter(0))
//    val heightUiState: StateFlow<CalendarUiState> = _heightUiState

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.OnDateClick -> {
                viewModelScope.launch {

                    // ΒΑΛΕ ΤΣΕΚ ΓΙΑ ΗΜΕΡΟΜΗΝΙΑ ΚΑΙ ΣΚΑΣΕ UPDATE ΜΕΤΑ ΑΠΟ FETCH ΒΑΣΗΣ
//                    _cardUiState.update { it.copy(cardsChosen = repository.getHistoryInformation()) }

                }
            }
//            is CalendarEvent.OnCardClick -> {
//                viewModelScope.launch {
//                    if (event.height == 69) {
//                        _heightUiState.update {it.copy(heightChosen = 138)}
//                    } else {
//                        _heightUiState.update {it.copy(heightChosen = 69)}
//                    }
//                }
//            }
        }
    }

    sealed class CalendarUiState {
        data class CardListPick(val cardsChosen: List<CardModel>) : CalendarUiState()
//        data class HeightSetter(val heightChosen: Int): CalendarUiState()
    }
}