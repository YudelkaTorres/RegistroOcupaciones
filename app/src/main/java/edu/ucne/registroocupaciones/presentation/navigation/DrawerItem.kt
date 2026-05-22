package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    Surface(
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer
        else
            Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(title) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(text = title)
        }
    }
}