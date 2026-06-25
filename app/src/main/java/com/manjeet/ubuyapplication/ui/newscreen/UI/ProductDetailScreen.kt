package com.manjeet.ubuyapplication.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.manjeet.ubuyapplication.R
import com.manjeet.ubuyapplication.model.Product
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import com.manjeet.ubuyapplication.ui.newscreen.UI.SelectedWarrantyPlan


import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.ViewModel.ProductViewModel
import com.manjeet.ubuyapplication.model.Data
import com.manjeet.ubuyapplication.model.ProductDetailData
import com.manjeet.ubuyapplication.model.ProductSectionsState
import com.manjeet.ubuyapplication.model.ShippingBreakdownData
import com.manjeet.ubuyapplication.model.VehicleFilterData
import com.manjeet.ubuyapplication.ui.newscreen.UI.UCareWarrantyBottomSheetContent
import com.manjeet.ubuyapplication.viewmodel.ProductDetailUiState
import com.manjeet.ubuyapplication.viewmodel.ProductDetailViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.manjeet.ubuyapplication.model.Modifydata
import com.manjeet.ubuyapplication.model.ProductFaqItem
import com.manjeet.ubuyapplication.model.ProductReviewData
import com.manjeet.ubuyapplication.model.ProsCons
import com.manjeet.ubuyapplication.model.ReviewData




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productData: ProductDetailData,
    product: Product,
    navController: androidx.navigation.NavHostController,
    productsList: List<com.manjeet.ubuyapplication.model.Product> = emptyList(),
    sectionsState: ProductSectionsState,
    modifyDataResponse: com.manjeet.ubuyapplication.model.Modifydata? = null,
    responseWrapper: Any?, // 🔥 Yeh hamari active state ka main link hai
    reviewDataResponse: com.manjeet.ubuyapplication.model.ProductReviewData? = null,
    onVariationSelected: (headingName: String, optionId: String) -> Unit,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onAddVehicleClick: () -> Unit = {},
    onViewBundleItemClick: (String) -> Unit = {},
    onAddWarrantyClick: () -> Unit = {}
)

