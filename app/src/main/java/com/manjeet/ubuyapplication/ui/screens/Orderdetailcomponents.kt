package com.manjeet.ubuyapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.manjeet.ubuyapplication.Order
import com.manjeet.ubuyapplication.R

// ==========================================
// 1. PACKING STATUS LAYOUT ENGINE BLOCK
// ==========================================
@Composable
fun PackingStatusLayout(
    navController: NavHostController,  // FIX 1: Added navController parameter
    order: Order
) {
    val currentStatus = if (!order.order_status_en.isNullOrEmpty()) order.order_status_en else order.order_status

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Order status",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFF3F4F6))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Order #${order.order_increment_id} is currently $currentStatus",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            order.items.forEachIndexed { index, item ->
                ShipmentProductCard(
                    navController = navController,   // FIX 2: Pass navController
                    status = currentStatus,
                    description = if (index == 0) "Left the warehouse" else "The store is preparing the items.",
                    deliveryDate = order.order_date,
                    progress = if (index == 0) 0.7f else 0.3f,
                    images = listOf(item.image),
                    order = order,
                    onTrackingClick = {               // FIX 3: Added missing onTrackingClick lambda
                        navController.navigate("trackOrder")
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentRow("Order Date", order.order_date)
            PaymentRow("Status Context", currentStatus)

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.payment_method.ifEmpty { "Payment" },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Total paid",
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                }
                Text(
                    text = order.order_total,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }
    }
}

// ==========================================
// 2. SHIPMENT EXTENSION DROPDOWN LAYOUT
// ==========================================
@Composable
fun ShipmentProductCard(
    navController: NavHostController,
    status: String,
    description: String,
    deliveryDate: String,
    progress: Float,
    images: List<String>,
    order: Order,
    onTrackingClick: () -> Unit   // FIX 4: Kept as a proper parameter (was unused/commented before)
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(300),
        label = "rotation"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            border = BorderStroke(3.dp, Color(0xFFF3F4F6))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        images.forEach { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .clickable { isExpanded = !isExpanded }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isExpanded) "Hide Details" else "View Details",
                            color = Color(0xFFB07D3E),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFFB07D3E),
                            modifier = Modifier
                                .size(18.dp)
                                .graphicsLayer(rotationZ = rotationState)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Estimated delivery $deliveryDate", fontSize = 13.sp, color = Color.Gray)
                Row {
                    Text(status, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(" • $description", color = Color.Gray, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF9FAFB),
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🇺🇸", fontSize = 16.sp)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp)
                                .height(6.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color(0xFFE5E7EB), CircleShape))
                            Box(modifier = Modifier.fillMaxWidth(progress).height(4.dp).background(Color(0xFF2E7D32), CircleShape))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(4) {
                                    Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                                }
                            }
                        }
                        Text("🇬🇧", fontSize = 16.sp)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Order status", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(20.dp)) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, null, modifier = Modifier.size(14.dp), tint = Color(0xFF2E7D32))
                            Spacer(Modifier.width(4.dp))
                            Text(status, color = Color(0xFF2E7D32), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Estimated arrival", color = Color.Gray, fontSize = 13.sp)
                        Text(deliveryDate, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TimelineItem("Order confirmed", "Verified", true, false, isFirst = true)
                TimelineItem("Packing", "Preparing items", progress >= 0.3f, false)
                TimelineItem("Shipped", "Left warehouse", progress >= 0.7f, false)
                TimelineItem("Delivered", "To your address", progress == 1.0f, false, isLast = true)

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTrackingClick() },  // FIX 5: Use callback instead of direct navController call
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text("Shipment tracking no")
                            Text(order.order_increment_id)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                order.items.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            DetailProductItem(
                                name = item.name,
                                desc = "Qty: 1",
                                price = order.order_total,
                                img = item.image
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. DELIVERED / CANCELLED STATUS LAYOUT
// ==========================================
@Composable
fun DeliveredStatusLayout(order: Order) {
    val trueStatus = if (!order.order_status_en.isNullOrEmpty()) order.order_status_en else order.order_status
    val isCancelled = trueStatus.equals("Cancelled", ignoreCase = true) || trueStatus.equals("closed", ignoreCase = true)

    val topBoxBgColor = if (isCancelled) Color(0xFFFFEBEE) else Color(0xFFF3F4F6)
    val topIconColor = if (isCancelled) Color(0xFFD32F2F) else Color.Black
    val topIcon = if (isCancelled) Icons.Default.Close else Icons.Default.Check

    val circleIcon = if (isCancelled) Icons.Default.Cancel else Icons.Default.CheckCircle
    val circleIconColor = if (isCancelled) Color(0xFFD32F2F) else Color(0xFF10B981)

    Column(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = trueStatus, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Surface(
                        modifier = Modifier.size(28.dp),
                        color = topBoxBgColor,
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Icon(imageVector = topIcon, contentDescription = null, tint = topIconColor, modifier = Modifier.padding(6.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Icon(imageVector = circleIcon, contentDescription = null, tint = circleIconColor, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isCancelled) "Cancelled on ${order.order_date}" else "Ordered on ${order.order_date}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(11.dp))
                        val addressDetails = StringBuilder()
                        if (order.ship_to.isNotEmpty()) addressDetails.append("Delivered to: ${order.ship_to}")
                        if (order.full_address.isNotEmpty()) addressDetails.append("\nAddress: ${order.full_address}")

                        if (addressDetails.isNotEmpty()) {
                            Text(text = addressDetails.toString(), fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                order.items.forEachIndexed { index, item ->
                    val isNetworkImage = item.image.startsWith("http://") || item.image.startsWith("https://")

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(3.dp, Color(0xFFF3F4F6)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF9F9F9))
                                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isNetworkImage) {
                                        AsyncImage(
                                            model = item.image,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    } else {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_launcher_background),
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    val cleanedName = remember(item.name) {
                                        HtmlCompat.fromHtml(item.name, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                                    }
                                    Text(text = cleanedName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("US · 1 pcs", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Text(order.order_total, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
                            Row(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.weight(1f)) { ProductActionButton(Icons.Default.Support, "Support") {} }
                                Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFF3F4F6)))
                                Box(modifier = Modifier.weight(1f)) { ProductActionButton(Icons.Default.StarBorder, "Write review") {} }
                                Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFF3F4F6)))
                                Box(modifier = Modifier.weight(1f)) { ProductActionButton(Icons.Default.RotateLeft, "Return") {} }
                            }
                        }
                    }

                    if (index < order.items.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PaymentRow("Price", order.order_total)
                    if (order.shipping_method.isNotEmpty()) {
                        PaymentRow(order.shipping_method, "Included")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Total paid", fontSize = 16.sp, color = Color.Gray)
                    Text(text = order.order_total, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Order again", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    border = BorderStroke(1.dp, Color(0xFFB07D3E)),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB07D3E))
                ) {
                    Text("Request a return", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DefaultStatusLayout(order: Order) {
    Box(modifier = Modifier.padding(16.dp)) {
        Text("Status Processing...")
    }
}

// ==========================================
// 4. SUB-COMPOSABLES REUSABLE UI ELEMENTS
// ==========================================
@Composable
fun ProductActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.DarkGray)
            Spacer(Modifier.width(6.dp))
            Text(label, fontSize = 12.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun PaymentRow(label: String, value: String, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDiscount) Color(0xFF2E7D32) else Color.Black
        )
    }
}

@Composable
fun TimelineItem(
    title: String,
    subtitle: String,
    isDone: Boolean,
    isCurrent: Boolean,
    isLast: Boolean = false,
    isFirst: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.width(36.dp).fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .width(14.dp)
                    .fillMaxHeight()
                    .background(
                        color = if (isDone || isCurrent) Color(0xFF4CAF50) else Color(0xFFF0F0F0),
                        shape = when {
                            isFirst -> RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                            isLast -> RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                            else -> RoundedCornerShape(0.dp)
                        }
                    )
            )

            Box(
                modifier = Modifier.padding(top = 6.dp).size(14.dp).background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(Icons.Default.Check, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(10.dp))
                } else if (isCurrent) {
                    Box(modifier = Modifier.size(8.dp).border(1.5.dp, Color(0xFF4CAF50), CircleShape))
                } else {
                    Box(modifier = Modifier.size(4.dp).background(Color(0xFFD1D1D1), CircleShape))
                }
            }
        }

        Column(modifier = Modifier.padding(start = 12.dp, bottom = 28.dp).weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isDone || isCurrent) FontWeight.Bold else FontWeight.Medium,
                color = if (isDone || isCurrent) Color.Black else Color.Gray
            )
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun DetailProductItem(
    name: String,
    desc: String,
    price: String,
    img: String,
    countryCode: String = "US",
    qty: String = "1"
) {
    val flagEmoji = countryCode.uppercase().map { char ->
        Character.toChars(char.code + 0x1F1E6 - 65).concatToString()
    }.joinToString("")

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = img,
                contentDescription = name,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = HtmlCompat.fromHtml(name, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = desc, color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.size(24.dp).background(Color(0xFFF3F4F6), CircleShape).border(1.dp, Color(0xFFE5E7EB), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text(text = flagEmoji, fontSize = 13.sp) }
                Text(text = countryCode.uppercase(), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF374151))
                Text(text = "•", color = Color.Gray, fontSize = 12.sp)
                Text(text = "Qty: $qty", fontSize = 13.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
            }
            Text(text = price, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.Black)
        }
    }
}

@Composable
fun DetailActionItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}