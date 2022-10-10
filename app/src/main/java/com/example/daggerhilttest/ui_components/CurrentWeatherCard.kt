package com.example.daggerhilttest.ui_components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.daggerhilttest.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.daggerhilttest.models.CurrentWeather
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun CurrentWeatherCard(currentWeather: CurrentWeather?, placeHolderVisibility: Boolean) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
            .placeholder(
                visible = placeHolderVisibility,
                color = shimmerColor,
                shape = RoundedCornerShape(7.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White,
                )
            ),
        shape = RoundedCornerShape(7)
    ) {
        Box() {
            Image(
                painter = painterResource(id = R.drawable.waves),
                contentDescription = "",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            weatherCardDetails()
        }
    }
}

@Composable
fun weatherCardDetails(
    date: String = "12 July 2022",
    time: String = "8:00 PM",
    temperature: String = "28",
    tempImage: Any? = null
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "$date | $time",
            color = Color.White,
            modifier = Modifier.align(alignment = Alignment.End)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    bottom = 10.dp,
                    end = 10.dp
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 30.dp, end = 50.dp)
                    .align(Alignment.CenterVertically)
                    .scale(3f),
                painter = rememberAsyncImagePainter(model = "https://openweathermap.org/img/wn/10d@4x.png"),
                contentDescription = "",
            )
            Text(
                text = "$temperature\u2103",
                color = Color.White,
                fontSize = 60.sp,
            )
        }
    }
}