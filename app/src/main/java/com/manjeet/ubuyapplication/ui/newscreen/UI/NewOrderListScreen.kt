package com.manjeet.ubuyapplication.ui.newscreen.UI

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.manjeet.ubuyapplication.Order

@Composable
fun NewOrderListScreen(
    ordersList: List<Order>,
    onOrderClick: (Order) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF1F5))
    ) {
        // --- SEARCH BAR & FILTER BADGES
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search for an item", color = Color.Gray, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F3F6),
                        unfocusedContainerColor = Color(0xFFF1F3F6),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterBadge(icon = Icons.Default.SwapVert, text = "Sort by")
                FilterBadge(icon = Icons.Default.FilterList, text = "Filter")
                FilterBadge(icon = Icons.Default.CalendarMonth, text = "Date range")
            }
        }

        // --- ORDER LIST RENDERING ---
        if (ordersList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No orders found.", color = Color.Gray, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(ordersList) { order ->
                    OrderCard(
                        order = order,
                        onClick = { onOrderClick(order) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = Modifier.clickable { }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.DarkGray)
            Text(text = text, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
    }
}

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) { //  Type: Order
    val currentStatus = if (!order.order_status_en.isNullOrEmpty()) order.order_status_en else order.order_status
    val (statusBg, statusColor) = when {
        currentStatus.equals("Delivered", ignoreCase = true) || currentStatus.equals("complete", ignoreCase = true) -> Color(0xFFEDF2F7) to Color(0xFF2D3748)
        currentStatus.equals("Packing", ignoreCase = true) || currentStatus.equals("processing", ignoreCase = true) -> Color(0xFFE6FFFA) to Color(0xFF319795)
        currentStatus.equals("Cancelled", ignoreCase = true) || currentStatus.equals("closed", ignoreCase = true) -> Color(0xFFFEFCBF) to Color(0xFFB7791F)
        else -> Color(0xFFE6FFFA) to Color(0xFF319795)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(order.order_date ?: "", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                    Text(" #${order.order_increment_id ?: ""}", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp))
                }
                Surface(color = statusBg, shape = RoundedCornerShape(12.dp)) {
                    Text(text = currentStatus ?: "", color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                val itemsList = order.items ?: listOf()
                val totalItems = itemsList.size
                val maxVisibleImages = 3
                val visibleItems = itemsList.take(maxVisibleImages)

                visibleItems.forEach { item ->
                    Column(modifier = Modifier.width(80.dp).padding(end = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(
                            modifier = Modifier.size(65.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            val finalImageUrl = when {
                                item.image.isNullOrEmpty() -> ""
                                item.image!!.startsWith("http") -> item.image!!
                                else -> "https://images.ubuy.com" + item.image
                            }
                            AsyncImage(model = finalImageUrl, contentDescription = item.name, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize().padding(4.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.name ?: "Product", fontSize = 11.sp, fontWeight = FontWeight.Normal, color = Color(0xFF374151), maxLines = 1, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }
                }

                if (totalItems > maxVisibleImages) {
                    Card(modifier = Modifier.size(65.dp).align(Alignment.Top), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color(0xFFE5E7EB)), colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "+${totalItems - maxVisibleImages}", color = Color(0xFF374151), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = order.order_total ?: "", modifier = Modifier.align(Alignment.End), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.Black)
        }
    }
}


