package com.manjeet.ubuyapplication.ui.newscreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.manjeet.ubuyapplication.R
//import com.manjeet.ubuyapplication.Item
import com.manjeet.ubuyapplication.model.Order
//import com.manjeet.ubuyapplication.OrderRepository
import com.manjeet.ubuyapplication.model.Item
import com.manjeet.ubuyapplication.model.OrderRepository


// ─────────────────────────────────────────────────────────────────────────────
// DATA CLASS
// ─────────────────────────────────────────────────────────────────────────────
data class ShipmentBottomSheetData(
    val estimatedDelivery: String,
    val trackingNumber: String,
    val orderStatusEn: String?,
    val items: List<Item>,
    val completedDots: Int
)

// ─────────────────────────────────────────────────────────────────────────────
// HARDCODED TIMELINE STEPS
// ─────────────────────────────────────────────────────────────────────────────
data class TimelineStep(
    val label: String,
    val subtitle: String,
    val matchingStatuses: List<String>
)

val HARDCODED_TIMELINE_STEPS = listOf(
    TimelineStep("Pending", "Order received", listOf("pending", "pending_payment", "new")),
    TimelineStep("Confirmed", "Payment verified and order accepted", listOf("confirmed")),
    TimelineStep("Processing", "The store is preparing the items", listOf("processing", "preparing", "packing", "holded")),
    TimelineStep("Shipped", "Order has left the warehouse and is on its way", listOf("shipped", "complete_shipped")),
    TimelineStep("Out for delivery", "Courier is delivering your package", listOf("out_for_delivery", "out for delivery")),
    TimelineStep("Out for delivery", "Courier is delivering your package", listOf("out_for_delivery_2")),
    TimelineStep("Delivered", "Your order has been delivered", listOf("delivered", "complete"))
)

// ─────────────────────────────────────────────────────────────────────────────
// HELPER — completed steps from JSON status
// ─────────────────────────────────────────────────────────────────────────────
fun resolveCompletedStepCount(orderStatusEn: String?): Int {
    if (orderStatusEn == null) return 1
    val s = orderStatusEn.trim().lowercase()
    for (i in HARDCODED_TIMELINE_STEPS.indices.reversed()) {
        if (HARDCODED_TIMELINE_STEPS[i].matchingStatuses.any { it.lowercase() == s }) return i + 1
    }
    return 1
}

