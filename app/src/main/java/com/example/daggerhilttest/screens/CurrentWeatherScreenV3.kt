package com.example.daggerhilttest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.ViewSidebar
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PaintingStyle.Companion.Fill
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

val blackFontColor = Color(0xFF303030)
val grayFontColor = Color(0xFF8A8A8A)

@Composable
@Preview
fun CurrentWeatherScreenV3() {
    Column(
        modifier = Modifier
            .background(Color(0xFFF5F8FF))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val (locationTextField, dateTimeTextField, weatherIcon, currentTempTextField, weatherDescTextField, weatherInfoCard, sidebarIcon, searchBarIcon) = createRefs()
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "sidebar icon",
                modifier = Modifier
                    .size(34.dp)
                    .constrainAs(sidebarIcon) {
                        start.linkTo(parent.start)
                    })
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search icon",
                modifier = Modifier
                    .size(34.dp)
                    .constrainAs(searchBarIcon) {
                        end.linkTo(parent.end)
                    })
            Text(
                text = "Jaipur",
                color = blackFontColor,
                fontSize = 30.sp,
                modifier = Modifier.constrainAs(locationTextField) {
                    start.linkTo(parent.start)
                    top.linkTo(sidebarIcon.bottom, margin = 18.dp)
                })
            Text(
                text = "Sunday, 1 AM", color = grayFontColor,
                fontSize = 18.sp,
                modifier = Modifier.constrainAs(dateTimeTextField) {
                    top.linkTo(locationTextField.bottom, margin = 2.dp)
                    start.linkTo(parent.start)
                })
            Icon(
                imageVector = Icons.Default.Thunderstorm,
                contentDescription = "weather icon",
                modifier = Modifier
                    .size(94.dp)
                    .constrainAs(weatherIcon) {
                        top.linkTo(dateTimeTextField.bottom, margin = 14.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            Text(
                text = "30°",
                color = blackFontColor,
                fontSize = 70.sp,
                modifier = Modifier.constrainAs(currentTempTextField) {
                    top.linkTo(weatherIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Text(
                text = "Partly Cloudy", color = Color.Gray,
                fontSize = 22.sp,
                modifier = Modifier.constrainAs(weatherDescTextField) {
                    top.linkTo(currentTempTextField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            WeatherDetailsCard(
                modifier = Modifier
                    .constrainAs(weatherInfoCard) {
                        top.linkTo(weatherDescTextField.bottom, margin = 30.dp)
                    }
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun WeatherDetailsCard(modifier: Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(18.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F7F7))
                .padding(vertical = 12.dp, horizontal = 12.dp)
        ) {
            Text(
                text = "Today",
                color = blackFontColor,
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 14.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(3) {
                    WeatherDetailItem()
                }

            }
            HourlyForecastItemsRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
            )
//
//            HourlyGraph(
//                modifier = Modifier
//                    .padding(top = 38.dp)
//            )
        }
    }
}

@Composable
fun WeatherDetailItem() {
    Column() {
        Text("Pressure", fontSize = 18.sp, color = grayFontColor)
        Text("810mb", color = blackFontColor, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun HourlyForecastItem() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(tint = Color.DarkGray, imageVector = Icons.Default.Cloud, contentDescription = "icon")
        Text(
            text = "26°",
            color = blackFontColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = "4 pm", fontSize = 18.sp, color = grayFontColor)

    }
}

@Composable
fun HourlyForecastItemsRow(modifier: Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        items(7) {
            HourlyForecastItem()
        }
    }
}
