//package com.example.daggerhilttest.screens
//
//import android.text.TextUtils
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.modifier.modifierLocalConsumer
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import co.yml.charts.axis.AxisData
//import co.yml.charts.common.model.PlotType
//import co.yml.charts.common.model.Point
//import co.yml.charts.ui.linechart.LineChart
//import co.yml.charts.ui.linechart.model.GridLines
//import co.yml.charts.ui.linechart.model.IntersectionPoint
//import co.yml.charts.ui.linechart.model.Line
//import co.yml.charts.ui.linechart.model.LineChartData
//import co.yml.charts.ui.linechart.model.LinePlotData
//import co.yml.charts.ui.linechart.model.LineStyle
//import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
//import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
//import co.yml.charts.ui.linechart.model.ShadowUnderLine
//import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
//import com.patrykandpatryk.vico.compose.axis.horizontal.topAxis
//import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
//import com.patrykandpatryk.vico.compose.chart.Chart
//import com.patrykandpatryk.vico.compose.chart.line.lineChart
//import com.patrykandpatryk.vico.compose.chart.line.lineSpec
//import com.patrykandpatryk.vico.compose.component.shape.shader.verticalGradient
//import com.patrykandpatryk.vico.compose.component.shape.textComponent
//import com.patrykandpatryk.vico.core.axis.AxisPosition
//import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
//import com.patrykandpatryk.vico.core.component.marker.MarkerComponent
//import com.patrykandpatryk.vico.core.component.shape.LineComponent
//import com.patrykandpatryk.vico.core.component.text.TextComponent
//import com.patrykandpatryk.vico.core.entry.entryModelOf
//import com.patrykandpatryk.vico.core.marker.Marker
//import kotlin.math.truncate
//
//@Composable
//fun rememberMarker() =
//    MarkerComponent(
//        label = textComponent(color = Color.Red),
//        guideline = null,
//        indicator = null
//    )
//
//private val markerMap: Map<Float, Marker>
//    @Composable get() = mapOf(
//        4f to rememberMarker(),
//        0f to rememberMarker(),
//        1f to rememberMarker(),
//        2f to rememberMarker(),
//        3f to rememberMarker()
//    )
//
//@Composable
//fun HourlyGraph(modifier: Modifier) {
//    val model1 = entryModelOf(0, 2, 4, 0, 2)
//
//    val yAxisValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { i, _ ->
//        "${i.toInt()}℃"
//    }
//
//    val yellow = Color(0xFF000000)
//    Box(modifier = modifier) {
//        Chart(
//            chart = lineChart(
////            persistentMarkers = markerMap,
//                lines = listOf(
//                    lineSpec(
//                        lineColor = yellow,
//                        lineBackgroundShader = verticalGradient(
//                            arrayOf(yellow.copy(0.5f), yellow.copy(alpha = 0f)),
//                        ),
//                    ),
//                )
//            ),
//            model = model1,
//            startAxis = startAxis(
//                axis = null,
//                guideline = null,
//                tick = null,
//                valueFormatter = yAxisValueFormatter
//            ),
//            bottomAxis = bottomAxis(
//                label = textComponent(color = Color.Black),
//                axis = null,
//                tick = null,
//                guideline = null,
//            ),
//        )
//    }
//
//}