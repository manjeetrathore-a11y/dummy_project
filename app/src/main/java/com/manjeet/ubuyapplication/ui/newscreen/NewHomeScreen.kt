package com.manjeet.ubuyapplication.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.R
// ── Imports ko double check karein ──
import com.manjeet.ubuyapplication.model.Products
//import com.manjeet.ubuyapplication.viewmodel.ProductViewModel // 👈 Agar error aaye toh is path ko project structure se check karein
import com.manjeet.ubuyapplication.ui.newscreen.AppScreenState
import com.manjeet.ubuyapplication.ui.newscreen.DynamicTopAppBar
import com.manjeet.ubuyapplication.utils.SortOption
import com.manjeet.ubuyapplication.utils.getBrandsForCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.manjeet.ubuyapplication.ui.newscreen.BrandLogoCircle
import com.manjeet.ubuyapplication.ui.newscreen.BrandPageContent
import com.manjeet.ubuyapplication.ui.newscreen.FilterSheetContent
import com.manjeet.ubuyapplication.ui.newscreen.ResultsHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import com.manjeet.ubuyapplication.ViewModel.ProductViewModel
import com.manjeet.ubuyapplication.ui.newscreen.SearchResultItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHomeScreen(
    viewModel: ProductViewModel, // 👈 Explicit reference pass kiya
    showTopBar: Boolean = true,
    onTrackOrderClick: () -> Unit = {},
    onProductClick: (Products) -> Unit = {}
){
    // ── Search State ──────────────────────────────────────────────────────────
    var searchText          by remember { mutableStateOf("") }
    var isSearchFocused     by remember { mutableStateOf(false) }
    var showResults         by remember { mutableStateOf(false) }
    var selectedBrand       by remember { mutableStateOf<String?>(null) }
    var previousSearchQuery by remember { mutableStateOf("") }
    var isTransitioning     by remember { mutableStateOf(false) }

    val focusManager     = LocalFocusManager.current
    val isSearchModeActive = isSearchFocused || searchText.isNotEmpty() || showResults

    val recentSearches = remember { mutableStateListOf("projector", "apple iphone", "shampoo") }
    var currentSort by remember { mutableStateOf(SortOption.RELEVANCE) }

    // ── Filter Sheet State ────────────────────────────────────────────────────
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope       = rememberCoroutineScope()

    LaunchedEffect(selectedBrand) {
        if (selectedBrand != null) {
            isTransitioning = true
            delay(300)
            isTransitioning = false
        }
    }

    BackHandler(enabled = searchText.isNotEmpty() || showResults || selectedBrand != null) {
        when {
            selectedBrand != null -> {
                selectedBrand = null
                searchText    = previousSearchQuery
            }
            showResults -> {
                showResults = false
                searchText  = ""
            }
            else -> searchText = ""
        }
    }

    // Static collections
    val popularSearches = listOf(
        "iPhone 17", "Samsung S24", "Nike Shoes", "Adidas Sneakers",
        "Men's Jacket", "Girl's Dress", "Denim Jeans", "Puma RS-X"
    )

    val categories = listOf(
        "Mobiles"        to R.drawable.img_7,
        "Shoes"          to R.drawable.img_12,
        "Men's Fashion"  to R.drawable.img_13,
        "Girl's Fashion" to R.drawable.img_14,
        "Electronics"    to R.drawable.headphones
    )

    // ── 3. Type Inference Error Fix (Explicit Type Defined) ───────────────────
    val productList: List<Products> = viewModel.productsList

    val displayQuery = searchText.trim().replace(Regex("\\s+"), " ")

    val filteredCategories = categories.filter { it.first.contains(displayQuery, ignoreCase = true) }
    val filteredPopular    = popularSearches.filter { it.contains(displayQuery, ignoreCase = true) }.sorted()

    // 👈 'it' aur 'let' inference blocks ko types ke sath declare kiya taaki compiler confuse na ho
    val filteredProducts: List<Products> = productList
        .filter { product: Products ->
            (product.name?.contains(displayQuery, ignoreCase = true) == true) ||
                    (product.store?.contains(displayQuery, ignoreCase = true) == true) ||
                    (product.brand?.contains(displayQuery, ignoreCase = true) == true)
        }
        .let { list: List<Products> ->
            when (currentSort) {
                SortOption.RELEVANCE     -> list
                SortOption.PRICE_LOW_HIGH  -> list.sortedBy { item: Products -> item.price ?: 0 }
                SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { item: Products -> item.price ?: 0 }
            }
        }

    val brandProducts: List<Products> = if (selectedBrand != null)
        filteredProducts.filter { product: Products -> product.brand?.equals(selectedBrand, ignoreCase = true) == true }
    else emptyList()

    // ─────────────────────────────────────────────────────────────────────────
    // ROOT LAYOUT
    // ─────────────────────────────────────────────────────────────────────────
    Column(modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(
            visible = !isSearchModeActive,
            enter   = fadeIn(),
            exit    = fadeOut()
        ) {
            DynamicTopAppBar(
                screenState = AppScreenState.HOME,
                onBackClick = {}
            )
        }

        // Sticky Search Bar
        Surface(
            modifier      = Modifier.fillMaxWidth(),
            color         = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value         = viewModel.searchQuery, // 👈 FIXED: Direct ViewModel ki state se read karein
                    onValueChange = { it ->
                        // 🌟 FIXED: ViewModel ko live batayein taaki 'onSearchQueryChanged' trigger ho aur unused na rahe
                        viewModel.onSearchQueryChanged(it)

                        showResults   = false
                        selectedBrand = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isSearchFocused = focusState.isFocused
                            if (focusState.isFocused && showResults) showResults = false
                        },
                    placeholder   = { Text("Search products on uBuy...", color = Color.Gray) },
                    leadingIcon   = {
                        IconButton(onClick = {
                            if (isSearchModeActive) {
                                focusManager.clearFocus()
                                viewModel.clearSearch() // 👈 FIXED: Search reset method call kiya
                                showResults   = false
                                selectedBrand = null
                            }
                        }) {
                            Icon(
                                imageVector  = if (isSearchModeActive) Icons.Default.ArrowBack else Icons.Default.Search,
                                contentDescription = null,
                                tint         = Color.Gray
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        // Keyboard ka search button dabane par action logic
                        val clean = viewModel.searchQuery.trim().replace(Regex("\\s+"), " ")
                        if (clean.isNotBlank()) {
                            viewModel.onSearchQueryChanged(clean) // 👈 FIXED: Final clean string data filter apply kiya

                            if (!recentSearches.contains(clean)) recentSearches.add(0, clean)
                            showResults           = true
                            focusManager.clearFocus()
                        }
                    }),
                    shape  = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor     = Color(0xFFF2F3F5),
                        unfocusedContainerColor   = Color(0xFFF2F3F5),
                        focusedIndicatorColor     = Color.Transparent,
                        unfocusedIndicatorColor   = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }

        // Body Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFF1F5))
        ) {
            if (selectedBrand != null) {
                BrandPageContent(
                    brandName    = selectedBrand!!,
                    products     = emptyList(),
                    isTransitioning = isTransitioning,
                    currentSort  = currentSort,
                    onSortClick  = { currentSort = currentSort.next() },
                    onProductClick = {}
                )
            }
            else if (isSearchModeActive) {
                SearchOverlay(
                    searchText         = searchText,
                    showResults        = showResults,
                    displayQuery       = displayQuery,
                    recentSearches     = recentSearches,
                    filteredPopular    = filteredPopular,
                    filteredProducts   = filteredProducts,
                    filteredCategories = filteredCategories,
                    currentSort        = currentSort,
                    onSortToggle       = { currentSort = currentSort.next() },
                    onFilterClick      = { showFilterSheet = true },
                    onSearchTermClick  = { term ->
                        searchText  = term
                        showResults = true
                        focusManager.clearFocus()
                    },
                    onBrandClick       = { brand ->
                        previousSearchQuery = searchText
                        searchText          = brand
                        selectedBrand       = brand
                    },
                    onClearRecent      = { recentSearches.clear() },
                    onProductClick     = onProductClick
                )
            }
            else {
                DefaultHomeContent()
            }
        }
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState       = sheetState,
            containerColor   = Color.White,
            dragHandle       = { BottomSheetDefaults.DragHandle(color = Color(0xFFE0E0E0)) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
            ) {
                FilterSheetContent(
                    viewModel = viewModel, // 👈 SYSTEM UPDATE: Yeh line add kar dijiye error chali jayegi!
                    onApply = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showFilterSheet = false
                        }
                    },
                    onClear = { showFilterSheet = false }
                )
            }
        }
    }
}

