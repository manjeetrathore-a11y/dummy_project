package com.manjeet.ubuyapplication.ui.screens



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.manjeet.ubuyapplication.R


@Composable
fun AppBottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Updated labels and routes to match your "Orders" screen requirement
    val items = listOf("home_screen", "categories", "orders", "account")
    val labels = listOf("Home", "Categories", "Orders", "Account")

    // Ensure you have an icon for orders (like a box or clipboard)
    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_categories,
        R.drawable.ic_cart, // You can use ic_cart or an ic_orders if you have one
        R.drawable.ic_account
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, route ->
            val isSelected = currentRoute == route
            val activeColor = Color(0xFFB87333) // Your Copper/Brown theme color
            val inactiveColor = Color(0xFF5F6368)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },

                label = {
                    Text(
                        text = labels.getOrNull(index) ?: "",
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        },
                        color = if (isSelected) {
                            activeColor
                        } else {
                            inactiveColor
                        }
                    )
                },

                icon = {

                    // Safe icon loading
                    val iconRes = icons.getOrNull(index)

                    if (iconRes != null && iconRes != 0) {

                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = labels.getOrNull(index) ?: "",
                            modifier = Modifier.size(24.dp),
                            tint = if (isSelected) {
                                activeColor
                            } else {
                                inactiveColor
                            }
                        )

                    } else {

                        // Fallback icon if resource is invalid
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Default Icon",
                            modifier = Modifier.size(24.dp),
                            tint = inactiveColor
                        )
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = activeColor,
                    selectedTextColor = activeColor,
                    unselectedIconColor = inactiveColor,
                    unselectedTextColor = inactiveColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun MainAppShell(rootNavController: NavController) {
    val nestedNavController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomNavigation(nestedNavController) }
    ) { innerPadding ->
        NavHost(
            navController = nestedNavController,
            startDestination = "home_screen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home_screen") { HomeScreen() }
            composable("categories") { PlaceholderScreen("Categories") }
            composable("cart") { PlaceholderScreen("Shopping Cart") }
            composable("account") { PlaceholderScreen("My Account") }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun ManualNavPreview() {
    // We create a rememberNavController just for the preview to work
    val navController = rememberNavController()

    MaterialTheme {
        AppBottomNavigation(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavPreview() {
    MaterialTheme {
        // We create a fake NavController so the preview can render correctly
        val navController = rememberNavController()

        AppBottomNavigation(navController = navController)
    }
}