package com.ics342.labs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentConditionsViewModel @Inject constructor(private val apiService: ApiService): ViewModel() {

    private val _currentData: MutableLiveData<WeatherData> = MutableLiveData()
    val currentData: LiveData<WeatherData>
        get() = _currentData


    fun viewAppeared() = viewModelScope.launch {
        _currentData.value = apiService.getWeatherData()
    }
}