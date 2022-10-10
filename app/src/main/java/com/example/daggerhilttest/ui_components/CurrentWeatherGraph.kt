package com.example.daggerhilttest.ui_components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.daggerhilttest.models.CurrentWeather
import com.example.daggerhilttest.models.CurrentWeatherGraph
import com.example.daggerhilttest.models.HourlyForecastLocal
import com.example.daggerhilttest.models.Weather
import com.example.daggerhilttest.ui.theme.graphLineBottomColor
import com.example.daggerhilttest.ui.theme.graphLineColor
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.example.daggerhilttest.util.marker
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.compose.chart.line.lineSpec
import com.patrykandpatryk.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatryk.vico.core.axis.AxisPosition
import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatryk.vico.core.chart.line.LineChart
import com.patrykandpatryk.vico.core.entry.ChartEntryModel
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry
import com.patrykandpatryk.vico.core.entry.entryModelOf
import com.patrykandpatryk.vico.core.marker.Marker
import java.util.*
import kotlin.random.Random
import kotlin.random.Random.Default.nextFloat


private var axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { _, _ -> "" }

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CurrentWeatherGraph(currentWeatherGraph: CurrentWeatherGraph?, visibility: Boolean) {

    if (currentWeatherGraph != null) {
        currentWeatherGraph.timeStampMap!!.forEach { (key, _) ->
            currentWeatherGraph.markerMap!![key] = marker()
        }
        axisValueFormatter = AxisValueFormatter { i, j ->
            currentWeatherGraph.timeStampMap[i].toString()
        }
    }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentHeight()
            .placeholder(
                visible = visibility,
                color = shimmerColor,
                shape = RoundedCornerShape(7.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White,
                )
            )
    ) {
        if(currentWeatherGraph != null) {
            Chart(
                chart = lineChart(
                    lines = listOf(
                        lineSpec(
                            lineColor = graphLineColor, lineBackgroundShader = verticalGradient(
                                arrayOf(
                                    graphLineBottomColor.copy(alpha = 0.5f),
                                    graphLineBottomColor.copy(alpha = 0.5f)
                                )
                            )
                        )
                    ),
                    spacing = 60.dp,
                    persistentMarkers = currentWeatherGraph.markerMap,
                ),
                chartModelProducer = currentWeatherGraph.chartProducer!!,
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.White),
                startAxis = startAxis(),
                bottomAxis = bottomAxis(valueFormatter = axisValueFormatter)
            )
        } else {
            Text(text = "Unable to fetch graph data")
        }
    }
}