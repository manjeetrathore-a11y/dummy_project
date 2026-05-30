package com.manjeet.ubuyapplication.ui.newscreen.UI

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.R
import com.manjeet.ubuyapplication.model.CashbackSection
import com.manjeet.ubuyapplication.model.SavingsSection
import com.manjeet.ubuyapplication.model.TxnValue


@Composable
fun CashbackHistoryScreen(
    cashbackSection: CashbackSection?,
    savingsSection: SavingsSection?,
    onBackClick: () -> Unit
) {

    val transactions: ArrayList<TxnValue> = cashbackSection?.tableData?.values ?: arrayListOf()


    val availableAmount = savingsSection?.totalSavings?.amount ?: "0.00"
    val pendingAmount = savingsSection?.shippingSavings?.amount ?: "0.00"
    val totalAmount = savingsSection?.totalSavings?.amount ?: "0.00"
    val currencySymbol = savingsSection?.totalSavings?.currency ?: "INR"

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF1F5))
    ) {
        //  LAZYCOLUMN START (Full scrollable view)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            // ==========================================
            // HEADER SECTION & SUMMARY CARDS
            // ==========================================
            item {
                Text(
                    text = cashbackSection?.headings ?: "Cashback Transaction",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.padding(top = 20.dp, bottom = 14.dp)
                )

                // Available Card
                SummaryRowItem(
                    title = "Available Cashback",
                    amount = "$currencySymbol $availableAmount",
                    iconId = R.drawable.ic_wallet
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Pending Card
                SummaryRowItem(
                    title = "Pending Cashback",
                    amount = "$currencySymbol $pendingAmount",
                    iconId = R.drawable.ic_pending_clock
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Total Card
                SummaryRowItem(
                    title = "Total Cashback",
                    amount = "$currencySymbol $totalAmount",
                    iconId = R.drawable.ic_coin
                )

                // ==========================================
                // MIDDLE BAR: HISTORY TITLE & FILTER DROPDOWN
                // ==========================================
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = cashbackSection?.tableData?.heading?.txnType ?: "Cashback History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1F2937)
                    )

                    // Timeline Dropdown button pill view
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF78350F)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = cashbackSection?.filers?.selectedLabel ?: "All Time",
                                fontSize = 13.sp,
                                color = Color(0xFF374151),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "▼", fontSize = 10.sp, color = Color(0xFF6B7280))
                        }
                    }
                }
            }

            // ==========================================
            // TRANSACTIONS LIST INJECTOR
            // ==========================================
            items(transactions) { txn ->
                HistoryCardItem(txn = txn)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SummaryRowItem(title: String, amount: String, iconId: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEFF1F5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF3F4F6), shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4B5563))
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = amount, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            }
        }
    }
}


@Composable
fun HistoryCardItem(txn: TxnValue) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Row 1: Calendar icon + Date & Dynamic Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = txn.txnDateTime ?: "N/A", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                }

                // Status mapping color filter logic
                val currentStatus = txn.status ?: "Processing"
                val badgeBgColor = if (currentStatus == "Processing") Color(0xFFEFF6FF) else Color(0xFFFEF3C7)
                val badgeTextColor = if (currentStatus == "Processing") Color(0xFF1D4ED8) else Color(0xFFD97706)

                Card(
                    colors = CardDefaults.cardColors(containerColor = badgeBgColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = currentStatus,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF3F4F6))

            // Row 2: Transaction Type Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Transaction Type:", fontSize = 13.sp, color = Color(0xFF6B7280))
                Text(text = txn.txnType ?: "-", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1F2937))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 3: Details & Dynamic Order Number allocation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Transaction Details:", fontSize = 13.sp, color = Color(0xFF6B7280))
                Text(
                    text = "Cashback Added for Order\n#${txn.orderNumber ?: "N/A"}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Row 4: Credited Amount Text Highlight
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Credited:", fontSize = 13.sp, color = Color(0xFF6B7280))
                Text(text = txn.creditAmount ?: "0.00", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
            }
        }
    }
}