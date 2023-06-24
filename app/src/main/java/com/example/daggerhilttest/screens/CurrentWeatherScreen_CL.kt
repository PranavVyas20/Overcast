package com.example.daggerhilttest.screens

import android.text.style.ParagraphStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import com.example.daggerhilttest.R

val primaryColorPink = Color(0xFFFF64D4)
val primaryColorBlue = Color(0xFF42C6FF)
val primaryColorYellow = Color(0xFFFFE142)

val sfProFont = FontFamily(
    Font(R.font.sf_pro_medium)
)

@Preview
@Composable
fun CurrentWeatherScreenII() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = primaryColorYellow)
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, bottom = 20.dp)

        ) {
            val rightGuideline = createGuidelineFromStart(0.4f)

            val (cityNameField,
                dateField,
                dailySummaryParagraph,
                tempFeild,
                tempValueFeild,
                tempDetailCard,
                hourlyForecatLazyRow,
                hourlyForecatFeild,
                dailySummaryTextField,
                sidebarIcon,
                hourlyForecastGraph) = createRefs()

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "sidebar icon",
                modifier = Modifier
                    .size(34.dp)
                    .constrainAs(sidebarIcon) {
                        start.linkTo(parent.start, margin = 18.dp)
                        bottom.linkTo(cityNameField.bottom)
                        top.linkTo(cityNameField.top)
                    })
            Text(
                text = "Paris",
                fontFamily = sfProFont,
                modifier = Modifier
                    .constrainAs(cityNameField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )

            Text(
                text = "Monday, 19 June",
                fontFamily = sfProFont,

                modifier = Modifier
                    .constrainAs(dateField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(cityNameField.bottom, margin = 12.dp)
                    }
                    .background(color = Color.Black, shape = RoundedCornerShape(34.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),

                color = primaryColorYellow,
                fontWeight = FontWeight.Bold,
            )

            Text("Rain",
                fontFamily = sfProFont,
                modifier = Modifier
                    .constrainAs(tempFeild) {
                        top.linkTo(dateField.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 10.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Text(text = "27°",
                fontFamily = sfProFont,
                fontSize = 190.sp, modifier = Modifier.constrainAs(tempValueFeild) {
                    top.linkTo(dateField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Text(
                text = "Daily Summary",
                fontFamily = sfProFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                modifier = Modifier.constrainAs(dailySummaryTextField) {
                    top.linkTo(tempValueFeild.bottom)
                    start.linkTo(parent.start, margin = 18.dp)
                })
            DailyWeatherSummaryView(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .constrainAs(dailySummaryParagraph) {
                        top.linkTo(dailySummaryTextField.bottom, margin = 8.dp)
                        start.linkTo(tempDetailCard.start)
                        end.linkTo(tempDetailCard.end)
                    })
            Card(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .constrainAs(tempDetailCard) {
                        top.linkTo(dailySummaryParagraph.bottom, margin = 18.dp)
                    },
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(vertical = 22.dp, horizontal = 20.dp)
                ) {
                    WeatherDetailItem(
                        itemIcon = Icons.Default.Air,
                        headingText = "heading",
                        subHeadingText = "Wind"
                    )
                    WeatherDetailItem(
                        itemIcon = Icons.Default.WaterDrop,
                        headingText = "heading",
                        subHeadingText = "Humidity"
                    )
                    WeatherDetailItem(
                        itemIcon = Icons.Default.RemoveRedEye,
                        headingText = "heading",
                        subHeadingText = "Visibility"
                    )
                }
            }
            HourlyForecastView(
                modifier = Modifier
                    .constrainAs(hourlyForecatLazyRow) {
                        top.linkTo(tempDetailCard.bottom, margin = 16.dp)
                        start.linkTo(tempDetailCard.start)
                        end.linkTo(tempDetailCard.end)
                    })
            HourlyGraph(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .border(2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    .constrainAs(hourlyForecastGraph) {
                        top.linkTo(hourlyForecatLazyRow.bottom, margin = 18.dp)
                    })

        }
    }


}

@Composable
fun WeatherDetailItem(
    itemIcon: ImageVector,
    headingText: String,
    subHeadingText: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Icon(
            imageVector = itemIcon,
            tint = primaryColorYellow,
            contentDescription = "item icon",
            modifier = Modifier
                .scale(1.5f)
                .padding(top = 5.dp, bottom = 10.dp)
        )
        Text(
            text = headingText,
            fontFamily = sfProFont,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.body2,
            color = primaryColorYellow,
            fontWeight = FontWeight.W600
        )
        Text(
            text = subHeadingText,
            fontFamily = sfProFont,
            color = primaryColorYellow,
            fontSize = 16.sp,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W600
        )
    }
}

@Composable
fun HourlyForecastView(modifier: Modifier) {
    ConstraintLayout(modifier) {
        val (weeklyForecastText, arrowIcon, weeklyForecastRow) = createRefs()
        Text(
            text = "Hourly Forecast",
            fontFamily = sfProFont,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.constrainAs(weeklyForecastText) {
                start.linkTo(parent.start, margin = 18.dp)
            })

        Icon(
            imageVector = Icons.Default.TrendingFlat,
            contentDescription = "Right arrow",
            tint = Color.Black,
            modifier = Modifier.constrainAs(arrowIcon) {
                end.linkTo(parent.end, margin = 18.dp)
            }
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 18.dp),
            modifier = Modifier
                .constrainAs(weeklyForecastRow) {
                    top.linkTo(weeklyForecastText.bottom, margin = 12.dp)
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(9) {
                HourlyForecastItem()
            }
        }
    }
}

@Preview
@Composable
fun HourlyForecastItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp, horizontal = 18.dp)
    ) {
        Text(
            "26°", fontSize = 18.sp, fontFamily = sfProFont,
        )
        Icon(imageVector = Icons.Default.WaterDrop, contentDescription = "temp icon")
        Text(
            "3 pm", fontSize = 16.sp, fontFamily = sfProFont,
        )

    }
}

@Composable
fun DailyWeatherSummaryView(modifier: Modifier) {
    val text =
        "Now it feels like 31, but actually it is 33. Today, the temperature is felt in the range from 31 to 27"

    Text(
        modifier = modifier,
        fontFamily = sfProFont,
        text = buildAnnotatedString {
            append(
                AnnotatedString(
                    text = text,
                )
            )
        },
        fontSize = 18.sp,
    )
}