package com.example.daggerhilttest.ui_components

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


enum class SunLoaderState {
    Collapsed,
    Expanded,
    Original
}

@Composable
fun AnimatedSunLoader() {

    var currentScaleState by remember {
        mutableStateOf(SunLoaderState.Collapsed)
    }
    val transition = updateTransition(currentScaleState, label = "sun_loader_state")

    val sunLoaderScale by transition.animateFloat(
        label = "",
        transitionSpec = {
            tween(
                durationMillis = 5000,
                easing = LinearEasing
            )
        }) { sunLoaderState ->
        when (sunLoaderState) {
            SunLoaderState.Collapsed -> 0f
            SunLoaderState.Expanded -> 1.5f
            SunLoaderState.Original -> 1.0f
        }
    }


    var triggerAnimation by remember { mutableStateOf(false) }

    var scaleAnimProgress by remember {
        mutableStateOf(0f)
    }
    var rotationAnimProgress by remember {
        mutableStateOf(0f)
    }
    var expandAnimEnded by remember {
        mutableStateOf(false)
    }
    var collapseAnimEnded by remember {
        mutableStateOf(false)
    }
    val animatedSunRotation by animateFloatAsState(
        targetValue = if (collapseAnimEnded) 360f else 0f,
        animationSpec = tween(2500, easing = LinearEasing),
    )
    var targetValue: Float by remember {
        mutableStateOf(0f)
    }
    LaunchedEffect(key1 = triggerAnimation, key2 = rotationAnimProgress, key3 = expandAnimEnded) {
        targetValue = when {
            triggerAnimation -> 1.5f
            rotationAnimProgress >= 0.75f -> 0f
            expandAnimEnded -> 1f
            else ->
                0f

        }
    }

    val animatedSunScale by animateFloatAsState(
        finishedListener = { value ->
//            expandAnimEnded = true
            triggerAnimation = false
        },
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 500)
    )
    LaunchedEffect(key1 = Unit) {
//        triggerAnimation = true
        currentScaleState = SunLoaderState.Expanded

    }
    LaunchedEffect(key1 = animatedSunRotation, key2 = animatedSunScale) {
        Log.e("LaunchedEffect", "$animatedSunRotation")
//        if (animatedSunRotation == 360f) {
//            rotationAnimProgress = 0f
//            expandAnimEnded = false
//            scaleAnimProgress = 0f
//        }
//        collapseAnimEnded = expandAnimEnded && animatedSunScale <= 1.09f
    }

    LaunchedEffect(key1 = sunLoaderScale, animatedSunRotation) {
        when (sunLoaderScale) {
            0f -> {}
            1f -> {}
            1.5f -> {
                expandAnimEnded = true
                currentScaleState = SunLoaderState.Original
            }

            else -> {}
        }
        scaleAnimProgress =
            calculateAnimationProgress(
                sunLoaderScale
            )
        Log.d(
            "taggggg",
            "scale anim progress - $scaleAnimProgress sun loader scale - $sunLoaderScale"
        )
        rotationAnimProgress = calculatePercentage(
            startValue = 0f,
            endValue = 360f,
            currentValue = animatedSunRotation
        )
    }
    DrawSunWithTriangles(
        animatedSunScale = sunLoaderScale,
        scaleAnimProgress = scaleAnimProgress,
        isExpandAnim = expandAnimEnded.not(),
        rotateAnimProgress = rotationAnimProgress,
        modifier = Modifier.rotate(animatedSunRotation),
        sunLoaderState = currentScaleState
    )
}

@Composable
fun DrawSunWithTriangles(
    modifier: Modifier,
    animatedSunScale: Float,
    scaleAnimProgress: Float,
    rotateAnimProgress: Float,
    isExpandAnim: Boolean,
    sunLoaderState: SunLoaderState
) {
    Log.d("tagggg", "$isExpandAnim")
    val yellowColor = Color(0xFFFFF077)
    val boundaryOrangeColor = Color(0xFFFFB115)
    val boundaryOrangeColor_2 = Color(0xFFFFE719)
    val triangleBoundaryDisappearTime = 350
    val animatedTriangleHeight by animateFloatAsState(
        targetValue = if (rotateAnimProgress < 0.65f) 50f else 0f,
        animationSpec = tween(triangleBoundaryDisappearTime, easing = LinearEasing)
    )
    val animatedTriangleHeightAboveBoundary by animateFloatAsState(
        targetValue = if (rotateAnimProgress < 0.65f) 15f else 0f,
        animationSpec = tween(triangleBoundaryDisappearTime, easing = LinearEasing)
    )
    val circleRadius: Float = calculateCircleRadiusV2(scaleAnimProgress, 190f)

//        calculateCircleRadius(if (isExpandAnim) scaleAnimProgress else (scaleAnimProgress + (1f - scaleAnimProgress)))
    Canvas(
        modifier = modifier
            .scale(animatedSunScale)
            .size(200.dp)
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
        drawPath(path = path2, color = boundaryOrangeColor_2)
        drawPath(path = path, color = boundaryOrangeColor)
        scale(1f) {

        }

        rotate(0f) {
            drawCircle(
                color = yellowColor,
                radius = circleRadius,
                center = Offset(size.width / 2, size.height / 2)
            )
        }

    }
}

