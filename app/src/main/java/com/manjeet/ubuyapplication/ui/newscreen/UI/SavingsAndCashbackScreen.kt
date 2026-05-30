package com.manjeet.ubuyapplication.ui.newscreen.UI

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.R
import com.manjeet.ubuyapplication.model.UbuyMembershipData
import com.manjeet.ubuyapplication.model.TxnValue

@Composable
fun SavingsAndCashbackScreen(
    ubuyMembershipData: UbuyMembershipData?,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var isExpanded by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All Time") }

    val savingsData = ubuyMembershipData?.data?.savingsSection
    val shippingData = ubuyMembershipData?.data?.shippingSection
    val rawTxList = shippingData?.tableData?.values ?: arrayListOf()
    val txList = when (selectedFilter) {
        "Last 3 Months" -> if (rawTxList.isNotEmpty()) listOf(rawTxList.first()) else emptyList()
        "Last 6 Months" -> rawTxList.take(2)
        else -> rawTxList
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF1F5))
    ) {
        // TOP CUSTOM NAVIGATION BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Savings and cashback",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            //  CARDS SECTION
            val totalAmt = savingsData?.totalSavings?.amount ?: "0.00"
            val totalCurr = savingsData?.totalSavings?.currency ?: "INR"
            TopOverviewCard(
                iconRes = R.drawable.ic_wallet,
                title = savingsData?.totalSavings?.heading ?: "Total Savings",
                subtitle = savingsData?.totalSavings?.subHeading ?: "Shipping and cashback",
                amountDisplay = "$totalCurr $totalAmt"
            )
            Spacer(modifier = Modifier.height(12.dp))

            val shipAmt = savingsData?.shippingSavings?.amount ?: "0.00"
            val shipCurr = savingsData?.shippingSavings?.currency ?: "INR"
            TopOverviewCard(
                iconRes = R.drawable.ic_wallet,
                title = savingsData?.shippingSavings?.heading ?: "Shipping Savings",
                subtitle = savingsData?.shippingSavings?.subHeading ?: "Saved on fast delivery",
                amountDisplay = "$shipCurr $shipAmt"
            )
            Spacer(modifier = Modifier.height(12.dp))

            TopOverviewCard(
                iconRes = R.drawable.ic_coin,
                title = "Total Cashback",
                subtitle = "Cashback earned from orders",
                amountDisplay = "$totalCurr $totalAmt"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            //     LIST SECTION
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Shipping savings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )


                Box {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        modifier = Modifier.clickable { isDropdownExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF78350F)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            //  Dynamic Text Label
                            Text(
                                text = selectedFilter,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF78350F)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "▼",
                                fontSize = 8.sp,
                                color = Color(0xFF78350F)
                            )
                        }
                    }

                    //  DROPDOWN
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp))
                    ) {
                        val filterOptions = listOf("All Time", "Last 3 Months", "Last 6 Months")
                        filterOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option,
                                        fontSize = 14.sp,
                                        color = if (selectedFilter == option) Color(0xFF78350F) else Color(0xFF1F2937),
                                        fontWeight = if (selectedFilter == option) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    selectedFilter = option
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

           // ==========================================
           //  FIXED DYNAMIC FILTER LIST MANAGEMENT
           // ==========================================
            val rawTxList = shippingData?.tableData?.values ?: arrayListOf()

            val txList = when (selectedFilter) {
                "Last 3 Months" -> if (rawTxList.isNotEmpty()) listOf(rawTxList.first()) else emptyList()
                "Last 6 Months" -> rawTxList.take(2)
                else -> rawTxList

            }

            if (txList.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.box),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No data found", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            } else {
                // IsExpanded state tracking text toggle logic
                val itemsToShow = if (isExpanded) txList else txList.take(3)
                itemsToShow.forEach { txn ->
                    TransactionItemRow(txn)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (txList.size > 3) {
                    Text(
                        text = if (isExpanded) "View Less ∧" else "View More ∨",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF78350F),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isExpanded = !isExpanded }
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

        // =========================
        //   GRAPH CARD:)
        // =========================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Shipping Discount by Month",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(text = "Total Shipping Discount", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Live dataset
                    val graphVals = shippingData?.graph?.datasets?.firstOrNull()?.values
                    val aprVal = graphVals?.apr2026 ?: 2000.0
                    val marVal = graphVals?.mar2026 ?: 2200.0
                    val febVal = graphVals?.feb2026 ?: 25000.0

                    val maxGraphValue = 30000.0

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // 1. Y-AXIS LABELS
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(
                                    end = 12.dp,
                                    bottom = 20.dp
                                ), // Bottom padding to align with bars baseline
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                "30000",
                                fontSize = 11.sp,
                                color = Color(0xFF9CA3AF),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "20000",
                                fontSize = 11.sp,
                                color = Color(0xFF9CA3AF),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "10000",
                                fontSize = 11.sp,
                                color = Color(0xFF9CA3AF),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "0",
                                fontSize = 11.sp,
                                color = Color(0xFF9CA3AF),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // 2. MAIN GRAPH CANVAS LAYER WITH HORIZONTAL DASHED ALIGNMENT
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            // Background Guidelines Layer
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 26.dp) // Aligns grid lines perfectly
                            ) {
                                val canvasHeight = size.height
                                val lineCount = 4
                                val step = canvasHeight / (lineCount - 1)

                                for (i in 0 until lineCount) {
                                    val y = step * i
                                    drawLine(
                                        color = Color(0xFFF3F4F6),
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = 1.5f,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(
                                                12f,
                                                8f
                                            ), 0f
                                        )
                                    )
                                }


                                drawLine(
                                    color = Color(0xFFE5E7EB),
                                    start = Offset(0f, canvasHeight),
                                    end = Offset(size.width, canvasHeight),
                                    strokeWidth = 2f
                                )
                            }

                            //  DYNAMIC BARS OVERLAY
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                BarChartColumn(
                                    label = "Apr 2026",
                                    value = aprVal,
                                    maxVal = maxGraphValue
                                )
                                BarChartColumn(
                                    label = "Mar 2026",
                                    value = marVal,
                                    maxVal = maxGraphValue
                                )
                                BarChartColumn(
                                    label = "Feb 2026",
                                    value = febVal,
                                    maxVal = maxGraphValue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun TopOverviewCard(iconRes: Int, title: String, subtitle: String, amountDisplay: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color(0xFFF3F4F6), CircleShape), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Unspecified)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = amountDisplay, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            }
        }
    }
}

@Composable
fun TransactionItemRow(txn: TxnValue) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = txn.txnDateTime ?: "Oct 30, 2025", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                }
                Surface(color = Color(0xFFFEF3C7), shape = RoundedCornerShape(6.dp)) {
                    Text(text = "Not Eligible", color = Color(0xFFD97706), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Transaction Type:", fontSize = 12.sp, color = Color.Gray)
                Text(txn.txnType ?: "Receive Cashback On Purchase", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1F2937))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Order Number:", fontSize = 12.sp, color = Color.Gray)
                Text(txn.orderNumber ?: "168231641411215", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Amount:", fontSize = 12.sp, color = Color.Gray)
                Text(txn.creditAmount ?: "INR 2898.645", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
            }
        }
    }
}


@Composable
fun BarChartColumn(label: String, value: Double, maxVal: Double) {

    val scaleFactor = (value / maxVal).coerceIn(0.04, 1.0)

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // The Visual Data Bar Column
        Box(
            modifier = Modifier
                .width(32.dp)
                .weight(1f, fill = false)
                .fillMaxHeight(scaleFactor.toFloat() * 0.82f)
                .background(
                    color = Color(0xFF064E3B),
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Month Text Label
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )
    }
}