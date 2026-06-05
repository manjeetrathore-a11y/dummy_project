package com.manjeet.ubuyapplication.ui.newscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.R

enum class AppScreenState {
    HOME,
    ORDER_LIST,
    ORDER_DETAIL,
    TRACK_INPUT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTopAppBar(
    screenState: AppScreenState,
    currentRoute: String? = null,
    orderId: String? = null,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. LEFT SIDE: Back Arrow
        if (screenState != AppScreenState.HOME || currentRoute == "membership") {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 2. CENTER CONTENT
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = if (screenState == AppScreenState.HOME && currentRoute != "membership") Alignment.CenterStart else Alignment.Center
        ) {
            when {

                currentRoute == "membership" -> {
                    Text(text = "Ubuy Plus+ Membership", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                screenState == AppScreenState.HOME -> {
                    Image(
                        painter = painterResource(id = R.drawable.ubuy_logo),
                        contentDescription = "Ubuy Logo",
                        modifier = Modifier.height(60.dp)
                    )
                }
                screenState == AppScreenState.ORDER_LIST -> {
                    Text(text = "Orders", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                screenState == AppScreenState.ORDER_DETAIL -> {
                    Text(text = "Order #${orderId ?: ""}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                else -> {}
            }
        }

        // 3. RIGHT SIDE
        if (screenState != AppScreenState.HOME || currentRoute == "membership") {
            IconButton(onClick = { /* Handle Notification Click */ }) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notifications"
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "1. Home State Preview")
@Composable
fun HomeTopAppBarPreview() {
    DynamicTopAppBar(
        screenState = AppScreenState.HOME,
        onBackClick = {}
    )
}

@Preview(showBackground = true, name = "4. Membership Preview")
@Composable
fun MembershipTopAppBarPreview() {
    DynamicTopAppBar(
        screenState = AppScreenState.HOME,
        currentRoute = "membership",
        onBackClick = {}
    )
}

@Preview(showBackground = true, name = "2. Order List Preview")
@Composable
fun OrderListTopAppBarPreview() {
    DynamicTopAppBar(
        screenState = AppScreenState.ORDER_LIST,
        onBackClick = {}
    )
}

@Preview(showBackground = true, name = "3. Order Detail Preview")
@Composable
fun OrderDetailTopAppBarPreview() {
    DynamicTopAppBar(
        screenState = AppScreenState.ORDER_DETAIL,
        orderId = "987654321",
        onBackClick = {}
    )
}