package com.example.daggerhilttest.ui_components

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

enum class SunFlamesStateV2 {
    expanded, collapsed, original
}

const val expandedScale = 1f
const val originalScale = 0.5f
const val collapsedScale = 0f
const val sunRadius = 190f
const val animDuration = 700

@Composable
fun SunLoaderV2(onAnimEnd: () -> Unit, key: Int) {
    val yellowColor = Color(0xFFFFF077)
    val boundaryOrangeColor = Color(0xFFFFB115)
    val boundaryOrangeColor_2 = Color(0xFFFFE719)
    val animatedTriangleHeightAboveBoundary = 15f
    val animatedTriangleHeight = 50f

    // Sun flames states
    var sunFlamesState by remember {
        mutableStateOf(SunFlamesStateV2.collapsed)
    }
    val sunFlameTransition =
        updateTransition(targetState = sunFlamesState, label = "sunFlameTransition")

    val sunFlameScale by sunFlameTransition.animateFloat(label = "", transitionSpec = {
        tween(
            durationMillis = animDuration, easing = FastOutSlowInEasing
        )
    }) { sunFlameState ->
        when (sunFlameState) {
            SunFlamesStateV2.expanded -> expandedScale
            SunFlamesStateV2.collapsed -> collapsedScale
            SunFlamesStateV2.original -> originalScale
        }
    }

    // Sun states
    var sunState by remember {
        mutableStateOf(SunFlamesStateV2.collapsed)
    }
    val sunTransition = updateTransition(targetState = sunState, label = "sunTransition")

    val sunScale by sunTransition.animateFloat(label = "", transitionSpec = {
        tween(
            durationMillis = 600, easing = FastOutSlowInEasing
        )
    }) { sunStateee ->
        when (sunStateee) {
            SunFlamesStateV2.expanded -> expandedScale
            SunFlamesStateV2.collapsed -> collapsedScale
            SunFlamesStateV2.original -> originalScale
        }
    }

    var triggerRotation by remember {
        mutableStateOf(false)
    }

    val sunRotation by animateFloatAsState(
        targetValue = if (triggerRotation) 360f else 0f,
        animationSpec = tween(durationMillis = 1800, easing = LinearEasing),
        finishedListener = {
            onAnimEnd.invoke()
        }
    )

    // Initially trigger the sun flame scale animation
    LaunchedEffect(key1 = Unit) {
        sunFlamesState = SunFlamesStateV2.expanded
    }

    LaunchedEffect(key1 = sunRotation) {
        Log.d("sun_rotation", "$sunRotation")
        if (sunRotation >= 340f) {
            sunFlamesState = SunFlamesStateV2.collapsed
            delay(250)
            sunState = SunFlamesStateV2.collapsed
        }
    }
    LaunchedEffect(key1 = sunFlameScale, sunScale) {
        // Start expanding the sun scale

        if (sunRotation > 0f) {
            return@LaunchedEffect
        }
        if (sunScale == 1.0f && sunFlameScale == 1.0f) {
            sunState = SunFlamesStateV2.original
        } else if (sunFlamesState == SunFlamesStateV2.expanded && sunFlameScale >= 0.25f && sunState == SunFlamesStateV2.collapsed) {
            sunState = SunFlamesStateV2.expanded
        } else if (sunState == SunFlamesStateV2.original && sunFlamesState != SunFlamesStateV2.original) {
            sunFlamesState = SunFlamesStateV2.original
        } else if (sunFlamesState == SunFlamesStateV2.original && sunState == SunFlamesStateV2.original && sunScale <= 0.6f && sunFlameScale <= 0.6f) {
            triggerRotation = true
        }

    }
    Log.d("key_tag", "$key, rot - $triggerRotation")

    Canvas(
        modifier = Modifier.size(200.dp)
    ) {

        val path = getPath(
            circleCenter = Offset(size.width / 2, size.height / 2),
            circleRadius = 190f,
            numTriangles = 10,
            heightAboveBoundary = animatedTriangleHeightAboveBoundary,
            triangleHeight = animatedTriangleHeight
        )
        val path2 = getPath(
            circleCenter = Offset(size.width / 2, size.height / 2),
            circleRadius = 190f,
            numTriangles = 10,
            heightAboveBoundary = animatedTriangleHeightAboveBoundary,
            triangleHeight = animatedTriangleHeight,
            rotationDegree = 20f
        )
        scale(sunFlameScale) {
            rotate(sunRotation) {
                drawPath(path = path2, color = boundaryOrangeColor_2)
                drawPath(path = path, color = boundaryOrangeColor)
            }

        }

        scale(sunScale) {
            drawCircle(
                color = yellowColor,
                radius = sunRadius,
                center = Offset(size.width / 2, size.height / 2)
            )
        }

    }
}

