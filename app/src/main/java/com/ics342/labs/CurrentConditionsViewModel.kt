package com.ics342.labs

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
abstract class CurrentConditionsViewModel @Inject constructor(private val apiService: ApiService): ViewModel() {

    private val _currentData: MutableLiveData<WeatherData> = MutableLiveData()
    val currentData: LiveData<WeatherData>
        get() = _currentData

    val textFieldText: MutableLiveData<String> = MutableLiveData()
    fun viewAppeared() = viewModelScope.launch {
        _currentData.value = apiService.getWeatherData()
    }
    private val _currentWeather = MutableStateFlow<WeatherData?>(null)
    val currentWeather: StateFlow<WeatherData?> = _currentWeather

    fun fetchCurrentWeather(zip: String, apiKey: String) = viewModelScope.launch {
        _currentWeather.value = apiService.getCurrentWeather(zip, apiKey)
    }

    private val _zipCode = MutableStateFlow("")
    val zipCode: StateFlow<String> = _zipCode

    fun updateZipCode(newZipCode: String) {
        _zipCode.value = newZipCode
    }
    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.matches(Regex("\\d{5}"))
    }
    fun fetchWeatherDataByCity(zipCode: String) {
        if (isValidZipCode(zipCode)) {
            viewModelScope.launch {
                try {
                    val response = apiService.getWeatherData(zipCode)
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        _currentData.value = weatherData
                    } else {
                        // Handle error case
                        Log.e(
                            "CurrentConditionsViewModel",
                            "API call failed: ${response.errorBody()}"
                        )
                        // Handle invalid city input (e.g., show an error message to the user)
                        Log.e("CurrentConditionsViewModel", "Invalid city: $zipCode")
                    }
                } catch (e: Exception) {
                    // Handle exception
                    Log.e("CurrentConditionsViewModel", "Exception: $e")
                }
            }
        }
    }

    fun updateTextFieldText(newText: String) {
        textFieldText.value = newText ?: ""
    }
}