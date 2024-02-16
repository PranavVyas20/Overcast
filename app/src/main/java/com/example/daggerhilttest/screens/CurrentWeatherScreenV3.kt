package com.example.daggerhilttest.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.daggerhilttest.R
import com.example.daggerhilttest.models.v2.WeatherDataV2
import com.example.daggerhilttest.ui.theme.productSans
import com.example.daggerhilttest.ui.theme.purpleBgColor
import com.example.daggerhilttest.viewmodels.WeatherViewModel

@Composable
fun CurrentWeatherScreenV3(weatherViewModel: WeatherViewModel) {
    val currentLocationState =
        weatherViewModel.currentLocationState.collectAsStateWithLifecycle().value
    val currentWeatherState =
        weatherViewModel.currentWeatherStateV3.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = Unit) {
        weatherViewModel.getWeather(26.3609, 75.9289)
        weatherViewModel.getLocationFromGeocoding(26.3609, 75.9289)
    }


}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMotionApi::class)
@Composable
fun CurrentWeatherScreenV3Content(weatherData: WeatherDataV2) {
    val swipingState = rememberSwipeableState(initialValue = SwipingStates.EXPANDED)
    BoxWithConstraints(//to get the max height
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        val heightInPx = with(LocalDensity.current) { maxHeight.toPx() }
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset, source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        swipingState.performDrag(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset, available: Offset, source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return swipingState.performDrag(delta).toOffset()
                }

                override suspend fun onPostFling(
                    consumed: Velocity, available: Velocity
                ): Velocity {
                    swipingState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }
        val motionSceneContent = remember {
            context.resources.openRawResource(R.raw.motion_scene).readBytes().decodeToString()
        }

        Box(//root container
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .swipeable(
                    state = swipingState, thresholds = { _, _ ->
                        FractionalThreshold(0.05f)//it can be 0.5 in general
                    }, orientation = Orientation.Vertical, anchors = mapOf(
                        0f to SwipingStates.COLLAPSED,//min height is collapsed
                        heightInPx to SwipingStates.EXPANDED,//max height is expanded
                    )
                )
                .nestedScroll(nestedScrollConnection)
        ) {
            val computedProgress by remember { //progress value will be decided as par state
                derivedStateOf {
                    if (swipingState.progress.to == SwipingStates.COLLAPSED) swipingState.progress.fraction
                    else 1f - swipingState.progress.fraction
                }
            }
            MotionLayout(
                modifier = Modifier.fillMaxSize(),
                progress = computedProgress,
                motionScene = MotionScene(motionSceneContent)
            ) {

                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .layoutId("bg_image")
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.weather_bg),
                    contentDescription = ""
                )
                Text(
                    "30°",
                    color = Color.White,
                    fontSize = 80.sp,
                    fontFamily = productSans,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.layoutId("tempTextField")
                )

                Text(
                    "Feels like 29°",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = productSans,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.layoutId("feelsLikeTextField")
                )
                Text(
                    text = "12 Dec 2023",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = productSans,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.layoutId("timeTextField")
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.layoutId("weatherIcon")
                ) {
                    AsyncImage(
                        modifier = Modifier.size(107.dp),
                        colorFilter = ColorFilter.tint(purpleBgColor.copy(alpha = 0.75f)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(weatherData.currentWeather.icon)
                            .decoderFactory(factory = SvgDecoder.Factory()).build(),
                        contentDescription = null
                    )
//                    Text(
//                        text = weatherCondition,
//                        fontSize = 18.sp,
//                        color = Color.White,
//                        fontFamily = productSans,
//                        fontWeight = FontWeight(400)
//                    )
                }
                SearchbarWithSuggestionView(
                    modifier = Modifier
                        .animateContentSize()
                        .layoutId("searchbar"),
                    location = "Bangalore",
                    onPlaceSuggestionClick = {

                    },
                    onTextQueryChange = {

                    },
                    suggestions = listOf()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .layoutId("weatherDetailsListView")
                ) {
                    CurrentWeatherScreenBottomSheetContent(
                        currentWeather = weatherData.currentWeather,
                        hourlyForecast = weatherData.weatherForecast.first().hourlyForecastData,
                        dayForecast = weatherData.weatherForecast,
                        modifier = Modifier.background(color = purpleBgColor)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMotionApi::class)
@Composable
fun testComposable(weatherData: WeatherDataV2) {
    val swipingState = rememberSwipeableState(initialValue = SwipingStates.EXPANDED)
    BoxWithConstraints(//to get the max height
        modifier = Modifier.fillMaxSize()
    ) {
        val heightInPx = with(LocalDensity.current) { maxHeight.toPx() }
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset, source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        swipingState.performDrag(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset, available: Offset, source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return swipingState.performDrag(delta).toOffset()
                }

                override suspend fun onPostFling(
                    consumed: Velocity, available: Velocity
                ): Velocity {
                    swipingState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }

        Box(//root container
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .swipeable(
                    state = swipingState, thresholds = { _, _ ->
                        FractionalThreshold(0.5f)//it can be 0.5 in general
                    }, orientation = Orientation.Vertical, anchors = mapOf(
                        0f to SwipingStates.COLLAPSED,//min height is collapsed
                        heightInPx to SwipingStates.EXPANDED,//max height is expanded
                    )
                )
                .nestedScroll(nestedScrollConnection)
        ) {
            val computedProgress by remember { //progress value will be decided as par state
                derivedStateOf {
                    if (swipingState.progress.to == SwipingStates.COLLAPSED) swipingState.progress.fraction
                    else 1f - swipingState.progress.fraction
                }
            }
            MotionLayout(
                modifier = Modifier
                    .fillMaxSize(),
                start = ConstraintSet {
                    val bgImage = createRefFor("bgImage")
                    val body = createRefFor("body")
                    val tempTextValueField = createRefFor("tempTextValueField")
                    val tempFeelsLikeTextField = createRefFor("tempFeelsLikeTextField")
                    val dateTimeTextField = createRefFor("dateTimeTextField")
                    val tempIcon = createRefFor("tempIcon")
                    val searchBar = createRefFor("searchBar")
                    constrain(searchBar) {
                        top.linkTo(parent.top, 8.dp)
                    }
                    constrain(bgImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        this.height = Dimension.wrapContent
                    }
                    constrain(tempTextValueField) {
                        top.linkTo(searchBar.top, margin = 30.dp)
                        bottom.linkTo(dateTimeTextField.top)
                        start.linkTo(parent.start, margin = 23.dp)
                    }
                    constrain(tempFeelsLikeTextField) {
                        end.linkTo(parent.end, margin = 18.dp)
                        bottom.linkTo(bgImage.bottom, margin = 12.dp)
                    }
                    constrain(tempIcon) {
                        bottom.linkTo(tempTextValueField.bottom)
                        end.linkTo(parent.end, margin = 18.dp)
                    }
                    constrain(body) {
                        top.linkTo(bgImage.bottom, 8.dp)
                        height = Dimension.fillToConstraints
                        bottom.linkTo(parent.bottom, 0.dp)
                    }
                    constrain(dateTimeTextField) {
                        bottom.linkTo(bgImage.bottom, margin = 12.dp)
                        start.linkTo(tempTextValueField.start)
                    }
                },
                end = ConstraintSet {
                    val bgImage = createRefFor("bgImage")
                    val body = createRefFor("body")
                    val tempTextValueField = createRefFor("tempTextValueField")
                    val tempFeelsLikeTextField = createRefFor("tempFeelsLikeTextField")
                    val dateTimeTextField = createRefFor("dateTimeTextField")
                    val tempIcon = createRefFor("tempIcon")
                    val searchBar = createRefFor("searchBar")
                    constrain(searchBar) {
                        top.linkTo(parent.top, 8.dp)
                    }
                    constrain(bgImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.value(80.dp)
                    }
                    constrain(tempTextValueField) {
                        top.linkTo(searchBar.top, margin = 30.dp)
                        bottom.linkTo(dateTimeTextField.top)
                        start.linkTo(parent.start, margin = 23.dp)
                    }
                    constrain(tempFeelsLikeTextField) {
                        end.linkTo(parent.end, margin = 18.dp)
                        bottom.linkTo(bgImage.bottom, margin = 12.dp)
                    }
                    constrain(tempIcon) {
                        bottom.linkTo(tempTextValueField.bottom)
                        end.linkTo(parent.end, margin = 18.dp)
                    }
                    constrain(body) {
                        top.linkTo(bgImage.bottom, 8.dp)
                        height = Dimension.fillToConstraints
                        bottom.linkTo(parent.bottom, 0.dp)
                    }
                    constrain(dateTimeTextField) {
                        bottom.linkTo(bgImage.bottom, margin = 12.dp)
                        start.linkTo(tempTextValueField.start)
                    }
                },
                progress = computedProgress,
            ) {
                Box(
                    modifier = Modifier
                        .layoutId("body")
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    //content, not necessarily scrollable or list
                    CurrentWeatherScreenBottomSheetContent(
                        currentWeather = weatherData.currentWeather,
                        hourlyForecast = weatherData.weatherForecast.first().hourlyForecastData,
                        dayForecast = weatherData.weatherForecast,
                    )
                }
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .layoutId("bgImage")
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.weather_bg),
                    contentDescription = ""
                )

                Text(
                    "23°",
                    color = Color.White,
                    fontSize = 80.sp,
                    fontFamily = productSans,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.layoutId("tempTextValueField")
                )
                Text(
                    "Feels like 23°",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = productSans,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.layoutId("tempFeelsLikeTextField")
                )
                Text(
                    text = "12 December 2023",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = productSans,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.layoutId("dateTimeTextField")
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.layoutId("tempIcon")
                ) {
                    AsyncImage(
                        modifier = Modifier.size(107.dp),
                        colorFilter = ColorFilter.tint(purpleBgColor.copy(alpha = 0.75f)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(weatherData.currentWeather.icon)
                            .decoderFactory(factory = SvgDecoder.Factory()).build(),
                        contentDescription = null
                    )
                    Text(
                        text = "weatherCondition",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = productSans,
                        fontWeight = FontWeight(400)
                    )
                }
                SearchbarWithSuggestionView(modifier = Modifier
                    .layoutId("searchBar")
                    .padding(horizontal = 10.dp)
                    .animateContentSize(),
                    location = "location",
                    onPlaceSuggestionClick = {

                    },
                    onTextQueryChange = {},
                    suggestions = listOf()
                )
            }
        }
    }
}