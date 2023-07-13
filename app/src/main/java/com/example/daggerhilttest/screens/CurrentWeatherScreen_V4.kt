package com.example.daggerhilttest.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.daggerhilttest.R
import com.example.daggerhilttest.models.WeatherExtraDetail
import com.example.daggerhilttest.ui.theme.productSans
import com.example.daggerhilttest.ui_components.ExpandableSearchBar
import com.example.daggerhilttest.ui_components.MaterialSearchBar
import com.example.daggerhilttest.ui_components.WeatherExtraDetailItem
import com.example.daggerhilttest.util.WeatherExtraDetailType

@Preview
@Composable
fun CurrentWeatherScreen_V4() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF6EDFF))
    ) {
        item {
            WeatherDetailsGridView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                noOfItemsInRow = 2,
                weatherDetailItems = listOf(
                    WeatherExtraDetail(
                        type = WeatherExtraDetailType.WIND_SPEED,
                        title = "Wind Speed",
                        value = "12km/h",
                        icon = Icons.Default.Air
                    ),
                    WeatherExtraDetail(
                        type = WeatherExtraDetailType.WIND_SPEED,
                        title = "Wind Speed",
                        value = "12km/h",
                        icon = Icons.Default.Air
                    ),
                    WeatherExtraDetail(
                        type = WeatherExtraDetailType.WIND_SPEED,
                        title = "Wind Speed",
                        value = "12km/h",
                        icon = Icons.Default.Air
                    ),
                    WeatherExtraDetail(
                        type = WeatherExtraDetailType.WIND_SPEED,
                        title = "Wind Speed",
                        value = "12km/h",
                        icon = Icons.Default.Air
                    )
                )
            )
        }

        item {
            HourlyForecastView(
                paddingValues = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            )

        }
        item {
            HourlyForecastGraph(
                paddingValues = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            )

        }
        item {
            WeatherDetailsGridView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                noOfItemsInRow = 2,
                weatherDetailItems = listOf(
                    WeatherExtraDetail(
                        type = WeatherExtraDetailType.SUNRISE,
                        title = "Sunrise",
                        value = "5:23 AM",
                        icon = Icons.Outlined.WbSunny
                    ),
                    WeatherExtraDetail(
                        type = WeatherExtraDetailType.SUNSET,
                        title = "Sunset",
                        value = "7:59 PM",
                        icon = Icons.Outlined.NightsStay
                    ),
                )
            )
        }
    }

}

@Preview
@Composable
fun BottomSheetScaffoldContent() {
    val configuration = LocalConfiguration.current
    val screenHeight = remember { configuration.screenHeightDp.dp }
    val layoutHeight = screenHeight * 0.25f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF6EDFF))
    ) {

        Image(
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.weather_bg_cropped),
            contentDescription = ""
        )
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(layoutHeight)
        ) {
            val (tempValueTextField, feelsLikeTextField, weatherImage, dateTextField, maxTempTextField, minTempField) = createRefs()

            Text(
                "3°",
                fontSize = 110.sp,
                color = Color.White,
                fontFamily = productSans,
                fontWeight = FontWeight(400),
                modifier = Modifier
                    .constrainAs(tempValueTextField) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, margin = 23.dp)
                    })

            Text(
                "Feels like -2°",
                fontSize = 18.sp,
                fontFamily = productSans,
                fontWeight = FontWeight(400),
                color = Color.White,
                modifier = Modifier.constrainAs(feelsLikeTextField) {
                    bottom.linkTo(tempValueTextField.bottom, margin = (26).dp)
                    start.linkTo(tempValueTextField.end, margin = (-20).dp)
                })
            Text(
                "January 18, 16:14",
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = productSans,
                fontWeight = FontWeight(400),
                modifier = Modifier.constrainAs(dateTextField) {
                    bottom.linkTo(parent.bottom, margin = 4.dp)
                    start.linkTo(tempValueTextField.start)

                })

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.constrainAs(maxTempTextField) {
                    end.linkTo(parent.end, margin = 18.dp)
                    bottom.linkTo(parent.bottom, margin = 4.dp)

                }) {
                Text(
                    "Day 3°",
                    fontFamily = productSans,
                    fontWeight = FontWeight(700),
                    fontSize = 18.sp,
                    color = Color.White,
                )
                Text(
                    "Night -1°",
                    fontFamily = productSans,
                    fontWeight = FontWeight(700),
                    fontSize = 18.sp,
                    color = Color.White,
                )
            }

        }
    }
}

