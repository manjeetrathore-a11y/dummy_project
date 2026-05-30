package com.manjeet.ubuyapplication.ui.newscreen.UI

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.model.UbuyMembershipData

@Composable
fun AccountTabScreen(
    ubuyMembershipData: UbuyMembershipData?,
    onTrackOrderClick: () -> Unit,
    onViewOrdersClick: () -> Unit,
    onMembershipClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    var showSavingsAndCashback by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F6))
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            //  USER PROFILE CARD
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(54.dp).background(Color(0xFFE5E7EB), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Manjeet Rathore", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                        Text(text = "manjeet.0001@ubuy.com", fontSize = 13.sp, color = Color(0xFF6B7280))
                    }
                }
            }

            // SECTION: ORDERS MANAGEMENT
            Text(text = "My Orders & Logistics", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    AccountActionRow(icon = "📦", title = "Track Live Shipment", onClick = onTrackOrderClick)

                    HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)

                    AccountActionRow(icon = "📋", title = "Order Purchase History", onClick = onViewOrdersClick)

                    HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)

                    AccountActionRow(
                        icon = "💰",
                        title = "Savings and cashback",
                        onClick = { showSavingsAndCashback = true }
                    )
                }
            }

            // SECTION: PREMIUM SERVICES
            Text(text = "Premium Subscriptions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    AccountActionRow(
                        icon = "👑",
                        title = "Ubuy Plus+ Membership",
                        isHighlight = true,
                        onClick = onMembershipClick
                    )
                }
            }
        }

        // =========================================================================
        //  DYNAMIC FULL SCREEN OVERLAY FOR SAVINGS AND CASHBACK
        // =========================================================================

        if (showSavingsAndCashback) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEFF1F5))
            ) {
                SavingsAndCashbackScreen(
                    ubuyMembershipData = ubuyMembershipData,
                    onBackClick = { showSavingsAndCashback = false }
                )
            }
        }
    }
}


@Composable
fun AccountActionRow(
    icon: String,
    title: String,
    isHighlight: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
                color = if (isHighlight) Color(0xFF145334) else Color(0xFF1F2937)
            )
        }
        Text(
            text = "→",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isHighlight) Color(0xFF145334) else Color(0xFF9CA3AF)
        )
    }
}