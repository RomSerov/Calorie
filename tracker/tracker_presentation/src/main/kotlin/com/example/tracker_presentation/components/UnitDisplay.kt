package com.example.tracker_presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.core_ui.LocalSpacing
import com.example.ui.CalorieTheme

@Composable
fun UnitDisplay(
    modifier: Modifier = Modifier,
    amount: Int,
    unit: String,
    amountTextSize: TextUnit = 20.sp,
    amountColor: Color = MaterialTheme.colorScheme.onBackground,
    unitTextSize: TextUnit = 14.sp,
    unitColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.alignBy(LastBaseline),
            text = amount.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = amountTextSize,
            color = amountColor
        )
        Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
        Text(
            modifier = Modifier.alignBy(LastBaseline),
            text = unit,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = unitTextSize,
            color = unitColor
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun UnitDisplayPreview() {
    CalorieTheme {
        UnitDisplay(
            amount = 30,
            unit = "g"
        )
    }
}