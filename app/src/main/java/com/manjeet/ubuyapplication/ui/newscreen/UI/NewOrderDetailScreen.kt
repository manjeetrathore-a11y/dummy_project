package com.manjeet.ubuyapplication.ui.newscreen.UI

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.manjeet.ubuyapplication.model.Order
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun PackingStatusLayout(
    navController: NavHostController,
    order: Order
) {
    val currentStatus = if (!order.order_status_en.isNullOrEmpty()) order.order_status_en else order.order_status


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
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
                        navController = navController,
                        status = currentStatus ?: "Processing",
                        description = if (index == 0) "Left the warehouse" else "The store is preparing the items.",
                        deliveryDate = order.order_date ?: "",
                        progress = if (index == 0) 0.7f else 0.3f,
                        images = listOf(item.image ?: ""),
                        order = order,
                        onTrackingClick = {
                            navController.navigate("trackOrder")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // -- JSON-MAPPED PRICE BREAKDOWN ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Price Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Price", color = Color(0xFF6B7280), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    Text(text = order.order_total ?: "KWD 0.00", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                // 2. Standard Shipping Row (Dynamic shipping_method formatting)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val displayMethod = if (order.shipping_method?.lowercase() == "express") "Express Shipping" else "Standard Shipping"
                    Text(text = displayMethod, color = Color(0xFF6B7280), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    Text(text = "KWD 1.50", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                // 3. Customs & Duties Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Customs & Duties", color = Color(0xFF6B7280), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    Text(text = "KWD 0.80", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                // 4. VAT Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "VAT", color = Color(0xFF6B7280), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    Text(text = "KWD 1.20", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                // 5. U-Care Warranty Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "U-Care warranty total", color = Color(0xFF6B7280), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    Text(text = "KWD 0.00", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                // 6. Discount Row (Dynamic Green Text)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Discount", color = Color(0xFF6B7280), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    Text(text = "-KWD 0.00", color = Color(0xFF10B981), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)
                Spacer(modifier = Modifier.height(4.dp))

                // 7. Total Paid Bottom Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Dynamic Payment Method Badge Container
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            color = Color.White
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val rawPayment = order.payment_method ?: ""
                                val cleanPaymentText = when {
                                    rawPayment.contains("gpay", ignoreCase = true) -> "G Pay"
                                    rawPayment.contains("apple", ignoreCase = true) -> "Apple Pay"
                                    rawPayment.contains("paypal", ignoreCase = true) -> "PayPal"
                                    else -> rawPayment.uppercase().replace("_", " ")
                                }

                                Text(
                                    text = cleanPaymentText.ifEmpty { "G Pay" },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Total paid",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = order.order_total ?: "KWD 0.00",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun ShipmentProductCard(
    navController: NavHostController,
    status: String,
    description: String,
    deliveryDate: String,
    progress: Float,
    images: List<String>,
    order: Order,
    onTrackingClick: () -> Unit
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
                            val finalImgUrl = when {
                                imageUrl.isEmpty() -> ""
                                imageUrl.startsWith("http") -> imageUrl
                                else -> "https://images.ubuy.com" + imageUrl
                            }
                            AsyncImage(
                                model = finalImgUrl,
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

                val context = androidx.compose.ui.platform.LocalContext.current

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            try {

                                navController.navigate("trackOrder?orderId=${order.order_increment_id ?: ""}")
                            } catch (e: IllegalArgumentException) {
                                android.widget.Toast.makeText(
                                    context,
                                    "Tracking screen route not found in graph!",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
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
                            Text(
                                text = "Shipment tracking no",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = order.order_increment_id ?: "N/A",
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
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
                            val finalItemImg = when {
                                item.image.isNullOrEmpty() -> ""
                                item.image.startsWith("http") -> item.image
                                else -> "https://images.ubuy.com" + item.image
                            }
                            DetailProductItem(
                                name = item.name ?: "Product Item",
                                desc = "Qty: 1",
                                price = order.order_total ?: "",
                                img = finalItemImg
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveredStatusLayout(order: Order) {
    val trueStatus = if (!order.order_status_en.isNullOrEmpty()) order.order_status_en else order.order_status
    val isCancelled = trueStatus.equals("Cancelled", ignoreCase = true) || trueStatus.equals("closed", ignoreCase = true)

    val topBoxBgColor = if (isCancelled) Color(0xFFFFEBEE) else Color(0xFFF3F4F6)
    val topIconColor = if (isCancelled) Color(0xFFD32F2F) else Color.Black
    val topIcon = if (isCancelled) Icons.Default.Close else Icons.Default.Check

    val circleIcon = if (isCancelled) Icons.Default.Cancel else Icons.Default.CheckCircle
    val circleIconColor = if (isCancelled) Color(0xFFD32F2F) else Color(0xFF10B981)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
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
                    Text(text = trueStatus ?: "", fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
                        if (!order.ship_to.isNullOrEmpty()) addressDetails.append("Delivered to: ${order.ship_to}")
                        if (!order.full_address.isNullOrEmpty()) addressDetails.append("\nAddress: ${order.full_address}")

                        if (addressDetails.isNotEmpty()) {
                            Text(text = addressDetails.toString(), fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                order.items.forEachIndexed { index, item ->
                    val isNetworkImage = item.image?.let { it.startsWith("http://") || it.startsWith("https://") } ?: false

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
                                        val staticImg = if (!item.image.isNullOrEmpty()) {
                                            "https://images.ubuy.com" + item.image
                                        } else ""
                                        AsyncImage(
                                            model = staticImg,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    val cleanedName = remember(item.name) {
                                        HtmlCompat.fromHtml(item.name ?: "Product", HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                                    }
                                    Text(text = cleanedName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("US · 1 pcs", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Text(order.order_total ?: "", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
                    PaymentRow("Price", order.order_total ?: "")
                    if (!order.shipping_method.isNullOrEmpty()) {
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
                    Text(text = order.order_total ?: "", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
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
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
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
fun DetailActionItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = Color(0xFFF3F4F6), modifier = Modifier.size(36.dp)) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.padding(8.dp), tint = Color.DarkGray)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun DetailProductItem(name: String, desc: String, price: String, img: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = img, contentDescription = null, modifier = Modifier.size(45.dp), contentScale = ContentScale.Fit)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = desc, fontSize = 11.sp, color = Color.Gray)
        }
        Text(text = price, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

// ---  MAIN ENTRY SCREEN ---

@Composable
fun OrderDetailPage(
    navController: NavHostController,
    order: Order,
    onBack: () -> Unit
) {
    val masterScrollState = rememberScrollState()
    val currentStatus = if (!order.order_status_en.isNullOrEmpty()) order.order_status_en else order.order_status

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .verticalScroll(masterScrollState)
    ) {
        // --- 1. Top App Navigation Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
            }
            Text(
                text = "Order #${order.order_increment_id}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = {}) { Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications") }
        }

        //  2. FIRST SEPARATE CARD AREA

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            when {
                currentStatus.equals("Delivered", ignoreCase = true) ||
                        currentStatus.equals("complete", ignoreCase = true) ||
                        currentStatus.equals("Cancelled", ignoreCase = true) ||
                        currentStatus.equals("closed", ignoreCase = true) -> {
                    DeliveredStatusLayout(order = order)
                }

                currentStatus.equals("Packing", ignoreCase = true) ||
                        currentStatus.equals("processing", ignoreCase = true) -> {
                    PackingStatusLayout(
                        navController = navController,
                        order = order
                    )
                }
                else -> { DefaultStatusLayout(order = order) }
            }
        }

        // 3. SECOND SEPARATE CARD AREA: Standalone Support Actions Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                DetailActionItem(
                    icon = Icons.Default.Settings,
                    title = "Have a problem with this order?",
                    subtitle = "Contact support via email"
                )
                HorizontalDivider(
                    color = Color(0xFFF3F4F6),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                DetailActionItem(
                    icon = Icons.Default.Download,
                    title = "Download invoice",
                    subtitle = "Get a .pdf invoice for this order"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ========================================================
//  1. CORRECTED MOCK DATA FOR PREVIEW RENDER
// ========================================================
//private val mockOrderPreview = Order(
//    order_id = 12345,
//    order_increment_id = "109-873642-11",
//    order_date = "22 May 2026",
//    order_status = "Packing",
//    order_status_en = "Packing",
//    order_total = "₹4,250.00",
//    payment_method = "Credit Card",
//    ship_to = "Manjeet Kumar",
//    full_address = "Sector 5, Mansarovar, Jaipur, Rajasthan - 302020",
//    shipping_method = "Express Delivery",
//    address_type = "Home",
//    consolidation_shipment_message = "",
//    shipment_data = listOf(),
//    items = listOf(
//        com.manjeet.ubuyapplication.model.Item(
//            name = "Premium Wireless Noise Cancelling Headphones",
//            image = "/example_image.png",
//            item_id = 1
//        )
//    )
//)
//
//// ========================================================
//// 2. PREVIEW FOR PACKING STATUS LAYOUT
//// ========================================================
//@Preview(showBackground = true, name = "Packing Status Layout Preview")
//@Composable
//fun PackingStatusLayoutPreview() {
//    val dummyNavController = NavHostController(androidx.compose.ui.platform.LocalContext.current)
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFFF3F4F6))
//    ) {
//        PackingStatusLayout(
//            navController = dummyNavController,
//            order = mockOrderPreview
//        )
//    }
//}
//
//// ========================================================
////  3. PREVIEW FOR DELIVERED STATUS LAYOUT
//// ========================================================
//@Preview(showBackground = true, name = "Delivered Status Layout Preview")
//@Composable
//fun DeliveredStatusLayoutPreview() {
//    val deliveredMockOrder = mockOrderPreview.copy(
//        order_status = "Delivered",
//        order_status_en = "Delivered"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFFF3F4F6))
//    ) {
//        DeliveredStatusLayout(
//            order = deliveredMockOrder
//        )
//    }
//}
//
//
//// ========================================================
////  1. EXACT REAL-JSON MOCK FOR FULL PAGE PREVIEW
//// ========================================================
//private val mockFullPageOrder = Order(
//    order_id = 263564,
//    order_increment_id = "12604986924",
//    ship_to = "Soumalya Hajra",
//    order_date = "24/4/2026",
//    order_total = "KWD22.82", // Real JSON total price
//    order_status = "Processing",
//    order_status_en = "Processing",
//    full_address = "Soumalya HajraZbzbdfsdfgskldfgdhfgisdfhiosdfgsiodfhgsidfghsidfhgsidfghisdfghiosdfghisodfg Zbzbfgsdfhsfdhshshsrjryjeyjeytjetyjetyjetyjefgaudfgsdif, Zbsbhjfgfjjjjjjjjjjjjjjjjjjjjjjjjjjjjjsdfsdfisdfiogshfi, Abdally, Kuwait, KW, 54545445",
//    shipping_method = "express",
//    address_type = "Home",
//    payment_method = "adyen_gpay", // dynamic G Pay binding layout test
//    consolidation_shipment_message = "",
//    shipment_data = arrayListOf(),
//    items = arrayListOf(
//        com.manjeet.ubuyapplication.model.Item(
//            name = "Tote Bag Tote Bag",
//            image = "https://m.media-amazon.com/images/I/21dCSvpKXuL._AC_SR400_.jpg",
//            item_id = 541950
//        )
//    )
//)
//
//// =======================================================
////  2. FULL SCREEN ENTRY PREVIEW (OrderDetailPage)
//// =======================================================
//@Preview(showBackground = true, name = "Full Order Detail Screen", showSystemUi = true)
//@Composable
//fun OrderDetailPagePreview() {
//    val dummyNavController = NavHostController(androidx.compose.ui.platform.LocalContext.current)
//
//    OrderDetailPage(
//        navController = dummyNavController,
//        order = mockFullPageOrder,
//        onBack = { /* No-Op for preview */ }
//    )
//}


