package com.ics342.labs

import retrofit2.http.GET

interface ApiService {
    @GET("/path/to/data")
    suspend fun getWeatherData(): WeatherData

    suspend fun getForecastData(): List<Forecast>
}