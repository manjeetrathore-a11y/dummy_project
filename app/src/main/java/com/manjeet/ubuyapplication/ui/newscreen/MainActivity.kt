package com.manjeet.ubuyapplication.ui.newscreen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.manjeet.ubuyapplication.model.Order
import com.manjeet.ubuyapplication.model.OrderRepository
import com.manjeet.ubuyapplication.model.Product
import com.manjeet.ubuyapplication.ui.newscreen.UI.OrderDetailPage
import com.manjeet.ubuyapplication.ui.newscreen.UI.NewOrderListScreen
import com.manjeet.ubuyapplication.ui.newscreen.UI.AccountTabScreen
import com.manjeet.ubuyapplication.ui.newscreen.UI.UbuyMembershipScreen
import com.manjeet.ubuyapplication.ui.newscreen.AppScreenState
import com.manjeet.ubuyapplication.ui.newscreen.DynamicTopAppBar
import com.manjeet.ubuyapplication.ui.newscreen.TrackOrderInputScreen
import com.manjeet.ubuyapplication.ui.screens.NewHomeScreen
//import com.manjeet.ubuyapplication.ui.screens.BrandPageContent
import com.manjeet.ubuyapplication.ui.screens.ProductDetailScreen // Added detail screen import
import com.manjeet.ubuyapplication.viewmodel.MembershipViewModel
import com.manjeet.ubuyapplication.utils.SortOption

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.parseColor("#EFF1F5")
        val decorView = window.decorView
        WindowCompat.getInsetsController(window, decorView).apply {
            isAppearanceLightStatusBars = true
        }

        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current

    val parsedOrderRepository: OrderRepository? = remember {
        try {
            val jsonString = context.assets.open("order_response_data.json").bufferedReader()
                .use { it.readText() }
            Gson().fromJson(jsonString, OrderRepository::class.java)
        } catch (e: Exception) {
            null
        }
    }

    val jsonOrdersList = parsedOrderRepository?.data?.orders ?: emptyList()
    var currentScreenState by remember { mutableStateOf(AppScreenState.HOME) }
    var selectedOrder by remember { mutableStateOf<Order?>(null) }

    var selectedProductForDetail by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(currentRoute) {
        currentScreenState = when {
            currentRoute == BottomNavItem.Home.route -> AppScreenState.HOME
            currentRoute == BottomNavItem.Account.route -> AppScreenState.HOME
            currentRoute == "orders" -> AppScreenState.ORDER_LIST
            currentRoute?.startsWith("orderdetail") == true -> AppScreenState.ORDER_DETAIL
            currentRoute?.startsWith("trackOrder") == true -> AppScreenState.HOME
            currentRoute?.startsWith("brand_page") == true -> AppScreenState.HOME
            currentRoute?.startsWith("product_detail") == true -> AppScreenState.ORDER_DETAIL
            else -> AppScreenState.HOME
        }
    }

    val currentOrderId = navBackStackEntry?.arguments?.getString("orderId")

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Category,
        BottomNavItem.Cart,
        BottomNavItem.Account
    )

    Scaffold(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        topBar = {
            if (currentScreenState != AppScreenState.ORDER_DETAIL &&
                currentRoute != BottomNavItem.Account.route &&
                currentRoute != "product_detail" && // Dynamic TopBar hide condition for product detail
                currentRoute != BottomNavItem.Home.route) {

                DynamicTopAppBar(
                    screenState = currentScreenState,
                    currentRoute = currentRoute,
                    orderId = currentOrderId ?: selectedOrder?.order_increment_id,
                    onBackClick = { navController.popBackStack() }
                )
            }
        },
        bottomBar = {
            // Keep tabs active only on core home/category landing views
            if (currentRoute == BottomNavItem.Home.route ||
                currentRoute == BottomNavItem.Category.route ||
                currentRoute == BottomNavItem.Cart.route ||
                currentRoute == BottomNavItem.Account.route) {

                NavigationBar(containerColor = Color.White) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title, fontSize = 11.sp) },
                            selected = currentRoute == item.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF145334),
                                selectedTextColor = Color(0xFF145334),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color(0xFFEFF6F0)
                            ),
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    if (currentScreenState == AppScreenState.ORDER_DETAIL || currentRoute == "product_detail")
                        androidx.compose.foundation.layout.PaddingValues(0.dp)
                    else innerPadding
                )
        ) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                // TAB 1: INTEGRATED SMART HOME SCREEN ENTRY
                composable(BottomNavItem.Home.route) {
                    NewHomeScreen(
                        showTopBar = false,
                        onTrackOrderClick = { navController.navigate("trackOrder") },
                        onProductClick = { clickedProduct: Product ->
                            selectedProductForDetail = clickedProduct
                            navController.navigate("product_detail")
                        }
                    )
                }

                // TAB 2: CATEGORY PLACEHOLDER
                composable(BottomNavItem.Category.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("Category Screen Coming Soon", color = Color.Gray)
                    }
                }

                // TAB 3: CART PLACEHOLDER
                composable(BottomNavItem.Cart.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("Shopping Cart Empty", color = Color.Gray)
                    }
                }

                // TAB 4: ACCOUNT PROFILE HUB
                composable(BottomNavItem.Account.route) {
                    val membershipViewModel: MembershipViewModel = viewModel()
                    val membershipData by membershipViewModel.membershipData.collectAsState()

                    AccountTabScreen(
                        ubuyMembershipData = membershipData,
                        onTrackOrderClick = { navController.navigate("trackOrder") },
                        onViewOrdersClick = { navController.navigate("orders") },
                        onMembershipClick = { navController.navigate("membership") }
                    )
                }

                // 🆕 SUB-ROUTE: DYNAMIC BRAND LISTING VIEW CONTAINER
                composable("brand_page") {
                    var sortOption by remember { mutableStateOf(SortOption.RELEVANCE) }

                    val mockBrandProducts = listOf(
                        Product("CASEKOO Armor Shockproof Designed for iPhone 17 Pro Case [16FT Military Grade Protection]", "£29.99", com.manjeet.ubuyapplication.R.drawable.img_4, "Electronics", "Roborock"),
                        Product("Roborock Qrevo Series Robot Vacuum and Mop, 8000Pa Suction, Upgraded Auto Mop", "£29.00", com.manjeet.ubuyapplication.R.drawable.img_4, "Electronics", "Roborock")
                    )

                    BrandPageContent(
                        brandName = "Roborock",
                        products = mockBrandProducts,
                        isTransitioning = false,
                        currentSort = sortOption,
                        onSortClick = {
                            sortOption = if (sortOption == SortOption.RELEVANCE) SortOption.PRICE_LOW_HIGH else SortOption.RELEVANCE
                        },
                        onProductClick = { clickedProduct ->
                            selectedProductForDetail = clickedProduct
                            navController.navigate("product_detail")
                        }
                    )
                }

                composable("product_detail") {
                    val fallbackProduct = Product(
                        name = "Roborock Qrevo Series Robot Vacuum and Mop, 8000Pa Suction, Upgraded Auto Mop Washing Heavy Variant Base Cover",
                        price = "£29.00",
                        image = com.manjeet.ubuyapplication.R.drawable.img_4,
                        category = "Electronics",
                        brand = "Roborock"
                    )

                    ProductDetailScreen(
                        product = selectedProductForDetail ?: fallbackProduct,
                        onBackClick = { navController.popBackStack() },
                        onAddToCartClick = {
                        }
                    )
                }

                composable(
                    route = "trackOrder?orderId={orderId}",
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType; nullable = true; defaultValue = null })
                ) { backStackEntry ->
                    val argumentOrderId = backStackEntry.arguments?.getString("orderId") ?: ""
                    TrackOrderInputScreen(navController = navController, orderId = argumentOrderId, onBack = { navController.popBackStack() })
                }

                // SUB-ROUTE: ORDERS HISTORIES LIST SCREEN
                composable("orders") {
                    NewOrderListScreen(
                        ordersList = jsonOrdersList,
                        onOrderClick = { clickedOrder ->
                            selectedOrder = clickedOrder
                            navController.navigate("orderdetail/${clickedOrder.order_increment_id}")
                        }
                    )
                }

                // SUB-ROUTE: DECOUPLED ORDER DETAIL SCREEN
                composable("orderdetail/{orderId}") { backStackEntry ->
                    val orderToShow: Order = selectedOrder ?: remember {
                        Order(
                            address_type = "",
                            consolidation_shipment_message = "",
                            full_address = "",
                            items = emptyList(),
                            order_date = "22 May 2026",
                            order_id = 0,
                            order_increment_id = backStackEntry.arguments?.getString("orderId") ?: "",
                            order_status = "Packing",
                            order_status_en = "Packing",
                            order_total = "₹0.00",
                            payment_method = "",
                            ship_to = "",
                            shipment_data = emptyList(),
                            shipping_method = ""
                        )
                    }
                    OrderDetailPage(navController = navController, order = orderToShow, onBack = { navController.popBackStack() })
                }

                // SUB-ROUTE: UBUY PLUS+ MEMBERSHIP
                composable("membership") {
                    UbuyMembershipScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("nav_home", "Home", Icons.Default.Home)
    object Category : BottomNavItem("nav_category", "Category", Icons.Default.List)
    object Cart : BottomNavItem("nav_cart", "Cart", Icons.Default.ShoppingCart)
    object Account : BottomNavItem("nav_account", "Account", Icons.Default.Person)
}