{
    // Interactive State Management For Selection Items
    var selectedColorIndex by remember { mutableStateOf(0) }
    var selectedSizeIndex by remember { mutableStateOf(0) }
    var selectedImageRes by remember { mutableStateOf(product.image) }

    val colorVariants = listOf("Black", "Clear", "Cosmic Orange", "Deep Blue")
    val sizeVariants = listOf("iPhone 17 ", "iPhone 14", "iPhone 13", "iPhone 12 ")

    val galleryImages = listOf(product.image, product.image, product.image, product.image)

    var selectedWarrantyPlan by remember { mutableStateOf<SelectedWarrantyPlan?>(null) }


    var showWarrantyBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden // ← allows open/expand, only blocks drag-to-hide

        }
    )




    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                title = {
                    Text(
                        text = product.category ?: "Product Detail",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = Color.Black)
                    }
                }
            )
        },
        bottomBar = {
            // Persistent Bottom CTA Execution Panel
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF3F4F6),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Buy Now Logic */ },
                        modifier = Modifier
                            .weight(0.4f)
                            .height(46.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.5.dp, Color(0xFF6E471E)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6E471E),
                            containerColor = Color(0xFFEAEAEA)
                        )
                    ) {
                        Text(
                            text = "Buy now",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onAddToCartClick,
                        modifier = Modifier
                            .weight(0.6f)
                            .height(46.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF134E35),
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add to cart",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF4F6F9))
        ) {

            // ─── 🔑 MASTER CONFIG: RESOLVE UNIFIED STATE FOR BOTH CARDS UPFRONT (FIXED POSITION) ───
            val liveData = (responseWrapper as? com.manjeet.ubuyapplication.model.ProductDetailResponse)?.data
            val finalDataToUse = liveData ?: productData
// ─── CARD 1: COMBINED INFO HEADER & IMAGE CAROUSEL CONTAINER (FULLY SYNCED WITH NEW DATA CLASS) ───
            item {
                // ─── 🔑 IMAGE MAPPER FIXED: Complete live tracking across variant updates ───
                val imageList = remember(finalDataToUse.productImages, finalDataToUse.productImage) {
                    val images = finalDataToUse.productImages
                    val singleImg = finalDataToUse.productImage

                    if (!images.isNullOrEmpty()) {
                        images.map {
                            // 🎯 FIXED: Updated to use 'thumbList' variable name instead of old crashing code
                            (it.full ?: "").ifEmpty { (it.thumbList ?: "").ifEmpty { it.thumb ?: "" } }
                        }.filter { it.isNotEmpty() }
                    } else if (!singleImg.isNullOrEmpty()) {
                        listOf(singleImg)
                    } else {
                        emptyList()
                    }
                }

                // Pager State Initialization
                val pagerState = androidx.compose.foundation.pager.rememberPagerState(
                    initialPage = 0,
                    pageCount = { imageList.size }
                )

                // Automatically snap pager to 0 if list shuffles/updates (e.g. Color variation changes)
                LaunchedEffect(imageList) {
                    if (imageList.isNotEmpty()) {
                        pagerState.scrollToPage(0)
                    }
                }

                val coroutineScope = rememberCoroutineScope()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

                        // ─── ROW 1: BRAND LOGO & STARS ─────────────────────────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color(0xFFDC2626), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (finalDataToUse.brand ?: "A").ifEmpty { "A" }.take(1),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = (finalDataToUse.brand ?: "Apple").ifEmpty { "Apple" },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) {
                                    Icon(
                                        Icons.Default.Star,
                                        null,
                                        tint = Color(0xFFFFC107),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = " 4.8 (145)",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // ─── ROW 2: PRODUCT TITLE (LIVE UPDATING ON VARIANT CLICK) ───────────
                        Text(
                            text = (finalDataToUse.productTitle ?: "Apple iPhone Air").ifEmpty { "Apple iPhone Air" },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // ─── ROW 3: GREEN PRODUCT SHEET LABEL ──────────────────────────────
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFF10B981),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "A",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Product Sheet",
                                color = Color(0xFF10B981),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // ─── ROW 4: RESPONDENTS RECOMMENDATION TEXT ────────────────────────
                        Text(
                            text = "86% of respondents would recommend this to a friend",
                            fontSize = 12.sp,
                            color = Color(0xFF10B981),
                            fontStyle = FontStyle.Italic
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // ─── ROW 5: ACTIONS BUTTONS & STORE ALIGNMENT ───────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                onClick = {},
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.FavoriteBorder,
                                        null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Surface(
                                onClick = {},
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Share,
                                        null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val currentStore = (finalDataToUse.ubuyStore ?: "JP").uppercase()
                                Text(
                                    text = "$currentStore Store ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray
                                )

                                // Dynamic Flag Selection based on ubuyStore
                                val flagResource = if (currentStore == "US") R.drawable.ic_usa_flag else R.drawable.ic_uk_flag

                                Image(
                                    painter = painterResource(id = flagResource),
                                    contentDescription = "Store Flag",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ─── ROW 6: HORIZONTAL SLIDING PAGER WITH COIL ENHANCEMENT ───
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageList.isNotEmpty()) {
                                androidx.compose.foundation.pager.HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxSize(),
                                    userScrollEnabled = true
                                ) { index ->
                                    val currentSlideSource = imageList.getOrNull(index) ?: ""

                                    androidx.compose.runtime.key(currentSlideSource) {
                                        coil.compose.AsyncImage(
                                            model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                                .data(currentSlideSource.ifEmpty { R.drawable.img_4 })
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Product Carousel Slide Image #$index",
                                            placeholder = painterResource(id = R.drawable.img_4),
                                            error = painterResource(id = R.drawable.img_4),
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.img_4),
                                    contentDescription = "Local Project Asset Fallback Framework",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            if ((finalDataToUse.bestSeller ?: false) || finalDataToUse.isDealActive == "yes") {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .background(Color(0xFFDC2626), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Trending",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ─── ROW 7: THUMBNAILS HORIZONTAL SCROLL GALLERY SYSTEM ───
                        if (imageList.size > 1) {
                            androidx.compose.foundation.lazy.LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(
                                    count = imageList.size,
                                    key = { index -> "${imageList.getOrNull(index) ?: ""}_${index}_thumb" }
                                ) { index ->
                                    val isSelected = pagerState.currentPage == index
                                    val borderStrokeColor = if (isSelected) Color(0xFFFFC107) else Color(0xFFE5E7EB)
                                    val borderWidthDp = if (isSelected) 2.5.dp else 1.dp

                                    val currentThumbnailSource = imageList.getOrNull(index) ?: ""

                                    Card(
                                        modifier = Modifier.size(54.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(borderWidthDp, borderStrokeColor),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        onClick = {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            androidx.compose.runtime.key(currentThumbnailSource) {
                                                coil.compose.AsyncImage(
                                                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                                        .data(currentThumbnailSource.ifEmpty { R.drawable.img_4 })
                                                        .crossfade(true)
                                                        .build(),
                                                    contentDescription = "Thumbnail selection target",
                                                    modifier = Modifier.size(44.dp),
                                                    contentScale = ContentScale.Fit,
                                                    placeholder = painterResource(id = R.drawable.img_4),
                                                    error = painterResource(id = R.drawable.img_4)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } // END CARD 1


// CARD 2: UNIFIED MASTER CONTAINER WITH DYNAMIC SYNCHRONIZATION (FULLY NULL-SAFE DEPLOYMENT)
            item {
                val variationsList = remember(finalDataToUse.variationData) {
                    finalDataToUse.variationData ?: emptyList()
                }

                val colorVariation = variationsList.find {
                    it.heading?.contains("Color", ignoreCase = true) == true
                }
                val sizeVariation = variationsList.find {
                    it.heading?.contains("Size", ignoreCase = true) == true ||
                            it.heading?.contains("Storage", ignoreCase = true) == true ||
                            it.heading?.contains("Capacity", ignoreCase = true) == true
                }

                // 🎯 LIVE MATCHING SELECTION EXTRACTORS
                val selectedColorOption = colorVariation?.options?.find { it.selected == true }
                val currentSelectedColorId = selectedColorOption?.optionId ?: ""
                val currentSelectedColorName = selectedColorOption?.value ?: ""

                val selectedSizeOption = sizeVariation?.options?.find { it.selected == true }
                val currentSelectedSizeId = selectedSizeOption?.optionId ?: ""
                val currentSelectedSizeName = selectedSizeOption?.value ?: ""

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp)
                    ) {

                        // 🎨 SECTION 1: COLOR SELECTION (Dynamic Heading Integrated)
                        if (colorVariation != null && !colorVariation.options.isNullOrEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Color: ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = currentSelectedColorName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.ROOT) else it.toString() },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF374151)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            androidx.compose.foundation.lazy.LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            ) {
                                val safeOptions = colorVariation.options.orEmpty()
                                items(
                                    count = safeOptions.size,
                                    key = { index -> safeOptions[index].optionId ?: index.toString() }
                                ) { index ->
                                    val option = safeOptions[index]
                                    val isColorSelected = currentSelectedColorId == option.optionId
                                    val isAvailable = option.available == true

                                    val cleanChipImgUrl = remember(option.imgUrl, currentSelectedColorId) {
                                        val raw = option.imgUrl ?: ""
                                        // 🛠️ FIX REPLACEMENT: Miniature resolution ko clear view thumbnail se swap kiya
                                        if (raw.contains("._SS64_")) raw.replace("._SS64_", "._AC_SX342_") else raw
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clickable(enabled = isAvailable) {
                                                val targetHeading = colorVariation.heading ?: "Color"
                                                val targetOptionId = option.optionId ?: ""
                                                android.util.Log.d("UBUY_DEBUG", "⚡ [UI-Click] COLOR SELECT: $targetOptionId")
                                                onVariationSelected(targetHeading, targetOptionId)
                                            }
                                            .then(if (!isAvailable) Modifier.alpha(0.4f) else Modifier)
                                    ) {
                                        Card(
                                            modifier = Modifier.size(64.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(
                                                width = if (isColorSelected) 2.5.dp else 1.dp,
                                                color = if (isColorSelected) Color(0xFFFFC107) else Color(0xFFE5E7EB)
                                            ),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
                                        ) {
                                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                coil.compose.AsyncImage(
                                                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                                        .data(cleanChipImgUrl.ifEmpty { R.drawable.img_4 })
                                                        .crossfade(true)
                                                        .build(),
                                                    contentDescription = option.value ?: "",
                                                    modifier = Modifier.size(52.dp).padding(4.dp),
                                                    contentScale = ContentScale.Fit,
                                                    placeholder = painterResource(id = R.drawable.img_4),
                                                    error = painterResource(id = R.drawable.img_4)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))

                                        val displayChipName = (option.value ?: "").replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(java.util.Locale.ROOT) else it.toString()
                                        }
                                        Text(
                                            text = displayChipName,
                                            fontSize = 11.sp,
                                            fontWeight = if (isColorSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isColorSelected) Color.Black else Color.Gray
                                        )
                                    }
                                }
                            }
                        }

                        if (colorVariation != null && sizeVariation != null) {
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // 📏 SECTION 2: SIZE / STORAGE SELECTION MATRIX
                        if (sizeVariation != null && !sizeVariation.options.isNullOrEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Memory Storage Capacity: ",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = currentSelectedSizeName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF374151)
                                    )
                                }
                                Text(
                                    text = "Size Chart",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F),
                                    modifier = Modifier.clickable { }
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                sizeVariation.options.orEmpty().chunked(3).forEach { chunkedOptions ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        chunkedOptions.forEach { option ->
                                            val isSizeSelected = currentSelectedSizeId == option.optionId
                                            val isAvailable = option.available == true

                                            Surface(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(46.dp)
                                                    .clickable(enabled = isAvailable) {
                                                        val targetHeading = sizeVariation.heading ?: "Size"
                                                        val targetOptionId = option.optionId ?: ""
                                                        android.util.Log.d("UBUY_DEBUG", "⚡ [UI-Click] SIZE SELECT: $targetOptionId")
                                                        onVariationSelected(targetHeading, targetOptionId)
                                                    }
                                                    .then(if (!isAvailable) Modifier.alpha(0.35f) else Modifier),
                                                shape = RoundedCornerShape(10.dp),
                                                color = if (isSizeSelected) Color(0xFFFFFBEB) else Color.White,
                                                border = BorderStroke(
                                                    width = if (isSizeSelected) 2.5.dp else 1.dp,
                                                    color = if (isSizeSelected) Color(0xFFFFC107) else Color(0xFFE5E7EB)
                                                )
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = option.value ?: "",
                                                        fontSize = 13.sp,
                                                        fontWeight = if (isSizeSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                        color = if (isSizeSelected) Color.Black else if (!isAvailable) Color.LightGray else Color(0xFF374151)
                                                    )
                                                }
                                            }
                                        }
                                        if (chunkedOptions.size < 3) {
                                            Spacer(modifier = Modifier.weight((3 - chunkedOptions.size).toFloat()))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } //END CARD 2

            // CARD 3: SINGLE UNIFIED MASTER CONTAINER FOR REMAINING SECTIONS
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // INNER BOX 4: EXTENDED DEAL & STOCK METRICS
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier.size(8.dp)
                                            .background(Color(0xFF2E7D32), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "In stock",
                                        color = Color(0xFF2E7D32),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text("# 176625820", color = Color.Gray, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Total Bought : 16.8k", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // 🌟 Direct price validation lookup from response fields
                                val finalPriceText =
                                    if (productData.price != null && productData.price != 0) {
                                        "£${productData.price}.00"
                                    } else {
                                        "£29.99" // Backup fallback hardcoded text
                                    }

                                Text(
                                    text = finalPriceText,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "You Save : 20%",
                                    color = Color(0xFF2E7D32),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = Color(0xFFD32F2F),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        "Limited Time Deal",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Only 7 items left in stock.",
                                    color = Color(0xFFD32F2F),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "This item is not Fulfilled by Ubuy. Delivery takes 10+ days minimum. If delivery issues arise, the item may be cancelled and refunded.",
                                color = Color(0xFF2E7D32),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0xFFFAFAFA),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                            ) {
                                Text(
                                    text = "Excluding VAT: VAT will be calculated on the checkout page.",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        // INNER BOX 5: SECURED TRANSACTIONS & PAYMENT TRUST BADGES
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Secured Transaction",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val paymentMethods = listOf(
                                    R.drawable.ic_apple_pay,
                                    R.drawable.ic_mastercard,
                                    R.drawable.ic_applepay,
                                    R.drawable.ic_googlepay
                                )
                                paymentMethods.forEach { drawableResId ->
                                    Surface(
                                        modifier = Modifier.weight(1f).height(36.dp),
                                        color = Color(0xFFF9FAFB),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize().padding(6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = painterResource(id = drawableResId),
                                                contentDescription = "Payment Method",
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 14.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        color = Color(0xFFFFA3C4),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "Klarna.",
                                            fontWeight = FontWeight.Black,
                                            color = Color.Black,
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(
                                                horizontal = 14.dp,
                                                vertical = 8.dp
                                            )
                                        )
                                    }
                                    Text(text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                color = Color(0xFF374151),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        ) { append("Buy now, pay in 30 days. ") }
                                        withStyle(
                                            style = SpanStyle(
                                                color = Color(0xFF1F2937),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                textDecoration = TextDecoration.Underline
                                            )
                                        ) { append("Learn more") }
                                    })
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(2.dp, Color(0xFFE5E7EB)),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = "Ships from Ubuy, Sold by : UBNA Distribution LLC",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF85586F)
                                )
                            }
                        }

                        // INNER BOX 6: VOLTAGE TRANSFORMER WARNING BLOCK
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Text("⚠️", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Step Down Voltage Transformer required for using electronics products of US store (110-120). Recommended power converters are available here.",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray,
                                        lineHeight = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    val transformerImages = listOf(
                                        R.drawable.img_32,
                                        R.drawable.img_29,
                                        R.drawable.img_30,
                                        R.drawable.img_4
                                    )

                                    transformerImages.forEach { imageResId ->
                                        Box(
                                            modifier = Modifier.size(40.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = painterResource(id = imageResId),
                                                contentDescription = "Recommended Converter",
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .border(
                                                        BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                                        RoundedCornerShape(4.dp)
                                                    ),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // INNER BOX 7: VEHICLE FILTER UTILITY CARD (DYNAMICALLY LINKED)
                        sectionsState.vehicleFilter?.let { filterData ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onAddVehicleClick() }
                                    .padding(14.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = filterData.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "+",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = filterData.warningText,
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Attention: We strongly recommend to fill the vehicle information before adding the product in your cart to get the perfect match. In the event of skipping this step, the product will be excluded from our return policy.",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        // INNER BOX 8: BUNDLE ITEMS STACK (DYNAMICALLY LINKED)
                        sectionsState.bundleInfo?.let { bundle ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "This bundle contains 2 items",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Bundle Product 1
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
                                            Image(
                                                painter = painterResource(id = R.drawable.img_adapter),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(52.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(text = "Portable Power Station 500W,444Wh 110V for RV/Van...", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            OutlinedButton(
                                                onClick = { onViewBundleItemClick("item_1") },
                                                modifier = Modifier.height(32.dp),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                                border = BorderStroke(1.dp, Color(0xFFB87333)),
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB87333))
                                            ) {
                                                Text("View Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Bundle Product 2
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
                                            Image(
                                                painter = painterResource(id = R.drawable.img_stand),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(52.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(text = "Portable Power Station 330W, Solar Powered Generator with...", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            OutlinedButton(
                                                onClick = { onViewBundleItemClick("item_2") },
                                                modifier = Modifier.height(32.dp),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                                border = BorderStroke(1.dp, Color(0xFFB87333)),
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB87333))
                                            ) {
                                                Text("View Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        } // END INNER BOX 8

                        // INNER BOX 9: SHIPPING FLOW & DETAILED COST BREAKDOWN
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "All items ship from the US to the UK. Estimated shipping and import fees shown; precise costs will be available at checkout.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF4B5563),
                                        lineHeight = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Estimated fastest delivery", fontSize = 13.sp, color = Color(0xFF374151), fontWeight = FontWeight.Medium)
                                    Text("Tue, Nov 11", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Circular Flags route
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_usa_flag),
                                            contentDescription = "US Flag",
                                            modifier = Modifier.size(18.dp).clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("US", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }

                                    Text(
                                        text = " ---------------- ✈ --------------- ",
                                        color = Color(0xFFFFC107),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_uk_flag),
                                            contentDescription = "UK Flag",
                                            modifier = Modifier.size(18.dp).clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("UK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                val breakdownItems = listOf(
                                    "Subtotal" to "£24.00",
                                    "Standard Shipping" to "£19.20",
                                    "Customs & Duties" to "£4.90",
                                    "VAT" to "£9.60"
                                )
                                breakdownItems.forEach { (label, cost) ->
                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = label, fontSize = 13.sp, color = Color(0xFF4B5563))
                                        Text(text = cost, fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(thickness = 1.dp, color = Color(0xFFE5E7EB))
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Estimated Grand Total", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text("£57.60", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                }
                            }
                        } // END INNER BOX 9

                        // INNER BOX 10: UNLOCK FAST SHIPPING
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_fast_delivery),
                                    contentDescription = "Fast Shipping Icon",
                                    modifier = Modifier.size(52.dp).padding(end = 12.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(text = "Unlock Fast Shipping!", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Add additional items worth ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("£534.8") }
                                            append(" to get Fast Shipping at standard price.")
                                        },
                                        fontSize = 13.sp,
                                        color = Color(0xFF4B5563),
                                        lineHeight = 18.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    LinearProgressIndicator(
                                        progress = 0.6f,
                                        modifier = Modifier.fillMaxWidth().height(6.dp),
                                        color = Color(0xFFD97706),
                                        trackColor = Color(0xFFE5E7EB),
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                            }
                        } // END INNER BOX 10

                        // INNER BOX 11: U-CARE WARRANTY PLAN
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {

                                if (selectedWarrantyPlan == null) {
                                    // ── EMPTY STATE (original) ──
                                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_warranty),
                                            contentDescription = "U-Care Warranty Icon",
                                            modifier = Modifier.size(52.dp).padding(end = 12.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                        Column {
                                            Text(text = "U-Care Warranty", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "From basic repairs to full 3-year coverage with accidental, spill, and fire protection options.",
                                                fontSize = 13.sp,
                                                color = Color(0xFF4B5563),
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFF3F4F6))
                                            .clickable { showWarrantyBottomSheet = true } // 👈 Sheet yahan se khulegi
                                            .padding(vertical = 14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "Add a U-Care Plan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF4B5563))
                                        }
                                    }

                                } else {
                                    // ── SELECTED STATE ──
                                    val plan = selectedWarrantyPlan!!

                                    // Colored banner with logo + plan name
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = plan.bannerColor,
                                                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                                            )
                                            .padding(horizontal = 14.dp, vertical = 12.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = plan.logoResId),
                                                contentDescription = "${plan.planName} Logo",
                                                modifier = Modifier.size(36.dp),
                                                contentScale = ContentScale.Fit
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(text = plan.planName, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                                Text(text = "UCare warranty plan", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                                            }
                                        }
                                    }

                                    // Tier title + price + Change button
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = plan.tierTitle, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = plan.price, fontSize = 14.sp, fontWeight = FontWeight.Black, color = plan.bannerColor)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .border(BorderStroke(1.dp, plan.bannerColor), RoundedCornerShape(8.dp))
                                                .clickable { showWarrantyBottomSheet = true }
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(text = "Change", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = plan.bannerColor)
                                        }
                                    }
                                }
                            }
                        } // END INNER BOX 11

                        Spacer(modifier = Modifier.height(24.dp))

                    } // END Card 3 Column
                } // END Card 3
            } // END Card 3 item

// ─────────────────────────────────────────────────────────────────────────────
// CARD 4: 🎯 100% REAL DATA BOUND - AI PRODUCT INSIGHTS & FAQ
// ─────────────────────────────────────────────────────────────────────────────
            item {
                // ─── 📦 1. DIRECT BINDING FROM YOUR REAL RESPONSE MODELS ───
                val prosConsObj =
                    remember(modifyDataResponse) { modifyDataResponse?.data?.prosCons }
                val featuresObj =
                    remember(modifyDataResponse) { modifyDataResponse?.data?.features }

                val prosList: List<String> = remember(prosConsObj) {
                    prosConsObj?.pros?.filter { it.isNotBlank() } ?: emptyList()
                }

                val consList: List<String> = remember(prosConsObj) {
                    prosConsObj?.cons?.filter { it.isNotBlank() } ?: emptyList()
                }

                val quoteText: String = remember(prosConsObj) {
                    prosConsObj?.editorialReview ?: ""
                }

                val faqItemsList: List<com.manjeet.ubuyapplication.model.ProductFaqItem> =
                    remember(featuresObj) {
                        featuresObj?.faq?.filter { !it.question.isNullOrBlank() && !it.answer.isNullOrBlank() }
                            ?: emptyList()
                    }

                val featuresData = modifyDataResponse?.data?.features // Aapka features object

                // Main layout container renders cleanly ONLY if any of the dynamic data sections exist
                if (prosList.isNotEmpty() || consList.isNotEmpty() || quoteText.isNotEmpty() || faqItemsList.isNotEmpty()) {
                    Column(modifier = androidx.compose.ui.Modifier.fillMaxWidth()) {



                        // ── FEATURES & BENEFITS  ───────────────────────
                        val featuresData = modifyDataResponse?.data?.features

                        if (featuresData != null && featuresData.featuresList.isNotEmpty()) {
                            Column(
                                modifier = androidx.compose.ui.Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                // ── 1. EXACT SECTION TITLE ──
                                Text(
                                    text = "Features & Benefits",
                                    fontSize = 20.sp, // Match premium large bold look
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827),
                                    modifier = androidx.compose.ui.Modifier
                                        .padding(start = 20.dp, end = 20.dp, bottom = 14.dp)
                                )

                                // ── 2. MAIN CARD EMBED WITH HIGH ROUNDED CORNERS ──
                                Card(
                                    modifier = androidx.compose.ui.Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    shape = RoundedCornerShape(28.dp), // Premium smooth corner from the image
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(
                                        1.dp,
                                        Color(0xFFE5E7EB)
                                    ) // Soft thin outer boundary line
                                ) {
                                    Column(
                                        modifier = androidx.compose.ui.Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = 20.dp,
                                                vertical = 24.dp
                                            ), // Spacious inside padding
                                        verticalArrangement = Arrangement.spacedBy(18.dp) // Exact vertical line gap between points
                                    ) {
                                        featuresData.featuresList.forEach { featureText ->
                                            if (featureText.isNotBlank()) {
                                                Row(
                                                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.Top
                                                ) {
                                                    // 🎯 EXACT ORANGE/BROWN BULLET POINT DOT
                                                    Text(
                                                        text = "•",
                                                        fontSize = 20.sp, // Bold visible dot size
                                                        color = Color(0xFFC2410C), // Exact Deep Orange/Brown hex tint
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = androidx.compose.ui.Modifier.padding(
                                                            end = 12.dp,
                                                            top = 0.dp
                                                        ) // Perfect horizontal text push
                                                    )

                                                    // 🎯 CLEAN RESILIENT DETAIL TEXT
                                                    Text(
                                                        text = featureText,
                                                        fontSize = 13.sp,
                                                        color = Color(0xFF374151), // Premium Charcoal Dark text color
                                                        fontWeight = FontWeight.Normal,
                                                        lineHeight = 19.sp // Perfect readable text spacing vertical
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        // ── PROS CARD ────────────────────────────────────────────────────────
                        if (prosList.isNotEmpty() || consList.isNotEmpty()) {
                            Row(
                                modifier = androidx.compose.ui.Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()) // Single scroll container for both columns
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp) // Gap between Pros column and Cons column
                            ) {

                                // ─── 🟢 LEFT COLUMN: PROS (Exact Width Layout) ───
                                Column(
                                    modifier = androidx.compose.ui.Modifier
                                        .width(260.dp) // Fixed width taaki right side ka Cons shuru me kata hua dikhe
                                        .padding(start = 16.dp), // Screen boundary gap for the first view
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    // Section Title
                                    Text(
                                        text = "Pros",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827),
                                        modifier = androidx.compose.ui.Modifier.padding(bottom = 4.dp)
                                    )

                                    // Loop for Pros items
                                    prosList.forEach { proTag ->
                                        if (proTag.isNotBlank()) {
                                            Box(
                                                modifier = androidx.compose.ui.Modifier
                                                    .fillMaxWidth()
                                                    .height(80.dp) // 🔥 FIXED HEIGHT: Isse saare boxes ka size ekdum same aur equal ho jayega
                                                    .background(
                                                        Color.White,
                                                        RoundedCornerShape(24.dp)
                                                    ) // White pill layout
                                                    .padding(horizontal = 14.dp, vertical = 14.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.Top) {
                                                    // 🔥 TINT KE BINA DIRECT DRAWABLE ICON
                                                    androidx.compose.material3.Icon(
                                                        painter = androidx.compose.ui.res.painterResource(
                                                            id = com.manjeet.ubuyapplication.R.drawable.ic_thumbs_up
                                                        ), // Aapka drawable name
                                                        contentDescription = "Thumbs Up",
                                                        modifier = androidx.compose.ui.Modifier
                                                            .padding(end = 10.dp, top = 2.dp)
                                                            .size(40.dp) // Icon ka standard size match karne ke liye
                                                    )

                                                    Text(
                                                        text = proTag,
                                                        fontSize = 14.sp,
                                                        color = Color(0xFF374151),
                                                        fontWeight = FontWeight.Medium,
                                                        lineHeight = 16.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // ─── 🟠 RIGHT COLUMN: CONS (Exact Width Layout) ───
                                Column(
                                    modifier = androidx.compose.ui.Modifier
                                        .width(260.dp) // Matching exact width structure
                                        .padding(end = 16.dp), // Padding for the last view block boundary
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    // Section Title
                                    Text(
                                        text = "Cons",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827),
                                        modifier = androidx.compose.ui.Modifier.padding(bottom = 4.dp)
                                    )

                                    // Loop for Cons items
                                    consList.forEach { conTag ->
                                        if (conTag.isNotBlank()) {
                                            Box(
                                                modifier = androidx.compose.ui.Modifier
                                                    .fillMaxWidth()
                                                    .height(80.dp) // 🔥 FIXED HEIGHT: Pros aur Cons dono ke saare boxes ekdum symmetrical rahenge
                                                    .background(
                                                        Color.White,
                                                        RoundedCornerShape(24.dp)
                                                    ) // White pill layout
                                                    .padding(horizontal = 14.dp, vertical = 14.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.Top) {
                                                    // 🔥 TEXT EMOJI KI JAGAH DIRECT THUMPS DOWN DRAWABLE ICON
                                                    androidx.compose.material3.Icon(
                                                        painter = androidx.compose.ui.res.painterResource(
                                                            id = com.manjeet.ubuyapplication.R.drawable.ic_thumbs_down
                                                        ), // Aapka thumbs down drawable file name
                                                        contentDescription = "Thumbs Down",
                                                        tint = androidx.compose.ui.graphics.Color.Unspecified, // 🔥 Unspecified lagane se bina color change ke asli image dikhegi
                                                        modifier = androidx.compose.ui.Modifier
                                                            .padding(end = 10.dp, top = 2.dp)
                                                            .size(40.dp) // Icon ka standard size match karne ke liye
                                                    )

                                                    Text(
                                                        text = conTag,
                                                        fontSize = 14.sp,
                                                        color = Color(0xFF374151),
                                                        fontWeight = FontWeight.Medium,
                                                        lineHeight = 16.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        // ── PRODUCT DESCRIPTION INTERMEDIATE ROW (FINAL FIXED CLICK WORKING) ──
                        Card(
                            modifier = androidx.compose.ui.Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)), // Premium Deep Green

                            // 🌟 ACTUALLY TRIGGER NAVIGATION NOW USING SAFE BASE64 ENCODING
                            onClick = {
                                // 🌟 Check if data is null or empty, and fall back to title if needed for testing
                                val rawDescription = if (!productData.productDescription.isNullOrBlank()) {
                                    productData.productDescription!!
                                } else if (!productData.productTitle.isNullOrBlank()) {
                                    "<h3>${productData.productTitle}</h3><p>Product information description wrapper loaded via live asset pipeline matching JSON requirements.</p>"
                                } else {
                                    "<p>No description asset data found in the current model parser stream.</p>"
                                }

                                // Logcat mein filter 'UBUY_DEBUG' laga kar check kijiye ki data pass kya ho raha hai
                                android.util.Log.d("UBUY_DEBUG", "Original Description Text: $rawDescription")

                                // Android 24+ safe URL encoding wrapper
                                val encodedDesc = android.util.Base64.encodeToString(
                                    rawDescription.toByteArray(Charsets.UTF_8),
                                    android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP
                                )

                                navController.navigate("description_screen/$encodedDesc")
                            }
                        ) {
                            Row(
                                modifier = androidx.compose.ui.Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = androidx.compose.ui.res.painterResource(
                                        id = com.manjeet.ubuyapplication.R.drawable.description_icon
                                    ),
                                    contentDescription = "Product Description Icon",
                                    modifier = androidx.compose.ui.Modifier
                                        .padding(end = 12.dp)
                                        .size(24.dp),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Fit
                                )

                                Text(
                                    text = "Product Description",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = androidx.compose.ui.Modifier.weight(1f)
                                )

                                Box(
                                    modifier = androidx.compose.ui.Modifier
                                        .size(20.dp)
                                        .background(Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Navigate Details",
                                        tint = Color(0xFF064E3B),
                                        modifier = androidx.compose.ui.Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        // ── QUESTIONS & ANSWER CARD  ───────────────────
                        // ── FAQ CARD WITH SEAMLESS BLUR-FADE MASK OVERLAY ───────────────────────────────────
                        if (faqItemsList.isNotEmpty()) {
                            var isCardExpanded by remember { mutableStateOf(false) }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                // Title Section
                                Text(
                                    text = "Questions & Answer",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827),
                                    modifier = Modifier
                                        .padding(start = 20.dp, end = 20.dp, bottom = 14.dp)
                                )

                                // Main Container Card
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    shape = RoundedCornerShape(28.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateContentSize(animationSpec = tween(durationMillis = 300)) // Smooth structural transitions
                                            .padding(
                                                start = 20.dp,
                                                end = 20.dp,
                                                top = 24.dp,
                                                bottom = 16.dp
                                            )
                                    ) {
                                        // 🎯 CONTROLLER BOX: Dynamic Height constraint combined with Offscreen composite pipeline
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .then(
                                                    if (!isCardExpanded) {
                                                        Modifier
                                                            .heightIn(max = 340.dp) // Perfect height setup for selective preview cutting
                                                            .graphicsLayer {
                                                                // Prevents content disappearance/blank screen by isolating render pipeline
                                                                compositingStrategy =
                                                                    CompositingStrategy.Offscreen
                                                            }
                                                            .drawWithContent {
                                                                drawContent()
                                                                // Premium transparency blur gradient running strictly along the lowest lip edge
                                                                drawRect(
                                                                    brush = Brush.verticalGradient(
                                                                        colors = listOf(
                                                                            Color.White,
                                                                            Color.Transparent
                                                                        ),
                                                                        startY = size.height * 0.70f, // Starts fading beautifully at 70% threshold
                                                                        endY = size.height
                                                                    ),
                                                                    blendMode = BlendMode.DstIn
                                                                )
                                                            }
                                                    } else {
                                                        Modifier.wrapContentHeight()
                                                    }
                                                )
                                        ) {
                                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                                faqItemsList.forEach { faqItem ->
                                                    val currentQuestion = faqItem.question ?: ""
                                                    val currentAnswer = faqItem.answer ?: ""

                                                    if (currentQuestion.isNotEmpty() && currentAnswer.isNotEmpty()) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            verticalAlignment = Alignment.Top
                                                        ) {
                                                            // Orange Bullet Point Dot
                                                            Text(
                                                                text = "•",
                                                                fontSize = 20.sp,
                                                                color = Color(0xFFC2410C),
                                                                fontWeight = FontWeight.Bold,
                                                                modifier = Modifier.padding(end = 12.dp)
                                                            )

                                                            Column(modifier = Modifier.weight(1f)) {
                                                                Text(
                                                                    text = currentQuestion,
                                                                    fontSize = 13.sp,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = Color(0xFF111827),
                                                                    lineHeight = 18.sp
                                                                )
                                                                Spacer(modifier = Modifier.height(4.dp))
                                                                Text(
                                                                    text = currentAnswer,
                                                                    fontSize = 13.sp,
                                                                    color = Color(0xFF4B5563),
                                                                    lineHeight = 19.sp
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // ── TOGGLE ACTION BUTTON (READ MORE / READ LESS) ──
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { isCardExpanded = !isCardExpanded }
                                                .padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = if (isCardExpanded) "Read less" else "Read more",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFFC2410C)
                                            )
                                            Icon(
                                                imageVector = if (isCardExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                                contentDescription = "Toggle View Layout Trigger",
                                                tint = Color(0xFFC2410C),
                                                modifier = Modifier
                                                    .padding(start = 4.dp)
                                                    .size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } // ─── END CARD 4 ───//


// ─────────────────────────────────────────────────────────────────────────────
// CARD 5: 🎯 WHY CHOOSE THIS PRODUCT (MATCHING SPECIFIC UI SCHEME)
// ─────────────────────────────────────────────────────────────────────────────
            item {
                val reviewObj = reviewDataResponse
                    ?: (responseWrapper as? com.manjeet.ubuyapplication.model.OtherDetail)?.data

                val cardTitle = reviewObj?.chooseProduct?.title ?: "Why choose this product?"
                val featuresList = reviewObj?.chooseProduct?.features ?: emptyList()

                if (featuresList.isNotEmpty()) {
                    Text(
                        text = cardTitle,
                        fontSize = 18.sp, // UI standard match karne ke liye thoda bada kiya hai
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 8.dp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(24.dp), // 🔥 UI ke hisab se 24.dp kiya hai premium curve ke liye
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp), // Padding inside card increased to 20.dp
                            verticalArrangement = Arrangement.spacedBy(16.dp) // List items spacing matching sample
                        ) {
                            featuresList.forEach { featureItem ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    // 🔥 DUMMY EMOJI KI JAGAH DRAWABLE TICK IMAGE LAGAYA HAI
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_brown_checkmark), // Aapne drawables folder mein jo bhi checkmark photo daali hai vo yahan likhein
                                        contentDescription = "Feature Checked",
                                        modifier = Modifier
                                            .padding(top = 2.dp, end = 12.dp) // Perfect line alignment text ke sath
                                            .size(34.dp) // Size matched with image layout
                                    )

                                    Text(
                                        text = featureItem,
                                        fontSize = 14.sp,
                                        color = Color(0xFF374151),
                                        lineHeight = 20.sp, // Line height exact match text layouting ke liye
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ─────────────────────────────────────────────────────────────────────────────
            // CARD 6: 📊 RATINGS & REVIEWS WITH RECOMMENDATION INSIGHTS
            // ─────────────────────────────────────────────────────────────────────────────
            item {
                val reviewObj = reviewDataResponse
                    ?: (responseWrapper as? com.manjeet.ubuyapplication.model.OtherDetail)?.data

                val ratingDetail = reviewObj?.customerRatingDetail
                val totalReviewsCount = ratingDetail?.totalReview ?: 0
                val finalRating = ratingDetail?.totalRating ?: 0.0
                val breakdown = ratingDetail?.ratingBreakdown

                val recSummary = reviewObj?.recommendationSummary
                val recommendPercentage = recSummary?.recommendPercentage ?: 0
                val complaintRate = recSummary?.complaintRate ?: ""

                if (totalReviewsCount > 0) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Ratings & reviews",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            )
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                // ─── Top Rating Breakdown Row ───
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1.1f)
                                    ) {
                                        Text(
                                            text = String.format(java.util.Locale.US, "%.1f", finalRating),
                                            fontSize = 38.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF111827)
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            // 🔥 Spacing ko tight karne ke liye spacedBy use kiya (apne hisab se 2.dp ya 1.dp kar sakte hain)
                                            horizontalArrangement = Arrangement.spacedBy(-10.dp, Alignment.CenterHorizontally)
                                        ) {
                                            val totalStarsCount = finalRating.toInt()
                                            for (starIdx in 0 until 5) {
                                                Image(
                                                    painter = painterResource(
                                                        id = if (starIdx < totalStarsCount) R.drawable.ic_rating_star else R.drawable.ic_rating_star_empty
                                                    ),
                                                    contentDescription = "Star Indicator",
                                                    modifier = Modifier.size(30.dp) // Screenshot standard ke mutabik size ko 16.dp kiya hai
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "$totalReviewsCount ratings",
                                            fontSize = 12.sp,
                                            color = Color(0xFF9CA3AF)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier.weight(1.9f),
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        val star5 = breakdown?.star5 ?: 0
                                        val star4 = breakdown?.star4 ?: 0
                                        val star3 = breakdown?.star3 ?: 0
                                        val star2 = breakdown?.star2 ?: 0
                                        val star1 = breakdown?.star1 ?: 0

                                        val totalSumOfStars = (star5 + star4 + star3 + star2 + star1).toFloat()
                                        val totalValue: Float = if (totalSumOfStars > 0f) totalSumOfStars else 1f

                                        val barsMap = listOf(
                                            Pair(5, star5),
                                            Pair(4, star4),
                                            Pair(3, star3),
                                            Pair(2, star2),
                                            Pair(1, star1)
                                        )

                                        barsMap.forEach { dataPair ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "${dataPair.first}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF374151), modifier = Modifier.width(10.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_rating_star),
                                                    contentDescription = "Star Indicator",
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))

                                                val progressPercentage = dataPair.second.toFloat() / totalValue
                                                LinearProgressIndicator(
                                                    progress = progressPercentage.coerceIn(0f, 1f),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(5.dp)
                                                        .clip(RoundedCornerShape(3.dp)),
                                                    color = Color(0xFF1F2937),
                                                    trackColor = Color(0xFFF3F4F6)
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = "${dataPair.second}x", fontSize = 11.sp, color = Color(0xFF9CA3AF), modifier = Modifier.width(20.dp), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                                            }
                                        }
                                    }
                                }

                                // ─── Insights Section (Nested Subcards) ───
                                if (recommendPercentage > 0 || complaintRate.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(20.dp))

                                    if (recommendPercentage > 0) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),                                            border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(14.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_recommend_star_hand),
                                                    contentDescription = "Recommendation Star Icon",
                                                    modifier = Modifier.size(74.dp)
                                                )

                                                Spacer(modifier = Modifier.width(14.dp))

                                                Column {
                                                    Text(
                                                        text = "$recommendPercentage%",
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF111827)
                                                    )
                                                    Text(
                                                        text = "of customers recommend this product",
                                                        fontSize = 15.sp,
                                                        color = Color(0xFF4B5563)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    if (complaintRate.isNotEmpty()) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),                                            border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(14.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_wrench_complaint), // Aapne res/drawable mein jo bhi image naam daala hai vo yahan likhein (jaise ic_wrench, low_complaint etc)
                                                    contentDescription = "Low Complaint Rate Icon",
                                                    modifier = Modifier.size(74.dp) // Premium visual look ke liye exact square size bounds
                                                )

                                                Spacer(modifier = Modifier.width(14.dp))

                                                Column {
                                                    Text(
                                                        text = complaintRate,
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF111827)
                                                    )
                                                    Text(
                                                        text = "low complaint rate",
                                                        fontSize = 15.sp,
                                                        color = Color(0xFF4B5563)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

// ─────────────────────────────────────────────────────────────────────────────
// CARD 7: 💬 CUSTOMER VERIFIED REVIEWS (WITH READ ALL & WRITE REVIEW BOTTOM SHEET)
// ─────────────────────────────────────────────────────────────────────────────
            item {
                val reviewObj = (reviewDataResponse as? com.manjeet.ubuyapplication.model.ProductReviewData)
                    ?: (responseWrapper as? com.manjeet.ubuyapplication.model.OtherDetail)?.data
                    ?: (responseWrapper as? com.manjeet.ubuyapplication.model.ProductReviewData)

                var verifiedReviewsList = reviewObj?.reviewData ?: emptyList<com.manjeet.ubuyapplication.model.ReviewData>()

                // ... (Aapka existing fallback JSON parsing code start)
                if (verifiedReviewsList.isEmpty() && responseWrapper != null) {
                    try {
                        val gson = com.google.gson.Gson()
                        val rawJson = gson.toJson(responseWrapper)
                        val jsonObject = org.json.JSONObject(rawJson)
                        val rootNode = if (jsonObject.has("data")) jsonObject.optJSONObject("data") else jsonObject
                        val reviewArray = rootNode?.optJSONArray("review_data")

                        if (reviewArray != null) {
                            val tempReviews = mutableListOf<com.manjeet.ubuyapplication.model.ReviewData>()
                            for (i in 0 until reviewArray.length()) {
                                val item = reviewArray.getJSONObject(i)
                                tempReviews.add(
                                    com.manjeet.ubuyapplication.model.ReviewData(
                                        nickname = item.optString("nickname"),
                                        country = item.optString("country"),
                                        createdAt = item.optString("created_at"),
                                        rating = item.optInt("rating", 5),
                                        verifiedPurchase = item.optBoolean("verified_purchase", true),
                                        title = item.optString("title"),
                                        detail = item.optString("detail")
                                    )
                                )
                            }
                            verifiedReviewsList = tempReviews
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                // ... (Aapka existing fallback JSON parsing code end)

                // 🌟 STATE CONTROL FOR BOTTOM SHEET DETECT WRITER
                var showWriteReviewSheet by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

                if (verifiedReviewsList.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // Main Review White Box Container Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                        ) {
                            Column {
                                // Sirf top 3 reviews ko show karne ke liye slice kiya
                                val displayedReviews = verifiedReviewsList.take(3)
                                displayedReviews.forEachIndexed { index, reviewItem ->
                                    VerifiedReviewRowItem(reviewItem = reviewItem)

                                    if (index < displayedReviews.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            thickness = 1.dp,
                                            color = Color(0xFFF3F4F6)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 🌟 BUTTON 1: READ ALL REVIEWS (Visible only if size > 3)
                        if (verifiedReviewsList.size > 3) {
                            OutlinedButton(
                                onClick = { /* Handle Navigate to all reviews page */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, Color(0xFF78350F)), // Brownish border color matching SS
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF78350F))
                            ) {
                                Text(
                                    text = "Read all reviews",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // 🌟 BUTTON 2: WRITE A REVIEW (Always Visible)
                        Button(
                            onClick = { showWriteReviewSheet = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF064E3B)) // Deep Green matching SS
                        ) {
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                                    contentDescription = "Edit Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Write a review",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // 🌟 TRIGGER THE PREMIUM BOTTOM SHEET BLOCK
                if (showWriteReviewSheet) {
                    WriteReviewBottomSheet(
                        onDismiss = { showWriteReviewSheet = false },
                        onSubmit = { rating, nickname, message, anonymous ->
                            // Handle review payload submit actions here
                            showWriteReviewSheet = false
                        }
                    )
                }
            }
        } // END LazyColumn


    }



    if (showWarrantyBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showWarrantyBottomSheet = false },
            sheetState = sheetState,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = Color.White
        ) {
            UCareWarrantyBottomSheetContent(
                previouslySelectedPlan = selectedWarrantyPlan,
                onCloseClick = { showWarrantyBottomSheet = false },
                onPlanSelected = { plan ->
                    selectedWarrantyPlan = plan
                    showWarrantyBottomSheet = false
                }
            )
        }
    }
} // END ProductDetailScreen



// ─────────────────────────────────────────────────────────────────────────────
//  Product Detail Route
// ─────────────────────────────────────────────────────────────────────────────


@Composable
fun ProductDetailRoute(
    productId: String,
    productsList: List<com.manjeet.ubuyapplication.model.Products> = emptyList(),
    productViewModel: ProductDetailViewModel,
    navController: androidx.navigation.NavHostController,
    onBackClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    // ✅ ViewModel ke exact signature ke sath align kiya (productsList ko bhi pass kar diya backup ke liye)
    LaunchedEffect(productId) {
        productViewModel.fetchProductDetailsFromList(
            context = context,
            productsList = productsList,
            productId = productId
        )
    }

    // Lifecycle aware state collection
    val state by productViewModel.uiState.collectAsStateWithLifecycle()

    when (val activeState = state) {
        is ProductDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF134E35))
            }
        }
        is ProductDetailUiState.Success -> {
            val realWrapper = activeState.responseWrapper

            // ✅ CRITICAL FIX: Cast karne ki zarurat hi nahi hai, kyunki data pehle se hi ProductDetailData hai!
            val productDetailData = realWrapper.data ?: com.manjeet.ubuyapplication.model.ProductDetailData()

            // Safe fallback initialization for UI support if needed
            val baseProduct = Product(
                name = productDetailData.productTitle ?: "No Name",
                price = productDetailData.price.toString(),
                image = R.drawable.img_4,
                category = productDetailData.categoryName ?: "Electronics",
                brand = productDetailData.brand ?: "Generic"
            )

            ProductDetailScreen(
                productData = productDetailData, // ✅ FIXED: Directly passing the clean synchronized data object
                product = baseProduct,
                navController = navController,
                sectionsState = activeState.sectionsState,
                responseWrapper = realWrapper,

                modifyDataResponse = activeState.modifyDataResponse,
                reviewDataResponse = activeState.reviewDataResponse,

                // 📢 CLICK DISPATCHER REGISTERED ON VIEWMODEL FLOWS
                onVariationSelected = { heading, optionId ->
                    productViewModel.updateSelectedVariationOption(heading, optionId)
                },

                onBackClick = onBackClick,
                onAddToCartClick = { },
                onAddVehicleClick = { },
                onViewBundleItemClick = { itemId -> },
                onAddWarrantyClick = { }
            )
        }
        is ProductDetailUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = activeState.message,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Composable
fun ProductFaqAccordionItem(question: String, answer: String) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable { isExpanded = !isExpanded }
            .background(if (isExpanded) Color(0xFFF9FAFB) else Color.White)
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = question, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Icon(imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = answer, fontSize = 13.sp, color = Color(0xFF4B5563))
        }
    }
}

@Composable
fun VerifiedReviewRowItem(reviewItem: com.manjeet.ubuyapplication.model.ReviewData) {
    // State to toggle expanded text layout
    var isExpanded by remember { mutableStateOf(false) }

    // State to monitor if text exceeds our threshold line limits
    var isLongText by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // ─── Profile Header Row ───
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (reviewItem.nickname ?: "U").take(1).uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B5563),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reviewItem.nickname ?: "Anonymous",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    // 🔥 Individual reviews ke stars ke beech ka space tight karne ke liye spacedBy lagaya
                    horizontalArrangement = Arrangement.spacedBy(-8.dp)
                ) {
                    val reviewStars = reviewItem.rating ?: 5
                    for (starIdx in 0 until 5) {
                        // 🔥 IMAGE TOGGLE FOR INDIVIDUAL USER REVIEWS
                        Image(
                            painter = painterResource(
                                id = if (starIdx < reviewStars) R.drawable.ic_rating_star else R.drawable.ic_rating_star_empty
                            ),
                            contentDescription = "User Review Star",
                            modifier = Modifier.size(24.dp) // Screenshot ke standard design ke mutabik individual review stars thode chote (14.dp) premium lagte hain
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${reviewItem.country ?: ""}  ${reviewItem.createdAt ?: ""}",
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Verified purchase ",
                        fontSize = 11.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "✓",
                        fontSize = 11.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ─── Review Content Text with Dynamic Layout Listener ───
        val fullText = buildString {
            if (!reviewItem.title.isNullOrEmpty()) {
                append(reviewItem.title)
                append(". ")
            }
            append(reviewItem.detail ?: "")
        }

        Text(
            text = fullText,
            fontSize = 13.sp,
            color = Color(0xFF4B5563),
            lineHeight = 18.sp,
            // Agat expand nahi hua hai toh max 3 lines tak truncate (cut) karega
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                // Check if the actual text content line size is greater than 3
                if (textLayoutResult.lineCount > 3 || textLayoutResult.didOverflowHeight) {
                    isLongText = true
                }
            }
        )

        // ─── Expandable Arrow Selector Button (VISIBLE ONLY IF TEXT IS LONG) ───
        if (isLongText) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Show less " else "Full review ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309)
                )
                Text(
                    text = if (isExpanded) "∧" else "∨",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309)
                )
            }
        }
    }
}


//===========================Review bottom sheet==================================//

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, nickname: String, message: String, isAnonymous: Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // User Form Input States
    var selectedRating by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    var nicknameText by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var experienceText by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var isAnonymousChecked by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header Row Layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Close,
                        contentDescription = "Close Sheet",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(0.4f))
                Text(
                    text = "Write your review",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(0.6f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🌟 1. Star Rating Box Component Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                border = BorderStroke(1.dp, Color(0xFFF3F4F6))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your rating *",
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        for (starIndex in 1..5) {
                            IconButton(
                                onClick = { selectedRating = starIndex },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (starIndex <= selectedRating)
                                        androidx.compose.material.icons.Icons.Default.Star
                                    else
                                        androidx.compose.material.icons.Icons.Default.Star, // StarBorder usage fallback
                                    contentDescription = "Star $starIndex",
                                    tint = if (starIndex <= selectedRating) Color(0xFFFBBF24) else Color(0xFFE5E7EB),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🌟 2. Nickname Input Field
            Text("Nickname *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = nicknameText,
                onValueChange = { nicknameText = it },
                placeholder = { Text("Enter nickname", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF064E3B),
                    unfocusedBorderColor = Color(0xFFD1D5DB)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🌟 3. Experience Field Text Area
            Text("Your experience with the product *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = experienceText,
                onValueChange = { experienceText = it },
                placeholder = { Text("Enter message", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF064E3B),
                    unfocusedBorderColor = Color(0xFFD1D5DB)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🌟 4. Upload Photos Placeholder Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD1D5DB)) // Dashed effect pattern matching layout
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Share, // ImageIcon container proxy
                        contentDescription = "Upload Icon",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload photos", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("JPEG, PNG, max. 10 MB", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🌟 5. Anonymous Review Section Row Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Anonymous review", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Personal details hidden in the review", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(
                    checked = isAnonymousChecked,
                    onCheckedChange = { isAnonymousChecked = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF064E3B)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🌟 6. Bottom Action Footer Action Sheet Row Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(0.35f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0xFF78350F)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF78350F))
                ) {
                    Text("Cancel", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { onSubmit(selectedRating, nicknameText, experienceText, isAnonymousChecked) },
                    modifier = Modifier
                        .weight(0.65f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF064E3B)),
                    enabled = selectedRating > 0 && nicknameText.isNotBlank() && experienceText.isNotBlank()
                ) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Check,
                            contentDescription = "Submit Done",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Submit review", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}