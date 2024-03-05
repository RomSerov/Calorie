package com.example.onboarding_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.core_ui.LocalSpacing
import com.example.ui.CalorieTheme

@Composable
fun UnitTextField(
    modifier: Modifier = Modifier,
    value: String,
    unit: String,
    textStyle: TextStyle = TextStyle(
        color = MaterialTheme.colorScheme.inversePrimary,
        fontSize = 70.sp,
        textAlign = TextAlign.Center
    ),
    onValueChange: (String) -> Unit
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .width(IntrinsicSize.Min)
                .alignBy(LastBaseline),
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )
        //Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier
                .weight(1f)
                .alignBy(LastBaseline),
            text = unit
        )
    }
}

@Preview
@Composable
private fun UnitTextFieldPreview() {
    CalorieTheme {
        UnitTextField(
            value = "34",
            unit = "cm",
            onValueChange = {}
        )
    }
}