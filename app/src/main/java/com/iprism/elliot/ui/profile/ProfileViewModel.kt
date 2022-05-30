package com.iprism.elliot.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _suggestionUiState = MutableStateFlow(
        listOf(String())
    )
    val suggestionUiState = _suggestionUiState.asStateFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadSuggestions -> {
                viewModelScope.launch {
                    repository.getAllSuggestions().collect { suggestions ->
                        _suggestionUiState.value = suggestions
                    }
                }
            }
            is ProfileEvent.SetMealTime -> {
                viewModelScope.launch {
                    // GIA EVENT.MEAL VALE EVENT.TIMESTART, EVENT.TIMEEND
                }
            }
        }
    }
}