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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manjeet.ubuyapplication.ui.screens.AppScreenState
import com.manjeet.ubuyapplication.ui.screens.HomeScreen
import com.manjeet.ubuyapplication.ui.screens.LanguageCountrySelectionScreen
import com.manjeet.ubuyapplication.ui.screens.LoginSignupScreen
import com.manjeet.ubuyapplication.ui.screens.OrderDetailPage
import com.manjeet.ubuyapplication.ui.screens.OrderListScreen
import com.manjeet.ubuyapplication.ui.screens.SplashAnimationScreen
import com.manjeet.ubuyapplication.ui.screens.TrackOrderInputScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
                    window.statusBarColor = android.graphics.Color.TRANSPARENT
                }
            }
            MainNavigation()
        }
    }
}

@Composable
fun MainNavigation() {

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(4000)
        showSplash = false
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (showSplash) {
            SplashAnimationScreen()
        } else {
            val navController = rememberNavController()

            // ── SCREEN STATE: owned here, passed down to HomeScreen ──────────
            // This is the single source of truth for which sub-screen is active.
            var currentScreenState by remember { mutableStateOf(AppScreenState.HOME) }
            var selectedOrder by remember { mutableStateOf<Order?>(null) }

            NavHost(
                navController = navController,
                startDestination = "language_country"
            ) {

                composable("language_country") {
                    LanguageCountrySelectionScreen(navController)
                }

                // ── HOME SCREEN (no NavHost inside, state passed as params) ──
//                composable("home_screen") {
//                    HomeScreen(
//                        currentScreenState = currentScreenState,
//                        onScreenStateChange = { currentScreenState = it },
//                        selectedOrder = selectedOrder,
//                        onOrderSelected = { selectedOrder = it }
//                    )
//                }

                composable("login_signup") {
                    LoginSignupScreen(navController)
                }

                // ── ORDERS ROUTE (standalone, not used inside HomeScreen) ─────
                composable("orders_route") {
                    var clickedOrder by remember { mutableStateOf<Order?>(null) }
                    var showTrackingInput by remember { mutableStateOf(false) }
                    val dummyOrdersList = remember { emptyList<Order>() }

                    when {
                        showTrackingInput && clickedOrder != null -> {
                            TrackOrderInputScreen(
                                navController = navController,
                                orderId = clickedOrder!!.order_increment_id,
                                onBack = { showTrackingInput = false }
                            )
                        }

//                        clickedOrder != null -> {
//                            val order = clickedOrder!!  // captured once — safe to reuse
//                            OrderDetailPage(
//                                order = order,
//                                onBack = { clickedOrder = null },
//                                onTrackingClick = {
//                                    Log.d("TAG", "Navigating to TrackOrderInputScreen for: ${order.order_increment_id}")
//                                    showTrackingInput = true
//                                }
//                            )
//                        }

                        else -> {
                            OrderListScreen(
                                ordersList = dummyOrdersList,
                                onOrderClick = { selected -> clickedOrder = selected },
                                onBack = { navController.popBackStack() },
                                onTrackingClick = {
                                    Log.d("TAG", "MainActivity: List screen tracking clicked")
                                }
                            )
                        }
                    }
                }

                composable("account") {
                    // Account screen goes here
                }

                composable("trackOrder") {
                    TrackOrderInputScreen(
                        navController = navController,
                        orderId = "",
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}