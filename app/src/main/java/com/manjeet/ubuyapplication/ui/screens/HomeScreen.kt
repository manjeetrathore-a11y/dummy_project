//package com.manjeet.ubuyapplication.ui.screens
//
//import android.R.attr.order
//import android.util.Log
//import androidx.activity.compose.BackHandler
//import androidx.compose.animation.animateContentSize
//import androidx.compose.animation.core.LinearOutSlowInEasing
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material.icons.filled.History
//import androidx.compose.material.icons.filled.Inventory
//import androidx.compose.material.icons.filled.Launch
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.google.gson.Gson
//import com.manjeet.ubuyapplication.model.OrderRepository
//import com.manjeet.ubuyapplication.R
////import com.manjeet.ubuyapplication.data.model.Product
//import com.manjeet.ubuyapplication.model.Product
//import com.manjeet.ubuyapplication.utils.SortOption
//import com.manjeet.ubuyapplication.utils.getBrandsForCategory
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.navigation.NavController
//import androidx.navigation.NavDestination
//import androidx.navigation.NavHostController
//import com.manjeet.ubuyapplication.Orderss
//import com.manjeet.ubuyapplication.ui.newscreen.AppScreenState
//import com.manjeet.ubuyapplication.ui.newscreen.DynamicTopAppBar
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    onButtonClick: () -> Unit,
//    navController: NavHostController,
//    onClick: () -> Unit,
//    currentScreenState: AppScreenState,
//    onScreenStateChange: (AppScreenState) -> Unit,
//    selectedOrder: Orderss?,
//    onOrderSelected: (Orderss?) -> Unit
//) {
//    // STATES
//    var searchText by remember { mutableStateOf("") }
//    var isSearchFocused by remember { mutableStateOf(false) }
//    var showResults by remember { mutableStateOf(false) }
//    var selectedBrand by remember { mutableStateOf<String?>(null) }
//    var isTransitioning by remember { mutableStateOf(false) }
//    var previousSearchQuery by remember { mutableStateOf("") }
//    var isSearchActive by remember { mutableStateOf(false) }
//
//
//    val focusManager = LocalFocusManager.current
//    val isSearchModeActive = isSearchFocused || searchText.isNotEmpty() || showResults
//
//
//    // Import these: androidx.compose.material3.*
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = false // This allows it to stay at a specific height
//    )
//
//
//    // The "?" means this can safely be "null" (empty) if something goes wrong
//    val context = LocalContext.current
//
//    val parsedOrderRepository: OrderRepository? = remember {
//        try {
//            val jsonString = context.assets.open("order_response_data.json").bufferedReader()
//                .use { it.readText() }
//            val repository = Gson().fromJson(jsonString, OrderRepository::class.java)
//
//            // ADD THIS LINE: It prints a message to your logs if loading succeeds!
//            android.util.Log.d(
//                "ORDER_DEBUG",
//                "JSON loaded successfully! Orders found: ${repository?.data?.orders?.size}"
//            )
//
//            repository
//        } catch (e: Exception) {
//            // ADD THIS LINE: It prints the EXACT error message to your Logcat window
//            android.util.Log.e("ORDER_DEBUG", "JSON parsing failed! Error: ", e)
//            null
//        }
//    }
//
//
////        skipPartiallyExpanded = true // This is the key for "as per data" height
//
//    val scope = rememberCoroutineScope()
//    var showFilterSheet by remember { mutableStateOf(false) }
//
//    // Trigger transition animation when brand is selected
//    LaunchedEffect(selectedBrand) {
//        if (selectedBrand != null) {
//            isTransitioning = true
//            delay(300) // Animation duration
//            isTransitioning = false
//        }
//    }
//
//    // BACK BUTTON HANDLING
//    BackHandler(enabled = searchText.isNotEmpty() || showResults || selectedBrand != null) {
//        if (selectedBrand != null) {
//            // Go back to results and RESTORE the search bar text
//            selectedBrand = null
//            searchText = previousSearchQuery
//        } else if (showResults) {
//            showResults = false
//            searchText = ""
//        } else {
//            searchText = ""
//        }
//    }
//
//    val recentSearches = remember {
//        mutableStateListOf("projector", "apple iphone", "shampoo")
//    }
//
//    val productList = remember {
//        listOf(
//            // PHONES (Apple & Samsung)
//            Product("Apple iPhone 17 Pro Max Ultra", "£1299", R.drawable.img_4, "Mobiles", "APPLE"),
//            Product("Apple iPhone 16 Pro Plus", "£1099", R.drawable.img_7, "Mobiles", "APPLE"),
//            Product("Apple iPhone 15 Silicone Case", "£45", R.drawable.img_6, "Mobiles", "APPLE"),
//            Product("Samsung Galaxy S24 Ultra 5G", "£1149", R.drawable.img_8, "Mobiles", "SAMSUNG"),
//            Product("Samsung Galaxy Z Fold 5", "£1599", R.drawable.img_9, "Mobiles", "SAMSUNG"),
//            Product("Apple iPhone 17 Pro Max Ultra", "£1299", R.drawable.img_4, "Mobiles", "APPLE"),
//            Product("Apple iPhone 16 Pro Plus", "£1099", R.drawable.img_7, "Mobiles", "APPLE"),
//            Product("Apple iPhone 15 Silicone Case", "£45", R.drawable.img_6, "Mobiles", "APPLE"),
//            Product("Samsung Galaxy S24 Ultra 5G", "£1149", R.drawable.img_8, "Mobiles", "SAMSUNG"),
//            Product("Samsung Galaxy Z Fold 5", "£1599", R.drawable.img_9, "Mobiles", "SAMSUNG"),
//            Product("Samsung Galaxy A54 128GB", "£349", R.drawable.img_11, "Mobiles", "SAMSUNG"),
//            Product("Samsung Galaxy A54 128GB", "£349", R.drawable.img_11, "Mobiles", "SAMSUNG"),
//
//            // SHOES (Nike, Adidas, Puma)
//            Product("Nike Air Jordan 1 Retro High", "£165", R.drawable.img_15, "Shoes", "NIKE"),
//            Product("Nike Air Max 270 Running Shoes", "£130", R.drawable.img_16, "Shoes", "NIKE"),
//            Product("Adidas Originals Superstar", "£85", R.drawable.img_17, "Shoes", "ADIDAS"),
//            Product("Adidas Ultraboost Light", "£170", R.drawable.img_18, "Shoes", "ADIDAS"),
//            Product("Puma RS-X Reinvent Sneakers", "£95", R.drawable.img_19, "Shoes", "PUMA"),
//            Product("Nike Air Jordan 1 Retro High", "£165", R.drawable.img_15, "Shoes", "NIKE"),
//            Product("Nike Air Max 270 Running Shoes", "£130", R.drawable.img_16, "Shoes", "NIKE"),
//            Product("Adidas Originals Superstar", "£85", R.drawable.img_17, "Shoes", "ADIDAS"),
//            Product("Adidas Ultraboost Light", "£170", R.drawable.img_18, "Shoes", "ADIDAS"),
//            Product("Puma RS-X Reinvent Sneakers", "£95", R.drawable.img_19, "Shoes", "PUMA"),
//            Product("Puma Velocity Nitro 2", "£110", R.drawable.img_20, "Shoes", "PUMA"),
//            Product("Puma Velocity Nitro 2", "£110", R.drawable.img_20, "Shoes", "PUMA"),
//
//            // MEN'S FASHION
//            Product(
//                "Men's Leather Biker Jacket",
//                "£89",
//                R.drawable.img_21,
//                "Men's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Men's Slim Fit Denim Jeans",
//                "£45",
//                R.drawable.img_22,
//                "Men's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Men's Puffer Winter Jacket",
//                "£120",
//                R.drawable.img_23,
//                "Men's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Men's Leather Biker Jacket",
//                "£89",
//                R.drawable.img_21,
//                "Men's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Men's Slim Fit Denim Jeans",
//                "£45",
//                R.drawable.img_22,
//                "Men's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Men's Puffer Winter Jacket",
//                "£120",
//                R.drawable.img_23,
//                "Men's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Men's Straight Leg Blue Jeans",
//                "£38",
//                R.drawable.img_24,
//                "Men's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Men's Straight Leg Blue Jeans",
//                "£38",
//                R.drawable.img_24,
//                "Men's Fashion",
//                "FASHION"
//            ),
//
//// GIRL'S FASHION
//            Product(
//                "Girl's Floral Summer Dress",
//                "£32",
//                R.drawable.img_25,
//                "Girl's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Girl's Cotton Party Top",
//                "£18",
//                R.drawable.img_26,
//                "Girl's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Girl's Denim Skirt and Top Set",
//                "£40",
//                R.drawable.img_27,
//                "Girl's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Girl's Floral Summer Dress",
//                "£32",
//                R.drawable.img_25,
//                "Girl's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Girl's Cotton Party Top",
//                "£18",
//                R.drawable.img_26,
//                "Girl's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Girl's Denim Skirt and Top Set",
//                "£40",
//                R.drawable.img_27,
//                "Girl's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Girl's Pink Princess Gown",
//                "£55",
//                R.drawable.img_28,
//                "Girl's Fashion",
//                "FASHION"
//            ),
//            Product(
//                "Girl's Pink Princess Gown",
//                "£55",
//                R.drawable.img_28,
//                "Girl's Fashion",
//                "FASHION"
//            ),
//
//// ELECTRONICS & HOME
//            Product(
//                "Sony WH-1000XM5 Headphones",
//                "£320",
//                R.drawable.img_29,
//                "ELECTRONICS & HOME",
//                "SONY"
//            ),
//            Product(
//                "Wireless Bluetooth Speaker",
//                "£55",
//                R.drawable.img_32,
//                "ELECTRONICS & HOME",
//                "TECH"
//            ),
//            Product(
//                "Logitech Mechanical Keyboard",
//                "£110",
//                R.drawable.img_30,
//                "ELECTRONICS & HOME",
//                "LOGITECH"
//            ),
//            Product(
//                "Sony WH-1000XM5 Headphones",
//                "£320",
//                R.drawable.img_29,
//                "ELECTRONICS & HOME",
//                "SONY"
//            ),
//            Product(
//                "Wireless Bluetooth Speaker",
//                "£55",
//                R.drawable.img_32,
//                "ELECTRONICS & HOME",
//                "TECH"
//            ),
//            Product(
//                "Logitech Mechanical Keyboard",
//                "£110",
//                R.drawable.img_30,
//                "ELECTRONICS & HOME",
//                "LOGITECH"
//            ),
//            Product(
//                "Sony WH-1000XM5 Headphones",
//                "£320",
//                R.drawable.img_29,
//                "ELECTRONICS & HOME",
//                "SONY"
//            ),
//            Product(
//                "Wireless Bluetooth Speaker",
//                "£55",
//                R.drawable.img_32,
//                "ELECTRONICS & HOME",
//                "TECH"
//            ),
//            Product(
//                "Logitech Mechanical Keyboard",
//                "£110",
//                R.drawable.img_30,
//                "ELECTRONICS & HOME",
//                "LOGITECH"
//            ),
//            Product(
//                "Ergonomic Mesh Office Chair",
//                "£210",
//                R.drawable.img_31,
//                "ELECTRONICS & HOME",
//                "FURNITURE"
//            )
//        )
//    }
//
//    val popularSearches = listOf(
//        "iPhone 17", "Samsung S24", "Nike Shoes", "Adidas Sneakers",
//        "Men's Jacket", "Girl's Dress", "Denim Jeans", "Puma RS-X"
//    )
//
//    val categories = listOf(
//        "Mobiles" to R.drawable.img_7,
//        "Shoes" to R.drawable.img_12,
//        "Men's Fashion" to R.drawable.img_13,
//        "Girl's Fashion" to R.drawable.img_14,
//        "Electronics" to R.drawable.headphones
//    )
////
////    val context = LocalContext.current
////    val parsedOrderRepository = remember {
////        // 1. Reads the local JSON string file from your assets folder
////        val jsonString = context.assets.open("order_response.json").bufferedReader().use { it.readText() }
////
////        // 2. FIXES BOTH ERRORS: Explicitly defines the conversion target type <OrderRepository>
////        Gson().fromJson<OrderRepository>(jsonString, OrderRepository::class.java)
////    }
//
//    var currentSort by remember { mutableStateOf(SortOption.RELEVANCE) }
//
//    val displayQuery = searchText.trim().replace(Regex("\\s+"), " ")
//
//
//    val filteredCategories =
//        categories.filter { it.first.contains(displayQuery, ignoreCase = true) }
//    val filteredPopular =
//        popularSearches.filter { it.contains(displayQuery, ignoreCase = true) }.sorted()
//
//
//    val filteredProducts = productList.filter {
//        it.name.contains(displayQuery, ignoreCase = true) ||
//                it.category.contains(displayQuery, ignoreCase = true) ||
//                it.brand.contains(displayQuery, ignoreCase = true)
//    }.let { unsortedList ->
//        when (currentSort) {
//            SortOption.RELEVANCE -> unsortedList
//            SortOption.PRICE_LOW_HIGH -> unsortedList.sortedBy {
//                it.price.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
//            }
//
//            SortOption.PRICE_HIGH_LOW -> unsortedList.sortedByDescending {
//                it.price.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
//            }
//        }
//    }
//
//    // Brand products
//    val brandProducts = if (selectedBrand != null) {
//        filteredProducts.filter { it.brand.equals(selectedBrand, ignoreCase = true) }
//    } else {
//        emptyList()
//    }
//
//    val bottomNavController = rememberNavController()
//    // 1. THIS IS THE KEY: The variable must be at the top level of the function
//
//    // Your existing search/brand state (keep these if you have them)
//    // var isSearchModeActive by remember { mutableStateOf(false) }
//
//    var showOrderList by remember { mutableStateOf(false) }
//    var clickedOrder by remember { mutableStateOf<Orderss?>(null) }
//
//   // 2. Main screen switcher logic jo handle karega list vs detail swap
////    if (showOrderList) {
////        if (clickedOrder != null) {
////            //  Agar clickedOrder null nahi hai, toh automatic detail screen render karo
////            OrderDetailPage(
////                order = clickedOrder!!,
////                onBack = { clickedOrder = null } // Back press par vapas list par aane ke liye
////            )
////        } else {
////            // Agar clickedOrder null hai, toh normal list screen dikhao
////            OrderListScreen(
////                ordersList = parsedOrderRepository?.data?.orders ?: emptyList(),
////                onOrderClick = { selectedOrder ->
////                    clickedOrder = selectedOrder // Card click hote hi detail page open ho jayega!
////                },
////                onBack = { showOrderList = false }
////            )
////        }
////    }
//
//    if (!isSearchModeActive && selectedBrand == null) {
//
//    }
//    var currentScreenState by remember { mutableStateOf(AppScreenState.HOME) }
//    var selectedOrder by remember { mutableStateOf<Orderss?>(null) }
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        contentWindowInsets = WindowInsets.statusBars,
////        bottomBar = { AppBottomNavigation(navController = bottomNavController) },
//        topBar = {
//            CenterAlignedTopAppBar(
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                    containerColor = Color.White,
//                    scrolledContainerColor = Color.White
//                ),
//                title = {
//                    // --- APNA DYNAMIC TOOLBAR ---
//                    DynamicTopAppBar(
//                        screenState = currentScreenState,
//                        orderId = selectedOrder?.order_increment_id,
//                        onBackClick = {
//                            when (currentScreenState) {
//                                AppScreenState.TRACK_INPUT -> {
//                                    currentScreenState = AppScreenState.ORDER_DETAIL // Track page se back -> Order Detail
//                                }
//                                AppScreenState.ORDER_DETAIL -> {
//                                    currentScreenState = AppScreenState.ORDER_LIST
//                                    selectedOrder = null
//                                }
//                                AppScreenState.ORDER_LIST -> {
//                                    currentScreenState = AppScreenState.HOME
//                                    bottomNavController.navigate("home")
//                                }
//                                else -> {}
//                            }
//                        },
////                        onFilterClick = { showFilterSheet = true }
//                    )
//
//                    // BottomSheet handler
//                    if (showFilterSheet && currentScreenState == AppScreenState.HOME) {
//                        ModalBottomSheet(
//                            onDismissRequest = { showFilterSheet = false },
//                            sheetState = sheetState,
//                            containerColor = Color.White,
//                            dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFFE0E0E0)) }
//                        ) {
//                            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f)) {
//                                FilterSheetContent(
//                                    onApply = {
//                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
//                                            if (!sheetState.isVisible) showFilterSheet = false
//                                        }
//                                    },
//                                    onClear = { showFilterSheet = false }
//                                )
//                            }
//                        }
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//
//        // ======================================================================
//        // 3. CENTRAL NAVHOST ARCHITECTURE (Saara render iske andar safe hai)
//        // ======================================================================
//        NavHost(
//            navController = bottomNavController,
//            startDestination = "home",
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding) // Proper dynamic window safe spacing
//        ) {
//            // 🏠 HOME TAB
//            composable("home") {
//                LaunchedEffect(Unit) { currentScreenState = AppScreenState.HOME }
//
//                // Aapka pure search bar content layout yahan safely un-comment karke rkh sakte hain.
//            }
//
//            // 📁 CATEGORIES TAB
//            composable("categories") {
//                LaunchedEffect(Unit) { currentScreenState = AppScreenState.HOME }
//                // Categories Screen Layout
//            }
//
//            // 📦 ORDERS SYSTEM (State Machine Controller)
//            composable("orders") {
//                // Agar user home se pehli baar clicks karke orders par aayega:
//                if (currentScreenState == AppScreenState.HOME) {
//                    currentScreenState = AppScreenState.ORDER_LIST
//                }
//
//                when (currentScreenState) {
//
//                    // CASE A: DETAIL VIEW STATE
//                    AppScreenState.ORDER_DETAIL -> {
//                        OrderDetailPage(
//                            order = selectedOrder!!,
//                            onBack = {
//                                currentScreenState = AppScreenState.ORDER_LIST
//                                selectedOrder = null
//                            },
//                            navController = navController,
//
//                            onTrackingClick = {
//                                // 🚀 FIXED: State badalne ke sath-sath data link pipeline trigger kijiye!
//                                android.util.Log.d("NAV_TRACKING", "Navigating to tracking layout screen!")
//                                currentScreenState = AppScreenState.TRACK_INPUT
//                            }
//                        )
//                    }
//
//                    // CASE B: INPUT TRACK SEQUENCE ENGINE STATE
//                    // CASE B: INPUT TRACK SEQUENCE ENGINE STATE
////                    AppScreenState.TRACK_INPUT -> {
////                        TrackOrderInputScreen(
////                            // 🚀 selectedOrder ka data direct pass karein
////                            orderId = selectedOrder?.order_increment_id ?: "No ID",
////                            onBack = {
////                                currentScreenState = AppScreenState.ORDER_DETAIL // Wapas detail par bhejega
////                            }
////                        )
////                    }
//
//                    // CASE C: MASTER ORDER LIST DISPATCHER STATE
//                    AppScreenState.ORDER_LIST -> {
//                        OrderListScreen(
//                            ordersList = parsedOrderRepository?.data?.orders ?: emptyList<com.manjeet.ubuyapplication.Orderss>(),                            onOrderClick = { order ->
////                                Log.d("TAG","578  homescreen $clickedOrder  $selectedOrder")
//
//                                selectedOrder = order
//                                currentScreenState = AppScreenState.ORDER_DETAIL // Card tap processing line
//
//                            },
//                            onBack = {
////                                Log.d("TAG","582  homescreen $clickedOrder  $selectedOrder")
//
//                                currentScreenState = AppScreenState.HOME
//                                bottomNavController.navigate("home")
//                            },
//
//                            onTrackingClick = {   Log.d("TAG","588  homescreen $clickedOrder  $selectedOrder")
//                            }
//
//
//                        )
//                    }
//
//                    // DEFAULT STATE: Fallback backup protector
//                    else -> {
//                        currentScreenState = AppScreenState.ORDER_LIST
//                    }
//                }
//            }
//
//            // 👤 ACCOUNT TAB
//            composable("account") {
//                LaunchedEffect(Unit) { currentScreenState = AppScreenState.HOME }
//                // Account Layout Content
//            }
//        }
//    }
//
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color(0xFFDBE5EF))
//        )
//        {
//     //SEARCH BAR SECTION`
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//                OutlinedTextField(
//                    value = searchText,
//                    onValueChange = {
//                        searchText = it
//                        // If the user starts typing again, we clear the brand/results to show suggestions
//                        showResults = false
//                        selectedBrand = null
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .onFocusChanged { focusState ->
//                            isSearchFocused = focusState.isFocused
//                            if (focusState.isFocused) {
//                                // TRAP: When user clicks the bar, activate the search page mode
//                                isSearchActive = true
//
//                                if (showResults) {
//                                    showResults = false
//                                }
//                            }
//                        },
//                    placeholder = { Text("Search products on uBuy...", color = Color.Gray) },
//                    leadingIcon = {
//                        IconButton(onClick = {
//                            // Optional: If you have a back arrow here, use it to exit search
//                            if (isSearchActive) isSearchActive = false
//                        }) {
//                            Icon(
//                                imageVector = if (isSearchActive) Icons.Default.ArrowBack else Icons.Default.Search,
//                                contentDescription = null,
//                                tint = Color.Gray
//                            )
//                        }
//                    },
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                    keyboardActions = KeyboardActions(onSearch = {
//                        val cleanQuery = searchText.trim().replace(Regex("\\s+"), " ")
//                        if (cleanQuery.isNotBlank()) {
//                            searchText = cleanQuery
//                            // Save the query so we can restore it after looking at a brand
//                            previousSearchQuery = cleanQuery
//
//                            if (!recentSearches.contains(cleanQuery)) {
//                                recentSearches.add(0, cleanQuery)
//                            }
//                            showResults = true
//                            isSearchActive = true // Ensure we stay in search mode
//                            focusManager.clearFocus()
//                        }
//                    }),
//                    shape = RoundedCornerShape(20.dp),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color(0xFFF2F3F5),
//                        unfocusedContainerColor = Color(0xFFF2F3F5),
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    ),
//                    singleLine = true
//                )
//            }
//
//    // CONTENT AREA
//
//    if (true
//    // !isSearchModeActive && selectedBrand == null
//    ) {
//
//        if (showOrderList && parsedOrderRepository != null)
//        // --- THE LIST VIEW ---
//        // This screen will ONLY open now if showOrderList is explicitly set to true
//            OrderListScreen(
//                ordersList = parsedOrderRepository.data.orders,
//                onOrderClick = { selectedOrder ->
//
//                    Log.d("TAG","697  homescreen $clickedOrder  $selectedOrder")
//                    clickedOrder = selectedOrder
//                },
//                onBack = { showOrderList = false },
//                onTrackingClick ={
//                    Log.d("TAG","710  homescreen $clickedOrder  $selectedOrder")
//
//                }
//            )
//        else
//        // --- THE DEFAULT / EMPTY STATE ---
//        // Your Order Track card button lives safely inside here
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            )
//            {
//                // 1. FILTER PILLS
////                        Row(
////                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
////                            horizontalArrangement = Arrangement.spacedBy(8.dp)
////                        ) {
////                            ActionButtonPill(
////                                icon = Icons.Default.SwapVert,
////                                text = "Sort by",
////                                onClick = {})
////                            ActionButtonPill(
////                                icon = Icons.Default.FilterList,
////                                text = "Filter",
////                                onClick = {})
////                            ActionButtonPill(
////                                icon = Icons.Default.DateRange,
////                                text = "Date range",
////                                onClick = {})
////                        }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                // 2. EMPTY STATE IMAGE & TEXT
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.box),
//                        contentDescription = null,
//                        modifier = Modifier.size(130.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(24.dp))
//
//                    Text(
//                        text = "No Orders Yet!",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.ExtraBold,
//                        color = Color.Black
//                    )
//
//                    Text(
//                        text = "You haven't placed any orders yet.",
//                        fontSize = 14.sp,
//                        color = Color.Gray,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.weight(1.2f))
//
//                //
//                // 3. TRACK ORDER BUTTON (Fixed onClick)
//
//
//                when {
//                    // CONDITION 1: Sabse badi priority. Agar kisi card par click ho chuka hai, toh sirf Detail Screen dikhao.
//                    clickedOrder != null -> {
//                        OrderDetailPage(
//                            order = clickedOrder!!,
//                            onBack = {
//                                clickedOrder = null
//                            },
//                            navController = navController,
//                            onTrackingClick = {}
//                        )
//                    }
//
//                    // CONDITION 2: Agar detail band hai aur 'Track order' dabaya gaya hai, toh List Screen dikhao.
//                    showOrderList -> {
//                        OrderListScreen(
//                            ordersList = parsedOrderRepository?.data?.orders ?: emptyList(),
//                            onOrderClick = { selectedOrder ->
//                                Log.d("TAG","784  homescreen $clickedOrder  $selectedOrder")
//                                clickedOrder =
//                                    selectedOrder // Card click hote hi detail page active ho jayega
//                            },
//                            onBack = {
//                                showOrderList = false
//                            },
//                            onTrackingClick = {
//                                Log.d("TAG", "MainActivity: List screen tracking clicked")
//
//                            }
//                        )
//                    }
//
//                    // DEFAULT STATE: Jab dono false hain, toh pehle  "Track order" button wala page hi khulega!
//                    else -> {
//                        // Aapka baaki ka UI layout code (jo bhi page par pehle se tha) yahan rahega...
//
//                        Button(
//                            onClick = {
//                                showOrderList = true // Button dabate hi list open hogi!
//                                Log.d("TAG", "CLICKED")
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(44.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(
//                                    0xFF135D39
//                                )
//                            ),
//                            shape = RoundedCornerShape(28.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.Inventory,
//                                contentDescription = null,
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                "Track order",
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 16.sp
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//                }
//            }
//
//        //--------------------------------after search---------------------//
//
//    } else if (selectedBrand != null) {
//        // BRAND PAGE WITH TRANSITION
//        BrandPageContent(
//            brandName = selectedBrand!!,
//            products = brandProducts,
//            isTransitioning = isTransitioning,
//            currentSort = currentSort,
//            onSortClick = {
//                currentSort = when (currentSort) {
//                    SortOption.RELEVANCE -> SortOption.PRICE_LOW_HIGH
//                    SortOption.PRICE_LOW_HIGH -> SortOption.PRICE_HIGH_LOW
//                    SortOption.PRICE_HIGH_LOW -> SortOption.RELEVANCE
//                }
//            }
//        )
//    } else {
//        // SEARCH RESULTS PAGE
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp)
//        ) {
//            // STATE 1: EMPTY SEARCH
//            if (searchText.isEmpty() && !showResults) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 10.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        "Recent searches",
//                        color = Color(0xFF757575),
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        "Clear",
//                        color = Color.Blue,
//                        fontSize = 13.sp,
//                        modifier = Modifier.clickable { recentSearches.clear() })
//                }
//                recentSearches.take(3).forEach { term ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 12.dp)
//                            .clickable {
//                                searchText = term
//                                showResults = true
//                                focusManager.clearFocus()
//                            },
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.History,
//                            contentDescription = null,
//                            tint = Color(0xFF757575),
//                            modifier = Modifier.size(18.dp)
//                        )
//                        Spacer(modifier = Modifier.width(16.dp))
//                        Text(text = term, fontSize = 15.sp, color = Color.Black)
//                    }
//                }
//
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    thickness = 1.dp,
//                    color = Color(0xFFF2F3F5)
//                )
//
//                Text(
//                    "Recently viewed",
//                    color = Color(0xFF757575),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Surface(
//                        modifier = Modifier.size(40.dp),
//                        color = Color(0xFFF2F3F5),
//                        shape = RoundedCornerShape(4.dp)
//                    ) {
//                        Icon(
//                            Icons.Default.Search,
//                            contentDescription = null,
//                            tint = Color.LightGray,
//                            modifier = Modifier.padding(10.dp)
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Text(
//                        "Recently viewed item placeholder...",
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//
//            // STATE 2: TYPING (SUGGESTIONS)
//            else if (searchText.isNotEmpty() && !showResults) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .verticalScroll(rememberScrollState())
//                ) {
//                    // Popular Searches
//                    if (filteredPopular.isNotEmpty()) {
//                        Text(
//                            "Popular searches",
//                            color = Color.Gray,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
//                        )
//                        filteredPopular.take(5).forEach { term ->
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .clickable {
//                                        searchText = term
//                                        showResults = true
//                                        focusManager.clearFocus()
//                                    }
//                                    .padding(vertical = 10.dp)) {
//                                Icon(
//                                    Icons.Default.Search,
//                                    contentDescription = null,
//                                    tint = Color.LightGray,
//                                    modifier = Modifier.size(18.dp)
//                                )
//                                Spacer(modifier = Modifier.width(12.dp))
//                                Text(text = term, fontWeight = FontWeight.SemiBold)
//                            }
//                        }
//                        HorizontalDivider(
//                            modifier = Modifier.padding(vertical = 12.dp),
//                            thickness = 1.dp,
//                            color = Color(0xFFF2F3F5)
//                        )
//                    }
//
//                    // Products Mini List
//                    if (filteredProducts.isNotEmpty()) {
//                        Text(
//                            "Products",
//                            color = Color.Gray,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(vertical = 8.dp)
//                        )
//                        filteredProducts.take(3).forEach { product ->
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 8.dp)
//                                    .clickable {
//                                        showResults = true
//                                        focusManager.clearFocus()
//                                    },
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Image(
//                                    painter = painterResource(id = product.image),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(45.dp)
//                                        .background(
//                                            Color(0xFFF9F9F9),
//                                            RoundedCornerShape(4.dp)
//                                        )
//                                )
//                                Spacer(modifier = Modifier.width(12.dp))
//                                Text(
//                                    text = product.name,
//                                    fontSize = 14.sp,
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis,
//                                    fontWeight = FontWeight.Medium
//                                )
//                            }
//                        }
//                        HorizontalDivider(
//                            modifier = Modifier.padding(vertical = 12.dp),
//                            thickness = 1.dp,
//                            color = Color(0xFFF2F3F5)
//                        )
//                    }
//
//                    // Categories Grid
//                    if (filteredCategories.isNotEmpty()) {
//                        Text(
//                            "Categories",
//                            color = Color(0xFF757575),
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(vertical = 12.dp)
//                        )
//                        filteredCategories.chunked(2).forEach { rowItems ->
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 4.dp),
//                                horizontalArrangement = Arrangement.spacedBy(10.dp)
//                            ) {
//                                rowItems.forEach { (name, _) ->
//                                    Surface(
//                                        modifier = Modifier
//                                            .weight(1f)
//                                            .height(60.dp)
//                                            .clickable {
//                                                searchText = name
//                                                showResults = true
//                                                focusManager.clearFocus()
//                                            },
//                                        shape = RoundedCornerShape(18.dp),
//                                        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
//                                        color = Color.White
//                                    ) {
//                                        Row(
//                                            modifier = Modifier.padding(horizontal = 12.dp),
//                                            verticalAlignment = Alignment.CenterVertically
//                                        ) {
//                                            Icon(
//                                                imageVector = Icons.Default.Launch,
//                                                contentDescription = null,
//                                                tint = Color(0xFFB87333),
//                                                modifier = Modifier.size(20.dp)
//                                            )
//                                            Spacer(modifier = Modifier.width(10.dp))
//                                            Text(
//                                                text = name,
//                                                fontSize = 14.sp,
//                                                fontWeight = FontWeight.Black,
//                                                color = Color.Black,
//                                                maxLines = 1
//                                            )
//                                        }
//                                    }
//                                }
//                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
//                            }
//                        }
//                        HorizontalDivider(
//                            modifier = Modifier.padding(vertical = 16.dp),
//                            thickness = 1.dp,
//                            color = Color(0xFFF2F3F5)
//                        )
//                    }
//
//                    Text(
//                        "Recent searches",
//                        color = Color.Gray,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    recentSearches.take(2).forEach { term ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 12.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                Icons.Default.History,
//                                contentDescription = null,
//                                tint = Color.LightGray,
//                                modifier = Modifier.size(18.dp)
//                            )
//                            Spacer(modifier = Modifier.width(16.dp))
//                            Text(text = term, color = Color.Black)
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(100.dp))
//                }
//            }
//
//            // STATE 3: SHOWING RESULTS
//            else if (showResults) {
//                val currentCategory = filteredProducts.firstOrNull()?.category ?: searchText
//                val relevantBrands = getBrandsForCategory(currentCategory)
//
//                ResultsHeader(
//                    totalCount = filteredProducts.size,
//                    query = displayQuery,
//                    currentSort = currentSort,
//                    onSortClick = {
//                        currentSort = when (currentSort) {
//                            SortOption.RELEVANCE -> SortOption.PRICE_LOW_HIGH
//                            SortOption.PRICE_LOW_HIGH -> SortOption.PRICE_HIGH_LOW
//                            SortOption.PRICE_HIGH_LOW -> SortOption.RELEVANCE
//                        }
//                    },
//                    onFilterClick = {
//                        showFilterSheet = true // This activates the sheet
//                    }
//                )
//
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize(),
//                    contentPadding = PaddingValues(bottom = 16.dp)
//                ) {
//                    itemsIndexed(filteredProducts) { index, product ->
//                        SearchResultItem(product)
//
//                        // Brand Row after 4th item
//                        if (index == 3 && relevantBrands.isNotEmpty()) {
//                            Column(
//                                modifier = Modifier.fillMaxWidth()
//                                    .padding(vertical = 16.dp)
//                            ) {
//                                Text(
//                                    text = "Top Brands",
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 18.sp,
//                                    modifier = Modifier.padding(16.dp, 12.dp)
//                                )
//                                LazyRow(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    contentPadding = PaddingValues(horizontal = 16.dp),
//                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                                ) {
//                                    items(relevantBrands) { (name, logo) ->
//                                        BrandLogoCircle(
//                                            brandName = name,
//                                            brandLogo = logo,
//                                            onClick = {
//                                                previousSearchQuery =
//                                                    searchText // Save "shoes"
//                                                searchText =
//                                                    name               // Update bar to "NIKE"
//                                                selectedBrand = name
//                                            }
//                                        )
//                                    }
//                                }
//                            }
//                            HorizontalDivider(
//                                modifier = Modifier.padding(bottom = 16.dp),
//                                thickness = 1.dp,
//                                color = Color(0xFFF2F3F5)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AppNavBar(
//    navController: NavHostController,
//    startDestination: String
//){
//
//
//
//}
//
////                            item {
////                                if (filteredProducts.isNotEmpty()) {
////                                    Box(
////                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
////                                        contentAlignment = Alignment.Center
////                                    ) {
////                                        CircularProgressIndicator(
////                                            modifier = Modifier.size(24.dp),
////                                            strokeWidth = 2.dp
////                                        )
////                                    }
////                                }
////                            }
////                        }
////                    }
////                }
////            }
////        }
////    }
//
//
