package com.example.daggerhilttest.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.daggerhilttest.R
import com.example.daggerhilttest.models.PlaceSuggestion
import com.example.daggerhilttest.models.v2.CurrentWeatherDataV2
import com.example.daggerhilttest.models.v1.WeatherExtraDetailItem
import com.example.daggerhilttest.models.v2.GraphPoints
import com.example.daggerhilttest.models.v2.HourlyForecastDataV2
import com.example.daggerhilttest.models.v2.WeatherForecastData
import com.example.daggerhilttest.ui.theme.productSans
import com.example.daggerhilttest.ui.theme.purpleBgColor
import com.example.daggerhilttest.ui.theme.purpleWeatherItemColor
import com.example.daggerhilttest.ui_components.CurrentWeatherGraphV2
import com.example.daggerhilttest.ui_components.SearchBarV2
import com.example.daggerhilttest.util.WeatherExtraDetailType

val purpleColor = Color(0xFFEBDEFF)

@Composable
fun CurrentWeatherScreenBottomSheetContent(
    currentWeather: CurrentWeatherDataV2,
    hourlyForecast: List<HourlyForecastDataV2>,
    dayForecast: List<GraphPoints>
) {
    LazyColumn(
        modifier = Modifier.background(color = purpleBgColor),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            ButtonsLayout()
        }
        item {
            WeatherDetailsGridView(
                modifier = Modifier
                    .fillMaxWidth(),
                noOfItemsInRow = 2,
                weatherDetailItems = currentWeather.weatherDetailItems
            )
        }

        item {
            HourlyForecastView(
                hourlyForecast = hourlyForecast
            )

        }
        item {
            Log.d("column_tag", dayForecast.size.toString())
            CurrentWeatherGraphV2(graphPoints = dayForecast)
        }
        item {
            WeatherDetailsGridView(
                modifier = Modifier
                    .fillMaxWidth(),
                noOfItemsInRow = 2,
                weatherDetailItems = currentWeather.sunsetSunriseWeatherItems
            )
        }
    }
}

@Composable
fun BottomSheetScaffoldContent(
    location: String,
    temp: Float,
    icon: String,
    time: String,
    feelsLikeTemp: Float,
    weatherCondition: String,
    placesSuggestions: List<PlaceSuggestion>,
    getAutoCompleteSuggestion: (String) -> Unit,
    getLatLongFromPlaceId: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .background(color = Color(0xFF101e37))
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (
                tempValueTextField,
                feelsLikeTextField,
                searchbarWithSuggestionView,
                weatherIcon,
                bgImage,
                searchbarView,
                dateTextField,
            ) = createRefs()

            Image(contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(bgImage) {}
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.weather_bg),
                contentDescription = ""
            )

            Text("${temp.toInt()}°",
                color = Color.White,
                fontSize = 80.sp,
                fontFamily = productSans,
                fontWeight = FontWeight(400),
                modifier = Modifier.constrainAs(tempValueTextField) {
                    top.linkTo(searchbarView.top, margin = 30.dp)
                    bottom.linkTo(dateTextField.top)
                    start.linkTo(parent.start, margin = 23.dp)
                })

            Text("Feels like ${feelsLikeTemp}°",
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = productSans,
                fontWeight = FontWeight(400),
                modifier = Modifier.constrainAs(feelsLikeTextField) {
                    end.linkTo(parent.end, margin = 18.dp)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                })
            Text(text = time,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = productSans,
                fontWeight = FontWeight(400),
                modifier = Modifier.constrainAs(dateTextField) {
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                    start.linkTo(tempValueTextField.start)

                })

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.constrainAs(weatherIcon) {
                    bottom.linkTo(tempValueTextField.bottom)
                    end.linkTo(parent.end, margin = 18.dp)
                }) {
                AsyncImage(
                    modifier = Modifier.size(107.dp),
                    colorFilter = ColorFilter.tint(purpleBgColor.copy(alpha = 0.75f)),
                    model = ImageRequest.Builder(LocalContext.current).data(icon)
                        .decoderFactory(factory = SvgDecoder.Factory()).build(),
                    contentDescription = null
                )
                Text(
                    text = weatherCondition,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = productSans,
                    fontWeight = FontWeight(400)
                )
            }
            SearchbarWithSuggestionView(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .animateContentSize()
                    .constrainAs(searchbarView) {
                        top.linkTo(parent.top, 8.dp)
                    }, location = location,
                onPlaceSuggestionClick = {
                    getLatLongFromPlaceId(it)
                },
                onTextQueryChange = {
                    getAutoCompleteSuggestion(it)
                },
                suggestions = placesSuggestions
            )
        }
    }
}

