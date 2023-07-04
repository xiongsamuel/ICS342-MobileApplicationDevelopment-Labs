package com.ics342.labs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ics342.labs.data.DataItem
import com.ics342.labs.ui.theme.LabsTheme

private val dataItems = listOf(
    DataItem(1, "Item 1", "Description 1"),
    DataItem(2, "Item 2", "Description 2"),
    DataItem(3, "Item 3", "Description 3"),
    DataItem(4, "Item 4", "Description 4"),
    DataItem(5, "Item 5", "Description 5"),
    DataItem(6, "Item 6", "Description 6"),
    DataItem(7, "Item 7", "Description 7"),
    DataItem(8, "Item 8", "Description 8"),
    DataItem(9, "Item 9", "Description 9"),
    DataItem(10, "Item 10", "Description 10"),
    DataItem(11, "Item 11", "Description 11"),
    DataItem(12, "Item 12", "Description 12"),
    DataItem(13, "Item 13", "Description 13"),
    DataItem(14, "Item 14", "Description 14"),
    DataItem(15, "Item 15", "Description 15"),
    DataItem(16, "Item 16", "Description 16"),
    DataItem(17, "Item 17", "Description 17"),
    DataItem(18, "Item 18", "Description 18"),
    DataItem(19, "Item 19", "Description 19"),
    DataItem(20, "Item 20", "Description 20"),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabsTheme {
                // A surface container using the 'background' color from the theme
                DataItemList(dataItems = dataItems)
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavView()
                }

            }
        }
    }
}


@Composable
fun DataItemView(dataItem: DataItem) {
    /* Create the view for the data item here. */
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ID: ${dataItem.id}",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = dataItem.name,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = dataItem.description,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}



@Composable
fun DataItemList(dataItems: List<DataItem>) =
    /* Create the list here. This function will call DataItemView() */
    LazyColumn {
        items(dataItems){ dataItem ->
            DataItemView(dataItem)
        }
    }

@Composable
fun DetailScreenView(onClickHome: () -> Unit, dataItem: DataItem) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = dataItem.id.toString(),
                style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = dataItem.name,
                style = MaterialTheme.typography.headlineLarge)
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = dataItem.description,
            style = MaterialTheme.typography.bodyLarge)
        Button(
            onClick = onClickHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go Back")
        }
    }
}

@Composable
fun HomeView(navController: NavHostController) {
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(dataItems) { DataItem ->
                Button(
                    onClick = { navController.navigate("Detail") },
                    colors = ButtonDefaults.buttonColors(Color.DarkGray.copy(alpha = 1F))
                ) {
                    DataItemView(DataItem)
                }
            }
        }
    }
}

@Composable
fun NavView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeView(navController)
        }
        composable("Detail") {
            DetailScreenView(
                onClickHome = { navController.popBackStack() }
            )
        }

    }
}

fun DetailScreenView(onClickHome: () -> Unit) {
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LabsTheme {
        NavView()
    }
}
