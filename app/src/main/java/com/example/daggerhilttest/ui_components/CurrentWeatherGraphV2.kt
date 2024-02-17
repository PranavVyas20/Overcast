package com.example.daggerhilttest.ui_components

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daggerhilttest.screens.purpleColor
import com.example.daggerhilttest.ui.theme.grayLineColor

@OptIn(ExperimentalTextApi::class)
@Composable
fun CurrentWeatherGraphV2(graphPoints: List<GraphPoints>, showPoints: Boolean = false) {
    val textMeasurer = rememberTextMeasurer()
    val sortedList = remember { graphPoints.sortedBy { it.temp } }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(purpleColor)
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
        ) {

            // 7.5 percent of the total available width
            val p7_5_width = size.width * 0.075f
            // 10 percent of the total available height
            val p10_height = size.height * 0.1f

            val rangedGraphPointsList =
                createRangedList(
                    graphPoints = graphPoints,
                    size = size,
                    heightFactor = p10_height,
                    widthFactor = p7_5_width
                )
            val path = generateSmoothPath(rangedGraphPointsList, size)
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB7A5DF),
                        Color(0xFFE0D4F8)
                    )
                ),
                style = Fill,
            )
            drawPath(
                path = path,
                color = Black,
                style = Stroke(width = 2f),
            )
            rangedGraphPointsList.forEachIndexed { index, graphPointsV2 ->
                if (showPoints)
                    drawCircle(
                        color = Black,
                        radius = 9f,
                        center = Offset(
                            x = if (index == 0) {
                                graphPointsV2.dayyX + 12f
                            } else if (index == 6) {
                                graphPointsV2.dayyX - 12f
                            } else {
                                graphPointsV2.dayyX
                            },
                            y = graphPointsV2.temppY
                        )
                    )
                drawText(
                    textMeasurer = textMeasurer,
                    text = graphPoints[index].day,
                    topLeft = Offset(
                        x = if (index == 0) {
                            graphPointsV2.dayyX + 20f - textMeasurer.measure(graphPoints[index].day).size.width / 3f
                        } else if (index == 6) {
                            graphPointsV2.dayyX - 24f - textMeasurer.measure(graphPoints[index].day).size.width / 3f
                        } else {
                            graphPointsV2.dayyX - textMeasurer.measure(graphPoints[index].day).size.width / 3f
                        },
                        y = size.height - 30
                    ),
                    style = TextStyle(fontSize = 10.sp)
                )
            }
            sortedList.forEachIndexed { index, points ->
                val y =
                    mapValue(
                        value = points.temp,
                        fromRangeStart = sortedList.maxOf {
                            it.temp
                        },
                        fromRangeEnd = sortedList.minOf {
                            it.temp
                        },
                        toRangeStart = 0f,
                        toRangeEnd = size.height - p10_height - 20f
                    )
                val x =
                    mapValue(
                        value = index.toFloat(),
                        fromRangeStart = 0f,
                        fromRangeEnd = sortedList.lastIndex.toFloat(),
                        toRangeStart = 100f,
                        toRangeEnd = size.width - 100f
                    )

                if (index == 0 || index == 6) {
                    if(index == 6)
                    drawLine(
                        color = Gray,
                        strokeWidth = 4f,
                        start = Offset(
                            x = 100f,
                            y = y + textMeasurer.measure(
                                points.temp.toInt().toString()
                            ).size.height / 2f
                        ),
                        end = Offset(
                            x = size.width, y = y + textMeasurer.measure(
                                points.temp.toInt().toString()
                            ).size.height / 2f
                        )
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "${points.temp.toInt()} °C",
                        topLeft = Offset(20f, y),
                        style = TextStyle(fontSize = 10.sp)
                    )
                }
                /*drawLine(
                    color = Color.Black,
                    start = Offset(x, y),
                    end = Offset(
                        x = x,
                        y = size.height - textMeasurer.measure(points.day).size.height
                    ),
                    pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 10f))
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = points.temp.toInt().toString(),
                    topLeft = Offset(
                        x - textMeasurer.measure(
                            points.temp.toInt().toString()
                        ).size.width / 3.0f,
                        y - textMeasurer.measure(points.temp.toInt().toString()).size.height
                    ),
                    style = TextStyle(fontSize = 10.sp, color = Color.Black)
                )*/
            }
            val midTemp = (sortedList.maxOf { it.temp } + sortedList.minOf { it.temp }) / 2
            val maxY = mapValue(
                value = sortedList[6].temp,
                fromRangeStart = sortedList.maxOf {
                    it.temp
                },
                fromRangeEnd = sortedList.minOf {
                    it.temp
                },
                toRangeStart = 0f,
                toRangeEnd = size.height - p10_height - 20f
            )
            val minY = mapValue(
                value = sortedList[0].temp,
                fromRangeStart = sortedList.maxOf {
                    it.temp
                },
                fromRangeEnd = sortedList.minOf {
                    it.temp
                },
                toRangeStart = 0f,
                toRangeEnd = size.height - p10_height - 20f
            )
            val midY = (maxY + minY) / 2

            drawLine(
                color = Gray,
                strokeWidth = 4f,
                start = Offset(
                    x = 100f,
                    y = midY + textMeasurer.measure(
                        midTemp.toInt().toString()
                    ).size.height / 2f
                ),
                end = Offset(
                    x = size.width, y = midY + textMeasurer.measure(
                        midTemp.toInt().toString()
                    ).size.height / 2f
                )
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "${midTemp.toInt()} °C",
                topLeft = Offset(20f, midY),
                style = TextStyle(fontSize = 10.sp)
            )
            drawLine(
                color = purpleColor,
                start = Offset(100f, size.height - 40),
                end = Offset(100f, 0f),
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

private fun generateSmoothPath(list: List<GraphPointsV2>, size: Size): Path {
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

    path.lineTo(size.width , size.height - 40)
    path.lineTo(100f, size.height - 40)
    path.close()
    return path
}

data class GraphPoints(val temp: Float, val day: String)

// Graph points range mapped to canvas size
data class GraphPointsV2(val dayyX: Float, val temppY: Float)

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
    graphPoints: List<GraphPoints>,
    size: Size,
    widthFactor: Float,
    heightFactor: Float
): List<GraphPointsV2> {
    return graphPoints.mapIndexed { index, points ->
        val y =
            mapValue(
                value = points.temp,
                fromRangeStart = graphPoints.maxOf {
                    it.temp
                },
                fromRangeEnd = graphPoints.minOf {
                    it.temp
                },
                toRangeStart = 0f,
                // subtracting the height factor will prevent the curve touching the bottom of the graph
                toRangeEnd = size.height - heightFactor
            )
        val x =
            mapValue(
                value = index.toFloat(),
                fromRangeStart = 0f,
                fromRangeEnd = graphPoints.lastIndex.toFloat(),
                // basically giving the spacing between canvas start and curve
                toRangeStart = 0f + 100f,
                // basically giving the spacing between canvas end and curve
                toRangeEnd = size.width
            )
        GraphPointsV2(
            dayyX = x,
            temppY = y
        )
    }
}