@Composable
fun WeatherExtraDetailItem(weatherExtraDetail: WeatherExtraDetail) {
    ConstraintLayout(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFEBDEFF))
            .fillMaxWidth()
    ) {

        val (titleTextField, valueTextField, icon, column) = createRefs()
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .height(28.dp)
            .width(28.dp)
            .clip(CircleShape)
            .background(Color.White)
            .constrainAs(icon) {
                top.linkTo(parent.top, margin = 18.dp)
                start.linkTo(parent.start, margin = 8.dp)
                bottom.linkTo(parent.bottom, margin = 18.dp)
            }) {
            Icon(
                imageVector = weatherExtraDetail.icon,
                contentDescription = "",
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.constrainAs(column) {
                centerVerticallyTo(parent)
                start.linkTo(icon.end, margin = 12.dp)
                end.linkTo(parent.end, margin = 12.dp)
            }) {
            Text(
                text = weatherExtraDetail.title,
                fontSize = 14.sp,
                fontFamily = productSans,
                letterSpacing = 0.25.sp,
                fontWeight = FontWeight(400),
            )

            Text(
                weatherExtraDetail.value, fontSize = 14.sp,
                fontFamily = productSans,
                letterSpacing = 0.15.sp,
                fontWeight = FontWeight(400)
            )
        }
    }

}

@Composable
fun WeatherDetailsGridView(
    modifier: Modifier,
    noOfItemsInRow: Int,
    weatherDetailItems: List<WeatherExtraDetail>
) {
    val chunkedList = weatherDetailItems.chunked(noOfItemsInRow)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for (subList in chunkedList) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                subList.forEach { it ->
                    Box(modifier = Modifier.weight(1f)) {
                        WeatherExtraDetailItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit) {
    val selectedColor = Color(0xFFE0B6FF)
    val unselectedColor = Color.White
    val isSelected by remember { (mutableStateOf(false)) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                color = if (isSelected) selectedColor else unselectedColor,
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Text(
            "Today",
            fontSize = 16.sp,
            color = Color.Black,
            fontFamily = productSans,
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.padding(horizontal = 34.dp, vertical = 9.dp)
        )
    }
}

@Preview
@Composable
fun HourlyForecastItemNew() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 13.sp, fontFamily = productSans,
                        letterSpacing = 0.13.sp,
                        fontWeight = FontWeight(400)
                    )
                ) {
                    append("12")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 10.sp, fontFamily = productSans,
                        letterSpacing = 0.19.sp,
                        fontWeight = FontWeight(400)
                    )
                ) {
                    append("AM")
                }
            }
        )
        Icon(imageVector = Icons.Default.Cloud, contentDescription = "Weather icon")
        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 18.sp)) {
                append("10")
            }
            withStyle(style = SpanStyle(fontSize = 18.sp)) {
                append("°")
            }
        })
    }
}

@Composable
fun HourlyForecastView(paddingValues: PaddingValues) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .clip(RoundedCornerShape(18.dp))
            .background(color = Color(0xFFEBDEFF))
    ) {
        val (icon, headingTextField, hourlyForecastLazyRow) = createRefs()
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .height(28.dp)
            .width(28.dp)
            .clip(CircleShape)
            .background(Color.White)
            .constrainAs(icon) {
                top.linkTo(parent.top, margin = 12.dp)
                start.linkTo(parent.start, margin = 11.dp)
            }) {
            Icon(
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp),
                imageVector = Icons.Default.HistoryToggleOff,
                contentDescription = "hourly_forecast_icon"
            )
        }

        Text(
            "Hourly Forecast",
            fontSize = 14.sp,
            fontFamily = productSans,
            letterSpacing = 0.25.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.constrainAs(headingTextField) {
                top.linkTo(parent.top, margin = 14.dp)
                start.linkTo(icon.end, margin = 8.dp)

            })

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(20.dp), modifier = Modifier
                .fillMaxWidth()
                .constrainAs(hourlyForecastLazyRow) {
                    top.linkTo(headingTextField.bottom)
                    start.linkTo(parent.start)
                }) {
            items(6) {
                HourlyForecastItemNew()
            }
        }
    }
}

