package com.example.findit.presentation.screen.mark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class MarkViewModel : ViewModel() {

    private val _selectedLatLng = MutableStateFlow<LatLng?>(null)
    val selectedLatLng: StateFlow<LatLng?> = _selectedLatLng

    private val _effect = MutableSharedFlow<MarkEffect>()
    val effect: SharedFlow<MarkEffect> = _effect

    fun onEvent(event: MarkEvent) {
        when (event) {
            is MarkEvent.UpdateSelectedLocation -> {
                updateSelectedLocation(event.latLng)
            }

            MarkEvent.NavigateBack ->{
                viewModelScope.launch {
                    _effect.emit(MarkEffect.NavigateBack(_selectedLatLng.value))
                }
            }
        }
    }
    private fun updateSelectedLocation(latLng: LatLng) {
        _selectedLatLng.value = latLng
    }

}
