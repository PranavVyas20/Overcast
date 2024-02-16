import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class SwipingStates {
    EXPANDED,
    COLLAPSED
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun NestedScrollingLayout() {
    val swipingState = rememberSwipeableState(initialValue = SwipingStates.COLLAPSED)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val constraintsScope = this
        val maxHeight = with(LocalDensity.current) {
            constraintsScope.maxHeight.toPx()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Green)
                .swipeable(
                    state = swipingState,
                    anchors = mapOf(
                        0f to SwipingStates.EXPANDED,
                        maxHeight to SwipingStates.COLLAPSED
                    ),
                    orientation = Orientation.Vertical
                )
                .offset {
                    IntOffset(
                        x = 0,
                        y = swipingState.offset.value.roundToInt()
                    )
                }) {
/*            LazyColumn(
                modifier = Modifier
                    .swipeable(
                        state = swipingState,
                        anchors = mapOf(
                            0f to SwipingStates.EXPANDED,
                            maxHeight to SwipingStates.COLLAPSED
                        ),
                        orientation = Orientation.Vertical
                    )
                    .background(Color.Red.copy(alpha = 0.2f))
                    .fillMaxSize(),
                state = rememberLazyListState()
            ) {
                items(count = 100) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(12.dp)
                            .background(Color.Green)
                    ) {}
                }
            }*/
        }
    }
}

@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeableSample() {
    val width = 96.dp
    val squareSize = 48.dp

    val swipeableState = rememberSwipeableState(0)
    val progress = swipeableState.progress.fraction
    Log.d("progress-tag", progress.toString())
    val sizePx = with(LocalDensity.current) { 300.dp.toPx() }
    val anchors = mapOf(
        0f to 0,
        100f to 1
    )
    Box(
        modifier = Modifier
            .width(100.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                resistance = null,
                orientation = Orientation.Horizontal,
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .width(100.dp)
                .height(40.dp)
                .background(Color.DarkGray)
        )
    }
}