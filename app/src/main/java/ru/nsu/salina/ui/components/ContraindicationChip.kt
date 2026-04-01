package ru.nsu.salina.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nsu.salina.domain.model.Contraindication
import ru.nsu.salina.ui.theme.ChipShape
import ru.nsu.salina.ui.theme.PrimaryBlue
import ru.nsu.salina.ui.theme.SurfaceBlue
import ru.nsu.salina.ui.theme.BackgroundWhite

@Composable
fun ContraindicationChip(
    contraindication: Contraindication,
    isSelected: Boolean,
    onToggle: (Contraindication) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) PrimaryBlue else BackgroundWhite
    val textColor = if (isSelected) BackgroundWhite else MaterialTheme.colorScheme.onBackground
    val borderColor = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.outline

    Text(
        text = contraindication.name,
        style = MaterialTheme.typography.bodyMedium,
        color = textColor,
        modifier = modifier
            .background(backgroundColor, ChipShape)
            .border(1.dp, borderColor, ChipShape)
            .clickable { onToggle(contraindication) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
