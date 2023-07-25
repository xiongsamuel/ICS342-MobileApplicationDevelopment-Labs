package com.ics342.labs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(private val apiService: ApiService): ViewModel() {

    private val _forecastData: MutableLiveData<WeatherData> = MutableLiveData()
    val forecastData: LiveData<WeatherData>
        get() = _forecastData


    fun viewAppeared() = viewModelScope.launch {
        _forecastData.value = apiService.getWeatherData()
    }
}