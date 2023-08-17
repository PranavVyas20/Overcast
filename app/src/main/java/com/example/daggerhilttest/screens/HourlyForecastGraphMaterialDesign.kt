package com.example.daggerhilttest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.daggerhilttest.models.v2.WeatherForecastData
import com.example.daggerhilttest.ui.theme.darkPurpleColor
import com.example.daggerhilttest.ui.theme.productSans
import com.example.daggerhilttest.ui.theme.purpleBgColor
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.compose.chart.line.lineSpec
import com.patrykandpatryk.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatryk.vico.compose.component.shape.textComponent
import com.patrykandpatryk.vico.core.axis.AxisPosition
import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatryk.vico.core.component.shape.LineComponent
import com.patrykandpatryk.vico.core.component.text.TextComponent
import com.patrykandpatryk.vico.core.dimensions.MutableDimensions
import com.patrykandpatryk.vico.core.entry.FloatEntry
import com.patrykandpatryk.vico.core.entry.entryModelOf

@Composable
fun HourlyGraph(modifier: Modifier, dayForecast: List<WeatherForecastData>) {
    val graphPoints = remember {
        handleGraphPointsCreationV2(dayForecast)
    }
    val graphModel = entryModelOf(graphPoints)
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val yAxisValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { i, _ ->
        "${i.toInt()}°"
    }
    val xAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { i, j ->
        days[i.toInt()]
    }

    val Black = Color(0xFF000000)
    ConstraintLayout(
        modifier = modifier
    ) {
        val (graph, icon, headingTextField) = createRefs()

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
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "hourly_forecast_icon"
            )
        }

        Text(
            text = "Day Forecast",
            fontSize = 14.sp,
            fontFamily = productSans,
            letterSpacing = 0.15.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.constrainAs(headingTextField) {
                centerVerticallyTo(icon)
                start.linkTo(icon.end, margin = 8.dp)
            })
        Chart(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .constrainAs(graph) {
                    top.linkTo(icon.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 6.dp)
                },
            isHorizontalScrollEnabled = true,
            chart = lineChart(
                spacing = 40.dp,
                minY = graphPoints.minOf { it.y } - 2,
                lines = listOf(
                    lineSpec(
                        lineColor = Color.Black,
                        lineBackgroundShader = verticalGradient(
                            arrayOf(darkPurpleColor.copy(0.4f), darkPurpleColor.copy(alpha = 0f)),
                        ),
                    ),
                )
            ),
            model = graphModel,
            startAxis = startAxis(
                axis = null,
                tick = null,
                label = textComponent(
                    margins = MutableDimensions(
                        verticalDp = 10f,
                        horizontalDp = 20f
                    )
                ),
                guideline = LineComponent(Color.LightGray.toArgb(), thicknessDp = 1.5f),
                valueFormatter = yAxisValueFormatter
            ),
            bottomAxis = bottomAxis(
                valueFormatter = xAxisValueFormatter,
                guideline = null,
                tick = null,
                label = textComponent(
                    margins = MutableDimensions(
                        verticalDp = 20f,
                        horizontalDp = 0f
                    )
                )
            ),
        )
    }
}

private fun handleGraphPointsCreationV2(dayForecast: List<WeatherForecastData>): List<FloatEntry> {
    return dayForecast.take(7).mapIndexed { index, weatherForecastData ->
        FloatEntry(x = index.toFloat(), y = weatherForecastData.temp.toFloat())
    }
}

@Preview
@Composable
fun GraphPreview() {
    Surface {
        val list = listOf<WeatherForecastData>(
            WeatherForecastData(
                temp = 28.0,
                minTemp = 0.0,
                maxTemp = 0.0,
                hourlyForecastData = listOf(),
                icon = "",
                dateTime = ""
            ),
            WeatherForecastData(
                temp = 29.0,
                minTemp = 0.0,
                maxTemp = 0.0,
                hourlyForecastData = listOf(),
                icon = "",
                dateTime = ""
            ),
            WeatherForecastData(
                temp = 30.0,
                minTemp = 0.0,
                maxTemp = 0.0,
                hourlyForecastData = listOf(),
                icon = "",
                dateTime = ""
            ),
            WeatherForecastData(
                temp = 28.0,
                minTemp = 0.0,
                maxTemp = 0.0,
                hourlyForecastData = listOf(),
                icon = "",
                dateTime = ""
            ),
            WeatherForecastData(
                temp = 29.0,
                minTemp = 0.0,
                maxTemp = 0.0,
                hourlyForecastData = listOf(),
                icon = "",
                dateTime = ""
            ),
            WeatherForecastData(
                temp = 28.0,
                minTemp = 0.0,
                maxTemp = 0.0,
                hourlyForecastData = listOf(),
                icon = "",
                dateTime = ""
            ),
        )
        HourlyGraph(modifier = Modifier, list)
    }
}

