package com.example.daggerhilttest.ui_components

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daggerhilttest.models.v2.GraphPoints
import com.example.daggerhilttest.screens.purpleColor
import com.example.daggerhilttest.ui.theme.productSans

@OptIn(ExperimentalTextApi::class)
@Composable
fun CurrentWeatherGraphV2(graphPoints: List<GraphPoints>, showPoints: Boolean = false) {
    val textMeasurer = rememberTextMeasurer()
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(purpleColor)
            .padding(10.dp)
    ) {
        Row {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(28.dp)
                    .width(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
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
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp),
                fontFamily = productSans,
                fontSize = 14.sp
            )
        }
        Canvas(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
        ) {

            // 7.5 percent of the total available width
            val p7_5_width = size.width * 0.075f
            // 10 percent of the total available height
            val p12_height = size.height * 0.12f

            val rangedGraphPointsList = createRangedList(
                graphPoints = graphPoints,
                size = size,
                heightFactor = p12_height,
                widthFactor = p7_5_width
            )
            val path = generateSmoothPath(rangedGraphPointsList, size)
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB7A5DF), Color(0xFFE0D4F8)
                    )
                ),
                style = Fill,
            )
            drawPath(
                path = path,
                color = Black.copy(alpha = 0.3f),
                style = Stroke(width = 4f),
            )
            rangedGraphPointsList.forEachIndexed { index, graphPointsV2 ->
                if (showPoints) {
                    drawCircle(
                        color = Black.copy(alpha = 0.6f), radius = 7f, center = Offset(
                            x = when (index) {
                                0 -> graphPointsV2.dayyX + 10f
                                6 -> graphPointsV2.dayyX - 10f
                                else -> graphPointsV2.dayyX
                            }, y = graphPointsV2.temppY
                        )
                    )
                }
                drawText(
                    textMeasurer = textMeasurer,
                    text = "${graphPoints[index].temp.toInt()}°",
                    style = TextStyle(fontSize = 11.sp, fontFamily = productSans),
                    topLeft = Offset(
                        x = when (index) {
                            0 -> graphPointsV2.dayyX + 4f
                            6 -> graphPointsV2.dayyX - 50f
                            else -> graphPointsV2.dayyX - textMeasurer.measure(
                                graphPoints[index].temp.toInt().toString()
                            ).size.width / 2.7f
                        }, y = graphPointsV2.temppY - textMeasurer.measure(
                            graphPoints[index].temp.toInt().toString()
                        ).size.height / 1.3f
                    )
                )
                drawText(
                    textMeasurer = textMeasurer, text = graphPoints[index].day, topLeft = Offset(
                        x = when (index) {
                            0 -> graphPointsV2.dayyX
                            6 -> graphPointsV2.dayyX - 55f
                            else -> graphPointsV2.dayyX - textMeasurer.measure(graphPoints[index].day).size.width / 3f
                        }, y = size.height - 30
                    ), style = TextStyle(fontSize = 11.sp, fontFamily = productSans)
                )
            }
            drawLine(
                color = purpleColor,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 5f
            )
            drawLine(
                color = purpleColor,
                start = Offset(size.width, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = 5f
            )
            drawLine(
                color = purpleColor,
                start = Offset(0f, size.height - 40),
                end = Offset(size.width, size.height - 40),
                strokeWidth = 5f
            )
        }
    }
}

@Composable
@Preview
fun CustomGraphPreview() {
    val list: List<GraphPoints> = remember {
        listOf(
            GraphPoints(temp = 33f, "Mon"),
            GraphPoints(temp = 32f, "Tue"),
            GraphPoints(temp = 29f, "Wed"),
            GraphPoints(temp = 30f, "Thu"),
            GraphPoints(temp = 32f, "Fri"),
            GraphPoints(temp = 28f, "Sat"),
            GraphPoints(temp = 34f, "Sun"),
        )
    }
    CurrentWeatherGraphV2(graphPoints = list)
}

private fun generateSmoothPath(list: List<GraphPointsMapped>, size: Size): Path {
    val path = Path()
    var prevX = list[0].dayyX
    var prevY = list[0].temppY
    path.moveTo(prevX, prevY)
    for (index in 1 until list.size) {
        val tempY = list[index].temppY
        val dayX = list[index].dayyX

        val cp1 = PointF((dayX + prevX) / 2f, prevY)
        val cp2 = PointF((dayX + prevX) / 2f, tempY)

        path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, dayX, tempY)
        prevX = dayX
        prevY = tempY
    }

    path.lineTo(size.width, size.height - 40)
    path.lineTo(0f, size.height - 40)
    path.close()
    return path
}

// Graph points range mapped to canvas size
data class GraphPointsMapped(val dayyX: Float, val temppY: Float)

fun mapValue(
    value: Float,
    fromRangeStart: Float,
    fromRangeEnd: Float,
    toRangeStart: Float,
    toRangeEnd: Float,
): Float {
    val fromRange = fromRangeEnd - fromRangeStart
    val toRange = toRangeEnd - toRangeStart

    val scaledValue = (value - fromRangeStart) / fromRange
    return toRangeStart + (scaledValue * toRange)
}

fun createRangedList(
    graphPoints: List<GraphPoints>, size: Size, widthFactor: Float, heightFactor: Float
): List<GraphPointsMapped> {
    return graphPoints.mapIndexed { index, points ->
        val y = mapValue(value = points.temp, fromRangeStart = graphPoints.maxOf {
            it.temp
        }, fromRangeEnd = graphPoints.minOf {
            it.temp
        },
            // Basically the max point in the graph will be at this min height from (0.0) from top
            toRangeStart = 30f,
            // subtracting the height factor will prevent the curve touching the bottom of the graph
            toRangeEnd = size.height - heightFactor
        )
        val x = mapValue(
            value = index.toFloat(),
            fromRangeStart = 0f,
            fromRangeEnd = graphPoints.lastIndex.toFloat(),
            // basically giving the spacing between canvas start and curve
            toRangeStart = 0f,
            // basically giving the spacing between canvas end and curve
            toRangeEnd = size.width
        )
        GraphPointsMapped(
            dayyX = x, temppY = y
        )
    }
}

