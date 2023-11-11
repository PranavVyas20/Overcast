package com.example.daggerhilttest.ui_components

import androidx.compose.foundation.Image
import com.example.daggerhilttest.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.daggerhilttest.models.v1.CurrentWeather
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun CurrentWeatherCard(
    weatherViewModel: WeatherViewModel,
    currentWeather: CurrentWeather?,
    placeHolderVisibility: Boolean
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 10.dp, bottom = 10.dp, end = 10.dp)
            .placeholder(
                visible = placeHolderVisibility,
                color = shimmerColor,
                shape = RoundedCornerShape(7.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White,
                )
            ), shape = RoundedCornerShape(7)
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.waves),
                contentDescription = "",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            if (currentWeather != null) {
//                val date = weatherViewModel.getDateFromUnix(currentWeather.unixTime!!)
//                val time = weatherViewModel.getTimeFromUnix(currentWeather.unixTime)
//                val temp =
//                    weatherViewModel.convertTempToCelsius(currentWeather.mainTempData!!.temp!!)
//                val iconId = currentWeather.weatherList!![0].icon
//                val iconUrl = Constants.BASE_ICON_URL + iconId + Constants.ICON_URL_SUFFIX
//                WeatherCardDetails(
//                    date = date,
//                    time = time,
//                    temperature = temp.toString(),
//                    tempIconImageUrl = iconUrl
//                )
            } else {
                Row(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                ) {}
            }
        }
    }
}

@Composable
fun WeatherCardDetails(
    date: String, time: String, temperature: String, tempIconImageUrl: String
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
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.scale(2.3f), model = tempIconImageUrl, loading = {
                    CircularProgressIndicator()
                }, contentDescription = ""
            )
            Text(
                text = "$temperature\u2103",
                color = Color.White,
                fontSize = 60.sp,
            )
        }
    }
}