@Composable
fun SearchbarWithSuggestionView(
    modifier: Modifier,
    location: String,
    suggestions: List<PlaceSuggestion>,
    onTextQueryChange: (textQuery: String) -> Unit,
    onPlaceSuggestionClick: (id: String) -> Unit
) {
    var bgColor by remember { mutableStateOf(Color.Transparent) }
    var showSuggestionView by remember {
        mutableStateOf(false)
    }

    ConstraintLayout(
        modifier = modifier
            .background(
                color = bgColor,
                shape = RoundedCornerShape(26.dp)
            )
            .clip(RoundedCornerShape(26.dp))
    ) {
        val (searchBar, suggestionView) = createRefs()
        SearchBarV2(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(searchBar) {
                    top.linkTo(parent.top)
                }
                .background(color = Color.Transparent),
            locationName = location,
            onCloseIconClicked = {
                showSuggestionView = false
                bgColor = Color.Transparent
            },
            onTextQueryChanged = { textQuery ->
                if (suggestions.isNotEmpty() && textQuery.isNotEmpty()) {
                    showSuggestionView = true
                    bgColor = purpleColor
                }
                onTextQueryChange(textQuery)
            }
        )

        if (showSuggestionView) {
            Column(
                modifier = Modifier
                    .constrainAs(suggestionView) {
                        top.linkTo(searchBar.bottom)
                    }
                    .fillMaxWidth()
                    .background(purpleColor)
            ) {
                suggestions.forEachIndexed { index, placeSuggestion ->
                    LocationSuggestionView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .clickable { onPlaceSuggestionClick(placeSuggestion.placeId) },
                        location = placeSuggestion.place,
                        showBottomSeparator = index != suggestions.lastIndex
                    )
                }
            }
        }
    }
}

@Composable
fun LocationSuggestionView(modifier: Modifier, location: String, showBottomSeparator: Boolean) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(location)
        if (showBottomSeparator) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.DarkGray)
            )
        }
    }
}

@Composable
fun WeatherExtraDetailItem(weatherExtraDetailItem: WeatherExtraDetailItem) {
    ConstraintLayout(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = purpleWeatherItemColor)
            .fillMaxWidth()
    ) {

        val (titleTextField, valueTextField, icon, column) = createRefs()
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
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
                imageVector = weatherExtraDetailItem.icon,
                contentDescription = "",
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.constrainAs(column) {
                centerVerticallyTo(parent)
                start.linkTo(icon.end, 8.dp)
            }) {
            Text(
                text = weatherExtraDetailItem.title,
                fontSize = 14.sp,
                fontFamily = productSans,
                letterSpacing = 0.25.sp,
                fontWeight = FontWeight(400),
            )

            Text(
                weatherExtraDetailItem.value,
                fontSize = 14.sp,
                fontFamily = productSans,
                letterSpacing = 0.15.sp,
                fontWeight = FontWeight(400)
            )
        }
    }

}

