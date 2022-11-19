package com.example.daggerhilttest.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.daggerhilttest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2

@Composable
fun NetworkErrorLayout(
    currentLat: Double,
    currentLong: Double,
    getCurrentWeather: suspend (Double, Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.oops),
            contentDescription = "network_error_img",
        )
        Text(text = "Network Error", modifier = Modifier.align(Alignment.CenterHorizontally))
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    getCurrentWeather(currentLat, currentLong)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }

    }
}