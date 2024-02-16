package com.example.daggerhilttest.activities

import android.graphics.ColorFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.daggerhilttest.R
import com.example.daggerhilttest.databinding.ActivityMainV2Binding
import com.example.daggerhilttest.models.v1.WeatherExtraDetailItem
import com.example.daggerhilttest.screens.ButtonsLayout
import com.example.daggerhilttest.screens.SearchBottomSheetFragment
import com.example.daggerhilttest.screens.SearchbarWithSuggestionView
import com.example.daggerhilttest.screens.WeatherDetailsGridView
import com.example.daggerhilttest.screens.WeatherExtraDetailItem
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.example.daggerhilttest.ui_components.ExpandableSearchBar
import com.example.daggerhilttest.util.WeatherExtraDetailType
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivityV2 : AppCompatActivity() {

    private lateinit var binding: ActivityMainV2Binding
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainV2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                val maxCornerRadius = 100f // Maximum corner radius
                val minCornerRadius = 0f  // Minimum corner radius

                val startTextSize = 114.dp // Starting text size
                val endTextSize = 54.dp   // Ending text size

                // Interpolate the text size based on progress
                val currentTextSize = startTextSize - (startTextSize - endTextSize) * progress

                val currentCornerRadius =
                    (maxCornerRadius * (1 - progress)).coerceAtLeast(minCornerRadius)
                if (startId == R.id.start) {
                    if(progress > 0.1 && progress < 0.95) {
                        binding.tvTempValue.textSize = currentTextSize.value

                        val shapeAppearanceModel = ShapeAppearanceModel.builder()
                            .setBottomLeftCorner(CornerFamily.ROUNDED, currentCornerRadius)
                            .setBottomRightCorner(CornerFamily.ROUNDED, currentCornerRadius)
                            .build()
                        binding.iv.shapeAppearanceModel = shapeAppearanceModel
                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(currentId == R.id.start) {
                    val shapeAppearanceModel = ShapeAppearanceModel.builder()
                        .setBottomLeftCorner(CornerFamily.ROUNDED, 100f)
                        .setBottomRightCorner(CornerFamily.ROUNDED, 100f)
                        .build()
                    binding.iv.shapeAppearanceModel = shapeAppearanceModel
                } else if(currentId == R.id.end) {
                    val shapeAppearanceModel = ShapeAppearanceModel.builder()
                        .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                        .build()
                    binding.iv.shapeAppearanceModel = shapeAppearanceModel
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })

        val bottomSheet = SearchBottomSheetFragment()
        val openBottomSheet = { bottomSheet.show(supportFragmentManager, "tag") }
        binding.buttonsLayout.setContent {
            ButtonsLayout(includeTopPadding = false)
        }
        binding.composeView.setContent {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                repeat(10) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(12.dp)
                            .background(
                                Color.Black
                            )
                    )
                }
                LazyRow(
                    state = rememberLazyListState(), modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Black
                        )
                ) {
                    items(count = 10) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(horizontal = 10.dp)
                                .background(Color.Red)
                        )
                    }
                }
                Button(modifier = Modifier.align(Alignment.CenterHorizontally),onClick = {
                    openBottomSheet.invoke()
                }) {
                    Text("Click me")
                }
            }
        }
    }
}

@Preview
@Composable
fun testPreview() {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth()
            .background(Color.Black)
            .padding(20.dp)
    ) {
        repeat(10) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .padding(horizontal = 10.dp)
                    .background(Color.Red)
            )
        }
    }
}