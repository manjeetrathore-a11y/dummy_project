package com.manjeet.ubuyapplication.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.StrokeCap

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    // Interactive State Management For Selection Items
    var selectedColorIndex by remember { mutableStateOf(0) }
    var selectedSizeIndex by remember { mutableStateOf(0) }
    var selectedImageRes by remember { mutableStateOf(product.image) }

    val colorVariants = listOf("Black", "Clear", "Cosmic Orange", "Deep Blue")
    val sizeVariants = listOf("iPhone 17 ", "iPhone 14", "iPhone 13", "iPhone 12 ")

    val galleryImages = listOf(product.image, product.image, product.image, product.image)

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
                        text = product.category,
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
                        shape = RoundedCornerShape(18.dp),                         colors = ButtonDefaults.buttonColors(
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

            // CARD 1: COMBINED INFO HEADER & IMAGE CONTAINER
            item {
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
                                Text(text = product.brand.take(1), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = product.brand, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                            Spacer(modifier = Modifier.weight(1f))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                                }
                                Text(" 4.9 (145)", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // ─── ROW 2: PRODUCT TITLE ─────────────────────────────────────────
                        Text(text = product.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937), lineHeight = 22.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        // ─── ROW 3: GREEN PRODUCT SHEET LABEL ──────────────────────────────
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFF10B981),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("A", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Product Sheet", color = Color(0xFF10B981), fontSize = 13.sp, fontWeight = FontWeight.Bold)
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

                        // ─── ROW 5: ACTIONS BUTTONS & US STORE ALIGNMENT ───────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Heart Action Button
                            Surface(
                                onClick = {},
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.FavoriteBorder, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Share Action Button
                            Surface(
                                onClick = {},
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Share, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // US Store Text Right Side Shift
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("US Store ", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                                Image(
                                    painter = painterResource(id = R.drawable.ic_usa_flag),
                                    contentDescription = "US Store Flag",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ─── ROW 6: MAIN IMAGE VIEWER WITH TRENDING BADGE ─────────────────
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = selectedImageRes),
                                contentDescription = "Main product preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )

                            // Trending Badge Overlay (As shown on top left inside picture)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .background(Color(0xFFDC2626), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Trending", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // ─── ROW 7: THUMBNAILS GALLERY ──────────────────────────────────────

                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(54.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Image(

                                    painter = painterResource(id = selectedImageRes),
                                    contentDescription = "Single thumbnail preview",
                                    modifier = Modifier.size(44.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }

                    }
                }
            }

            // ─────────────────────────────────────────────────────────────────
            //  UNIFIED MASTER CONTAINER FOR COLOR & SIZE SELECTION (WITHOUT INNER BOXES)
            // ─────────────────────────────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
//                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        //  SECTION 1: COLOR SELECTION MATRIX
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("Color", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.height(10.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(colorVariants.size) { index ->
                                    val isColorSelected = selectedColorIndex == index
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.clickable { selectedColorIndex = index }
                                    ) {
                                        Card(
                                            modifier = Modifier.size(64.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(width = if (isColorSelected) 2.dp else 1.dp, color = if (isColorSelected) Color(0xFFFFC107) else Color(0xFFE5E7EB)),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
                                        ) {
                                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                Image(painter = painterResource(id = product.image), contentDescription = null, modifier = Modifier.size(50.dp))
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = colorVariants[index], fontSize = 11.sp, fontWeight = if (isColorSelected) FontWeight.Bold else FontWeight.Normal, color = if (isColorSelected) Color.Black else Color.Gray)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // SECTION 2: SIZE SELECTION GRID
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Size", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                Text("Size Chart", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB87333), modifier = Modifier.clickable { })
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                sizeVariants.chunked(3).forEach { pairItems ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        pairItems.forEach { sizeLabel ->
                                            val currentIdx = sizeVariants.indexOf(sizeLabel)
                                            val isSizeSelected = selectedSizeIndex == currentIdx
                                            Surface(
                                                modifier = Modifier.weight(1f).height(40.dp).clickable { selectedSizeIndex = currentIdx },
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (isSizeSelected) Color.White else Color(0xFFF3F4F6),
                                                border = BorderStroke(width = 1.dp, color = if (isSizeSelected) Color(0xFFFFC107) else Color.Transparent)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(text = sizeLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                                                }
                                            }
                                        }
                                        if (pairItems.size < 3) {
                                            Spacer(modifier = Modifier.weight((3 - pairItems.size).toFloat()))
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }


            // ─────────────────────────────────────────────────────────────────
            // HE SINGLE UNIFIED MASTER CONTAINER FOR CARD 4 AND EVERYTHING AFTER IT
            // ─────────────────────────────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        //  INNER BOX 4: EXTENDED DEAL & STOCK METRICS
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF2E7D32), CircleShape))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("In stock", color = Color(0xFF2E7D32), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                Text("# 176625820", color = Color.Gray, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Total Bought : 16.8k", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = product.price, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("You Save : 20%", color = Color(0xFF2E7D32), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = Color(0xFFD32F2F), shape = RoundedCornerShape(4.dp)) {
                                    Text("Limited Time Deal", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Only 7 items left in stock.", color = Color(0xFFD32F2F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "This item is not Fulfilled by Ubuy. Delivery takes 10+ days minimum. If delivery issues arise, the item may be cancelled and refunded.", color = Color(0xFF2E7D32), fontSize = 12.sp, lineHeight = 16.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(modifier = Modifier.fillMaxWidth(), color = Color(0xFFFAFAFA), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color(0xFFEEEEEE))) {
                                Text(text = "Excluding VAT: VAT will be calculated on the checkout page.", fontSize = 12.sp, color = Color.DarkGray, modifier = Modifier.padding(12.dp))
                            }
                        }

                        //  INNER BOX 5: SECURED TRANSACTIONS & PAYMENT TRUST BADGES
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("Secured Transaction", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val paymentMethods = listOf(R.drawable.ic_apple_pay, R.drawable.ic_mastercard, R.drawable.ic_applepay, R.drawable.ic_googlepay)
                                paymentMethods.forEach { drawableResId ->
                                    Surface(modifier = Modifier.weight(1f).height(36.dp), color = Color(0xFFF9FAFB), shape = RoundedCornerShape(6.dp), border = BorderStroke(1.dp, Color(0xFFE5E7EB))) {
                                        Box(modifier = Modifier.fillMaxSize().padding(6.dp), contentAlignment = Alignment.Center) {
                                            Image(painter = painterResource(id = drawableResId), contentDescription = "Payment Method", modifier = Modifier.fillMaxSize())
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color(0xFFE5E7EB))) {
                                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Surface(color = Color(0xFFFFA3C4), shape = RoundedCornerShape(10.dp)) {
                                        Text(text = "Klarna.", fontWeight = FontWeight.Black, color = Color.Black, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                                    }
                                    Text(text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = Color(0xFF374151), fontSize = 13.sp, fontWeight = FontWeight.Normal)) { append("Buy now, pay in 30 days. ") }
                                        withStyle(style = SpanStyle(color = Color(0xFF1F2937), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, textDecoration = TextDecoration.Underline)) { append("Learn more") }
                                    })
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(BorderStroke(2.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
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

                        //   INNER BOX 6: VOLTAGE TRANSFORMER WARNING BLOCK
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
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
                                            modifier = Modifier
                                                .size(40.dp),
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

                        // INNER BOX 7: VEHICLE FILTER UTILITY CARD
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Add your vehicle", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Attention: We strongly recommend to fill the vehicle information before adding the product in your cart to get the perfect match. In the event of skipping this step, the product will be excluded from our return policy.", fontSize = 12.sp, color = Color.Gray, lineHeight = 16.sp)
                            }
                        }

                        //  INNER BOX 8: BUNDLE ITEMS STACK
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
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
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
                                            onClick = {},
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
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
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
                                            onClick = {},
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

                        //  INNER BOX 9: SHIPPING FLOW & DETAILED COST BREAKDOWN
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

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFF3F4F6), border = BorderStroke(1.dp, Color(0xFFE5E7EB))) {
                                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Text("🇺🇸 US", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Text(" ------------- ✈ ------------- ", color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFF3F4F6), border = BorderStroke(1.dp, Color(0xFFE5E7EB))) {
                                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Text("🇬🇧 UK", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
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
                        }

                        //  INNER BOX 10: UNLOCK FAST SHIPPING
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
                                    modifier = Modifier
                                        .size(52.dp)
                                        .padding(end = 12.dp),
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
                                        progress = { 0.6f },
                                        modifier = Modifier.fillMaxWidth().height(6.dp),
                                        color = Color(0xFFD97706),
                                        trackColor = Color(0xFFE5E7EB),
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                            }
                        }

                        // INNER BOX 11: U-CARE WARRANTY PLAN
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_warranty),
                                        contentDescription = "U-Care Warranty Icon",
                                        modifier = Modifier
                                            .size(52.dp)
                                            .padding(end = 12.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                    Column {
                                        Text(text = "U-Care Warranty", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "From basic repairs to full 3-year coverage with accidental, spill, and fire protection options.", fontSize = 13.sp, color = Color(0xFF4B5563), lineHeight = 18.sp)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF3F4F6))
                                        .clickable { }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "Add a U-Care Plan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF4B5563))
                                    }
                                }
                            }
                        }

                    }
                }
            }

            // Bottom space buffer
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}













// ─────────────────────────────────────────────────────────────────────────────
// ✅ TOP LEVEL PREVIEW
// ─────────────────────────────────────────────────────────────────────────────
@Preview(name = "Product Detail Screen Active Frame", showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    MaterialTheme {
        val previewProduct = Product(
            name = "Roborock Qrevo Series Robot Vacuum and Mop, 8000Pa Suction, Upgraded Auto Mop Washing Heavy Variant Base Cover",
            price = "£29.99",
            image = R.drawable.img_4,
            category = "Electronics",
            brand = "Roborock"
        )
        ProductDetailScreen(
            product = previewProduct,
            onBackClick = {},
            onAddToCartClick = {}
        )
    }
}