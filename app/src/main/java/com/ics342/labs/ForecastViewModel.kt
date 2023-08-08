package com.ics342.labs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _forecast = MutableStateFlow<Forecast?>(null)
    val forecast: StateFlow<Forecast?> = _forecast

    fun fetchForecast(zip: String, apiKey: String) = viewModelScope.launch {
        _forecast.value = apiService.getForecast(zip, apiKey)
    }
    fun fetchWeatherForecast(zipCode: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getForecast(zipCode)
                if (response.isSuccessful) {
                    val forecastData = response.body()
                    if (forecastData != null) {
                        _forecastLiveData.value = forecastData.forecasts
                    } else {
                        // Handle null data or empty list
                        Log.e("ForecastViewModel", "Null data or empty list")
                    }
                } else {
                    // Handle error response
                    Log.e("ForecastViewModel", "API call failed: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("ForecastViewModel", "Exception: $e")
            }
        }
    }
}