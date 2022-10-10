package com.example.daggerhilttest.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.daggerhilttest.R
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun HourlyForecastItem(
    isCurrentWeather: Boolean = false,
    bgImage: Int = R.drawable.waves,
    time: String,
    iconUrl: String,
    temperature: String,
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = 0.dp
    ) {
        Box() {
            Image(
                painter = painterResource(id = bgImage),
                contentDescription = "",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            HourlyForestDetails(time, iconUrl, temperature)
        }
    }
}

@Composable
fun HourlyForestDetails(
    time: String,
    icon: String,
    temperature: String
) {
    Column(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        Text(
            text = time,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 25.dp)
        )
        SubcomposeAsyncImage(
            model = icon,
            loading = {
                CircularProgressIndicator()
            },
            contentDescription = ""
        )
        Text(
            text = temperature,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 25.dp)
        )
    }
}

@Preview
@Composable
fun preview() {
//    HourlyForecastItem(time = "Now", iconUrl = "R", temperature = "28")
}