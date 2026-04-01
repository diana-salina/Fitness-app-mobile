package ru.nsu.salina.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import ru.nsu.salina.R
import ru.nsu.salina.ui.navigation.NavRoutes
import ru.nsu.salina.ui.theme.PrimaryBlue
import ru.nsu.salina.ui.theme.BackgroundWhite
import ru.nsu.salina.ui.theme.DisabledGray

data class BottomBarItem(
    val route: String,
    val label: String,
    val iconResId: Int
)

val bottomBarItems = listOf(
    BottomBarItem(NavRoutes.WORKOUTS, "Тренировки", R.drawable.ic_trains),
    BottomBarItem(NavRoutes.CALENDAR, "Календарь", R.drawable.ic_calendar),
    BottomBarItem(NavRoutes.PROFILE, "Профиль", R.drawable.ic_profile)
)

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(containerColor = BackgroundWhite) {
        bottomBarItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    unselectedIconColor = DisabledGray,
                    unselectedTextColor = DisabledGray,
                    indicatorColor = BackgroundWhite
                )
            )
        }
    }
}