@Composable
private fun DefaultHomeContent() {
    Box(
        modifier          = Modifier.fillMaxSize(),
        contentAlignment  = Alignment.Center
    ) {
        Text(
            text       = "Home Screen",
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = Color(0xFF69F0AE),
            textAlign  = TextAlign.Center
        )
    }
}

@Composable
private fun SearchOverlay(
    searchText         : String,
    showResults        : Boolean,
    displayQuery       : String,
    recentSearches     : MutableList<String>,
    filteredPopular    : List<String>,
    filteredProducts   : List<Products>,
    filteredCategories : List<Pair<String, Int>>,
    currentSort        : SortOption,
    onSortToggle       : () -> Unit,
    onFilterClick      : () -> Unit,
    onSearchTermClick  : (String) -> Unit,
    onBrandClick       : (String) -> Unit,
    onClearRecent      : () -> Unit,
    onProductClick     : (Products) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        if (searchText.isEmpty() && !showResults) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Recent searches", color = Color(0xFF757575), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Clear",
                    color    = Color.Blue,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onClearRecent() }
                )
            }

            recentSearches.take(3).forEach { term ->
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable { onSearchTermClick(term) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF757575), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = term, fontSize = 15.sp, color = Color.Black)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = Color(0xFFF2F3F5))

            Text("Recently viewed", color = Color(0xFF757575), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Row(
                modifier          = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(modifier = Modifier.size(40.dp), color = Color(0xFFF2F3F5), shape = RoundedCornerShape(4.dp)) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray, modifier = Modifier.padding(10.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Recently viewed item placeholder...", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        else if (searchText.isNotEmpty() && !showResults) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

                if (filteredPopular.isNotEmpty()) {
                    Text("Popular searches", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                    filteredPopular.take(5).forEach { term ->
                        Row(
                            modifier          = Modifier
                                .fillMaxWidth()
                                .clickable { onSearchTermClick(term) }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = term, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFF2F3F5))
                }

                if (filteredProducts.isNotEmpty()) {
                    Text("Products", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp))
                    filteredProducts.take(3).forEach { product: Products -> // 👈 Explicit type parameter fix
                        Row(
                            modifier          = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { onSearchTermClick(product.name ?: "") },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(45.dp).background(Color(0xFFF9F9F9), RoundedCornerShape(4.dp)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = product.name ?: "", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFF2F3F5))
                }

                if (filteredCategories.isNotEmpty()) {
                    Text("Categories", color = Color(0xFF757575), fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 12.dp))
                    filteredCategories.chunked(2).forEach { rowItems ->
                        Row(
                            modifier              = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { (name, _) ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(60.dp)
                                        .clickable { onSearchTermClick(name) },
                                    shape  = RoundedCornerShape(18.dp),
                                    border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                                    color  = Color.White
                                ) {
                                    Row(modifier = Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Launch, contentDescription = null, tint = Color(0xFFB87333), modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.Black, maxLines = 1)
                                    }
                                }
                            }
                            if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color(0xFFF2F3F5))
                }

                Text("Recent searches", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                recentSearches.take(2).forEach { term ->
                    Row(
                        modifier          = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = term, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        else if (showResults) {
            val currentCategory  = filteredProducts.firstOrNull()?.store ?: searchText
            val relevantBrands   = getBrandsForCategory(currentCategory)

            ResultsHeader(
                totalCount  = filteredProducts.size,
                query       = displayQuery,
                currentSort = currentSort,
                onSortClick = onSortToggle,
                onFilterClick = onFilterClick
            )
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp) // 👈 Padding badhai taaki cards chipke na
            ) {
                itemsIndexed(filteredProducts) { index: Int, product: Products -> // Explicit type item lambda fix

                    // 🌟 FIXED: Plain Text Box ko hata kar hamara premium SearchResultItem card lagaya
                    SearchResultItem(
                        product = product,
                        onProductClick = onProductClick
                    )


                    if (index == 3 && relevantBrands.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                            Text(
                                text = "Top Brands",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(16.dp, 12.dp)
                            )
                            LazyRow(
                                modifier       = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(relevantBrands) { (name, logo) ->
                                    BrandLogoCircle(
                                        brandName = name,
                                        brandLogo = logo,
                                        onClick   = { onBrandClick(name) }
                                    )
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), thickness = 1.dp, color = Color(0xFFF2F3F5))
                    }
                }
            }
        }
    }
}

private fun SortOption.next() = when (this) {
    SortOption.RELEVANCE      -> SortOption.PRICE_LOW_HIGH
    SortOption.PRICE_LOW_HIGH -> SortOption.PRICE_HIGH_LOW
    SortOption.PRICE_HIGH_LOW -> SortOption.RELEVANCE
}

// ─────────────────────────────────────────────────────────────────────────────
// COMPOSE COMPOSE STUDIO PREVIEWS
// ─────────────────────────────────────────────────────────────────────────────
//
//@Preview(name = "1. Default Base Empty State", showBackground = true, backgroundColor = 0xFFF9FAFB)
//@Composable
//fun NewHomeScreenDefaultPreview() {
//    MaterialTheme {
//        NewHomeScreen(
//            showTopBar = false,
//            onTrackOrderClick = {}
//        )
//    }
//}
//
//@Preview(name = "2. Search View suggestions layer", showBackground = true)
//@Composable
//fun NewHomeScreenSearchActivePreview() {
//    MaterialTheme {
//        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
//            val recentSearches = remember { mutableStateListOf("projector", "apple iphone", "shampoo") }
//            val popularSearches = listOf("iPhone 17", "Samsung S24", "Nike Shoes", "Adidas Sneakers")
//            val categoriesMock = listOf(
//                "Mobiles" to R.drawable.img_7,
//                "Shoes" to R.drawable.img_12
//            )
//
//            SearchOverlay(
//                searchText = "",
//                showResults = false,
//                displayQuery = "",
//                recentSearches = recentSearches,
//                filteredPopular = popularSearches,
//                filteredProducts = emptyList(),
//                filteredCategories = categoriesMock,
//                currentSort = SortOption.RELEVANCE,
//                onSortToggle = {},
//                onFilterClick = {},
//                onSearchTermClick = {},
//                onBrandClick = {},
//                onClearRecent = {},
//                onProductClick = {}            )
//        }
//    }
//}
//
//@Preview(name = "3. Search active with filtering keywords typing", showBackground = true)
//@Composable
//fun NewHomeScreenSearchTypingPreview() {
//    MaterialTheme {
//        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
//            val recentSearches = remember { mutableStateListOf("projector", "apple iphone") }
//            val popularSearches = listOf("iPhone 17", "iPhone 16 Pro Plus")
//            val categoriesMock = listOf("Mobiles" to R.drawable.img_7)
//            val productsMock = listOf(
//                Product("Apple iPhone 17 Pro Max Ultra", "£1299", R.drawable.img_4, "Mobiles", "APPLE"),
//                Product("Apple iPhone 16 Pro Plus", "£1099", R.drawable.img_7, "Mobiles", "APPLE")
//            )
//
//            SearchOverlay(
//                searchText = "Apple",
//                showResults = false,
//                displayQuery = "Apple",
//                recentSearches = recentSearches,
//                filteredPopular = popularSearches,
//                filteredProducts = productsMock,
//                filteredCategories = categoriesMock,
//                currentSort = SortOption.RELEVANCE,
//                onSortToggle = {},
//                onFilterClick = {},
//                onSearchTermClick = {},
//                onBrandClick = {},
//                onClearRecent = {},
//                onProductClick = {}
//            )
//        }
//    }
//}
//@Preview(name = "4. Final Search Product Results View", showBackground = true)
//@Composable
//fun NewHomeScreenProductResultsPreview() {
//    MaterialTheme {
//        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
//            val mockRecentSearches = remember { mutableStateListOf("projector") }
//            val mockProductsResultList = listOf(
//                Product("CASEKOO Armor Shockproof Designed for iPhone 17 Pro Case [16FT Military Grade Protection]...", "£29.99", R.drawable.img_4, "Mobiles", "Apple"),
//                Product("CASEKOO Armor Shockproof Designed for iPhone 17 Pro Case [16FT Military Grade Protection]...", "£29.99", R.drawable.img_6, "Mobiles", "Apple"),
//                Product("CASEKOO Armor Shockproof Designed for iPhone 17 Pro Case [16FT Military Grade Protection]...", "£29.99", R.drawable.img_7, "Mobiles", "Apple"),
//                Product("CASEKOO Armor Shockproof Designed for iPhone 17 Pro Case [16FT Military Grade Protection]...", "£29.99", R.drawable.img_4, "Mobiles", "Apple")
//            )
//            val mockCategories = listOf("Mobiles" to R.drawable.img_7)
//
//            SearchOverlay(
//                searchText = "iPhone 17 case",
//                showResults = true,
//                displayQuery = "iPhone 17 case",
//                recentSearches = mockRecentSearches,
//                filteredPopular = emptyList(),
//                filteredProducts = mockProductsResultList,
//                filteredCategories = mockCategories,
//                currentSort = SortOption.RELEVANCE,
//                onSortToggle = {},
//                onFilterClick = {},
//                onSearchTermClick = {},
//                onBrandClick = {},
//                onClearRecent = {},
//                onProductClick = {}
//            )
//        }
//    }
//}