package com.manjeet.ubuyapplication.ui.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.R
import com.manjeet.ubuyapplication.model.Product
import com.manjeet.ubuyapplication.utils.SortOption
import java.text.DecimalFormat


// BRAND PAGE CONTENT WITH TRANSITION
@Composable
fun BrandPageContent(
    brandName: String,
    products: List<Product>,
    isTransitioning: Boolean,
    currentSort: SortOption,
    onSortClick: () -> Unit
) {
    val sortedProducts = when (currentSort) {
        SortOption.RELEVANCE -> products
        SortOption.PRICE_LOW_HIGH -> products.sortedBy {
            it.price.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
        }
        SortOption.PRICE_HIGH_LOW -> products.sortedByDescending {
            it.price.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ResultsHeader(
                totalCount = products.size,
                query = brandName,
                currentSort = currentSort,
                onSortClick = onSortClick,
                onFilterClick = { }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                items(sortedProducts) { product ->
                    SearchResultItem(product)
                }

                item {
                    if (products.isNotEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }

        // TRANSITION OVERLAY (VISIBLE REFRESH)
        if (isTransitioning) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        strokeWidth = 4.dp,
                        color = Color(0xFFB87333)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "Loading $brandName products...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


// RESULTS HEADER
@Composable
fun ResultsHeader(
    totalCount: Int,
    query: String,
    currentSort: SortOption,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    // 1. PERFORMANCE FIX: Formatter is now cached in memory
    val formatter = remember {
        DecimalFormat("#,###").apply {
            decimalFormatSymbols = decimalFormatSymbols.apply {
                groupingSeparator = ' '
            }
        }
    }

    val formattedCount = remember(totalCount) { formatter.format(totalCount) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White) // Ensures no transparency lag
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 2. RESULTS SUMMARY
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formattedCount,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = " results for ",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = "\"$query\"",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = TextStyle(fontStyle = FontStyle.Italic),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. INTERACTIVE BUTTONS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ActionButtonPill(
                icon = Icons.Default.SwapVert,
                text = "Sort: ${currentSort.displayName}",
                onClick = onSortClick
            )

            ActionButtonPill(
                icon = Icons.Default.FilterList,
                text = "Filter",
                onClick = onFilterClick
            )
        }

        // 4. CLEAN SEPARATOR
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            color = Color(0xFFF2F3F5),
            thickness = 1.dp
        )
    }
}

@Composable
fun ActionButtonPill(icon: ImageVector, text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFB87333),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun SearchResultItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(100.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = product.image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
                    )
                    Surface(
                        color = Color(0xFFFFC107),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            "32% off",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    modifier = Modifier.height(28.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val swatchColors = listOf(Color(0xFF23293E), Color(0xFF4A608A), Color(0xFFF07D39))
                        Row {
                            swatchColors.forEachIndexed { index, color ->
                                Surface(
                                    shape = CircleShape,
                                    color = color,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .offset(x = if (index > 0) (-(4 * index)).dp else 0.dp)
                                        .border(1.dp, Color.White, CircleShape)
                                ) {}
                            }
                        }

                        Text(
                            text = "+4",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row {
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(" 4.5 (24)", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFF5D4037),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(text = "Ubuy Store ", color = Color.Black, fontSize = 12.sp)

                Spacer(Modifier.height(6.dp))

                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = Color(0xFF42A5F5),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        " Globle  Store",
                        fontSize = 11.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = product.price,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "£34.99",
                        fontSize = 14.sp,
                        color = Color.Red,
                        style = androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.LineThrough)
                    )
                }
            }
        }
    }
}

@Composable
fun BrandLogoCircle(brandName: String, brandLogo: Int, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(120.dp)
            .clickable { onClick() }
            .padding(2.dp),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = brandLogo),
                    contentDescription = brandName,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(text = brandName, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            }
        }
    }
}