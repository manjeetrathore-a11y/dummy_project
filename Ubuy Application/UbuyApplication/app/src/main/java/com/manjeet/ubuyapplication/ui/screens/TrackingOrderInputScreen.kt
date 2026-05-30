package com.manjeet.ubuyapplication.ui.screens


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderInputScreen(
    orderId: String,
    onBack: () -> Unit
) {
    // State management for TextField
    var orderNumberInput by remember { mutableStateOf(orderId) }
    val focusManager = LocalFocusManager.current

    // Tag for Logcat tracing
    val logTag = "TrackOrderEngine"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)) // Light grey professional background matching image
            .padding(16.dp)
    ) {

        // 1. PAGE TITLE HEADER
        Text(
            text = "Track your order",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // 2. MAIN WHITE CARD CONTAINER
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 📦 3. FLYING PACKAGE DELIVERY ILLUSTRATION
                // Replace R.drawable.ic_delivery_box with your actual box-with-wings drawable ID
                Image(
                    painter = painterResource(id = android.R.drawable.ic_dialog_map), // Fallback image placeholder
                    contentDescription = "Delivery Tracking Icon",
                    modifier = Modifier
                        .size(90.dp)
                        .padding(bottom = 16.dp)
                )

                // 4. SUB-TEXT INSTRUCTIONS
                Text(
                    text = "Enter your order number to check your order status.",
                    fontSize = 15.sp,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 20.dp),
                    lineHeight = 20.sp
                )

                // 5. INPUT FIELD LABEL
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Order number ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "*",
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 6. TEXT FIELD INPUT
                TextField(
                    value = orderNumberInput,
                    onValueChange = { orderNumberInput = it },
                    placeholder = { Text("Enter order number", color = Color(0xFF9CA3AF)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            Log.d(logTag, "Search Action Triggered via Keyboard. Input: $orderNumberInput")
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF3F4F6),
                        unfocusedContainerColor = Color(0xFFF3F4F6),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xFF1F2937),
                        unfocusedTextColor = Color(0xFF1F2937)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 7. TRACK ORDER SUBMIT BUTTON
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        // 🪵 LOG ACTION STAMP
                        Log.d(logTag, "===============================================")
                        Log.d(logTag, "TRACK BUTTON CLICKED!")
                        Log.d(logTag, "Current input order sequence ID: '$orderNumberInput'")
                        Log.d(logTag, "===============================================")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF14532D) // Dark Green hex shade from screenshot image
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Track order",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


//  COMPOSE PREVIEW ENGINE (TOP-LEVEL ALONE)

@Preview(showBackground = true, showSystemUi = true, name = "Track Order Input View")
@Composable
fun TrackOrderInputScreenPreview() {
    TrackOrderInputScreen(
        orderId = "UBY987654321", // Pass dummy order ID for testing interface
        onBack = { /* No-Op in preview mode */ }
    )
}