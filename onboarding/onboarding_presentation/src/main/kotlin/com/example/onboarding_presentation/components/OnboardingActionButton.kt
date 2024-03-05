package com.example.onboarding_presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_ui.LocalSpacing
import com.example.ui.CalorieTheme

@Composable
fun OnboardingActionButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(100.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(LocalSpacing.current.spaceSmall),
            text = text,
            style = textStyle,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnboardingActionButtonPreview() {
    CalorieTheme {
        OnboardingActionButton(
            text = "Test",
            onClick = {}
        )
    }
}