package com.example.daggerhilttest.ui_components

import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.DefaultStrokeLineJoin
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toIntRect
import androidx.compose.ui.unit.toSize
import com.example.daggerhilttest.R
import com.example.daggerhilttest.screens.purpleColor
import com.example.daggerhilttest.ui.theme.purpleBgColor
import com.example.daggerhilttest.ui.theme.purpleBgColor2
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.math.roundToInt


@Composable
fun canvas2(graphPoints: List<GraphPoints>) {
    val textMeasurer = rememberTextMeasurer()
    val sortedList = remember { graphPoints.sortedByDescending { it.temp } }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(purpleColor)
            .padding(top = 40.dp)
    ) {
        Canvas(
            modifier = Modifier
                .height(205.dp)
                .fillMaxWidth()
        ) {

            // 7.5 percent of the total available width
            val p7_5 = (size.width * 0.075f)
            sortedList.forEachIndexed { index, graphPoints ->
                val y1 = mapValue(
                    value = graphPoints.temp,
                    fromRangeStart = sortedList.maxOf {
                        it.temp
                    },
                    fromRangeEnd = sortedList.minOf {
                        it.temp
                    },
                    toRangeStart = 0f,
                    toRangeEnd = size.height - p7_5
                )
                /*                drawText(
                                    textMeasurer = textMeasurer,
                                    text = graphPoints.temp.toString(),
                                    topLeft = Offset(
                                        x = 0f,
                                        y = y1 - textMeasurer.measure(graphPoints.temp.toString()).size.height / 2.5f
                                    ),
                                    style = TextStyle(fontSize = 10.sp)
                                )*/
            }
            val rangedGraphPointsList =
                createRangedList(graphPoints = graphPoints, size = size, factor = p7_5)
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
                color = Color.Black,
                style = Stroke(width = 5f),
            )
            graphPoints.forEachIndexed { index, points ->
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
                        toRangeEnd = size.height - p7_5
                    )
                val x =
                    mapValue(
                        value = index.toFloat(),
                        fromRangeStart = 0f,
                        fromRangeEnd = sortedList.lastIndex.toFloat(),
                        toRangeStart = if (index == 0) 0f + textMeasurer.measure(
                            points.temp.toInt().toString()
                        ).size.width / 2f else 0f,
                        toRangeEnd = if (index == graphPoints.lastIndex) size.width - textMeasurer.measure(
                            points.temp.toInt().toString()
                        ).size.width / 2f else size.width
                    )
                // drawing temp values above circles
                /*                drawCircle(
                                    color = Color.Red,
                                    radius = 8f,
                                    center = Offset(x, y)
                                )*/
                drawLine(
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
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = points.day,
                    topLeft = Offset(
                        x = x - textMeasurer.measure(points.day).size.width / 2.6f,
                        y = size.height - size.height * 0.1f
                    ),
                    style = TextStyle(fontSize = 10.sp)
                )
            }
        }
    }
}

@Composable
@Preview
fun CustomGraphPreview() {
    val list: List<GraphPoints> = remember {
        listOf(
            GraphPoints(temp = 33f, "M"),
            GraphPoints(temp = 32f, "T"),
            GraphPoints(temp = 29f, "W"),
            GraphPoints(temp = 30f, "T"),
            GraphPoints(temp = 31f, "F"),
            GraphPoints(temp = 29f, "S"),
            GraphPoints(temp = 30f, "S"),
        )
    }
    canvas2(graphPoints = list)
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

    path.lineTo(size.width, size.height)
    path.lineTo(0f, size.height)
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
    factor: Float
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
                toRangeEnd = size.height - factor
            )
        val x =
            mapValue(
                value = index.toFloat(),
                fromRangeStart = 0f,
                fromRangeEnd = graphPoints.lastIndex.toFloat(),
                toRangeStart = 0f,
                toRangeEnd = size.width
            )
        GraphPointsV2(
            dayyX = x,
            temppY = y
        )
    }
}

