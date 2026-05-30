package com.manjeet.ubuyapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manjeet.ubuyapplication.ui.screens.HomeScreen
import com.manjeet.ubuyapplication.ui.screens.LanguageCountrySelectionScreen
import com.manjeet.ubuyapplication.ui.screens.LoginSignupScreen
import com.manjeet.ubuyapplication.ui.screens.OrderDetailPage
import com.manjeet.ubuyapplication.ui.screens.OrderListScreen
import com.manjeet.ubuyapplication.ui.screens.SplashAnimationScreen
import com.manjeet.ubuyapplication.ui.screens.WelcomeSlidesScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // --- ADD THIS SECTION TO FIX STATUS BAR VISIBILITY ---
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    // This makes the status bar icons DARK
                    // Use 'true' for a white/light top bar so you can see the clock
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true

                    // Optional: Set status bar color to transparent to match your UI
                    window.statusBarColor = android.graphics.Color.TRANSPARENT
                }
            }
            // -------------------------------------------------------

            MainNavigation()
        }
    }
}

@Composable
fun MainNavigation() {

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(4000) // Splash screen duration
        showSplash = false
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (showSplash) {
            SplashAnimationScreen()
        } else {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "language_country"
            ) {

                // Language + Country Screen
                composable("language_country") {
                    LanguageCountrySelectionScreen(navController)
                }

                // Welcome Slides Screen
                composable("home_screen") {
                    HomeScreen()
                }




                // Login + Signup Screen
                composable("login_signup") {
                    LoginSignupScreen(navController)
                }


                // --- DYNAMIC ORDERS PIPELINE NAVIGATION ---
                composable("orders_route") {
                    // Yahan hum state maintain kar rahe hain select hone wale order ki
                    var clickedOrder by remember { mutableStateOf<Order?>(null) }

                    // Mock list pipeline: Yahan aap apna ViewModel data variable pass karein
                    val dummyOrdersList = remember { emptyList<Order>() }

                    if (clickedOrder != null) {
                        // Agar user kisi order par click karta hai, toh use Details dikhao
                        OrderDetailPage(
                            order = clickedOrder!!,
                            onBack = { clickedOrder = null },
                            onTrackingClick = {}
                        )
                    } else {
                        // Varna use default scrollable list screen dikhao
                        OrderListScreen(
                            ordersList = dummyOrdersList,
                            onOrderClick = { selected ->
                                clickedOrder = selected
                            },
                            onBack = {
                                navController.popBackStack()
                            },
                            onTrackingClick = {     Log.d("TAG","123  MainActivity ")
                            }
                        )
                    }
                }

                // 5. Account Screen (If you want to navigate to it directly)
                composable("account") {
                    // You can call your AccountScreen here if it's not inside MainApp
                }


            }
        }
    }
}

