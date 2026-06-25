package com.manjeet.ubuyapplication.ui.newscreen.UI




import android.text.Spanned
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDescriptionScreen(
    htmlDescription: String,
    onBackClick: () -> Unit
) {
    // 🌟 HTML Spanned core parsing code memorized dynamically to avoid layout lag
    val spannedDescription: Spanned = remember(htmlDescription) {
        val safeHtml = if (htmlDescription.isNotBlank()) htmlDescription else "No description available."
        try {
            HtmlCompat.fromHtml(safeHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } catch (e: Exception) {
            HtmlCompat.fromHtml("No description asset loaded", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                title = {
                    Text(
                        text = "Product Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF4F6F9)) // Smooth premium layout background color
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Detailed Overview",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // 🌟 USING NATIVE ANDROIDVIEW TO RENDER RICH HTML STRUCTURES SEAMLESSLY (BULLET POINTS, BOLD TEXT, ETC.)
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = { context ->
                            TextView(context).apply {
                                textSize = 14f // Text layout sizing
                                setTextColor(android.graphics.Color.parseColor("#374151")) // Charcoal gray tint mapping
                                // Handling custom line spacing matching the premium styling requirements
                                setLineSpacing(8f, 1.1f)
                            }
                        },
                        update = { textView ->
                            textView.text = spannedDescription
                        }
                    )
                }
            }
        }
    }
}