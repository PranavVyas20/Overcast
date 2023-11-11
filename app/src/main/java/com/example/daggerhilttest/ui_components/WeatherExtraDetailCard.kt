package com.example.daggerhilttest.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daggerhilttest.models.v1.CurrentWeather
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun WeatherExtraDetailCard(
    weatherViewModel: WeatherViewModel,
    currentWeather: CurrentWeather?,
    placeHolderVisibility: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .placeholder(
                visible = placeHolderVisibility,
                color = shimmerColor,
                shape = RoundedCornerShape(8.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White,
                )
            ),
        shape = RoundedCornerShape(10.dp),
        elevation = 0.dp
    )
    {
        if(currentWeather!=null) {
            Row(
                modifier = Modifier
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//                WeatherExtraDetailItem(
//                    itemIcon = Icons.Outlined.Thermostat,
//                    headingText = "${weatherViewModel.convertTempToCelsius(currentWeather.mainTempData!!.feelsLike!!)} ℃",
//                    subHeadingText = "Feels Like"
//                )
//                WeatherExtraDetailItem(
//                    itemIcon = Icons.Outlined.WaterDrop,
//                    headingText = "${currentWeather.mainTempData.humidity} %",
//                    subHeadingText = "Humidity"
//                )
//                WeatherExtraDetailItem(
//                    itemIcon = Icons.Outlined.Speed,
//                    headingText = "${currentWeather.mainTempData.pressure} hPa",
//                    subHeadingText = "Air Preassure"
//                )
            }
        } else {
            Row(modifier = Modifier.height(100.dp)){}
        }
    }
}

@Composable
fun WeatherExtraDetailItem(
    itemIcon: ImageVector,
    headingText: String,
    subHeadingText: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Icon(
            imageVector = itemIcon,
            contentDescription = "item icon",
            modifier = Modifier.scale(1.5f).
                    padding(top = 5.dp, bottom = 10.dp)
        )
        Text(
            text = headingText,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W600
        )
        Text(
            text = subHeadingText,
            fontSize = 15.sp,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W600
        )
    }
}

@Preview
@Composable
fun CardPreview() {
//    WeatherExtraDetailCard()
}