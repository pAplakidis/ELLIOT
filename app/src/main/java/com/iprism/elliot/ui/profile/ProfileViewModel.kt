package com.iprism.elliot.ui.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val sharedPref: SharedPreferences
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
                // Store meal times in shared preferences file.
                with(sharedPref.edit()) {
                    when (event.meal) {
                        "breakfast" -> {
                            putInt("breakfastStart", event.hourStart)
                            putInt("breakfastEnd", event.hourEnd)
                        }
                        "lunch" -> {
                            putInt("lunchStart", event.hourStart)
                            putInt("lunchEnd", event.hourEnd)
                        }
                        else -> {
                            putInt("dinnerStart", event.hourStart)
                            putInt("dinnerEnd", event.hourEnd)
                        }
                    }
                    apply()
                }
            }
        }
    }
}