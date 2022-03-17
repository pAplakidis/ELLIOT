package com.example.elliot.ui.history

import androidx.lifecycle.ViewModel
import com.example.elliot.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {
    private var foodId: Int = 0;

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.OnDateClick -> TODO()
            is CalendarEvent.OnCardClick -> TODO()
        }
    }
}