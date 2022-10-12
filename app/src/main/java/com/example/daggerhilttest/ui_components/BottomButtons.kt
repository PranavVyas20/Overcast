package com.example.daggerhilttest.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun BottomButtonLayout() {
    val todayBtnSelectedState = remember {
        mutableStateOf(true)
    }
    val thisWeekBtnSelectedState = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .padding(
                start = 10.dp,
                top = 20.dp,
                bottom = 20.dp,
                end = 20.dp
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(30.dp)
            )
            .fillMaxWidth(),
    ) {
        BottomButton(
            btnText = "Today",
            isSelected = todayBtnSelectedState.value,
            modifier = Modifier
                .weight(1f),
            onBtnClick = {
                todayBtnSelectedState.value = true
                thisWeekBtnSelectedState.value = false
            }
        )
        BottomButton(
            btnText = "This Week",
            isSelected = thisWeekBtnSelectedState.value,
            modifier = Modifier
                .weight(1f),
            onBtnClick = {
                todayBtnSelectedState.value = false
                thisWeekBtnSelectedState.value = true
            }
        )
    }
}

@Composable
fun BottomButton(
    isSelected: Boolean,
    btnText: String,
    modifier: Modifier,
    onBtnClick:() -> Unit
)
{
    Button(
        onClick = { onBtnClick() },
        modifier = modifier,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (isSelected) Color.Black else Color.White)

    ) {
        Text(
            text = btnText,
            modifier.padding(
                top = 10.dp,
                bottom = 10.dp
            ),
            color = if (isSelected) Color.White else Color.Black,
            textAlign = TextAlign.Center
        )
    }
}