private fun getPath(
    triangleHeight: Float,
    circleCenter: Offset,
    circleRadius: Float,
    numTriangles: Int,
    heightAboveBoundary: Float,
    rotationDegree: Float = 0f // Degree by which to rotate the path
): Path {
    val path = Path()
    val triangleAngle = 360f / numTriangles
    // Draw triangles along the circle boundary
    repeat(numTriangles) { index ->
        val angle = index * triangleAngle
        var startX =
            circleCenter.x + (circleRadius + heightAboveBoundary) * cos(Math.toRadians(angle.toDouble())).toFloat()
        var startY =
            circleCenter.y + (circleRadius + heightAboveBoundary) * sin(Math.toRadians(angle.toDouble())).toFloat()

        var endX =
            circleCenter.x + (circleRadius + heightAboveBoundary) * cos(Math.toRadians((angle + triangleAngle).toDouble())).toFloat()
        var endY =
            circleCenter.y + (circleRadius + heightAboveBoundary) * sin(Math.toRadians((angle + triangleAngle).toDouble())).toFloat()

        val midValues = calculatePointAtHeightFromArc(
            startX = startX, startY = startY, endX = endX, endY = endY, height = triangleHeight
        )
        var middleX = midValues.first
        var middleY = midValues.second

        // Apply rotation to each vertex
        startX -= circleCenter.x
        startY -= circleCenter.y
        endX -= circleCenter.x
        endY -= circleCenter.y
        middleX -= circleCenter.x
        middleY -= circleCenter.y

        val cosTheta = cos(Math.toRadians(rotationDegree.toDouble())).toFloat()
        val sinTheta = sin(Math.toRadians(rotationDegree.toDouble())).toFloat()

        val rotatedStartX = startX * cosTheta - startY * sinTheta + circleCenter.x
        val rotatedStartY = startX * sinTheta + startY * cosTheta + circleCenter.y
        val rotatedEndX = endX * cosTheta - endY * sinTheta + circleCenter.x
        val rotatedEndY = endX * sinTheta + endY * cosTheta + circleCenter.y
        val rotatedMiddleX = middleX * cosTheta - middleY * sinTheta + circleCenter.x
        val rotatedMiddleY = middleX * sinTheta + middleY * cosTheta + circleCenter.y

        path.lineTo(rotatedStartX, rotatedStartY)
        path.lineTo(rotatedMiddleX, rotatedMiddleY)
        path.lineTo(rotatedEndX, rotatedEndY)
    }
    return path
}

@Preview
@Composable
fun SunLoaderPreviewV2() {
    var key by remember {
        mutableStateOf(0)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SunLoaderV2(key = key, onAnimEnd = {
            key++
        })
    }
}

@Preview
@Composable
fun testRotation() {
    var totalRotation by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    val rotation by animateFloatAsState(
        targetValue = totalRotation,
        animationSpec = tween(5000, easing = FastOutLinearInEasing),
        finishedListener = {
            scope.launch {
                delay(200)
                totalRotation += 360f

            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        delay(1500)
        totalRotation += 360f
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .rotate(rotation)
                .background(Color.Red)
        )
    }
}
