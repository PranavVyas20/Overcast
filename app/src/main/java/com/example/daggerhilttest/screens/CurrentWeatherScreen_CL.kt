package com.example.daggerhilttest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.constraintlayout.compose.ConstraintLayout

@Preview
@Composable
fun CurrentWeatherScreenII() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFF64D4))
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)

        ) {
            val (cityNameField, dateField, tempFeild, tempValueFeild, tempDetailCard, hourlyForecatLazyRow, hourlyForecatFeild) = createRefs()
            Text(
                text = "Jaipur",
                modifier = Modifier
                    .constrainAs(cityNameField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )

            Text(
                text = "Monday, 19 June",
                modifier = Modifier
                    .constrainAs(dateField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(cityNameField.bottom, margin = 12.dp)
                    }
                    .background(color = Color.Black, shape = RoundedCornerShape(34.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),

                color = Color(0xFFFF64D4),
                fontWeight = FontWeight.Bold,
            )

            Text("Rain", modifier = Modifier
                .constrainAs(tempFeild) {
                    top.linkTo(dateField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 10.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Text(text = "27°", fontSize = 190.sp, modifier = Modifier.constrainAs(tempValueFeild) {
                top.linkTo(dateField.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .constrainAs(tempDetailCard) {
                        top.linkTo(tempValueFeild.bottom)
                    },
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(vertical = 22.dp, horizontal = 20.dp)
                ) {
                    WeatherDetailItem(
                        itemIcon = Icons.Default.Air,
                        headingText = "heading",
                        subHeadingText = "Wind"
                    )
                    WeatherDetailItem(
                        itemIcon = Icons.Default.WaterDrop,
                        headingText = "heading",
                        subHeadingText = "Humidity"
                    )
                    WeatherDetailItem(
                        itemIcon = Icons.Default.RemoveRedEye,
                        headingText = "heading",
                        subHeadingText = "Visibility"
                    )
                }
            }
            WeeklyForecastView(
                modifier = Modifier
                    .constrainAs(hourlyForecatLazyRow) {
                        top.linkTo(tempDetailCard.bottom, margin = 16.dp)
                        start.linkTo(tempDetailCard.start)
                        end.linkTo(tempDetailCard.end)
                    })

        }
    }


}

@Composable
fun WeatherDetailItem(
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
            tint = Color(0xFFFF64D4),
            contentDescription = "item icon",
            modifier = Modifier
                .scale(1.5f)
                .padding(top = 5.dp, bottom = 10.dp)
        )
        Text(
            text = headingText,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.body2,
            color = Color(0xFFFF64D4),
            fontWeight = FontWeight.W600
        )
        Text(
            text = subHeadingText,
            color = Color(0xFFFF64D4),
            fontSize = 12.sp,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W600
        )
    }
}

@Composable
fun WeeklyForecastView(modifier: Modifier) {
    ConstraintLayout(modifier) {
        val (weeklyForecastText, arrowIcon, weeklyForecastRow) = createRefs()
        Text(
            text = "Weekly Forecast",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.constrainAs(weeklyForecastText) {
                start.linkTo(parent.start, margin = 18.dp)
            })

        Icon(
            imageVector = Icons.Default.TrendingFlat,
            contentDescription = "Right arrow",
            tint = Color.Black,
            modifier = Modifier.constrainAs(arrowIcon) {
                end.linkTo(parent.end, margin = 18.dp)
            }
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 18.dp),
            modifier = Modifier
                .constrainAs(weeklyForecastRow) {
                    top.linkTo(weeklyForecastText.bottom, margin = 12.dp)
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(9) {
                WeeklyForecastItem()
            }
        }
    }
}

@Preview
@Composable
fun WeeklyForecastItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text("26°", fontSize = 16.sp)
        Icon(imageVector = Icons.Default.WaterDrop, contentDescription = "temp icon")
        Text("31 Jan", fontSize = 12.sp)

    }
}