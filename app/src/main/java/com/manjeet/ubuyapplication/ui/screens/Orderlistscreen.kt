//package com.manjeet.ubuyapplication.ui.screens
//
//import android.util.Log
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import coil.compose.AsyncImage
//import com.manjeet.ubuyapplication.Orderss
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OrderListScreen(
//    ordersList: List<Orderss>,
//    onOrderClick: (Orderss) -> Unit,
//    onBack: () -> Unit,
//    onTrackingClick: () -> Unit   // make sure this is in the parameter
//) {
//    var clickedOrder by remember { mutableStateOf<Orderss?>(null) }
//
//    Scaffold { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color(0xFFF3F4F6))
//        ) {
//            when {
//                clickedOrder != null -> {
//                    OrderDetailPage(
//                        order = clickedOrder!!,
//                        onTrackingClick = { onTrackingClick() },
//                        onBack = { clickedOrder = null },
//                        navController = TODO()
//                    )
//                }
//
//                // STEP 2: Agar detail band hai, toh normal list pipeline chalao
//                else -> {
//                    Column(modifier = Modifier.fillMaxSize()) {
//                        // --- 1. TOP BAR ---
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .background(Color.White)
//                                .padding(horizontal = 8.dp, vertical = 8.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            IconButton(onClick = onBack) {
//                                Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(20.dp))
//                            }
//                            Text(
//                                text = "Orders",
//                                fontSize = 18.sp,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.weight(1f),
//                                textAlign = TextAlign.Center
//                            )
//                            IconButton(onClick = { /* Notification action */ }) {
//                                Icon(Icons.Default.NotificationsNone, contentDescription = null)
//                            }
//                        }
//
//                        // --- 2. FILTER ROW ---
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .background(Color.White)
//                                .padding(horizontal = 16.dp, vertical = 12.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Row(
//                                modifier = Modifier.weight(1f),
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                // Apne Custom Pills functions ko use karein
//                                Text("Sort / Filter Rows", color = Color.Gray, fontSize = 12.sp)
//                            }
//                        }
//
//                        // --- 3. SCROLLABLE LIST COLUMN ---
//                        LazyColumn(
//                            modifier = Modifier.fillMaxSize(),
//                            contentPadding = PaddingValues(16.dp),
//                            verticalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//                            items(ordersList) { order ->
//                                OrderCard(
//                                    order = order,
//                                    onClick = {
//                                        onOrderClick(order) // Inform the top system state
//                                        clickedOrder = order // UI locally shifts to internal details view
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun OrderCard(order: Orderss, onClick: () -> Unit) {
//    val currentStatus = if (!order.order_status_en.isNullOrEmpty()) order.order_status_en else order.order_status
//    val (statusBg, statusColor) = when {
//        currentStatus.equals("Delivered", ignoreCase = true) || currentStatus.equals("complete", ignoreCase = true) -> {
//            Color(0xFFF3F4F6) to Color(0xFF374151)
//        }
//        currentStatus.equals("Packing", ignoreCase = true) || currentStatus.equals("processing", ignoreCase = true) -> {
//            Color(0xFFE8F5E9) to Color(0xFF2E7D32)
//        }
//        currentStatus.equals("Cancelled", ignoreCase = true) || currentStatus.equals("closed", ignoreCase = true) -> {
//            Color(0xFFFFEBEE) to Color(0xFFD32F2F)
//        }
//        else -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
//    }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() },
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(2.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row {
//                    Text(order.order_date, fontWeight = FontWeight.Bold, fontSize = 14.sp)
//                    Text(" #${order.order_increment_id}", color = Color.Gray, fontSize = 14.sp)
//                }
//
//                Surface(
//                    color = statusBg,
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                ) {
//                    Text(
//                        text = currentStatus,
//                        color = statusColor,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(vertical = 8.dp)
//            ) {
//                val totalItems = order.items.size
//                val maxVisibleImages = 4
//                val visibleItems = order.items.take(maxVisibleImages)
//
//                visibleItems.forEach { item ->
//                    Card(
//                        modifier = Modifier
//                            .size(60.dp)
//                            .padding(end = 8.dp),
//                        shape = RoundedCornerShape(8.dp),
//                        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
//                        colors = CardDefaults.cardColors(containerColor = Color.White)
//                    ) {
//                        AsyncImage(
//                            model = item.image,
//                            contentDescription = item.name,
//                            contentScale = ContentScale.Fit,
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    }
//                }
//
//                if (totalItems > maxVisibleImages) {
//                    val remainingCount = totalItems - maxVisibleImages
//                    Card(
//                        modifier = Modifier.size(60.dp),
//                        shape = RoundedCornerShape(8.dp),
//                        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
//                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
//                    ) {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = "+$remainingCount",
//                                color = Color(0xFF374151),
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = order.order_total,
//                modifier = Modifier.align(Alignment.End),
//                fontWeight = FontWeight.ExtraBold,
//                fontSize = 16.sp,
//                color = Color.Black
//            )
//        }
//    }
//}
//
//// --- DETAIL PAGE CONTAINER ---
//@Composable
//fun OrderDetailPage(
//    navController: NavHostController,
//    order: Orderss,
//    onBack: () -> Unit,
//    onTrackingClick: (String) -> Unit
//
//
//)
//{
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF3F4F6))
//            .verticalScroll(rememberScrollState())
//    ) {
//        // --- Top App Navigation Bar ---
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.White)
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = onBack) {
//                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
//            }
//            Text(
//                text = "Order #${order.order_increment_id}",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.weight(1f),
//                textAlign = TextAlign.Center
//            )
//            IconButton(onClick = {}) {
//                Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications")
//            }
//        }
//
//        // --- Main Content Switcher (Checks JSON Status) ---
//        val currentStatus = if (!order.order_status_en.isNullOrEmpty()) order.order_status_en else order.order_status
//
//        when {
//            currentStatus.equals("Delivered", ignoreCase = true) ||
//                    currentStatus.equals("complete", ignoreCase = true) ||
//                    currentStatus.equals("Cancelled", ignoreCase = true) ||
//                    currentStatus.equals("closed", ignoreCase = true) -> {
//                DeliveredStatusLayout(order = order)
//            }
//
//            currentStatus.equals("Packing", ignoreCase = true) ||
//                    currentStatus.equals("processing", ignoreCase = true) -> {
//                PackingStatusLayout(
//                    navController = navController,
//                    order = order
//                )
//            }
//
//            else -> {
//                DefaultStatusLayout(order = order)
//            }
//        }
//
//        // --- Support & Invoice Bottom Actions Card ---
////        Card(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 17.dp, vertical = 8.dp)
////                .clickable {
////                    onTrackingClick()
////                },
////            colors = CardDefaults.cardColors(containerColor = Color.White),
////            shape = RoundedCornerShape(12.dp),
////            border = BorderStroke(1.dp, Color(0xFFF3F4F6))
////        ) {
////            Row(
////                modifier = Modifier
////                    .padding(16.dp)
////                    .fillMaxWidth(),
////                horizontalArrangement = Arrangement.SpaceBetween,
////                verticalAlignment = Alignment.CenterVertically
////            ) {
////                Column {
////                    Text("Shipment tracking no", fontSize = 14.sp, color = Color.Gray)
////                    Text(
////                        text = order.order_increment_id,
////                        fontSize = 16.sp,
////                        fontWeight = FontWeight.Bold,
////                        color = Color(0xFFB07D3E),
////                        textDecoration = TextDecoration.Underline
////                    )
////                }
////            }
////        }
//
//        // 🚀 FIX: Brackets sahi karke items ko list column me wrap kiya hai
//        Column(modifier = Modifier.padding(horizontal = 17.dp)) {
//            DetailActionItem(
//                icon = Icons.Default.Settings,
//                title = "Have a problem with this order?",
//                subtitle = "Contact support via email"
//            )
//            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
//            DetailActionItem(
//                icon = Icons.Default.Download,
//                title = "Download invoice",
//                subtitle = "Get a .pdf invoice for this order"
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//    }
//}