@Composable
fun HourlyForecastGraph(paddingValues: PaddingValues) {
    HourlyGraph(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues = paddingValues)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFEBDEFF))
    )
}

@Preview
@Composable
fun WeeklyForecastItem() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFEBDEFF), shape = RoundedCornerShape(16.dp))
    ) {
        val (date, weather, maxTemp, minTemp, seperator, icon) = createRefs()
        Text(
            "Thursday, Jan 19",
            fontSize = 16.sp,
            fontFamily = productSans,
            letterSpacing = 0.15.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.constrainAs(date) {
                top.linkTo(parent.top, margin = 15.dp)
                start.linkTo(parent.start, margin = 16.dp)
            })

        Text(
            "Cloudy",
            color = Color(0xFF494649),
            fontSize = 16.sp,
            fontFamily = productSans,
            letterSpacing = 0.15.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.constrainAs(weather) {
                top.linkTo(date.bottom, margin = 3.dp)
                start.linkTo(date.start)
                bottom.linkTo(parent.bottom, margin = 18.dp)
            })

        Box(modifier = Modifier
            .height(54.dp)
            .width(54.dp)
            .clip(CircleShape)
            .background(Color.White)
            .constrainAs(icon) {
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(parent.top, margin = 14.dp)
                bottom.linkTo(parent.bottom, margin = 14.dp)
            })

        Box(modifier = Modifier
            .background(Color.Black)
            .width(1.dp)
            .constrainAs(seperator) {
                top.linkTo(icon.top)
                end.linkTo(icon.start, margin = 20.dp)
                bottom.linkTo(icon.bottom)
                height = Dimension.fillToConstraints
            }
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp, fontFamily = productSans,
                        letterSpacing = 0.5.sp,
                        fontWeight = FontWeight(400)
                    )
                ) {
                    append("3")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp, fontFamily = productSans,
                        letterSpacing = 0.5.sp,
                        fontWeight = FontWeight(400)
                    )
                ) {
                    append("°")
                }
            },
            modifier = Modifier.constrainAs(maxTemp) {
                end.linkTo(seperator.start, margin = 10.dp)
                top.linkTo(date.top)
            }
        )

        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp, fontFamily = productSans,
                        letterSpacing = 0.5.sp,
                        fontWeight = FontWeight(400)
                    )
                ) {
                    append("-4")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp, fontFamily = productSans,
                        letterSpacing = 0.5.sp,
                        fontWeight = FontWeight(400)
                    )
                ) {
                    append("°")
                }
            },
            modifier = Modifier.constrainAs(minTemp) {
                end.linkTo(seperator.start, margin = 10.dp)
                bottom.linkTo(weather.bottom)
            }
        )
    }
}

@Preview
@Composable
fun WeatherExtraDetailItemPreview() {
    WeatherExtraDetailItem(
        weatherExtraDetail = WeatherExtraDetail(
            type = WeatherExtraDetailType.WIND_SPEED,
            title = "Wind Speed",
            value = "12km/h",
            icon = Icons.Default.Air
        )
    )
}

@Preview
@Composable
fun WeatherDetailsGridViewPreview() {
    WeatherDetailsGridView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        noOfItemsInRow = 2,
        weatherDetailItems = listOf(
            WeatherExtraDetail(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind Speed",
                value = "12km/h",
                icon = Icons.Default.Air
            ),
            WeatherExtraDetail(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind Speed",
                value = "12km/h",
                icon = Icons.Default.Air
            ),
            WeatherExtraDetail(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind Speed",
                value = "12km/h",
                icon = Icons.Default.Air
            ),
            WeatherExtraDetail(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind Speed",
                value = "12km/h",
                icon = Icons.Default.Air
            )
        )
    )
}



