package com.manjeet.ubuyapplication.ui.screens

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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    orderId: String? = null,
    onBackClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    // Agar hum default Home standard se hatkar orders ya detail par hain, toh automatic design change hoga
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. LEFT SIDE: Back Arrow (Home par nahi dikhega, baki screens par dikhega)
        if (screenState != AppScreenState.HOME) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 2. CENTER CONTENT: Logo ya text badlega screenState ke hisab se
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = if (screenState == AppScreenState.HOME) Alignment.CenterStart else Alignment.Center
        ) {
            when (screenState) {
                AppScreenState.HOME -> {
                    Image(
                        painter = painterResource(id = R.drawable.ubuy_logo),
                        contentDescription = "Ubuy Logo",
                        modifier = Modifier.height(60.dp)
                    )
                }
                AppScreenState.ORDER_LIST -> {
                    Text(text = "Orders", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                AppScreenState.ORDER_DETAIL -> {
                    Text(text = "Order #${orderId ?: ""}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                else -> {}
            }
        }

        // 3. RIGHT SIDE: Home par Filter icon dikhega, baki par Notification icon dikhega
//        IconButton(onClick = if (screenState == AppScreenState.HOME) onFilterClick else { {} }) {
//            if (screenState == AppScreenState.HOME) {
//                Icon(Icons.Default.FilterList, contentDescription = "Filter")
//            } else {
//                Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications")
//            }


        // Actions block ke andar bas itna code rakhna hai:
        if (screenState != AppScreenState.HOME) {
            IconButton(onClick = { /* Handle Notification Click */ }) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notifications"
                )
            }
        }
        }
    }




