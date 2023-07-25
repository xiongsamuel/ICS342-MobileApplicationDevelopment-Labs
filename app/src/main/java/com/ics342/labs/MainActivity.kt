package com.ics342.labs


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ics342.labs.data.DayForecast
import com.ics342.labs.data.ForecastTemp
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private val ForecastItems = listOf(
    DayForecast(
        1688430192,
        1689825612000,
        1689886872000,
        ForecastTemp(25.0f, 20.0f, 30.0f),
        1023F,
        100
    ),
    DayForecast(
        1688516592,
        1689825732000,
        1689886992000,
        ForecastTemp(15.0f, 33.0f, 60.0f),
        1202F,
        88
    ),
    DayForecast(
        1688602992,
        1689825852000,
        1689887112000,
        ForecastTemp(20.0f, 21.0f, 60.0f),
        1024F,
        97
    ),
    DayForecast(
        1688689392,
        1689825972000,
        1689887232000,
        ForecastTemp(24.0f, 22.0f, 30.0f),
        1025F,
        98
    ),
    DayForecast(
        1688862192,
        1689826092000,
        1689887352000,
        ForecastTemp(16.0f, 23.0f, 30.0f),
        1123F,
        122
    ),
    DayForecast(
        1688948592,
        1689826212000,
        1689887472000,
        ForecastTemp(19.0f, 24.0f, 30.0f),
        1103F,
        123
    ),
    DayForecast(
        1689034992,
        1689826332000,
        1689887592000,
        ForecastTemp(20.0f, 25.0f, 30.0f),
        1204F,
        111
    ),
    DayForecast(
        1689121392,
        1689826452000,
        1689887712000,
        ForecastTemp(26.0f, 26.0f, 30.0f),
        1353F,
        121
    ),
    DayForecast(
        1689207792,
        1689826572000,
        1689887832000,
        ForecastTemp(23.0f, 27.0f, 30.0f),
        1015F,
        102
    ),
    DayForecast(
        1689294192,
        1689826692000,
        1689887952000,
        ForecastTemp(22.0f, 28.0f, 30.0f),
        1068F,
        101
    ),
    DayForecast(
        1689380592,
        1689826812000,
        1689888072000,
        ForecastTemp(17.0f, 29.0f, 30.0f),
        1489F,
        90
    ),
    DayForecast(
        1689466992,
        1689826932000,
        1689888192000,
        ForecastTemp(14.0f, 15.0f, 30.0f),
        1488F,
        54
    ),
    DayForecast(
        1689553392,
        1689827052000,
        1689888312000,
        ForecastTemp(18.0f, 16.0f, 30.0f),
        1126F,
        45
    ),
    DayForecast(
        1689639792,
        1689827172000,
        1689888432000,
        ForecastTemp(27.0f, 17.0f, 30.0f),
        1025F,
        120
    ),
    DayForecast(
        1689726192,
        1689827292000,
        1689888552000,
        ForecastTemp(28.0f, 18.0f, 30.0f),
        1024F,
        110
    ),
    DayForecast(
        1689812592,
        1689827412000,
        1689888672000,
        ForecastTemp(29.0f, 19.0f, 30.0f),
        1045F,
        99
    ),
)

class MainActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "HomeScreen") {
                this.composable("HomeScreen") {
                    myWeatherApp(navController)
                }
            }
        }
    }

    @Composable
    fun myWeatherApp(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.weatherApp_name),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(R.string.location),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)

            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.temperature),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 85.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = stringResource(R.string.prediction),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.lowTemp),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(R.string.highTemp),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(R.string.humidity),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(R.string.pressure),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 18.sp
            )
            Column(
                modifier = Modifier
                    .background(Color.Gray)
                    .padding(16.dp)
                    .width(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = {
                        navController.navigate("ForecastListDisplay");
                    })
            ) {
                Text(
                    text = "Forecast",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ForecastScreen(ForecastItems: DayForecast) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Sunny Weather Image",
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = ForecastDate(ForecastItems.date),
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.size(25.dp))
            Column {
                Text(
                    text = "Temp: ${ForecastItems.temp.day.toInt()}º",
                )
                Row {
                    Text(
                        text = "High: ${ForecastItems.temp.max.toInt()}º",
                    )
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = "Low: ${ForecastItems.temp.min.toInt()}º",
                    )
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            Column {
                Text(
                    text = "Sunrise: ${ForecastTime(ForecastItems.sunrise)}",
                )
                Text(
                    text = "Sunset: ${ForecastTime(ForecastItems.sunset)}",
                )
            }
        }
        Spacer(modifier = Modifier.size(15.dp))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ForecastList(ForecastItems: List<DayForecast>) {
        Column {
            titleBar("Forecast")
            LazyColumn {
                items(items = ForecastItems) {
                    ForecastScreen(ForecastItems = it)
                }
            }
        }
    }

    @Composable
    fun titleBar(title: String) {
        Column(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxWidth()
        ) {
            Text(
                text = "$title",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ForecastDate(date: Long): String {
        val instant = Instant.ofEpochSecond(date)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("MMM dd")
        return dateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ForecastTime(timestamp: Long): String {
        //Formatting long to time
        val instant = Instant.ofEpochMilli(timestamp)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("HH:mm a")
        return dateTime.format(formatter)
    }

    @Composable
    fun CurrentConditionsView(
        viewModel: CurrentConditionsViewModel = hiltViewModel()
    ) {
        val weatherData = viewModel.currentData.observeAsState()
    }
    @Composable
    fun ForecastView(
        viewModel: ForecastViewModel = hiltViewModel()
    ) {
        val weatherData = viewModel.forecastData.observeAsState()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object MyAppModule {

        @Provides
        fun provideMoshi(): Moshi {
            return Moshi.Builder()
                .build()
        }

        @Provides
        fun provideRetrofit(moshi: Moshi): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://openweathermap.org/current")
                .baseUrl("https://openweathermap.org/forecast16")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        @Provides
        fun provideApiService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}