// ==========================================================================
// CAPSULE PROGRESS BAR — reusable, dot count driven by completedDots
//============================================================================
@Composable
fun ShipmentCapsuleBar(
    completedDots: Int,
    leftDrawable: Int,
    isLeftCircle: Boolean = true
) {
    val totalDots = 6
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 6.dp)
    ) {
        Image(
            painter = painterResource(leftDrawable),
            contentDescription = null,
            modifier = if (isLeftCircle)
                Modifier.size(18.dp).clip(CircleShape)
            else
                Modifier.size(14.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFF3F4F6)),
            contentAlignment = Alignment.CenterStart
        ) {
            // green fill width fraction
            val fillFraction = (completedDots.coerceIn(0, totalDots).toFloat() / totalDots)
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(fillFraction).background(Color(0xFF10B981)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(totalDots) { index ->
                    val dotCompleted = index < completedDots
                    val isActive = index == completedDots - 1
                    when {
                        isActive -> Box(
                            modifier = Modifier
                                .padding(horizontal = if (index == 0 || index == totalDots - 1) 4.dp else 0.dp)
                                .size(10.dp)
                                .border(2.dp, Color(0xFF10B981), RoundedCornerShape(5.dp))
                                .background(Color.White, RoundedCornerShape(5.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.size(4.dp).background(Color(0xFF10B981), RoundedCornerShape(2.dp)))
                        }
                        dotCompleted -> Box(
                            modifier = Modifier
                                .padding(horizontal = if (index == 0 || index == totalDots - 1) 4.dp else 0.dp)
                                .size(4.dp)
                                .background(Color.White, RoundedCornerShape(2.dp))
                        )
                        else -> Box(
                            modifier = Modifier
                                .padding(horizontal = if (index == 0 || index == totalDots - 1) 4.dp else 0.dp)
                                .size(4.dp)
                                .background(Color(0xFF9CA3AF), RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(6.dp))
        Image(
            painter = painterResource(R.drawable.ic_uk_flag),
            contentDescription = "UK Flag",
            modifier = Modifier.size(18.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

// ========================================================================
// BOTTOM SHEET
// ========================================================================


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusBottomSheet(
    data: ShipmentBottomSheetData,
    onDismiss: () -> Unit
) {
    val isPreview = androidx.compose.ui.platform.LocalInspectionMode.current
    val completedCount = resolveCompletedStepCount(data.orderStatusEn)


    val sheetContent = @Composable {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // DRAG HANDLE SIMULATION FOR PREVIEW MODE
            if (isPreview) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp, bottom = 12.dp)
                        .width(40.dp).height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE5E7EB))
                )
            }

            // TITLE ROW
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Order Status", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Text("✕", fontSize = 16.sp, color = Color(0xFF6B7280))
                }
            }

            // DATE — in a bordered Box (from JSON order_date)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Estimated time of delivery", fontSize = 13.sp, color = Color(0xFF6B7280))
                    Text(
                        text = data.estimatedDelivery,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TIMELINE
            HARDCODED_TIMELINE_STEPS.forEachIndexed { index, step ->
                // Determine the exact step states based on completedCount
                val isDone = index < completedCount - 1
                val isCurrent = index == completedCount - 1
                val isCompleted = index < completedCount

                val isFirst = index == 0
                val isLast = index == HARDCODED_TIMELINE_STEPS.lastIndex

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        // 1. Thick Capsule Progress Line Background
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .fillMaxHeight()
                                .background(
                                    color = if (isDone || isCurrent) Color(0xFF10B981) else Color(0xFFF3F4F6),
                                    shape = when {
                                        isFirst -> RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                                        isLast -> RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                                        else -> RoundedCornerShape(0.dp)
                                    }
                                )
                        )

                        // 2. Clear Foreground Milestone Node
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .size(14.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(10.dp)
                                )
                            } else if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .border(1.5.dp, Color(0xFF10B981), CircleShape)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(Color(0xFFD1D1D1), CircleShape)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Right Text Content Column
                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp, bottom = 28.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = step.label,
                            fontSize = 14.sp,
                            fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Medium,
                            color = if (isCompleted) Color(0xFF1F2937) else Color(0xFF9CA3AF)
                        )
                        Text(
                            text = step.subtitle,
                            fontSize = 12.sp,
                            color = if (isCompleted) Color(0xFF4B5563) else Color(0xFFD1D5DB),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // TRACKING NUMBER + DHL CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Shipment tracking number",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = data.trackingNumber,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFBBF24)
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.ic_dhl),
                        contentDescription = "DHL",
                        modifier = Modifier
                            .height(28.dp)
                            .wrapContentWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // ITEMS
            data.items.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, Color(0xFFE5E7EB))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isPreview) {
                            Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE5E7EB)), contentAlignment = Alignment.Center) {
                                Text("📦", fontSize = 16.sp)
                            }
                        } else {
                            AsyncImage(
                                model = item.image,
                                contentDescription = null,
                                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF3F4F6)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name ?: "",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1F2937),
                                maxLines = 2,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🇺🇸", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "US", fontSize = 12.sp, color = Color(0xFF6B7280))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "1 pcs", fontSize = 12.sp, color = Color(0xFF6B7280))
                            }
                        }
                    }
                }
            }
        }
    }

    // CONDITIONAL WRAPPER
    if (isPreview) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White
        ) {
            sheetContent()
        }
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 4.dp)
                        .width(40.dp).height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE5E7EB))
                )
            }
        ) {
            sheetContent()
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// MAIN SCREEN
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderInputScreen(
    navController: NavHostController,
    orderId: String = "",
    onBack: () -> Unit
) {
    var orderNumberInput by remember { mutableStateOf(orderId) }
    var isErrorActive by remember { mutableStateOf(false) }
    var isSuccessActive by remember { mutableStateOf(false) }
    var selectedShipmentIndex by remember { mutableStateOf(0) }
    var foundOrderDetails by remember { mutableStateOf<Order?>(null) }
    var bottomSheetData by remember { mutableStateOf<ShipmentBottomSheetData?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val logTag = "TrackOrderEngine"
    val scrollState = rememberScrollState()

    val orderRepository: OrderRepository? = remember {
        try {
            val jsonString = context.assets.open("order_response_data.json").bufferedReader().use { it.readText() }
            Gson().fromJson(jsonString, OrderRepository::class.java)
        } catch (e: Exception) {
            Log.e(logTag, "Database verification engine fetch error", e)
            null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F6))
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // 1. HEADER
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp), tint = Color(0xFF1F2937))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Track your order", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            }

            // 2. MAIN CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 3. LOGO
                    if (androidx.compose.ui.platform.LocalInspectionMode.current) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .padding(bottom = 16.dp)
                                .background(Color(0xFFE5E7EB), shape = RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📦", fontSize = 24.sp)
                        }
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_tracking),
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .padding(bottom = 16.dp)
                        )
                    }

                    // 4. INSTRUCTIONS
                    Text(
                        text = "Enter your order number to check your order status.",
                        fontSize = 15.sp, color = Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 20.dp), lineHeight = 20.sp
                    )

                    // 5. LABEL
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Text("Order number ", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937))
                        Text("*", fontSize = 14.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 6. TEXT FIELD
                    BasicTextField(
                        value = orderNumberInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 20) {
                                orderNumberInput = input
                                if (isErrorActive) isErrorActive = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF1F2937)),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier.fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, if (isErrorActive) Color(0xFFDC2626) else Color.Transparent, RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF3F4F6))
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    if (orderNumberInput.isEmpty()) Text("Enter order number", fontSize = 14.sp, color = Color(0xFF9CA3AF))
                                    innerTextField()
                                }
                            }
                        }
                    )

                    // ERROR STATE
                    if (isErrorActive) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("We couldn't find tracking details for this order number", color = Color(0xFFDC2626), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF6B7280), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Check your order confirmation email for your order number. If problems persist, contact support", fontSize = 13.sp, color = Color(0xFF4B5563), lineHeight = 18.sp)
                            }
                        }
                    }

                    // SUCCESS LAYOUT
                    AnimatedVisibility(
                        visible = isSuccessActive && foundOrderDetails != null,
                        enter = fadeIn(tween(700)) + expandVertically(tween(700)),
                        exit = fadeOut(tween(400)) + shrinkVertically(tween(400))
                    ) {
                        val currentOrder = foundOrderDetails!!
                        val totalItemsCount = currentOrder.items.size
                        val pkg1Dots = resolveCompletedStepCount(currentOrder.order_status_en).coerceAtMost(4)
                        val pkg2Dots = resolveCompletedStepCount("processing").coerceAtMost(4)

                        Column {
                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF4B5563), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (totalItemsCount > 2) "Order ${currentOrder.order_increment_id} is arriving in two shipments"
                                        else "Order ${currentOrder.order_increment_id} is arriving in one shipment",
                                        fontSize = 13.sp, color = Color(0xFF374151), fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (totalItemsCount <= 2) {
                                // LAYOUT 1: SINGLE SHIPMENT CARD
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        selectedShipmentIndex = 0
                                        bottomSheetData = ShipmentBottomSheetData(
                                            estimatedDelivery = currentOrder.order_date,
                                            trackingNumber = currentOrder.order_increment_id,
                                            orderStatusEn = currentOrder.order_status_en,
                                            items = currentOrder.items,
                                            completedDots = pkg1Dots
                                        )
                                        showBottomSheet = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(if (selectedShipmentIndex == 0) 1.5.dp else 1.dp, if (selectedShipmentIndex == 0) Color(0xFFFBBF24) else Color(0xFFE5E7EB))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            items(currentOrder.items) { item ->
                                                AsyncImage(model = item.image, contentDescription = null, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF3F4F6)), contentScale = ContentScale.Crop)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(text = currentOrder.order_date, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("${currentOrder.order_status_en} • Shipment details fetched from system", fontSize = 12.sp, color = Color(0xFF4B5563))
                                        Spacer(modifier = Modifier.height(16.dp))
                                        ShipmentCapsuleBar(completedDots = pkg1Dots, leftDrawable = R.drawable.ic_usa_flag, isLeftCircle = true)
                                    }
                                }
                            } else {
                                // LAYOUT 2: TWO SHIPMENT CARDS
                                // Package 1
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        selectedShipmentIndex = 0
                                        bottomSheetData = ShipmentBottomSheetData(
                                            estimatedDelivery = currentOrder.order_date,
                                            trackingNumber = currentOrder.order_increment_id,
                                            orderStatusEn = currentOrder.order_status_en,
                                            items = currentOrder.items.take(2),
                                            completedDots = pkg1Dots
                                        )
                                        showBottomSheet = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(if (selectedShipmentIndex == 0) 1.5.dp else 1.dp, if (selectedShipmentIndex == 0) Color(0xFFFBBF24) else Color(0xFFE5E7EB))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            items(currentOrder.items.take(2)) { item ->
                                                AsyncImage(model = item.image, contentDescription = null, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF3F4F6)), contentScale = ContentScale.Crop)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(text = currentOrder.order_date, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("Out for delivery • Courier is delivering your package", fontSize = 12.sp, color = Color(0xFF4B5563))
                                        Spacer(modifier = Modifier.height(16.dp))
                                        ShipmentCapsuleBar(completedDots = pkg1Dots, leftDrawable = R.drawable.ic_usa_flag, isLeftCircle = true)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Package 2
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        selectedShipmentIndex = 1
                                        bottomSheetData = ShipmentBottomSheetData(
                                            estimatedDelivery = currentOrder.order_date,
                                            trackingNumber = currentOrder.order_increment_id,
                                            orderStatusEn = "processing",
                                            items = currentOrder.items.drop(2),
                                            completedDots = pkg2Dots
                                        )
                                        showBottomSheet = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(if (selectedShipmentIndex == 1) 1.5.dp else 1.dp, if (selectedShipmentIndex == 1) Color(0xFFFBBF24) else Color(0xFFE5E7EB))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            items(currentOrder.items.drop(2)) { item ->
                                                AsyncImage(model = item.image, contentDescription = null, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF3F4F6)), contentScale = ContentScale.Crop)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(text = currentOrder.order_date, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("Processing • The store is preparing the items.", fontSize = 12.sp, color = Color(0xFF4B5563))
                                        Spacer(modifier = Modifier.height(16.dp))
                                        ShipmentCapsuleBar(completedDots = pkg2Dots, leftDrawable = R.drawable.img_42, isLeftCircle = false)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 7. TRACK BUTTON
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            val currentInput = orderNumberInput.trim()


                            val ordersList = orderRepository?.data as? List<Order>
                                ?: (orderRepository?.data as? List<*>)

                            val matchedOrder = ordersList?.find { item ->
                                val order = item as? Order
                                order?.order_increment_id.equals(currentInput, ignoreCase = true)
                            } as? Order

                            if (currentInput.isEmpty() || matchedOrder == null) {
                                isErrorActive = true
                                isSuccessActive = false
                                foundOrderDetails = null
                            } else {
                                isErrorActive = false
                                isSuccessActive = true
                                foundOrderDetails = matchedOrder
                            }

                            Log.d(logTag, "===============================================")
                            Log.d(logTag, "TRACK BUTTON CLICKED!")
                            Log.d(logTag, "Current input order sequence ID: '$orderNumberInput'")
                            Log.d(logTag, "===============================================")
                        },
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14532D)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Track order", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // BOTTOM SHEET
        if (showBottomSheet && bottomSheetData != null) {
            OrderStatusBottomSheet(
                data = bottomSheetData!!,
                onDismiss = { showBottomSheet = false; bottomSheetData = null }
            )
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// SAFE JETPACK COMPOSE PREVIEWS (Self-Contained)
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Initial Entry View", showSystemUi = true)
@Composable
fun TrackOrderInitialPreview() {
    TrackOrderInputScreen(
        navController = androidx.navigation.compose.rememberNavController(),
        orderId = "",
        onBack = {}
    )
}

@Preview(name = "Success View - Multi Shipment Layout", showSystemUi = true)
@Composable
fun TrackOrderSuccessPreview() {
    val scrollState = rememberScrollState()

    val sampleOrderIncrementId = "UB-9482-105"
    val sampleOrderDate = "May 28, 2026"
    val sampleOrderStatusEn = "out_for_delivery"

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F6))
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // 1. HEADER
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF1F2937))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Track your order", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            }

            // 2. MAIN LAYOUT CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.ic_tracking), contentDescription = null, modifier = Modifier.size(90.dp).padding(bottom = 16.dp))
                    Text(text = "Enter your order number to check your order status.", fontSize = 15.sp, color = Color(0xFF374151), modifier = Modifier.padding(bottom = 20.dp), lineHeight = 20.sp)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Order number ", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937))
                        Text("*", fontSize = 14.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Input Textbox Field Simulation
                    BasicTextField(
                        value = sampleOrderIncrementId,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF1F2937)),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)).background(Color(0xFFF3F4F6)).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                innerTextField()
                            }
                        }
                    )

                    // Force multi-shipment UI layout cards to draw directly on screen
                    val pkg1Dots = resolveCompletedStepCount(sampleOrderStatusEn).coerceAtMost(4)
                    val pkg2Dots = resolveCompletedStepCount("processing").coerceAtMost(4)

                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)), border = BorderStroke(1.dp, Color(0xFFE5E7EB))) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF4B5563), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Order $sampleOrderIncrementId is arriving in two shipments", fontSize = 13.sp, color = Color(0xFF374151), fontWeight = FontWeight.Medium)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Package 1 Component Render Simulation
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.5.dp, Color(0xFFFBBF24))) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(2) { index ->
                                        Box(modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE5E7EB)), contentAlignment = Alignment.Center) { Text("📦", fontSize = 18.sp) }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = sampleOrderDate, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Out for delivery • Courier is delivering your package", fontSize = 12.sp, color = Color(0xFF4B5563))
                                Spacer(modifier = Modifier.height(16.dp))
                                ShipmentCapsuleBar(completedDots = pkg1Dots, leftDrawable = R.drawable.ic_usa_flag, isLeftCircle = true)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Package 2 Component Render Simulation
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFE5E7EB))) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(1) { index ->
                                        Box(modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE5E7EB)), contentAlignment = Alignment.Center) { Text("📦", fontSize = 18.sp) }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = sampleOrderDate, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Processing • The store is preparing the items.", fontSize = 12.sp, color = Color(0xFF4B5563))
                                Spacer(modifier = Modifier.height(16.dp))
                                ShipmentCapsuleBar(completedDots = pkg2Dots, leftDrawable = R.drawable.globe, isLeftCircle = false)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Track Button Setup
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(42.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14532D)), shape = RoundedCornerShape(14.dp)) {
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Track order", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Order Status Bottom Sheet Preview", showSystemUi = true)
@Composable
fun OrderStatusBottomSheetPreview() {
    val mockItems = listOf(
        Item(
            item_id = 101,
            name = "Premium Wireless Noise-Cancelling Earbuds",
            image = "https://example.com/image.png"
        ),
        Item(
            item_id = 102,
            name = "USB-C Fast Charging Cable (2m)",
            image = "https://example.com/image.png"
        )
    )

    // 2. Package the data payload safely
    val mockBottomSheetData = ShipmentBottomSheetData(
        estimatedDelivery = "Thursday, May 28, 2026",
        trackingNumber = "DHL-7394-UX9",
        orderStatusEn = "out_for_delivery",
        items = mockItems,
        completedDots = 3
    )

    // 3. Render container layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9CA3AF))
    ) {
        OrderStatusBottomSheet(
            data = mockBottomSheetData,
            onDismiss = {}
        )
    }
}
