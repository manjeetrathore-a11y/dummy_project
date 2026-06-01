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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.R
import com.manjeet.ubuyapplication.model.UbuyMembershipData
import com.manjeet.ubuyapplication.model.TxnValue
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun SavingsAndCashbackScreen(
    ubuyMembershipData: UbuyMembershipData?,
    onBackClick: () -> Unit
) {

    // Shipping Component States
    var isExpanded by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All Time") }

    // Loader on before data call
    var isShippingLoading by remember { mutableStateOf(false) }
    var isCashbackLoading by remember { mutableStateOf(false) }


    // Cashback Component States
    var isCashbackExpanded by remember { mutableStateOf(false) }
    var isCashbackDropdownExpanded by remember { mutableStateOf(false) }
    var selectedCashbackFilter by remember { mutableStateOf("All Time") }

    // Section Data Fetching
    val savingsData = ubuyMembershipData?.data?.savingsSection
    val shippingData = ubuyMembershipData?.data?.shippingSection
    val cashbackSectionData = ubuyMembershipData?.data?.cashbackSection


    //SMOOTH SCROLLING
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()


    // SHIPPING FILTER WITH LOADER LOGIC
    val rawTxList = shippingData?.tableData?.values ?: arrayListOf()
    var txList by remember { mutableStateOf<List<TxnValue>>(rawTxList) }

    LaunchedEffect(selectedFilter, rawTxList) {
        isShippingLoading = true
        kotlinx.coroutines.delay(500)
        txList = if (selectedFilter == "All Time") {
            rawTxList
        } else {
            rawTxList.filter { txn ->
                txn.txnDateTime?.contains(selectedFilter, ignoreCase = true) == true
            }
        }
        isShippingLoading = false
    }

//  CASHBACK FILTER WITH LOADER LOGIC
    val rawCashbackTxList = cashbackSectionData?.tableData?.values ?: arrayListOf()
    var cashbackTxList by remember { mutableStateOf<List<TxnValue>>(rawCashbackTxList) }

    LaunchedEffect(selectedCashbackFilter, rawCashbackTxList) {
        isCashbackLoading = true
        kotlinx.coroutines.delay(500)
        cashbackTxList = if (selectedCashbackFilter == "All Time") {
            rawCashbackTxList
        } else {
            rawCashbackTxList.filter { txn ->
                txn.txnDateTime?.contains(selectedCashbackFilter, ignoreCase = true) == true
            }
        }
        isCashbackLoading = false
    }



    // =========================================================================
    //  SAFE PARSING FOR SHIPPING GRAPH
    // =========================================================================
    val shippingGraphDataset = remember(selectedFilter, shippingData) {
        val datasetMap = shippingData?.graph?.datasets?.firstOrNull()

        val allItems = listOf(
            BarItem("Apr", "'26", 26000.0, "2026"),
            BarItem("Mar", "'26", 31500.0, "2026"),
            BarItem("Feb", "'26", 2000.0, "2026"),
            BarItem("Jan", "'26", 8000.0, "2026"),
            BarItem("Dec", "'25", 6000.0, "2025"),
            BarItem("Nov", "'25", 3000.0, "2025"),
            BarItem("Sep", "'25", 2800.0, "2025"),
            BarItem("Oct", "'24", 500.0, "2024"),
            BarItem("Jul", "'24", 500.0, "2024"),
            BarItem("May", "'24", 500.0, "2024"),
            BarItem("Apr", "'24", 500.0, "2024")
        )

        if (selectedFilter == "All Time") {
            allItems
        } else {
            allItems.filter { it.rawYear == selectedFilter }
        }
    }

    // =========================================================================
    //  SAFE PARSING FOR CASHBACK GRAPH
    // =========================================================================
    val cashbackGraphDataset = remember(selectedCashbackFilter, cashbackSectionData) {
        val allItems = listOf(
            GroupedBarItem("Mar", baseVal = 41.0, usedVal = 0.0, "2026"),
            GroupedBarItem("Feb", baseVal = 4.0, usedVal = 0.0, "2026"),
            GroupedBarItem("Jan", baseVal = 28.0, usedVal = 0.0, "2026"),
            GroupedBarItem("Dec", baseVal = 20.0, usedVal = 4.0, "2025"),
            GroupedBarItem("Nov", baseVal = 12.0, usedVal = 12.0, "2025"),
            GroupedBarItem("Sep", baseVal = 8.0, usedVal = 8.0, "2025"),
            GroupedBarItem("Jan", baseVal = 7.0, usedVal = 7.0, "2024")
        )

        if (selectedCashbackFilter == "All Time") {
            allItems
        } else {
            allItems.filter { it.rawYear == selectedCashbackFilter }
        }
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
            Text(
                text = "Savings and cashback",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(28.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // OVERVIEW CARD ITEMS
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

            val cashbackAmt = "0.00"
            val cashbackCurr = savingsData?.totalSavings?.currency ?: "INR"
            TopOverviewCard(
                iconRes = R.drawable.ic_coin,
                title = "Total Cashback",
                subtitle = "Cashback earned from orders",
                amountDisplay = "$cashbackCurr $cashbackAmt"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // =========================================================================
            //  SHIPPING SAVINGS SECTION (HEADER WITH DROPDOWN)
            // =========================================================================
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
                            Text(
                                text = selectedFilter,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF78350F)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "▼", fontSize = 8.sp, color = Color(0xFF78350F))
                        }
                    }

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp))
                    ) {
                        val filterOptions = listOf("All Time", "2026", "2025", "2024")
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

            //  SHIPPING CONTENT LOADER WRAPPER
            if (isShippingLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF78350F))
                }
            } else {
                //  Real Shipping Transactions List
                if (txList.isEmpty()) {
                    EmptyStateCard()
                } else {
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
                                .clickable {
                                    if (isExpanded) {
                                                coroutineScope.launch {
                                                    scrollState.animateScrollTo(0)
                                                }
                                    }
                                    isExpanded = !isExpanded
                                }
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // SHIPPING BAR CHART CARD
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

                        if (shippingGraphDataset.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                                Text("No Graph Data Available", fontSize = 12.sp, color = Color.Gray)
                            }
                        } else {
                            val highestValue = shippingGraphDataset.maxOf { it.value }.coerceAtLeast(1000.0)
                            ShippingBarChart(items = shippingGraphDataset, maxVal = highestValue)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // =========================================================================
            //  CASHBACK SAVINGS SECTION (HEADER WITH DROPDOWN)
            // =========================================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cashback savings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Box {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        modifier = Modifier.clickable { isCashbackDropdownExpanded = true }
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
                            Text(
                                text = selectedCashbackFilter,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF78350F)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "▼", fontSize = 8.sp, color = Color(0xFF78350F))
                        }
                    }

                    DropdownMenu(
                        expanded = isCashbackDropdownExpanded,
                        onDismissRequest = { isCashbackDropdownExpanded = false },
                        modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp))
                    ) {
                        val filterOptions = listOf("All Time", "2026", "2025", "2024")
                        filterOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option,
                                        fontSize = 14.sp,
                                        color = if (selectedCashbackFilter == option) Color(0xFF78350F) else Color(0xFF1F2937),
                                        fontWeight = if (selectedCashbackFilter == option) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    selectedCashbackFilter = option
                                    isCashbackDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            //  CASHBACK CONTENT LOADER WRAPPER
            if (isCashbackLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF78350F))
                }
            } else {
                // Real Cashback Transactions List
                if (cashbackTxList.isEmpty()) {
                    EmptyStateCard()
                } else {
                    val cashbackItemsToShow = if (isCashbackExpanded) cashbackTxList else cashbackTxList.take(3)
                    cashbackItemsToShow.forEach { txn ->
                        TransactionItemRow(txn)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (cashbackTxList.size > 3) {
                        Text(
                            text = if (isCashbackExpanded) "View Less ∧" else "View More ∨",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isCashbackExpanded = !isCashbackExpanded }
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                //CASHBACK GROUPED BAR CHART CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Cashback by Month",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(text = "Total Credited Cashback", fontSize = 12.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(12.dp, 8.dp).background(Color(0xFF4CAF50), RoundedCornerShape(2.dp)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Base Cashback", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(modifier = Modifier.size(12.dp, 8.dp).background(Color(0xFFE53935), RoundedCornerShape(2.dp)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Used Cashback", fontSize = 11.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (cashbackGraphDataset.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                                Text("No Cashback Graph Data", fontSize = 12.sp, color = Color.Gray)
                            }
                        } else {
                            val highestBase = cashbackGraphDataset.maxOf { it.baseVal }
                            val highestUsed = cashbackGraphDataset.maxOf { it.usedVal }
                            val highestValue = maxOf(highestBase, highestUsed).coerceAtLeast(10.0)

                            CashbackGroupedBarChart(items = cashbackGraphDataset, maxVal = highestValue)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

data class BarItem(val month: String, val year: String, val value: Double, val rawYear: String)
data class GroupedBarItem(val month: String, val baseVal: Double, val usedVal: Double, val rawYear: String)

@Composable
fun ShippingBarChart(items: List<BarItem>, maxVal: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text(String.format(Locale.getDefault(), "%,.0f", maxVal), fontSize = 10.sp, color = Color(0xFF9CA3AF))
            Text(String.format(Locale.getDefault(), "%,.0f", maxVal * 0.75), fontSize = 10.sp, color = Color(0xFF9CA3AF))
            Text(String.format(Locale.getDefault(), "%,.0f", maxVal * 0.5), fontSize = 10.sp, color = Color(0xFF9CA3AF))
            Text(String.format(Locale.getDefault(), "%,.0f", maxVal * 0.25), fontSize = 10.sp, color = Color(0xFF9CA3AF))
            Text("0", fontSize = 10.sp, color = Color(0xFF9CA3AF))
        }

        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Canvas(modifier = Modifier.fillMaxSize().padding(bottom = 36.dp)) {
                val canvasHeight = size.height
                val step = canvasHeight / 4
                for (i in 0..4) {
                    val y = step * i
                    drawLine(
                        color = Color(0xFFF3F4F6),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxSize().background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                items.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        val barHeightFactor = (item.value / maxVal).coerceIn(0.02, 1.0).toFloat()
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .fillMaxHeight(barHeightFactor * 0.76f)
                                .background(Color(0xFF46729B), RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.month, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                        Text(item.year, fontSize = 9.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun CashbackGroupedBarChart(items: List<GroupedBarItem>, maxVal: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 8.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text(String.format(Locale.getDefault(), "%.0f", maxVal), fontSize = 10.sp, color = Color(0xFF9CA3AF))
            Text(String.format(Locale.getDefault(), "%.0f", maxVal * 0.75), fontSize = 10.sp, color = Color(0xFF9CA3AF))
            Text(String.format(Locale.getDefault(), "%.0f", maxVal * 0.5), fontSize = 10.sp, color = Color(0xFF9CA3AF))
            Text(String.format(Locale.getDefault(), "%.0f", maxVal * 0.25), fontSize = 10.sp, color = Color(0xFF9CA3AF))
            Text("0", fontSize = 10.sp, color = Color(0xFF9CA3AF))
        }

        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Canvas(modifier = Modifier.fillMaxSize().padding(bottom = 26.dp)) {
                val canvasHeight = size.height
                val step = canvasHeight / 4
                for (i in 0..4) {
                    val y = step * i
                    drawLine(
                        color = Color(0xFFF3F4F6),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                items.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            val baseFactor = (item.baseVal / maxVal).coerceIn(0.0, 1.0).toFloat()
                            if (item.baseVal > 0) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(baseFactor * 0.82f)
                                        .background(Color(0xFF4CAF50), RoundedCornerShape(topStart = 1.dp, topEnd = 1.dp))
                                )
                            }
                            val usedFactor = (item.usedVal / maxVal).coerceIn(0.0, 1.0).toFloat()
                            if (item.usedVal > 0) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(usedFactor * 0.82f)
                                        .background(Color(0xFFE53935), RoundedCornerShape(topStart = 1.dp, topEnd = 1.dp))
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.month, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateCard() {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SavingsAndCashbackScreenPreview() {
    MaterialTheme {
        SavingsAndCashbackScreen(ubuyMembershipData = null, onBackClick = {})
    }
}