@Composable
fun WeatherDetailsGridView(
    modifier: Modifier, noOfItemsInRow: Int, weatherDetailItems: List<WeatherExtraDetailItem>
) {
    val chunkedList = weatherDetailItems.chunked(noOfItemsInRow)
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
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
fun PrimaryButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val selectedColor = Color(0xFFE0B6FF)
    val unselectedColor = Color.White

    Button(modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) selectedColor else unselectedColor,
        ),
        onClick = { onClick() }) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black,
            fontFamily = productSans,
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun ButtonsLayout() {
    var isTodayBtnSelected by remember {
        mutableStateOf(true)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PrimaryButton(
            text = "Today",
            isSelected = isTodayBtnSelected,
            onClick = { isTodayBtnSelected = true },
            modifier = Modifier.weight(1f)
        )
        PrimaryButton(
            text = "15 Days",
            isSelected = !isTodayBtnSelected,
            onClick = { isTodayBtnSelected = false },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun HourlyForecastItemView(hourlyForecast: HourlyForecastDataV2) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val time = hourlyForecast.formattedTime.dropLast(2)
        val amPm = hourlyForecast.formattedTime.takeLast(2)
        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 18.sp)) {
                append(time)
            }
            withStyle(style = SpanStyle(fontSize = 13.sp)) {
                append(amPm)
            }
        })
        AsyncImage(
            modifier = Modifier
                .size(38.dp),
            model = ImageRequest.Builder(LocalContext.current).data(hourlyForecast.icon)
                .decoderFactory(factory = SvgDecoder.Factory()).build(),
            contentDescription = null
        )
        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 18.sp)) {
                append(hourlyForecast.temp.toInt().toString())
            }
            withStyle(style = SpanStyle(fontSize = 18.sp)) {
                append("°")
            }
        })
    }
}

@Composable
fun HourlyForecastView(
    hourlyForecast: List<HourlyForecastDataV2>
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(color = purpleWeatherItemColor)
    ) {
        val (icon, headingTextField, hourlyForecastLazyRow) = createRefs()
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
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

        Text("Hourly Forecast",
            fontSize = 14.sp,
            fontFamily = productSans,
            letterSpacing = 0.25.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.constrainAs(headingTextField) {
                centerVerticallyTo(icon)
                start.linkTo(icon.end, margin = 8.dp)

            })

        LazyRow(horizontalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(hourlyForecastLazyRow) {
                    top.linkTo(headingTextField.bottom)
                    start.linkTo(parent.start)
                }) {
            items(hourlyForecast) { item ->
                HourlyForecastItemView(item)
            }
        }
    }
}

@Composable
fun HourlyForecastGraph(paddingValues: PaddingValues, dayForecast: List<WeatherForecastData>) {
    // Convert dayforecast to graph points here !!!
    HourlyGraph(
        dayForecast = dayForecast,
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
        Text("Thursday, Jan 19",
            fontSize = 16.sp,
            fontFamily = productSans,
            letterSpacing = 0.15.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.constrainAs(date) {
                top.linkTo(parent.top, margin = 15.dp)
                start.linkTo(parent.start, margin = 16.dp)
            })

        Text("Cloudy",
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
            })
        Text(buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp,
                    fontFamily = productSans,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight(400)
                )
            ) {
                append("3")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp,
                    fontFamily = productSans,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight(400)
                )
            ) {
                append("°")
            }
        }, modifier = Modifier.constrainAs(maxTemp) {
            end.linkTo(seperator.start, margin = 10.dp)
            top.linkTo(date.top)
        })

        Text(buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp,
                    fontFamily = productSans,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight(400)
                )
            ) {
                append("-4")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp,
                    fontFamily = productSans,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight(400)
                )
            ) {
                append("°")
            }
        }, modifier = Modifier.constrainAs(minTemp) {
            end.linkTo(seperator.start, margin = 10.dp)
            bottom.linkTo(weather.bottom)
        })
    }
}

@Preview
@Composable
fun WeatherExtraDetailItemPreview() {
    WeatherExtraDetailItem(
        weatherExtraDetailItem = WeatherExtraDetailItem(
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
            .fillMaxWidth(),
        noOfItemsInRow = 2,
        weatherDetailItems = listOf(
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind Speed",
                value = "12km/h",
                icon = Icons.Default.Air
            ), WeatherExtraDetailItem(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind Speed",
                value = "12km/h",
                icon = Icons.Default.Air
            ), WeatherExtraDetailItem(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind Speed",
                value = "12km/h",
                icon = Icons.Default.Air
            ), WeatherExtraDetailItem(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind Speed",
                value = "12km/h",
                icon = Icons.Default.Air
            )
        )
    )
}