fun calculateCircleRadius(animationProgress: Float): Float {
    val minRadius = 0f
    val maxRadius = 190f
    val mappedRadius = minRadius + animationProgress * (maxRadius - minRadius)
    return mappedRadius.coerceIn(minRadius, maxRadius)
}

fun calculatePercentage(startValue: Float, endValue: Float, currentValue: Float): Float {
    require(startValue <= endValue) { "Start value must be less than or equal to end value" }
    require(currentValue in startValue..endValue) { "Current value must be between start and end values" }

    val range = endValue - startValue
    val distanceFromStart = currentValue - startValue
    val percentage = (distanceFromStart / range)
    return percentage.coerceIn(0f, 1f) // Ensure the percentage is between 0 and 1
}

fun calculatePercentageV2(minValue: Float, maxValue: Float, currentValue: Float): Float {
    require(minValue <= maxValue) { "minValue must be less than or equal to maxValue" }
    require(currentValue in minValue..maxValue) { "currentValue must be within the range defined by minValue and maxValue" }

    val range = maxValue - minValue
    val distanceFromMin = currentValue - minValue

    return (distanceFromMin / range) * 100
}

fun calculateAnimationProgress(scale: Float): Float {
    return when {
        scale <= 1.5f -> scale / 1.5f // First part of the animation: scale from 0f to 1.5f
        scale <= 2f -> 1f // Second part of the animation: scale is at 1.5f
        else -> (2f - scale) / 1f // Third part of the animation: scale from 1.5f to 0f
    }
}

fun calculateCircleRadiusV2(animationProgress: Float, maxRadius: Float): Float {
    // For the first part of the animation (0f to 1.5f), scale up the circle radius gradually
    return if (animationProgress <= 1.5f) {
        maxRadius * animationProgress
    }
    // For the second part of the animation (1.5f to 1f), keep the circle radius constant at maxRadius
    else if (animationProgress <= 2f) {
        maxRadius
    }
    // For the third part of the animation (1f to 0f), scale down the circle radius gradually
    else {
        maxRadius
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
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY, height = triangleHeight
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


fun calculatePointAtHeightFromArc(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    height: Float
): Pair<Float, Float> {
    // Step 1: Calculate the center point of the arc
    val centerX = (startX + endX) / 2
    val centerY = (startY + endY) / 2

    // Step 2: Calculate the angle of the arc
    val dx = startX - centerX
    val dy = startY - centerY
    val angle = atan2(dy, dx)

    // Step 3: Find the midpoint of the arc
    val midpointX = centerX + (startX - centerX) / 8.5f
    val midpointY = centerY + (startY - centerY) / 8.5f

    // Step 4: Calculate the coordinates of the point at a specified height above the midpoint
    val pointX = midpointX - height * cos(angle - PI / 2).toFloat()
    val pointY = midpointY - height * sin(angle - PI / 2).toFloat()

    return Pair(pointX, pointY)
}

@Preview
@Composable
fun SunLoaderCanvasPreview() {
    DrawSunWithTriangles(
        animatedSunScale = 1f,
        scaleAnimProgress = 1f,
        isExpandAnim = false,
        rotateAnimProgress = 1f,
        modifier = Modifier,
        sunLoaderState = SunLoaderState.Expanded
    )
}

@Preview
@Composable
fun SunLoaderPreview() {
    var trigger by remember {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier, contentAlignment = Alignment.Center) {
            AnimatedSunLoader()
            Box(
                modifier = Modifier.size(190.dp).clip(CircleShape).background(Color.Red)
            )
        }
    }
}









