package com.example.onboarding_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_ui.LocalSpacing
import com.example.ui.CalorieTheme

@Composable
fun OnboardingSelectButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    color: Color,
    selectedTextColor: Color,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(100.dp)
            )
            .background(
                color = if (isSelected) color else Color.Transparent
            )
            .clickable {
                onClick()
            }
            .padding(LocalSpacing.current.spaceMedium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (isSelected) selectedTextColor else color
        )
    }
}

@Preview
@Composable
private fun OnboardingSelectButton() {
    CalorieTheme {
        OnboardingSelectButton(
            text = "Button",
            isSelected = true,
            color = Color.White,
            selectedTextColor = Color.Black,
            onClick = {}
        )
    }
}